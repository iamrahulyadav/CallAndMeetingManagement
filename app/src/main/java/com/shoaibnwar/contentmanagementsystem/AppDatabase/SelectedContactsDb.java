package com.shoaibnwar.contentmanagementsystem.AppDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.shoaibnwar.contentmanagementsystem.Helpers.ContactDbHelper;

import java.util.ArrayList;

/**
 * Created by gold on 8/2/2018.
 */

public class SelectedContactsDb extends SQLiteOpenHelper {
    Context context;
    public static final String DATABASE_NAME = "selectedcontacts.db";
    private static final int DatabaseVersion = 1;
    public static final String NAME_OF_TABLE = "contactstable";
    public static final String Col_1 = "id";
    public static final String Col_2 = "contactbookid";
    public static final String Col_3 = "contactname";
    public static final String Col_4 = "contactnumber";
    public static final String Col_5 = "contactaddress";
    public static final String Col_6 = "contactcompany";
    public static final String Col_7 = "email";
    public static final String Col_8 = "remarks";
    public static final String Col_9 = "lastconverstion";
    public static final String Col_10 = "followuptype";
    public static final String Col_11 = "followup";
    public static final String Col_12 = "rating";



    String CREATE_TABLE_CALL = "CREATE TABLE " + NAME_OF_TABLE + "("
            + Col_1 + " integer PRIMARY KEY AUTOINCREMENT,"
            + Col_2 + " TEXT, "
            + Col_3 + " TEXT, "
            + Col_4 + " TEXT, "
            + Col_5 + " TEXT, "
            + Col_6 + " TEXT, "
            + Col_7 + " TEXT, "
            + Col_8 + " TEXT, "
            + Col_9 + " TEXT, "
            + Col_10 + " TEXT, "
            + Col_11 + " TEXT, "
            + Col_12 + " TEXT "
            + ")";


    public SelectedContactsDb(Context context) {
        super(context, DATABASE_NAME, null, DatabaseVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CALL);
        //db.execSQL(Create_Virtual_Table_Call);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NAME_OF_TABLE);
        //db.execSQL("DROP TABLE IF EXISTS " + Create_Virtual_Table_Call);

    }

    //inserting post in databse
    public long insertDatatoDb(ContactDbHelper post) {
        long result;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(Col_1, post.getId());
        values.put(Col_2, post.getContactBookId());
        values.put(Col_3, post.getContactName());
        values.put(Col_4, post.getContactNumber());
        values.put(Col_5, post.getContactAddress());
        values.put(Col_6, post.getContactCompany());
        values.put(Col_7, post.getContactEmail());
        values.put(Col_8, post.getContactRemarks());
        values.put(Col_9, post.getContactLastConversationDate());
        values.put(Col_10, post.getContactFollowupType());
        values.put(Col_11, post.getContactFollowupDate());
        values.put(Col_12, post.getContactRating());

        Log.e("TAg", "name name name  " + post.getContactName());
        //inserting valuse into table columns
        result = db.insert(NAME_OF_TABLE, null, values);
        db.close();
        return result;
    }

    /* fetching records from Database Table*/
    public ArrayList<ContactDbHelper> getContacts() {
        String query = "SELECT * FROM " + NAME_OF_TABLE;
        ArrayList<ContactDbHelper> addingToList = new ArrayList<ContactDbHelper>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        if (c != null) {
            while (c.moveToNext()) {
                ContactDbHelper myHelper = new ContactDbHelper();
                String id = c.getString(c.getColumnIndex(Col_1));
                String contactBookId = c.getString(c.getColumnIndex(Col_2));
                String contactName = c.getString(c.getColumnIndex(Col_3));
                String contactNumber = c.getString(c.getColumnIndex(Col_4));
                String contactAddress = c.getString(c.getColumnIndex(Col_5));
                String contactCompany = c.getString(c.getColumnIndex(Col_6));
                String contactEmail = c.getString(c.getColumnIndex(Col_7));
                String contactRemarks = c.getString(c.getColumnIndex(Col_8));
                String contatLastConversationDate = c.getString(c.getColumnIndex(Col_9));
                String contactFollowupType = c.getString(c.getColumnIndex(Col_10));
                String contactFollowupDate = c.getString(c.getColumnIndex(Col_11));
                String contactRating = c.getString(c.getColumnIndex(Col_12));

                myHelper.setId(id);
                myHelper.setContactBookId(contactBookId);
                myHelper.setContactName(contactName);
                myHelper.setContactNumber(contactNumber);
                myHelper.setContactAddress(contactAddress);
                myHelper.setContactCompany(contactCompany);
                myHelper.setContactEmail(contactEmail);
                myHelper.setContactRemarks(contactRemarks);
                myHelper.setContactLastConversationDate(contatLastConversationDate);
                myHelper.setContactFollowupType(contactFollowupType);
                myHelper.setContactFollowupDate(contactFollowupDate);
                myHelper.setContactRating(contactRating);
                //adding data to array list
                addingToList.add(myHelper);
            }
        }

        db.close();
        return addingToList;

    }

    //Updatating post
    public boolean updateTable(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Col_7,status);
        db.update(NAME_OF_TABLE, contentValues, "id = ?", new String[]{Integer.toString(id)});
        db.close();
        return true;
    }

    //deleting post
    public boolean deleteFromTable(long rowId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(NAME_OF_TABLE, Col_1 + "=" + rowId, null);
        db.close();

        return true;

    }

    public int getCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + NAME_OF_TABLE;
        return db.rawQuery(query, null).getCount();
    }

}

