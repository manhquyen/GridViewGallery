package com.project.namhp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by NamHp on 12/3/2015.
 */
public class DataHelper extends SQLiteOpenHelper {
    public static  final String DATABASE_NAME = "gallery_db";
    private static final int DATABASE_VERSION = 1;
    public DataHelper(Context context) {
        super(context, getDatabasePath(context),null,DATABASE_VERSION);
    }

    private static String getDatabasePath(Context context) {
        String mTemplePath = context.getFilesDir().toString();
        mTemplePath += File.separator;
        mTemplePath += "Data";
        File folder = new File((mTemplePath));
        if(!folder.exists()){
            folder.mkdir();
        }
        mTemplePath += File.separator;
        mTemplePath += DATABASE_NAME;

        return mTemplePath;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        GalleryTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        GalleryTable.onUpdate(db,oldVersion,newVersion);

    }
}
