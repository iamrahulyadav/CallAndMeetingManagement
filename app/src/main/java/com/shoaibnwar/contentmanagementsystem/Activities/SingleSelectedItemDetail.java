package com.shoaibnwar.contentmanagementsystem.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shoaibnwar.contentmanagementsystem.R;

public class SingleSelectedItemDetail extends AppCompatActivity {

    RelativeLayout rl_iv_back_arrow;
    ImageView iv_circuler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_selected_item_detail);
        init();
        backArrowClickHandler();
    }
    private void init()
    {
        rl_iv_back_arrow  = (RelativeLayout) findViewById(R.id.rl_iv_back_arrow);
        rl_iv_back_arrow.bringToFront();
        iv_circuler = (ImageView) findViewById(R.id.iv_circuler);


    }

    private void backArrowClickHandler()
    {
        rl_iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}
