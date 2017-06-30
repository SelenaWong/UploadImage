package com.wezley.uploadImage.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wezley on 2017/6/30.
 */
public class ImageCropUtil {

    public static  File createImageFile(Context context){
        try {
            WeakReference<Context> reference = new WeakReference<Context>(context);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageName = "JPEG_" + timeStamp + "_";
            if(reference.get()!=null){
                File storageDir = reference.get().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File imageFile = File.createTempFile(imageName, ".jpg", storageDir);
                return imageFile;
            }
            return null;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  null;
    }

    /****************************三星手机获取到的图片裁剪后需要旋转*****************************************/

    public static int getBitmapFromDegree(String path){
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,ExifInterface.ORIENTATION_NORMAL);
            switch(orientation){
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }
        return degree;
    }

    /****
     * @description 旋转图片
     * @param bmp
     * @param degree
     * @return
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bmp,int degree){
        Bitmap desBmp =null;
        try {
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            desBmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        }catch (OutOfMemoryError ex){
            ex.printStackTrace();
        }
        if(desBmp==null){
            desBmp=bmp;
        }
        if(bmp!=desBmp){
            bmp.recycle();
        }
        return desBmp;
    }


    /***
     * @description compressImage
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,baos);
        int options =100;
        while(baos.toByteArray().length/1024>100){
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG,options,baos);//压缩图片，图片格式，图片质量100为质量最好，0为质量最差，保存压缩后的数据
            options -= 10;
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(bais,null,null);

        return bitmap;
    }

























}
