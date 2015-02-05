package com.qingfengweb.baoqi.collectInfo;

import java.io.File;

import android.os.Environment;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

public class FinalValues{
	/*������ʶ�������๦�ܵ�activity*/  
	public static final int CAMERA_WITH_DATA = 3023;  

	/*������ʶ����gallery��activity*/  
	public static final int PHOTO_PICKED_WITH_DATA = 3021;  

	/*���յ���Ƭ�洢λ��*/  
	public static final File PHOTO_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera");  
	public static final File PHOTO_BIAODIWU_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/BiaoDiWu");
	public static final File PHOTO_IDCARD_ZHENGMIAN_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Idcard/zhengmian");
	public static final File PHOTO_IDCARD_FANMIAN_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Idcard/fanmian");
	public static final File PHOTO_COLLECT_SUOPEI_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/SuoPeiShenQingShu");
	public static final File PHOTO_COLLECT_JIASHI_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/JiaShiZheng");
	public static final File PHOTO_COLLECT_PEIKZHJIAN_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/LingQuPeiKuangZhengJian");
	public static final File PHOTO_COLLECT_SURVEY_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/XianChangSurvey");
	public static final File PHOTO_COLLECT_BAODANZHENGBEN_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/BaoXianDanZhengBen");
	public static final File PHOTO_COLLECT_SHIGUCHULI_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/ShiGuChuLiZhengMing");
	public static final File PHOTO_COLLECT_FAYUANZHENGMING_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/FaYuanZhengMing");
	public static final File PHOTO_COLLECT_CHESUN_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/CheSunZiLiao");
	public static final File PHOTO_COLLECT_CAICHANSUNSHI_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/CaiChanSunZiLiao");
	public static final File PHOTO_COLLECT_PERSONSUNSHI_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/RenShangSun");
	public static final File PHOTO_COLLECT_CHELIANGDAOQIANG_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/CheLiangDaoQiang");
	public static final File PHOTO_COLLECT_QITA_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/Camera/Collect/CarInsure/QiTa");
	public static final String SDCARD_ROOT_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();//·��   
    
	public static final String SAVE_PATH_IN_SDCARD = "/bi.data/"; //ͼƬ���������ݱ����ļ���   
    public static final String IMAGE_CAPTURE_NAME = "cameraTmp.png"; //��Ƭ����   
    public final static int REQUEST_CODE_TAKE_PICTURE = 12;//�������ղ����ı�־  
    
    public final static int SCREEN_CAMERA_WIDTH=600;
    public final static int SCREEN_CAMERA_HEIGHT=350;
    
}
