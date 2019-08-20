package com.rider.myride.permisions;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;


public class Permisions {
    // Camera group.
    public static final String CAMERA = Manifest.permission.CAMERA;
    // Location group.
    public static final String ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    //storage
    public static final String ACCES_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String MNC = "MNC";

    /**
     * Create an array from a given permissions.
     * <p>
     * // @throws IllegalArgumentException
     */
    public static String[] asArray(@NonNull String... permissions) {
        if (permissions.length == 0) {
            throw new IllegalArgumentException("There is no given permission");
        }

        final String[] dest = new String[permissions.length];
        for (int i = 0, len = permissions.length; i < len; i++) {
            dest[i] = permissions[i];
        }
        return dest;
    }

    /**
     * Check that given permission have been granted.
     */
    public static boolean hasGranted(int grantResult) {
        return grantResult == PERMISSION_GRANTED;
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link /*PackageManager#PERMISSION_GRANTE
     */

    public static boolean hasGranted(int[] grantResults) {
        for (int result : grantResults) {
            if (!hasGranted(result)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the Context has access to a given permission.
     * Always returns true on platforms below M.
     */
    public static boolean hasSelfPermission(Context context, String permission) {
        if (isMNC()) {
            return permissionHasGranted(context, permission);
        }
        return true;
    }

    /**
     * Returns true if the Context has access to all given permissions.
     * Always returns true on platforms below M.
     */
    public static boolean hasSelfPermissions(Context context, String[] permissions) {
        if (!isMNC()) {
            return true;
        }

        for (String permission : permissions) {
            if (!permissionHasGranted(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Requests permissions to be granted to this application.
     */
    public static void requestAllPermissions(@NonNull Activity activity, @NonNull String[] permissions, int requestCode) {
        if (isMNC()) {
            internalRequestPermissions(activity, permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void internalRequestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (activity == null) {
            throw new IllegalArgumentException("Given activity is null.");
        }
        activity.requestPermissions(permissions, requestCode);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static boolean permissionHasGranted(Context context, String permission) {
        return hasGranted(context.checkSelfPermission(permission));
    }

    private static boolean isMNC() {
        return MNC.equals(Build.VERSION.CODENAME);
    }


}
