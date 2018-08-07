package com.shoaibnwar.contentmanagementsystem.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.shoaibnwar.contentmanagementsystem.Adapters.GridViewAdpaterForEmailTempletes;
import com.shoaibnwar.contentmanagementsystem.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AfterCallDetailActivity extends AppCompatActivity {

    RelativeLayout rl_iv_back_arrow;
    RadioGroup rg_follow_up;
    LinearLayout ll_for_follow_up_call, ll_for_follow_up_meeting;
    RelativeLayout rl_et_meeting_address;
    TextView tv_set_call_date, tv_set_call_time;
    TextView tv_set_meeting_date, tv_set_meeting_time;
    Spinner sp_rating;
    public static int tagForDateDialog = 0;
    public static int tagForTimeDialog = 0;
    RelativeLayout rl_email_tamplates;
    GridView grid_view;
    GridViewAdpaterForEmailTempletes emailAdapter;
    List<String> emailTempletsList;
    TextView tv_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_call_detail);

        init();
        radioSelectListener();
        onBackArrowClickListener();
        settingCallingDate();
        settingCallingTime();
        settingMeetingDate();
        settingMeetingTime();
        gridViewItemClickHancdler();
        skipButtonClickHandler();

    }
    private void init()
    {
        rl_iv_back_arrow = (RelativeLayout) findViewById(R.id.rl_iv_back_arrow);
        rg_follow_up = (RadioGroup) findViewById(R.id.rg_follow_up);
        ll_for_follow_up_call = (LinearLayout) findViewById(R.id.ll_for_follow_up_call);
        ll_for_follow_up_call.setVisibility(View.GONE);
        ll_for_follow_up_meeting = (LinearLayout) findViewById(R.id.ll_for_follow_up_meeting);
        ll_for_follow_up_meeting.setVisibility(View.GONE);
        rl_et_meeting_address = (RelativeLayout) findViewById(R.id.rl_et_meeting_address);
        rl_et_meeting_address.setVisibility(View.GONE);
        tv_set_call_date = (TextView) findViewById(R.id.tv_set_call_date);
        tv_set_call_time = (TextView) findViewById(R.id.tv_set_call_time);
        tv_set_meeting_date = (TextView) findViewById(R.id.tv_set_meeting_date);
        tv_set_meeting_time = (TextView) findViewById(R.id.tv_set_meeting_time);
        tv_skip = (TextView) findViewById(R.id.tv_skip);

        sp_rating = (Spinner) findViewById(R.id.sp_rating);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(AfterCallDetailActivity.this,
                R.array.ratings, R.layout.spinner_rating);

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        sp_rating.setAdapter(adapter);

        rl_email_tamplates = (RelativeLayout) findViewById(R.id.rl_email_tamplates);
        grid_view = (GridView) findViewById(R.id.grid_view);
        emailTempletsList = new ArrayList<>();
        emailTempletsList.add("Email");
        emailTempletsList.add("Email");
        emailTempletsList.add("Email");
        String[] myResArray = getResources().getStringArray(R.array.email_temlets_text);
        emailTempletsList = Arrays.asList(myResArray);
        emailAdapter = new GridViewAdpaterForEmailTempletes(AfterCallDetailActivity.this, emailTempletsList);
        grid_view.setAdapter(emailAdapter);


    }//end of init

    private void radioSelectListener()
    {
        rg_follow_up.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) findViewById(checkedId);
                String followUptype = rb.getText().toString();
                if (followUptype.equals("Call")){
                    ll_for_follow_up_call.setVisibility(View.VISIBLE);
                    ll_for_follow_up_call.setAnimation(AnimationUtils.loadAnimation(AfterCallDetailActivity.this, R.anim.let_to_right));
                    ll_for_follow_up_meeting.setVisibility(View.GONE);
                    rl_et_meeting_address.setVisibility(View.GONE);
                    rl_email_tamplates.setVisibility(View.GONE);
                }
                else if (followUptype.equals("Meeting")){
                    ll_for_follow_up_meeting.setVisibility(View.VISIBLE);
                    ll_for_follow_up_meeting.setAnimation(AnimationUtils.loadAnimation(AfterCallDetailActivity.this, R.anim.let_to_right));
                    ll_for_follow_up_call.setVisibility(View.GONE);
                    rl_et_meeting_address.setVisibility(View.VISIBLE);
                    rl_et_meeting_address.setAnimation(AnimationUtils.loadAnimation(AfterCallDetailActivity.this, R.anim.let_to_right));
                    rl_email_tamplates.setVisibility(View.GONE);

                }
                else if (followUptype.equals("Email")){

                    ll_for_follow_up_meeting.setVisibility(View.GONE);
                    ll_for_follow_up_call.setVisibility(View.GONE);
                    rl_et_meeting_address.setVisibility(View.GONE);
                    rl_email_tamplates.setVisibility(View.VISIBLE);
                    rl_email_tamplates.setAnimation(AnimationUtils.loadAnimation(AfterCallDetailActivity.this, R.anim.let_to_right));
                }
            }
        });
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

    private void settingCallingDate()
    {
        tv_set_call_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new DatePickerFragment();
              dFragment.show(getFragmentManager(), "Date Picker");
              tagForDateDialog = 1;

            }
        });
    }
    private void settingMeetingDate()
    {
        tv_set_meeting_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dFragment = new DatePickerFragment();
                dFragment.show(getFragmentManager(), "Date Picker");
                tagForDateDialog = 2;

            }
        });
    }

    private void settingCallingTime()
    {
        tv_set_call_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dFragment = new TimePickerFragment();
                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Time Picker");
                tagForTimeDialog = 1;

            }
        });


    }

    private void settingMeetingTime()
    {
        tv_set_meeting_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dFragment = new TimePickerFragment();
                // Show the date picker dialog fragment
                dFragment.show(getFragmentManager(), "Time Picker");
                tagForTimeDialog  = 2;
            }
        });


    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // DatePickerDialog THEME_DEVICE_DEFAULT_LIGHT
            DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                    R.style.datepicker,this,year,month,day);


            // Return the DatePickerDialog
            return  dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            TextView tv = null;
            if (tagForDateDialog==1) {
                tv = (TextView) getActivity().findViewById(R.id.tv_set_call_date);
            }
            else if (tagForDateDialog==2) {
                tv = (TextView) getActivity().findViewById(R.id.tv_set_meeting_date);
            }
            Calendar cal = Calendar.getInstance(); cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);

            Date chosenDate = cal.getTime();
            String myFormat = "dd-MMM-yyyy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            String dateis = sdf.format(chosenDate);
            tv.setText(dateis);

        }
    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            // Get a Calendar instance
            final Calendar calendar = Calendar.getInstance();
            // Get the current hour and minute
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // TimePickerDialog Theme : THEME_DEVICE_DEFAULT_LIGHT
            TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                    R.style.datepicker,this,hour,minute,false);


            // Return the TimePickerDialog
            return tpd;
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
            // Do something with the returned time
            TextView tv = null;
            if (tagForTimeDialog==1) {
                tv = (TextView) getActivity().findViewById(R.id.tv_set_call_time);
            }
            else if (tagForTimeDialog==2) {
                tv = (TextView) getActivity().findViewById(R.id.tv_set_meeting_time);
            }

            String am_pm = "";

            Calendar datetime = Calendar.getInstance();
            datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            datetime.set(Calendar.MINUTE, minute);

            if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                am_pm = "AM";
            else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                am_pm = "PM";


            String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";
            String hours = strHrsToShow;
            int inthurs = Integer.parseInt(hours);
            if (inthurs<10){
                String stringhours = String.valueOf(inthurs);
                hours = "0"+hours;
            }
            int minuts = datetime.get(Calendar.MINUTE);
            String stringMinuts = String.valueOf(minuts);
            if (minute<10){
                stringMinuts = "0"+stringMinuts;

            }
            tv.setText( hours+":"+stringMinuts+" "+am_pm );

            //tv.setText("HHH:MMM\n" + hourOfDay + ":" + minute);
        }
    }

    private void gridViewItemClickHancdler()
    {
        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textView = (TextView) view.findViewById(R.id.tv_text);
                Intent i = new Intent(AfterCallDetailActivity.this, EmailingActivity.class);
                startActivity(i);
                Log.e("TAg", "text is " + textView.getText().toString());
            }
        });
    }

    private void skipButtonClickHandler()
    {
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AfterCallDetailActivity.this, EmailingActivity.class));
            }
        });
    }
}
