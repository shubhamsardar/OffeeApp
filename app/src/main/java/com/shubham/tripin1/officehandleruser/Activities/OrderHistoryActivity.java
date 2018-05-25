package com.shubham.tripin1.officehandleruser.Activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import com.shubham.tripin1.officehandleruser.Adapters.MiniOrderAdapter;
import com.shubham.tripin1.officehandleruser.Managers.SharedPrefManager;
import com.shubham.tripin1.officehandleruser.Model.CoffeeOrder;
import com.shubham.tripin1.officehandleruser.Model.MyOrder;
import com.shubham.tripin1.officehandleruser.R;
import com.shubham.tripin1.officehandleruser.Utils.CircleTransform;
import com.shubham.tripin1.officehandleruser.holders.OrderListHolder2;

public class OrderHistoryActivity extends AppCompatActivity {
    private FirebaseRecyclerAdapter firebaseRecyclerAdapterOrders;
    private DatabaseReference ref;
    private Context mContext;
    private RecyclerView mRecyclarOrders;
    private StorageReference storageReference;
    private SharedPrefManager mSharedPref;
    AVLoadingIndicatorView indicatorView;
    private boolean isOnScreen = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        mContext = this;
        mSharedPref = new SharedPrefManager(mContext);
        getSupportActionBar().setTitle("Offee - Your Orders History!");
        initView();
        storageReference = FirebaseStorage.getInstance().getReference();
        ref = FirebaseDatabase.getInstance().getReference().child("history");

        Query query = ref.orderByChild("mUserMobile").equalTo(mSharedPref.getMobileNo());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    indicatorView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        firebaseRecyclerAdapterOrders = new FirebaseRecyclerAdapter<MyOrder, OrderListHolder2>(MyOrder.class, R.layout.item_mainorder, OrderListHolder2.class, query) {

            @Override
            protected void populateViewHolder(final OrderListHolder2 viewHolder, final MyOrder model, final int position) {
                MiniOrderAdapter miniOrderAdapter = new MiniOrderAdapter(model.getmOrderList(), mContext);
                viewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                viewHolder.recyclerView.setAdapter(miniOrderAdapter);
                viewHolder.mTxtUserName.setText(model.getmUserName());
                viewHolder.mTxtTimeAgo.setText(gettimeDiff(model.getmTimeAgo())+", ₹"+model.getOrderCost());
                miniOrderAdapter.notifyDataSetChanged();
                MyOrder myOrder = model;
                Glide.with(getApplicationContext())
                        .using(new FirebaseImageLoader())
                        .load(storageReference.child("userImages/" + model.getmUserMobile() + ".jpg"))
                        .centerCrop().crossFade().bitmapTransform(new CircleTransform(mContext))
                        .placeholder(R.drawable.profile)
                        .into(viewHolder.imageUser);

                for (CoffeeOrder o : myOrder.getmOrderList()) {
                    o.setmOrderStatus("Delivered");
                }

            }
        };


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclarOrders.setLayoutManager(linearLayoutManager);
        mRecyclarOrders.setAdapter(firebaseRecyclerAdapterOrders);
        firebaseRecyclerAdapterOrders.notifyDataSetChanged();

    }



    private void initView() {
        mRecyclarOrders = (RecyclerView) findViewById(R.id.rv_main_orders2);
        indicatorView = (AVLoadingIndicatorView) findViewById(R.id.AVLoadingIndicatorView22);
    }

    public String gettimeDiff(String time) {

        String diff = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate;
        try {
            startDate = df.parse(time);
            if (startDate != null) {
                Date endDate = new Date();
                long duration = endDate.getTime() - startDate.getTime();
                long diffInSeconds = Math.abs(TimeUnit.MILLISECONDS.toSeconds(duration));
                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
                if (diffInSeconds == 0) {
                    return "Realtime!";
                }
                if (diffInSeconds < 60) {
                    diff = "" + diffInSeconds + " sec ago";
                } else if (diffInMinutes < 60) {
                    diff = "" + diffInMinutes + " min ago";
                } else if (diffInHours < 24) {
                    diff = "" + diffInHours + " hrs ago";
                } else {
                    long daysago = duration / (1000 * 60 * 60 * 24);
                    diff = "" + daysago + " days ago";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;

    }



}
