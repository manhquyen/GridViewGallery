package com.project.namhp.adapter;

/**
 * Created by namhp on 15/10/2015.
 */

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter2 extends BaseAdapter {

    private Context context;
    ArrayList<String> imageList = new ArrayList<String>();

    public ImageAdapter2(Context c) {
        context = c;
    }

    public void add(String path) {
        imageList.add(path);
    }

    public void remove(int values) {
        imageList.remove(values);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public String getItem(int positiosn) {
        // TODO Auto-generated method stub
        return imageList.get(positiosn);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    static class ViewHodler {
        ImageView imageView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //ImageView imageView;
        ViewHodler viewHodler = new ViewHodler();
        if (convertView == null) {
            viewHodler.imageView = new ImageView(context);
            viewHodler.imageView.setLayoutParams(new GridView.LayoutParams(1450, 2000));
            viewHodler.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //viewHodler.imageView.setPadding(8, 8, 8, 8);
        } else {
            viewHodler.imageView = (ImageView) convertView;
        }
        Bitmap bm1 = BitmapFactory.decodeFile(imageList.get(position));
        //Bitmap bm = decodeSampledBitmapFromUri(imageList.get(position), 200, 220);
        viewHodler.imageView.setImageBitmap(bm1);
        return viewHodler.imageView;
    }

    public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

        Bitmap bm = null;
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(path, options);

        return bm;
    }

    public int calculateInSampleSize(

            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }

        return inSampleSize;
    }

}