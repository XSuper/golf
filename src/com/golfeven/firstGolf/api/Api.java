package com.golfeven.firstGolf.api;

import java.io.InputStream;

import com.golfeven.firstGolf.bean.User;
import com.golfeven.firstGolf.common.Constant;
import com.golfeven.firstGolf.common.StringUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class Api {
	private static Api api;
	FinalHttp fh;
	private Api(){
		fh = new FinalHttp();
	}
	/**
	 * 得到实体单例
	 * @return
	 */
	public static Api getInstance(){
		if(api == null){
			api = new Api();
		}
		return api;
	}
	/**
	 * 登陆
	 * @param uname
	 * @param upass
	 * @param mCallBack
	 */
	public void login(String uname,String upass,AjaxCallBack<String> mCallBack) {
		// TODO Auto-generated method stub
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.login");
		params.put("userid", uname);
		params.put("pwd", upass);
		fh.get(Constant.URL_BASE, params, mCallBack);
		

	}
	/**
	 * 更新用户的地址信息
	 * @param user
	 * @param longitude
	 * @param latitude
	 */
	public void updatePlace(User user,String longitude,String latitude){
		
		updatePlace(user.getMid(), user.getToken(), longitude, latitude, null);
	}
	public void updatePlace(String mid,String token,String longitude,String latitude,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.updateLocation");
		params.put("mid", mid);
		params.put("lng", longitude);
		params.put("token", token);
		params.put("lat", latitude);
		fh.get(Constant.URL_BASE, params, mCallBack);
	}
	
	/**
	 * 注册
	 * @param userid
	 * @param pass
	 * @param uname
	 * @param mCallBack
	 */
	public void register(String userid,String pass,String uname,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.register");
		params.put("uname", uname);
		params.put("pwd", pass);
		if(StringUtils.isPhone(userid)){
			params.put("registby", "phone");
			params.put("mobile", userid);
		}else if(StringUtils.isEmail(userid)){
			params.put("registby", "email");
			params.put("email", userid);
		}
		fh.get(Constant.URL_BASE, params, mCallBack);
		
	}
	/**
	 * 拿球友详细信息
	 * @param uid 该球友id
	 * @param mid 当前用户id
	 * @param lat 
	 * @param lng
	 * @param mCallBack
	 */
	public void getBallFriendDetail(String uid,String mid,String lat,String lng,AjaxCallBack<String> mCallBack ){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.MemberInfo");
		params.put("mid", mid);
		params.put("id",uid);
		params.put("lng", lng);
		params.put("lat", lat);
		fh.get(Constant.URL_BASE, params, mCallBack);
	}
	
	/**
	 * 拿个人相集
	 * @param uid
	 * @param mCallBack
	 */
	public void getPhoto(String uid,AjaxCallBack<String> mCallBack ){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.getPicList");
		params.put("mid",uid);
		fh.get(Constant.URL_BASE, params, mCallBack);
	}
	/**
	 * 上传头像
	 * @param user
	 * @param in
	 * @param mCallBack
	 */
	public void uploadFace(User user,InputStream in,AjaxCallBack<String> mCallBack){
		AjaxParams params = new AjaxParams();
		params.put("cmd", "Member.changeFace");
		params.put("mid",user.getMid());
		params.put("token",user.getToken());
		params.put("img",in, "face.jpg", "image/jpeg");
		fh.post(Constant.URL_BASE, params, mCallBack);
		
	}

}
