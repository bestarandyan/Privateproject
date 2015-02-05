package com.chinaLife.claimAssistant.database;
import com.chinaLife.claimAssistant.bean.sc_AppInfo;
import com.chinaLife.claimAssistant.bean.sc_Caseinfo;
import com.chinaLife.claimAssistant.bean.sc_ClaimInfo;
import com.chinaLife.claimAssistant.bean.sc_ClaimPhotoInfo;
import com.chinaLife.claimAssistant.bean.sc_HelpInfo;
import com.chinaLife.claimAssistant.bean.sc_LegendInfo;
import com.chinaLife.claimAssistant.bean.sc_LogInfo;
import com.chinaLife.claimAssistant.bean.sc_MessageInfo;
import com.chinaLife.claimAssistant.bean.sc_NoticeInfo;
import com.chinaLife.claimAssistant.bean.sc_SynDataTime;
import com.chinaLife.claimAssistant.bean.sc_UploadBlockInfo;
import com.chinaLife.claimAssistant.bean.sc_UploadInfo;
import com.chinaLife.claimAssistant.bean.sc_UserInfo;
import com.chinaLife.content.sc_Contentvalues;
import com.sqlcrypt.database.ContentValues;
import com.sqlcrypt.database.sqlite.SQLiteDatabase;
import com.sqlcrypt.database.sqlite.SQLiteOpenHelper;

import android.content.Context;

/**
 * @author
 * database 管理
 */
public class sc_SQLiteHelper extends SQLiteOpenHelper {
	/**
	 * 数据库名称
	 */
	private static final String DATABASENAME = "chinalife.db";
	/**
	 *数据库版本
	 */
	private static final int VERSION = 3;
	/**
	 * 构造数据库
	 * @param context
	 */
	public sc_SQLiteHelper(Context context) {
		super(context, DATABASENAME, "123", null, VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(sc_AppInfo.TABLE_CREATE);
		db.execSQL(sc_UserInfo.TABLE_CREATE);
		db.execSQL(sc_Caseinfo.TABLE_CREATE);
		db.execSQL(sc_ClaimInfo.TABLE_CREATE);
		db.execSQL(sc_ClaimPhotoInfo.TABLE_CREATE);
		db.execSQL(sc_UploadInfo.TABLE_CREATE);
		db.execSQL(sc_UploadBlockInfo.TABLE_CREATE);
		db.execSQL(sc_LegendInfo.TABLE_CREATE);
		db.execSQL(sc_SynDataTime.TABLE_CREATE);
		db.execSQL(sc_MessageInfo.TABLE_CREATE);
		db.execSQL(sc_NoticeInfo.TABLE_CREATE);
		db.execSQL(sc_HelpInfo.TABLE_CREATE);
		db.execSQL(sc_LogInfo.TABLE_CREATE);
		
		
//		"legendid text,"+//图例id
//		"type integer,"+//图例类型：1-拍照图例2-单证图例
//		"code text,"+//图例代码
//		"legendimageurl text,"+//图例图片url
//		"legendimagesd text,"+//图例图片sd卡位置
//		"legendtext text,"+//图例文字描述
//		"maskimageurl text,"+//蒙版图片url
//		"maskimagesd text,"+//蒙版图片sd卡位置
//		"masktext text,"+//蒙版文字描述
//		"remark text"+//图例备注
		int i;
		ContentValues values = new ContentValues();
		for(i=1;i<=13;i++){
			values.clear();
			values.put("legendid",i);
			values.put("legendimageurl", "");
			values.put("legendimagesd", sc_Contentvalues.LegendImage[i-1]);
			values.put("legendtext", sc_Contentvalues.Legendtext[i-1]);
			values.put("maskimageurl", "");
			values.put("maskimagesd", sc_Contentvalues.Legend1Image[i-1]);
			values.put("masktext", sc_Contentvalues.Legendtext1[i-1]);
			db.insert("legendinfo", null, values);
		}
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("ALTER TABLE claiminfo ADD COLUMN iscomfirm integer");// 是否将款项划到账号上；0不选，1选
//		db.execSQL("ALTER TABLE claiminfo ADD COLUMN bankcode text");// 总行代码
//		db.execSQL("ALTER TABLE claiminfo ADD COLUMN bankphonenumber text");// 手机号
		db.execSQL("drop table if exists appinfo");
		db.execSQL("drop table if exists userinfo");
		db.execSQL("drop table if exists caseinfo");
		db.execSQL("drop table if exists claiminfo");
		db.execSQL("drop table if exists claimphotoinfo");
		db.execSQL("drop table if exists uploadinfo");
		db.execSQL("drop table if exists uploadblockinfo");
		db.execSQL("drop table if exists legendinfo");
		db.execSQL("drop table if exists syndatatime");
		db.execSQL("drop table if exists messageinfo");
		db.execSQL("drop table if exists noticeinfo");
		db.execSQL("drop table if exists helpinfo");
		db.execSQL("drop table if exists loginfo");
		db.execSQL(sc_AppInfo.TABLE_CREATE);
		db.execSQL(sc_UserInfo.TABLE_CREATE);
		db.execSQL(sc_Caseinfo.TABLE_CREATE);
		db.execSQL(sc_ClaimInfo.TABLE_CREATE);
		db.execSQL(sc_ClaimPhotoInfo.TABLE_CREATE);
		db.execSQL(sc_UploadInfo.TABLE_CREATE);
		db.execSQL(sc_UploadBlockInfo.TABLE_CREATE);
		db.execSQL(sc_LegendInfo.TABLE_CREATE);
		db.execSQL(sc_SynDataTime.TABLE_CREATE);
		db.execSQL(sc_MessageInfo.TABLE_CREATE);
		db.execSQL(sc_NoticeInfo.TABLE_CREATE);
		db.execSQL(sc_HelpInfo.TABLE_CREATE);
		db.execSQL(sc_LogInfo.TABLE_CREATE);
		
		int i;
		ContentValues values = new ContentValues();
		for(i=1;i<=13;i++){
			values.clear();
			values.put("legendid", i);
			values.put("legendimageurl", "");
			values.put("legendimagesd", sc_Contentvalues.LegendImage[i-1]);
			values.put("legendtext", sc_Contentvalues.Legendtext[i-1]);
			values.put("maskimageurl", "");
			values.put("maskimagesd", sc_Contentvalues.Legend1Image[i-1]);
			values.put("masktext", sc_Contentvalues.Legendtext1[i-1]);
			db.insert("legendinfo", null, values);
		}
	}

}
