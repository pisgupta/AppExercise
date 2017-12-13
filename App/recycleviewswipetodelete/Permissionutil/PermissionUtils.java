package com.example.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.Serializable;

/**
 * Created by pankaj.kumar on 2/7/2017.
 */

public class PermissionUtils {
    private static final String TAG = "PermissionUtils";
    /**
     * class
     */
    private static PermissionUtils instance = new PermissionUtils();
    private PermissionHandlerListener mListener;

    /**
     * Variable
     */
    private String mPermission;
    protected String mMessage;
    private int REQUEST_CODE;
    private static Context mContext;
    private static boolean firstTime;

    /**
     * Constructor
     */

    public PermissionUtils() {
    }

    /**
     * Make class singleton to get class object
     */

    public static synchronized PermissionUtils getInstance(Context context) {
        mContext = context;
        firstTime = true;
        return instance == null ? instance = new PermissionUtils() : instance;
    }

    public interface PermissionHandlerListener extends Serializable {
        void onPermissionGranted(int resuestCode, String permission);
    }

    /**
     * Method to set listener and handle dynamic permission
     *
     * @param message
     * @param permission
     * @param requestcode
     * @param mListener
     */
    public void setListener(String message, String permission, int requestcode, PermissionHandlerListener mListener) {
        //Setting the value on instance variable
        Log.e(TAG, "setListener: ");
        mPermission = permission;
        this.mListener = mListener;
        REQUEST_CODE = requestcode;
        mMessage = message;
        //Checking the current api version of device
        int currentVersion = Build.VERSION.SDK_INT;
        if (currentVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext, mPermission) != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, mPermission)) {
                    // The rationale always comes as false for the first time so handling that condition
                    if (firstTime) {
                        ActivityCompat.requestPermissions((Activity) mContext, new String[]{mPermission}, REQUEST_CODE);
                    } else {
                        // If the show rationale comes as false, show an alert to the user that the permissions are necessary
                        firstTime = false;
                        showPermissionDeniedAlertDialog(mContext, message);
                    }
                } else {
                    // Else if the show rationale is true, just show the request permission dialog to the user
                    firstTime = false;
                    ActivityCompat.requestPermissions((Activity) mContext, new String[]{mPermission}, requestcode);
                }
            } else {
                // If the permission has already been granted just call the success listener
                firstTime = false;
                mListener.onPermissionGranted(REQUEST_CODE, mPermission);
            }
        }

    }

    /**
     * Method called as a callback when a permission is granted or denied
     *
     * @param requestcode
     * @param permision
     * @param grantResult
     */
    public void onRequestPermissionsResult(int requestcode, String[] permision, int[] grantResult) {
        Log.e(TAG, "onRequestPermissionsResult: ");
        if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_GRANTED) {
            mListener.onPermissionGranted(REQUEST_CODE, mPermission);
        } else {
            showPermissionDeniedAlertDialog(mContext, mMessage);
        }
    }


    /**
     * Alert user with the importance of allowing permission.
     *
     * @param context
     */
    private void showPermissionDeniedAlertDialog(final Context context, String message) {
        Log.e(TAG, "showPermissionDeniedAlertDialog: ");
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(message + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // If the rationale can be shown directly open the request permission dialog
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, mPermission)) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{mPermission}, REQUEST_CODE);
                }
                // else open the settings screen of the app
                else {
                    ShareUtils mShareUtils = ShareUtils.getInstance();
                    mShareUtils.openAppSettingsScreen(context);
                }
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

}
