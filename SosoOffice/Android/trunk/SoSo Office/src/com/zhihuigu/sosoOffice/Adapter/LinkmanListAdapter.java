/**
 * 
 */
package com.zhihuigu.sosoOffice.Adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhihuigu.sosoOffice.DetailDemandActivity;
import com.zhihuigu.sosoOffice.DetailLinkManActivity;
import com.zhihuigu.sosoOffice.DetailPersonalDataActivity;
import com.zhihuigu.sosoOffice.R;
import com.zhihuigu.sosoOffice.WriteNewLetterActivity;
import com.zhihuigu.sosoOffice.constant.ErrorType;
import com.zhihuigu.sosoOffice.constant.MyApplication;
import com.zhihuigu.sosoOffice.database.DBHelper;
import com.zhihuigu.sosoOffice.model.Messages;
import com.zhihuigu.sosoOffice.model.SoSoContactInfo;
import com.zhihuigu.sosoOffice.thread.SoSoUploadData;
import com.zhihuigu.sosoOffice.utils.ChineseToEnglish;
import com.zhihuigu.sosoOffice.utils.CommonUtils;
import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.NetworkCheck;
import com.zhihuigu.sosoOffice.utils.StringUtils;

/**
 * @author ������
 * @createDate 2013/1/14
 * ��ϵ���б�������
 *
 */
public class LinkmanListAdapter extends BaseAdapter implements OnClickListener{
	private LayoutInflater inflater;
	private Activity c;
	private ArrayList<Map<String, Object>> list;
	private List<Object> linearList = new ArrayList<Object>();
	
	private SoSoUploadData uploaddata;// �������������
	private boolean click_limit = true;// ��ť������ƣ���ֹ�ظ��ύ����
	private String reponse = "";// �ӷ�������ȡ��Ӧֵ
	private boolean runnable_tag = false;// �ж������Ƿ����û�����ֹͣ
	private ProgressDialog progressdialog = null;
	private int position = 0;
	private int activityFlag = 0;////0�������ҳ������ 1�����д����������  2�����������������
	public ArrayList<Messages> messageList = new ArrayList<Messages>();
	
