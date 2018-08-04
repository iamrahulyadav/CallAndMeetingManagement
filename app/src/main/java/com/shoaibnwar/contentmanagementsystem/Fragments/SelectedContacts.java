package com.shoaibnwar.contentmanagementsystem.Fragments;

/**
 * Created by gold on 8/1/2018.
 */

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.shoaibnwar.contentmanagementsystem.Activities.AddNewContactActivity;
import com.shoaibnwar.contentmanagementsystem.Activities.FilteredContactForCall;
import com.shoaibnwar.contentmanagementsystem.Adapters.SelectedContactsAdapter;
import com.shoaibnwar.contentmanagementsystem.AppDatabase.SelectedContactsDb;
import com.shoaibnwar.contentmanagementsystem.Helpers.ContactDbHelper;
import com.shoaibnwar.contentmanagementsystem.R;
import java.util.ArrayList;
import java.util.HashMap;
public class SelectedContacts extends Fragment {

    RecyclerView rvContacts;
    ProgressBar progressbar;
    EditText search_view;
    TextView tv_select_all;
    int count = 0;

    FloatingActionMenu materialDesignFAM;
    FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;
    private boolean initiated;
    private Drawable background;
    private Drawable deleteIcon;
    private int xMarkMargin;
    private int leftcolorCode;
    private String leftSwipeLable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_layout_selected_contacts, container, false);
        init(v);
        voidPagerChangeListener();

        return v;
    }

    private void init(View view) {
        rvContacts = (RecyclerView) view.findViewById(R.id.rvContacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter

            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                // view the background view

                *//*View itemView = viewHolder.itemView;
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
                c.drawText("Delete", xMarkLeft + 40, xMarkTop + 10, paint);*//*


                //super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

            }
        };

// attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvContacts);*/

        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        SelectedContactsDb db = new SelectedContactsDb(getActivity());
        count = db.getCount();

        //floating button menu
        materialDesignFAM = (FloatingActionMenu) view.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item3);


        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu first item clicked
                materialDesignFAM.toggle(false);
                final Dialog filterDialog = new Dialog(getActivity());
                filterDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                filterDialog.setContentView(R.layout.custome_filter_dialog);
                final LinearLayout ll_view = (LinearLayout) filterDialog.findViewById(R.id.ll_view);
                final RelativeLayout rl_apply = (RelativeLayout) filterDialog.findViewById(R.id.rl_apply);
                final LinearLayout ll_clips = (LinearLayout) filterDialog.findViewById(R.id.ll_clips);
                final EditText et_company_name = (EditText) filterDialog.findViewById(R.id.et_company_name);
                ll_clips.bringToFront();
                final ImageView iv_clip_1 = (ImageView) filterDialog.findViewById(R.id.iv_clip_1);
                iv_clip_1.bringToFront();
               Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.bonce_anim);
                ll_view.startAnimation(animation);

                rl_apply.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        if (et_company_name.getText().toString().length()<1)
                        {
                            et_company_name.setError("Should not empty");

                        }else {

                        filterDialog.dismiss();
                        Intent i = new Intent(getActivity(), FilteredContactForCall.class);
                        i.putExtra("keyword", et_company_name.getText().toString());
                        startActivity(i);
                       /* if (doesUserHavePermission()) {
                            makeingCall("03174022272");
                        }else {
                            checkPermissionForCall();
                        }*/
                    }}
                });


                //showing dialog
                filterDialog.show();


            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), AddNewContactActivity.class));
                //TODO something when floating action menu second item clicked
                materialDesignFAM.toggle(false);
            }
        });
        floatingActionButton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO something when floating action menu third item clicked

            }
        });

    }

    private void ininit(){
        background = new ColorDrawable();
        xMarkMargin = (int) getActivity().getResources().getDimension(R.dimen.cardview_default_radius);
        deleteIcon = ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_menu_delete);
        deleteIcon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        initiated = true;
    }
    @Override
    public void onResume() {
        super.onResume();
        gettingValuesFromDB();
    }

    private void voidPagerChangeListener()
    {
        ViewPager viewPager = (ViewPager)getActivity().findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("ATg", "the postion of page is " + position);
                if (position == 0){
                    gettingValuesFromDB();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void gettingValuesFromDB()
    {
        SelectedContactsDb db = new SelectedContactsDb(getActivity());
        int currentCount = db.getCount();
       // if (currentCount!= count) {
            ArrayList<ContactDbHelper> dbHelpers = db.getContacts();
            ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
            if (dbHelpers.size() > 0) {
                for (ContactDbHelper helpler : dbHelpers) {
                    String id = helpler.getId();
                    String bookedId = helpler.getContactBookId();
                    String name = helpler.getContactName();
                    String number = helpler.getContactNumber();

                    HashMap<String, String> mapList = new HashMap<>();
                    mapList.put("id", id);
                    mapList.put("phonebookId", bookedId);
                    mapList.put("name", name);
                    mapList.put("number", number);
                    dataList.add(mapList);
                }

                final SelectedContactsAdapter contactAdapter = new SelectedContactsAdapter(getActivity(), dataList);
                rvContacts.setAdapter(contactAdapter);
           // }
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
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)
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
        int result = getActivity().checkCallingOrSelfPermission(Manifest.permission.CALL_PHONE);
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
                Toast.makeText(getActivity(), "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void makeingCall(String number)
    {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        startActivityForResult(intent, 1234);

        // add PhoneStateListener
        PhoneCallListener phoneListener = new PhoneCallListener();
        TelephonyManager telephonyManager = (TelephonyManager) getActivity()
                .getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            Log.e("TAG", "the result code is " + requestCode);
            Log.e("TAG", "the result code is " + resultCode);

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

                    // restart app
                    Intent i = getActivity().getPackageManager()
                            .getLaunchIntentForPackage(
                                    getActivity().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                    isPhoneCalling = false;
                }

            }
        }
    }


}