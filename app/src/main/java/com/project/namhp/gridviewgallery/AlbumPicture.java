package com.project.namhp.gridviewgallery;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.project.namhp.Ultil.AboutUltil;
import com.project.namhp.adapter.ImageAdapter;
import com.project.namhp.control.HomeControl;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AlbumPicture extends AppCompatActivity {
    private GridView mGridView;
    private static ImageAdapter mImageAdapter;
    private String folderPath;
    private String location;
    public String str;
    public static final  int RESULT_CODE =1995;

    private int TAKE_PHOTO_CODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_picture);
        Bundle mBundle = getIntent().getExtras();
        location = mBundle.getString("position");
        folderPath = mBundle.getString("folderPath");
        mGridView = (GridView) findViewById(R.id.gridview_album);
        mImageAdapter = new ImageAdapter(this);
        File targetDirector = new File(folderPath);
        //addphoto();
        File[] files = targetDirector.listFiles();
        for (File file : files) {
            mImageAdapter.add(file.getAbsolutePath());
        }

       // mImageAdapter.add(str);
        //mImageAdapter.notifyDataSetChanged();
        //Toast.makeText(getApplicationContext(),location,Toast.LENGTH_LONG).show();
        mGridView.setAdapter(mImageAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO
                Intent intent = new Intent(AlbumPicture.this,PictureView.class);
                intent.putExtra("cameraFolder",folderPath);
                intent.putExtra("locations",position);
                startActivity(intent);
            }
        });
        registerForContextMenu(mGridView);



    }
    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picture_album_view_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.new_photo_album:
                openCamera();
                //printKeyHash();
                break;
            case R.id.add_photo_album:
                Intent intent = new Intent(AlbumPicture.this,AddPhotoAlbum.class);
                //startActivity(intent);
                startActivityForResult(intent,RESULT_CODE);
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void addphoto() {
        mImageAdapter.add(location);
        mImageAdapter.notifyDataSetChanged();
    }
    private void openCamera() {
        //final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        Random random = new Random();
        int i  = random.nextInt(100000);
        // here,counter will be incremented each time,and the picture taken by camera will be stored as 1.jpg,2.jpg and likewise.
        //count++;
        String file = folderPath + i + ".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
        }
        mImageAdapter.add(file);
        mImageAdapter.notifyDataSetChanged();
        Uri outputFileUri = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            Log.d("CameraDemo", "Pic saved");
        }
        if(requestCode == RESULT_CODE ){
            if(resultCode==RESULT_OK){

                str = data.getStringExtra(AddPhotoAlbum.RESULT_AB);
                //str= String.valueOf(data.getIntExtra(AddPhotoAlbum.RESULT_AB,1));
                mImageAdapter.add(str);
                mImageAdapter.notifyDataSetChanged();
                Toast.makeText(AlbumPicture.this, str, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.albumpicture_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //ContextMenu.ContextMenuInfo menuInfo = (ContextMenu.ContextMenuInfo)item.getMenuInfo();
        final int position = menuInfo.position;
        switch (item.getItemId()){
            case R.id.del_in_album:
                delFile(position);
                break;
            case R.id.set_home_album:
                new HomeControl(this).setHome(mImageAdapter.getItem(position));
                break;
        }
        return super.onContextItemSelected(item);


    }

    private void delFile(int position) {
        File file = new File(mImageAdapter.getItem(position));
        file.delete();
        mImageAdapter.remove(position);
        mImageAdapter.notifyDataSetChanged();
    }
}
