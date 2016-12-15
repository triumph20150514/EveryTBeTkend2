package com.trimph.flescroll;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * 权限工具
 * @author hzw
 */
public class PermissionUtils {
	public static final int RESULT_REQUEST_PREMISSION_CODE = 1;

	/**
	 * 判断权限是否授权
	 * 
	 * @param context
	 * @param permission
	 *            权限
	 * @return 未授权返回 true
	 */
	public static boolean isLackPermission(Context context, String permission) {
		if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
			return true;
		}
		return false;
	}

	/**
	 * 判断几个权限中是否有没有授权的
	 * 
	 * @param context
	 * @param permissions
	 *            权限数组
	 * @return 未授权返回 true
	 */
	public static boolean isLackPermissions(Context context, String... permissions) {
		for (String permission : permissions) {
			if (isLackPermission(context, permission)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 用户是否关闭不在提示所有权限提升
	 * 
	 * @param activity
	 * @param permissions
	 * @return 不再提示所有权限，返回true
	 */
	public static boolean hasDelayAllPermissions(Activity activity, String... permissions) {
		int count = 0;
		for (String permission : permissions) {
			if (isLackPermission(activity, permission)
					&& !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
				count++;
			}
		}
		if (count == permissions.length) {
			return true;
		}
		return false;
	}

	/**
	 * 申请权限
	 * 
	 * @param activity
	 * @param permissions
	 */
	public static void requestPermissions(Activity activity, String... permissions) {
		ActivityCompat.requestPermissions(activity, permissions, RESULT_REQUEST_PREMISSION_CODE);
	}
}
