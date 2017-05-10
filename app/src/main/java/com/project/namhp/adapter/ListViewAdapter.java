package com.project.namhp.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.namhp.gridviewgallery.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by Hoang Nam on 12/6/2015.
 */
public class ListViewAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<String> mArrayList;
    //private ArrayAdapter<String> mArrayAdapter;
    private int layoutId;
    public ListViewAdapter(Context context, ArrayList<String> arrayList,int layoutId) {
        super(context, layoutId, arrayList);
        this.context = context;
        this.mArrayList= arrayList;
        this.layoutId = layoutId;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public int getPosition(Object item) {
        return super.getPosition(item);
    }
    public void removeItem(int i){
        mArrayList.remove(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater  = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layoutId,null);
        final String tmp = mArrayList.get(position);
        final TextView textView = (TextView) convertView.findViewById(R.id.text_album);
        final ImageView imageView  = (ImageView) convertView.findViewById(R.id.imageView2);
        imageView.setImageResource(R.drawable.galleryicon01);
        textView.setText(tmp);
        return convertView;
    }
}
