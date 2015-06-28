package demoapp.app.com.appexercise;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.demo.appmodel.AppModel;
import com.demo.apputil.DownLoadStatusReceiver;
import com.demo.apputil.FileUtils;
import com.demo.apputil.ListImageAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements DownLoadStatusReceiver.Receiver {
    private DownLoadStatusReceiver mReceiver;
    private ListView listView;
    private List<AppModel> modelListView;
    private ListImageAdapter listViewAdapter;
    private String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        modelListView = new ArrayList<AppModel>();
        listView = (ListView) findViewById(R.id.mylistview);
        listViewAdapter = new ListImageAdapter(this, modelListView);
        listView.setAdapter(listViewAdapter);

        if (FileUtils.isNetWorkAvailable(getApplicationContext())) {
            startMyService();
        } else {
            Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case ImageIntentService.STATUS_RUNNING:
                break;
            case ImageIntentService.STATUS_FINISHED:

                AppModel item = new AppModel();
                try {
                    byte bytes[] = resultData.getByteArray("result");
                    String imagepath = resultData.getString("imagepath");
                /* Update ListView with result */
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    item.setBitmapImage(bmp);
                    item.setImagepath(imagepath);
                    modelListView.add(item);
                    listViewAdapter.notifyDataSetChanged();
                } catch (Exception ex) {
                    Log.e(TAG, ex.toString());
                }
                break;
            case ImageIntentService.STATUS_ERROR:
                break;
        }
    }

    public void startMyService() {
        mReceiver = new DownLoadStatusReceiver(new Handler());
        mReceiver.setReceiver(MainActivity.this);
        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, ImageIntentService.class);
        intent.putExtra("receiver", mReceiver);
        startService(intent);
    }

}
