/**
 * 
 */
package com.qingfengweb.client.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.qingfengweb.android.R;
import com.qingfengweb.client.adapter.ViewPagerAdapter;
import com.qingfengweb.client.bean.DetailServicesInfo;
import com.qingfengweb.client.bean.UpdateSystemTime;
import com.qingfengweb.client.data.AccessServer;
import com.qingfengweb.client.data.FinalValues;
import com.qingfengweb.client.data.MyApplication;
import com.qingfengweb.client.database.DBHelper;
import com.qingfengweb.client.database.SQLiteHelper;
import com.qingfengweb.filedownload.CallbackImpl;
import com.qingfengweb.filedownload.FileUtils;
import com.qingfengweb.filedownload.ImageLoadFromUrlOrId;

/**
 * @author qingfeng
 *
 */
public class DetailServiceActivity extends Activity implements OnClickListener{
	private ImageButton backBtn;
	private LinearLayout contentLayout;
	ImageLoadFromUrlOrId imageLoad = new ImageLoadFromUrlOrId();
	TextView titleTv;
	LinearLayout proLayout;
	//����������ά�����ǷŲ���������滻��ͼƬ��Դ�ģ�
//	private static String format ="\\[img\\s+id\\=\\\"?(\\d+(\\,\\d+)*)\\\"?\\s*\\]";/*"\\[img(\\s+(\\w+)\\=\\\"?([\\d,]+)\\\"?)*\\s*\\]";*/
	DBHelper dbHelper = null;
	private String currentType = "1";//��ǰ�ķ�������
	private String[] titleStr = {"iOS���ƿ���","Android���ƿ���","WindowsPhone���ƿ���","HTML5��վ���ƿ���","��ͳ��վ���ƿ���","��Ŀ��Ӫ���ƹ�"};
	String contentStr = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_detailservices);
		currentType = getIntent().getStringExtra("type");
		findview();
		dbHelper = DBHelper.getInstance(this);
		new Thread(getDataFromLocalRunnable).start();
	}
	/**
	 * �ӱ��ػ�ȡ����
	 */
Runnable getDataFromLocalRunnable = new Runnable() {
	
	@Override
	public void run() {
		String sql = "select * from "+DetailServicesInfo.TableName+" where type = "+currentType;
		List<Map<String, Object>> list = dbHelper.selectRow(sql, null);
		if(list!=null && list.size()>0){
			contentStr = list.get(0).get("content").toString().trim();
		}
		Message msg = new Message();
		msg.what = 0;
		msg.obj = list;
		handler.sendMessage(msg);
	}
};

