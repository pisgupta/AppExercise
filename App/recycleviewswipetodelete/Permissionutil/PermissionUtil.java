package shake.device.com.permissiontest;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * Created by pankaj.kumar on 3/22/2017.
 */

public class PermissionUtil {
    private static final String TAG = "PermissionUtil";
    private static PermissionUtil.PermissionStatus permissionStatus;
    private static Context mContext;
    private static int requestCode;
    private static PermissionUtil permissionUtil;


    interface PermissionStatus {
        public void permissionStatus(int requestCode);
    }


    private PermissionUtil() {

    }

    public static PermissionUtil getInstance(Context Context, PermissionStatus mpermissionStatus) {
        mContext = Context;
        permissionStatus = mpermissionStatus;
        return permissionUtil == null ? new PermissionUtil() : permissionUtil;
    }

    public void checkPermission(Context context, String permision, int requestCode) {
        Log.d(TAG, "checkPermission: ");
        if (ContextCompat.checkSelfPermission(context, permision) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permision)) {
                Log.d(TAG, "checkPermission: if");
                ActivityCompat.requestPermissions((Activity) context, new String[]{permision}, requestCode);

            } else {
                Log.d(TAG, "checkPermission: else");
                ActivityCompat.requestPermissions((Activity) context, new String[]{permision}, requestCode);
            }
        }
    }
}
