/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.zhihuigu.sosoOffice.DetailRoomInfoActivity;
import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.Adapter.RoomManagerAdatper.ViewHolder;
import com.zhihuigu.sosoOffice.View.ScrollLayoutView;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * @author 刘星星
 * @createDate 2013/1/18
 * 房源管理的楼层中的gridView控件的适配器
 *
 */
public class RoomManagerGridViewAdapter extends BaseAdapter{

	Context context;
	ArrayList<HashMap<String,Object>> list;
	
	ArrayList<SoSoUploadData> listrequest = new ArrayList<SoSoUploadData>();//请求列表
	
	
	private SoSoUploadData uploaddata;// 服务器请求对象
	private boolean click_limit = true;// 按钮点击限制，防止重复提交动作
	private String reponse = "";// 从服务器获取响应值
	private boolean runnable_tag = false;// 判断请求是否是用户主动停止
	private ProgressDialog progressdialog = null;
	private int position = 0;
	public int scrollWidth = 0;
	
	public RoomManagerGridViewAdapter(Context context,ArrayList<HashMap<String,Object>> list) {
		this.context = context;
		this.list = list ;
		int i;
		scrollWidth = Integer.parseInt(context.getResources().getString(R.string.room_scroll_width));
		for(i=0;i<list.size();i++){
			listrequest.add(i, null);
		}
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_roomgridview, null);
			holder.roomNumber = (TextView) convertView.findViewById(R.id.roomNumberText);
			holder.scrollView = (ScrollLayoutView) convertView.findViewById(R.id.scrollView);
			holder.menImg = (ImageView) convertView.findViewById(R.id.menImg);
			holder.rightLayout = (LinearLayout) convertView.findViewById(R.id.roomRight);
			RelativeLayout.LayoutParams param= new RelativeLayout.LayoutParams(scrollWidth, LayoutParams.WRAP_CONTENT);
			holder.scrollView.setLayoutParams(param);
			holder.menImg.setLayoutParams(param);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.setViewContent(position);
		return convertView;
	}
	
	class ViewHolder{
		TextView roomNumber;
		ScrollLayoutView scrollView;
		AlertDialog dialog = null;
		ImageView menImg;
		LinearLayout rightLayout;
		private float currentX = 0;
		Handler handler = new Handler();
		Runnable runnable  = new Runnable() {
			
			@Override
			public void run() {
				int width = menImg.getWidth();
				scrollView.smoothScrollTo(width, 0);
			}
		};
		public void setViewContent(int position){
			HashMap<String,Object> map = list.get(position);
			String roomNumberStr = map.get("officemc").toString();
			String state = map.get("officestate").toString();
			if(roomNumberStr!=null && roomNumberStr.length()>5){
				roomNumberStr = roomNumberStr.substring(0, 5)+"...";
			}
			roomNumber.setText(roomNumberStr);
			int width = menImg.getWidth();
			if(state.equals("1")){//已租
				handler.post(runnable);
				roomNumber.setBackgroundColor(Color.rgb(194,194,194));
			}else if(state.equals("0")){//未租
				scrollView.smoothScrollTo(width, 0);
				roomNumber.setBackgroundColor(Color.rgb(180,216,32));
			}
			roomNumber.setTag(position);
			rightLayout.setTag(position);
			rightLayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int position = (Integer) v.getTag();
					MyApplication.getInstance().setOfficeid(list.get(position).get("officeid").toString());
					Intent intent = new Intent(context, DetailRoomInfoActivity.class);
					context.startActivity(intent);
				}
			});
			roomNumber.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int position = (Integer) v.getTag();
					MyApplication.getInstance().setOfficeid(list.get(position).get("officeid").toString());
					Intent intent = new Intent(context, DetailRoomInfoActivity.class);
					context.startActivity(intent);
				}
			});
			scrollView.setTag(position);
			scrollView.setOnTouchListener(new OnTouchListener() {
				private boolean boo = true;
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					try {
						RoomManagerGridViewAdapter.this.position  = (Integer)scrollView.getTag();
						int width = menImg.getWidth();
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							currentX = event.getX();
						} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
								/*if(scrollView.getScrollX()<=width/2){// 向右滚动
									boo = true;
								}else{// 向左滚动
									boo = false;
								}*/
							if(currentX > event.getX()){//已租
								scrollView.scrollTo(width, 0);
								roomNumber.setBackgroundColor(Color.rgb(194,194,194));
								
							}else if(currentX < event.getX()){//未租
								scrollView.scrollTo(0, 0);
								roomNumber.setBackgroundColor(Color.rgb(180,216,32));
								
							}
							currentX = event.getX();
						}else if(event.getAction() == MotionEvent.ACTION_UP){
							/*if(scrollView.getScrollX()<=width/2){// 向右滚动
								boo = true;
							}else{// 向左滚动
								boo = false;
							}
							if(boo){// 向右滚动
								scrollView.scrollTo(0, 0);
								roomNumber.setBackgroundColor(Color.rgb(180,216,32));
							}else{// 向左滚动
								scrollView.scrollTo(width, 0);
								roomNumber.setBackgroundColor(Color.rgb(194,194,194));
							}*/
							/*if(currentX > event.getX()){
								scrollView.scrollTo(width, 0);
							}else{
								scrollView.scrollTo(0, 0);
							}*/
							new Thread(runnable1).start();
							currentX = event.getX();
						}
						return false;

					} catch (Exception e) {
						return false;
					}
				}
			});
		}
	}
	
	/**
	 * 更新房源状态
	 * true,更新成功，false 更新失败
	 */
	public boolean updateOfficeState(int index) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				context).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				context).getAPPKEY()));
		String officestate = (list.get(index).get("officestate").equals("1"))?"0":"1";
		params.add(new BasicNameValuePair("OfficeID", list.get(index).get("officeid").toString()));
		params.add(new BasicNameValuePair("OfficeState", officestate));
		SoSoUploadData uploaddata = new SoSoUploadData(context, "OfficeUpdateState.aspx", params);
		listrequest.remove(index);
		listrequest.add(index, uploaddata);
		uploaddata.Post();
		if(uploaddata!=null){
			reponse = uploaddata.getReponse();
		}
		if(index<list.size()){
			dealReponse1(index,list.get(index).get("officeid").toString(),officestate);
		}
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	/***
	 * author by Ring 处理请求后的响应值
	 */

	public void dealReponse1(int index,String officeid,String officestate) {
		boolean b = false;
			if (StringUtils.CheckReponse(reponse)) {
				ContentValues values = new ContentValues();
				values.put("isrent", officestate);
				b=DBHelper.getInstance(context).update("soso_officeinfo", values, "officeid=?", new String[]{officeid});
				if(b&&index<list.size()){
					HashMap<String,Object> map = list.get(position);//根据position得到相应布局的数据
					HashMap<String,Object> newMap = new HashMap<String, Object>();
//					newMap.put("floors", map.get("floors"));
					list.get(index).put("officestate",officestate);//更新房源状态
//					newMap.put("officemc", map.get("officemc"));
//					newMap.put("priceup", map.get("priceup"));
//					newMap.put("acreage", map.get("acreage"));
//					newMap.put("floors", map.get("floors").toString());
//					newMap.put("officetype", map.get("officetype").toString());
//					newMap.put("pricedown", map.get("pricedown").toString());
//					newMap.put("areaup", map.get("areaup").toString());
//					list.remove(position);
//					list.add(position, newMap);
				}
			}
	}
	
	/**
	 * author by Ring 处理耗时操作
	 */
	public Runnable runnable1 = new Runnable() {
		@Override
		public void run() {
//			if (click_limit) {
//				click_limit = false;
//			} else {
//				return;
//			}
			uploaddata = listrequest.get(position);
			if (uploaddata != null) {
				uploaddata.overReponse();
				uploaddata = null;
			}
			if (NetworkCheck.IsHaveInternet(context)) {
				updateOfficeState(position);
			} else {
				handler.sendEmptyMessage(4);// 没有网络时给用户提示

			}

			click_limit = true;
		}
	};
	
	/**
	 * author by Ring 处理页面跳转动作
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// 删除成功，跳出对话框提示用户
				break;
			case 2:// 从注册界面跳转到登录界面
//				i.setClass(context, LoginActivity.class);
//				context.startActivity(i);
//				context.finish();
				break;
			case 3:// 推送失败给用户提示
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					message = context.getResources().getString(
							R.string.progress_timeout);
				} else {
					message = context.getResources().getString(R.string.pushroom_failure);
				}
				MessageBox.CreateAlertDialog(message,
						context);
				break;
			case 4:// 没有网络时给用户提示
				MessageBox.CreateAlertDialog(context);
				break;
			case 5:// 打开进度条
				progressdialog = new ProgressDialog(context);
				progressdialog.setMessage(context.getResources().getString(
						R.string.progress_message_submit));
				progressdialog.setCanceledOnTouchOutside(false);
				progressdialog.setCancelable(true);
				progressdialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						runnable_tag = true;
						if (uploaddata != null) {
							uploaddata.overReponse();
						}
						return false;
					}
				});
				progressdialog.show();
				break;
			case 6:// 关闭进度条
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			}
		};
	};
	
	
}
