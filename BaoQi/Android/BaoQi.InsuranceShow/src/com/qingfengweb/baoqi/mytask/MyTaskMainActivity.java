package com.qingfengweb.baoqi.mytask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.GeoPoint;
import com.baidu.mapapi.LocationListener;
import com.baidu.mapapi.MKAddrInfo;
import com.baidu.mapapi.MKDrivingRouteResult;
import com.baidu.mapapi.MKPlanNode;
import com.baidu.mapapi.MKPoiInfo;
import com.baidu.mapapi.MKPoiResult;
import com.baidu.mapapi.MKSearch;
import com.baidu.mapapi.MKSearchListener;
import com.baidu.mapapi.MKTransitRouteResult;
import com.baidu.mapapi.MKWalkingRouteResult;
import com.baidu.mapapi.MapActivity;
import com.baidu.mapapi.MapController;
import com.baidu.mapapi.MapView;
import com.baidu.mapapi.MyLocationOverlay;
import com.baidu.mapapi.PoiOverlay;
import com.baidu.mapapi.RouteOverlay;
import com.qingfengweb.baoqi.hospitalquery.BMapApiDemoApp;
import com.qingfengweb.baoqi.insuranceShow.InsuranceShowMainActivity;
import com.qingfengweb.baoqi.insuranceShow.R;

