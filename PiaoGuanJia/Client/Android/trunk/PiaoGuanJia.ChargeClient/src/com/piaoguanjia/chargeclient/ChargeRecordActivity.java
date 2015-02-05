/**
 * 
 */
package com.piaoguanjia.chargeclient;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piaoguanjia.chargeclient.XListView.IXListViewListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

/**
 * @author ������
 * @createDate 2013/5/7
 * ��ֵ��¼����
 */
public class ChargeRecordActivity extends Activity implements OnItemClickListener,OnClickListener,IXListViewListener{
	private ImageView backBtn;
	private Button btn1,btn2;//
	private XListView recordListView;//
	private TextView countItem;
	private ArrayList<HashMap<String,String>> lastList = new ArrayList<HashMap<String,String>>();
	private ArrayList<HashMap<String,String>> historyList = new ArrayList<HashMap<String,String>>();
	private boolean historyFlag = false;//�ж��Ƿ�Ϊ��ʷ��¼
	public ProgressDialog progress;
//	public int LastCurrentPage = 1;//�����ֵ�б�ĵ�ǰҳ
//	public int historyCurrentPage = 1;//
	private static final int countPage = 20;//ÿһҳҪ��ʾ����������
	private ProjectDBHelper dbHelper = null;
	private SQLiteDatabase dbDatabase = null;
	private boolean isFirstUpadte = true;//�Ƿ�Ϊ�״μ��أ�����֮����Ҫ�ж��Ƿ�Ϊ�״μ�������Ϊ�״μ��ص�������Ϊ�˸������ݣ����״μ���Ϊ��ȡ�µ�����
	private int isUpRefresh = 2;//2��������ˢ��   1�����������ظ���
	ChargeRecordAdapter adapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_chargerecord);
		dbHelper = new ProjectDBHelper(this);
		dbDatabase = dbHelper.getWritableDatabase();
		findview();
		initData();
		CommonUtils.hideSoftKeyboard(this);
	}
	@Override
	protected void onDestroy() {
		dbDatabase.close();
		super.onDestroy();
	}
	private void createProgress(){
    	if(progress!=null){
    		progress.cancel();
    	}
    	progress = new ProgressDialog(this);
    	progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	progress.setCanceledOnTouchOutside(false);
    	progress.setCancelable(false);
    	progress.show();
    }
	Runnable initDataRunnable = new Runnable() {
		@Override
		public void run() {
			String msg = getDataConnect();//���ʷ�����
			isFirstUpadte = false;//
			System.out.println("���ʳ�ֵ�б���-----------"+msg);
			if(msg==null){//���ӳ�ʱ
				handler.sendEmptyMessage(2);//��ȡ�б�ʧ��
			}else{
			if(msg.equals("0") || msg.equals("-1") || msg.equals("-2")  
					|| msg.equals("-3") || msg.equals("-4") || msg.equals("-5")
					 || msg.equals("-6") || msg.equals("-15") || msg.equals("-16") 
					 || msg.equals("-17") || msg.equals("-18")|| msg.equals("-31")|| msg.equals("-32")
					 || msg.equals("-33")|| msg.equals("-34")|| msg.equals("-35")|| msg.equals("-36")){
				handler.sendEmptyMessage(0);//��ȡ�б�ʧ��
			}else{
				jsonChargeToBundle(msg);//���ݿ����
				handler.sendEmptyMessage(1);//��ȡ�б�ɹ�
			}
				
			}
			
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what == 0){//��ȡ�б�ʧ��
//				dialogWronFun("��ȡ��¼ʧ�ܣ�",ChargeRecordActivity.this);
			}else if(msg.what == 1){//��ȡ�б�ɹ�
				selectData();
				notifiListView();
			}else if(msg.what == 2){
//				dialogWronFun("���ӳ�ʱ��",ChargeRecordActivity.this);
			}
			if(isUpRefresh == 2){//����ˢ��
				recordListView.stopRefresh();
			}else if (isUpRefresh == 1) {//�������ظ���
				recordListView.stopLoadMore();
				
			}
			if(progress!=null){
				progress.dismiss();
			}
			super.handleMessage(msg);
		}
		
	};
	/**
	 * �ӱ��ز�������
	 */
	private void selectData(){
		Cursor cursor = null;
		historyList.clear();
		lastList.clear();
		if(historyFlag){
			cursor = dbDatabase.query(Constant.CHARGE_RECORD_TABLE_NAME, 
					null, "status=? or status=?", new String[]{"1","2"}, null, null, "auditTime desc");
		}else{
			cursor = dbDatabase.query(Constant.CHARGE_RECORD_TABLE_NAME, 
					null, "status=?", new String[]{"0"}, null, null, "createTime desc");
		}
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("chargeid", cursor.getString(cursor.getColumnIndex("chargeid")));
			map.put("username", cursor.getString(cursor.getColumnIndex("username")));
			map.put("amount", cursor.getString(cursor.getColumnIndex("amount")));
			map.put("accountTypeDis", cursor.getString(cursor.getColumnIndex("accountTypeDis")));
			map.put("createTime", cursor.getString(cursor.getColumnIndex("createTime")));
			map.put("auditTime", cursor.getString(cursor.getColumnIndex("auditTime")));
			map.put("status", cursor.getString(cursor.getColumnIndex("status")));
			if(cursor.getString(cursor.getColumnIndex("status")).equals("0")){
				lastList.add(map);
			}else if (cursor.getString(cursor.getColumnIndex("status")).equals("1")) {
				historyList.add(map);
			}else if (cursor.getString(cursor.getColumnIndex("status")).equals("2")) {
				historyList.add(map);
			}
		}
		cursor.close();
	}
	private String getDataConnect(){
			String msg =null;
			String username = MyApplication.getInstance().getUsername();
			String password = MyApplication.getInstance().getPassword();
			try {
				String urlStr = Constant.CONNECT+Constant.ADDCHARGE_INTEGERFACE;
				HttpPost request = new HttpPost(urlStr);
				// ������ݲ��������Ƚ϶�Ļ������ǿ��ԶԴ��ݵĲ������з�װ
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("appid", Constant.APPID));
				params.add(new BasicNameValuePair("appkey", Constant.APPKEY));
				params.add(new BasicNameValuePair("action", "list"));
				params.add(new BasicNameValuePair("username", username));
				params.add(new BasicNameValuePair("password", password));
				params.add(new BasicNameValuePair("limit", ""+countPage));
//				params.add(new BasicNameValuePair("currentPage",historyFlag?historyCurrentPage+"":LastCurrentPage+""));
				if(historyFlag && historyList.size()>0){
					if(isFirstUpadte){
//						dbDatabase = dbHelper.getWritableDatabase();
//						Cursor cursor= dbDatabase.query(Constant.LAST_UPDATE_TIME_TAB_STRING, 
//								null, "username=?", new String[]{MyApplication.getInstance().getUsername()}, null, null, null);
//						cursor.moveToFirst();
//						String lastUpdateString = cursor.getString(cursor.getColumnIndex("updatetime"));
//						params.add(new BasicNameValuePair("lastUpdateTime", lastUpdateString));
//						cursor.close();
//						dbDatabase.close();
						params.add(new BasicNameValuePair("lastUpdateTime", historyList.get(0).get("auditTime")));
						params.add(new BasicNameValuePair("isUpOrDown", "2"));
						System.out.println("������������ʱ���ǣ�----------auditTime=="+historyList.get(0).get("auditTime"));
					}else {
//						dbDatabase = dbHelper.getWritableDatabase();
//						Cursor cursor= dbDatabase.query(Constant.CHARGE_RECORD_TABLE_NAME, 
//								null, "status=? or status=?", new String[]{"1","2"}, null, null, "auditTime desc");
//						cursor.moveToLast();
//						String lastTimeString = cursor.getString(cursor.getColumnIndex("auditTime"));
//						params.add(new BasicNameValuePair("lastUpdateTime", lastTimeString));
//						cursor.close();
//						dbDatabase.close();
						
						if(isUpRefresh == 2){//����ˢ��
//							String  lastString = lastList.get(0).get("createTime").toString().trim();
							String  historyString = historyList.get(0).get("auditTime").toString().trim();
//							if(CommonUtils.isBeforeTime(lastString, historyString)){
//								params.add(new BasicNameValuePair("lastUpdateTime", lastString));
//							}else{
								params.add(new BasicNameValuePair("lastUpdateTime", historyString));
//							}
							params.add(new BasicNameValuePair("isUpOrDown", "2"));
							System.out.println("������������ʱ���ǣ�----------auditTime=="+historyList.get(0).get("auditTime"));
						}else if (isUpRefresh == 1) {//�����鿴����
//							String  lastString = lastList.get(lastList.size()-1).get("createTime").toString().trim();
							String  historyString = historyList.get(historyList.size()-1).get("auditTime").toString().trim();
//							if(CommonUtils.isBeforeTime(lastString, historyString)){
//								params.add(new BasicNameValuePair("lastUpdateTime", lastString));
//							}else {
								params.add(new BasicNameValuePair("lastUpdateTime", historyString));
//							}
							params.add(new BasicNameValuePair("isUpOrDown", "1"));
							System.out.println("������������ʱ���ǣ�----------auditTime=="+historyList.get(historyList.size()-1).get("auditTime"));
						}
					}
				}else if(!historyFlag && lastList.size()>0){
					if(isFirstUpadte){
//						dbDatabase = dbHelper.getWritableDatabase();
//						Cursor cursor= dbDatabase.query(Constant.LAST_UPDATE_TIME_TAB_STRING, 
//								null, "username=?", new String[]{MyApplication.getInstance().getUsername()}, null, null, null);
//						cursor.moveToFirst();
//						String lastUpdateString = cursor.getString(cursor.getColumnIndex("updatetime"));
						params.add(new BasicNameValuePair("lastUpdateTime", lastList.get(0).get("createTime")));
						params.add(new BasicNameValuePair("isUpOrDown", "2"));
						System.out.println("������������ʱ���ǣ�----------createTime=="+lastList.get(0).get("createTime"));
//						dbDatabase.close();
					}else {
						
						if(isUpRefresh == 2){//����ˢ��
							String  lastString = lastList.get(0).get("createTime").toString().trim();
//							String  historyString = historyList.get(0).get("auditTime").toString().trim();
//							if(CommonUtils.isBeforeTime(lastString, historyString)){
								params.add(new BasicNameValuePair("lastUpdateTime", lastString));
//							}else{
//								params.add(new BasicNameValuePair("lastUpdateTime", historyString));
//							}
							params.add(new BasicNameValuePair("isUpOrDown", "2"));
							System.out.println("������������ʱ���ǣ�----------createTime=="+lastList.get(0).get("createTime"));
						}else if (isUpRefresh == 1) {//�����鿴����
							String  lastString = lastList.get(lastList.size()-1).get("createTime").toString().trim();
//							String  historyString = historyList.get(historyList.size()-1).get("createTime").toString().trim();
//							if(CommonUtils.isBeforeTime(lastString, historyString)){
								params.add(new BasicNameValuePair("lastUpdateTime", lastString));
//							}else {
//								params.add(new BasicNameValuePair("lastUpdateTime", historyString));
//							}
							params.add(new BasicNameValuePair("isUpOrDown", "1"));
							System.out.println("������������ʱ���ǣ�----------createTime=="+lastList.get(lastList.size()-1).get("createTime"));
						}
					}
				}else {
					params.add(new BasicNameValuePair("lastUpdateTime", ""));
					params.add(new BasicNameValuePair("isUpOrDown", "1"));
					System.out.println("������������ʱ���ǣ�----------��=="+"");
				}
				params.add(new BasicNameValuePair("isNewOrOld", historyFlag?"2":"1"));
			    request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			    HttpClient client = new DefaultHttpClient();
//			    // ����ʱ
                client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
//                // ��ȡ��ʱ
                client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);
				HttpResponse response = client.execute(request);
				msg = EntityUtils.toString(response.getEntity());
				}catch (Exception e) {
					e.printStackTrace();
				}
			return msg;
	}
	public void jsonChargeToBundle(String msg){
		Type listType = new TypeToken<LinkedList<ChargeBean>>() {}.getType();
		Gson gson = new Gson();
		LinkedList<ChargeBean> chargeBeans = null;
		ChargeBean chargeBean = null;
		chargeBeans = gson.fromJson(msg, listType);
		if (chargeBeans != null && chargeBeans.size() > 0) {
			for (Iterator<ChargeBean> iterator = chargeBeans.iterator(); iterator.hasNext();) {
				chargeBean = (ChargeBean) iterator.next();
				ContentValues cValues = new ContentValues();
				cValues.put("chargeid", chargeBean.getChargeid());
				cValues.put("username", chargeBean.getUsername());
				cValues.put("amount", chargeBean.getAmount());
				cValues.put("accountTypeDis", chargeBean.getAccountTypeDis());
				cValues.put("createTime", chargeBean.getCreateTime());
				cValues.put("auditTime", chargeBean.getAuditTime());
				cValues.put("status", chargeBean.getStatus());
				Cursor cursor = dbDatabase.query(Constant.CHARGE_RECORD_TABLE_NAME, 
						null, "chargeid=?", new String[]{chargeBean.getChargeid()}, null, null, null);
				if(cursor==null){
					dbDatabase.insert(Constant.CHARGE_RECORD_TABLE_NAME, null, cValues);
				}else{
					dbDatabase.delete(Constant.CHARGE_RECORD_TABLE_NAME, "chargeid=?", new String[]{chargeBean.getChargeid()});
					dbDatabase.insert(Constant.CHARGE_RECORD_TABLE_NAME, null, cValues);
				}
//				if(chargeBean == chargeBeans.get(chargeBeans.size()-1)){
//					Cursor cursor1 = dbDatabase.query(Constant.LAST_UPDATE_TIME_TAB_STRING, 
//							null, "username=?", new String[]{MyApplication.getInstance().getUsername()}, null, null, null);
//					ContentValues uValues = new ContentValues();
//					uValues.put("username", MyApplication.getInstance().getUsername());
//					uValues.put("chargetype", "0");
//					uValues.put("updatetime", chargeBean.getCreateTime());
//					if(cursor1==null){
//						dbDatabase.insert(Constant.LAST_UPDATE_TIME_TAB_STRING,null, uValues);
//					}else{
//						dbDatabase.delete(Constant.LAST_UPDATE_TIME_TAB_STRING,"username=?", new String[]{MyApplication.getInstance().getUsername()});
//						dbDatabase.insert(Constant.LAST_UPDATE_TIME_TAB_STRING,null, uValues);
//					}
//					cursor1.close();
//				}
				cursor.close();
			}
			
		}
	}
	private void initData(){
		historyFlag = getIntent().getBooleanExtra("historyFlag", false);
		lastList.clear();
		historyList.clear();
		if(historyFlag){
			btn2.setBackgroundResource(R.drawable.chargebg);
			btn2.setTextColor(getResources().getColor(R.color.charge_type_text_yellow));
			btn1.setBackgroundColor(Color.TRANSPARENT);
			btn1.setTextColor(Color.WHITE);
		}
		 dbDatabase.execSQL(Constant.CHARGESQL_STRING);
	     selectData();
	     notifiListView();
		ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
        	dialogWronFun("δ��⵽���磬���������������ӣ�",ChargeRecordActivity.this);
        }else {
        	isFirstUpadte = true;
        	getData();
		}
	}
	private void getData(){
		createProgress();
		if(historyFlag){
			progress.setMessage("��������ʷ���ݣ����Ժ�");
		}else{
			progress.setMessage("������������ݣ����Ժ�");
		}
		new Thread(initDataRunnable).start();
	}
	private void findview(){
		backBtn = (ImageView) findViewById(R.id.backBtn);
		btn1 = (Button) findViewById(R.id.button1);
		btn2 = (Button) findViewById(R.id.button2);
		recordListView = (XListView) findViewById(R.id.recordListView);
		backBtn.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		countItem = (TextView) findViewById(R.id.countItem);
		recordListView.setOnItemClickListener(this);
		recordListView.setPullLoadEnable(true);
		recordListView.setPullRefreshEnable(true);
		recordListView.setXListViewListener(this);
	}
	/**
	 * 
	 * @param flag trueΪ��ʷ falseΪ���
	 */
	private void notifiListView(){
		try {
		if(historyFlag){
		    adapter = new ChargeRecordAdapter(this, historyList,historyFlag);
			recordListView.setAdapter(adapter);
			recordListView.setCacheColorHint(0);
//			recordListView.setSelection((historyCurrentPage-1)*countPage-1);
			countItem.setText(historyList.size()+"");
		}else{
			adapter = new ChargeRecordAdapter(this, lastList,historyFlag);
			recordListView.setAdapter(adapter);
			recordListView.setCacheColorHint(0);
//			recordListView.setSelection((LastCurrentPage-1)*countPage-1);
			countItem.setText(lastList.size()+"");
		}
		} catch (IllegalStateException e) {
			System.out.println("------------�б��ֱ����ˡ���������");
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent i = new Intent(this,ExamineChargeActivity.class);
		i.putExtra("chargeId", historyFlag?historyList.get(position-1).get("chargeid"):lastList.get(position-1).get("chargeid"));
		i.putExtra("historyFlag", historyFlag);
		startActivityForResult(i, 11);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			if(requestCode == 11){
				System.out.println("�һ����ˡ���������������������������������������������������������������������������������������������������������");
				initData();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){//���ؼ�
			finish();
		}else if(v == btn1){//���
			String hasAuditString = MyApplication.getInstance().getHasAuditPerm();
			
			if(hasAuditString!=null && hasAuditString.equals("1")){
				btn1.setBackgroundResource(R.drawable.chargebg);
				btn1.setTextColor(getResources().getColor(R.color.charge_type_text_yellow));
				btn2.setBackgroundColor(Color.TRANSPARENT);
				btn2.setTextColor(Color.WHITE);
				historyFlag = false;
				selectData();
				ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
		        	dialogWronFun("δ��⵽���磬���������������ӣ�",ChargeRecordActivity.this);
		        	recordListView.stopLoadMore();
		        }else{
		        	if(lastList.size()==0){
						createProgress();
						progress.setMessage("��������ʷ���ݣ����Ժ�");
						getData();
					}
		        }
		        notifiListView();
			}else {
				dialogWronFun("��û����˳�ֵ��Ȩ�ޣ�", this);
			}
		}else if(v == btn2){//��ʷ
			btn2.setBackgroundResource(R.drawable.chargebg);
			btn2.setTextColor(getResources().getColor(R.color.charge_type_text_yellow));
			btn1.setBackgroundColor(Color.TRANSPARENT);
			btn1.setTextColor(Color.WHITE);
			historyFlag = true;
			selectData();
			ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
	        	dialogWronFun("δ��⵽���磬���������������ӣ�",ChargeRecordActivity.this);
	        	recordListView.stopLoadMore();
	        }else{
	        	if(historyList.size()==0){
					createProgress();
					progress.setMessage("��������ʷ���ݣ����Ժ�");
					getData();
				}
	        }
	        notifiListView();
		}
		
	}
	public void dialogWronFun(CharSequence str,Context context){
      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
      	alert.setMessage(str);
      	alert.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
      			
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			});
      	alert.show();
      }
	@Override
	public void onRefresh() {
		System.out.println("----------------------��ʼˢ���б���Ŷ -------------------------------------");
		ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
        	dialogWronFun("δ��⵽���磬���������������ӣ�",ChargeRecordActivity.this);
        	recordListView.stopRefresh();
        }else{
//        	if(historyFlag){
//        		historyCurrentPage++;
//        	}else{
//        		LastCurrentPage++;
//        	}
        	isUpRefresh = 2;
        	getData();
        }
        if (historyFlag && historyList.size()>0) {
        	recordListView.setRefreshTime(historyList.get(0).get("auditTime").toString());
		}else if (!historyFlag && lastList.size()>0) {
			recordListView.setRefreshTime(lastList.get(0).get("createTime").toString());
		}
			
        
	}
	@Override
	public void onLoadMore() {
		System.out.println("----------------------��ʼˢ���б���Ŷ -------------------------------------");
		ConnectivityManager cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cwjManager.getActiveNetworkInfo() ==null || !cwjManager.getActiveNetworkInfo().isAvailable()) {		                	 
        	dialogWronFun("δ��⵽���磬���������������ӣ�",ChargeRecordActivity.this);
        	recordListView.stopLoadMore();
        }else{
//        	if(historyFlag){
//        		historyCurrentPage++;
//        	}else{
//        		LastCurrentPage++;
//        	}
        	isUpRefresh = 1;
        	getData();
        }
	}
}
