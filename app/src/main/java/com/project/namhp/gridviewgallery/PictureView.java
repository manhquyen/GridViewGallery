    package com.project.namhp.gridviewgallery;

    import android.app.Activity;
    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.os.Environment;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.GestureDetector;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.MotionEvent;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.RelativeLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.facebook.CallbackManager;
    import com.facebook.FacebookCallback;
    import com.facebook.FacebookException;
    import com.facebook.FacebookSdk;
    import com.facebook.login.LoginManager;
    import com.facebook.login.LoginResult;
    import com.facebook.share.model.SharePhoto;
    import com.facebook.share.model.SharePhotoContent;
    import com.facebook.share.widget.ShareDialog;
    import com.project.namhp.Ultil.AboutUltil;
    import com.project.namhp.adapter.ImageAdapter;
    import com.project.namhp.control.HomeControl;
    import com.project.namhp.control.PictureControl;
    import com.project.namhp.control.SwipeDetector;

    import java.io.File;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

    import uk.co.senab.photoview.PhotoViewAttacher;

    public class PictureView extends AppCompatActivity implements GestureDetector.OnGestureListener {
        private ImageView imageView;
        private static final int UI_ANIMATION_DELAY = 300;
        private View mContentView;
        private View mControlsView;
        private boolean mVisible;
        private PhotoViewAttacher mAttacher;// zoom anh
        private PictureControl mPictureControl;
        private SwipeDetector mSwipeDetector;
        //private View.OnClickListener listenner;
        private GestureDetector gestureDetector;
        private boolean isZoom = false;
        private ArrayList<String> mArrayList;
        private int location;
        private LoginManager manager;
        private CallbackManager callbackManager; //facebook
        public String filePath;
        private Activity mContext;
         ShareDialog shareDialog;
        private TextView mInfoPic;
        private String mStrInfo;
        private AboutUltil mAboutUltil;
        private int[] i;
        private ImageAdapter imageAdapter;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_picture_view);
            gestureDetector = new GestureDetector(this);
            imageView = (ImageView) findViewById(R.id.image_view);
            Bundle bundle = getIntent().getExtras();
            final String s = (String) bundle.get("values");
            FacebookSdk.sdkInitialize(getApplicationContext());
            shareDialog = new ShareDialog(this);
            final String cameraFolder = (String) bundle.get("cameraFolder");
            location = bundle.getInt("locations");
            i = new int[]{location};
            mArrayList = new ArrayList<>();
            String demo = Environment.getExternalStorageDirectory().getAbsolutePath() + "/New Folder/";
            File file = new File(cameraFolder);
            for (File tmp : file.listFiles()) {
                mArrayList.add(tmp.getAbsolutePath());
            }

            //Log.d("namhpb2", mArrayList.get(location+1)+"");
            Bitmap bitmap = BitmapFactory.decodeFile(mArrayList.get(location));
            imageView.setImageBitmap(bitmap);
            mInfoPic = (TextView) findViewById(R.id.infoPic);

            //mAttacher= new PhotoViewAttacher(imageView);
            //registerForContextMenu(imageView);
            mPictureControl = new PictureControl(getApplicationContext());
            mSwipeDetector = new SwipeDetector(); // vuot chuyen anh
            RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.view);
            imageView.setOnTouchListener(mSwipeDetector);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSwipeDetector.swipeDetected()) {
                        if (mSwipeDetector.getAction() == SwipeDetector.Action.LR) {
                            if (i[0] > 0) {
                                Bitmap leftIMG = BitmapFactory.decodeFile(mArrayList.get(i[0] - 1));
                                i[0] = i[0] - 1;
                                imageView.setImageBitmap(leftIMG);
                            } else {
                                Toast.makeText(getApplicationContext(), "Dang la anh dau tien", Toast.LENGTH_SHORT).show();
                            }

                        } else if (mSwipeDetector.getAction() == SwipeDetector.Action.RL) {
                            if (i[0] < (mArrayList.size() - 1)) {
                                Bitmap rightIMG = BitmapFactory.decodeFile(mArrayList.get(i[0] + 1));
                                i[0] = i[0] + 1;
                                imageView.setImageBitmap(rightIMG);
                            } else {
                                Toast.makeText(getApplicationContext(), "last picture", Toast.LENGTH_SHORT).show();
                            }


                        } else if (mSwipeDetector.getAction() == SwipeDetector.Action.BT) {
                            mInfoPic.setText(getInfo(mArrayList.get(i[0])));
                        }
                        else if(mSwipeDetector.getAction() == SwipeDetector.Action.TB){
                            mInfoPic.setText("");
                            mStrInfo = "";
                        }
                    }

                }
            });
            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    xulyzoom();
                    return true;
                }
            });
        }

        private void getInformations(int i) {
            File file = new File(mArrayList.get(i));
            String name = file.getName();
            long leng = file.length();
            String path = file.getAbsolutePath();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thông tin chi tiết:");
            builder.setMessage("Tên: " + name + "\n" + "Kích thước: " + leng + " byte" + "\n" + "Vị trí: " + path);
            builder.setNeutralButton("Close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            //Toast.makeText(getApplicationContext(), "Tên: " + name + "\n" + "Kích thước: " + leng + " byte" + "\n" + "Vị trí: " + path, Toast.LENGTH_LONG).show();
        }

        private String getInfo(String s) {
            File file = new File(s);
            String name = file.getName();
            long leng = file.length()/1204;
            String path = file.getAbsolutePath();
            mStrInfo += "Tên: " + name + "\n" + "Kích thước: " + leng + " kb" + "\n" + "Vị trí: " + path;
            return mStrInfo;
        }

        private void xulyzoom() {
            if (isZoom == false) {
                mAttacher = new PhotoViewAttacher(imageView);
                isZoom = true;
                Toast.makeText(getApplicationContext(), "zoom enable", Toast.LENGTH_SHORT).show();
            } else {
                mAttacher.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mAttacher.cleanup();
                        mAttacher.notify();
                        Toast.makeText(getApplicationContext(), "zoom disable", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });

            }

        }


        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }



        @Override
        public MenuInflater getMenuInflater() {
            return super.getMenuInflater();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_full, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.shareInView:
                    xulychiase();
                    Toast.makeText(PictureView.this, "OK", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.infoinView:
                    getInformations(i[0]);
                    break;
                case R.id.about:
                    showAboutApp();
                    break;
                case R.id.set_home:
                    sethome();

            }
            return super.onOptionsItemSelected(item);
        }

        private void sethome() {
            new HomeControl(this).setHome(mArrayList.get(i[0]));
        }


        private void showAboutApp() {
            mAboutUltil = new AboutUltil(this);
            mAboutUltil.showAboutApp();
        }

    //    private void showAboutApp() {
    //        final Dialog dialog = new Dialog(this);
    //        dialog.setContentView(R.layout.about_fragment);
    //        dialog.setTitle("About:");
    //        Button btnClose = (Button) dialog.findViewById(R.id.btn_close);
    //        btnClose.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                dialog.dismiss();
    //            }
    //        });
    //        dialog.show();
    //    }

        public void xulychiase() {
            callbackManager = CallbackManager.Factory.create();
            List<String> permissionNeeds = Arrays.asList("publish_actions");

            //this loginManager helps you eliminate adding a LoginButton to your UI
            manager = LoginManager.getInstance();
            manager.logInWithPublishPermissions(this, permissionNeeds);

            manager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    sharePhotoToFacebook();
                    Log.d("myLog","OK") ;               }

                private void sharePhotoToFacebook() {
                   Bitmap img = BitmapFactory.decodeFile(mArrayList.get(location));
                    //Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.mirandakerr);
                    SharePhoto sharePhoto = new SharePhoto.Builder()
                            .setBitmap(img)
                            .setCaption("Mạnh Quyền")
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
                    Log.d("myLog","Error") ;
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
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);

        }

    }
