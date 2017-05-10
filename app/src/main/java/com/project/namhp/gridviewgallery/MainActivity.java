package com.project.namhp.gridviewgallery;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.project.namhp.Ultil.AboutUltil;
import com.project.namhp.adapter.ImageAdapter;
import com.project.namhp.control.HomeControl;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    //actions bar

    private ImageAdapter imageAdapter;
    private GridView gridview;
    private int TAKE_PHOTO_CODE = 0;
    public static int count;
    final String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/New Folder/camera";
    final String cameraFolder = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/";
    private CallbackManager callbackManager;
    private LoginManager manager;
    private ShareDialog shareDialog;
    private HomeControl mHomeControl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        gridview = (GridView) findViewById(R.id.gridview);
        imageAdapter = new ImageAdapter(this);
        gridview.setAdapter(imageAdapter);
        registerForContextMenu(gridview);
        //////share facebook
       shareDialog = new ShareDialog(this);
        // this part is optional
        //--//--//
        //get the keyhash of app to share facebook applicant
        printKeyHash();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, PictureView.class);
                intent.putExtra("values", imageAdapter.getItem(position));
                intent.putExtra("cameraFolder",cameraFolder);
                Log.d("namhpb",position+"");
                intent.putExtra("locations",position);
                //intent.putExtra("positions",position);
                startActivity(intent);
            }
        });

        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory()
                .getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/New Folder/";
        //String targetPath = ExternalStorageDirectoryPath+ "/Picture/picFolder/";

        //Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG).show();
       File targetDirector = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/");

        File[] files = targetDirector.listFiles();
        for (File file : files) {
             imageAdapter.add(file.getAbsolutePath());
        }
        /*Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<String>();
        //ArrayList<String> resultIAV = new ArrayList<String>();

        String[] directories = null;
        if (u != null)
        {
            c = managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {

                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for(int i=0;i<dirList.size();i++)
        {
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();
            if(imageList == null)
                continue;
            for (File imagePath : imageList) {
                try {

                    if(imagePath.isDirectory())
                    {
                        imageList = imagePath.listFiles();

                    }
                    if ( imagePath.getName().contains(".jpg")|| imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
                            )
                    {



                        String path= imagePath.getAbsolutePath();
                        imageAdapter.add(path);

                    }
                }
                //  }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }*/
    }

    @NonNull
    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.aboutMain:
//                Intent gallery = new Intent(getApplicationContext(), GalleryView.class);
//                startActivity(gallery);
                new AboutUltil(this).showAboutApp();
                break;
            case R.id.album_list:
                Intent newintent = new Intent(getApplicationContext(),Album.class);
                startActivity(newintent);
                //Toast.makeText(getApplicationContext(),"clgt",Toast.LENGTH_LONG).show();
                break;
            case R.id.camera:
                openCamera();
                //printKeyHash();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openCamera() {
        //final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        File newdir = new File(dir);
        newdir.mkdirs();
        Random random = new Random();
        count = random.nextInt(100000);
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


        }else
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //ContextMenu.ContextMenuInfo menuInfo = (ContextMenu.ContextMenuInfo)item.getMenuInfo();
        final int indext = menuInfo.position;
        switch (item.getItemId()) {
            case R.id.delete:
                xulyxoa(indext);
                break;
            case R.id.share:
                xulychiase(indext);
                break;
            case  R.id.set_home_wallpaper:
                sethome(indext);


        }


        return super.onContextItemSelected(item);
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    /*private void xulychiase(int i){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_share);
        builder.setTitle("Share to").setPositiveButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create().show();

    }*/
    private void sethome(int indext) {
        String path = imageAdapter.getItem(indext);
        mHomeControl = new HomeControl(this);
        mHomeControl.setHome(path);


    }

    public void xulychiase(final int posions) {
        callbackManager = CallbackManager.Factory.create();
        List<String> permissionNeeds = Arrays.asList("publish_actions");

        //this loginManager helps you eliminate adding a LoginButton to your UI
        manager = LoginManager.getInstance();
        manager.logInWithPublishPermissions(this, permissionNeeds);

        manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                sharePhotoToFacebook(posions);
            }

            private void sharePhotoToFacebook(int posions) {
                Bitmap img = BitmapFactory.decodeFile(imageAdapter.getItem(posions));
                SharePhoto sharePhoto = new SharePhoto.Builder()
                        .setBitmap(img)
                        .setCaption("")
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(sharePhoto)
                        .build();
                //ShareApi.share(content, null);
                shareDialog.show(content);
            }

            @Override
            public void onCancel() {
                System.out.println("onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                System.out.println("onError");
            }
        });
        /*if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Hello Facebook")
                    .setContentDescription(
                            "The 'Hello Facebook' sample  showcases simple Facebook integration")
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .build();

            shareDialog.show(linkContent);
        }*/
    }


    private void xulyxoa(int indext) {
        File del = new File(imageAdapter.getItem(indext));
        imageAdapter.remove(indext);
        del.delete();
        imageAdapter.notifyDataSetChanged();
        gridview.refreshDrawableState();
        /*Intent intent = new Intent(MainActivity.this,MainActivity.class);
        startActivity(intent);*/


    }
    private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.project.namhp.gridviewgallery",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash1:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", e.toString());
        }
    }
}
