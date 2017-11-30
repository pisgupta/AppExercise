package com.truckdriver.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.truckdriver.R;
import com.truckdriver.model.MyBuddyResponse;
import com.truckdriver.model.SearchBuddyResponse;
import com.truckdriver.view.MyBuddyView;

/**
 * Created by pankaj.kumar on 11/8/2016.
 */

public class MyBuddyListAdapter extends RecyclerView.Adapter<MyBuddyListAdapter.MyViewHolder> {
    private static final String TAG = "MyBuddyListAdapter";
    private Context mContext;
    private MyBuddyResponse.Result responseResults[];
    private OnBuddyItemClickListener listener;
    private MyBuddyView myBuddyView;
    private Typeface mTypeface;
    public boolean undoOn;
    private MyBuddyResponse.Result itemsPendingRemoval[];
    private static final int PENDING_REMOVAL_TIMEOUT = 3000;
    private Handler handler = new Handler();

    public boolean isUndoOn() {
        return undoOn;
    }

    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public MyBuddyListAdapter(Context mContext, MyBuddyResponse.Result responseResults[], OnBuddyItemClickListener itemClickListener, MyBuddyView myBuddyView) {
        this.listener = itemClickListener;
        this.mContext = mContext;
        this.responseResults = responseResults;
        itemsPendingRemoval = responseResults;
        this.myBuddyView = myBuddyView;
        mTypeface = Typeface.createFromAsset(mContext.getAssets(), "lato_bold.ttf");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_buddy_row_adapter, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MyBuddyResponse.Result result = responseResults[position];
        holder.onBind(result, listener, position);
    }

    @Override
    public int getItemCount() {
        return responseResults.length;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "MyViewHolder";
        private ImageView buddy_profile_image;
        private TextView buddy_name, buddy_email, buddy_phone, buddy_status;
        public RelativeLayout mRelativeLayout;
        private Button btnaccept, btnreject;

        public MyViewHolder(View view) {
            super(view);
            mRelativeLayout = (RelativeLayout) view.findViewById(R.id.my_buddy_list);
            buddy_profile_image = (ImageView) view.findViewById(R.id.img_mybuddy_profilepicture);
            buddy_name = (TextView) view.findViewById(R.id.txt_mybuddy_name);
            buddy_email = (TextView) view.findViewById(R.id.txt_mybuddy_email);
            buddy_phone = (TextView) view.findViewById(R.id.txt_mybuddy_phone);
            buddy_status = (TextView) view.findViewById(R.id.txt_buddy_status);
            btnaccept = (Button) view.findViewById(R.id.btn_accept);
            btnreject = (Button) view.findViewById(R.id.btn_reject);
            btnaccept.setTypeface(mTypeface);
            btnreject.setTypeface(mTypeface);

        }

        public void onBind(final MyBuddyResponse.Result result, final OnBuddyItemClickListener listener, final int po) {
            String imageName = "";
            buddy_name.setText(result.getFirstName() + " " + result.getLastName());
            buddy_email.setText(result.getEmail());
            buddy_phone.setText(result.getPhoneNumber());
            buddy_status.setText(result.getStatus());
            if (result.getStatus().equalsIgnoreCase("Accept") || result.getStatus().equalsIgnoreCase("Reject")) {
                buttonHide(btnaccept, btnreject);
            } else {

            }
            if (result.getStatus().equals("Accept")) {
                buddy_status.setTextColor(mContext.getResources().getColor(R.color.button_accept_green));
                buddy_status.setText("Accepted");
            } else if (result.getStatus().equals("Reject")) {
                buddy_status.setTextColor(mContext.getResources().getColor(R.color.button_reject_red));
                buddy_status.setText("Rejected");
            } else {
                buddy_status.setTextColor(mContext.getResources().getColor(R.color.hint_color));
            }

            /**
             * Set profile image from web link
             */
            try {
                imageName = result.getImage();
                Log.e(TAG, "onBind: " + imageName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (imageName == null || imageName.equals("")) {

            } else {
                String img_url = "http://trackdrivers.netsmartz.us/profilepics/" + result.getImage();
                Log.e(TAG, "displayProfile: " + img_url);
                Picasso.with(mContext)
                        .load(img_url)
                        .resize(100, 100)
                        .centerCrop()
                        .into(buddy_profile_image);
            }
            mRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(result, po);
                }
            });


            btnaccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myBuddyView.acceptBuddy(responseResults[po].getDriverBuddyId());
                }
            });
            btnreject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myBuddyView.rejectBuddy(responseResults[po].getDriverBuddyId());
                }
            });


        }
    }

    public interface OnBuddyItemClickListener {
        public void onItemClick(MyBuddyResponse.Result result, int position);
    }


    public void buttonHide(Button accept, Button reject) {
        accept.setVisibility(View.GONE);
        reject.setVisibility(View.GONE);
    }

    public void pendingRemoval(int position) {
        Log.e(TAG, "pendingRemoval: ");
        final MyBuddyResponse.Result item = responseResults[position];

        Runnable pendingRemovalRunnable = new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "run: ");
                //remove(item);
            }
        };
        handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);


    }

    public void remove(int position) {
        Log.e(TAG, "remove: " + position);

        final MyBuddyResponse.Result item = responseResults[position];
        /*if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (items.contains(item)) {
            items.remove(position);
            notifyItemRemoved(position);
        }*/
    }

    public boolean isPendingRemoval(int position) {
        Log.e(TAG, "isPendingRemoval: ");
        final MyBuddyResponse.Result item = responseResults[position];
        //return itemsPendingRemoval.contains(item);
        return true;
    }
}