Runnable getDataFromServiceRunnable = new Runnable() {
	
	@Override
	public void run() {
		String msg = AccessServer.getContentDetail(currentType);
		if(isNoData(msg)){//������
			handler.sendEmptyMessage(2);
		}else if(msg.equals("404")){
			handler.sendEmptyMessage(3);
		}else{
			ContentValues contentValues = new ContentValues();
			if(contentStr!=null && !msg.equals(contentStr)){
				contentValues.put("type", currentType);
				contentValues.put("content", msg);
				int a = dbHelper.open().update(DetailServicesInfo.TableName, contentValues, "type=?", new String[]{currentType});
				if(a == 0){
					dbHelper.open().insert(DetailServicesInfo.TableName, null, contentValues);
				}
				System.out.println(msg);
				Message msg1 = new Message();
				msg1.obj = msg;
				msg1.what = 1;
				handler.sendMessage(msg1);
			}else if(contentStr == null){
				dbHelper.insert(DetailServicesInfo.TableName, contentValues);
				System.out.println(msg);
				Message msg1 = new Message();
				msg1.obj = msg;
				msg1.what = 1;
				handler.sendMessage(msg1);
			}
			
		}
	}
};
Handler handler = new Handler(){

	@SuppressLint("ShowToast")
	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message msg) {
		if(msg.what == 0){//�������ݲ�ѯ���
			if(contentStr!=null && contentStr.length()>0){//��������������ʾ���ص�����
				handleData(contentStr);
				proLayout.setVisibility(View.GONE);
				new Thread(getDataFromServiceRunnable).start();
			}else{//���û����ȥ��������ȡ
				proLayout.setVisibility(View.VISIBLE);
				new Thread(getDataFromServiceRunnable).start();
			}
		}else if(msg.what == 1){//��������ȡ�����ݽ��
			String str = (String) msg.obj;
			handleData(str);
			proLayout.setVisibility(View.GONE);
		}else if(msg.what == 3){//��������ȡ����ʧ��
			Toast.makeText(getApplicationContext(), "���ʷ�����ʧ�ܣ�", 3000).show();
			proLayout.setVisibility(View.GONE);
		}else{//������������
//			Toast.makeText(getApplicationContext(), "������", 3000).show();
			proLayout.setVisibility(View.GONE);
		}
		
		super.handleMessage(msg);
	}
	
};
	/**
	 * �жϷ���������ֵ�Ƿ�Ϊ�����ݵĸ�ʽ
	 * @param str
	 * @return
	 */
	private boolean isNoData(String str){
		String format = "\\d+\\,20[1,2]\\d-((0?[0-9])|1[0-2])-(([0-2][0-9])|3[0-1])(\\s([0-1][0-9]|2[0-4]):([0-5][0-9])(:([0-5][0-9]))?)?";
		Matcher matcher = Pattern.compile(format).matcher(str);
		while(matcher.find()){
			return true;
		}
		return false;
	}
	/**
	 * ��������ݿ���ߴ��������ص����е����ݣ����û�����ݣ���Ҫ�������
	 * @param data
	 */
	private void handleData(String data){
		//ͨ��������ʽƥ�������ֵ�е�ͼƬ������ֵ���浽�����С�
				ArrayList<HashMap<String,String>> dataList = selectImageId(data);
				for(int i=0;i<dataList.size();i++){//���������е�ͼƬ����  ���ϵĵ�Ԫ��������ͼƬ����ĸ���
					int currentIndex = data.indexOf(dataList.get(i).get("img"));//�õ���ǰƥ������ݶ��������е�λ��
					//�õ���һ��ƥ������ݶ��������е�λ��
					int previousIndex = data.indexOf(dataList.get(i>0?i-1:0).get("img"))+dataList.get(i>0?i-1:0).get("img").length();
					if(currentIndex<=previousIndex){
						previousIndex = currentIndex;
					}
					//�õ�ƥ������ݶ�ǰ���һ�β�ƥ�������
					String content = data.substring(i==0?0:previousIndex, currentIndex);
					if(content!=null && content.trim().length()>0){
						TextView tv = new TextView(getApplicationContext());
						tv.setTextColor(Color.BLACK);
						tv.setText(content.trim());
						contentLayout.addView(tv);
					}
					//���õ����Զ��ŷָ��id�ַ����ָ��id����
					String[] imgStr = dataList.get(i).get("imgId").split(",");
					if(imgStr!=null && imgStr.length>0){
						ArrayList<View> pageViews = new ArrayList<View>();  
						for(int img=0;img<imgStr.length;img++){//����id����
							ImageView imageView = new ImageView(getApplicationContext());
							//��ΪͼƬ����id�����ġ����Կ������ж�һ�¸�ͼƬ�ڱ����Ƿ����
							Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.SDPATH+FinalValues.SERVICES_IMG+imgStr[img]+".png");
							if(bitmap!=null){//���������ֱ�ӽ������ø��µ�ImageView�ؼ�
								imageView.setImageBitmap(bitmap);
								//��ͼƬ�Ŀ�͸߷���һ��Bundle�����ø�ImageView��Tag,��������������������ı���������ؼ�����ʵ�߶�
								Bundle bundle = new Bundle();
								bundle.putInt("height", bitmap.getHeight());
								bundle.putInt("width", bitmap.getWidth());
								imageView.setTag(bundle);
							}else{
								//���ͼƬ�ڱ��ز����ڣ������id׼��ȥ���������ء�
								CallbackImpl callbackImpl = new CallbackImpl(this,imageView);
								List<NameValuePair> list = new ArrayList<NameValuePair>();
								list.add(new BasicNameValuePair("appid", AccessServer.APPID));
								list.add(new BasicNameValuePair("appkey", AccessServer.APPKEY));
								list.add(new BasicNameValuePair("action", AccessServer.DOWNLOAD_IMAGE_DETAIL_ACTION));
								list.add(new BasicNameValuePair("imageid", imgStr[img]));
								list.add(new BasicNameValuePair("type", "1"));
								imageLoad.loadImageFromId(DetailServiceActivity.this, AccessServer.CONTENT_INTERFACE,
										list, R.drawable.xx, FinalValues.SERVICES_IMG, callbackImpl,true,false,MyApplication.getInstant().getScreenW(),0,0);
							}
							pageViews.add(imageView);//���²����Ŀؼ����뼯���У���ViewPager��Adapter��
						}
						contentLayout.addView(getViewPager(pageViews));//����һ���µ�ViewPager�����뵽ͼƬ������ȥ��
					}
					if(i == dataList.size()-1){//��һ����Ϊ�˻�ȡ���һ��ͼƬ����ĺ���Ŀ����е�����
						String lastContent = data.substring(currentIndex+dataList.get(i).get("img").length());
						if(lastContent!=null && lastContent.length()>0){//ֻ���������ݵ�ʱ��������µĿؼ���
							TextView lastTv = new TextView(getApplicationContext());
							lastTv.setTextColor(Color.BLACK);
							lastTv.setText(lastContent);
							contentLayout.addView(lastTv);
						}
					}
				}
	}
	/**
	 * �õ�һ��ViewPager
	 * @param pageViews
	 * @return
	 */
	@SuppressLint("InlinedApi")
	private View getViewPager(ArrayList<View> pageViews){
		View view = LayoutInflater.from(this).inflate(R.layout.view_viewpager, null);
		ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
		LinearLayout dotLayout = (LinearLayout) view.findViewById(R.id.dotLayout);
		int height = 0;
		int width = 0;
		for(int i=0;i<pageViews.size();i++){//���ViewPager����߿ؼ��ĸ߶ȣ�������ͨ�������еĿؼ����ݹ����Ĳ�������߶ȵ�
			Bundle bundle = (Bundle)pageViews.get(i).getTag();
			if(bundle!=null){
				int h = bundle.getInt("height");
				int w = bundle.getInt("width");
				int currentW = MyApplication.getInstant().getScreenW()-20;
				height = h*currentW/w;
			}
			LinearLayout.LayoutParams dotParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			dotParam.setMargins(0, 0, 10, 0);
			ImageView dotImage = new ImageView(this);
			dotImage.setBackgroundResource(R.drawable.qf_ad_dot1);
			dotImage.setLayoutParams(dotParam);
			dotLayout.addView(dotImage);
		}
		if(dotLayout.getChildCount()>0){
			((ImageView)(dotLayout.getChildAt(0))).setBackgroundResource(R.drawable.qf_ad_dot3);
		}
		RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, height);
		viewPager.setLayoutParams(param);//ΪViewPager���ø߶ȣ���������õĻ�����ͼƬ�Ѿ����ڵ������ViewPager����ʾ������
		viewPager.setAdapter(new ViewPagerAdapter(pageViews));//��ViewPager����������
		viewPager.setOnPageChangeListener(new CasePageChangeListener(dotLayout));
		return view;
	}
	/**
	 * �ӷ���ֵ���ҵ���Ҫ��ͼƬID 
	 * @param str
	 * @return ������ƥ����������ݶ�   ��ͼƬid����
	 */
	private ArrayList<HashMap<String,String>> selectImageId(String str){
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
		Pattern pattern = Pattern.compile("\\[(\\w+)(\\s+(\\w+)\\=\\\"?([\\w,]+)\\\"?)*\\s*\\]");
		Matcher m = pattern.matcher(str);
		while(m.find()) {
			HashMap<String,String> map = new HashMap<String, String>();
			map.put("img", m.group(0));
			if(m.group(1).equals("img")) {
				Pattern pattern2 = Pattern.compile("(\\w+)\\=\\\"?([\\w,]*)\\\"?");
				Matcher m2 = pattern2.matcher(m.group());
				while(m2.find()) {
					System.out.println("group:"+m2.group());
					for(int i=0; i<=m2.groupCount(); i++) {
						System.out.println("group"+i+":"+m2.group(i));
						if(m2.group(1).equals("id")) {
							System.out.println("�ҵ�idֵΪ:"+m2.group(2));
							map.put("imgId", m2.group(2));
						}
					}
				}
			}
			list.add(map);
		}
		return list;
	}
	 // ָ��ҳ������¼�������
    class CasePageChangeListener implements OnPageChangeListener {  
    	LinearLayout dotLayout;
    	  public CasePageChangeListener(LinearLayout dotLayout) {
    		  this.dotLayout = dotLayout;
		}
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
        	for(int i=0;i<dotLayout.getChildCount();i++){
        		if(arg0 == i){
        			dotLayout.getChildAt(i).setBackgroundResource(R.drawable.qf_ad_dot3);
        		}else{
        			dotLayout.getChildAt(i).setBackgroundResource(R.drawable.qf_ad_dot1);
        		}
        	}
        }  
    }  
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
			overridePendingTransition(R.anim.back_enter,R.anim.back_exit);
		}
	}
	private void findview(){
		backBtn = (ImageButton) findViewById(R.id.serviceBackBtn);
		backBtn.setOnClickListener(this);
		contentLayout = (LinearLayout) findViewById(R.id.contentLayout);
		titleTv = (TextView) findViewById(R.id.titleTv);
		titleTv.setText(titleStr[Integer.parseInt(currentType)-1]);
		proLayout = (LinearLayout) findViewById(R.id.proLayout);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			overridePendingTransition(R.anim.back_enter,R.anim.back_exit);
		}
		return true;
	}
