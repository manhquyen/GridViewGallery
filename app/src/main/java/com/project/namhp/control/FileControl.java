package com.project.namhp.control;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by NamHp on 11/22/2015.
 */
public class FileControl {
    public ArrayList<String> mArrayList;
    public String folderPath;
    private Context mContext;
    public FileControl(Context context,String s){
        this.folderPath = s;
        this.mContext=context;
    }
    public FileControl(){

    }
    public ArrayList<String> listFile(){
        File file = new File(folderPath);
        for(File tmp : file.listFiles()){
            mArrayList.add(tmp.getAbsolutePath());
        }
        return mArrayList;
    }



}
