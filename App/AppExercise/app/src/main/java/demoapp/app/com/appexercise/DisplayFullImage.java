package demoapp.app.com.appexercise;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.demo.appmodel.AppModel;
import com.demo.apputil.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by gupta on 6/27/2015.
 */
public class DisplayFullImage extends Activity {
    private ImageView imageview;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_full_image);
        imageview = (ImageView) findViewById(R.id.dispplayfullimage);
        try {
            if (FileUtils.isNetWorkAvailable(getApplicationContext())) {
                String imageurl = getIntent().getStringExtra("imageurl");
                new GetImageTask().execute(imageurl);
            } else {
                Toast.makeText(getApplicationContext(), "Network not available", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("In progress...");
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private class GetImageTask extends AsyncTask<String, Integer, Bitmap> {
        private Activity context;
        List<AppModel> rowItems;
        int noOfURLs;

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap map = null;
            map = downloadImage(urls[0]);
            return map;
        }


        private Bitmap downloadImage(String urlString) {

            int count = 0;
            Bitmap bitmap = null;

            java.net.URL url;
            InputStream inputStream = null;
            BufferedOutputStream outputStream = null;

            try {
                url = new URL(urlString);
                URLConnection connection = url.openConnection();
                int lenghtOfFile = connection.getContentLength();

                inputStream = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream dataStream = new ByteArrayOutputStream();

                outputStream = new BufferedOutputStream(dataStream);

                byte data[] = new byte[512];
                long total = 0;

                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    /*publishing progress update on UI thread.
                    Invokes onProgressUpdate()*/
                    publishProgress((int) ((total * 100) / lenghtOfFile));

                    // writing data to byte array stream
                    outputStream.write(data, 0, count);
                }
                outputStream.flush();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inSampleSize = 1;

                byte[] bytes = dataStream.toByteArray();
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, bmOptions);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                FileUtils.close(inputStream);
                FileUtils.close(outputStream);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressDialog.dismiss();
            imageview.setImageBitmap(bitmap);
        }

    }
}
