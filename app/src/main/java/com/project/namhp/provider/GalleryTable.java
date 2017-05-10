package com.project.namhp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by NamHp on 12/3/2015.
 */
public class GalleryTable {

    public static final String TABLE_NAME = "gallery_table";
    public static final Uri CONTENT_URI = Uri.parse("content://"+ "com.project.namhp.database" + "/" + TABLE_NAME);
    public static final String ID = "_id";
    public static final String GALLERY_NAME = "gallery_name";
    public static final String CREATE_TABLE = "CREATE TABLE" + TABLE_NAME + " (" + ID + "integer prinary key autoincrement" +
            GALLERY_NAME + "TEXT NOT NULL" + ")";
    //khoi tao database
    public static void onCreate(SQLiteDatabase sqliteDatabase){
        sqliteDatabase.execSQL(CREATE_TABLE);
    }
    //update database
    public static void onUpdate(SQLiteDatabase sqliteDatabase,int oldVersion,int newVersion){
        if(oldVersion ==1 && newVersion ==2 ){
            sqliteDatabase.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME);
        }
    }
    //del
    public static  void deleteData(Context context){
        context.getContentResolver().delete(GalleryTable.CONTENT_URI,null,null);
    }
}
