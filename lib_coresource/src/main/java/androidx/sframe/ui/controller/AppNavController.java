package androidx.sframe.ui.controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.sframe.navigator.NavDestination;
import androidx.sframe.navigator.NavGraph;
import androidx.sframe.navigator.NavOptions;

/**
 * @Author create by Zoran on 2019-09-25
 * @Email : 171905184@qq.com
 * @Description :
 */
public interface AppNavController<Page> {

	AppNavController<Page> finishActivity();

	NavDestination startActivity(@SuppressLint("UnknownNullness") Intent intent);

	NavDestination startActivity(@SuppressLint("UnknownNullness") Intent intent, @Nullable Bundle options);

	NavDestination startActivityForResult(@SuppressLint("UnknownNullness") Intent intent, int requestCode);

	NavDestination startActivityForResult(@SuppressLint("UnknownNullness") Intent intent, int requestCode, @Nullable Bundle options);

	NavDestination startActivity(@NonNull Class<? extends FragmentActivity> activity);

	NavDestination startActivity(@NonNull Class<? extends FragmentActivity> activity, @Nullable Bundle args);

	NavDestination startActivity(@NonNull Class<? extends FragmentActivity> activity, @Nullable Bundle args, @Nullable Bundle options);

	NavDestination startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode);

	NavDestination startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode, @Nullable Bundle args);

	NavDestination startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode, @Nullable Bundle args, @Nullable Bundle options);

	NavDestination showFragment(@NonNull Class<? extends DialogFragment> dialogFragment);

	NavDestination showFragment(@NonNull Class<? extends DialogFragment> dialogFragment, @Nullable Bundle args);

	NavDestination pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment);

	NavDestination pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable Bundle args);

	NavDestination pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable NavOptions navOptions);

	NavDestination pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable Bundle args, @Nullable NavOptions navOptions);

	NavDestination startFragment(@NonNull Class<? extends Fragment> fragment);

	NavDestination startFragment(@NonNull Class<? extends Fragment> fragment, @Nullable Bundle args);

	NavDestination startFragment(@NonNull Class<? extends Fragment> fragment, @Nullable Bundle args, @Nullable Bundle options);

	NavDestination startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode);

	NavDestination startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode, @Nullable Bundle args);

	NavDestination startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode, @Nullable Bundle args, @Nullable Bundle options);

	NavDestination navigate(@NonNull NavDestination navDestination);

	NavDestination navigate(@NonNull NavDestination navDestination, @Nullable Bundle args);

	NavDestination navigate(@NonNull NavDestination navDestination, @Nullable Bundle args, @Nullable NavOptions navOptions);

	NavOptions getNavOptions();

	NavGraph getNavGraph();

	boolean navigateUp();

	boolean popBackStack();
}
