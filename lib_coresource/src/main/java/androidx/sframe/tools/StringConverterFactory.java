package androidx.sframe.tools;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Author create by ok on 2017/10/18
 * Email : ok@163.com.
 */

public class StringConverterFactory extends Converter.Factory {

	private static final StringConverterFactory INSTANCE = new StringConverterFactory();

	public static StringConverterFactory create() {
		return INSTANCE;
	}

	@Override
	public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
		if (type == String.class) {
			return StringConverter.INSTANCE;
		}
		//其它类型我们不处理，返回null就行
		return super.responseBodyConverter(type, annotations, retrofit);
	}

	private static class StringConverter implements Converter<ResponseBody, String> {

		static final StringConverter INSTANCE = new StringConverter();

		@Override
		public String convert(@NonNull ResponseBody value) throws IOException {
			return value.string();
		}
	}
}

