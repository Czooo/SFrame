package androidx.sframe.ui.controller.impl;

import android.content.Context;
import android.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.Objects;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sframe.ui.controller.UIViewController;
import androidx.sframe.ui.controller.UIViewMethodController;

/**
 * Author create by ok on 2019/5/10
 * Email : ok@163.com.
 */
abstract class AbsUIViewMethod<PreView extends View> implements UIViewMethodController<PreView> {

	private final UIViewController mViewController;

	private PreView preView;

	protected AbsUIViewMethod(@NonNull UIViewController controller, @Nullable PreView preView) {
		this.mViewController = controller;
		this.preView = preView;
	}

	@NonNull
	@Override
	public final PreView getPreView() {
		return Objects.requireNonNull(this.preView, "Not find view");
	}

	@NonNull
	@Override
	public <V extends View> V set(@NonNull Class<V> viewClass) {
		try {
			if (viewClass.isInstance(this.getPreView())) {
				return (V) this.getPreView();
			}
			throw new ClassCastException(this.getPreView().getClass().getName() + " cannot be cast to " + viewClass.getName());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@NonNull
	@Override
	public final <V extends View> V findViewById(@IdRes int id) {
		return this.getViewController().findViewById(id);
	}

	@NonNull
	@Override
	public final <V extends View> V findChildViewById(@IdRes int id) {
		return this.getPreView().findViewById(id);
	}

	@NonNull
	@Override
	public final Context getContext() {
		return this.getViewController().getContext();
	}

	@NonNull
	@Override
	public final UIViewController getViewController() {
		return this.mViewController;
	}

	@NonNull
	@Override
	public final UIViewMethod<View> findAtParent() {
		return this.getViewController().findAtParent();
	}

	@NonNull
	@Override
	public final UIViewMethod<View> findAt(@IdRes int id) {
		return this.getViewController().findAt(id);
	}

	@NonNull
	@Override
	public final UIViewMethod<View> findAt(@NonNull View preView) {
		return this.getViewController().findAt(preView);
	}

	@NonNull
	@Override
	public final UIViewMethod<View> findChildAt(@IdRes int id) {
		return this.findChildAt(this.findChildViewById(id));
	}

	@NonNull
	@Override
	public final UIViewMethod<View> findChildAt(@NonNull View preView) {
		return this.getViewController().findAt(preView);
	}

	@NonNull
	@Override
	public <Method extends UIViewMethod<V>, V extends View> Method get(@NonNull Class<Method> methodClass) {
		try {
			// noinspection TryWithIdenticalCatches
			if (UIViewMethod.class.isAssignableFrom(methodClass)) {
				// 获取当前new的对象的泛型的父类类型
				ParameterizedType parameterizedType = (ParameterizedType) methodClass.getGenericSuperclass();

				if (parameterizedType != null) {
					// 获取第一个类型参数的真实类型
					Class<?> parameterType = (Class<?>) parameterizedType.getActualTypeArguments()[0];

					if (parameterType.isInstance(this.getPreView())) {
						V preView = (V) this.getPreView();
						// 获取当前new的对象的构造方法
						Constructor<Method> constructor = methodClass.getDeclaredConstructor(UIViewController.class, parameterType);
						// 设置当前new的对象的构造方法为共有权限
						constructor.setAccessible(true);
						// new对象，并传入参数
						return constructor.newInstance(this.getViewController(), preView);
					}
					throw new ClassCastException(this.getPreView().getClass().getName() + " cannot be cast to " + parameterType.getName());
				}
				throw new ClassCastException(methodClass.getName() + " is not a generic superclass");
			} else {
				return methodClass.newInstance();
			}
		} catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			throw new RuntimeException("Cannot create an instance of " + methodClass, e);
		}
	}

	@CallSuper
	@Override
	public UIViewMethodController<PreView> recycled() {
		this.preView = null;
		return this;
	}
}
