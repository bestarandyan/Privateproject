package com.example.riversystem;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

/**
 * content provider
 * @author Administrator
 *
 */
public class RiverContentProvider extends ContentProvider{
	


	private static final String PROVIDER_NAME="com.example";
	
	protected static final Uri CONTENT_URI=Uri.parse("content://"+PROVIDER_NAME+"/river");
	protected static final String _ID="_id";
	protected static final String NAME="name";
	protected static final String LENGTH="length";
	protected static final String INTRODUCTION="introduction";
	protected static final String IMAGE_URL="image_url";
	
	private static SQLiteDatabase database;
	private static final int DATABASE_VERSION=4;
	private static final int ITEMS=1;
	private static final int ITEM=2;

	private static UriMatcher uriMatcher;
	
	static {
		uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(PROVIDER_NAME, "river/#", ITEM);
		uriMatcher.addURI(PROVIDER_NAME, "river/",ITEMS);
	}
	

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		Log.d("listview", "uri match getType");
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		Log.d("listview","on create cp yes");
		database=new RiverDataBaseHelper(getContext(),"rivers",null,
				DATABASE_VERSION).getWritableDatabase();
		return database!=null;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		Cursor cursor=null;
		switch(uriMatcher.match(uri)){//judge the result is one row data or  a vector
		case ITEMS:
			cursor=database.query("rivers", projection, selection, 
					selectionArgs, null, null, sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			return cursor;
		case ITEM:
			cursor=database.query("rivers", projection, _ID+"="
					+ uri.getPathSegments().get(1),selectionArgs,
					null,null,sortOrder);
			cursor.setNotificationUri(getContext().getContentResolver(), uri);
			return cursor;
		default:
			throw new IllegalArgumentException("unknow uri:"+ uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int returnValue=database.update("rivers", values, selection, 
				selectionArgs);
		getContext().getContentResolver().notifyChange(uri,null);
		return returnValue;
	}

	private static class RiverDataBaseHelper extends SQLiteOpenHelper{
		
		private List<River> rivers=new ArrayList<River>();

		public RiverDataBaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		private void initRiverList(){
			River river=new River(
					"长江",6380,
					"长江（古称江、大江等）位于中国境内，发源于中国青海省唐古拉山各拉丹东雪山的姜根迪如冰川中，是中国、亚洲第一大、世界第三大（同时也是长）河流，其长度仅次于尼罗河及亚马逊河，超过地球半径",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/5/58/Dusk_on_the_Yangtze_River.jpg/800px-Dusk_on_the_Yangtze_River.jpg");
			rivers.add(river);
			river=new River(
					"黄河",
					5464,
					"中国古代称河，发源于中国青海省巴颜喀拉山脉，流经青海、四川、甘肃、宁夏、内蒙古、陕西、山西、河南、山东数个省区，并于山东省东营市垦利县注入渤海，是中国第二长河，仅次于长江，也是世界第七长河流",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Hukou_Waterfall.jpg/800px-Hukou_Waterfall.jpg");
			rivers.add(river);
			river = new River("辽河", 1390,
					"辽河是中华人民共和国东北地区南部的大河，流经河北、内蒙古、吉林、辽宁等省区。",
					"http://imgsrc.baidu.com/baike/pic/item/389aa8fdb7b8322e08244d3c.jpg");
			rivers.add(river);

			river = new River(
					"海河",
					73,
					"海河由于地势不高，每当海中涨潮时，河水湍流，落潮时河水顺流，和海水的潮汐相同，因此取名为'海河'。海河支流的永定河原名为永定定河，就是因为河道经常改变，只是皇帝为了祈愿它不再泛滥才人为改名为永定河",
					"http://upload.wikimedia.org/wikipedia/commons/thumb/6/66/%E7%82%AB%E5%BD%A9%E6%B4%A5%E9%97%A899%E6%B5%B7%E6%B2%B3%E5%A4%9C%E6%99%AF.jpg/800px-%E7%82%AB%E5%BD%A9%E6%B4%A5%E9%97%A899%E6%B5%B7%E6%B2%B3%E5%A4%9C%E6%99%AF.jpg");
			rivers.add(river);

			river = new River(
					"淮河",
					1000,
					"淮河发源于河南省桐柏山，东流经河南，安徽，江苏三省，淮河下游水分三路，主流通过三河闸，出三河，经宝应湖、高邮湖在三江营入长江，是为入江水道，至此全长约1,000公里；在洪泽湖东岸出高良涧闸，经苏北灌溉渠在扁担港入黄海；第三路在洪泽湖东北岸出二河闸，经淮沭河北上连云港市，经临洪口注入海州湾,2003年开通淮河入海水道，自二河闸下游，紧贴苏北灌溉渠北岸入海。",
					"http://imgsrc.baidu.com/baike/pic/item/d8b8c92a9d091b69d42af1b2.jpg");
			rivers.add(river);

			river = new River(
					"黑龙江",
					4444,
					"发源于蒙古国肯特山东麓，在石勒喀河与额尔古纳河交汇处形成。经过中国黑龙江省北界与俄罗斯哈巴罗夫斯克边疆区东南界，流入鄂霍次克海鞑靼海峡,黑龙江是中国三大河流之一、世界十大河之一（有些资料计为第六）。黑龙江本是中国的内河，19世纪中后期沙俄强行占领中国黑龙江以北、乌苏里江以东大片领土之后，才成为中俄界河,2004年，中国和俄罗斯签署边界协定，将两国国界以黑龙江为基本界限划清",
					"http://imgsrc.baidu.com/baike/pic/item/d56b36344a36c27a5ab5f554.jpg");
			rivers.add(river);

			river = new River(
					"珠江",
					2400,
					"原指广州到入海口的一段河道，后来逐渐成为西江、北江、东江和珠江三角洲诸河的其干流西江发源于云南省东北部沾益县的马雄山，干流流经云南、贵州、广西、广东四省（自治区）及香港、澳门特别行政区",
					"http://upload.wikimedia.org/wikipedia/commons/a/a3/Pearl_river%2C_Guangzhou.JPG");
			rivers.add(river);			
		}
		@Override
		public void onCreate(SQLiteDatabase database) {
			initRiverList();
			database.execSQL("create table if not exists rivers("
					+" _id integer primary key autoincrement,"+" name text,"
					+"length integer,"+" introduction text,"
					+"image_url text"+");");
			
			SQLiteStatement statement=database.compileStatement("insert into rivers(name,length,introduction,image_url) values (?,?,?,?)");
			
			for(River r:rivers){
				int index=1;
				statement.bindString(index++,r.getName());
				statement.bindLong(index++,r.getLength());
				statement.bindString(index++,r.getIntroduction());
				statement.bindString(index++,r.getImageUrl());
				statement.executeInsert();
			}
			statement.close();
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			database.execSQL("drop table if exists rivers");
			onCreate(database);
		}

	}	
}
