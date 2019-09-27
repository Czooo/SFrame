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
import androidx.sframe.navigator.Navigator;

/**
 * @Author create by Zoran on 2019-09-25
 * @Email : 171905184@qq.com
 * @Description :
 */
public interface AppNavController<Page> {

	AppNavController<Page> onSaveInstanceState(@NonNull Bundle saveInstanceState);

	AppNavController<Page> onRestoreInstanceState(@Nullable Bundle saveInstanceState);

	AppNavController<Page> showProgressFragment();

	AppNavController<Page> hideProgressFragment();

	AppNavController<Page> startActivity(@SuppressLint("UnknownNullness") Intent intent);

	AppNavController<Page> startActivity(@SuppressLint("UnknownNullness") Intent intent, @Nullable Bundle options);

	AppNavController<Page> startActivityForResult(@SuppressLint("UnknownNullness") Intent intent, int requestCode);

	AppNavController<Page> startActivityForResult(@SuppressLint("UnknownNullness") Intent intent, int requestCode, @Nullable Bundle options);

	AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> activity);

	AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> activity, @Nullable Bundle args);

	AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> activity, @Nullable Bundle args, @Nullable Bundle options);

	AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode);

	AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode, @Nullable Bundle args);

	AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> activity, int requestCode, @Nullable Bundle args, @Nullable Bundle options);

	AppNavController<Page> showFragment(@NonNull Class<? extends DialogFragment> dialogFragment);

	AppNavController<Page> showFragment(@NonNull Class<? extends DialogFragment> dialogFragment, @Nullable Bundle args);

	AppNavController<Page> pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment);

	AppNavController<Page> pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable Bundle args);

	AppNavController<Page> pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable Navigator.NavOptions options);

	AppNavController<Page> pushFragment(@IdRes int containerId, @NonNull Class<? extends Fragment> fragment, @Nullable Bundle args, @Nullable Navigator.NavOptions options);

	AppNavController<Page> startFragment(@NonNull Class<? extends Fragment> fragment);

	AppNavController<Page> startFragment(@NonNull Class<? extends Fragment> fragment, @Nullable Bundle args);

	AppNavController<Page> startFragment(@NonNull Class<? extends Fragment> fragment, @Nullable Bundle args, @Nullable Bundle options);

	AppNavController<Page> startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode);

	AppNavController<Page> startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode, @Nullable Bundle args);

	AppNavController<Page> startFragmentForResult(@NonNull Class<? extends Fragment> fragment, int requestCode, @Nullable Bundle args, @Nullable Bundle options);

	AppNavController<Page> addNavigator(@NonNull String name, @NonNull Navigator<? extends Navigator.NavDestination> navigator);

	<T extends Navigator<?>> T getNavigator(@NonNull String name);

	Navigator.NavOptions getDefaultOptions();

	boolean popBackStack();
}
