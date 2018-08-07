package com.shoaibnwar.contentmanagementsystem.Activities;

import android.os.AsyncTask;
import android.os.Build;
import android.os.ResultReceiver;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shoaibnwar.contentmanagementsystem.Helpers.Mail;
import com.shoaibnwar.contentmanagementsystem.R;

public class EmailingActivity extends AppCompatActivity {

    RelativeLayout rl_send;
    ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailing);

        init();
        sendEmailClickHandler();
    }

    private void init() {
        rl_send = (RelativeLayout) findViewById(R.id.rl_send);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        progress_bar.setVisibility(View.GONE);

    }

    private void sendEmailClickHandler()
    {
        rl_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SendEmail sendEmail = new SendEmail();
                sendEmail.execute();
            }
        });

    }

    public class SendEmail extends AsyncTask<Void,Void,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... params) {
            String data = "test";
            try {

                Mail sender = new Mail("marketing@ideacentricity.biz", "Getrixi123!");
                // sender.addAttachment(Environment.getExternalStorageDirectory()+Imagepath);
                sender.sendMail("New Booking Order",
                        "Test Message",
                        "marketing@ideacentricity.biz",
                        "shoaibanwar.vu@gmail.com");

            } catch (Exception e) {
                Log.d("tag", "Exception Occur" + e.toString());
            }
            return data;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String data) {
            Log.e("tag", "Post Excute Data is " + data);
            progress_bar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(),"Mail Sent Successfully",Toast.LENGTH_SHORT).show();

            }
        }
}
