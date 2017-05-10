package com.project.namhp.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by NamHp on 12/3/2015.
 */
public class MyDatabase {
    /*Tên database*/
    private static final String DATABASE_NAME = "DB_GALLERY";

    /*Version database*/
    private static final int DATABASE_VERSION = 1;

    /*Tên tabel và các column trong database*/
    private static final String TABLE_ACCOUNT = "TABLEN_ALBUM";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "ten_abum";
    //public static final String COLUMN_PASSWORD = "matkhau";
    //public static final String COLUMN_NAME = "hoten";
    private ArrayList<String> mArrayList;

    /*Các đối tượng khác*/
    private static Context context;
    static SQLiteDatabase db;
    private OpenHelper openHelper;

    /*Hàm dựng, khởi tạo đối tượng*/
    public MyDatabase(Context c){
        MyDatabase.context = c;
    }

    /*Hàm mở kết nối tới database*/
    public MyDatabase open() throws SQLException {
        openHelper = new OpenHelper(context);
        db = openHelper.getWritableDatabase();
        return this;
    }

    /*Hàm đóng kết nối với database*/
    public void close(){
        openHelper.close();
    }

    /*Hàm createData dùng để chèn dữ mới dữ liệu vào database*/
    public long createData(String Id, String names) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, Id);
        cv.put(COLUMN_NAME, names);
        //cv.put(COLUMN_NAME, "nodata");
        return db.insert(TABLE_ACCOUNT, null, cv);
    }

    /*Hàm getData trả về toàn bộ dữ liệu của table ACCOUNT của database dưới 1 chuỗi*/
    public ArrayList<String> getData() {
        mArrayList = new ArrayList<>();
        String[] columns = new String[] {COLUMN_ID,COLUMN_NAME};
        Cursor c = db.query(TABLE_ACCOUNT, columns, null, null, null, null, null);
        /*if(c==null)
            Log.v("Cursor", "C is NULL");*/
        String result="";
        //getColumnIndex(COLUMN_ID); là lấy chỉ số, vị trí của cột COLUMN_ID ...
        int id = c.getColumnIndex(COLUMN_ID);
        //int iN = c.getColumnIndex(COLUMN_ACC);
        //int iMK = c.getColumnIndex(COLUMN_PASSWORD);
        int iNames = c.getColumnIndex(COLUMN_NAME);

        //Vòng lặp lấy dữ liệu của con trỏ
        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            mArrayList.add(c.getString(iNames));
        }
        c.close();
        //Log.v("Result", result);
        return mArrayList;
    }
    public int deleteAcc(String acc) {
        return db.delete(TABLE_ACCOUNT, COLUMN_NAME + "='" + acc + "'", null);
    }

    /*Hàm xóa toàn bộ table ACCOUNT*/
    public int deleteAccountAll() {
        return db.delete(TABLE_ACCOUNT, null, null);
    }

    //---------------- class OpenHelper ------------------
    private static class OpenHelper extends SQLiteOpenHelper {

        /*Hàm dựng khởi tạo 1 OpenHelper*/
        public OpenHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        /*Tạo mới database*/
        @Override
        public void onCreate(SQLiteDatabase arg0) {
            arg0.execSQL("CREATE TABLE " + TABLE_ACCOUNT + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    //+ COLUMN_ACC + " TEXT NOT NULL, "
                    //+ COLUMN_PASSWORD + " TEXT NOT NULL, "
                    + COLUMN_NAME + " TEXT NOT NULL);");
        }

        /*Kiểm tra phiên bản database nếu khác sẽ thay đổi*/
        @Override
        public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
            arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNT);
            onCreate(arg0);
        }
    }
}