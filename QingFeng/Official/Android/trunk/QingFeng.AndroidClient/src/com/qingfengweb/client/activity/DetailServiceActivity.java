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
	//下面的这个二维数组是放查找正则和替换的图片资源的：
//	private static String format ="\\[img\\s+id\\=\\\"?(\\d+(\\,\\d+)*)\\\"?\\s*\\]";/*"\\[img(\\s+(\\w+)\\=\\\"?([\\d,]+)\\\"?)*\\s*\\]";*/
	DBHelper dbHelper = null;
	private String currentType = "1";//当前的服务类型
	private String[] titleStr = {"iOS定制开发","Android定制开发","WindowsPhone定制开发","HTML5网站定制开发","传统网站定制开发","项目运营与推广"};
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
	 * 从本地获取详情
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
		if(isNoData(msg)){//无数据
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
		if(msg.what == 0){//本地数据查询结果
			if(contentStr!=null && contentStr.length()>0){//本地有数据先显示本地的数据
				handleData(contentStr);
				proLayout.setVisibility(View.GONE);
				new Thread(getDataFromServiceRunnable).start();
			}else{//如果没有则去服务器获取
				proLayout.setVisibility(View.VISIBLE);
				new Thread(getDataFromServiceRunnable).start();
			}
		}else if(msg.what == 1){//服务器获取的数据结果
			String str = (String) msg.obj;
			handleData(str);
			proLayout.setVisibility(View.GONE);
		}else if(msg.what == 3){//服务器获取数据失败
			Toast.makeText(getApplicationContext(), "访问服务器失败！", 3000).show();
			proLayout.setVisibility(View.GONE);
		}else{//服务器无数据
//			Toast.makeText(getApplicationContext(), "无数据", 3000).show();
			proLayout.setVisibility(View.GONE);
		}
		
		super.handleMessage(msg);
	}
	
};
	/**
	 * 判断服务器返回值是否为无数据的格式
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
	 * 处理从数据库或者从网上下载到的有的数据，如果没有数据，不要经过这里。
	 * @param data
	 */
	private void handleData(String data){
		//通过正则表达式匹配出返回值中的图片，并把值保存到集合中。
				ArrayList<HashMap<String,String>> dataList = selectImageId(data);
				for(int i=0;i<dataList.size();i++){//遍历集合中的图片区域，  集合的单元个数代表图片区域的个数
					int currentIndex = data.indexOf(dataList.get(i).get("img"));//得到当前匹配的数据段在数据中的位置
					//得到上一个匹配的数据段在数据中的位置
					int previousIndex = data.indexOf(dataList.get(i>0?i-1:0).get("img"))+dataList.get(i>0?i-1:0).get("img").length();
					if(currentIndex<=previousIndex){
						previousIndex = currentIndex;
					}
					//得到匹配的数据段前面的一段不匹配的数据
					String content = data.substring(i==0?0:previousIndex, currentIndex);
					if(content!=null && content.trim().length()>0){
						TextView tv = new TextView(getApplicationContext());
						tv.setTextColor(Color.BLACK);
						tv.setText(content.trim());
						contentLayout.addView(tv);
					}
					//将得到的以逗号分割的id字符串分割成id数组
					String[] imgStr = dataList.get(i).get("imgId").split(",");
					if(imgStr!=null && imgStr.length>0){
						ArrayList<View> pageViews = new ArrayList<View>();  
						for(int img=0;img<imgStr.length;img++){//遍历id数组
							ImageView imageView = new ImageView(getApplicationContext());
							//因为图片是以id命名的。所以可以先判断一下该图片在本地是否存在
							Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.SDPATH+FinalValues.SERVICES_IMG+imgStr[img]+".png");
							if(bitmap!=null){//如果存在则直接将其设置给新的ImageView控件
								imageView.setImageBitmap(bitmap);
								//将图片的宽和高放在一个Bundle中设置给ImageView的Tag,后面可以用这两个参数的比例来计算控件的真实高度
								Bundle bundle = new Bundle();
								bundle.putInt("height", bitmap.getHeight());
								bundle.putInt("width", bitmap.getWidth());
								imageView.setTag(bundle);
							}else{
								//如果图片在本地不存在，则根据id准备去服务器下载。
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
							pageViews.add(imageView);//将新产生的控件加入集合中，给ViewPager的Adapter用
						}
						contentLayout.addView(getViewPager(pageViews));//生成一个新的ViewPager并加入到图片区域中去。
					}
					if(i == dataList.size()-1){//这一段是为了获取最后一个图片区域的后面的可能有的文字
						String lastContent = data.substring(currentIndex+dataList.get(i).get("img").length());
						if(lastContent!=null && lastContent.length()>0){//只有在有内容的时候才生成新的控件。
							TextView lastTv = new TextView(getApplicationContext());
							lastTv.setTextColor(Color.BLACK);
							lastTv.setText(lastContent);
							contentLayout.addView(lastTv);
						}
					}
				}
	}
	/**
	 * 得到一个ViewPager
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
		for(int i=0;i<pageViews.size();i++){//算出ViewPager中最高控件的高度，这里是通过集合中的控件传递过来的参数算出高度的
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
		viewPager.setLayoutParams(param);//为ViewPager设置高度，如果不设置的话，在图片已经存在的情况下ViewPager会显示不出来
		viewPager.setAdapter(new ViewPagerAdapter(pageViews));//给ViewPager设置适配器
		viewPager.setOnPageChangeListener(new CasePageChangeListener(dotLayout));
		return view;
	}
	/**
	 * 从返回值中找到需要的图片ID 
	 * @param str
	 * @return 保存有匹配出来的数据段   和图片id集合
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
							System.out.println("找到id值为:"+m2.group(2));
							map.put("imgId", m2.group(2));
						}
					}
				}
			}
			list.add(map);
		}
		return list;
	}
	 // 指引页面更改事件监听器
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
////之所以不用stringbuffer是因为这个东西可以将object添加进去
//SpannableStringBuilder builder = new SpannableStringBuilder(text);
//
//for (int i = 0; i < FilterUbbs.length; i++) {//循环遍历你所有的表情进行查找替换
//  //正则匹配
//  Matcher matcher = Pattern.compile(FilterUbbs[i][1]).matcher(text);
//  
//  while (matcher.find()) {//查找匹配的类型
//  	String a = matcher.group(0);
//      String b =  matcher.group(1);
//      System.out.println(a);
//      System.out.println(b);
//          int resId = Integer.parseInt(FilterUbbs[i][0]);//需要替换的图片的资源ID
//          //哪哪，最重要的是这句话，将文字替换成图片
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
