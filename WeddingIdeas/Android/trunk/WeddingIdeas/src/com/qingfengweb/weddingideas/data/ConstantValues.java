/**
 * 
 */
package com.qingfengweb.weddingideas.data;

import android.os.Environment;

import com.qingfengweb.weddingideas.R;

/**
 * @author qingfeng
 *
 */
public class ConstantValues {
	public static final int[] ANIMRESIN = {R.anim.in_left_right
		,R.anim.in_right_left
		,R.anim.push_down_in
		,R.anim.push_left_in
		,R.anim.in_anim
		,R.anim.push_right_in};
	
	public static final int[] ANIMRESOUT = { R.anim.out_left_right
		,R.anim.out_right_left
		,R.anim.push_down_out
		,R.anim.push_left_out
		,R.anim.out_anim
		,R.anim.push_right_out};
	
	public static String sdcardUrl = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
	public static String weddingIdeasIMgUrl = "weddingideas/";
	public static String yangzhaoImgUrl = weddingIdeasIMgUrl+"yangzhao/";
	public static String taoxiImgUrl = weddingIdeasIMgUrl+"taoxi/";
	public static String huodongImgUrl = weddingIdeasIMgUrl+"huodong/";
	public static String preferenceImgUrl = weddingIdeasIMgUrl+"preferences/";
	
}
