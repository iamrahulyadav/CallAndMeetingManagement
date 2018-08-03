package com.shoaibnwar.contentmanagementsystem.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoaibnwar.contentmanagementsystem.Activities.SingleSelectedItemDetail;
import com.shoaibnwar.contentmanagementsystem.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gold on 8/2/2018.
 */

public class SelectedContactsAdapter extends RecyclerView.Adapter<SelectedContactsAdapter.ContactViewHolder>{

    private Activity mContext;
    public ArrayList<HashMap<String, String>> mSelectedContactList;

    public SelectedContactsAdapter(Activity mContext, ArrayList<HashMap<String, String>> selectedContactList){
        this.mContext = mContext;
        this.mSelectedContactList = selectedContactList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.selected_contact_layout, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {

        String dbId = mSelectedContactList.get(position).get("id");
        String phonebookId = mSelectedContactList.get(position).get("phonebook");
        String name = mSelectedContactList.get(position).get("name");
        String number = mSelectedContactList.get(position).get("number");
        holder.tvContactName.setText(name);
        holder.tvPhoneNumber.setText(number);
        holder.tv_contact_id.setText(dbId);
        holder.tv_phonebook_id.setText(phonebookId);
        Picasso.with(mContext).load(R.drawable.circle).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.ivContactImage);

        holder.rl_single_item_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(mContext, SingleSelectedItemDetail.class));

            }
        });

    }

    @Override
    public int getItemCount() {
        return mSelectedContactList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout rl_single_item_1;
        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        TextView tv_contact_id;
        TextView tv_phonebook_id;
        TextView tv_view_details;

        public ContactViewHolder(View itemView) {
            super(itemView);
            rl_single_item_1 = (RelativeLayout) itemView.findViewById(R.id.rl_single_item_1);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            tv_contact_id = (TextView) itemView.findViewById(R.id.tv_contact_id);
            tv_phonebook_id = (TextView) itemView.findViewById(R.id.tv_phonebook_id);
            tv_view_details = (TextView) itemView.findViewById(R.id.tv_view_details);
        }
    }


}
