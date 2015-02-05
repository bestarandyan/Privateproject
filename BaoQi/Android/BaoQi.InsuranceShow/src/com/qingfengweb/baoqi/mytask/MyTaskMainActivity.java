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
	public LocationListener mLocationListener = null;//onResumeʱע���listener��onPauseʱ��ҪRemove
	public MyLocationOverlay mLocationOverlay = null;	//��λͼ��
	public LocationListener myLocationListener = null;//createʱע���listener��Destroyʱ��ҪRemove
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
		locationOverlayFun();//��λ��ǰλ��
		MyLocationFun();//�õ���ǰ��γ��
		ButtonTextUnderLineFun();//Ϊ��Ҫ���»��ߵİ�ť�ϵ��ּ��»���
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
     * @author ������
     * �ұߵ� �������鵯��������Ľ�������ť�����¼�
     */
     class AcceptTaskButtonListener implements View.OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			//acceptFun();
			dialogWronFun2("ȷ��Ҫ������",MyTaskMainActivity.this);
			
		}
  	   
     }
     
     
     public void dialogWronFun2(CharSequence str,Context context){
	      	LayoutInflater inflater=(LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
			    View view=inflater.inflate(R.layout.worningdialog, null);
			    TextView tv=(TextView) view.findViewById(R.id.worningTv);
			    tv.setText(str);
	      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
	      	alert.setView(view);
	      	alert.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
	      			
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						accepted = 1;
						acceptTaskBtn.setText("�ѽ���");
						acceptTaskBtn.setClickable(false);
						acceptTaskBtn.setEnabled(false);
						acceptTaskBtn.setTextColor(Color.GRAY);
						tvaccept.setText("�ѽ���");
			 			tvaccept.setClickable(false);
			 			tvaccept.setEnabled(false);
			 			tvaccept.setTextColor(Color.GRAY);
						 rightLinearLayout.removeAllViews();
						 acceptFun();
						 rightLinearLayout.addView(acceptRelativeLayout);
						
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
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
			
			
			dialogWronFun("�ύ�ɹ�!!!!!!",MyTaskMainActivity.this);
		}
		
	}
	 public void dialogWronFun(CharSequence str,Context context){
	      	LayoutInflater inflater=(LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
			    View view=inflater.inflate(R.layout.worningdialog, null);
			    TextView tv=(TextView) view.findViewById(R.id.worningTv);
			    tv.setText(str);
	      	AlertDialog.Builder alert=new AlertDialog.Builder(context);
	      	alert.setView(view);
	      	alert.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
	      			
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
				// ������յ��name���и�ֵ��Ҳ����ֱ�Ӷ����긳ֵ����ֵ�����򽫸��������������
				MKPlanNode stNode = new MKPlanNode();
				stNode.name = str_qishi;
				//stNode.pt �������궨λ
				MKPlanNode enNode = new MKPlanNode();
				enNode.name = str_daoda;
				
				// ʵ��ʹ�����������յ���н�����ȷ���趨
				mMKSearch.drivingSearch("����", stNode, "����", enNode);
				
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
			if(M_LuKuangBtn.getText().toString().equals("ʵʱ·��")){
				mMapView.setTraffic(true);
				M_LuKuangBtn.setText("�ر�·��");
			}else if(M_LuKuangBtn.getText().toString().equals("�ر�·��")){
				mMapView.setTraffic(false);
				M_LuKuangBtn.setText("ʵʱ·��");
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
	  	  group1.put("group", "�����÷¤�ߴ�����������Э��"); 
	  	  Map<String, String> group2 = new HashMap<String, String>(); 
	  	  group2.put("group", "������������·1555Ū��С�㹺������"); 
	  	  groups.add(group1); 
	  	  groups.add(group2); 

	  	//׼����һ��һ���б��еĶ����б�����:���������б�,�ֱ���ʾ"childData1"��"childData2" 
	  	  List<Map<String, String>> child1 = new ArrayList<Map<String, String>>(); 
	  	  Map<String, String> child1Data1 = new HashMap<String, String>(); 
	  	  child1Data1.put("time", "2011��1��1�� 11:00"); 
	  	  child1Data1.put("address", "�����÷¤�ߴ�");
	  	  child1Data1.put("person", "������");
	  	  child1Data1.put("phone", "13873844140");
	  	  child1Data1.put("info", "��������������Э�����赽���ֳ�Э�����˽�������򡣡���������������");
	  	  child1.add(child1Data1); 

	  	  //׼���ڶ���һ���б��еĶ����б�����:һ�������б�,��ʾ"child2Data1" 
	  	  List<Map<String, String>> child2 = new ArrayList<Map<String, String>>(); 
	  	  Map<String, String> child2Data1 = new HashMap<String, String>(); 
	  	  child2Data1.put("time", "2011��3��3�� 13:00"); 
	 	  child2Data1.put("address", "������������·1555Ū");
	 	  child2Data1.put("person", "�Ŵ���");
	 	  child2Data1.put("phone", "13391012240");
	 	  child2Data1.put("info", "��С���й����յ�������ǰ���ֳ��������˽Ȿ���յ�һЩ���������������������������");
	  	  child2.add(child2Data1); 
	  	//��һ��list���󱣴����еĶ����б����� 
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
     * @author ������
     * ��������ļ����¼�
     */
    public void acceptFun(){
 	   List<Map<String,String>> acceptedList=new ArrayList<Map<String,String>>();
			Map<String,String> acceptMap1=new HashMap<String,String>();
			acceptMap1.put("returnreason", "�˰�������ﲻ�ϸ�");
			acceptMap1.put("returnperson", "л����");
			acceptMap1.put("returnphone", "13873844142");
			acceptMap1.put("returnJobNumber", "007");
			acceptedList.add(acceptMap1);
			Map<String,String> acceptMap2=new HashMap<String,String>();
			acceptMap2.put("returnreason", "�˰�������ﲻ�ϸ�");
			acceptMap2.put("returnperson", "л����");
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
	 * @author ������
	 * Expandable �ؼ���ʹ�� 
	 * 
	 */
	public class mytaskExpandableAdapter extends BaseExpandableListAdapter{ 
		
		  private Context context; 
		  List<Map<String, String>> groups; 
		  List<List<Map<String, String>>> childs; 
		  /* 
		  * ���캯��: ExpandableAdapter
		  * ����1:context���� 
		  * ����2:һ���б�����Դ 
		  * ����3:�����б�����Դ 
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
		   * @author ������
		   * Ϊ�¼��Ŀؼ���ֵ
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
		    * ��������ʱ�����ݴ���
		    * @author ������
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
		      	alert.setTitle("��ʾ").setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
		      			
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							rightLinearLayout.removeAllViews();
				 		     acceptedTaskInfoOfValuesFun(tag);
				 			 acceptFun();
				 			 if(accepted == 1 && tag == 0){
				 				tvaccept.setText("�ѽ���");
					 			tvaccept.setClickable(false);
					 			tvaccept.setTextColor(Color.GRAY);
					 			acceptTaskBtn.setText("�ѽ���");
								acceptTaskBtn.setClickable(false);
								acceptTaskBtn.setTextColor(Color.GRAY); 
				 			 }else if(detail == 1 && tag == 1){
				 				tvaccept.setText("�ѽ���");
					 			tvaccept.setClickable(false);
					 			tvaccept.setEnabled(false);
					 			tvaccept.setTextColor(Color.GRAY);
					 			acceptTaskBtn.setText("�ѽ���");
					 			acceptTaskBtn.setEnabled(false);
								acceptTaskBtn.setClickable(false);
								acceptTaskBtn.setTextColor(Color.GRAY); 
				 			 }
				 			
				 			 rightLinearLayout.addView(acceptRelativeLayout);
						}
					})
					.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							return;
						}
					}).create().show();
		      	
		      }
		  //��ȡ�����б��View���� 
		
		  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
		   ViewGroup parent) 
		  { 
		   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 //��ȡ�����б��Ӧ�Ĳ����ļ�, �������Ԫ��������Ӧ������ 
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
				tvaccept.setText("�ѽ���");
	 			tvaccept.setClickable(false);
	 			tvaccept.setEnabled(false);
	 			tvaccept.setTextColor(Color.GRAY);
	 			acceptTaskBtn.setText("�ѽ���");
				acceptTaskBtn.setClickable(false);
				acceptTaskBtn.setEnabled(false);
				acceptTaskBtn.setTextColor(Color.GRAY); 
			 }
		   if(detail == 1 && groupPosition == 1){
			   tvaccept.setText("�ѽ���");
	 			tvaccept.setClickable(false);
	 			tvaccept.setEnabled(false);
	 			tvaccept.setTextColor(Color.GRAY);
	 			acceptTaskBtn.setText("�ѽ���");
				acceptTaskBtn.setClickable(false);
				acceptTaskBtn.setEnabled(false);
				acceptTaskBtn.setTextColor(Color.GRAY); 
		   }
		   /*
		    * ����鿴��ť�����¼�
		    * @author ������
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
	 				acceptTaskBtn.setText("����");
					acceptTaskBtn.setClickable(true);
					acceptTaskBtn.setEnabled(true);
					acceptTaskBtn.setTextColor(Color.WHITE); 
	 			 }else if(accepted == 1 && tag == 0){
	 				acceptTaskBtn.setText("�ѽ���");
					acceptTaskBtn.setClickable(false);
					acceptTaskBtn.setEnabled(false);
					acceptTaskBtn.setTextColor(Color.GRAY); 
	 			 }else if(detail == 0 && tag == 1){
	 				acceptTaskBtn.setText("����");
					acceptTaskBtn.setClickable(true);
					acceptTaskBtn.setEnabled(true);
					acceptTaskBtn.setTextColor(Color.WHITE); 
	 			 }else if(accepted == 1 && tag == 1){
	 				acceptTaskBtn.setText("�ѽ���");
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
			 			 dialogWronFun1("ȷ��������",MyTaskMainActivity.this,tag);
			 			 
				 	   }else if(tag==1){
				 		  detail = 1;
				 		  dialogWronFun1("ȷ��������",MyTaskMainActivity.this,tag);
				 			
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

		  //��ȡһ���б�View���� 
		  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		   { 
			  
		  String text = groups.get(groupPosition).get("group"); 
		  LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   
		  //��ȡһ���б����ļ�,������ӦԪ������ 
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
     * @author ������
     * ��ǰ������ʾ
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
       /* ��HistoryButtonListener
        * @author������
        * ��ʷ������ʾ
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
		 * ��ǰλ�ö�λ
		 */
			 public void locationOverlayFun(){
				   app = (BMapApiDemoApp)this.getApplication();
			 		if (app.mBMapMan == null) {
			 			app.mBMapMan = new BMapManager(getApplication());
			 			app.mBMapMan.init(app.mStrKey, new BMapApiDemoApp.MyGeneralListener());
			 		}
			 		app.mBMapMan.start();
			         // ���ʹ�õ�ͼSDK�����ʼ����ͼActivity
			         super.initMapActivity(app.mBMapMan);
			         //layout1=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			          //relative=(RelativeLayout) layout1.inflate(R.layout.map_view, null);
			         mMapView = (MapView)findViewById(R.id.mytask_bmapView);
			         mMapView.setBuiltInZoomControls(true);
			         //���������Ŷ���������Ҳ��ʾoverlay,Ĭ��Ϊ������
			         mMapView.setDrawOverlayWhenZooming(true);
			         
			 		// ��Ӷ�λͼ��
			         mLocationOverlay = new MyLocationOverlay(this, mMapView);
			        
			 		mMapView.getOverlays().add(mLocationOverlay);
			         // ע�ᶨλ�¼�
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
				 * ��ǰ��γ�ȶ�λ
				 * �����ݾ�γ�ȶ�λ��ǰ�ֵ���Ϣ
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
			        // ע�ᶨλ�¼�
			        myLocationListener = new LocationListener(){
						public void onLocationChanged(Location location) {
						
							jingdu=location.getLongitude();
							weidu=location.getLatitude();
							if(location != null){
								String strLog = String.format("����ǰ��λ��:\r" +
										"γ��:%f\r" +
										"����:%f",
										location.getLongitude(), location.getLatitude());
								 try {  
					                    // ���û�����ľ�γ��ֵת����int����   
					                    int longitude = (int) (1E6 * jingdu);  
					                    int latitude = (int) (1E6 *weidu);  
					                    // ��ѯ�þ�γ��ֵ����Ӧ�ĵ�ַλ����Ϣ   
					                    
					                    mMKSearch.reverseGeocode(new GeoPoint(latitude, longitude));  
					                    
					                } catch (Exception e) {  
					                	jingwei="��ѯ��������������ľ�γ��ֵ��";  
					                }  
								

							}
						}
			        };
			       
			       return jingwei;
			 }
			 /** 
			  * ʵ��MKSearchListener�ӿ�,����ʵ���첽�������񣬵õ�������� 
			  *  
			  * @author liufeng 
			  */  
			 public class MySearchListener implements MKSearchListener {  
			     /** 
			      * ���ݾ�γ��������ַ��Ϣ��� 
			      * @param result ������� 
			      * @param iError ����ţ�0��ʾ��ȷ���أ� 
			      */  
			 
				
			        public void onGetAddrResult(MKAddrInfo result, int iError) {  
			        	if (result == null) {  
			                return;  
			            }  
			            StringBuffer sb = new StringBuffer();  
			            // ��γ������Ӧ��λ��   
			            sb.append(result.strAddr).append("/n");  
			  
			            // �жϸõ�ַ�����Ƿ���POI��Point of Interest,����Ȥ�㣩   
			             /*if (null != result.poiList) {  
			                // �������е���Ȥ����Ϣ   
			                for (MKPoiInfo poiInfo : result.poiList) {  
			                    sb.append("���ƣ�").append(poiInfo.name).append("/n");  
			                    sb.append("��ַ��").append(poiInfo.address).append("/n");  
			                    sb.append("���ȣ�").append(poiInfo.pt.getLongitudeE6() / 1000000.0f).append("/n");  
			                    sb.append("γ�ȣ�").append(poiInfo.pt.getLatitudeE6() / 1000000.0f).append("/n");  
			                    sb.append("�绰��").append(poiInfo.phoneNum).append("/n");  
			                    sb.append("�ʱࣺ").append(poiInfo.postCode).append("/n");  
			                    // poi���ͣ�0����ͨ�㣬1������վ��2��������·��3������վ��4��������·   
			                    sb.append("���ͣ�").append(poiInfo.ePoiType).append("/n");  
			                }  
			            } */
			            // ����ַ��Ϣ����Ȥ����Ϣ��ʾ��TextView��   
			      

			            jingwei=sb.toString();
			        }  

			   
			     /** 
			      * �ݳ�·��������� 
			      * @param result ������� 
			      * @param iError ����ţ�0��ʾ��ȷ���أ� 
			      */  
			     
			     public void onGetDrivingRouteResult(MKDrivingRouteResult result, int iError) {  
			    	// ����ſɲο�MKEvent�еĶ���
						if (iError != 0 || result == null) {
							Toast.makeText(MyTaskMainActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
							return;
						}
						RouteOverlay routeOverlay = new RouteOverlay(MyTaskMainActivity.this, mMapView);
					    // �˴���չʾһ��������Ϊʾ��
					    routeOverlay.setData(result.getPlan(0).getRoute(0));
					    mMapView.getOverlays().clear();
					    mMapView.getOverlays().add(routeOverlay);
					    mMapView.invalidate();
					    
					    mMapView.getController().animateTo(result.getStart().pt);
					    
					    
					 

			     }  
			   
			     /** 
			      * POI�����������Χ����������POI�������ܱ߼����� 
			      * @param result ������� 
			      * @param type ���ؽ�����ͣ�11,12,21:poi�б� 7:�����б� 
			      * @param iError ����ţ�0��ʾ��ȷ���أ� 
			      */  
			
			     public void onGetPoiResult(MKPoiResult result, int type, int iError) {  
			    	 if (result == null) {  
			                return;  
			            }  
			    	  mapController = mMapView.getController();  
			    	  mapController.setZoom(10);  
			            // �����ͼ�����е����и�����   
			    	  mMapView.getOverlays().clear();  
			            // PoiOverlay��baidu map api�ṩ��������ʾPOI��Overlay   
			            PoiOverlay poioverlay = new PoiOverlay(MyTaskMainActivity.this, mMapView);  
			            // ������������POI����   
			            poioverlay.setData(result.getAllPoi());  
			            // �ڵ�ͼ����ʾPoiOverlay��������������Ȥ���ע�ڵ�ͼ�ϣ�   
			            mMapView.getOverlays().add(poioverlay);  
			  
			            if(result.getNumPois() > 0) {  
			                // ��������һ������������ڵ�������Ϊ��ͼ������   
			                MKPoiInfo poiInfo = result.getPoi(0);  
			                mapController.setCenter(poiInfo.pt);  
			            }  
			              
			            sb.append("��������").append(result.getNumPois()).append("��POI/n");  
			            // ������ǰҳ���ص�POI��Ĭ��ֻ����10����   
			            for (MKPoiInfo poiInfo : result.getAllPoi()) { 
			                sb.append("���ƣ�").append(poiInfo.name);  
			                //sb.append("��ַ��").append(poiInfo.address).append("/n");   
			                //sb.append("���ȣ�").append(poiInfo.pt.getLongitudeE6() / 1000000.0f).append("/n");   
			                //sb.append("γ�ȣ�").append(poiInfo.pt.getLatitudeE6() / 1000000.0f).append("/n");   
			            }  
			  
			            // ͨ��AlertDialog��ʾ��ǰҳ��������POI   
			            new AlertDialog.Builder(MyTaskMainActivity.this)  
			            .setTitle("��������POI��Ϣ")  
			            .setMessage(sb.toString())  
			            .setPositiveButton("�ر�", new DialogInterface.OnClickListener() {  
			                public void onClick(DialogInterface dialog, int whichButton) {  
			                    dialog.dismiss();  
			                }  
			            }).create().show();  
			     }  
			   
			     /** 
			      * ��������·��������� 
			      * @param result ������� 
			      * @param iError ����ţ�0��ʾ��ȷ���أ� 
			      */  
			
			     public void onGetTransitRouteResult(MKTransitRouteResult result, int iError) {  
			     }  
			   
			     /** 
			      * ����·��������� 
			      * @param result ������� 
			      * @param iError ����ţ�0��ʾ��ȷ���أ� 
			      */  
			   
			     public void onGetWalkingRouteResult(MKWalkingRouteResult result, int iError) {  
			     }  
			 }  
	@Override
			protected void onDestroy() {
				// TODO Auto-generated method stub
		mMapView.setTraffic(false);
		M_LuKuangBtn.setText("ʵʱ·��");
				super.onDestroy();
			}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mMapView.setTraffic(false);
		M_LuKuangBtn.setText("ʵʱ·��  ");
		super.onStop();
	}
	@Override
	protected void onPause() {
		app = (BMapApiDemoApp)this.getApplication();
		app.mBMapMan.getLocationManager().removeUpdates(mLocationListener);
		mLocationOverlay.disableMyLocation();
        mLocationOverlay.disableCompass(); // �ر�ָ����
        app.mBMapMan.getLocationManager().removeUpdates(myLocationListener);
		
		app.mBMapMan.stop();
		super.onPause();
		 
	}
	@Override
	protected void onResume() {
	 app = (BMapApiDemoApp)this.getApplication();
		// ע�ᶨλ�¼�����λ�󽫵�ͼ�ƶ�����λ��
        app.mBMapMan.getLocationManager().requestLocationUpdates(mLocationListener);
        app.mBMapMan.getLocationManager().requestLocationUpdates(myLocationListener);
        mLocationOverlay.enableMyLocation();
        mLocationOverlay.enableCompass(); // ��ָ����
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
