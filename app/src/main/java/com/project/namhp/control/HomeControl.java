package com.project.namhp.control;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;

/**
 * Created by NamHp on 12/1/2015.
 */
public class HomeControl {
    private Context mContext;
    public HomeControl(Context context){
        this.mContext = context;
    }
    private WallpaperManager mWallpaperManager;
    public void setHome(String filepath){
        mWallpaperManager = WallpaperManager.getInstance(mContext);
        final Bitmap bitmap = BitmapFactory.decodeFile(filepath);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Hình nền");
        builder.setMessage("Bạn muốn đổi hình nền?");
        builder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    mWallpaperManager.setBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
}
