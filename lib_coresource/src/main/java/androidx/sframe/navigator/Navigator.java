package androidx.sframe.navigator;

import android.os.Bundle;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Author create by Zoran on 2019-10-04
 * @Email : 171905184@qq.com
 * @Description :
 */
public abstract class Navigator<T extends NavDestination> {

	@Target({TYPE})
	@Retention(RUNTIME)
	@SuppressWarnings("UnknownNullness")
	public @interface Name {
		String value();
	}

	@NonNull
	public abstract T obtain();

	@Nullable
	public abstract NavDestination navigate(@NonNull T navDestination, @Nullable Bundle args, @Nullable NavOptions navOptions);
}
