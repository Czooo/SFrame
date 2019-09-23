package androidx.sframe.ui.dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.R;
import androidx.sframe.ui.abs.AbsDialogFragment;
import androidx.sframe.ui.controller.AppPageController;

/**
 * Author create by ok on 2019-06-20
 * Email : ok@163.com.
 */
public class ProgressDialogFragment extends AbsDialogFragment implements AppPageController.ContentViewInterface {

	public ProgressDialogFragment(@NonNull AppPageController<?> supperPageController) {
		super(supperPageController);
	}

	@Override
	public int onPageLayoutId(@Nullable Bundle savedInstanceState) {
		return R.layout.dialog_fragment_progress_layout;
	}

	@Override
	public void onPageViewCreated(@Nullable Bundle savedInstanceState) {

	}
}
