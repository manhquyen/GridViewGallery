package com.project.namhp.gridviewgallery;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.ArrayList;
import com.project.namhp.control.PictureControl;
import com.project.namhp.control.SwipeDetector;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.gestures.OnGestureListener;

public class FullscreenActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,DialogInterface.OnMultiChoiceClickListener{

    private static final boolean AUTO_HIDE = true;

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    private ImageView imageView;
    private static final int UI_ANIMATION_DELAY = 300;
    private View mContentView;
    private View mControlsView;
    private boolean mVisible;
    private PhotoViewAttacher mAttacher;
    private PictureControl mPictureControl;
    private SwipeDetector mSwipeDetector;
    //private View.OnClickListener listenner;
    private GestureDetector gestureDetector;
    private boolean isZoom = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
//namhpb
        gestureDetector = new GestureDetector(this);
        imageView = (ImageView) findViewById(R.id.fullscreen_content);
        Bundle bundle = getIntent().getExtras();
        final String s = (String) bundle.get("values");
        final int location = bundle.getInt("locations");
        final int[] i = {location};
        final ArrayList<String> mArrayList = new ArrayList<>();
        String demo = Environment.getExternalStorageDirectory().getAbsolutePath()+"/New Folder/";
        File file = new File(demo);
        for(File tmp:file.listFiles()){
            mArrayList.add(tmp.getAbsolutePath());
        }

        //Log.d("namhpb2", mArrayList.get(location+1)+"");
        Bitmap bitmap = BitmapFactory.decodeFile(mArrayList.get(location));
        imageView.setImageBitmap(bitmap);
        findViewById(R.id.dummy_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInformations(s);
            }
        });
        //mAttacher= new PhotoViewAttacher(imageView);
        //registerForContextMenu(imageView);
        mPictureControl = new PictureControl(getApplicationContext());
        mSwipeDetector = new SwipeDetector();
        imageView.setOnTouchListener(mSwipeDetector);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSwipeDetector.swipeDetected()) {
                    if (mSwipeDetector.getAction() == SwipeDetector.Action.LR) {
                        if(i[0]>0){
                            Bitmap leftIMG = BitmapFactory.decodeFile(mArrayList.get(i[0] - 1));
                            i[0] = i[0] - 1;
                            imageView.setImageBitmap(leftIMG);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Dang la anh dau tien",Toast.LENGTH_SHORT).show();
                        }

                    } else if (mSwipeDetector.getAction() == SwipeDetector.Action.RL) {
                        if(i[0]<(mArrayList.size()-1)){
                            Bitmap rightIMG = BitmapFactory.decodeFile(mArrayList.get(i[0] + 1));
                            i[0] = i[0] + 1;
                            imageView.setImageBitmap(rightIMG);
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"last picture",Toast.LENGTH_SHORT).show();
                        }

                    }
                }

            }
        });
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(!isZoom){
                    xulyzoom();
                    isZoom = true;
                }
                else if(isZoom) {
                    Log.e("namhpb2", String.valueOf(isZoom));
                    mAttacher.cleanup();
                    mAttacher = null;
                    isZoom =false;
                }
                else{
                    Toast.makeText(getApplicationContext(),"nothing",Toast.LENGTH_SHORT).show();
                    Log.d("namhpb3", String.valueOf(isZoom));
                }

                return true;
            }
        });

    }

    private void getInformations(String s) {
        File file = new File(s);
        String name = file.getName();
        long leng = file.length();
        String path=  file.getAbsolutePath();
        Toast.makeText(getApplicationContext(),"Tên: "+name+ "\n" + "Kích thước: " +leng+ " byte" +"\n" +"Vị trí: " + path,Toast.LENGTH_LONG ).show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    private final Handler mHideHandler = new Handler();
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                xulyzoom();
        }
        return super.onContextItemSelected(item);

    }

    private void xulyzoom() {
        mAttacher = new PhotoViewAttacher(imageView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_full, menu);

    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        xulyzoom();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        xulyzoom();
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
}
