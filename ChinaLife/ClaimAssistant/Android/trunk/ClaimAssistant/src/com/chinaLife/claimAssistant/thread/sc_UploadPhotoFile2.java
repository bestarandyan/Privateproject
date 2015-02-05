package com.chinaLife.claimAssistant.thread;
/*package com.chinaLife.claimAssistant.thread;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.chinaLife.Interface.SurveySelfInterface;
import com.chinaLife.claimAssistant.MyApplication;
import com.chinaLife.claimAssistant.database.DBHelper;
import com.chinaLife.claimAssistant.tools.NetworkCheck;
import com.chinaLife.claimAssistant.tools.PhoneInfo;
import com.chinaLife.claimAssistant.tools.StringUtils;
import com.chinaLife.claimAssistant.tools.UploadFile;
import com.chinaLife.claimAssistant.tools.VisiteTimes;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Message;

public class UploadPhotoFile2 {

	private static UploadPhotoFile2 mInstance = null;
	private static File file = null; // 文件路径
	private  List<Map<String, Object>> selectresult = null;
	public static boolean isstop = false;
	private VisiteTimes visitetime = new VisiteTimes();
	
	
	private boolean isuploadsuccess = false;
	private int blocklength = 512;
	private int dataend = 0;//数据结束位置
	private int datastart = 0;//数据结束位置
	private long time1 = 0l;
	private long time2 = 0l;
	private int filesize;
	
	private final int speedmax = 30*1024;//最大速度30k/s
	private final int speedmin = 512;//最小速度0.5k/s
	
	public UploadPhotoFile2() {
	}

	public static UploadPhotoFile2 getInstance() {
		if (mInstance == null) {
			mInstance = new UploadPhotoFile2();
		}
		return mInstance;
	}

	public static void startMyThread() {
		if (NetworkCheck.IsHaveInternet(MyApplication.getInstance()
				.getContext())) {
			isstop = false;
			getInstance().Post();
		}
	}
	public void Post() {
		MyApplication.getInstance().setUploadon(true);
		while (!isstop) {
			selectresult = checkData();
			String response = ""; // 发送文件信息时响应的语句
			String _id = ""; // 该上传文件唯一标示id
			String uploadid = ""; // 上传信息表 上传id
			String photoid = ""; // 理赔图片信息表 图片id
			String savepath = ""; // 文件路径
			int i = 0; // 文件循环标识

			loop: while (i < selectresult.size()) {
				isuploadsuccess = false;
				file = new File(selectresult.get(i).get("savepath").toString());
				_id = selectresult.get(i).get("_id").toString();
				filesize = Integer.parseInt(selectresult
						.get(i).get("filesize").toString());
				if (!file.exists()) {
					//System.out.println(file.getName()+"不存在");
					i++;
					break loop;
				}
				if (selectresult.get(i).get("uploadid") == null) {
					String longitude;
					String latitude;
					try {
						longitude = selectresult.get(i).get("longitude")
								.toString();
						latitude = selectresult.get(i).get("latitude")
								.toString();
					} catch (Exception e) {
						longitude = "0";
						latitude = "0";
					}

					List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
					params.add(new BasicNameValuePair("appid",
							MyApplication.APPID));
					params.add(new BasicNameValuePair("appkey",
							MyApplication.APPKEY));
					params.add(new BasicNameValuePair("action", "upload2"));
					params.add(new BasicNameValuePair("IMEI", PhoneInfo
							.getIMEI(MyApplication.getInstance().getContext())));

					params.add(new BasicNameValuePair("legendid", selectresult
							.get(i).get("legendid").toString()));
					params.add(new BasicNameValuePair("type", selectresult
							.get(i).get("type").toString()));
					params.add(new BasicNameValuePair("name", selectresult
							.get(i).get("filename").toString()));
					try {
						params.add(new BasicNameValuePair("location",
								selectresult.get(i).get("location").toString()));
					} catch (Exception e) {
						params.add(new BasicNameValuePair("location", ""));
					}
					params.add(new BasicNameValuePair("longitude", longitude));
					params.add(new BasicNameValuePair("latitude", latitude));
					params.add(new BasicNameValuePair("claimid", selectresult
							.get(i).get("claimid").toString()));
					params.add(new BasicNameValuePair("time", selectresult
							.get(i).get("time").toString()));
					try {
						params.add(new BasicNameValuePair("photoid",
								selectresult.get(i).get("photoid").toString()));
					} catch (Exception e) {
						params.add(new BasicNameValuePair("photoid", ""));
					}
					params.add(new BasicNameValuePair("size", filesize+""));
					HttpPost request = new HttpPost(MyApplication.URL + "claim");
					HttpParams httpParameters = new BasicHttpParams();
					try {
						request.setEntity(new UrlEncodedFormEntity(params,
								HTTP.UTF_8));
						HttpConnectionParams.setConnectionTimeout(
								httpParameters, 10000);
						request.setParams(httpParameters);
					} catch (UnsupportedEncodingException e) {
						if(MyApplication.opLogger!=null){
							MyApplication.opLogger.error(e);
						}
						
						visitetime.count();
						if(visitetime.isOut2()){
							MyHandler.getInstance().sendEmptyMessage(-9);
							MyApplication.getInstance().getProgressdialog().dismiss();
							return;
						}else{
							if(visitetime.isInit()){
								visitetime.init();
							}
						}
						return;

					}
					// 发送请求
					try {
						// // 得到应答的字符串，这是一个 JSON 格式保存的数据
						response = connServerForResult(request);
					} catch (Exception e) {
						if(MyApplication.opLogger!=null){
							MyApplication.opLogger.error(e);
						}
						
						visitetime.count();
						if(visitetime.isOut2()){
							MyHandler.getInstance().sendEmptyMessage(-9);
							MyApplication.getInstance().getProgressdialog().dismiss();
							return;
						}else{
							if(visitetime.isInit()){
								visitetime.init();
							}
						}
						break loop;
					}

					if (!response.contains(",")) {
						visitetime.count();
						if(visitetime.isOut2()){
							MyHandler.getInstance().sendEmptyMessage(-9);
							MyApplication.getInstance().getProgressdialog().dismiss();
							return;
						}else{
							if(visitetime.isInit()){
								visitetime.init();
							}
						}
						continue;
					} else {
						String str[] = response.split(",");
						uploadid = str[0];
						photoid = str[1];
						savepath = str[2];
						if (uploadid.length() <= 0 || photoid.length() <= 0
								|| savepath.length() <= 0) {
							continue;
						}
						Date date = new Date();
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// 设置显示格式
						String nowTime = df.format(date);
						ContentValues values1 = new ContentValues();
						values1.put("uploadid", uploadid);
						values1.put("update_time", StringUtils.formatCalendar(nowTime,
								MyApplication.ERROR_VALUE));
						values1.put("block_count", 0);	
						
						
						ContentValues values2 = new ContentValues();
						values2.put("uploadid", uploadid);
						values2.put("photoid", photoid);
						values2.put("filename", savepath);		
						
						
						DBHelper.getInstance().update("uploadinfo", values1, "_id=?", new String[]{_id});
						DBHelper.getInstance().update("claimphotoinfo", values2, "legendid=? and claimid = ?", new String[]{selectresult.get(i).get("legendid")
								.toString(),selectresult.get(i).get("claimid")
								.toString()});
//						DBHelper.getInstance().execSql(
//								"update uploadinfo set uploadid ='"
//										+ uploadid
//										+ "',update_time='"
//										+ StringUtils.formatCalendar(nowTime,
//												MyApplication.ERROR_VALUE)
//										+ "',block_count=" + 0
//										+ " where _id=" + _id);
//
//						DBHelper.getInstance().execSql(
//								"update claimphotoinfo set uploadid ='"
//										+ uploadid
//										+ "',photoid= '"
//										+ photoid
//										+ "',filename= '"
//										+ savepath
//										+ "' where legendid='"
//										+ selectresult.get(i).get("legendid")
//												.toString()
//										+ "' and claimid='"
//										+ selectresult.get(i).get("claimid")
//												.toString() + "'");
					}

				} else {
					savepath = selectresult.get(i).get("serverpath").toString();
					uploadid = selectresult.get(i).get("uploadid").toString();
					datastart = Integer.parseInt(selectresult.get(i).get("block_count")
							.toString());
				}

				// 开启进度条
				MyApplication.getInstance().setProgressbarmax(
						Integer.parseInt(selectresult.get(i).get("filesize")
								.toString()));
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putString("legendid", selectresult.get(i).get("legendid")
						.toString());
				msg.what = -3;
				data.putInt(
						"max",
						Integer.parseInt(selectresult.get(i).get("filesize")
								.toString()));
				msg.setData(data);
				MyHandler.getInstance().sendMessage(msg);
				if(MyApplication.opLogger!=null){
					MyApplication.opLogger.info(MyApplication.getInstance().getClaimid()
						+ "图片正在上传----------图例为："
						+ selectresult.get(i).get("legendid").toString());
				}
				
				//System.out.println(MyApplication.getInstance().getClaimid()
						//+ "图片正在上传----------图例为："
						//+ selectresult.get(i).get("legendid").toString());

					while (!isuploadsuccess) {
						//System.out.println("i的值为：-----------" + i);
						if(isstop){
							MyApplication.getInstance().setUploadon(false);
							MyApplication.getInstance().getProgressdialog().dismiss();
							return;
						}
						try {
							if (dataend >= Integer.parseInt(selectresult
								.get(i).get("filesize").toString())) {
								ContentValues values = new ContentValues();
								values.put("status", 2);
								DBHelper.getInstance().execSql(
										"update uploadinfo set status =" + 2
												+ " where uploadid='" + uploadid
												+ "'");
								DBHelper.getInstance().execSql(
										"update claimphotoinfo set review_result =" + 3
												+ " where uploadid='" + uploadid
												+ "' and claimid='"+MyApplication.getInstance().getClaimid()+
														"' and legendid='"+selectresult.get(i).get("legendid")
														.toString()+"'");
								if(MyApplication.opLogger!=null){
									MyApplication.opLogger.info(MyApplication.getInstance()
										.getClaimid()
										+ "-----图片上传成功"
										+ "-------图例为："
										+ selectresult.get(i).get("legendid")
												.toString());
								}
								
								System.out.println(MyApplication.getInstance()
										.getClaimid()
										+ "-----图片上传成功"
										+ "-------图例为："
										+ selectresult.get(i).get("legendid")
												.toString());
								// 关闭进度条

								Message msg1 = new Message();
								Bundle data1 = new Bundle();
								data1.putString("legendid", selectresult.get(i)
										.get("legendid").toString());
								msg1.what = -5;
								msg1.setData(data1);
								MyHandler.getInstance().sendMessage(msg1);
								// DBHelper.getInstance().close();
								datastart = 0;
								dataend = 0;
								time1 = 0;
								time2 = 0;
								break loop;
							}
						} catch (Exception e) {
							if(MyApplication.opLogger!=null){
								MyApplication.opLogger.error(e);
							}
							
						}
						if(!Fenkuai(Integer.parseInt(selectresult
								.get(i).get("filesize").toString()))){
							MyHandler.getInstance().sendEmptyMessage(-9);
							return;
						}
						time1 = new Date().getTime();
						Map<String, String> params = new HashMap<String, String>();
						params.put("appid", MyApplication.APPID);
						params.put("appkey", MyApplication.APPKEY);
						params.put("action", "upload2");
						params.put("IMEI", PhoneInfo.getIMEI(MyApplication
								.getInstance().getContext()));
						params.put("end",dataend+"");
						params.put("start",datastart+"");
						params.put("uploadid",uploadid);
						params.put("path", savepath);
						Map<String, File> filesmap = new HashMap<String, File>();
						filesmap.put(file.getName(), file);
						String reponse = "初始值";
						try {
							reponse = UploadFile.post(MyApplication.URL
									+ "uploader", params, filesmap, datastart, dataend,
									selectresult.get(i).get("legendid")
											.toString());
							//System.out.println(params);
							//System.out.println(datastart+"--------"+dataend+"------"+reponse+"文件大小为："+selectresult.get(i).get("filesize")
									//.toString());
							time2 = new Date().getTime();
							if (reponse.trim().equals("") || reponse == null) {
								visitetime.count();
								if(visitetime.isOut2()){
									MyHandler.getInstance().sendEmptyMessage(-9);
									MyApplication.getInstance().getProgressdialog().dismiss();
									return;
								}else{
									if(visitetime.isInit()){
										visitetime.init();
									}
								}
								continue;
							}
							int reponsecode = -1;
							try {
								reponsecode = Integer.parseInt(reponse);
							} catch (Exception e) {
								if(MyApplication.opLogger!=null){
									MyApplication.opLogger.error(e);
								}
								visitetime.count();
								if(visitetime.isOut2()){
									MyHandler.getInstance().sendEmptyMessage(-9);
									MyApplication.getInstance().getProgressdialog().dismiss();
									return;
								}else{
									if(visitetime.isInit()){
										visitetime.init();
									}
								}
								continue;
							}
							if (reponsecode == 0) {
								//System.out.println(MyApplication.getInstance().getProgressdialog().isShowing()+"----------");
								DBHelper.getInstance().execSql(
										"update uploadinfo set block_count ="
												+ dataend
												+ " where _id=" + _id);
								if(dataend<Integer.parseInt(selectresult
										.get(i).get("filesize").toString())){
									datastart = dataend;
									dataend+=blocklength;
								}
								//System.out.println("============"+datastart+"============"+dataend+"============"+blocklength+"============");
							}else{
								//System.out.println("获取的服务器的响应值为"+reponse);
								//System.out.println("上传不成功");
								if(MyApplication.opLogger!=null){
									MyApplication.opLogger.info("获取的服务器的响应值为"+reponse);
								}
								visitetime.count();
								if(visitetime.isOut2()){
									MyHandler.getInstance().sendEmptyMessage(-9);
									MyApplication.getInstance().getProgressdialog().dismiss();
									return;
								}else{
									if(visitetime.isInit()){
										visitetime.init();
									}
								}
								continue;
							}
						} catch (IOException e) {
							if(MyApplication.opLogger!=null){
								MyApplication.opLogger.error(e);
							}
							time1 = new Date().getTime();
							visitetime.count();
							if(visitetime.isOut2()){
								MyHandler.getInstance().sendEmptyMessage(-9);
								MyApplication.getInstance().getProgressdialog().dismiss();
								return;
							}else{
								if(visitetime.isInit()){
									visitetime.init();
								}
							}
							if(MyApplication.opLogger!=null){
								MyApplication.opLogger.info("获取的服务器的响应值为"+reponse);
							}
							//System.out.println("获取的服务器的响应值为"+reponse);
							e.printStackTrace();
							continue;
						}
					}
					
				} 
				i++;
			}
			selectresult.clear();
			selectresult = checkData();
			if(selectresult.size()<=0){
				MyApplication.getInstance().setUploadon(false);
				SurveySelfInterface survey = (SurveySelfInterface)MyApplication.getInstance().getContext();
				survey.SurveySelf();
				isstop = true;
			}else{
				isstop = false;
			}
		}


	private boolean Fenkuai(int filesize) {
		if(time1 >= time2){
			dataend=datastart+blocklength;
			if(dataend>filesize){
				dataend = filesize;
			}
			return true;
		}else{
			int varspeed = (int)(blocklength*1000/(time2-time1));
			if(varspeed<speedmin){
				if(dataend>filesize){
					dataend = filesize;
				}
				return true;
			}else if(varspeed>=speedmin&&varspeed<speedmax){
				blocklength +=5*1024;
				if(dataend>filesize){
					dataend = filesize;
				}
				return true;
			}else{
				if(dataend>filesize){
					dataend = filesize;
				}
				return true;
			}
		}
	}


	private List<Map<String, Object>> checkData() {
		String sql = "";
		if (MyApplication.getInstance().getStepstate() == 0) {
			sql = "select uinfo.*,cinfo.type,cinfo.photoid,cinfo.filename as serverpath from uploadinfo uinfo inner join claimphotoinfo cinfo on uinfo.legendid = cinfo.legendid and uinfo.claimid = cinfo.claimid where uinfo.claimid = '"
					+ MyApplication.getInstance().getClaimid()
					+ "' and (uinfo.status =0 or uinfo.status =3)";
		} else {
			sql = "select uinfo.*,cinfo.type,cinfo.photoid,cinfo.filename as serverpath from uploadinfo uinfo inner join claimphotoinfo cinfo on uinfo.legendid = cinfo.legendid and uinfo.claimid = cinfo.claimid where uinfo.claimid = '"
					+ MyApplication.getInstance().getClaimid()
					+ "' and (uinfo.status =0 or uinfo.status =3) and cinfo.type =2 ";
		}
		List<Map<String, Object>> selectresult = DBHelper.getInstance()
				.selectRow(sql, null);
		return selectresult;
	}

	// 得到服务器响应数据
	private String connServerForResult(HttpPost request) {
		// HttpGet对象
		String strResult = "";
		try {
			// 获得HttpResponse对象
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
				return "-404";
			}
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的数据
				strResult = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (Exception e) {
			if(MyApplication.opLogger!=null){
				MyApplication.opLogger.error(e);
			}
			
			return "-1";
		}
		return strResult;
	}

}
*/