package com.weather_condition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 20.12.2017.
 */

public class Database extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION=1;//Database Version
    private static final String DATABASE_NAME="SQLite_sehirler";//Database Name
    private static final String TABLE_NAME="Sehirler";//Table Name

    private static String SEHIR_ID="sehir_id";
    private static String SEHIR_ADI="sehir_adi";


    public Database(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("
                +SEHIR_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +SEHIR_ADI+" TEXT NOT NULL"+")";
        sqLiteDatabase.execSQL(CREATE_TABLE);
      /*  sehirEkle("Adana");
        sehirEkle("Mersin");*/

    }

    private void sehirSil(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,SEHIR_ID+"= ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void sehirEkle(String sehirler){

            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(SEHIR_ADI,sehirler);
            db.insert(TABLE_NAME,null,values);
            db.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public List<Sehirler> tumKayitlar(){

        List<Sehirler> sehirlerList=new ArrayList<>();
        String selectQuery="SELECT * FROM "+TABLE_NAME;
        SQLiteDatabase db=this.getWritableDatabase();

        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                Sehirler s=new Sehirler();
                s.setSehirID(Integer.parseInt(cursor.getString(0)));
                s.setSehirAdi(cursor.getString(1));
                sehirlerList.add(s);
            }
            while (cursor.moveToNext());
        }
        return sehirlerList;
    }

    public void getSehirID(String sehir){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_NAME,new String[]{SEHIR_ID,SEHIR_ADI},SEHIR_ADI+"=?",new String[]{sehir},null,null,null,null);
        if(cursor != null){
            cursor.moveToFirst();
            int _id=cursor.getInt(0);
            sehirSil(_id);
        }
    }

}
