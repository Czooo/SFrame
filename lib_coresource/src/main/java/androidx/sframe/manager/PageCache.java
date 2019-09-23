package androidx.sframe.manager;

import java.util.ArrayDeque;

import androidx.annotation.NonNull;

/**
 * @Author create by Zoran on 2019-09-23
 * @Email : 171905184@qq.com
 * @Description :
 */
public abstract class PageCache<Page> {

	protected final ArrayDeque<Page> mPageStack = new ArrayDeque<>();

	public void put(@NonNull Page page) {
		this.mPageStack.add(page);
	}

	public void remove(@NonNull Page page) {
		this.mPageStack.remove(page);
	}

	public void clear() {
		this.mPageStack.clear();
	}

	public Page getFirst() {
		return this.mPageStack.getFirst();
	}

	public Page getLast() {
		return this.mPageStack.getLast();
	}

	public int size() {
		return this.mPageStack.size();
	}

	public boolean isEmpty() {
		return this.mPageStack.isEmpty();
	}

	public abstract void finishLast();
}
