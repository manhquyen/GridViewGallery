package com.project.namhp.gridviewgallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.project.namhp.adapter.ImageAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by MyPC on 09/05/2017.
 */

public class AddPhotoAlbum extends AppCompatActivity {
    private ImageAdapter imageAdapter;
    private ListView listView;
    private int TAKE_PHOTO_CODE = 0;
    public static int count;
    public static String RESULT_AB = "2017";
    final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/New Folder/camera";
    final String cameraFolder = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_photo_album); listView = (ListView) findViewById(R.id.list_add_photo);
        imageAdapter = new ImageAdapter(this);
        listView.setAdapter(imageAdapter);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Intent intent = new Intent(AddPhotoAlbum.this, AlbumPicture.class);//day vi tri anh dc chon vao album
                String str = imageAdapter.getItem(position);
                Intent intent = new Intent();
                intent.putExtra(RESULT_AB,str);
                setResult(RESULT_OK,intent);
                finish();

                Toast.makeText(getApplicationContext(),"them vao album "+ str,Toast.LENGTH_SHORT).show();
            }
        });
        File targetDirector = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/");

        File[] files = targetDirector.listFiles();
        for (File file : files) {
            imageAdapter.add(file.getAbsolutePath());
        }
    }
}
