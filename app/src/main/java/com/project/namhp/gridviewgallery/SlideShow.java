package com.project.namhp.gridviewgallery;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterViewFlipper;

import com.project.namhp.adapter.ImageAdapter;
import com.project.namhp.adapter.ImageAdapter2;

import java.io.File;

public class SlideShow extends AppCompatActivity {

    private AdapterViewFlipper simpleAdapterViewFlipper;
    //int[] fruitImages = {R.drawable.apple, R.drawable.pineapple, R.drawable.litchi, R.drawable.mango, R.drawable.banana};     // array of images
    String fruitNames[] = {"Apple", "Pine Apple", "Litchi", "Mango", "Banana"};
    private ImageAdapter2 imageAdapter;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slide_show);
        imageAdapter = new ImageAdapter2(this);
        mp = MediaPlayer.create(SlideShow.this, R.raw.tt);
        mp.setLooping(true);
        mp.setVolume(100, 100);
        mp.start();
        simpleAdapterViewFlipper = (AdapterViewFlipper) findViewById(R.id.simpleAdapterViewFlipper); // get the reference of AdapterViewFlipper
        // Custom Adapter for setting the data in Views
        //CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), fruitNames, fruitImages);
        File targetDirector = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/");

        File[] files = targetDirector.listFiles();
        for (File file : files) {
            imageAdapter.add(file.getAbsolutePath());
        }
        simpleAdapterViewFlipper.setAdapter(imageAdapter); // set adapter for AdapterViewFlipper
        // set interval time for flipping between views
        simpleAdapterViewFlipper.setFlipInterval(5000);
        // set auto start for flipping between views
        simpleAdapterViewFlipper.setAutoStart(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.release();
        finish();
    }
}