//	public static CharSequence addSmileySpans(Context mContext,
//  CharSequence text) {
////֮���Բ���stringbuffer����Ϊ����������Խ�object��ӽ�ȥ
//SpannableStringBuilder builder = new SpannableStringBuilder(text);
//
//for (int i = 0; i < FilterUbbs.length; i++) {//ѭ�����������еı�����в����滻
//  //����ƥ��
//  Matcher matcher = Pattern.compile(FilterUbbs[i][1]).matcher(text);
//  
//  while (matcher.find()) {//����ƥ�������
//  	String a = matcher.group(0);
//      String b =  matcher.group(1);
//      System.out.println(a);
//      System.out.println(b);
//          int resId = Integer.parseInt(FilterUbbs[i][0]);//��Ҫ�滻��ͼƬ����ԴID
//          //���ģ�����Ҫ������仰���������滻��ͼƬ
//          builder.setSpan(imageSpan,
//                          matcher.start(), matcher.end(),
//                          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//  }
//}
//
//return builder;
//}
//
	
//	private ArrayList<HashMap<String,String>> selectImage(String str){
//	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
//	Matcher matcher = Pattern.compile(format).matcher(str);
//	while(matcher.find()){
//		HashMap<String,String> map = new HashMap<String, String>();
//		map.put("img", matcher.group(0));
//		map.put("imgId", matcher.group(2));
//		list.add(map);
//	}
//	return list;
//}
}
