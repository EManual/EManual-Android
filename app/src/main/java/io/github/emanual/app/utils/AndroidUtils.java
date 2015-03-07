package io.github.emanual.app.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * 安卓设备工具类
 * 
 * @author Jayin Ton 需要的权限：<uses-permission
 *         android:name="android.permission.READ_PHONE_STATE"/>
 */
public class AndroidUtils {
	/**
	 * 获得屏幕的高宽(像数)
	 * 
	 * @param context
	 * @return size[0]为宽 size[1]为长
	 */
	public static int[] getScreenSize(Context context) {
		int[] size = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		manager.getDefaultDisplay().getMetrics(dm);
		size[0] = dm.widthPixels;
		size[1] = dm.heightPixels;
		return size;
	}

	/**
	 * 判断用户是否在wifi环境下
	 * 
	 * @param context
	 * @return true if it's in the environment of wifi
	 */
	public static boolean isInWifi(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connManager.getActiveNetworkInfo();
		if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 检测网络是否已经连接
	 * 
	 * @param context
	 * @return true if the network is connect
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return info != null && info.isConnected();

	}

	/**
	 * 获得手机ip地址
	 * 
	 * @return null if faild to get ip
	 */
	public static String getPhoneIP() {
		try {
			String ipv4;
			ArrayList<NetworkInterface> mylist = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface ni : mylist) {
				ArrayList<InetAddress> ialist = Collections.list(ni
						.getInetAddresses());
				for (InetAddress address : ialist) {
					if (!address.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(ipv4 = address
									.getHostAddress())) {
						return ipv4;
					}
				}

			}
		} catch (SocketException ex) {
             return null;
		}
		return null;
	}

	/**
	 * 获得本机Mac
	 * 
	 * @param context
	 * @return
	 */
	public static String getMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}
	/**
	 * 截图，不需要权限；只能截某一Activity的图
	 * @param acty 
	 * @return 改Activity 的bitmap
	 */
	public static Bitmap screenShot(Activity acty){
		 // View是你需要截图的View     
	    View view = acty.getWindow().getDecorView();     
	    view.setDrawingCacheEnabled(true);     
	    Bitmap fullScreen = view.getDrawingCache();
	    // 获取状态栏高度     
	    Rect frame = new Rect();    
	    acty.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);     
	    int statusBarHeight = frame.top;     
	    System.out.println(statusBarHeight);     
	    // 获取屏幕长和高     
	    int size[] = getScreenSize(acty);
	    int width = size[0];     
	    int height = size[1];     
	    // 去掉标题栏       
	    Bitmap actyScreen = Bitmap.createBitmap(fullScreen, 0, statusBarHeight, width, height     
	        - statusBarHeight);     
	    view.destroyDrawingCache();     
	    return actyScreen;     
	}
	/**
	 * 获得版本名称
	 * @param context
	 * @return
	 * @throws NameNotFoundException
	 */
	public static String getAppVersionName(Context context) throws NameNotFoundException{
		PackageManager packageManager = context.getPackageManager();
		PackageInfo info = packageManager.getPackageInfo(context.getPackageName(),0);
		return info.versionName;
	}
    /**
     * 获得版本号
     * @param context
     * @return
     * @throws NameNotFoundException
     */
	public static int getAppVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo info;
		try {
			info = packageManager.getPackageInfo(context.getPackageName(),0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 1;
		}
		return info.versionCode;
	}
	

}