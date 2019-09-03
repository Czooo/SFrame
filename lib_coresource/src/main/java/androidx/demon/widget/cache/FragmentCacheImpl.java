package androidx.demon.widget.cache;

import java.util.concurrent.atomic.AtomicInteger;

import androidx.demon.widget.adapter.FragmentPager;
import androidx.fragment.app.Fragment;

/**
 * Author create by ok on 2019/3/14
 * Email : ok@163.com.
 */
public abstract class FragmentCacheImpl implements IObjectCache<Fragment, FragmentPager> {

	private static final int DEFAULT_MAX_POOL_SIZE = 10;

	private final AtomicInteger mPoolSize = new AtomicInteger();

	private Node mNode;
	private int curPoolSize;

	public FragmentCacheImpl() {
		this(DEFAULT_MAX_POOL_SIZE);
	}

	public FragmentCacheImpl(int maxPoolSize) {
		this.mNode = new Node();
		this.mPoolSize.set(maxPoolSize);
	}

	public int getPoolSize() {
		return mPoolSize.get();
	}

	@Override
	public void prepare(FragmentPager pager, int position) {
		synchronized (this) {
			final int poolSize = getPoolSize();
			Node node = this.mNode;
			int curPoolSize = this.curPoolSize;

			while (curPoolSize < poolSize) {
				if (node.fragment == null) {
					node.fragment = create(pager, position);
				} else {
					Node n1 = new Node();
					n1.next = node;
					n1.fragment = create(pager, position);
					node = n1; //new node is the front
				}
				curPoolSize++;
			}
			this.mNode = node;
			this.curPoolSize = curPoolSize;
		}
	}

	@Override
	public Fragment obtain(FragmentPager pager, int position) {
		synchronized (this) {
			if (mNode.fragment != null) {
				Node node = this.mNode;
				Fragment fragment = node.fragment;
				mNode = node.next;
				//may null
				if (mNode == null) {
					mNode = new Node();
				}
				node.next = null;
				curPoolSize--;
				return fragment;
			}
		}
		return create(pager, position);
	}

	@Override
	public void recycle(Fragment fragment) {
		synchronized (this) {
			final int poolSize = getPoolSize();
			if (curPoolSize < poolSize) {
				Node node = new Node();
				node.next = mNode;
				node.fragment = fragment;
				this.mNode = node;
				curPoolSize++;
			}
		}
	}

	@Override
	public void clear() {
		synchronized (this) {
			Node node = this.mNode;
			while (node != null) {
				node.fragment = null;
				node = node.next;
			}
			this.mNode = new Node();
			curPoolSize = 0;
		}
	}

	@SuppressWarnings("hiding")
	public class Node {

		Fragment fragment;

		Node next;
	}
}
