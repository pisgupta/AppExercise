package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;


/*
 * ************************************************************************************************************************************************
 *
 * This is a reusable Utility class for Share functions. The code has been done here and can be used multiple times in the project
 *
 * @author
 *
 *******************************************************************************************************************************************
 */

public final class ShareUtils {

    /**
     * Variables
     */

    private static ShareUtils instance;
    private String GOOGLE_DOCS_LINK = "https://drive.google.com/viewerng/viewer?embedded=true&url=";
    private Context mContext;

    private ShareUtils() {

    }

    public interface OnPageLoadedListener {
        public void onPageLoaded();

    }


    public static synchronized ShareUtils getInstance() {

        return instance == null ? instance = new ShareUtils()
                : instance;
    }

    public boolean checkSMSSendAvailablity(Context context) {
        mContext = context;
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method is used to open the Settings screen of the app to handle the runtime
     * permissions in case they are denied
     *
     * @param context
     */
    public void openAppSettingsScreen(final Context context) {
        if (context == null) {
            return;
        }
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + context.getApplicationContext().getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        context.startActivity(i);
    }

}
