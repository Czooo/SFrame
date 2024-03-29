package androidx.sframe.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;

/**
 * Author create by ok on 2019/1/24
 * Email : ok@163.com.
 */
@SuppressLint("MissingPermission, HardwareIds")
public class DeviceUuidFactory {

	protected static final String PREFS_FILE = "device_id.xml";
	protected static final String PREFS_DEVICE_ID = "device_id";

	protected static UUID uuid;

	public DeviceUuidFactory(Context context) {

		if (uuid == null) {
			synchronized (DeviceUuidFactory.class) {
				if (uuid == null) {
					final SharedPreferences prefs = context
							.getSharedPreferences(PREFS_FILE, 0);
					final String id = prefs.getString(PREFS_DEVICE_ID, null);

					if (id != null) {
						// Use the ids previously computed and stored in the
						// prefs file
						uuid = UUID.fromString(id);

					} else {

						final String androidId = Settings.Secure
								.getString(context.getContentResolver(),
										Settings.Secure.ANDROID_ID);

						// Use the Android ID unless it's broken, in which case
						// fallback on deviceId,
						// unless it's not available, then fallback on a random
						// number which we store
						// to a prefs file
						try {
							if (!"9774d56d682e549c".equals(androidId)) {
								uuid = UUID.nameUUIDFromBytes(androidId
										.getBytes("utf8"));
							} else {
								final String deviceId = ((TelephonyManager) context
										.getSystemService(Context.TELEPHONY_SERVICE))
										.getDeviceId();
								uuid = deviceId != null ? UUID
										.nameUUIDFromBytes(deviceId
												.getBytes("utf8"))
										: generateDeviceUuid(context);
							}
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}

						// Write the value out to the prefs file
						prefs.edit()
								.putString(PREFS_DEVICE_ID, uuid.toString())
								.apply();

					}

				}
			}
		}

	}

	@SuppressLint("WifiManagerPotentialLeak")
	private UUID generateDeviceUuid(Context context) {

		// Get some of the hardware information
		String buildParams = Build.BOARD + Build.BRAND + Build.CPU_ABI
				+ Build.DEVICE + Build.DISPLAY + Build.FINGERPRINT + Build.HOST
				+ Build.ID + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT
				+ Build.TAGS + Build.TYPE + Build.USER;

		// Requires READ_PHONE_STATE
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		// gets the imei (GSM) or MEID/ESN (CDMA)
		String imei = tm.getDeviceId();

		// gets the android-assigned id
		String androidId = Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ANDROID_ID);

		// requires ACCESS_WIFI_STATE
		WifiManager wm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		// gets the MAC address
		String mac = wm.getConnectionInfo().getMacAddress();

		// if we've got nothing, return a random UUID
		if (isEmpty(imei) && isEmpty(androidId) && isEmpty(mac)) {
			return UUID.randomUUID();
		}

		// concatenate the string
		String fullHash = buildParams + imei + androidId + mac;

		return UUID.nameUUIDFromBytes(fullHash.getBytes());
	}

	/**
	 * Returns a unique UUID for the current android device. As with all UUIDs,
	 * this unique ID is "very highly likely" to be unique across all Android
	 * devices. Much more so than ANDROID_ID is.
	 * <p>
	 * The UUID is generated by using ANDROID_ID as the base key if appropriate,
	 * falling back on TelephonyManager.getDeviceID() if ANDROID_ID is known to
	 * be incorrect, and finally falling back on a random UUID that's persisted
	 * to SharedPreferences if getDeviceID() does not return a usable value.
	 * <p>
	 * In some rare circumstances, this ID may change. In particular, if the
	 * device is factory reset a new device ID may be generated. In addition, if
	 * a user upgrades their phone from certain buggy implementations of Android
	 * 2.2 to a newer, non-buggy version of Android, the device ID may change.
	 * Or, if a user uninstalls your app on a device that has neither a proper
	 * Android ID nor a Device ID, this ID may change on reinstallation.
	 * <p>
	 * Note that if the code falls back on using TelephonyManager.getDeviceId(),
	 * the resulting ID will NOT change after a factory reset. Something to be
	 * aware of.
	 * <p>
	 * Works around a bug in Android 2.2 for many devices when using ANDROID_ID
	 * directly.
	 *
	 * @return a UUID that may be used to uniquely identify your device for most
	 * purposes.
	 * @see https://code.google.com/p/android/issues/detail?id=10603
	 */
	public UUID getDeviceUuid() {
		return uuid;
	}

	private static boolean isEmpty(Object s) {
		if (s == null) {
			return true;
		}
		if ((s instanceof String) && (((String) s).trim().length() == 0)) {
			return true;
		}
		if (s instanceof Map) {
			return ((Map<?, ?>) s).isEmpty();
		}
		return false;
	}
}
