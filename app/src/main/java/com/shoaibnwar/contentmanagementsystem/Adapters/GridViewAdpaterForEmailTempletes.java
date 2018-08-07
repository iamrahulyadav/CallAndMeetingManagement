package com.shoaibnwar.contentmanagementsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoaibnwar.contentmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gold on 8/6/2018.
 */

public class GridViewAdpaterForEmailTempletes extends BaseAdapter {

    private Activity mContext;
    private List<String> emailsTemplets;

    // 1
    public GridViewAdpaterForEmailTempletes(Activity context, List<String> emailsTemplets) {
        this.mContext = context;
        this.emailsTemplets = emailsTemplets;
    }

    // 2
    @Override
    public int getCount() {
        return emailsTemplets.size();
    }

    // 3
    @Override
    public long getItemId(int position) {
        return 0;
    }

    // 4
    @Override
    public Object getItem(int position) {
        return null;
    }

    // 5
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.custome_ematil_tepmlet_layout, null);
            TextView tv_text = (TextView) grid.findViewById(R.id.tv_text);
            tv_text.setText(emailsTemplets.get(position).toString());
        } else {
            grid = (View) convertView;
        }

        return grid;
    }

}