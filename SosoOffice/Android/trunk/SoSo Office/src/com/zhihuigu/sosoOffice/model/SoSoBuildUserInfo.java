package com.zhihuigu.sosoOffice.model;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;

import com.zhihuigu.sosoOffice.database.DBHelper;

public class SoSoBuildUserInfo {
	/**author by Ring
	 * ¥����Ϣ��
	 */
	

	public static String TABLE_CREATE = "create table " +
			"soso_builduserinfo" +//���� soso_userinfo
			"("+ 
			"_id integer primary key autoincrement,"+//�Զ�����_id
			"buildid text,"+//¥��id
			"buildmc text,"+//¥������
			"userid text,"+//�û�id
			"buildtype integer,"+//¥������ 0,д��¥��1���̣�2��ҵ�ز�
			"address text"+//��ַ
			")";
	
	private String BuildID;
	private String BuildMC;
	private int BuildType;
	private String Address;
	private int IsUsed;
	private String UserID;
	
	public SoSoBuildUserInfo(){
		setAddress("");
		setBuildID("");
		setBuildMC("");
		setBuildType(0);
		setUserID("");
		setIsUsed(0);
	}
	
	public String getBuildID() {
		return BuildID;
	}

	public void setBuildID(String buildID) {
		BuildID = buildID;
	}

	public String getBuildMC() {
		return BuildMC;
	}

	public void setBuildMC(String buildMC) {
		BuildMC = buildMC;
	}

	public int getBuildType() {
		return BuildType;
	}

	public void setBuildType(int buildType) {
		BuildType = buildType;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public int getIsUsed() {
		return IsUsed;
	}

	public void setIsUsed(int isUsed) {
		IsUsed = isUsed;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	/**
	 * ��¥����Ϣ�������ݱ���
	 * @param context ������
	 * @param soSoBuildUserInfo ¥�̶���
	 * @return true ����ɹ���false ����ʧ��
	 */
	
	
	public boolean InsertBuildInfo(Context context,SoSoBuildUserInfo soSoBuildUserInfo){
		ContentValues values = new ContentValues();
		List<Map<String, Object>> selectresult = null;
		values.put("buildid", soSoBuildUserInfo.getBuildID());
		values.put("buildmc", soSoBuildUserInfo.getBuildMC());
		values.put("buildtype", soSoBuildUserInfo.getBuildType());
		values.put("address", soSoBuildUserInfo.getAddress());
		values.put("userid", soSoBuildUserInfo.getUserID());
		selectresult = DBHelper.getInstance(context).selectRow(
				"select * from soso_builduserinfo where buildid = '"
						+ soSoBuildUserInfo.getBuildID() + "' and userid = '"+soSoBuildUserInfo.getUserID()+"'", null);
		boolean b = false;
		if (selectresult != null) {
			if (selectresult.size() <= 0) {
				if (DBHelper.getInstance(context).insert("soso_builduserinfo",
						values)>0) {
					b =true ;
				}
			} else {
				b=DBHelper.getInstance(context)
						.update("soso_builduserinfo",
								values,
								"buildid = ? and userid =?",
								new String[] { soSoBuildUserInfo
										.getBuildID(),soSoBuildUserInfo.getUserID() });
			}
			selectresult.clear();
			selectresult = null;
		}
		values.clear();
		values=null;
		return b;
	}
	
	
	/**
	 * ��¥����Ϣ����¥�̱���
	 * @param context ������
	 * @param soSoBuildUserInfo ¥�̶����б�
	 * @return true ����ɹ���false ����ʧ��
	 */
	public void InsertBuildInfos(Context context,LinkedList<SoSoBuildUserInfo> soSoBuildUserInfos,String userid){
		if (soSoBuildUserInfos != null && soSoBuildUserInfos.size() > 0) {
			SoSoBuildUserInfo soSoBuildUserInfo = null;
			for (Iterator<SoSoBuildUserInfo> iterator = soSoBuildUserInfos
					.iterator(); iterator.hasNext();) {
				soSoBuildUserInfo = (SoSoBuildUserInfo) iterator.next();
				if (soSoBuildUserInfo.getIsUsed() == 1) {
					DBHelper.getInstance(context).delete("soso_builduserinfo",
							"buildid = ? and userid =?", new String[] { soSoBuildUserInfo
							.getBuildID(),soSoBuildUserInfo.getUserID() });
					continue;
				}
				soSoBuildUserInfo.setUserID(userid);
				soSoBuildUserInfo.InsertBuildInfo(context, soSoBuildUserInfo);
			}
			if (soSoBuildUserInfos != null) {
				soSoBuildUserInfos.clear();
				soSoBuildUserInfos = null;
			}
		}
		
		DBHelper.getInstance(context).close();
	}
	
	/**
	 * ���¥�̶���
	 * @param context ������
	 * @param soSoBuildUserInfo ¥�̶���
	 * @return true ����ɹ���false ����ʧ��
	 */
	public LinkedList<SoSoBuildUserInfo> SelectBuildInfo(Context context,String sql){
		List<Map<String, Object>> selectresult = null;
		LinkedList<SoSoBuildUserInfo> soSoBuildUserInfos = null;
		SoSoBuildUserInfo soSoBuildUserInfo = null;
		selectresult = DBHelper.getInstance(context).selectRow(sql, null);
		if(selectresult!=null&&selectresult.size()>0){
			soSoBuildUserInfos = new LinkedList<SoSoBuildUserInfo>();
			int i;
			for(i=0;i<selectresult.size();i++){
				soSoBuildUserInfo = new SoSoBuildUserInfo();
				soSoBuildUserInfo.setAddress(selectresult.get(i).get("address").toString());
				soSoBuildUserInfo.setBuildID(selectresult.get(i).get("buildid").toString());
				soSoBuildUserInfo.setBuildMC(selectresult.get(i).get("buildmc").toString());
				soSoBuildUserInfo.setUserID(selectresult.get(i).get("userid").toString());
				try {
					soSoBuildUserInfo.setBuildType(Integer.parseInt(selectresult
							.get(i).get("buildtype").toString()));
				} catch (Exception e) {
					soSoBuildUserInfo.setBuildType(0);
				}
				soSoBuildUserInfos.add(soSoBuildUserInfo);
			}
		}
		return soSoBuildUserInfos;
	}
	

}
