package com.qingfengweb.pinhuo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qingfengweb.pinhuo.adapter.AreaSelectAdapter;
import com.qingfengweb.pinhuo.datamanage.AssetsDatabaseManager;

public class EditInfoActivity extends BaseActivity implements OnClickListener,OnItemClickListener{
	TextView startAddressTv,endAddressTv,typeCar,lengthCar;
	RelativeLayout openLayout1,openLayout2,openLayout3,openLayout4;
	private PopupWindow selectPopupWindow = null;//分享弹出框或者是设置弹出框
	ListView provinceListView,cityListView,discrictListView;
	List<Map<String,Object>> provinceData,cityData,discrictData;
	LinearLayout carLayout;//只有司机才会有的布局
	String typeUser = "2";
	Button nextBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);//设置当前界面为无标题界面
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_edit_info);
        initView();//初始化控件
        initData();
    }
	/**
	 * 控件初始化函数
	 * @author 刘星星
	 * @createDate 2014/3/31
	 */
    private void initView(){
    	startAddressTv = (TextView) findViewById(R.id.startAddressEt);
    	startAddressTv.setOnClickListener(this);
    	endAddressTv = (TextView) findViewById(R.id.endAddressEt);
    	endAddressTv.setOnClickListener(this);
    	typeCar = (TextView) findViewById(R.id.type_car);
    	typeCar.setOnClickListener(this);
    	lengthCar = (TextView) findViewById(R.id.length_car);
    	lengthCar.setOnClickListener(this);
    	nextBtn = (Button) findViewById(R.id.nextBtn);
    	nextBtn.setOnClickListener(this);
    	carLayout = (LinearLayout) findViewById(R.id.carLayout);
    	typeUser = getIntent().getStringExtra("typeUser");
    	findViewById(R.id.backBtn).setOnClickListener(this);
    	if(typeUser.equals("2")){//司机
			carLayout.setVisibility(View.VISIBLE);
		}else if(typeUser.equals("3")){//专线
			carLayout.setVisibility(View.GONE);
		}
    	openLayout1 = (RelativeLayout) findViewById(R.id.openLayout1);
    	openLayout2 = (RelativeLayout) findViewById(R.id.openLayout2);
    	openLayout3 = (RelativeLayout) findViewById(R.id.openLayout3);
    	openLayout4 = (RelativeLayout) findViewById(R.id.openLayout4);
    	openLayout1.setOnClickListener(this);
    	openLayout2.setOnClickListener(this);
    	openLayout3.setOnClickListener(this);
    	openLayout4.setOnClickListener(this);
    }
    /**
     * 初始化数据
     * @author 刘星星
	 * @createDate 2014/3/31
     */
    private void initData(){
    	provinceData = new ArrayList<Map<String,Object>>();
    	cityData = new ArrayList<Map<String,Object>>();
    	discrictData = new ArrayList<Map<String,Object>>();
    	selectProvinceData();//查找数据库中的省份
        
    	if(provinceData!=null && provinceData.size()>0){//将第一个省下面的市显示      
    		String id = provinceData.get(0).get("id").toString();
    		selectCityData(id);
    	}
    	
    	if(cityData!=null && cityData.size()>0){//将第一个市下面的所有区显示
    		String id = cityData.get(0).get("id").toString();
    		selectDiscrictData(id);
    	}
    }
    private void notifyProvinceAdapter(ListView listview,List<Map<String,Object>> provinceData){
    	AreaSelectAdapter adapter = new AreaSelectAdapter(this, provinceData);
    	listview.setAdapter(adapter);
    }
    
    
    /**
	 * 初始化分享弹出框
	 */
	public void showShareDialog(View parent) {
		// PopupWindow浮动下拉框布局
		View selectCity = (View) this.getLayoutInflater().inflate(R.layout.dialog_select_city, null);
		AreaSelectAdapter adapter = new AreaSelectAdapter(this, provinceData);
		provinceListView = (ListView) selectCity.findViewById(R.id.provinceListView);
		cityListView = (ListView) selectCity.findViewById(R.id.cityListView);
		discrictListView = (ListView) selectCity.findViewById(R.id.discrictListView);
		
		provinceListView.setAdapter(adapter);
//		View parentView = provinceListView.getch
//		View item = parentView.findViewById(R.id.view);  
//        item.setBackgroundColor(Color.parseColor("#FF7E00"));//把当前选中的条目加上选中效果  
		
		adapter = new AreaSelectAdapter(this, cityData);
		cityListView.setAdapter(adapter);
		adapter = new AreaSelectAdapter(this, discrictData);
		discrictListView.setAdapter(adapter);
		
		provinceListView.setOnItemClickListener(this);
		cityListView.setOnItemClickListener(this);
		discrictListView.setOnItemClickListener(this);
		
		selectPopupWindow = new PopupWindow(selectCity, parent.getWidth(),LayoutParams.WRAP_CONTENT, true);
		// 这一句是为了实现弹出PopupWindow后，当点击屏幕其他部分及Back键时PopupWindow会消失，
		// 没有这一句则效果不能出来，但并不会影响背景
		selectPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		selectPopupWindow.setOutsideTouchable(true);
//		selectPopupWindow.setAnimationStyle(R.style.PopupAnimation);  
//		selectPopupWindow.showAtLocation(parent, Gravity.TOP| Gravity.TOP, 0, 0);  
		selectPopupWindow.showAsDropDown(parent, 0, 0);
//		selectPopupWindow.update();  
	}
    
    public void selectProvinceData(){
    	provinceData.clear();
    	String sql = "select * from region where length(layer)=6 and layer like '022%'";
    	AssetsDatabaseManager.initManager(getApplication());  
		// 获取管理对象，因为数据库需要通过管理对象才能够获取   
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();  
		// 通过管理对象获取数据库   
		SQLiteDatabase db1 = mg.getDatabase("region_no_country.sqlite");  
		Cursor provinceC = db1.rawQuery(sql, null);
		while(provinceC.moveToNext()){
			String province = provinceC.getString(provinceC.getColumnIndex("name")).toString();
			String id = provinceC.getString(provinceC.getColumnIndex("id")).toString();
			String zimu = provinceC.getString(provinceC.getColumnIndex("pinyin")).toString().substring(0, 1);
			Map<String,Object> mapHotChild = new HashMap<String, Object>();
			mapHotChild.put("name", province);
			mapHotChild.put("id", id);
			mapHotChild.put("zimu", zimu);
			provinceData.add(mapHotChild);
		}
    }
    
    public void selectCityData(String parentid){
    	cityData.clear();
    	String sql = "select * from region where length(layer)=9 and layer like '022%' and parentid='"+parentid+"'";
    	AssetsDatabaseManager.initManager(getApplication());  
		// 获取管理对象，因为数据库需要通过管理对象才能够获取   
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();  
		// 通过管理对象获取数据库   
		SQLiteDatabase db1 = mg.getDatabase("region_no_country.sqlite");  
		Cursor provinceC = db1.rawQuery(sql, null);
		while(provinceC.moveToNext()){
			String province = provinceC.getString(provinceC.getColumnIndex("name")).toString();
			String id = provinceC.getString(provinceC.getColumnIndex("id")).toString();
			String zimu = provinceC.getString(provinceC.getColumnIndex("pinyin")).toString().substring(0, 1);
			Map<String,Object> mapHotChild = new HashMap<String, Object>();
			mapHotChild.put("name", province);
			mapHotChild.put("id", id);
			mapHotChild.put("zimu", zimu);
			cityData.add(mapHotChild);
		}
    }
    
    public void selectDiscrictData(String parentid){
    	discrictData.clear();
    	String sql = "select * from region where length(layer)=12 and layer like '022%' and parentid='"+parentid+"'";
    	AssetsDatabaseManager.initManager(getApplication());  
		// 获取管理对象，因为数据库需要通过管理对象才能够获取   
		AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();  
		// 通过管理对象获取数据库   
		SQLiteDatabase db1 = mg.getDatabase("region_no_country.sqlite");  
		Cursor provinceC = db1.rawQuery(sql, null);
		while(provinceC.moveToNext()){
			String province = provinceC.getString(provinceC.getColumnIndex("name")).toString();
			String id = provinceC.getString(provinceC.getColumnIndex("id")).toString();
			String zimu = provinceC.getString(provinceC.getColumnIndex("pinyin")).toString().substring(0, 1);
			Map<String,Object> mapHotChild = new HashMap<String, Object>();
			mapHotChild.put("name", province);
			mapHotChild.put("id", id);
			mapHotChild.put("zimu", zimu);
			discrictData.add(mapHotChild);
		}
    }
	@Override
	public void onClick(View v) {
		if(v == openLayout1 || v == startAddressTv){
			showShareDialog(openLayout1);
		}else if(v == nextBtn){
			if(startAddressTv.getText().toString().equals("")){
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				startAddressTv.startAnimation(shake);
			}else if(endAddressTv.getText().toString().equals("")){
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				endAddressTv.startAnimation(shake);
			}else if(typeCar.getText().toString().equals("")){
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				typeCar.startAnimation(shake);
			}else if(lengthCar.getText().toString().equals("")){
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				lengthCar.startAnimation(shake);
			}else{
				Intent intent = new Intent(this,SuccessRegisterActivity.class);
				startActivity(intent);
				finish();
			}
			
			overridePendingTransition(R.anim.anim_enter, R.anim.anim_exit);
		}else if(v.getId() == R.id.backBtn){
			finish();
		}
	}
	int last_item = -1,last_item1 = -1;
	View oldView,oldView1;
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		if(arg0 == provinceListView){
			if(provinceData!=null && provinceData.size()>0){//将第一个省下面的市显示      
//				provinceData.get(arg2).put("state", value)
				View parentView = arg1;
				View item = parentView.findViewById(R.id.view);  
	            item.setBackgroundColor(Color.parseColor("#FF7E00"));//把当前选中的条目加上选中效果  
	            if (last_item != -1 && last_item != position) {//如果已经单击过条目并且上次保存的item位置和当前位置不同  
	                // oldView.setBackgroundColor(Color.WHITE);  
	                oldView.setBackgroundColor(Color.parseColor("#D7D7D7"));//把上次选中的样式去掉  
	            }  
	            oldView = item;//把当前的条目保存下来  
	            last_item = position;//把当前的位置保存下来  
	            
	    		String id = provinceData.get(position).get("id").toString();
	    		selectCityData(id);
	    		notifyProvinceAdapter(cityListView,cityData);
	    	}
	    	if(cityData!=null && cityData.size()>0){//将第一个市下面的所有区显示
	    		String id = cityData.get(0).get("id").toString();
	    		selectDiscrictData(id);
	    		notifyProvinceAdapter(discrictListView,discrictData);
	    	}
		}else if(arg0 == cityListView){
			if(cityData!=null && cityData.size()>0){//将第一个市下面的所有区显示
				View parentView = arg1;
				View item = parentView.findViewById(R.id.view);  
	            item.setBackgroundColor(Color.parseColor("#FF7E00"));//把当前选中的条目加上选中效果  
	            if (last_item1 != -1 && last_item1 != position) {//如果已经单击过条目并且上次保存的item位置和当前位置不同  
	                // oldView.setBackgroundColor(Color.WHITE);  
	            	oldView1.setBackgroundColor(Color.WHITE);//把上次选中的样式去掉  
	            }  
	            oldView1 = item;//把当前的条目保存下来  
	            last_item1 = position;//把当前的位置保存下来  
	            
	            
	    		String id = cityData.get(position).get("id").toString();
	    		selectDiscrictData(id);
	    		notifyProvinceAdapter(discrictListView,discrictData);
	    	}
		}else if(arg0 == discrictListView){
			
		}
	}
}
