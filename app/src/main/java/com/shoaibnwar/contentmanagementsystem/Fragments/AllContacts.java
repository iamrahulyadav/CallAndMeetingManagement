package com.shoaibnwar.contentmanagementsystem.Fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.shoaibnwar.contentmanagementsystem.Activities.MainActivity;
import com.shoaibnwar.contentmanagementsystem.Adapters.AllContactsAdapter;
import com.shoaibnwar.contentmanagementsystem.AppDatabase.SelectedContactsDb;
import com.shoaibnwar.contentmanagementsystem.Helpers.ContactDbHelper;
import com.shoaibnwar.contentmanagementsystem.Helpers.ContactVO;
import com.shoaibnwar.contentmanagementsystem.Interfaces.SelectionCheckInterface;
import com.shoaibnwar.contentmanagementsystem.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by gold on 8/1/2018.
 */

public class AllContacts extends Fragment implements SelectionCheckInterface{

    RecyclerView rvContacts;
    int READ_CONTACTS_PERMISSIONS_REQUEST = 210;
    ProgressBar progressbar;
    List<ContactVO> contactVOList;
    EditText search_view;
    TextView tv_select_all;
    boolean isSelectedAll = false;
    public ArrayList<HashMap<String, String>> selectedContactList;
    //TextView tv_done;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_layout_all_contacts, container, false);
        init(v);
        onItemClick();
        selectAllCheckBox();
        onClickHandlerForDoneButton();
        return v;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init(View view) {
        rvContacts = (RecyclerView) view.findViewById(R.id.rvContacts);
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        selectedContactList = new ArrayList<>();
        search_view = (EditText) view.findViewById(R.id.search_view);
        tv_select_all = (TextView) view.findViewById(R.id.tv_select_all);

        //tv_done = (TextView) view.findViewById(R.id.tv_done);

        if (doesUserHavePermission()){
            LoadContacts loadContacts = new LoadContacts();
            loadContacts.execute();
        }else
        {
            checkPermissionForReadContacts();
        }
    }
    private void getAllContacts() {
        contactVOList = new ArrayList();
        ContactVO contactVO;

        ContentResolver contentResolver = getActivity().getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
                if (hasPhoneNumber > 0) {
                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    contactVO = new ContactVO();
                    contactVO.setContactName(name);
                    contactVO.setContactId(id);

                    Cursor phoneCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id},
                            null);
                    if (phoneCursor.moveToNext()) {
                        String phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactVO.setContactNumber(phoneNumber);
                    }