public class MyTaskMainActivity extends MapActivity{
	public BMapManager mapManager;
	BMapApiDemoApp app;
	private Button M_LuKuangBtn;
	public MKSearch mMKSearch;
	EditText map_qishi,map_daoda;
	public MapView mMapView = null;
	public LocationListener mLocationListener = null;//onResume时注册此listener，onPause时需要Remove
	public MyLocationOverlay mLocationOverlay = null;	//定位图层
	public LocationListener myLocationListener = null;//create时注册此listener，Destroy时需要Remove
	public Double jingdu=null,weidu=null;
	private LayoutInflater layout1;
	Button map_searchBtn,acceptCloseBtn,acceptTopCloseBtn;
	 private MapController mapController; 
	 public static StringBuilder sb;  
	RelativeLayout relative;
	public GeoPoint pt;
	private Button lookMap,acceptTaskBtn,changeBtn,closeBtn,buttoncloseview;
	private Button changeCloseBtn,changeSubmit,changeCloBtn;
	public  String texttime = null, textadd = null, textperson = null,textphone = null, textinfo = null;
	private ExpandableListView elv,elvHistory;
	private List<Map<String, String>> groups;
	private LinearLayout currentLinear,historyLinear;
	private Button currentButton,historyButton;
	private LinearLayout rightLinearLayout;	
	public TextView detailTime,detailAddress,detailPerson,detailPhone,detailInfo;
	private RelativeLayout detailRelativeLayout,acceptRelativeLayout,changeRelativeLayout,surveyRelativeLayout,
		disabledRelativeLayout,cameraRelayout,cameraIdLayout,idcarLayout,againLayout,againCameraCar,
		mapRe,carInfoPai;
	public TextView acceptedTime,acceptedaddress,acceptedperson,acceptedphone,acceptedinfo;
	private LinearLayout mainLayout;
	 private LayoutInflater layout;
	 List<List<Map<String, String>>> childs;
	 private mytaskExpandableAdapter viewAdapter;
	 Button tvdetail,tvmap,tvaccept,tvchange;
	 private int accepted = 0;
	 private int detail = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mytask_main);
		mainLayout=(LinearLayout) findViewById(R.id.mytaskmainLinearLayout);
		layout=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		MyExpandable();
		initView();
		commonViewFun();
		locationOverlayFun();//定位当前位置
		MyLocationFun();//得到当前经纬度
		ButtonTextUnderLineFun();//为需要加下划线的按钮上的字加下划线
	}
	private void initView(){
		currentLinear=(LinearLayout) findViewById(R.id.LeftcurrenttaskLinearLayout);
        historyLinear=(LinearLayout) findViewById(R.id.LefthistorytaskLinearLayout);
        currentButton=(Button) findViewById(R.id.currentButton);
        historyButton=(Button) findViewById(R.id.historyButton);
        rightLinearLayout=(LinearLayout) findViewById(R.id.right);
        detailRelativeLayout=(RelativeLayout) layout.inflate(R.layout.detail_task, null);
        acceptRelativeLayout=(RelativeLayout) layout.inflate(R.layout.acceptedtask, null);
        changeRelativeLayout=(RelativeLayout) layout.inflate(R.layout.change, null);
        lookMap=(Button) detailRelativeLayout.findViewById(R.id.lookmap);
        acceptTaskBtn=(Button) detailRelativeLayout.findViewById(R.id.buttonaccept);
        changeBtn=(Button) detailRelativeLayout.findViewById(R.id.buttonchange);
        closeBtn=(Button) detailRelativeLayout.findViewById(R.id.buttonclose);
        buttoncloseview=(Button) detailRelativeLayout.findViewById(R.id.buttoncloseview);
        detailTime=(TextView) detailRelativeLayout.findViewById(R.id.rightdetailtime);
        detailAddress=(TextView)  detailRelativeLayout.findViewById(R.id.rightdetailaddress);
        detailPerson=(TextView)  detailRelativeLayout.findViewById(R.id.rightdetailperson);
        detailPhone=(TextView)  detailRelativeLayout.findViewById(R.id.rightdetailphone);
        detailInfo=(TextView)  detailRelativeLayout.findViewById(R.id.rightdetailinfo);
        acceptedTime=(TextView) acceptRelativeLayout.findViewById(R.id.acceptedtime);
  	 	acceptedaddress=(TextView)acceptRelativeLayout.findViewById(R.id.acceptedaddress);
  	 	acceptedperson=(TextView) acceptRelativeLayout.findViewById(R.id.acceptedperson);
  	 	acceptedphone=(TextView) acceptRelativeLayout.findViewById(R.id.acceptedphone);
  	 	acceptedinfo=(TextView) acceptRelativeLayout.findViewById(R.id.acceptedinfo);
  	 	M_LuKuangBtn=(Button) findViewById(R.id.lukuangBtn);
  	 	map_qishi=(EditText) findViewById(R.id.qishi);
  	 	map_daoda=(EditText) findViewById(R.id.daoda);
  	 	map_searchBtn=(Button) findViewById(R.id.searchLuxianBtn);
  	 	mapRe=(RelativeLayout) findViewById(R.id.MapRel);
  	 	acceptCloseBtn=(Button) acceptRelativeLayout.findViewById(R.id.rightacceptedcloseButton);
  	 	acceptTopCloseBtn=(Button) acceptRelativeLayout.findViewById(R.id.acceptedCloseButton);
  	 	changeCloseBtn=(Button) changeRelativeLayout.findViewById(R.id.changeviewclosebutton);
  	 	changeSubmit=(Button) changeRelativeLayout.findViewById(R.id.changesubmitbutton);
  	 	changeCloBtn=(Button) changeRelativeLayout.findViewById(R.id.changecanclebutton);
	}
	private void commonViewFun(){
		currentButton.setOnClickListener(new CurrentButtonListener());
		historyButton.setOnClickListener(new HistoryButtonListener());
		M_LuKuangBtn.setOnClickListener(new LuKuangListener());
		map_searchBtn.setOnClickListener(new PaintLujingListener());
		lookMap.setOnClickListener(new CloseBtnListener());
		acceptTaskBtn.setOnClickListener(new AcceptTaskButtonListener());
		changeBtn.setOnClickListener(new OpenViewListener(changeRelativeLayout));
		closeBtn.setOnClickListener(new CloseBtnListener());
		buttoncloseview.setOnClickListener(new CloseBtnListener());
		acceptCloseBtn.setOnClickListener(new CloseBtnListener());
		acceptTopCloseBtn.setOnClickListener(new CloseBtnListener());
		changeCloseBtn.setOnClickListener(new CloseBtnListener());
		changeCloBtn.setOnClickListener(new CloseBtnListener());
		changeSubmit.setOnClickListener(new SubmitListener());
	}
	 /*
     * @author 刘星星
     * 右边的 任务详情弹出框下面的接受任务按钮监听事件
     */
     class AcceptTaskButtonListener implements View.OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			//acceptFun();
			dialogWronFun2("确定要接受吗？",MyTaskMainActivity.this);
			
		}
  	   
     }
     
     
     public void dialogWronFun2(CharSequence str,Context context){
	      	LayoutInflater inflater=(LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
			    View view=inflater.inflate(R.layout.worningdialog, null);
			    TextView tv=(TextView) view.findViewById(R.id.worningTv);
			    tv.setText(str);
	      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
	      	alert.setView(view);
	      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
	      			
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						accepted = 1;
						acceptTaskBtn.setText("已接受");
						acceptTaskBtn.setClickable(false);
						acceptTaskBtn.setEnabled(false);
						acceptTaskBtn.setTextColor(Color.GRAY);
						tvaccept.setText("已接受");
			 			tvaccept.setClickable(false);
			 			tvaccept.setEnabled(false);
			 			tvaccept.setTextColor(Color.GRAY);
						 rightLinearLayout.removeAllViews();
						 acceptFun();
						 rightLinearLayout.addView(acceptRelativeLayout);
						
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						return;
					}
				}).create().show();
	      	
	      }
	class SubmitListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			
			dialogWronFun("提交成功!!!!!!",MyTaskMainActivity.this);
		}
		
	}
	 public void dialogWronFun(CharSequence str,Context context){
	      	LayoutInflater inflater=(LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
			    View view=inflater.inflate(R.layout.worningdialog, null);
			    TextView tv=(TextView) view.findViewById(R.id.worningTv);
			    tv.setText(str);
	      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
	      	alert.setView(view);
	      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
	      			
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						rightLinearLayout.removeAllViews();
						acceptFun();
						rightLinearLayout.addView(detailRelativeLayout);	
					}
				}).create().show();
	      	
	      }

	 public void underline(int start, int end,Button btn) {
			SpannableStringBuilder spannable = new SpannableStringBuilder(btn.getText().toString());
			CharacterStyle span = new UnderlineSpan();
			spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			btn.setText(spannable);

		}
	 public void ButtonTextUnderLineFun(){
	    
	    	underline(0,4,lookMap);
	    	
	    }
	 class PaintLujingListener implements View.OnClickListener{

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str_qishi=map_qishi.getText().toString();
				String str_daoda=map_daoda.getText().toString();
				// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
				MKPlanNode stNode = new MKPlanNode();
				stNode.name = str_qishi;
				//stNode.pt 根据坐标定位
				MKPlanNode enNode = new MKPlanNode();
				enNode.name = str_daoda;
				
				// 实际使用中请对起点终点城市进行正确的设定
				mMKSearch.drivingSearch("济南", stNode, "济南", enNode);
				
			}
	    	 
	     }
	 class CloseBtnListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			rightLinearLayout.removeAllViews();
			rightLinearLayout.addView(mapRe);
			locationOverlayFun();
		}
		 
	 }
	 class OpenViewListener implements View.OnClickListener{
		 private RelativeLayout relativeLayout;
		 public OpenViewListener(RelativeLayout relativeLayout) {
			// TODO Auto-generated constructor stub
			 this.relativeLayout=relativeLayout;
		}
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 rightLinearLayout.removeAllViews();
			 rightLinearLayout.addView(relativeLayout);
			
			
			
		}
		 
	 }
	class LuKuangListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(M_LuKuangBtn.getText().toString().equals("实时路况")){
				mMapView.setTraffic(true);
				M_LuKuangBtn.setText("关闭路况");
			}else if(M_LuKuangBtn.getText().toString().equals("关闭路况")){
				mMapView.setTraffic(false);
				M_LuKuangBtn.setText("实时路况");
			}
		}
		
	}
	private void MyExpandable(){
		   elv = (ExpandableListView)findViewById(R.id.mytask_expandable);
		   elvHistory = (ExpandableListView)findViewById(R.id.mytask_historycurrentexpandable);
		  elv.setCacheColorHint(0);
		  elvHistory.setCacheColorHint(0);
		  elv.setDividerHeight(0);
		  elvHistory.setDividerHeight(0);
		   groups = new ArrayList<Map<String, String>>(); 
	  	  Map<String, String> group1 = new HashMap<String, String>(); 
	  	  group1.put("group", "徐汇区梅陇七村李先生理赔协助"); 
	  	  Map<String, String> group2 = new HashMap<String, String>(); 
	  	  group2.put("group", "普陀区凯旋北路1555弄张小姐购买意向"); 
	  	  groups.add(group1); 
	  	  groups.add(group2); 

	  	//准备第一个一级列表中的二级列表数据:两个二级列表,分别显示"childData1"和"childData2" 
	  	  List<Map<String, String>> child1 = new ArrayList<Map<String, String>>(); 
	  	  Map<String, String> child1Data1 = new HashMap<String, String>(); 
	  	  child1Data1.put("time", "2011年1月1号 11:00"); 
	  	  child1Data1.put("address", "徐汇区梅陇七村");
	  	  child1Data1.put("person", "李翼龙");
	  	  child1Data1.put("phone", "13873844140");
	  	  child1Data1.put("info", "李先生请求理赔协助，需到达现场协助其了解理赔程序。。。。。。。。。");
	  	  child1.add(child1Data1); 

	  	  //准备第二个一级列表中的二级列表数据:一个二级列表,显示"child2Data1" 
	  	  List<Map<String, String>> child2 = new ArrayList<Map<String, String>>(); 
	  	  Map<String, String> child2Data1 = new HashMap<String, String>(); 
	  	  child2Data1.put("time", "2011年3月3号 13:00"); 
	 	  child2Data1.put("address", "普陀区凯旋北路1555弄");
	 	  child2Data1.put("person", "张春燕");
	 	  child2Data1.put("phone", "13391012240");
	 	  child2Data1.put("info", "张小姐有购买保险的意向，需前往现场帮助其了解本寿险的一些情况。。。。。。。。。。。。");
	  	  child2.add(child2Data1); 
	  	//用一个list对象保存所有的二级列表数据 
	  	    childs = new ArrayList<List<Map<String, String>>>();
	  	   childs.add(child1); 
	  	   childs.add(child2);
	  	   
	  	    viewAdapter = new mytaskExpandableAdapter(MyTaskMainActivity.this, groups, childs); 
		   elv.setAdapter(viewAdapter); 
		   elvHistory.setAdapter(viewAdapter); 
		   elv.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				for(int i=0;i<groups.size();i++)
		        {
		                if(groupPosition!=i)
		                {
		                	elv.collapseGroup(i);
		                }
		        }
			}
		});
		   elvHistory.setOnGroupExpandListener(new OnGroupExpandListener() {
				
				@Override
				public void onGroupExpand(int groupPosition) {
					// TODO Auto-generated method stub
					for(int i=0;i<groups.size();i++)
			        {
			                if(groupPosition!=i)
			                {
			                	elvHistory.collapseGroup(i);
			                }
			        }
				}
			});
	}
	

    /*acceptFun()
     * @author 刘星星
     * 接受任务的监听事件
     */
    public void acceptFun(){
 	   List<Map<String,String>> acceptedList=new ArrayList<Map<String,String>>();
			Map<String,String> acceptMap1=new HashMap<String,String>();
			acceptMap1.put("returnreason", "此案件标的物不合格");
			acceptMap1.put("returnperson", "谢国梁");
			acceptMap1.put("returnphone", "13873844142");
			acceptMap1.put("returnJobNumber", "007");
			acceptedList.add(acceptMap1);
			Map<String,String> acceptMap2=new HashMap<String,String>();
			acceptMap2.put("returnreason", "此案件标的物不合格");
			acceptMap2.put("returnperson", "谢国梁");
			acceptMap2.put("returnphone", "13873844142");
			acceptMap2.put("returnJobNumber", "007");
			acceptedList.add(acceptMap2);
			
			SimpleAdapter listAdapter=new SimpleAdapter(MyTaskMainActivity.this, acceptedList, R.layout.acceptedreturninfo, 
					new String[] {"returnreason","returnperson","returnphone","returnJobNumber"}, 
					new int[] {R.id.returnreason,R.id.returnperson,R.id.returnphone,R.id.returnJobNumber}); 
			ListView acceptedListeView=(ListView) acceptRelativeLayout.findViewById(R.id.returnjiluText);
			acceptedListeView.setDivider(null);
			acceptedListeView.setAdapter(listAdapter);
			acceptedTime.setText(texttime);
			acceptedaddress.setText(textadd);
			acceptedperson.setText(textperson);
			acceptedphone.setText(textphone);
			acceptedinfo.setText(textinfo);
    }
	/*
	 * @author 刘星星
	 * Expandable 控件的使用 
	 * 
	 */
	public class mytaskExpandableAdapter extends BaseExpandableListAdapter{ 
		
		  private Context context; 
		  List<Map<String, String>> groups; 
		  List<List<Map<String, String>>> childs; 
		  /* 
		  * 构造函数: ExpandableAdapter
		  * 参数1:context对象 
		  * 参数2:一级列表数据源 
		  * 参数3:二级列表数据源 
		  */ 
		  public mytaskExpandableAdapter(Context context, List<Map<String, String>> groups, List<List<Map<String, String>>> childs)
		   { 
		  this.groups = groups; 
		  this.childs = childs; 
		  this.context = context; 
		  } 
		  public Object getChild(int groupPosition, int childPosition) 
		  { 
		  return childs.get(groupPosition).get(childPosition); 
		  } 
		  public long getChildId(int groupPosition, int childPosition) 
		  { 
		  return childPosition; 
		  } 
		  /*taskInfoOfValuesFun(int tagGroup)
		   * @author 刘星星
		   * 为事件的控件负值
		   */
		  public void taskInfoOfValuesFun(int tagGroup){
	  	   		texttime = ((Map<String, String>) getChild(tagGroup, 0)).get("time").toString();
				textadd = ((Map<String, String>) getChild(tagGroup, 0)).get("address").toString();
				textperson = ((Map<String, String>) getChild(tagGroup, 0)).get("person").toString();
				textphone = ((Map<String, String>) getChild(tagGroup, 0)).get("phone").toString();
				textinfo = ((Map<String, String>) getChild(tagGroup, 0)).get("info").toString();
				detailTime.setText(texttime);
				detailAddress.setText(textadd);
				detailPerson.setText(textperson);
				detailPhone.setText(textphone);
				detailInfo.setText(textinfo);
	     }
		   /*
		    * 接受任务时的数据处理
		    * @author 刘星星
		    */
		  public void acceptedTaskInfoOfValuesFun(int tagGroup){
		
		   		texttime = ((Map<String, String>) getChild(tagGroup, 0)).get("time").toString();
				textadd = ((Map<String, String>) getChild(tagGroup, 0)).get("address").toString();
				textperson = ((Map<String, String>) getChild(tagGroup, 0)).get("person").toString();
				textphone = ((Map<String, String>) getChild(tagGroup, 0)).get("phone").toString();
				textinfo = ((Map<String, String>) getChild(tagGroup, 0)).get("info").toString();
				acceptedTime.setText(texttime);
				acceptedaddress.setText(textadd);
				acceptedperson.setText(textperson);
				acceptedphone.setText(textphone);
				acceptedinfo.setText(textinfo);
				
				
	   }
		  public void dialogWronFun1(CharSequence str,Context context,final int tag){
		      	LayoutInflater inflater=(LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
				    View view=inflater.inflate(R.layout.worningdialog, null);
				    TextView tv=(TextView) view.findViewById(R.id.worningTv);
				    tv.setText(str);
		      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
		      	alert.setView(view);
		      	alert.setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
		      			
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							rightLinearLayout.removeAllViews();
				 		     acceptedTaskInfoOfValuesFun(tag);
				 			 acceptFun();
				 			 if(accepted == 1 && tag == 0){
				 				tvaccept.setText("已接受");
					 			tvaccept.setClickable(false);
					 			tvaccept.setTextColor(Color.GRAY);
					 			acceptTaskBtn.setText("已接受");
								acceptTaskBtn.setClickable(false);
								acceptTaskBtn.setTextColor(Color.GRAY); 
				 			 }else if(detail == 1 && tag == 1){
				 				tvaccept.setText("已接受");
					 			tvaccept.setClickable(false);
					 			tvaccept.setEnabled(false);
					 			tvaccept.setTextColor(Color.GRAY);
					 			acceptTaskBtn.setText("已接受");
					 			acceptTaskBtn.setEnabled(false);
								acceptTaskBtn.setClickable(false);
								acceptTaskBtn.setTextColor(Color.GRAY); 
				 			 }
				 			
				 			 rightLinearLayout.addView(acceptRelativeLayout);
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return;
						}
					}).create().show();
		      	
		      }
		  //获取二级列表的View对象 
		
		  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
		   ViewGroup parent) 
		  { 
		   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 //获取二级列表对应的布局文件, 并将其各元素设置相应的属性 
		  LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.mytask_child, null);
		 
		    tvdetail = (Button) linearLayout.findViewById(R.id.leftdetail);
		    tvmap = (Button) linearLayout.findViewById(R.id.leftmap); 
		    tvaccept = (Button) linearLayout.findViewById(R.id.leftaccept); 
		    tvchange = (Button) linearLayout.findViewById(R.id.leftchange); 
		   tvdetail.setTag(groupPosition);
		   tvmap.setTag(groupPosition);
		   tvaccept.setTag(groupPosition);
		   tvchange.setTag(groupPosition);
		   if(accepted == 1 && groupPosition == 0){
				tvaccept.setText("已接受");
	 			tvaccept.setClickable(false);
	 			tvaccept.setEnabled(false);
	 			tvaccept.setTextColor(Color.GRAY);
	 			acceptTaskBtn.setText("已接受");
				acceptTaskBtn.setClickable(false);
				acceptTaskBtn.setEnabled(false);
				acceptTaskBtn.setTextColor(Color.GRAY); 
			 }
		   if(detail == 1 && groupPosition == 1){
			   tvaccept.setText("已接受");
	 			tvaccept.setClickable(false);
	 			tvaccept.setEnabled(false);
	 			tvaccept.setTextColor(Color.GRAY);
	 			acceptTaskBtn.setText("已接受");
				acceptTaskBtn.setClickable(false);
				acceptTaskBtn.setEnabled(false);
				acceptTaskBtn.setTextColor(Color.GRAY); 
		   }
		   /*
		    * 详情查看按钮监听事件
		    * @author 刘星星
		    */
		  tvdetail.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				// TODO Auto-generated method stub		
				 int tag=Integer.parseInt(v.getTag().toString());
			     //	 setContentView(mainLayout);
				 rightLinearLayout.removeAllViews();
	 			 taskInfoOfValuesFun(tag);
	 			 rightLinearLayout.addView(detailRelativeLayout);
	 			 if(accepted == 0 && tag == 0 ){
	 				acceptTaskBtn.setText("接受");
					acceptTaskBtn.setClickable(true);
					acceptTaskBtn.setEnabled(true);
					acceptTaskBtn.setTextColor(Color.WHITE); 
	 			 }else if(accepted == 1 && tag == 0){
	 				acceptTaskBtn.setText("已接受");
					acceptTaskBtn.setClickable(false);
					acceptTaskBtn.setEnabled(false);
					acceptTaskBtn.setTextColor(Color.GRAY); 
	 			 }else if(detail == 0 && tag == 1){
	 				acceptTaskBtn.setText("接受");
					acceptTaskBtn.setClickable(true);
					acceptTaskBtn.setEnabled(true);
					acceptTaskBtn.setTextColor(Color.WHITE); 
	 			 }else if(accepted == 1 && tag == 1){
	 				acceptTaskBtn.setText("已接受");
					acceptTaskBtn.setClickable(false);
					acceptTaskBtn.setEnabled(false);
					acceptTaskBtn.setTextColor(Color.GRAY); 
	 			 }
				}
		});
	
		   tvmap.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					rightLinearLayout.removeAllViews();
					 rightLinearLayout.addView(mapRe);
					 locationOverlayFun();
					 
				}
			});
		   
		    
		   tvaccept.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag=Integer.parseInt(v.getTag().toString());
					
					
			 		  if(tag==0){
			 			 accepted = 1;
			 			 dialogWronFun1("确定接受吗？",MyTaskMainActivity.this,tag);
			 			 
				 	   }else if(tag==1){
				 		  detail = 1;
				 		  dialogWronFun1("确定接受吗？",MyTaskMainActivity.this,tag);
				 			
				 	   }
				}
			});
		   
	
		    
		   tvchange.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(Integer.parseInt(v.getTag().toString())==0){
						 rightLinearLayout.removeAllViews();
						 rightLinearLayout.addView(changeRelativeLayout);
						
					}else if(Integer.parseInt(v.getTag().toString())==1){
						 rightLinearLayout.removeAllViews();
						 rightLinearLayout.addView(changeRelativeLayout);
						 
					}
					
				}
			});
		return linearLayout; 
		  } 

		
		  public int getChildrenCount(int groupPosition) 
		  { 
		  return childs.get(groupPosition).size(); 
		  } 


		  public Object getGroup(int groupPosition) 
		  { 
		  return groups.get(groupPosition); 
		  } 

		  public int getGroupCount() 
		  { 
		  return groups.size(); 
		  } 


		  public long getGroupId(int groupPosition) 
		  { 
			 
			 
		  return groupPosition; 
		  } 

		  //获取一级列表View对象 
		  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		   { 
			  
		  String text = groups.get(groupPosition).get("group"); 
		  LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   
		  //获取一级列表布局文件,设置相应元素属性 
		  LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.group, null);
		   TextView textView = (TextView)linearLayout.findViewById(R.id.groupTo); 
		   ImageView i=(ImageView) linearLayout.findViewById(R.id.groupimageView);
		   if(!isExpanded){
			   i.setImageResource(R.drawable.leftadd);
		   }else{
			   i.setImageResource(R.drawable.leftjian);
		   }
		  textView.setText(text); 
		return linearLayout; 
		  } 


		  public boolean hasStableIds() 
		  { 
		  return false; 
		  } 


		  public boolean isChildSelectable(int groupPosition, int childPosition) 
		  { 
		  return true; 
		  } 

		  } 
	
	/*
     * CurrentButtonListener
     * @author 刘星星
     * 当前任务显示
     */
      class CurrentButtonListener implements View.OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			currentLinear.setVisibility(View.VISIBLE);
			historyLinear.setVisibility(View.GONE);
			currentButton.setTextColor(Color.BLACK);
			historyButton.setTextColor(Color.WHITE);
			currentButton.setBackgroundResource(R.drawable.rw_tag);
			historyButton.setBackgroundResource(R.drawable.rw_tag_on);
		}
		
    }
       /* 类HistoryButtonListener
        * @author刘星星
        * 历史任务显示
        */
       class HistoryButtonListener implements View.OnClickListener{

			public void onClick(View v) {
				// TODO Auto-generated method stub
				historyLinear.setVisibility(View.VISIBLE);
				currentLinear.setVisibility(View.GONE);
				historyButton.setTextColor(Color.BLACK);
				currentButton.setTextColor(Color.WHITE);
				currentButton.setBackgroundResource(R.drawable.rw_tag_on);
				historyButton.setBackgroundResource(R.drawable.rw_tag);
			}
      }
       /*
		 * 当前位置定位
		 */
			 public void locationOverlayFun(){
				   app = (BMapApiDemoApp)this.getApplication();
			 		if (app.mBMapMan == null) {
			 			app.mBMapMan = new BMapManager(getApplication());
			 			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
			 		}
			 		app.mBMapMan.start();
			         // 如果使用地图SDK，请初始化地图Activity
			         super.initMapActivity(app.mBMapMan);
			         //layout1=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			          //relative=(RelativeLayout) layout1.inflate(R.layout.map_view, null);
			         mMapView = (MapView)findViewById(R.id.mytask_bmapView);
			         mMapView.setBuiltInZoomControls(true);
			         //设置在缩放动画过程中也显示overlay,默认为不绘制
			         mMapView.setDrawOverlayWhenZooming(true);
			         
			 		// 添加定位图层
			         mLocationOverlay = new MyLocationOverlay(this, mMapView);
			        
			 		mMapView.getOverlays().add(mLocationOverlay);
			         // 注册定位事件
			         mLocationListener = new LocationListener(){
			 			public void onLocationChanged(Location location) {
			 				if (location != null){
			 					 pt = new GeoPoint((int)(location.getLatitude()*1e6),
			 							(int)(location.getLongitude()*1e6));
			 					mMapView.getController().animateTo(pt);
			 					
			 				}
			 			}
			         };
		   }
				/*
				 * 当前经纬度定位
				 * 及根据经纬度定位当前街道信息
				 */
			 String jingwei=null;
			 public String MyLocationFun(){
				 BMapApiDemoApp app = (BMapApiDemoApp)this.getApplication();
					if (app.mBMapMan == null) {
						app.mBMapMan = new BMapManager(getApplication());
						app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
					}
					app.mBMapMan.start();
					
					mMKSearch = new MKSearch();  
					
					mapManager = new BMapManager(getApplication());
					mapManager.init(app.mStrKey, null);  
					mMKSearch.init(mapManager, new MySearchListener());  
			        // 注册定位事件
			        myLocationListener = new LocationListener(){
						public void onLocationChanged(Location location) {
						
							jingdu=location.getLongitude();
							weidu=location.getLatitude();
							if(location != null){
								String strLog = String.format("您当前的位置:\r" +
										"纬度:%f\r" +
										"经度:%f",
										location.getLongitude(), location.getLatitude());
								 try {  
					                    // 将用户输入的经纬度值转换成int类型   
					                    int longitude = (int) (1E6 * jingdu);  
					                    int latitude = (int) (1E6 *weidu);  
					                    // 查询该经纬度值所对应的地址位置信息   
					                    
					                    mMKSearch.reverseGeocode(new GeoPoint(latitude, longitude));  
					                    
					                } catch (Exception e) {  
					                	jingwei="查询出错，请检查您输入的经纬度值！";  
					                }  
								

							}
						}
			        };
			       
			       return jingwei;
			 }
			 /** 
			  * 实现MKSearchListener接口,用于实现异步搜索服务，得到搜索结果 
			  *  
			  * @author liufeng 
			  */  
			 public class MySearchListener implements MKSearchListener {  
			     /** 
			      * 根据经纬度搜索地址信息结果 
			      * @param result 搜索结果 
			      * @param iError 错误号（0表示正确返回） 
			      */  
			 
				
			        public void onGetAddrResult(MKAddrInfo result, int iError) {  
			        	if (result == null) {  
			                return;  
			            }  
			            StringBuffer sb = new StringBuffer();  
			            // 经纬度所对应的位置   
			            sb.append(result.strAddr).append("/n");  
			  
			            // 判断该地址附近是否有POI（Point of Interest,即兴趣点）   
			             /*if (null != result.poiList) {  
			                // 遍历所有的兴趣点信息   
			                for (MKPoiInfo poiInfo : result.poiList) {  
			                    sb.append("名称：").append(poiInfo.name).append("/n");  
			                    sb.append("地址：").append(poiInfo.address).append("/n");  
			                    sb.append("经度：").append(poiInfo.pt.getLongitudeE6() / 1000000.0f).append("/n");  
			                    sb.append("纬度：").append(poiInfo.pt.getLatitudeE6() / 1000000.0f).append("/n");  
			                    sb.append("电话：").append(poiInfo.phoneNum).append("/n");  
			                    sb.append("邮编：").append(poiInfo.postCode).append("/n");  
			                    // poi类型，0：普通点，1：公交站，2：公交线路，3：地铁站，4：地铁线路   
			                    sb.append("类型：").append(poiInfo.ePoiType).append("/n");  
			                }  
			            } */
			            // 将地址信息、兴趣点信息显示在TextView上   
			      

			            jingwei=sb.toString();
			        }  

			   
			     /** 
			      * 驾车路线搜索结果 
			      * @param result 搜索结果 
			      * @param iError 错误号（0表示正确返回） 
			      */  
			     
			     public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {  
			    	// 错误号可参考MKEvent中的定义
						if (iError != 0 || result == null) {
							Toast.makeText(MyTaskMainActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
							return;
						}
						RouteOverlay routeOverlay = new RouteOverlay(MyTaskMainActivity.this, mMapView);
					    // 此处仅展示一个方案作为示例
					    routeOverlay.setData(result.getPlan(0).getRoute(0));
					    mMapView.getOverlays().clear();
					    mMapView.getOverlays().add(routeOverlay);
					    mMapView.invalidate();
					    
					    mMapView.getController().animateTo(result.getStart().pt);
					    
					    
					 

			     }  
			   
			     /** 
			      * POI搜索结果（范围检索、城市POI检索、周边检索） 
			      * @param result 搜索结果 
			      * @param type 返回结果类型（11,12,21:poi列表 7:城市列表） 
			      * @param iError 错误号（0表示正确返回） 
			      */  
			
			     public void onGetPoiResult(MKPoiResult result, int type, int iError) {  
			    	 if (result == null) {  
			                return;  
			            }  
			    	  mapController = mMapView.getController();  
			    	  mapController.setZoom(10);  
			            // 清除地图上已有的所有覆盖物   
			    	  mMapView.getOverlays().clear();  
			            // PoiOverlay是baidu map api提供的用于显示POI的Overlay   
			            PoiOverlay poioverlay = new PoiOverlay(MyTaskMainActivity.this, mMapView);  
			            // 设置搜索到的POI数据   
			            poioverlay.setData(result.getAllPoi());  
			            // 在地图上显示PoiOverlay（将搜索到的兴趣点标注在地图上）   
			            mMapView.getOverlays().add(poioverlay);  
			  
			            if(result.getNumPois() > 0) {  
			                // 设置其中一个搜索结果所在地理坐标为地图的中心   
			                MKPoiInfo poiInfo = result.getPoi(0);  
			                mapController.setCenter(poiInfo.pt);  
			            }  
			              
			            sb.append("共搜索到").append(result.getNumPois()).append("个POI/n");  
			            // 遍历当前页返回的POI（默认只返回10个）   
			            for (MKPoiInfo poiInfo : result.getAllPoi()) { 
			                sb.append("名称：").append(poiInfo.name);  
			                //sb.append("地址：").append(poiInfo.address).append("/n");   
			                //sb.append("经度：").append(poiInfo.pt.getLongitudeE6() / 1000000.0f).append("/n");   
			                //sb.append("纬度：").append(poiInfo.pt.getLatitudeE6() / 1000000.0f).append("/n");   
			            }  
			  
			            // 通过AlertDialog显示当前页搜索到的POI   
			            new AlertDialog.Builder(MyTaskMainActivity.this)  
			            .setTitle("搜索到的POI信息")  
			            .setMessage(sb.toString())  
			            .setPositiveButton("关闭", new DialogInterface.OnClickListener() {  
			                public void onClick(DialogInterface dialog, int whichButton) {  
			                    dialog.dismiss();  
			                }  
			            }).create().show();  
			     }  
			   
			     /** 
			      * 公交换乘路线搜索结果 
			      * @param result 搜索结果 
			      * @param iError 错误号（0表示正确返回） 
			      */  
			
			     public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {  
			     }  
			   
			     /** 
			      * 步行路线搜索结果 
			      * @param result 搜索结果 
			      * @param iError 错误号（0表示正确返回） 
			      */  
			   
			     public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {  
			     }  
			 }  
	@Override
			protected void onDestroy() {
				// TODO Auto-generated method stub
		mMapView.setTraffic(false);
		M_LuKuangBtn.setText("实时路况");
				super.onDestroy();
			}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mMapView.setTraffic(false);
		M_LuKuangBtn.setText("实时路况  ");
		super.onStop();
	}
	@Override
	protected void onPause() {
		app = (BMapApiDemoApp)this.getApplication();
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		mLocationOverlay.disableMyLocation();
        mLocationOverlay.disableCompass(); // 关闭指南针
        app.mBMapMan.getLocationManager().removeUpdates(myLocationListener);
		
		app.mBMapMan.stop();
		super.onPause();
		 
	}
	@Override
	protected void onResume() {
	 app = (BMapApiDemoApp)this.getApplication();
		// 注册定位事件，定位后将地图移动到定位点
        app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
        app.mBMapMan.getLocationManager().requestLocationUpdates(myLocationListener);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableCompass(); // 打开指南针
		app.mBMapMan.start();
		super.onResume();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(MyTaskMainActivity.this,InsuranceShowMainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
