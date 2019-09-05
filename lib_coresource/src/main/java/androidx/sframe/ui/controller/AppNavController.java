package androidx.sframe.ui.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

/**
 * Author create by ok on 2019-06-18
 * Email : ok@163.com.
 */
public interface AppNavController<Page> {

	AppPageController<Page> getPageController();

	AppNavController<Page> showProgressPage();

	AppNavController<Page> dismissProgressPage();

	AppNavController<Page> showPage(@NonNull Class<? extends DialogFragment> pageClass);

	AppNavController<Page> showPage(@NonNull Class<? extends DialogFragment> pageClass, @Nullable Bundle args);

	AppNavController<Page> pushPage(@NonNull Class<? extends Fragment> pageClass);

	AppNavController<Page> pushPage(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args);

	AppNavController<Page> pushPage(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args, @Nullable NavOptions navOptions);

	AppNavController<Page> pushPage(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable FragmentNavigator.Extras navigatorExtras);

	AppNavController<Page> startPage(@NonNull Class<? extends Fragment> pageClass);

	AppNavController<Page> startPage(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args);

	AppNavController<Page> startPage(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args, @Nullable Bundle options);

	AppNavController<Page> startPageForResult(@NonNull Class<? extends Fragment> pageClass, int requestCode);

	AppNavController<Page> startPageForResult(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args, int requestCode);

	AppNavController<Page> startPageForResult(@NonNull Class<? extends Fragment> pageClass, @Nullable Bundle args, @Nullable Bundle options, int requestCode);

	AppNavController<Page> startActivity(@NonNull Intent intent);

	AppNavController<Page> startActivity(@NonNull Intent intent, @Nullable Bundle options);

	AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> pageClass);

	AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> pageClass, @Nullable Bundle args);

	AppNavController<Page> startActivity(@NonNull Class<? extends FragmentActivity> pageClass, @Nullable Bundle args, @Nullable Bundle options);

	AppNavController<Page> startActivityForResult(@NonNull Intent intent, int requestCode);

	AppNavController<Page> startActivityForResult(@NonNull Intent intent, @Nullable Bundle options, int requestCode);

	AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> pageClass, int requestCode);

	AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> pageClass, @Nullable Bundle args, int requestCode);

	AppNavController<Page> startActivityForResult(@NonNull Class<? extends FragmentActivity> pageClass, @Nullable Bundle args, @Nullable Bundle options, int requestCode);

	AppNavController<Page> navigate(@IdRes int resId);

	AppNavController<Page> navigate(@IdRes int resId, @Nullable Bundle args);

	AppNavController<Page> navigate(@IdRes int resId, @Nullable Bundle args, @Nullable NavOptions navOptions);

	AppNavController<Page> navigate(@IdRes int resId, @Nullable Bundle args, @Nullable NavOptions navOptions, @Nullable Navigator.Extras navigatorExtras);

	AppNavController<Page> addGraph(@NavigationRes int navResId);

	AppNavController<Page> addGraph(@NavigationRes int navResId, @Nullable Bundle args);

	AppNavController<Page> setGraph(@NavigationRes int navResId);

	AppNavController<Page> setGraph(@NavigationRes int navResId, @Nullable Bundle args);

	boolean navigateUp();

	boolean popBackStack();

	boolean popBackStack(@IdRes int destinationId, boolean inclusive);
}
