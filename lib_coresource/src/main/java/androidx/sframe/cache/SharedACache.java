package androidx.sframe.cache;

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.Map;

import androidx.sframe.manager.SFrameManager;

/**
 * Author create by ok on 2019/2/13
 * Email : ok@163.com.
 */
public class SharedACache {

	private static class SharedACacheHelper {
		private static final SharedACache INSTANCE = new SharedACache("SingleInstance.Shared");
	}

	public static SharedACache getInstance() {
		return SharedACacheHelper.INSTANCE;
	}

	private final SharedPreferences mSharedPreferences;

	private SharedPreferences.Editor mEditor;

	public SharedACache() {
		this("SharedACache.Shared");
	}

	public SharedACache(String fileName) {
		mSharedPreferences = SFrameManager.getInstance().getContext().getSharedPreferences(fileName, 0);
	}

	public SharedACache put(String key, String value) {
		editor().putString(key, value);
		return this;
	}

	public SharedACache put(String key, long value) {
		editor().putLong(key, value);
		return this;
	}

	public SharedACache put(String key, int value) {
		editor().putInt(key, value);
		return this;
	}

	public SharedACache put(String key, float value) {
		editor().putFloat(key, value);
		return this;
	}

	public SharedACache put(String key, boolean value) {
		editor().putBoolean(key, value);
		return this;
	}

	public <T> SharedACache putObjectModel(T model) {
		return putObjectModel(model.getClass().getName(), model);
	}

	public <T> SharedACache putObjectModel(String key, T model) {
		put(key, new Gson().toJson(model));
		return this;
	}

	public String getString(String key) {
		return mSharedPreferences.getString(key, null);
	}

	public long getLong(String key) {
		return mSharedPreferences.getLong(key, 0l);
	}

	public int getInt(String key) {
		return mSharedPreferences.getInt(key, 0);
	}

	public float getFloat(String key) {
		return mSharedPreferences.getFloat(key, 0f);
	}

	public boolean getBoolean(String key) {
		return mSharedPreferences.getBoolean(key, false);
	}

	public <T> T getObjectModel(Class<T> tClass) {
		String value = getString(tClass.getName());
		if (!TextUtils.isEmpty(value)) {
			return new Gson().fromJson(value, tClass);
		}
		return null;
	}

	public Map<String, ?> getAll() {
		return mSharedPreferences.getAll();
	}

	public boolean contains(String key) {
		return mSharedPreferences.contains(key);
	}

	public SharedACache remove(String key) {
		editor().remove(key);
		return this;
	}

	public boolean commit() {
		boolean commot = editor().commit();
		mEditor = null;
		return commot;
	}

	public boolean clear() {
		editor().clear();
		return commit();
	}

	private SharedPreferences.Editor editor() {
		if (mEditor == null) {
			synchronized (this) {
				mEditor = mSharedPreferences.edit();
			}
		}
		return mEditor;
	}
}
