/**
 * 
 */
package com.qingfengweb.weddingideas.beans;

/**
 * @author 刘星星
 * @createDate 2014、1、11
 *  样照实体类
 */
public class YangZhaoBean {
		public static String tbName = "yangzhaodata";
		public static String tbCreateSql = "create table if not exists "+tbName+"" +
				"(_id Integer primary key autoincrement," +
				"id text," +
				"storeid text," +
				"s_name text," +
				"c_photo text," +
				"log_type_two text," +
				"photo_one text," +
				"photo_two text," +
				"photo_thr text," +
				"photo_for text," +
				"photo_fiv text," +
				"p_desc text," +
				"opt_time text," +
				"opt_status varchar(10))";
		public String id = "";
		public String s_name = "";
		public String c_photo = "";
		public String log_type_two = "";
		public String photo_one = "";
		public String photo_two = "";
		public String photo_thr = "";
		public String photo_for = "";
		public String photo_fiv = "";
		public String p_desc = "";
		public String opt_time = "";
		public String opt_status = "";
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
		public String getLog_type_two() {
			return log_type_two;
		}
		public void setLog_type_two(String log_type_two) {
			this.log_type_two = log_type_two;
		}
		public String getPhoto_one() {
			return photo_one;
		}
		public void setPhoto_one(String photo_one) {
			this.photo_one = photo_one;
		}
		public String getPhoto_two() {
			return photo_two;
		}
		public void setPhoto_two(String photo_two) {
			this.photo_two = photo_two;
		}
		public String getPhoto_thr() {
			return photo_thr;
		}
		public void setPhoto_thr(String photo_thr) {
			this.photo_thr = photo_thr;
		}
		public String getPhoto_for() {
			return photo_for;
		}
		public void setPhoto_for(String photo_for) {
			this.photo_for = photo_for;
		}
		public String getPhoto_fiv() {
			return photo_fiv;
		}
		public void setPhoto_fiv(String photo_fiv) {
			this.photo_fiv = photo_fiv;
		}
		public String getP_desc() {
			return p_desc;
		}
		public void setP_desc(String p_desc) {
			this.p_desc = p_desc;
		}
		public String getOpt_time() {
			return opt_time;
		}
		public void setOpt_time(String opt_time) {
			this.opt_time = opt_time;
		}
		public String getOpt_status() {
			return opt_status;
		}
		public void setOpt_status(String opt_status) {
			this.opt_status = opt_status;
		}
		
		
		
		
}
