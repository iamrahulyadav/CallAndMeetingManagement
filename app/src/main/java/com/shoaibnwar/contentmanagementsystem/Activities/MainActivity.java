package com.shoaibnwar.contentmanagementsystem.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoaibnwar.contentmanagementsystem.Adapters.ViewPagerAdapterForFragments;
import com.shoaibnwar.contentmanagementsystem.Fragments.AllContacts;
import com.shoaibnwar.contentmanagementsystem.Fragments.SelectedContacts;
import com.shoaibnwar.contentmanagementsystem.R;

public class MainActivity extends AppCompatActivity{
    /*RecyclerView rvContacts;
    int READ_CONTACTS_PERMISSIONS_REQUEST = 210;
    ProgressBar progressbar;
    List<ContactVO> contactVOList;
    public ArrayList<HashMap<String, String>> selectedContactList;

*/
    public TextView tv_done;
    public static ViewPager viewPager;
    RelativeLayout tab_rl_diary, tab_rl_pets;

    TextView tab_tv_dairy, tab_tv_pets;
    
    
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* startActivity(new Intent(MainActivity.this, AfterCallDetailActivity.class));
        finish();*/
        init();
        onDairyTabClickListener();
        onPetTabClickListener();
        viewPagerTabChangeListner();
        

    }//end of onCreate

    @SuppressLint("NewApi")
    private void init(){

        tab_rl_diary = (RelativeLayout) findViewById(R.id.tab_rl_diary);
        tab_rl_pets = (RelativeLayout) findViewById(R.id.tab_rl_pets);

       
        tab_tv_dairy = (TextView) findViewById(R.id.tab_tv_dairy);
        tab_tv_pets = (TextView) findViewById(R.id.tab_tv_pets);
        tv_done = (TextView) findViewById(R.id.tv_done);

       viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);

        tab_rl_diary.setBackground(ContextCompat.getDrawable(MainActivity.this, R.color.white));
        tab_tv_dairy.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.linkedin_blue));
        viewPager.setCurrentItem(0);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapterForFragments adapter = new ViewPagerAdapterForFragments(getSupportFragmentManager());

        adapter.addFragment(new SelectedContacts(), "Selected Contacts");
        adapter.addFragment(new AllContacts(), "All Contacts");
        viewPager.setAdapter(adapter);

    }
    private void onDairyTabClickListener(){

        tab_rl_diary.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                tab_rl_diary.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.tab_button_style_after_click));
                tab_tv_dairy.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.linkedin_blue));


                tab_rl_pets.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.tabs_button_style));
                tab_tv_pets.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
              
                viewPager.setCurrentItem(0);

            }
        });
    }

    private void onPetTabClickListener(){

        tab_rl_pets.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View view) {
                tab_rl_diary.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.tabs_button_style));
                tab_tv_dairy.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));

                tab_rl_pets.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.tab_button_style_after_click));
                tab_tv_pets.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.linkedin_blue));

                viewPager.setCurrentItem(1);

            }
        });
    }



    private void viewPagerTabChangeListner(){

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("NewApi")
            @Override
            public void onPageSelected(int position) {

                Log.e("TAG", "the selected page position is: " + position);

                switch (position){

                    case 0:
                        //if (position == 0){
                        tab_rl_diary.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.tab_button_style_after_click));
                        tab_tv_dairy.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.linkedin_blue));


                        tab_rl_pets.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.tabs_button_style));
                        tab_tv_pets.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));

                     
                        break;
                    //}
                    case 1:
                        //if (position == 1){
                        tab_rl_diary.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.tabs_button_style));
                        tab_tv_dairy.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));


                        tab_rl_pets.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.tab_button_style_after_click));
                        tab_tv_pets.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.linkedin_blue));
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }



}
