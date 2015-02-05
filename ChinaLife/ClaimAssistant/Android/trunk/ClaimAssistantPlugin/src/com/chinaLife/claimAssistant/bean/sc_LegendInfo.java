package com.chinaLife.claimAssistant.bean;

import java.util.List;
import java.util.Map;

import com.chinaLife.claimAssistant.activity.R;
import com.chinaLife.claimAssistant.database.sc_DBHelper;

public class sc_LegendInfo {
	/**
	 * 图例信息表
	 */
	public static String TABLE_CREATE = "create table " +
			"legendinfo" +//表名legendinfo
			"("+ 
			"_id integer primary key autoincrement,"+//自动增长_id
			"legendid text,"+//图例id
			"type integer,"+//图例类型：1-拍照图例2-单证图例
			"code text,"+//图例代码
			"legendimageurl text,"+//图例图片url
			"legendimagesd text,"+//图例图片sd卡位置
			"legendtext text,"+//图例文字描述
			"maskimageurl text,"+//蒙版图片url
			"maskimagesd text,"+//蒙版图片sd卡位置
			"masktext text,"+//蒙版文字描述
			"updatetime text,"+//更新时间
			"remark text"+//图例备注
			")";
	
	/**
	 * 删除该表
	 */
	public static String TABLE_DELETE = "DROP TABLE IF EXISTS legendinfo";
	
	private String legendid;
	private String status;
	private String type;
	private String code;
	private String exampleimage;
	private String maskimage;
	private String ranking;
	private String name;
	private String remark;
	private String maskimagedescription;
	private String updatetime;
	
	public sc_LegendInfo(){
		setLegendid("");
		setStatus("");
		setType("");
		setCode("");
		setExampleimage("");
		setMaskimage("");
		setRanking("");
		setName("");
		setRemark("");
		setMaskimagedescription("");
		setUpdatetime("");
	}
	public String getLegendid() {
		return legendid;
	}
	public void setLegendid(String legendid) {
		this.legendid = legendid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getExampleimage() {
		return exampleimage;
	}
	public void setExampleimage(String exampleimage) {
		this.exampleimage = exampleimage;
	}
	public String getMaskimage() {
		return maskimage;
	}
	public void setMaskimage(String maskimage) {
		this.maskimage = maskimage;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMaskimagedescription() {
		return maskimagedescription;
	}
	public void setMaskimagedescription(String maskimagedescription) {
		this.maskimagedescription = maskimagedescription;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	
	
	/**
	 * 获取图例名称
	 * @return
	 */
	public static String getLegendText(String legendid){
		String legendtext = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = sc_DBHelper.getInstance().selectRow(
				"select legendtext from legendinfo where legendid = '"+legendid+"'", null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(
							"legendtext") != null) {
				legendtext = (selectresult.get(selectresult.size() - 1).get(
						"legendtext").toString());
			}
		}
		return legendtext;
	}
	
	/**
	 * 获取图例图片
	 * @return
	 */
	public static int getLegendImage(String legendid){
		int sourceimage = 0;
		int mylegendid = 0;
		try {
			mylegendid = Integer.parseInt(legendid);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(mylegendid>=1&&mylegendid<=13){
			List<Map<String, Object>> selectresult = null;
			selectresult = sc_DBHelper.getInstance().selectRow(
					"select legendimagesd from legendinfo where legendid = '"+legendid+"'", null);
			if (selectresult != null && selectresult.size() > 0) {
				if (selectresult.get(selectresult.size() - 1) != null
						&& selectresult.get(selectresult.size() - 1).get(
								"legendimagesd") != null) {
					sourceimage = Integer.parseInt((selectresult.get(selectresult.size() - 1).get(
							"legendimagesd").toString()));
				}
			}
		}else{
			sourceimage = R.drawable.sc_photo13;
		}
		return sourceimage;
	}
	
	/**
	 * 获取蒙版图片
	 * @return
	 */
	public static String getMasktext(String legendid){
		String masktext = "";
		List<Map<String, Object>> selectresult = null;
		selectresult = sc_DBHelper.getInstance().selectRow(
				"select masktext from legendinfo where legendid = '"+legendid+"'", null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(
							"masktext") != null) {
				masktext = (selectresult.get(selectresult.size() - 1).get(
						"masktext").toString());
			}
		}
		return masktext;
	}
	
	/**
	 * 获取蒙版图片
	 * @return
	 */
	public static int getMaskImage(String legendid){
		int sourceimage = 0;
		List<Map<String, Object>> selectresult = null;
		selectresult = sc_DBHelper.getInstance().selectRow(
				"select maskimagesd from legendinfo where legendid = '"+legendid+"'", null);
		if (selectresult != null && selectresult.size() > 0) {
			if (selectresult.get(selectresult.size() - 1) != null
					&& selectresult.get(selectresult.size() - 1).get(
							"maskimagesd") != null) {
				sourceimage = Integer.parseInt((selectresult.get(selectresult.size() - 1).get(
						"maskimagesd").toString()));
			}
		}
		return sourceimage;
	}
	

	
}
