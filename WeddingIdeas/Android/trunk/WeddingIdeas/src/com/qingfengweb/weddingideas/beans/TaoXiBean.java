/**
 * 
 */
package com.qingfengweb.weddingideas.beans;

/**
 * @author qingfeng
 *
 */
public class TaoXiBean {
	public static String tbName = "taoxidata";
	public static String tbCreateSql ="create table if not exists "+tbName+"" +
			"(_id Integer primary key autoincrement," +
			"id text," +
			"storeid text," +
			"s_name text," +
			"c_photo text," +
			"photo_c text," +
			"ori_price text," +
			"price_dis text," +
			"price_cut text," +
			"set_desc text," +
			"opt_time text)";
		public String id = "";
		public String s_name = "";
		public String c_photo = "";
		public String photo_c = "";
		public String ori_price = "";
		public String price_dis = "";
		public String price_cut = "";
		public String set_desc = "";
		public String opt_time = "";
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getS_name() {
			return s_name;
		}
		public void setS_name(String s_name) {
			this.s_name = s_name;
		}
		public String getC_photo() {
			return c_photo;
		}
		public void setC_photo(String c_photo) {
			this.c_photo = c_photo;
		}
		public String getPhoto_c() {
			return photo_c;
		}
		public void setPhoto_c(String photo_c) {
			this.photo_c = photo_c;
		}
		public String getOri_price() {
			return ori_price;
		}
		public void setOri_price(String ori_price) {
			this.ori_price = ori_price;
		}
		public String getPrice_dis() {
			return price_dis;
		}
		public void setPrice_dis(String price_dis) {
			this.price_dis = price_dis;
		}
		public String getPrice_cut() {
			return price_cut;
		}
		public void setPrice_cut(String price_cut) {
			this.price_cut = price_cut;
		}
		public String getSet_desc() {
			return set_desc;
		}
		public void setSet_desc(String set_desc) {
			this.set_desc = set_desc;
		}
		public String getOpt_time() {
			return opt_time;
		}
		public void setOpt_time(String opt_time) {
			this.opt_time = opt_time;
		}
		
}
