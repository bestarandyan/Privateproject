package com.qingfengweb.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class MessageBox {
	/**
	 * CODE=0001
	 * 提示框 </br>
	 * title：提示框标题 </br>
	 * msg ：提示信息 </br>
	 * conntext ：Activity </br>
	 * */
	public static Dialog CreateAlertDialog1(String title, String msg,Context context )
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg)
		.setTitle(title);
//		       .setCancelable(false)
//		       .setPositiveButton(btnname, new DialogInterface.OnClickListener() {
//		           public void onClick(DialogInterface dialog, int id) {
//		        	   dialog.dismiss();
//		           }
//		       });
		 return builder.create();
		
	}
	
	/**
	 * CODE=0002
	 * 提示框 </br>
	 * title：提示框标题 </br>
	 * msg ：提示信息 </br>
	 * conntext ：Activity </br>
	 * */
	public static AlertDialog.Builder createAlertDialogBuilder(String title,String leftBuff, String msg,Context context )
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg)
		.setTitle(title)
//		       .setCancelable(false)
		       .setPositiveButton(leftBuff, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		           }
		       });
		 return builder;
		
	}
	
	
	
	/**
	 * CODE=0003
	 * 提示框 </br>
	 * title：提示框标题 </br>
	 * msg ：提示信息 </br>
	 * btnname : Button按钮</br>
	 * conntext ：Activity </br>
	 * */
	public static Dialog createAlertDialog(String title, String msg, String btnname,Context context )
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg)
		.setTitle(title)
		       .setCancelable(false)
		       .setPositiveButton(btnname, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		           }
		       });
		 return builder.create();
		
	}
	/**
	 * CODE=0004
	 * 提示框 </br>
	 * title：提示框标题 </br>
	 * msg ：提示信息 </br>
	 * conntext ：Activity </br>
	 * */
	public static AlertDialog.Builder createAlertDialogBuilder(String title,String leftBuff,Context context )
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title)
//		       .setCancelable(false)
		       .setPositiveButton(leftBuff, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		           }
		       });
		 return builder;
		
	}
	
	
	
	/**
	 * 提示框</br>
	 * title ：提示框标题</br>
	 * msg : 提示信息</br>
	 * conntext ：Activity</br>
	 * */
	public static void CreateAlertDialog(String title, String msg,Context context )
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(msg)
		.setTitle(title)
		       .setCancelable(false)
		       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	
	
	
	public static void CreateAlertDialog1(String title, SpannableString ss,Context context )
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(ss)
		.setTitle(title)
		       .setCancelable(false)
		       .setPositiveButton("确定", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	//---------------------------------------------------------------------
	/**
	 * 圆形加载进度提示框 </br>
	 * Cancelable:canceable</br>
	 * Msg:提示内容</br>
	 * Context context</br>
	 * */
	public static ProgressDialog createProgressDialog(boolean canceable,String msg,Context context)
	{
		
		
		 ProgressDialog dialog = new ProgressDialog(context);
         dialog.setMessage(msg);
         dialog.setIndeterminate(true);
         dialog.setCancelable(canceable);
         
         return dialog;
	}
	
	/**
	 * 圆形加载进度提示框 </br>
	 * Cancelable:canceable</br>
	 * Title:标题 </br>
	 * Msg:提示内容</br>
	 * Context context</br>
	 * */
	public static ProgressDialog createProgressDialog(boolean canceable,String title,String msg,Context context){
		 ProgressDialog dialog = new ProgressDialog(context);
	     dialog.setTitle(title);
	     dialog.setMessage(msg);
	     dialog.setIndeterminate(true);
	     dialog.setCancelable(canceable);
	     return dialog;
	}
	
	
	
	//-----------------------------------------------------------------------------------
	
	/**
	 * 提示框AlertDialog.Builder</br>
	 * title:标题</br>
	 * msg:信息内容</br>
	 * leftButton：左边按钮名字</br>
	 * rightButton：右边按钮名字</br>
	 * Context context </br>
	 * 范例
	 * MessageBox.CreateAlertDialog("系统提示", "这个是个提示框",this).show();</br>
	
	 * 注意事项:这AlertDialog提示框需要额外增加按钮和按钮处理事件/br>
	 * */
	public static AlertDialog CreateAlertDialog2(String title,String msg,Context context){
		AlertDialog.Builder dialog=  new AlertDialog.Builder(context);
	    dialog.setTitle(title);
	    dialog.setMessage(msg);
	   AlertDialog alert =((Builder) dialog).create();
	return alert;
	}
	
	
	
	
	/**
	 * 提示框AlertDialog.Builder</br>
	 * title:标题</br>
	 * msg:信息内容</br>
	 * leftButton：左边按钮名字</br>
	 * rightButton：右边按钮名字</br>
	 * Context context </br>
	 * 范例
	 * MessageBox.CreateAlertDialog("系统提示", "这个是个提示框", "确定","返回", this).show();</br>
	 * 注意事项:本方法按键处理并为处理</br>
	 * */
	public static AlertDialog CreateAlertDialog2(String title,String msg,String leftButton,String rightButton,Context context){
		AlertDialog.Builder dialog=  new AlertDialog.Builder(context);
	  dialog.setTitle(title);
	  dialog.setMessage(msg);
	  dialog.setPositiveButton(leftButton, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int whichButton) {

             /* User clicked OK so do some stuff */
         }
     });
    
	  dialog.setNegativeButton(rightButton, new DialogInterface.OnClickListener() {
         public void onClick(DialogInterface dialog, int whichButton) {

             /* User clicked Cancel so do some stuff */
         }
     });
	  
	  AlertDialog alert =((Builder) dialog).create();
	
	return alert;
	}
	
	//---------------------------------------------------------------------------------
	public static Dialog CreateAlertDialog3(String title,View views,Context context){
		 LayoutInflater factory = LayoutInflater.from(context);
//	     final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
	     return new AlertDialog.Builder(context).setTitle(title).setView(views).create();
	
	}
	
	//----------------------------------------------------------------------------------
	/**
	 * 
	 * ChoiceClick选择框,多个Chick的List列表
	 * 本方法需要在外部添加按钮组件和绑定按钮触发事件<br>
	 * Title：标题<br>
	 * items ：Click的标签内容<br>
	 * states：Click的状态，状态为 ture或者false<br>
	 * 
	 *  
	 * */
	public static Dialog CreateAlertDialog4(String title,String items[],boolean states[],Context context)
	{
		 return new AlertDialog.Builder(context)
         .setTitle(title)
         .setMultiChoiceItems(items,states,
                 new DialogInterface.OnMultiChoiceClickListener() {
                     public void onClick(DialogInterface dialog, int whichButton,
                             boolean isChecked) {

                         /* User clicked on a check box do some stuff */
                     }
                 })
//         .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
//             public void onClick(DialogInterface dialog, int whichButton) {
//
//                 /* User clicked Yes so do some stuff */
//             }
//         })
//         .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
//             public void onClick(DialogInterface dialog, int whichButton) {
//
//                 /* User clicked No so do some stuff */
//             }
//         })
        .create();
		
		
		
		
	}
	
	
	//==================================================================================
	/**
	 * Toast类型提示框
	 * */
	public static Toast CreateToast(Context context, String msg)
	{
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		return toast;
	}
//	Toast主要用来提示用户，以实现友好的用户体验，这里给出两个Toast的例子：
//
//	1、使用图片
//	Toast toast = new Toast(this); 
//
//	ImageView view = new ImageView(this); 
//
//	view.setImageResource(R.drawable.icon); 
//
//	toast.setView(view); 
//
//	toast.show(); 
//
//
//	2、使用文字对话框
//	Toast toast = Toast.makeText(this, "lalalal", Toast.LENGTH_LONG); 
//
//	View textView = toast.getView(); 
//
//	LinearLayout lay = new LinearLayout(this); 
//
//	lay.setOrientation(LinearLayout.HORIZONTAL); 
//
//	ImageView view = new ImageView(this); 
//
//	view.setImageResource(R.drawable.icon); 
//
//	lay.addView(view); 
//
//	lay.addView(textView); 
//
//	toast.setView(lay); 
//
//	toast.show(); 


	
	//=============================================================================
	

}
