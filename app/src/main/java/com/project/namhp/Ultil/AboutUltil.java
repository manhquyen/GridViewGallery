package com.project.namhp.Ultil;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.project.namhp.gridviewgallery.R;

/**
 * Created by NamHp on 12/1/2015.
 */
public class AboutUltil {
    private Context mContext;
    public AboutUltil(Context context){
        this.mContext = context;
    }
    public void showAboutApp() {
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.about_fragment);
        Button btnClose = (Button) dialog.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}
