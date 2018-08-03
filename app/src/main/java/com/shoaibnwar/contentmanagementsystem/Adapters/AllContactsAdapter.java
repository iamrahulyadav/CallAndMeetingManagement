package com.shoaibnwar.contentmanagementsystem.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.shoaibnwar.contentmanagementsystem.Helpers.ContactVO;
import com.shoaibnwar.contentmanagementsystem.Interfaces.SelectionCheckInterface;
import com.shoaibnwar.contentmanagementsystem.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gold on 7/31/2018.
 */

public class AllContactsAdapter extends RecyclerView.Adapter<AllContactsAdapter.ContactViewHolder>{

    private List<ContactVO> contactVOList;
    private Context mContext;
    public ArrayList<HashMap<String, String>> mSelectedContactList;
    SelectionCheckInterface selectionCheckInterface;
    public boolean isSelectedAll = false;

    public AllContactsAdapter(List<ContactVO> contactVOList, Context mContext, ArrayList<HashMap<String, String>> selectedContactList, SelectionCheckInterface selectionCheckInterface){
        this.contactVOList = contactVOList;
        this.mContext = mContext;
        this.mSelectedContactList = selectedContactList;
        this.selectionCheckInterface = selectionCheckInterface;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_contact_view, null);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        final ContactVO contactVO = contactVOList.get(position);
        holder.tvContactName.setText(contactVO.getContactName());
        holder.tvPhoneNumber.setText(contactVO.getContactNumber());
        boolean isChecked = contactVO.isChecked();
       // Log.e("Tag", "the image uri is " + contactVO.getContactImage());
        Log.e("Tag", "the image uri is " + contactVOList.size());
        Log.e("Tag", "the image uri is " + contactVO.isChecked());
        Picasso.with(mContext).load(R.drawable.circle).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.ivContactImage);
        holder.tv_contact_id.setText(contactVO.getContactId());
        if (isChecked){
            holder.cb_checkbox.setChecked(true);
        }else {
            holder.cb_checkbox.setChecked(false);
        }
        holder.cb_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    HashMap<String, String> selectedContact = new HashMap<>();
                    selectedContact.put("id", contactVO.getContactId());
                    selectedContact.put("name", contactVO.getContactName());
                    selectedContact.put("number", contactVO.getContactNumber());

                    ContactVO contactVO1 = new ContactVO();
                    contactVO1.setChecked(true);
                    contactVO1.setContactId(contactVO.getContactId());
                    contactVO1.setContactName(contactVO.getContactName());
                    contactVO1.setContactNumber(contactVO.getContactNumber());
                    contactVOList.set(position, contactVO1);

                    mSelectedContactList.add(selectedContact);
                    contactVO.setChecked(true);
                    selectionCheckInterface.onClickItemCheck(contactVO.getContactId());
                }
                else if (!isChecked){

                    HashMap<String, String> selectedContact = new HashMap<>();
                    selectedContact.put("id", contactVO.getContactId());
                    selectedContact.put("name", contactVO.getContactName());
                    selectedContact.put("number", contactVO.getContactNumber());

                    ContactVO contactVO1 = new ContactVO();
                    contactVO1.setChecked(false);
                    contactVO1.setContactId(contactVO.getContactId());
                    contactVO1.setContactName(contactVO.getContactName());
                    contactVO1.setContactNumber(contactVO.getContactNumber());
                    contactVOList.set(position, contactVO1);

                    contactVO.setChecked(false);
                    mSelectedContactList.remove(selectedContact);
                    selectionCheckInterface.onClickItemCheck(contactVO.getContactId());

                }
                Log.e("TAg", "the current size of list is " + mSelectedContactList.size());
            }
        });

        //holder.cb_checkbox.setChecked(contactVO.isChecked());
    }

    @Override
    public int getItemCount() {
        return contactVOList.size();
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

        ImageView ivContactImage;
        TextView tvContactName;
        TextView tvPhoneNumber;
        CheckBox cb_checkbox;
        TextView tv_contact_id;

        public ContactViewHolder(View itemView) {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvContactName = (TextView) itemView.findViewById(R.id.tvContactName);
            tvPhoneNumber = (TextView) itemView.findViewById(R.id.tvPhoneNumber);
            cb_checkbox = (CheckBox) itemView.findViewById(R.id.cb_checkbox);
            tv_contact_id = (TextView) itemView.findViewById(R.id.tv_contact_id);
        }
    }

    public void selectedAll(){
        isSelectedAll=true;
        notifyDataSetChanged();
    }

}
