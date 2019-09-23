package androidx.sframe.helper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.controller.UILayoutController;
import androidx.sframe.ui.controller.impl.UIToolbarMethod;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

/**
 * Author create by ok on 2019-06-17
 * Email : ok@163.com.
 */
public class ViewModelProviders extends androidx.lifecycle.ViewModelProviders {

	@NonNull
	@MainThread
	public static ViewModelProvider of(@NonNull UILayoutController layoutController) {
		return new ViewModelProvider(layoutController, new UIToolbarMethodFactory(layoutController));
	}

	@NonNull
	@MainThread
	public static ViewModelProvider of(@NonNull ViewModelStoreOwner owner) {
		return of(owner, new ViewModelProvider.NewInstanceFactory());
	}

	@NonNull
	@MainThread
	public static ViewModelProvider of(@NonNull ViewModelStoreOwner owner, @Nullable ViewModelProvider.Factory factory) {
		if (factory == null) {
			factory = new ViewModelProvider.NewInstanceFactory();
		}
		return new ViewModelProvider(owner.getViewModelStore(), factory);
	}

	public static class UIToolbarMethodFactory extends ViewModelProvider.NewInstanceFactory {

		private final UILayoutController mLayoutController;

		private UIToolbarMethodFactory(@NonNull UILayoutController layoutController) {
			this.mLayoutController = layoutController;
		}

		@NonNull
		@Override
		public <Model extends ViewModel> Model create(@NonNull Class<Model> modelClass) {
			if (UIToolbarMethod.class.isAssignableFrom(modelClass)) {
				//noinspection TryWithIdenticalCatches
				try {
					Constructor<Model> constructor = modelClass.getConstructor(UILayoutController.class);
					constructor.setAccessible(true);
					return constructor.newInstance(this.mLayoutController);
				} catch (NoSuchMethodException e) {
					throw new RuntimeException("Cannot create an instance of " + modelClass, e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Cannot create an instance of " + modelClass, e);
				} catch (InstantiationException e) {
					throw new RuntimeException("Cannot create an instance of " + modelClass, e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException("Cannot create an instance of " + modelClass, e);
				}
			}
			return super.create(modelClass);
		}
	}
}
