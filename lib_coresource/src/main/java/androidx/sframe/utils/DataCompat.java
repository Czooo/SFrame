package androidx.sframe.utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;

/**
 * Author create by ok on 2019-06-15
 * Email : ok@163.com.
 */
public class DataCompat {

	public static ArrayList<String> getString(int page, int limit, String... def) {
		return getStringByPrefix(page, limit, "Data Position", def);
	}

	public static ArrayList<String> getStringByPrefix(int page, int limit, @NonNull String prefix, String... def) {
		final ArrayList<String> data = new ArrayList<>();
		for (int position = (page - 1) * limit; position < (page * limit); position++) {
			if (def.length > 0) {
				data.add(def[position % def.length]);
			} else {
				data.add(prefix + "(" + position + ")");
			}
		}
		return data;
	}

	public static <Data> ArrayList<Data> get(int page, int limit, Data... def) {
		final ArrayList<Data> data = new ArrayList<>();
		for (int position = (page - 1) * limit; position < (page * limit) & def.length > 0; position++) {
			data.add(def[position % def.length]);
		}
		return data;
	}
}
