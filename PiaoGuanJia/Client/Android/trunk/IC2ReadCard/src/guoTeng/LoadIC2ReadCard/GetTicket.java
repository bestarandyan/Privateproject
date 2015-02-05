package guoTeng.LoadIC2ReadCard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import com.google.gson.Gson;
import com.qingfengweb.content.DbContent;
import com.qingfengweb.content.InterfaceContent;
import com.qingfengweb.db.TicketBean;
import com.qingfengweb.db.UserBean;
public class GetTicket {
	public Context context;
	public GetTicket(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	public String loadFun(String username,String password){//用户登录向服务器发送请求
	       String taskStr = null;
	// 2. 使用 Apache HTTP 客户端实现
	       try {
			String urlStr = InterfaceContent.INTERFACE+InterfaceContent.USER_INTERFACE;
			HttpPost request = new HttpPost(urlStr);
			// 如果传递参数个数比较多的话，我们可以对传递的参数进行封装
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("appid", InterfaceContent.APP_ID));
			params.add(new BasicNameValuePair("appkey", InterfaceContent.APP_KEY));
			params.add(new BasicNameValuePair("action", InterfaceContent.USER_ACTION));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));
				request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				HttpResponse response = new DefaultHttpClient().execute(request);
				String msg = EntityUtils.toString(response.getEntity());
					taskStr = msg;
			
			}catch (Exception e) {
				//e.printStackTrace();
			}
			return taskStr;
	}
	
	public String getTicket(String idnumner){
		   String taskStr = null;
			// 2. 使用 Apache HTTP 客户端实现
					String urlStr = InterfaceContent.INTERFACE+InterfaceContent.TICKET_INTERFACE;
					HttpPost request = new HttpPost(urlStr);
					// 如果传递参数个数比较多的话，我们可以对传递的参数进行封装
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					params.add(new BasicNameValuePair("appid", InterfaceContent.APP_ID));
					params.add(new BasicNameValuePair("appkey", InterfaceContent.APP_KEY));
					params.add(new BasicNameValuePair("action", InterfaceContent.TICKET_ACTION));
					params.add(new BasicNameValuePair("username", UserBean.getInstence().getUsername()));
					params.add(new BasicNameValuePair("password", UserBean.getInstence().getPassword()));
					params.add(new BasicNameValuePair("idnumber", idnumner));
					try {
						request.setEntity( new UrlEncodedFormEntity(params,HTTP.UTF_8));
						HttpResponse response = new DefaultHttpClient().execute(request);
						if(response.getStatusLine().getStatusCode()==404){ 
							Toast.makeText(context, "访问不到页面", 3000).show();
						}else{
							String msg = EntityUtils.toString(response.getEntity());
							 if(msg.startsWith("{")){
								 taskStr = msg ;
							 }else{
								 taskStr = "";
								 }
							 }
					} catch (Exception e) {
						
					}
					return taskStr;
	}
	public Map<String,String> jsonGetTicket(String msg){
		Map<String,String> map = new HashMap<String,String>();
		Gson gson=new Gson();  
	    TicketBean user=gson.fromJson(msg, TicketBean.class); 
		  	map.put("orderid", user.getOrderid());
		  	map.put("ordertime", user.getOrdertime().substring(0, 10));
		  	map.put("username", user.getUsername());
		  	map.put("tickettype", user.getTickettype());
		  	map.put("ordercount", user.getOrdercount());
		  	map.put("createtime", user.getCreatetime());
		  	map.put("phonenumber", user.getPhonenumber());
		  	map.put("scenicname", user.getScenicname());
		  	map.put("idnumber", user.getIdnumber());
		return map;
	}
	public void insertTickets(Map<String,String> map,SQLiteDatabase db){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		String time = year+"-"+month+"-"+day+"  "+hour+"-"+minute;
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = sDateFormat.format(new java.util.Date());
		ContentValues cv = new ContentValues();
		Cursor C = db.query(DbContent.HISTORY_TICKET, null, "orderid=?", new String[]{map.get("orderid")}, null, null, null);
		if(C.getCount()==0){
			cv.put("orderid", map.get("orderid"));
			cv.put("ordertime", map.get("ordertime"));
			cv.put("username", map.get("username"));
			cv.put("tickettype", map.get("tickettype"));
			cv.put("ordercount", map.get("ordercount"));
			cv.put("createtime", map.get("createtime"));
			cv.put("idnumber", map.get("idnumber"));
			cv.put("phonenumber", map.get("phonenumber"));
			cv.put("scenicname", map.get("scenicname"));
			cv.put("testingTime", date);
			db.insert(DbContent.HISTORY_TICKET, null, cv);
		}
		C.close();
	}
	
	
	
}
