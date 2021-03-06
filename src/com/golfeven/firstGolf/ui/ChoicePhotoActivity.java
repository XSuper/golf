package com.golfeven.firstGolf.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.MyLog;
import com.golfeven.firstGolf.common.Utils;
import com.golfeven.firstGolf.widget.MyToast;

public class ChoicePhotoActivity extends BaseActivity {

	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESULT = 3;// 结果

	public static final String IMAGE_UNSPECIFIED = "image/*";

	ImageView imageView;
	@ViewInject(id = R.id.activity_choicephoto_take)
	Button button0;
	@ViewInject(id = R.id.activity_choicephoto_choice)
	Button button1;
	@ViewInject(id = R.id.activity_choicephoto_cancle)
	Button button2;
	
	private Activity my;
	Bitmap photo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_choicephoto);
		// imageView = (ImageView) findViewById(R.id.imageID);
		my = this;
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		// 相册
		button1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
				intent.setDataAndType(
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						IMAGE_UNSPECIFIED);
				startActivityForResult(intent, PHOTOZOOM);
			}
		});

		// 拍照
		button0.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
						Environment.getExternalStorageDirectory(), "temp.jpg")));
				
				startActivityForResult(intent, PHOTOHRAPH);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在跟目录下
			File picture = new File(Constant.IMG_CACHEPATH
					+ "/temp.jpg");
			System.out.println("------------------------" + picture.getPath());
			startPhotoZoom(Uri.fromFile(picture));
		}

		if (data == null)
			return;

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			startPhotoZoom(data.getData());
		}
		// 处理结果
		if (requestCode == PHOTORESULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				photo = extras.getParcelable("data");

				File file = new File(Constant.IMG_CACHEPATH+ "/face.jpg");
				FileOutputStream stream = null;
				try {
					stream = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);
				// (0 -// 100)压缩文件
				// imageView.setImageBitmap(photo);
//				byte buf[] = stream.toByteArray();
//				
//				ByteArrayInputStream in = new ByteArrayInputStream(buf);
				try {
					stream.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				FileInputStream in = null;
				try {
					in = new FileInputStream(file);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Api.getInstance().uploadFace(appContext.user, in, new AjaxCallBack<String>() {
					ProgressDialog progressDialog = Utils.initWaitingDialog(ChoicePhotoActivity.this);
					Intent intent = new Intent();
					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						progressDialog.dismiss();
						MyToast.customToast(ChoicePhotoActivity.this, Toast.LENGTH_SHORT, "成功","头像更换成功",Constant.TOAST_IMG_SUCCESS);
						intent.putExtra("flag",true);
						my.setResult(001, intent);
						my.finish();
						MyLog.v("face", t);
					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						MyLog.v("face", strMsg);
						progressDialog.dismiss();
						intent.putExtra("flag",false);
						my.finish();
						MyToast.customToast(ChoicePhotoActivity.this, Toast.LENGTH_SHORT, "失败","头像更换失败",Constant.TOAST_IMG_SUCCESS);
					}
					
				});
			}

			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 64);
		intent.putExtra("outputY", 64);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESULT);
	}
}
