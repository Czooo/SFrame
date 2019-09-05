package androidx.sframe.ui.controller.impl;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public class AppPageFragmentControllerImpl2 extends AppPageFragmentControllerImpl<Fragment> {

	public AppPageFragmentControllerImpl2(@NonNull PageProvider pageProvider) {
		super(pageProvider);
	}

	@NonNull
	@Override
	public final Fragment getPageOwner() {
		return (Fragment) this.getPageProvider();
	}
}
