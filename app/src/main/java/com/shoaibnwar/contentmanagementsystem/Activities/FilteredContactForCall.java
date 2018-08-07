package com.shoaibnwar.contentmanagementsystem.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.shoaibnwar.contentmanagementsystem.Adapters.SelectedContactsAdapter;
import com.shoaibnwar.contentmanagementsystem.AppDatabase.SelectedContactsDb;
import com.shoaibnwar.contentmanagementsystem.Fragments.SelectedContacts;
import com.shoaibnwar.contentmanagementsystem.Helpers.ContactDbHelper;
import com.shoaibnwar.contentmanagementsystem.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FilteredContactForCall extends AppCompatActivity {
    RecyclerView rvContacts;
    ProgressBar progressbar;
    EditText search_view;
    TextView tv_select_all;
    int count = 0;
    int currentCallNumber = 0;

    private boolean initiated;
    private Drawable background;
    private Drawable deleteIcon;
    private int xMarkMargin;
    private int leftcolorCode;
    private String leftSwipeLable;
    String keyword = "";
    SelectedContactsAdapter contactAdapter;
    android.support.design.widget.FloatingActionButton fb_play_lable, fb_pause_lable;
    boolean isDeleted = false;
    boolean callingProcess = false;
    ArrayList<HashMap<String, String>> dataList;
    RelativeLayout rl_iv_back_arrow;
    int backStatus = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_contact_for_call);


        init();
        gettingValuesFromDB();
        if (doesUserHavePermission()) {
            fbPlayButtonClickHanlder();
        }else {
            checkPermissionForCall();
        }
        fbPauseButtonClickHanlder();
        onBackArrowClickListener();


    }
    private void init() {
        Intent intent = getIntent();
        keyword = intent.getStringExtra("keyword");

        rvContacts = (RecyclerView) findViewById(R.id.rvContacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(FilteredContactForCall.this));

        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        SelectedContactsDb db = new SelectedContactsDb(FilteredContactForCall.this);
        count = db.getCount();
        fb_play_lable = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fb_play_lable);
        fb_pause_lable = (android.support.design.widget.FloatingActionButton) findViewById(R.id.fab_pause_labe);
        fb_play_lable.setVisibility(View.VISIBLE);
        fb_play_lable.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.linkedin_blue)));
        fb_pause_lable.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.linkedin_blue)));

        dataList = new ArrayList<>();
        rl_iv_back_arrow = (RelativeLayout) findViewById(R.id.rl_iv_back_arrow);


    }

    private void ininit(){
        background = new ColorDrawable();
        xMarkMargin = (int) getResources().getDimension(R.dimen.cardview_default_radius);
        deleteIcon = ContextCompat.getDrawable(FilteredContactForCall.this, android.R.drawable.ic_menu_delete);
        deleteIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initiated = true;
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    private void gettingValuesFromDB()
    {
        SelectedContactsDb db = new SelectedContactsDb(FilteredContactForCall.this);
        int currentCount = db.getCount();
        // if (currentCount!= count) {
        ArrayList<ContactDbHelper> dbHelpers = db.getContacts();

        if (dbHelpers.size() > 0) {
            for (ContactDbHelper helpler : dbHelpers) {
                String id = helpler.getId();
                String bookedId = helpler.getContactBookId();
                String name = helpler.getContactName();
                String number = helpler.getContactNumber();

                if (name.toLowerCase().contains(keyword.toLowerCase())) {
                    HashMap<String, String> mapList = new HashMap<>();
                    mapList.put("id", id);
                    mapList.put("phonebookId", bookedId);
                    mapList.put("name", name);
                    mapList.put("number", number);
                    dataList.add(mapList);
                }
            }

            contactAdapter = new SelectedContactsAdapter(FilteredContactForCall.this, dataList);
            rvContacts.setAdapter(contactAdapter);

            onSwiptDeleteAction();
        }
    }

    public String getLeftSwipeLable() {
        return leftSwipeLable;
    }

    public void setLeftSwipeLable(String leftSwipeLable) {
        this.leftSwipeLable = leftSwipeLable;
    }

    public int getLeftcolorCode() {
        return leftcolorCode;
    }

    public void setLeftcolorCode(int leftcolorCode) {
        this.leftcolorCode = leftcolorCode;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionForCall()
    {
        if (ContextCompat.checkSelfPermission(FilteredContactForCall.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {


            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.CALL_PHONE)) {
            }
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                    11);
        }
    }

    private boolean doesUserHavePermission()
    {
        int result = checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE);
        return result == PackageManager.PERMISSION_GRANTED;
    }
    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == 11) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                makeingCall("03174022272");
            } else {
                Toast.makeText(FilteredContactForCall.this, "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @SuppressLint("MissingPermission")
    private void makeingCall(String number)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivityForResult(intent, 1234);

        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getApplication()
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);

        currentCallNumber = currentCallNumber+1;
        backStatus = 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == 111) {

              if (callingProcess) {
                  Log.e("TAg", "the result back is " );
                        int nunberLeanggth = dataList.size();
                        if (currentCallNumber>=nunberLeanggth){
                            Log.e("TAg", "call process ended");
                            fb_play_lable.setVisibility(View.VISIBLE);
                            fb_pause_lable.setVisibility(View.GONE);
                            callingProcess = false;
                        }else {

                            String number  = dataList.get(currentCallNumber).get("number");
                            makeingCall("03174022272");

                        }
                    }
        }
    }

    //monitor phone call activities
    private class PhoneCallListener extends PhoneStateListener {

        private boolean isPhoneCalling = false;

        String LOG_TAG = "LOGGING 123";

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (TelephonyManager.CALL_STATE_RINGING == state) {
                // phone ringing
                Log.e(LOG_TAG, "RINGING, number: " + incomingNumber);
            }

            if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
                // active
                Log.e(LOG_TAG, "OFFHOOK");

                isPhoneCalling = true;
            }

            if (TelephonyManager.CALL_STATE_IDLE == state) {
                // run when class initial and phone call ended,
                // need detect flag from CALL_STATE_OFFHOOK
                Log.e(LOG_TAG, "IDLE");

                if (isPhoneCalling) {
                        Log.e(LOG_TAG, "Back to Applicaiton");
                        if (backStatus==0) {
                            Intent i = new Intent(FilteredContactForCall.this, AfterCallDetailActivity.class);
                            startActivityForResult(i, 111);
                            isPhoneCalling = false;
                        }
                        backStatus = 12;

                }

            }
        }
    }

    private void fbPlayButtonClickHanlder()
    {
        fb_play_lable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fb_play_lable.setVisibility(View.GONE);
                fb_pause_lable.setVisibility(View.VISIBLE);
                callingProcess = true;
                makeingCall("04235972039");


            }
        });
    }

    private void fbPauseButtonClickHanlder()
    {
        fb_pause_lable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fb_play_lable.setVisibility(View.VISIBLE);
                fb_pause_lable.setVisibility(View.GONE);
                callingProcess = false;
            }
        });
    }

    private void onSwiptDeleteAction()
    {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter

                contactAdapter.removeItemAt(viewHolder.getAdapterPosition());
                isDeleted = false;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view

                View itemView = viewHolder.itemView;
                if (!initiated) {
                    ininit();
                }

                int itemHeight = itemView.getBottom() - itemView.getTop();
                //Setting Swipe Background
                ((ColorDrawable) background).setColor(getLeftcolorCode());
                background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                background.draw(c);

                int intrinsicWidth = deleteIcon.getIntrinsicWidth();
                int intrinsicHeight = deleteIcon.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;

                //Setting Swipe Icon
                deleteIcon.setBounds(xMarkLeft, xMarkTop + 16, xMarkRight, xMarkBottom);
                deleteIcon.draw(c);

                //Setting Swipe Text
                Paint paint = new Paint();
                paint.setColor(Color.BLUE);
                paint.setTextSize(48);
                paint.setTextAlign(Paint.Align.CENTER);
                c.drawText("Delete", xMarkLeft + 40, xMarkTop + 10, paint);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                isDeleted = true;
            }
        };

// attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvContacts);
    }

    private void onBackArrowClickListener()
    {
        rl_iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}