                    phoneCursor.close();

                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (emailCursor.moveToNext()) {
                        String emailId = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        /*String imageUri = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
                        contactVO.setContactImage(imageUri);*/
                    }
                    contactVOList.add(contactVO);
                }
            }
        }
    }

    // Callback with the request from calling requestPermissions(...)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_CONTACTS_PERMISSIONS_REQUEST) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoadContacts loadContacts = new LoadContacts();
                loadContacts.execute();


            } else {
                Toast.makeText(getActivity(), "Read Contacts permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissionForReadContacts()
    {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {


            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_CONTACTS)) {
            }
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS},
                    READ_CONTACTS_PERMISSIONS_REQUEST);
        }
    }
    private boolean doesUserHavePermission()
    {
        int result = getActivity().checkCallingOrSelfPermission(Manifest.permission.READ_CONTACTS);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onClickItemCheck(String id) {

    }

    public class LoadContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getAllContacts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressbar.setVisibility(View.GONE);
            generalFunctionForAdapter(selectedContactList, contactVOList);
        }
    }
    private void onItemClick()
    {
        search_view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length()>0){
                List<ContactVO> temList = new ArrayList<>();
                for (int i=0;i<contactVOList.size();i++){
                    ContactVO contactVO = new ContactVO();
                    String name = contactVOList.get(i).getContactName();
                    if (name.toLowerCase().contains(s.toString().toLowerCase())){

                        String id = contactVOList.get(i).getContactId();
                        String number = contactVOList.get(i).getContactNumber();
                        contactVO.setContactId(id);
                        contactVO.setContactName(name);
                        contactVO.setContactNumber(number);
                        if (contactVOList.get(i).isChecked()) {
                            contactVO.setChecked(true);
                        }
                        temList.add(contactVO);
                    }
                }
                // Log.e("TAg", "the siz of temp list " + temList.size());
                    generalFunctionForAdapter(selectedContactList, temList);
            }
            else {
                    generalFunctionForAdapter(selectedContactList, contactVOList);
                }
            }
        });
    }

    private void selectAllCheckBox()
    {
        tv_select_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ContactVO> tempList = new ArrayList<>();
                selectedContactList.clear();
                if (tv_select_all.getText().toString().equals("Select All")) {
                   // isSelectedAll = true;
                    tv_select_all.setText("Un-Check All");
                    for (int i=0;i<contactVOList.size();i++)
                    {
                        HashMap<String, String> selectedContact = new HashMap<>();
                        selectedContact.put("id", contactVOList.get(i).getContactId());
                        selectedContact.put("name", contactVOList.get(i).getContactName());
                        selectedContact.put("number", contactVOList.get(i).getContactNumber());
                        selectedContactList.add(selectedContact);

                        ContactVO contactVO1 = new ContactVO();
                        contactVO1.setContactId(contactVOList.get(i).getContactId());
                        contactVO1.setContactName(contactVOList.get(i).getContactName());
                        contactVO1.setContactNumber(contactVOList.get(i).getContactNumber());
                        contactVO1.setChecked(true);
                        tempList.add(contactVO1);
                    }
                }
                else if (tv_select_all.getText().toString().equals("Un-Check All")){
                    //isSelectedAll = false;
                    tv_select_all.setText("Select All");
                    selectedContactList.clear();
                    for (int i=0;i<contactVOList.size();i++)
                    {
                        ContactVO contactVO1 = new ContactVO();
                        contactVO1.setContactId(contactVOList.get(i).getContactId());
                        contactVO1.setContactName(contactVOList.get(i).getContactName());
                        contactVO1.setContactNumber(contactVOList.get(i).getContactNumber());
                        contactVO1.setChecked(false);
                        tempList.add(contactVO1);
                    }
                }
                contactVOList = tempList;
                generalFunctionForAdapter(selectedContactList, contactVOList);
            }
        });
    }

    private void generalFunctionForAdapter(ArrayList<HashMap<String, String>> jHashList, List<ContactVO> jContactList)
    {

       /* Collections.sort(jContactList, new Comparator<ContactVO>() {
            public int compare(ContactVO v1, ContactVO v2) {
                return v1.getContactName().compareTo(v2.getContactName());
            }
        });*/
        //jHashList.clear();
        selectedContactList.clear();
        selectedContactList = jHashList;
        final List<ContactVO> tempList = new ArrayList<>();
        for (ContactVO contactVO : jContactList){
            HashMap<String, String> tempHsh = new HashMap<>();
            ContactVO contactVO1 = new ContactVO();
                contactVO1.setChecked(contactVO.isChecked());
            contactVO1.setContactNumber(contactVO.getContactNumber());
            contactVO1.setContactName(contactVO.getContactName());
            contactVO1.setContactId(contactVO.getContactId());
            tempList.add(contactVO1);

            tempHsh.put("id", contactVO.getContactId());
            tempHsh.put("name", contactVO.getContactName());
            tempHsh.put("number", contactVO.getContactNumber());
            jHashList.add(tempHsh);
        }

        final AllContactsAdapter contactAdapter = new AllContactsAdapter(tempList, getActivity().getApplicationContext(), jHashList, new SelectionCheckInterface() {
            @Override
            public void onClickItemCheck(String id) {

                if (contactVOList.size() == tempList.size()){

                for(int i=0; i<tempList.size(); i++){
                    String contactId = tempList.get(i).getContactId();
                    if (contactId.equals(id)){
                        ContactVO contactVO = new ContactVO();
                        contactVO.setChecked(tempList.get(i).isChecked());
                        contactVO.setContactNumber(tempList.get(i).getContactNumber());
                        contactVO.setContactName(tempList.get(i).getContactName());
                        contactVO.setContactId(tempList.get(i).getContactId());
                        contactVOList.set(i, contactVO);
                    }
                }
                }
                else {
                    for(int i=0; i<tempList.size(); i++){
                        String contactId = tempList.get(i).getContactId();
                        for (int j=0;j<contactVOList.size();j++){
                            String mainContactId = contactVOList.get(j).getContactId();
                            if (contactId.equals(mainContactId)){
                                ContactVO contactVO = new ContactVO();
                                contactVO.setChecked(tempList.get(i).isChecked());
                                contactVO.setContactNumber(tempList.get(i).getContactNumber());
                                contactVO.setContactName(tempList.get(i).getContactName());
                                contactVO.setContactId(tempList.get(i).getContactId());
                                contactVOList.set(j, contactVO);
                            }
                        }

                    }
                }

            }
        });
        rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvContacts.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();

    }

    private void onClickHandlerForDoneButton()
    {
        TextView tv_done = (TextView)getActivity().findViewById(R.id.tv_done);
        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedContactList.clear();

                for (ContactVO contactVO : contactVOList){
                    if (contactVO.isChecked()){
                        HashMap<String, String> selectedLsit = new HashMap<>();
                        String id = contactVO.getContactId();
                        String name = contactVO.getContactName();
                        String number = contactVO.getContactNumber();
                        selectedLsit.put("id", id);
                        selectedLsit.put("name", name);
                        selectedLsit.put("number", number);
                        selectedContactList.add(selectedLsit);
                    }
                }

                //adding Selected contact to database
                SelectedContactsDb db = new SelectedContactsDb(getActivity());
                ContactDbHelper helper = new ContactDbHelper();
                for (HashMap<String, String> selectedList : selectedContactList){
                    helper.setContactBookId(selectedList.get("phonebook"));
                    helper.setContactName(selectedList.get("name"));
                    helper.setContactNumber(selectedList.get("number"));
                    long insertedValue = db.insertDatatoDb(helper);
                    if (insertedValue>-1){
                        Log.e("TAg", "value inserted to database");
                    }

                }

                Toast.makeText(getActivity(), "Your contact added to Database Successfully", Toast.LENGTH_SHORT).show();
                ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
                viewPager.setCurrentItem(0);
            }
        });
    }

}