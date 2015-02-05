package com.qingfengweb.client.database;

import com.qingfengweb.client.bean.CaseInfo;
import com.qingfengweb.client.bean.CompanyInfo;
import com.qingfengweb.client.bean.ConnectWayInfo;
import com.qingfengweb.client.bean.ContentUpdateInfo;
import com.qingfengweb.client.bean.JobListInfo;
import com.qingfengweb.client.bean.ListCaseInfo;
import com.qingfengweb.client.bean.DetailServicesInfo;
import com.qingfengweb.client.bean.DeviceInfo;
import com.qingfengweb.client.bean.HomeImageInfo;
import com.qingfengweb.client.bean.ImageInfo;
import com.qingfengweb.client.bean.PositionInfo;
import com.qingfengweb.client.bean.ServicesInfo;
import com.qingfengweb.client.bean.StaffInfo;
import com.qingfengweb.client.bean.TeamInfo;
import com.qingfengweb.client.bean.UpLoadFileInfo;
import com.qingfengweb.client.bean.UpdateSystemTime;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author
 * database ����
 */
public class SQLiteHelper extends SQLiteOpenHelper {
	/**
	 * ���ݿ�����
	 */
	private static final String DATABASENAME = "qingfengweb.db";
	/**
	 *���ݿ�汾
	 */
	private static final int VERSION = 1;
	/**
	 * �������ݿ�
	 * @param context
	 */
	public SQLiteHelper(Context context) {
		super(context, DATABASENAME, null, VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DeviceInfo.TABLE_CREATE_SQL);//�����豸��
		db.execSQL(UpdateSystemTime.TABLE_CREATE_SQL);//�������»��Ʊ�
		db.execSQL(DetailServicesInfo.createTableSQL);//�������������
		db.execSQL(HomeImageInfo.createTableSQL);//������ҳͼƬ��Ϣ��
		db.execSQL(ContentUpdateInfo.createTableSQL);//�������ݸ��±�
		db.execSQL(CompanyInfo.createTableSQL);//������˾���ܱ�
		db.execSQL(TeamInfo.createTableSQL);//�����Ŷӳ�Ա��Ϣ��
		db.execSQL(CaseInfo.createTableSQL);//�����ͻ������б���Ϣ��
		db.execSQL(JobListInfo.createTableSQL);//����ְλ�б���Ϣ��
		db.execSQL(UpLoadFileInfo.CreateTabSQL);//�����ļ��ϴ���
		db.execSQL(ServicesInfo.createTabSQL);//����������Ŀ��
		db.execSQL(ConnectWayInfo.createTabSQL);
//		db.execSQL(StaffInfo.TABLE_CREATE);//Ա����
//		db.execSQL(ListCaseInfo.CREATE_TABLE_SQL);//������
//		db.execSQL(ImageInfo.TABLE_CREATE);//ͼƬ��Ϣ��
//		db.execSQL(PositionInfo.TABLE_CREATE);//ְλ��
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}
