package androidx.sframe.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import androidx.annotation.NonNull;

/**
 * Author create by ok on 2018/6/6 0006
 * Email : ok@163.com.
 */
public class NetCompat {

	/**
	 * 打开网络设置界面
	 */
	public static void openWirelessSettings(@NonNull Context context) {
		Intent mIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(mIntent);
	}

	/**
	 * 获取活动网络信息
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
	 */
	@SuppressLint("MissingPermission")
	private static NetworkInfo getActiveNetworkInfo(@NonNull Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectivityManager != null) {
			return mConnectivityManager.getActiveNetworkInfo();
		}
		return null;
	}

	/**
	 * 判断网络是否连接
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
	 */
	public static boolean isConnected() {
		return isConnected(SFrameManager.getInstance().getContext());
	}

	public static boolean isConnected(@NonNull Context context) {
		NetworkInfo info = getActiveNetworkInfo(context);
		return info != null && info.isConnected();
	}

	/**
	 * 判断移动数据是否打开
	 */
	@SuppressLint("PrivateApi")
	public static boolean hasDataEnabled(@NonNull Context context) {
		try {
			TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (mTelephonyManager != null) {
				Method getMobileDataEnabledMethod = mTelephonyManager.getClass().getDeclaredMethod("getDataEnabled");
				if (null != getMobileDataEnabledMethod) {
					return (boolean) getMobileDataEnabledMethod.invoke(mTelephonyManager);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 打开或关闭移动数据
	 * <p>需系统应用 需添加权限{@code <uses-permission android:name="android.permission.MODIFY_PHONE_STATE"/>}</p>
	 */
	public static void setDataEnabled(@NonNull Context context, boolean enabled) {
		try {
			TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			if (mTelephonyManager != null) {
				Method setMobileDataEnabledMethod = mTelephonyManager.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
				if (null != setMobileDataEnabledMethod) {
					setMobileDataEnabledMethod.invoke(mTelephonyManager, enabled);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断网络是否是4G
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
	 */
	public static boolean is4G(@NonNull Context context) {
		NetworkInfo info = getActiveNetworkInfo(context);
		return info != null && info.isAvailable() && info.getSubtype() == TelephonyManager.NETWORK_TYPE_LTE;
	}

	/**
	 * 判断wifi是否打开
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
	 */
	public static boolean hasWifiEnabled(@NonNull Context context) {
		WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		return mWifiManager != null && mWifiManager.isWifiEnabled();
	}

	/**
	 * 打开或关闭wifi
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.CHANGE_WIFI_STATE"/>}</p>
	 */
	@SuppressLint("MissingPermission")
	public static void setWifiEnabled(@NonNull Context context, boolean enabled) {
		WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if (mWifiManager != null) {
			if (enabled) {
				if (!mWifiManager.isWifiEnabled()) {
					mWifiManager.setWifiEnabled(true);
				}
			} else {
				if (mWifiManager.isWifiEnabled()) {
					mWifiManager.setWifiEnabled(false);
				}
			}
		}
	}

	/**
	 * 判断wifi是否连接状态
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>}</p>
	 */
	@SuppressLint("MissingPermission")
	public static boolean isWifiConnected(@NonNull Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return mConnectivityManager != null && mConnectivityManager.getActiveNetworkInfo() != null
				&& mConnectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
	}

	/**
	 * 判断wifi数据是否可用
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>}</p>
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
	 */
	public static boolean isWifiAvailable(@NonNull Context context) {
		return hasWifiEnabled(context) && isWifiConnected(context);
	}

	/**
	 * 获取网络运营商名称
	 * <p>中国移动、如中国联通、中国电信</p>
	 *
	 * @return 运营商名称
	 */
	public static String getNetworkOperatorName(@NonNull Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyManager != null ? mTelephonyManager.getNetworkOperatorName() : null;
	}

	/**
	 * 获取IP地址
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
	 *
	 * @param useIPv4 是否用IPv4
	 * @return IP地址
	 */
	public static String getIPAddress(boolean useIPv4) {
		try {
			for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
				NetworkInterface ni = nis.nextElement();
				// 防止小米手机返回10.0.2.15
				if (!ni.isUp()) continue;
				for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
					InetAddress inetAddress = addresses.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						String hostAddress = inetAddress.getHostAddress();
						boolean isIPv4 = hostAddress.indexOf(':') < 0;
						if (useIPv4) {
							if (isIPv4) return hostAddress;
						} else {
							if (!isIPv4) {
								int index = hostAddress.indexOf('%');
								return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
							}
						}
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取域名ip地址
	 * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
	 *
	 * @param domain 域名
	 * @return ip地址
	 */
	public static String getDomainAddress(final String domain) {
		try {
			ExecutorService exec = Executors.newCachedThreadPool();
			Future<String> fs = exec.submit(new Callable<String>() {
				@Override
				public String call() throws Exception {
					InetAddress inetAddress;
					try {
						inetAddress = InetAddress.getByName(domain);
						return inetAddress.getHostAddress();
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
					return null;
				}
			});
			return fs.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}
}
