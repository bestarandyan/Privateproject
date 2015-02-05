package com.chinaLife.claimAssistant.thread;

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

import com.chinaLife.claimAssistant.Interface.sc_SurveySelfInterface;
import com.chinaLife.claimAssistant.activity.Sc_MyApplication;
import com.chinaLife.claimAssistant.database.sc_DBHelper;
import com.chinaLife.claimAssistant.tools.sc_EncryptPhoto;
import com.chinaLife.claimAssistant.tools.sc_LogUtil;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;
import com.chinaLife.claimAssistant.tools.sc_PhoneInfo;
import com.chinaLife.claimAssistant.tools.sc_StringUtils;
import com.chinaLife.claimAssistant.tools.sc_UploadFile;
import com.chinaLife.claimAssistant.tools.sc_VisiteTimes;

import com.sqlcrypt.database.ContentValues;
import android.os.Bundle;
import android.os.Message;

public class sc_UploadPhotoFile {

	private static sc_UploadPhotoFile mInstance = null;
	private static File file = null; // 文件路径
	private static int BLOCKLENGTH_PHOTO = 1024 * 5;// 分块的大小
	private List<Map<String, Object>> selectresult = null;
	public static boolean isstop = false;
	private sc_VisiteTimes visitetime = new sc_VisiteTimes();

	public sc_UploadPhotoFile() {
	}

	public static sc_UploadPhotoFile getInstance() {
		if (mInstance == null) {
			mInstance = new sc_UploadPhotoFile();
		}
		return mInstance;
	}

	public static void startMyThread() {
		if (sc_NetworkCheck.IsHaveInternet(Sc_MyApplication.getInstance()
				.getContext())) {
			if (sc_PhoneInfo.isConnectedFast(Sc_MyApplication.getInstance()
					.getContext())) {
				BLOCKLENGTH_PHOTO = 1024 * 10;
			} else {
				BLOCKLENGTH_PHOTO = 1024 * 3;
			}

			isstop = false;
			getInstance().Post();
		}
	}

