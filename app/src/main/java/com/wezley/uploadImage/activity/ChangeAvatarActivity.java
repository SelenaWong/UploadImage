/**    
 * 文件名：LogisticPickupPopupWindow.java    
 *    
 * 版本信息：    
 * 日期：2015年8月13日    
 * Copyright 足下 Corporation 2015     
 * 版权所有    
 *    
 */
package com.wezley.uploadImage.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.BaseRequest;
import com.soundcloud.android.crop.Crop;
import com.wezley.uploadImage.R;
import com.wezley.uploadImage.callback.JsonCallback;
import com.wezley.uploadImage.model.ServerResponse;
import com.wezley.uploadImage.util.BitmapUtils;
import com.wezley.uploadImage.util.ImageCropUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;


import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


/**
 * 
 * 类名称：ChangeAvatarActivity 类描述：change avatar
 * 创建人：wesley 创建时间：2017年6月29日 下午7:35:56
 * 
 */
public class ChangeAvatarActivity extends Activity implements
		OnClickListener {

	@BindView(R.id.activity_usercenter_takephotoTv)TextView mTakePhotoTv;
	@BindView(R.id.activity_usercenter_choosepictureTv) TextView mChoosePictureTv;
	@BindView(R.id.activity_usercenter_cancelchangeimageTv)TextView mCancelTv;

	private final int RESULT_PICTURE_OK = 10001;
	private final int REQUEST_CAMERA = 1001;
	private String TAG = ChangeAvatarActivity.class.getSimpleName();
	private Uri mTempPhotoUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usercenter_changeimage);
		getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		ButterKnife.bind(this);
		mChoosePictureTv.setOnClickListener(this);
		mCancelTv.setOnClickListener(this);
		mTakePhotoTv.setOnClickListener(this);
	}

	public void TakePhotoes() {
		try {
			String state = Environment.getExternalStorageState();
			if (state.equals(Environment.MEDIA_MOUNTED)) {
				Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if (takeIntent.resolveActivity(getPackageManager()) != null) {
					File photoFile = null;
					photoFile = ImageCropUtil.createImageFile(ChangeAvatarActivity.this);
					if (photoFile != null) {
						Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), "com.wezley.uploadImage.fileProvider", photoFile);
						takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
						mTempPhotoUri = photoUri;
						startActivityForResult(takeIntent, REQUEST_CAMERA);
					} else {
						Toast.makeText(getApplicationContext(), "拍照失败", Toast.LENGTH_SHORT).show();
						finish();
					}
				} else {
					Toast.makeText(getApplicationContext(), "拍照失败", Toast.LENGTH_SHORT).show();
					finish();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请检查SD卡是否存在", Toast.LENGTH_SHORT).show();
				finish();
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}


	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.activity_usercenter_takephotoTv:
			TakePhotoes();
			break;
		case R.id.activity_usercenter_choosepictureTv:
			Crop.pickImage(this);
			break;
		case R.id.activity_usercenter_cancelchangeimageTv:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		OkGo.getInstance().cancelTag(this);
	}

	/*
         * (non-Javadoc)
         *
         * @see android.app.Activity#onActivityResult(int, int,
         * android.content.Intent)
         */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
				beginCrop( mTempPhotoUri );
			} else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
				beginCrop(data.getData());
			} else if (requestCode == Crop.REQUEST_CROP) {
				handleCrop(resultCode, data);
			} else {
				finish();
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}


	/****
	 * @description 处理裁剪结果
	 * @param resultCode
	 * @param result
     */
	private void handleCrop(int resultCode, Intent result) {
		try {
			if (resultCode == RESULT_OK) {
				Uri uri = Crop.getOutput(result);
				if (uri != null) {
					Bitmap photoBmp = getBitmapFromUri(uri);
					if (photoBmp != null) {
						int degree = ImageCropUtil.getBitmapFromDegree(uri.getPath());
						Bitmap newBitmap = ImageCropUtil.rotateBitmapByDegree(photoBmp, degree);
						/*String mBase = BitmapUtils.bitmapToBase64(newBitmap);//压缩文件并上传文件
						UpLoadUserImage(newBitmap, mBase);*/
						Intent it = getIntent();
						it.putExtra("BMP",newBitmap);
						setResult(RESULT_OK,it);
						finish();
					} else {
						finish();
					}
				} else {
					finish();
				}
			} else if (resultCode == Crop.RESULT_ERROR) {
				Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
				finish();
			}
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

	private void beginCrop(Uri source) {
		Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));//
		Crop.of(source, destination).asSquare().start(this);
	}

	public Bitmap getBitmapFromUri(Uri uri) {
		try {
			InputStream input = getContentResolver().openInputStream(uri);
			BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
			onlyBoundsOptions.inJustDecodeBounds = true;
			onlyBoundsOptions.inDither= true;
			onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
			BitmapFactory.decodeStream(input,null,onlyBoundsOptions);
			input.close();
			int originalWidth = onlyBoundsOptions.outWidth;
			int originalHeight = onlyBoundsOptions.outHeight;
			if(originalWidth==-1||originalHeight==-1){
				finish();
			}
			float hh = 800f;
			float ww = 480f;
			int be =1;//不缩放
			if(originalWidth>originalHeight&& originalWidth>ww){
				be = (int)(originalWidth/ww);
			}else if(originalWidth>originalHeight&& originalHeight>hh){
				be = (int)(originalHeight/hh);
			}
			if(be<=0){
				be=1;
			}
			//比例压缩
			BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
			bitmapOptions.inSampleSize = be;
			bitmapOptions.inDither = true;
			bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
			input = getContentResolver().openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(input,null,bitmapOptions);
			input.close();
			Bitmap _bitmap=ImageCropUtil.compressImage(bitmap);
			if (bitmap != _bitmap) {
				bitmap.recycle();
			}
			return _bitmap;
		}catch (IOException ex){
			ex.printStackTrace();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}


	public void UpLoadUserImage(final Bitmap bmp, String base) {
		String url = "http://....";
		Log.i(TAG, "url= " + url);
		Map<String, String> params = new HashMap<String, String>();
		params.put("avatar", base);
		OkGo.post(url)
				.params("avatar",base)
				.tag(ChangeAvatarActivity.this)
				.execute(new JsonCallback<ServerResponse<Map<String,String>>>() {
					@Override
					public void onBefore(BaseRequest request) {
						super.onBefore(request);
					}

					@Override
					public void onSuccess(ServerResponse<Map<String,String>>  responseData, Call call, Response response) {
						Log.i(TAG, "onSuccess");
						handleResponse(responseData.data, bmp, call, response);
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						Log.i(TAG, "onError");
						handleError(call, response, e);
						finish();
					}

					@Override
					public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
						System.out.println("upProgress -- " + totalSize + "  " + currentSize + "  " + progress + "  " + networkSpeed);
					}
				});
	}

	protected void handleResponse(Map<String,String> data,Bitmap bmp, Call call, Response response) {
        if(data==null||!data.containsKey("avatar")){
			return;
		}
		String imageUrl = data.get("avatar");
		Intent intent = new Intent();
		intent.putExtra("imgUrl", imageUrl);
		setResult(RESULT_PICTURE_OK, intent);
		if (bmp != null) {
			bmp.recycle();
		}
		finish();
	}

	protected void handleError(Call call, Response response, Exception e) {
		String errorMessage = "上传头像失败，请重新尝试";
		Toast.makeText(getApplicationContext(), errorMessage,Toast.LENGTH_SHORT).show();
	}
}