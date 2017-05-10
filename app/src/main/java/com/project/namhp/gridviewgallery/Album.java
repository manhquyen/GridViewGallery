package com.project.namhp.gridviewgallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.project.namhp.adapter.ListViewAdapter;
import com.project.namhp.provider.MyDatabase;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Album extends AppCompatActivity {
    private int TAKE_PHOTO_CODE = 0;
    private MyDatabase myDatabase;
    private ListView mListView;
    private ArrayList<String> mArrayList;
    //    private ArrayAdapter<String> mArrayAdapter;
    private int id = 0;
    private ListViewAdapter mListViewAdapter;
    private int dbSize;
    Random random = new Random();
    //private ListView mListView;
    final static private String systemPath = Environment.getExternalStorageDirectory().getAbsolutePath();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mListView = (ListView) findViewById(R.id.list_album);
        myDatabase = new MyDatabase(this);
        final int i = random.nextInt(10000);
        /*try {
            myDatabase.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        myDatabase.createData(String.valueOf(i),"Default");
        mArrayList = myDatabase.getData();
        String demo = mArrayList.get(0);
        myDatabase.close();*/
        //Toast.makeText(getApplicationContext(),demo,Toast.LENGTH_LONG).show();
        mArrayList = new ArrayList<>();
        myDatabase.open();
        mArrayList = myDatabase.getData();
        myDatabase.close();
        mListViewAdapter = new ListViewAdapter(getApplicationContext(), mArrayList, R.layout.simple_album_item);
        mListView.setAdapter(mListViewAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mArrayList.get(position).equals("Default")){
                    Intent intent = new Intent(Album.this,MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"default",Toast.LENGTH_SHORT).show();
                }
                else{
                    String path = Album.systemPath+"/"+mArrayList.get(position)+"/";
                    Intent intent = new Intent(Album.this,AlbumPicture.class);
                    intent.putExtra("folderPath",path);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),path,Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerForContextMenu(mListView);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.album_context_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
        //ContextMenu.ContextMenuInfo menuInfo = (ContextMenu.ContextMenuInfo)item.getMenuInfo();
        final int location = menuInfo.position;
        switch (item.getItemId()){
            case R.id.delete_context:
                xulyxoaAlbum(location);
        }

        return super.onContextItemSelected(item);
    }

    private void deleteOneAlbum(int location) {
        String s = mArrayList.get(location);
        myDatabase.open();
        myDatabase.deleteAcc(s);
        mArrayList.remove(location);
        mListViewAdapter.notifyDataSetChanged();
        deleteFolder(s);
        myDatabase.close();
        Toast.makeText(getApplicationContext(),"Đã xóa album",Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_album:
                addAlbum();
                break;
            case R.id.deleteall:
                //delAllAlbum();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void xulyxoaAlbum(final int i) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Xóa Album đã chọn?");
        mBuilder.setMessage("Bạn có chắc chắn xóa không");
        mBuilder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteOneAlbum(i);
            }
        });
        mBuilder.show();
    }

    private void xulyxoaAlbum() {

    }

    private void delOneAlbum(int i) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Xóa Album đã chọn?");
        mBuilder.setMessage("Bạn có chắc chắn xóa không");
        mBuilder.setNegativeButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //xulyxoaAlbum(i);
            }
        });
        //coding
    }

    private void addAlbum() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Album");
        builder.setMessage("Nhập tên album mới của bạn");
        final EditText editText = new EditText(getApplicationContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        builder.setView(editText);
        builder.setNegativeButton("Xong", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText() == null) {
                    Toast.makeText(getApplicationContext(), "ban chua nhap ten album", Toast.LENGTH_LONG).show();
                } else {
                    creatAblum(editText.getText() + "");
                }

            }
        });
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void creatAblum(String s) {
        myDatabase.open();
        int i = random.nextInt(10000);
        myDatabase.createData(String.valueOf(i), s);
        mArrayList.add(s);
        mListViewAdapter.notifyDataSetChanged();
        myDatabase.close();
        createFolder(s);

    }
    private void deleteFolder(String s) {
        String path = Album.systemPath+"/"+s+"/";
        File delFile = new File(path);
        delFile.delete();
        System.out.println("delele finish");

    }



    private void createFolder(String s) {
        File file = new File(s);
        file.mkdir();
        String dir = Album.systemPath + "/" + s + "/";
        openCamera(dir);

    }

    private void openCamera(String dir) {
        //final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();
        Random random = new Random();
        int count = random.nextInt(100000);
        // here,counter will be incremented each time,and the picture taken by camera will be stored as 1.jpg,2.jpg and likewise.
        //count++;
        String file = dir + count + ".jpg";
        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException e) {
        }

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
    }
}

