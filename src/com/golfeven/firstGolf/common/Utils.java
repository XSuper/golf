package com.golfeven.firstGolf.common;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;

import com.golfeven.firstGolf.bean.Score;

/**
 * 其让杂乱的帮助类
 * 
 * @author ISuper
 * 
 */
public class Utils {
	
	/**
	 * 等待加载的进度条
	 * @param context
	 * @param progressDialog
	 */
	public static ProgressDialog initWaitingDialog(Context context){
		ProgressDialog progressDialog
			 = new ProgressDialog(context);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setMessage("请稍等...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			
		progressDialog.show();
		return progressDialog;
	}
	/**
	 * 根据得到洞数生成
	 * 
	 * @param str
	 *            ”XX洞“
	 * @return
	 */
	public static List<Score> getScores(String str) {
		List<Score> scores = new ArrayList<Score>();
		str = str.replaceAll("洞", "").trim();
		int size = 9;
		try {

			size = Integer.parseInt(str);
		} catch (Exception e) {
			// TODO: handle exception
		}
		for (int i = 1; i <= size; i++) {
			Score score = new Score(i);
			scores.add(score);
		}

		return scores;

	}

}
