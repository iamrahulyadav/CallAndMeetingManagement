package com.shoaibnwar.contentmanagementsystem.Fragments;

/**
 * Created by gold on 8/1/2018.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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
        gettingValuesFromDB();
        return v;
    }
    private void init(View view) {
        rvContacts = (RecyclerView) view.findViewById(R.id.rvContacts);
        rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        SelectedContactsDb db = new SelectedContactsDb(getActivity());
        count = db.getCount();
    }

    @Override
    public void onResume() {
        super.onResume();

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
}