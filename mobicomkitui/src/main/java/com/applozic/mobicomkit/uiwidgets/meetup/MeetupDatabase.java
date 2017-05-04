package com.applozic.mobicomkit.uiwidgets.meetup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Septiawan Aji Pradan on 4/22/2017.
 */

public class MeetupDatabase extends SQLiteOpenHelper {
    private Context context;
    private static final String MEETUP_DATABASE = "meetup_database";
    private static final String MEETUP_TABLE = "meetup_table";
    private static final int DATABASE_VERSION = 3;
    private static final String ID = "id";
    private static final String JUDUL = "judul";
    private static final String TEMPAT= "tempat";
    private static final String JAM = "jam";
    private static final String TANGGAL = "tanggal";
    private static final String ID_SERVER = "id_server";



    public MeetupDatabase(Context context){
        super(context,MEETUP_DATABASE,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEETUP_TABLE = "CREATE TABLE "+MEETUP_TABLE+" ("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +ID_SERVER+" TEXT,"
                +JUDUL+" TEXT,"
                +TEMPAT+" TEXT,"
                +TANGGAL+" TEXT,"
                +JAM+" TEXT)";
        db.execSQL(CREATE_MEETUP_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop = "DROP TABLE IF EXISTS ";
        db.execSQL(drop + MEETUP_TABLE);
        onCreate(db);
    }

    public void dropTableMeetup(){
        SQLiteDatabase db = this.getWritableDatabase();
        String drop = "DROP TABLE IF EXISTS ";
        db.execSQL(drop + MEETUP_TABLE);
    }

    public void insertMeetUp(Meetup meetups){
        /*
            judul : Judul meetup
            tanggal : 29-04-2017
            waktu : 12:23:00
            tempat : kemang raya
         */
        Log.d("menikah",meetups.toString());
        Log.d("menikah_alamat",meetups.getAlamat().toString());

        SQLiteDatabase hapus = this.getWritableDatabase();
        hapus.execSQL("DELETE FROM "+MEETUP_TABLE+" WHERE "+ID_SERVER+"='"+meetups.getId());
        hapus.close();
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(ID_SERVER,meetups.getId());
            cv.put(JUDUL,meetups.getJudul());
            cv.put(TEMPAT,meetups.getAlamat());
            cv.put(TANGGAL,meetups.getTanggal());
            cv.put(JAM,meetups.getPukul());
            db.insert(MEETUP_TABLE,null,cv);
            db.close();
        }catch (Exception ex){
            Log.d("gagal_database_insert",ex.toString());
        }
    }

    public ArrayList<Meetup> getMeetup(){
        ArrayList<Meetup> arrayMeetup = new ArrayList<>();
        Meetup meetup;
        try{
            SQLiteDatabase database = this.getReadableDatabase();
            Cursor cursor = database.rawQuery("SELECT "+JUDUL+","+TEMPAT+","+JAM+","+TANGGAL+" FROM "+MEETUP_TABLE,null);
            if(cursor.moveToFirst()){
                do{
                    meetup = new Meetup();
                    meetup.setJudul(cursor.getString(cursor.getColumnIndex(JUDUL)));
                    meetup.setAlamat(cursor.getString(cursor.getColumnIndex(TEMPAT)));
                    meetup.setPukul(cursor.getString(cursor.getColumnIndex(JAM)));
                    meetup.setTanggal(cursor.getString(cursor.getColumnIndex(TANGGAL)));
                    arrayMeetup.add(meetup);
                }while(cursor.moveToNext());
            }else{
                Log.d("gagal_database_read","not_to_firts");
            }
        }catch (Exception e){
            Log.d("gagal_database_read",e.toString());
            return arrayMeetup;
        }
        return arrayMeetup;
    }
}
