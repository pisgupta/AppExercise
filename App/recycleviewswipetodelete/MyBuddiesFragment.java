package com.truckdriver.fragments;


import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.truckdriver.R;
import com.truckdriver.adapter.DividerItemDecoration;
import com.truckdriver.adapter.MyBuddyListAdapter;
import com.truckdriver.base.BaseFragment;
import com.truckdriver.model.MyBuddyResponse;
import com.truckdriver.presentor.MyBuddyPresentor;
import com.truckdriver.presentorimpl.MyBuddyPresentorImpl;
import com.truckdriver.utils.AlertDialogUtil;
import com.truckdriver.utils.CommonValidation;
import com.truckdriver.utils.CustomDialog;
import com.truckdriver.view.MyBuddyView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBuddiesFragment extends BaseFragment implements HeaderFragment.OnRightIconClick, MyBuddyView, MyBuddyListAdapter.OnBuddyItemClickListener, CustomDialog.OnDeleteBuddyResponse {
    private static final String TAG = "MyBuddiesFragment";
    private View mView;
    private MyBuddyPresentor myBuddyPresentor;
    private RecyclerView mRecyclerView;
    private MyBuddyListAdapter adapter;
    private AlertDialog mCustomDialog;
    private MyBuddyResponse.Result results[];
    private int deleteBuddyPosition;
    private TextView mTextView;

    @Override
    public View setContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_my_buddies, container, false);

        }
        inializeUI();
        return mView;
    }

    public void inializeUI() {
        setHeaderBarTitle(getResources().getString(R.string.title_track_my_buddies));
        setHeaderBarleftIcon(R.drawable.ic_menu);
        setHeaderBarRightIcon(R.drawable.ic_buddy_add);
        showRightIcon();
        myBuddyPresentor = new MyBuddyPresentorImpl((AppCompatActivity) getActivity(), this);
        mRecyclerView = (RecyclerView) mView.findViewById(R.id.my_buddy_list);
        mTextView = (TextView) mView.findViewById(R.id.txt_buddy_error);
        mCustomDialog = CustomDialog.deleteBuddy(getContext(), this);
        myBuddyPresentor.loadBuddyInformation();
        setOnRightIconClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        setHeaderBarTitle(getResources().getString(R.string.title_track_my_buddies));
        setHeaderBarleftIcon(R.drawable.ic_menu);
        setHeaderBarRightIcon(R.drawable.ic_buddy_add);
        showLeftIcon();
    }

    @Override
    public void onClick() {
        replaceFragment(R.id.activity_main_container_frame, new AddBuddyFragment(), AddBuddyFragment.class.getName(), true, getContext());
    }

    @Override
    public void onSuccess(MyBuddyResponse.Result[] results) {
        CommonValidation.hideKeyboard(getActivity(),getView());
        this.results = results;
        if (results.length > 0) {
            mTextView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            adapter = new MyBuddyListAdapter(getContext(), this.results, this, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            adapter.notifyDataSetChanged();
            setUpItemTouchHelper();
        } else {
            mTextView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        hideProgressbar();
    }

    @Override
    public void showProgress() {
        showProgressbar();
    }

    @Override
    public void hideProgress() {
        hideProgressbar();
    }

    @Override
    public void acceptBuddy(String driverBuddyId) {
        myBuddyPresentor.acceptBuddy(driverBuddyId);
    }

    @Override
    public void rejectBuddy(String driverBuddyId) {
        myBuddyPresentor.rejectBuddy(driverBuddyId);
    }

    @Override
    public void acceptBuddyResult(int i) {

        if (i == 1) {
            Toast.makeText(getContext(), "Buddy accepted success fully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Buddy not accepted success fully", Toast.LENGTH_SHORT).show();
        }
        myBuddyPresentor.loadBuddyInformation();
    }

    @Override
    public void rejectBuddyResult(int i) {
        if (i == 1) {
            Toast.makeText(getContext(), "Buddy rejected success fully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Buddy Not rejected success fully", Toast.LENGTH_SHORT).show();
        }
        myBuddyPresentor.loadBuddyInformation();
    }

    @Override
    public void deleteBuddyResult(int i) {
        if (i == 1) {
            Toast.makeText(getContext(), "Buddy deleted success fully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Buddy Not deleted success fully", Toast.LENGTH_SHORT).show();
        }
        myBuddyPresentor.loadBuddyInformation();
    }

    @Override
    public void onItemClick(MyBuddyResponse.Result result, int position) {
        BuddyDetailsFragment buddyDetailsFragment = new BuddyDetailsFragment();
        Bundle bd = new Bundle();
        bd.putString("name", result.getFirstName() + " " + result.getLastName());
        bd.putString("email", result.getEmail());
        bd.putString("phone", result.getPhoneNumber());
        buddyDetailsFragment.setArguments(bd);
        replaceFragment(R.id.activity_main_container_frame, buddyDetailsFragment, BuddyDetailsFragment.class.getName(), true, getActivity());
    }

    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(getActivity(), R.drawable.ic_clear_24dp);
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();
                MyBuddyListAdapter adapter = (MyBuddyListAdapter) recyclerView.getAdapter();
                if (adapter.isUndoOn() && adapter.isPendingRemoval(position)) {
                    return 0;
                }
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                MyBuddyListAdapter adapter = (MyBuddyListAdapter) mRecyclerView.getAdapter();
                boolean undoOn = adapter.isUndoOn();
                if (undoOn) {
                    Log.e(TAG, "onSwiped: " + swipedPosition);
                    adapter.pendingRemoval(swipedPosition);
                } else {
                    deleteBuddyPosition = swipedPosition;
                    Log.e(TAG, "onSwiped: " + swipedPosition);
                    mCustomDialog.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw red background
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
    }


    @Override
    public void onPositiveClick(View v) {
        mCustomDialog.dismiss();
        String id = results[deleteBuddyPosition].getDriverBuddyId();
        Log.e(TAG, "onPositiveClick: " + id);
        myBuddyPresentor.deleteBuddy(id);
        myBuddyPresentor.loadBuddyInformation();
    }

    @Override
    public void onNegativelick(View v) {
        mCustomDialog.dismiss();
        adapter.notifyDataSetChanged();
    }
}
