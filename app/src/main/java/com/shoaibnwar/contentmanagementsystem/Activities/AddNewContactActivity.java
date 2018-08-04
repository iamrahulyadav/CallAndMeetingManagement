package com.shoaibnwar.contentmanagementsystem.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shoaibnwar.contentmanagementsystem.AppDatabase.SelectedContactsDb;
import com.shoaibnwar.contentmanagementsystem.Helpers.ContactDbHelper;
import com.shoaibnwar.contentmanagementsystem.R;

public class AddNewContactActivity extends AppCompatActivity {

    RelativeLayout rl_iv_back_arrow, rl_save;
    EditText et_name, et_phone, et_email;
    EditText et_company_name, company_website, et_company_address;
    String contactName = "";
    String contactPhone = "";
    String contactEmail = "";
    String contactCompany = "";
    String contactCompanyWebsite = "";
    String contactAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);

        init();
        onBackArrowClickHandler();
        onSaveClickHandler();
    }

    private void init()
    {
        rl_iv_back_arrow = (RelativeLayout) findViewById(R.id.rl_iv_back_arrow);
        rl_iv_back_arrow.bringToFront();
        rl_save = (RelativeLayout) findViewById(R.id.rl_save);
        rl_save.bringToFront();
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_email = (EditText) findViewById(R.id.et_email);
        et_company_name = (EditText) findViewById(R.id.et_company_name);
        company_website = (EditText) findViewById(R.id.company_website);
        et_company_address = (EditText) findViewById(R.id.et_company_address);
    }

    private void onBackArrowClickHandler()
    {
        rl_iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onSaveClickHandler()
    {

        rl_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 contactName = et_name.getText().toString();
                 contactPhone = et_phone.getText().toString();
                 contactEmail = et_email.getText().toString();
                 contactCompany = et_company_name.getText().toString();
                 contactCompanyWebsite = company_website.getText().toString();
                 contactAddress = et_company_address.getText().toString();

                if (contactName.length()<1){
                    et_company_name.setError("Please Enter Name");
                }
                else if (contactPhone.length()<1){
                    et_phone.setError("Please Enter Phone");
                }
                else {
                    
                    SelectedContactsDb DB = new SelectedContactsDb(AddNewContactActivity.this);
                    ContactDbHelper helper = new ContactDbHelper();
                    helper.setContactName(contactName);
                    helper.setContactNumber(contactPhone);
                    helper.setContactEmail(contactEmail);
                    helper.setContactCompany(contactCompany);
                    helper.setContactAddress(contactAddress);
                    long insertedNumber = DB.insertDatatoDb(helper);
                    if (insertedNumber!=-1)
                    {
                        Toast.makeText(AddNewContactActivity.this, "Contact Added Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
}
