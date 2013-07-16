package com.golfeven.firstGolf.ui;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.golfeven.firstGolf.R;
import com.golfeven.firstGolf.api.Api;
import com.golfeven.firstGolf.base.BaseActivity;
import com.golfeven.firstGolf.bean.BallFriend;
import com.golfeven.firstGolf.bean.Photo;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.widget.HeadBack;

public class BallFriendDetailActivity extends BaseActivity{
	@ViewInject(id=R.id.activity_ballfriend_detail_headback) HeadBack headback;
	@ViewInject(id=R.id.activity_ballfriend_detail_photo) ImageView mPhoto;
	@ViewInject(id=R.id.activity_ballfriend_detail_face) ImageView face;
	
	
	private BallFriend ballFriend;
	
	private FinalBitmap fb;
	private List<Photo> photos;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ballfriend_detail);
		fb = appContext.getFB();
		Intent intent = getIntent();
		ballFriend = intent.getParcelableExtra("ballFriend");
		load();
		initValue();
	}
	
	private void initValue() {
		fb.display(face,Constant.URL_IMG_BASE+ballFriend.getFace());
		
		

	}
	private void load() {
		// TODO Auto-generated method stub
		Api api = Api.getInstance();
		
		//加载详细信息
		api.getBallFriendDetail(ballFriend.getMid(), appContext.user.getMid(), appContext.latitude, appContext.longitude, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				headback.setProgressVisible(false);
				ballFriend = JSON.parseObject(t, BallFriend.class);
				initValue();
			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, strMsg);
				headback.setProgressVisible(false);
			}
			
		});
		//加载个人相册
		api.getPhoto(ballFriend.getMid(), new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				photos = JSON.parseArray(t, Photo.class);
				if(photos!=null&&photos.size()!=0){
					fb.display(mPhoto, Constant.URL_IMG_BASE+photos.get(0).getPic());
					
				}
			}
		});
		face.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(BallFriendDetailActivity.this,ChoicePhotoActivity.class);
				startActivityForResult(intent,001);
				
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==resultCode&&resultCode==001){
			boolean flag = data.getBooleanExtra("flag",false);
			if(flag){
				Bitmap map = BitmapFactory.decodeFile(Constant.IMG_CACHEPATH+ "/face.jpg");
				face.setImageBitmap(map);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
		
	}
	
	

}
