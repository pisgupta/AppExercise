package com.demo.apputil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.appmodel.AppModel;

import java.util.List;

import demoapp.app.com.appexercise.DisplayFullImage;
import demoapp.app.com.appexercise.R;

/**
 * Created by gupta on 6/27/2015.
 */
public class ListImageAdapter extends BaseAdapter {
    private Context context;
    private List<AppModel> rowItems;

    public ListImageAdapter(Context context, List<AppModel> items) {
        this.context = context;
        this.rowItems = items;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtimageurl;
    }

    public int getCount() {
        return rowItems.size();
    }

    public Object getItem(int position) {
        return rowItems.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.layout_image_list, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.imageicon);
            holder.txtimageurl = (TextView) convertView.findViewById(R.id.imageurl);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        try {
            holder.imageView.setImageBitmap(rowItems.get(position).getBitmapImage());
            holder.txtimageurl.setText(rowItems.get(position).getImagepath());
        } catch (Exception ex) {
            Log.e("CustomListViewAdapter", ex.toString());
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayFullImage.class);
                intent.putExtra("imageurl", rowItems.get(position).getImagepath());
                context.startActivity(intent);
            }
        });


        return convertView;
    }
}
