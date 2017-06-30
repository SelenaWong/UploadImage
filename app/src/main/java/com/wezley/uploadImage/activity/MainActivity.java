package com.wezley.uploadImage.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wezley.uploadImage.R;
import com.wezley.uploadImage.view.RoundedImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends CheckPermissionsActivity {

   private int RESULT_PICTURE_OK =1000;
    @BindView(R.id.activity_main_rmv)RoundedImageView imageView;
    @Override
    protected void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.activity_main_rmv)
    public void takePhoto(){
        Intent imgIntent = new Intent(MainActivity.this, ChangeAvatarActivity.class);
        startActivityForResult(imgIntent,RESULT_PICTURE_OK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_PICTURE_OK && resultCode==RESULT_OK && data!=null){
            Bitmap bmp = data.getParcelableExtra("BMP");
            imageView.setImageBitmap(bmp);
        }
    }
}
