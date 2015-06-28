package demoapp.app.com.appexercise;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import com.demo.appmodel.AppModel;
import com.demo.appparsing.GetImageURL;
import com.demo.appparsing.XmlParsing;
import com.demo.apputil.DownLoadStatusReceiver;
import com.demo.constant.AppConstant;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by gupta on 6/27/2015.
 */
public class ImageIntentService extends IntentService {

    private ArrayList<AppModel> pathlist;
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    public static final String TAG = "ImageIntentService";


    public ImageIntentService() {
        super(ImageIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);
        pathlist = new XmlParsing().parseData(GetImageURL.getImageUrl(AppConstant.URL));
        for (int i = 0; i < pathlist.size(); i++) {
            Bundle bundle = new Bundle();
            try {
                byte[] buff = downloadData(pathlist.get(i).getIconpath());
                if (buff.length > 0) {
                    bundle.putByteArray("result", buff);
                    bundle.putString("imagepath",pathlist.get(i).getImagepath());
                    receiver.send(STATUS_FINISHED, bundle);
                } else {
                    Log.d(TAG, "Image Data not found");
                }
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
                bundle.putString(Intent.EXTRA_TEXT, ex.toString());
                receiver.send(STATUS_ERROR, bundle);
            }
        }
        Log.d(TAG, "Service Stop");
        this.stopSelf();
    }

    private byte[] downloadData(String requestUrl) {
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;
        try {
        /* forming th java.net.URL object */
            URL url = new URL(requestUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
        /* optional request header */
            urlConnection.setRequestProperty("Content-Type", "application/jpg");
        /* optional request header */
            urlConnection.setRequestProperty("Accept", "application/jpg");
        /* for Get request */
            urlConnection.setRequestMethod("GET");
            int statusCode = urlConnection.getResponseCode();
        /* 200 represents HTTP OK */
            if (statusCode == 200) {
                inputStream = new BufferedInputStream(urlConnection.getInputStream());
                byte[] buff = getBytes(inputStream);
                return buff;
            } else {
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return null;
    }

    public byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;

        if (is instanceof ByteArrayInputStream) {
            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            buf = new byte[size];
            while ((len = is.read(buf, 0, size)) != -1)
                bos.write(buf, 0, len);
            buf = bos.toByteArray();
        }
        return buf;
    }

}