	public void Post() {
		// System.out.println("块的大小为："+BLOCKLENGTH_PHOTO);
		Sc_MyApplication.getInstance().setUploadon(true);
		while (!isstop) {
			// if(!ThreadDemo.isstart){
			// continue;
			// }
			selectresult = checkData();
			String response = ""; // 发送文件信息时响应的语句
			String _id = ""; // 该上传文件唯一标示id
			String uploadid = ""; // 上传信息表 上传id
			String photoid = ""; // 理赔图片信息表 图片id
			String savepath = ""; // 文件路径
			int count = 0; // 分块数量
			int i = 0; // 文件循环标识

			loop: while (i < selectresult.size()) {

				file = new File(selectresult.get(i).get("savepath").toString());
				_id = selectresult.get(i).get("_id").toString();
				if (!file.exists()) {
					// System.out.println(file.getName()+"不存在");
					i++;
					break loop;
				}
				if (selectresult.get(i).get("uploadid") == null) {
					int a = (int) (Long.parseLong(selectresult.get(i)
							.get("filesize").toString()) / (BLOCKLENGTH_PHOTO));
					int b = (int) (Long.parseLong(selectresult.get(i)
							.get("filesize").toString()) % (BLOCKLENGTH_PHOTO));
					if (a == 0 && b == 0) {
						i++;
						break loop;
					}
					if (b > 0) {
						count = a + 1;
					} else {
						count = a;
					}
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
							Sc_MyApplication.APPID));
					params.add(new BasicNameValuePair("appkey",
							Sc_MyApplication.APPKEY));
					params.add(new BasicNameValuePair("phoneNumber", Sc_MyApplication
							.getInstance().getPhonenumber()));
					params.add(new BasicNameValuePair("plateNumber", Sc_MyApplication
							.getInstance().getPlatenumber()));
					params.add(new BasicNameValuePair("password", Sc_MyApplication
							.getInstance().getPassword()));
					params.add(new BasicNameValuePair("action", "upload"));
					params.add(new BasicNameValuePair("IMEI", sc_PhoneInfo
							.getIMEI(Sc_MyApplication.getInstance().getContext())));

					params.add(new BasicNameValuePair("legendid", selectresult
							.get(i).get("legendid").toString()));
					params.add(new BasicNameValuePair("type", selectresult
							.get(i).get("type").toString()));
					params.add(new BasicNameValuePair("name", selectresult
							.get(i).get("filename").toString()));
					params.add(new BasicNameValuePair("blockCount", count + ""));
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
					params.add(new BasicNameValuePair("size", selectresult
							.get(i).get("filesize").toString()));
					HttpPost request = new HttpPost(Sc_MyApplication.URL + "claim");
					HttpParams httpParameters = new BasicHttpParams();
					try {
						request.setEntity(new UrlEncodedFormEntity(params,
								HTTP.UTF_8));
						HttpConnectionParams.setConnectionTimeout(
								httpParameters, 10000);
						request.setParams(httpParameters);
					} catch (final UnsupportedEncodingException e) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								sc_LogUtil.sendLog(2, "请求参数错误："+e.getMessage());
							}
						}).start();
						visitetime.count();
						if (visitetime.isOut2()) {
							sc_MyHandler.getInstance().sendEmptyMessage(-14);
							Sc_MyApplication.getInstance().getProgressdialog()
									.dismiss();
							return;
						} else {
							if (visitetime.isInit()) {
								visitetime.init();
							}
						}
						return;
					}
					// 发送请求
					try {
						// // 得到应答的字符串，这是一个 JSON 格式保存的数据
						response = connServerForResult(request);
						if (params != null) {
							params.clear();
							params = null;
						}
						boolean b1 = false;
						if (response.contains("-31")) {
							b1= true;
						} else if (response.contains("-30")) {
							b1= true;
						} else if (response.contains("-33")) {
							b1= true;
						}
						if(b1){
							sc_MyHandler.getInstance().sendEmptyMessage(-101);
							return;
						}
					} catch (final Exception e) {
						e.printStackTrace();
						visitetime.count();
						if (visitetime.isOut2()) {
							sc_MyHandler.getInstance().sendEmptyMessage(-14);
							Sc_MyApplication.getInstance().getProgressdialog()
									.dismiss();
							return;
						} else {
							if (visitetime.isInit()) {
								visitetime.init();
							}
						}
						break loop;
					}

					if (!response.contains(",")) {
						visitetime.count();
						if (visitetime.isOut2()) {
							sc_MyHandler.getInstance().sendEmptyMessage(-14);
							Sc_MyApplication.getInstance().getProgressdialog()
									.dismiss();
							return;
						} else {
							if (visitetime.isInit()) {
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
						ContentValues values = new ContentValues();
						loop2: for (int j = 0; j < count; j++) {
							if (j == count - 1) {
								values.put("uploadid", uploadid);
								values.put("block_start", j * BLOCKLENGTH_PHOTO);
								values.put("block_end", file.length());
								values.put("status", 0);
								values.put("indexid", j + 1);
							} else {
								values.put("uploadid", uploadid);
								values.put("block_start", j * BLOCKLENGTH_PHOTO);
								values.put("block_end", (j + 1)
										* BLOCKLENGTH_PHOTO);
								values.put("status", 0);
								values.put("indexid", j + 1);
							}
							try {
								sc_DBHelper.getInstance().insert(
										"uploadblockinfo", values);
								sc_DBHelper.getInstance().close();
							} catch (final Exception e) {
								new Thread(new Runnable() {
									@Override
									public void run() {
										sc_LogUtil.sendLog(2, "上传照片数据库存储异常："+e.getMessage());
									}
								}).start();
								j--;
								break loop2;
							}
						}
						Date date = new Date();
						SimpleDateFormat df = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");// 设置显示格式
						String nowTime = df.format(date);
						
						ContentValues values1 = new ContentValues();
						values1.put("uploadid", uploadid);
						values1.put("update_time", sc_StringUtils.formatCalendar(nowTime,
								Sc_MyApplication.ERROR_VALUE));
						values1.put("block_count", count);	
						
						
						ContentValues values2 = new ContentValues();
						values2.put("uploadid", uploadid);
						values2.put("photoid", photoid);
						values2.put("filename", savepath);		
						
						
						sc_DBHelper.getInstance().update("uploadinfo", values1, "_id=?", new String[]{_id});
						sc_DBHelper.getInstance().update("claimphotoinfo", values2, "legendid=? and claimid = ?", new String[]{selectresult.get(i).get("legendid")
								.toString(),selectresult.get(i).get("claimid")
								.toString()});
//						DBHelper.getInstance().execSql(
//								"update uploadinfo set uploadid ='"
//										+ uploadid
//										+ "',update_time='"
//										+ StringUtils.formatCalendar(nowTime,
//												MyApplication.ERROR_VALUE)
//										+ "',block_count=" + count
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
					uploadid = selectresult.get(i).get("uploadid").toString();
				}
				// 查询每一个文件中的每一块儿
				String sql = "select uinfo.*,cinfo.filename "
						+ "from uploadblockinfo uinfo join"
						+ " claimphotoinfo cinfo on "
						+ "uinfo.uploadid = cinfo.uploadid "
						+ "where uinfo.uploadid = '" + uploadid
						+ "' and (uinfo.status = 0 " + "or uinfo.status = 3)";
				List<Map<String, Object>> selectresult1 = sc_DBHelper
						.getInstance().selectRow(sql, null);

				// 开启进度条
				Sc_MyApplication.getInstance().setProgressbarmax(
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
				sc_MyHandler.getInstance().sendMessage(msg);
				 System.out.println(Sc_MyApplication.getInstance().getClaimid()
				 + "图片正在上传----------图例为："
				 +
				 selectresult.get(i).get("legendid").toString()+"块的数量为："+selectresult1.size());

				if (selectresult1.size() > 0) {
					for (int j = 0; j < selectresult1.size(); j++) {
						 System.out.println("i的值为：-----------" + i);
						if (isstop) {
							Sc_MyApplication.getInstance().setUploadon(false);
							Sc_MyApplication.getInstance().getProgressdialog()
									.dismiss();
							return;
						}
						// List<BasicNameValuePair> params = new
						// ArrayList<BasicNameValuePair>();
						// params.add(new BasicNameValuePair("appid",
						// MyApplication.APPID));
						// params.add(new BasicNameValuePair("appkey",
						// MyApplication.APPKEY));
						// params.add(new
						// BasicNameValuePair("action","upload"));
						// params.add(new BasicNameValuePair("IMEI",
						// PhoneInfo.getIMEI(MyApplication
						// .getInstance().getContext())));
						// params.add(new BasicNameValuePair("blockIndex",
						// selectresult1.get(j).get("indexid").toString()));
						// params.add(new BasicNameValuePair("blockCount",
						// count + ""));
						// params.add(new BasicNameValuePair("start",
						// selectresult1.get(j).get("block_start")
						// .toString()));
						// params.add(new BasicNameValuePair("uploadid",
						// selectresult1.get(j).get("uploadid")
						// .toString()));
						// params.add(new BasicNameValuePair("path",
						// selectresult1.get(j).get("filename")
						// .toString()));
						Map<String, String> params = new HashMap<String, String>();
						params.put("appid", Sc_MyApplication.APPID);
						params.put("appkey", Sc_MyApplication.APPKEY);
						// params.put("guoqing", "bcd");
						params.put("action", "upload");
						params.put("IMEI", sc_PhoneInfo.getIMEI(Sc_MyApplication
								.getInstance().getContext()));
						params.put("blockIndex",
								selectresult1.get(j).get("indexid").toString());
						params.put("blockCount", count + "");
						params.put("start",
								selectresult1.get(j).get("block_start")
										.toString());
						params.put("uploadid",
								selectresult1.get(j).get("uploadid").toString());
						params.put("path", selectresult1.get(j).get("filename")
								.toString());
						int start = Integer.parseInt(selectresult1.get(j)
								.get("block_start").toString());
						int end = Integer.parseInt(selectresult1.get(j)
								.get("block_end").toString());
						Map<String, File> filesmap = new HashMap<String, File>();
						filesmap.put(file.getName(), file);
						String reponse = "初始值";
						try {
							reponse = sc_UploadFile.post(Sc_MyApplication.URL
									+ "uploader", params, filesmap, start, end,
									selectresult.get(i).get("legendid")
											.toString());
							 System.out.println(params+"---"+reponse);
							if (params != null) {
								params.clear();
								params = null;
							}
							if (reponse.trim().equals("") || reponse == null) {
								visitetime.count();
								if (visitetime.isOut2()) {
									sc_MyHandler.getInstance()
											.sendEmptyMessage(-14);
									Sc_MyApplication.getInstance()
											.getProgressdialog().dismiss();
									return;
								} else {
									if (visitetime.isInit()) {
										visitetime.init();
									}
								}
								j--;
								continue;
							}
							int reponsecode = -1;
							try {
								reponsecode = Integer.parseInt(reponse);
							} catch (final Exception e) {
								new Thread(new Runnable() {
									@Override
									public void run() {
										sc_LogUtil.sendLog(2, "请求返回值异常："+e.getMessage());
									}
								}).start();
								e.printStackTrace();
								visitetime.count();
								if (visitetime.isOut2()) {
									sc_MyHandler.getInstance()
											.sendEmptyMessage(-14);
									Sc_MyApplication.getInstance()
											.getProgressdialog().dismiss();
									return;
								} else {
									if (visitetime.isInit()) {
										visitetime.init();
									}
								}
								j--;
								continue;
							}
							if (reponsecode == 0) {
								System.out.println(Sc_MyApplication.getInstance()
										.getProgressdialog().isShowing()
										+ "----------");
								System.out.println("第"
										+ selectresult1.get(j).get("indexid")
												.toString() + "块上传成功");
								ContentValues values = new ContentValues();
								values.put("status", "2");
								sc_DBHelper.getInstance().update(
										"uploadblockinfo",
										values,
										"uploadid=? and indexid= ?",
										new String[] {
												uploadid,
												selectresult1.get(j)
														.get("indexid")
														.toString() });
								// DBHelper.getInstance().execSql(
								// "update uploadblockinfo set status ="
								// + 2
								// + " where uploadid= '"
								// + uploadid
								// + "' and indexid="
								// + Integer
								// .parseInt(selectresult1
								// .get(j)
								// .get("indexid")
								// .toString()));
							} else {
								 System.out.println("获取的服务器的响应值为"+reponse);
								 System.out.println("第"
								 + selectresult1.get(j).get("indexid")
								 .toString() + "块上传不成功");
								visitetime.count();
								if (visitetime.isOut2()) {
									sc_MyHandler.getInstance()
											.sendEmptyMessage(-14);
									Sc_MyApplication.getInstance()
											.getProgressdialog().dismiss();
									return;
								} else {
									if (visitetime.isInit()) {
										visitetime.init();
									}
								}
								j--;
								continue;
							}
						} catch (final IOException e) {
							new Thread(new Runnable() {
								@Override
								public void run() {
									sc_LogUtil.sendLog(2, "上传文件出现IO异常："+e.getMessage());
								}
							}).start();
							visitetime.count();
							if (visitetime.isOut2()) {
								sc_MyHandler.getInstance().sendEmptyMessage(-14);
								Sc_MyApplication.getInstance().getProgressdialog()
										.dismiss();
								return;
							} else {
								if (visitetime.isInit()) {
									visitetime.init();
								}
							}
							 System.out.println("获取的服务器的响应值为"+reponse);
							e.printStackTrace();
							j--;
							continue;
						}

					}

					// 查询每一个文件中的每一块儿
					String sql2 = "select uinfo.*,cinfo.filename "
							+ "from uploadblockinfo uinfo join"
							+ " claimphotoinfo cinfo on "
							+ "uinfo.uploadid = cinfo.uploadid "
							+ "where uinfo.uploadid = '" + uploadid
							+ "' and (uinfo.status = 0 "
							+ "or uinfo.status = 3)";
					List<Map<String, Object>> selectresult2 = sc_DBHelper
							.getInstance().selectRow(sql2, null);
					try {
						if (selectresult2.size() <= 0) {
							ContentValues values = new ContentValues();
							values.put("status", 2);
							sc_DBHelper.getInstance().update("uploadinfo", values, "uploadid=?", new String[]{uploadid});
							values.clear();
							values.put("review_result", 3);
							sc_DBHelper.getInstance().update("claimphotoinfo", values, "uploadid=? and claimid = ? and legendid= ?"
									, new String[]{uploadid,Sc_MyApplication.getInstance()
									.getClaimid(),selectresult.get(i)
									.get("legendid").toString()});
							// 关闭进度条
							Message msg1 = new Message();
							Bundle data1 = new Bundle();
							data1.putString("legendid", selectresult.get(i)
									.get("legendid").toString());
							msg1.what = -5;
							msg1.setData(data1);
							sc_MyHandler.getInstance().sendMessage(msg1);
							sc_EncryptPhoto.Encrypt(file);
						}
					} catch (final Exception e) {
						new Thread(new Runnable() {
							@Override
							public void run() {
								sc_LogUtil.sendLog(2, "上传文件出现异常："+e.getMessage());
							}
						}).start();
					}
				} else if (selectresult1.size() == 0) {
					ContentValues values = new ContentValues();
					values.put("status", 2);
					sc_DBHelper.getInstance().update("uploadinfo", values, "uploadid=?", new String[]{uploadid});
					Message msg1 = new Message();
					Bundle data1 = new Bundle();
					data1.putString("legendid",
							selectresult.get(i).get("legendid").toString());
					msg1.what = -5;
					msg1.setData(data1);
					sc_MyHandler.getInstance().sendMessage(msg1);
					sc_EncryptPhoto.Encrypt(file);
				}
				i++;
			}
			selectresult.clear();
			selectresult = checkData();
			if (selectresult.size() <= 0) {
				Sc_MyApplication.getInstance().setUploadon(false);
				sc_SurveySelfInterface survey = (sc_SurveySelfInterface) Sc_MyApplication
						.getInstance().getContext();
				survey.SurveySelf();
				selectresult.clear();
				selectresult = null;
				isstop = true;
			} else {
				isstop = false;
			}
		}
	}

	private List<Map<String, Object>> checkData() {
		String sql = "";
		if (Sc_MyApplication.getInstance().getStepstate() == 0) {
			sql = "select distinct uinfo.*,cinfo.type,cinfo.photoid from uploadinfo uinfo inner join claimphotoinfo cinfo on uinfo.legendid = cinfo.legendid and uinfo.claimid = cinfo.claimid and uinfo.type=cinfo.type where uinfo.claimid = '"
					+ Sc_MyApplication.getInstance().getClaimid()
					+ "' and (uinfo.status =0 or uinfo.status =3) and cinfo.type =1";
		} else {
			sql = "select distinct uinfo.*,cinfo.type,cinfo.photoid from uploadinfo uinfo inner join claimphotoinfo cinfo on uinfo.legendid = cinfo.legendid and uinfo.claimid = cinfo.claimid and uinfo.type=cinfo.type where uinfo.claimid = '"
					+ Sc_MyApplication.getInstance().getClaimid()
					+ "' and (uinfo.status =0 or uinfo.status =3) and cinfo.type =2 ";
		}
		List<Map<String, Object>> selectresult = sc_DBHelper.getInstance()
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
		} catch (final Exception e) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					sc_LogUtil.sendLog(2, "请求服务器失败："+e.getMessage());
				}
			}).start();
			return "-1";
		}

		return strResult;
	}

}