	public boolean delete = true;
	public LinkmanListAdapter(Activity context,
			ArrayList<Map<String, Object>> webNameArr,int activityFlag) {
		this.inflater = LayoutInflater.from(context);
		this.list = webNameArr;
		this.activityFlag = activityFlag;
		if(activityFlag == 2){
			this.c = context;
		}else{
			this.c = context.getParent();
		}
		for(int i=0;i<list.size();i++){
			messageList.add(new Messages());
		}
	}
	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_linkmanlist, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.zimuText = (TextView) convertView.findViewById(R.id.text_first_char_hint);
			holder.headImage = (ImageView) convertView.findViewById(R.id.headImage);
//			holder.remark = (TextView) convertView.findViewById(R.id.remark);
			holder.btnLinear = (LinearLayout) convertView.findViewById(R.id.btnlinear);
			holder.lookDetailLayout = (LinearLayout) convertView.findViewById(R.id.lookDetailLayout);
			holder.sendLetterLayout = (LinearLayout) convertView.findViewById(R.id.sendLetterLayout);
			holder.deleteLayout = (LinearLayout) convertView.findViewById(R.id.deleteLayout);
			holder.itemLayout = (LinearLayout) convertView.findViewById(R.id.itemLayout);
			holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
			holder.deleteText = (TextView) convertView.findViewById(R.id.deleteText);
			holder.addLinkMan = (ImageView) convertView.findViewById(R.id.addLinkMan);
			convertView.setTag(holder);
		}else{
			holder =  (ViewHolder) convertView.getTag();
		}
		final Messages msg = messageList.get(position);  
		holder.checkBox.setChecked(msg.isChecked);  
        //ע���������õĲ���onCheckedChangListener������ֵ��˼��һ�µ�  
		holder.checkBox.setOnClickListener(new OnClickListener() {  
                  
                public void onClick(View v) {  
                        if(msg.isChecked){  
                                msg.isChecked = false;  
                        }else{  
                                msg.isChecked = true;  
                        }  
                          
                }  
        });  
		
		holder.setViewContent(position);
		return convertView;
	}

	// �̳�BaseAdapter������ListViewÿ�е�����
	public final class ViewHolder{
		public TextView zimuText;
		public TextView name;
//		public TextView remark;
		public ImageView headImage;
		public LinearLayout lookDetailLayout;
		public LinearLayout sendLetterLayout ;
		public LinearLayout deleteLayout;
		public LinearLayout btnLinear;
		public LinearLayout itemLayout;
		public CheckBox checkBox;
		public TextView deleteText;
		public ImageView addLinkMan;
		public void setViewContent(int position){
			Map<String,Object> map = list.get(position);
			name.setText(map.get("name").toString());
//			remark.setText(map.get("remark").toString());
			headImage.setImageResource((Integer)map.get("headImageId"));
			String zimu = ChineseToEnglish.toEnglish(map.get("name").toString().charAt(0)).substring(0,1).toUpperCase();
			zimuText.setText(zimu);
			btnLinear.setTag(position);
			checkBox.setTag(position);
			lookDetailLayout.setTag(position);
			sendLetterLayout.setTag(position);
			deleteLayout.setTag(position);
			lookDetailLayout.setOnClickListener(LinkmanListAdapter.this);
			sendLetterLayout.setOnClickListener(LinkmanListAdapter.this);
			deleteLayout.setOnClickListener(LinkmanListAdapter.this);
			btnLinear.setVisibility(View.GONE);
			if(activityFlag == 0 || activityFlag == 2){
				checkBox.setVisibility(View.GONE);
				itemLayout.setTag(btnLinear);
				linearList.add((View)itemLayout.getTag());
			}
			//������ͬ������ĸ��ͬ�Ŀؼ�����
			if(position>0){
				String zimu1 = ChineseToEnglish.toEnglish(list.get(position-1).get("name").toString().charAt(0)).substring(0,1).toUpperCase();
				if(zimu.equals(zimu1)){
					zimuText.setVisibility(View.GONE);
				}else{
					zimuText.setVisibility(View.VISIBLE);
				}
			}else if(position == 0){
				zimuText.setVisibility(View.VISIBLE);
			}
			
			if(activityFlag == 0){//����ҳ��������
				//�������ʱ��ʾ��Ӧ����
					itemLayout.setOnClickListener(new ItemClickListener());
			}else if(activityFlag == 1){//�ӷ���վ������ת������
				final Messages msg = messageList.get(position);  
				name.setTag(checkBox);
				name.setOnClickListener(new OnClickListener() {  
		            
		            public void onClick(View v) {  
		                    if(msg.isChecked){  
		                            msg.isChecked = false;  
		                            ((CompoundButton) v.getTag()).setChecked(msg.isChecked);  
		                    }else{  
		                            msg.isChecked = true;  
		                            ((CompoundButton) v.getTag()).setChecked(msg.isChecked);  
		                    }  
		                      
		            }  
		    });  
			}else if(activityFlag == 2){//2�����������������
				//�������ʱ��ʾ��Ӧ����
				if(list.get(position).get("contactuserid")!=null&&list.get(position).get("contactuserid").toString().equals(MyApplication.getInstance(c)
										.getSosouserinfo(c).getUserID())){
					itemLayout.setOnClickListener(new ItemClickListener());
					lookDetailLayout.setVisibility(View.GONE);
					deleteText.setText("��Ϊ��ϵ��");
					delete = false;
					deleteLayout.setVisibility(View.GONE);
//					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//					params.setMargins(30, 0, 0, 0);
//					sendLetterLayout.setLayoutParams(params);
				}else{
					itemLayout.setOnClickListener(new ItemClickListener());
					lookDetailLayout.setVisibility(View.GONE);
					deleteText.setText("��Ϊ��ϵ��");
					addLinkMan.setBackgroundResource(R.drawable.soso_bg_contact_add);
					delete = false;
//					deleteLayout.setVisibility(View.GONE);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					params.setMargins(10, 0, 0, 0);
					deleteLayout.setLayoutParams(params);
				}
				
			}
			
		}
		
	}
	/**
	 * 
	 * @author  ������
	 * @createDate 2013/2/7
	 * item�ĵ���¼�
	 *
	 */
	private boolean btnState = false;
	class ItemClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(btnState){
				LinkmanListAdapter.this.notifyDataSetChanged();
//				LinkmanListAdapter.this.notifyAll();
				btnState = false;
			}else{
				if(activityFlag == 0 || activityFlag == 2){//����ҳ��������
					View view = (View)v.getTag();
					for(int i = 0;i<linearList.size();i++){
						if(view == (View)linearList.get(i)){
//							if(((View)linearList.get(i)).getVisibility() == View.VISIBLE){
////								((View)linearList.get(i)).setVisibility(View.GONE);
////								LinkmanListAdapter.this.notifyDataSetChanged();
//								btnState = true;
//							}else{
								((View)linearList.get(i)).setVisibility(View.VISIBLE);
								btnState = true;
//							}
						}else{
							((View)linearList.get(i)).setVisibility(View.GONE);
						}
					}
				}
			}
		
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		position = (Integer)v.getTag();
		if(v.getId()==R.id.lookDetailLayout){
			Intent intent = new Intent(c,DetailLinkManActivity.class);
			Bundle bundle = new Bundle();
//			"contactid text," + // ��ϵ��id
//			"contactuserid text," + // ��ϵ���û�id
//			"contactroleid text," + // ��ϵ���û���ɫid
//			"userid text,"+//�û�id
//			"username text,"+//�û���
//			"realname text," + // ��������
//			"headimage text," + // �û�ͼ������λ��
//			"headimagesd text," + // �û�ͼ��sd��λ��
//			"dictid text," + // ����id
//			"adddate text," + // ���ʱ��
//			"isused text" + // �Ƿ�ɾ����1ɾ��
			
			bundle.putString("path","");
			bundle.putString("realname", list.get(position).get("realname").toString());
			bundle.putString("gender", list.get(position).get("sex").toString());
			bundle.putString("birthday", list.get(position).get("birthday").toString());
			bundle.putString("company", list.get(position).get("company").toString());
			intent.putExtra("personalInfo",bundle);
			MyApplication.getInstance().setLinkManBack(true);
			c.startActivity(intent);	
			
		}else if(v.getId()==R.id.sendLetterLayout){
			Bundle bundle = new Bundle();
			bundle.putString("shoujianren", list.get(position).get("name").toString());
			bundle.putString("shoujianrenid", list.get(position).get("contactuserid").toString());
			bundle.putString("theme", "");
			bundle.putString("content", "");
			bundle.putString("WhoUserID", "");
			Intent intent  = new Intent(c,WriteNewLetterActivity.class);
			intent.putExtra("bundle", bundle);
			MyApplication.getInstance().setLinkManBack(true);
			c.startActivity(intent);
		}else if(v.getId()==R.id.deleteLayout){
			if(delete){//��ɾ������
			AlertDialog.Builder builder = new AlertDialog.Builder(
					c);
			builder.setMessage("ȷ��Ҫɾ������ϵ����")
					.setTitle(c.getResources().getString(R.string.prompt))
					.setCancelable(false)
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									new Thread(runnable).start();
								}
							}).setNegativeButton("ȡ��", null);
			AlertDialog alert = builder.create();
			alert.show();
			
			}else{//�������ϵ�˹���
				AlertDialog.Builder builder = new AlertDialog.Builder(c);
				builder.setMessage("ȷ��Ҫ��Ϊ��ϵ����")
						.setTitle(c.getResources().getString(R.string.prompt))
						.setCancelable(false)
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										new Thread(runnable).start();
									}
								}).setNegativeButton("ȡ��", null);
				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}
	
	
	/**
	 * ɾ����ϵ��
	 * true,ɾ���ɹ���false ɾ��ʧ��
	 */
	public boolean deleteLinkMan(int index) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				c).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				c).getAPPKEY()));
		params.add(new BasicNameValuePair("ContactID", list.get(index).get("contactid").toString()));
		uploaddata = new SoSoUploadData(c, "UserContactDelete.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(index,list.get(index).get("contactid").toString());
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * �����ϵ��
	 * true,��ӳɹ���false ���ʧ��
	 */
	public boolean addLinkMan(int index) {
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("appid", MyApplication.getInstance(
				c).getAPPID()));
		params.add(new BasicNameValuePair("appkey", MyApplication.getInstance(
				c).getAPPKEY()));
		params.add(new BasicNameValuePair("DictID", "0"));
		params.add(new BasicNameValuePair("UserID", MyApplication.getInstance(c).getSosouserinfo(c).getUserID()));
		params.add(new BasicNameValuePair("ContactUserID", list.get(index).get("contactuserid").toString()));
		uploaddata = new SoSoUploadData(c, "UserContactAdd.aspx", params);
		uploaddata.Post();
		reponse = uploaddata.getReponse();
		dealReponse(index);
		params.clear();
		params = null;
		if (StringUtils.CheckReponse(reponse)) {
			return true;
		} else {
			return false;
		}
	}
	
	/***
	 * author by Ring ����ɾ���ż���������Ӧֵ
	 */

	public void dealReponse(int index) {
		if (StringUtils.CheckReponse(reponse)) {
			Gson gson = new Gson();
			ContentValues values = new ContentValues();
			SoSoContactInfo sosocontactinfo = null;
			try {
				sosocontactinfo = gson.fromJson(reponse, SoSoContactInfo.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(sosocontactinfo!=null){
				values.put("contactid", sosocontactinfo.getContactUserID());
				values.put("contactuserid",list.get(index).get("contactuserid").toString());
				values.put("contactroleid","");
				values.put("userid", MyApplication.getInstance(c).getSosouserinfo(c).getUserID());
				values.put("username", MyApplication.getInstance(c).getSosouserinfo(c).getUserName());
				values.put("realname", "");
				values.put("headimage", "");
				values.put("dictid", "0");
				values.put("adddate", "");
				values.put("sex", "");
				values.put("birthday", "");
				values.put("company", "");
				List<Map<String, Object>> selectresult = null;
				selectresult = DBHelper.getInstance(c).selectRow(
						"select * from sosocontactinfo where contactid = '"
								+ sosocontactinfo.getContactID() + "'",
						null);
				if (selectresult != null) {
					if (selectresult.size() <= 0) {
						DBHelper.getInstance(c).insert(
								"sosocontactinfo", values);
					} else {
						DBHelper.getInstance(c).update(
								"sosocontactinfo",
								values,
								"contactid = ?",
								new String[] { sosocontactinfo
										.getContactID() });
					}
					selectresult.clear();
					selectresult = null;
				}
				values.clear();
			}
			if (values != null) {
				values.clear();
				values = null;
			}
			
			DBHelper.getInstance(c).close();
		}
	}
	/***
	 * author by Ring ����ɾ���ż���������Ӧֵ
	 */

	public void dealReponse(int index,String contactid) {
		boolean b = false;
			if (StringUtils.CheckReponse(reponse)) {
				b=DBHelper.getInstance(c).delete("sosocontactinfo", "contactid=?", new String[]{contactid});
				if(b){
					list.remove(index);
				}
			}
	}
	/**
	 * author by Ring ����ɾ���ż���ʱ����
	 */
	public Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (click_limit) {
				click_limit = false;
			} else {
				return;
			}
			if (NetworkCheck.IsHaveInternet(c)) {
				boolean b= false;
				handler.sendEmptyMessage(5);
				if(activityFlag == 2){//2�����������������
					b= addLinkMan(position);
				}else{
					b= deleteLinkMan(position);
				}
				
				handler.sendEmptyMessage(6);
				if (runnable_tag) {
					runnable_tag = true;
					click_limit = true;
					return;
				}
				if (b) {
					handler.sendEmptyMessage(7);// ɾ����ϵ�˳ɹ�
				} else {
					handler.sendEmptyMessage(3);// ɾ����ϵ��ʧ��
				}
			} else {
				handler.sendEmptyMessage(4);// û������ʱ���û���ʾ

			}

			click_limit = true;
		}
	};
	
	/**
	 * author by Ring ����ҳ����ת����
	 */
	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Intent i = new Intent();
			switch (msg.what) {
			case 1:// ɾ���ɹ��������Ի�����ʾ�û�
//				MessageBox.CreateAlertDialog(context.getResources().getString(R.string.deletelettlesuccess),
//						context);
//				notifyView();
				break;
			case 2:// ��ע�������ת����¼����
//				i.setClass(context, LoginActivity.class);
//				context.startActivity(i);
//				context.finish();
				break;
			case 3:// ע��ʧ�ܸ��û���ʾ
				String message = "";
				if (StringUtils.getErrorState(reponse).equals(ErrorType.SoSoTimeOut.getValue())) {
					message = c.getResources().getString(
							R.string.progress_timeout);
				} else if (StringUtils.getErrorState(reponse).equals(ErrorType.AddLinkAgain.getValue())) {
					message = "������ӹ����û�Ϊ��ϵ�ˣ�";
				} else {
					if(activityFlag == 2){//2�����������������
						message = "�����ϵ��ʧ�ܣ����Ժ����ԣ�";
					}else{
						message = c.getResources().getString(R.string.deletelinkman_failure);
					}
				}
				MessageBox.CreateAlertDialog(message,
						c);
				break;
			case 4:// û������ʱ���û���ʾ
				MessageBox.CreateAlertDialog(c);
				break;
			case 5:// �򿪽�����
				progressdialog = new ProgressDialog(c);
				progressdialog.setMessage(c.getResources().getString(
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
			case 6:// �رս�����
				if (progressdialog != null && progressdialog.isShowing()) {
					progressdialog.dismiss();
				}
				break;
			case 7:// ���/ɾ����ϵ�˳ɹ�
				if(activityFlag == 2){//2�����������������
					MessageBox.CreateAlertDialog("�����ϵ�˳ɹ���",
							c);
				}else{
					MessageBox.CreateAlertDialog(c.getResources().getString(
							R.string.deletelinkman_success),
							c);
					LinkmanListAdapter.this.notifyDataSetChanged();
				}
				
				break;
			case 8:// ���/ɾ����ϵ��ʧ��
				if(activityFlag == 2){//2�����������������
					MessageBox.CreateAlertDialog("�����ϵ��ʧ�ܣ�",
							c);
				}else{
					MessageBox.CreateAlertDialog(c.getResources().getString(
							R.string.deletecollectroom_failure),
							c);
				}
				
				break;
			}
		};
	};
}
