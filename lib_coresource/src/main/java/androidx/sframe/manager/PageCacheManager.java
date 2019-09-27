package androidx.sframe.manager;

import android.os.Process;

import androidx.core.app.ActivityCompat;
import androidx.sframe.ui.abs.AbsActivity;
import androidx.sframe.ui.abs.AbsFragment;

/**
 * @Author create by Zoran on 2019-09-23
 * @Email : 171905184@qq.com
 * @Description :
 */
public class PageCacheManager {

	private static final class Helper {
		private static final PageCacheManager INSTANCE = new PageCacheManager();
	}

	public static PageCacheManager getInstance() {
		return PageCacheManager.Helper.INSTANCE;
	}

	private final ActivityPageCache mActivityPageCache;
	private final FragmentPageCache mFragmentPageCache;

	private PageCacheManager() {
		this.mActivityPageCache = new ActivityPageCache();
		this.mFragmentPageCache = new FragmentPageCache();
	}

	public ActivityPageCache getActivityPageCache() {
		return this.mActivityPageCache;
	}

	public FragmentPageCache getFragmentPageCache() {
		return this.mFragmentPageCache;
	}

	public synchronized void quit() {
		for (AbsActivity activity : this.mActivityPageCache.mPageStack) {
			if (!activity.isFinishing()) {
				ActivityCompat.finishAfterTransition(activity);
			}
		}
		Process.killProcess(Process.myPid());
		System.exit(0);
		System.gc();
	}

	public static final class ActivityPageCache extends PageCache<AbsActivity> {
		@Override
		public void finishLast() {
			AbsActivity activity = this.mPageStack.peekLast();
			ActivityCompat.finishAfterTransition(activity);
		}
	}

	public static final class FragmentPageCache extends PageCache<AbsFragment> {
		@Override
		public void finishLast() {
			AbsFragment fragment = this.mPageStack.peekLast();
			if (!fragment.getNavController().popBackStack()) {
				ActivityCompat.finishAfterTransition(fragment.requireActivity());
			}
		}
	}
}
