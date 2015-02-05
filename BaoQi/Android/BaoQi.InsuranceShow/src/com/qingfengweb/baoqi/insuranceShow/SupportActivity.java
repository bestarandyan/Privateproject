package com.qingfengweb.baoqi.insuranceShow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.qingfengweb.baoqi.hospitalquery.HospitalQuery;
import com.qingfengweb.baoqi.insuranceShow.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SupportActivity extends Activity {
	
	
	private Button btn1;
	private Button btn2;
	private Button btn3;
	private Button btn4;
	private Button btn5;
	private Button h_backhomebtn;
	private Button homebtn;
	
	private TextView text1,text2,text3;
	
	private LinearLayout zhiye_right,right_linear;
	
	private ImageView image1,image2;
	
	private LinearLayout linear ;
	private LinearLayout four_ui;
	
	
	private int[] imagearray={R.drawable.service_guide,R.drawable.supportimage2};
	
	private LayoutInflater layout;
	public String[] group=new String[]{"学龄前儿童","离退休人员","公务员、职员","一般职业","农牧业","渔业"};  
	private String[][] childs=new String[][]{
			{},
			{},
			{},
			{"机关、团体、公司","公务员、职员","维修工、司机"},
			{"养殖工人","捕鱼人","水产实验人员","水产品加工人员","水族馆经营者","海洋渔船船员","近海渔业","远洋渔业","海洋渔船船员","渔场管理人员"},
			{"种植业","果农","农业技师","农具商","农业实验人员","苗圃工","农业工人"}};
	private ExpandableListView expandableView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉手机上的标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.l_support);
        layout=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        right_linear=(LinearLayout) findViewById(R.id.right_linearLayout);
        zhiye_right=(LinearLayout) layout.inflate(R.layout.zhiye_right, null);
        four_ui = (LinearLayout) findViewById(R.id.four_ui);
        right_linear.addView(zhiye_right);
        
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2);
        text3 = (TextView)findViewById(R.id.text3);
        
        btn1 = (Button)findViewById(R.id.btn_1);
        btn2 = (Button)findViewById(R.id.btn_2);
        btn3 = (Button)findViewById(R.id.btn_3);
        btn4 = (Button)findViewById(R.id.btn_4);
        btn5 = (Button)findViewById(R.id.btn_5);
        h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				image1.destroyDrawingCache();
		        image2.destroyDrawingCache();
		        image1 = null;
		        image2 = null;
				Intent intent = new Intent();
			  	intent.setClass(SupportActivity.this, InsuranceShowMainActivity.class);
			  	SupportActivity.this.startActivity(intent);
			  	SupportActivity.this.finish();
			  	System.gc();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				image1.destroyDrawingCache();
		        image2.destroyDrawingCache();
		        image1 = null;
		        image2 = null;
				Intent intent = new Intent();
			  	intent.setClass(SupportActivity.this, InsuranceShowMainActivity.class);
			  	SupportActivity.this.startActivity(intent);
			  	SupportActivity.this.finish();
			  	System.gc();
			}
		});
        
        image1 = (ImageView)findViewById(R.id.image1);
        image2 = (ImageView)findViewById(R.id.image2);
        linear = (LinearLayout)findViewById(R.id.main_relative);
        four_ui = (LinearLayout) findViewById(R.id.four_ui);
        try{
        	image1.setBackgroundResource(imagearray[0]);
        }catch(OutOfMemoryError e){
        	Toast.makeText(this, "内存不足，请重试", 4000).show();
        }
        
        image2.destroyDrawingCache();
        
        
        btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn1.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.service_tag03);
				btn5.setBackgroundResource(R.drawable.single_tag02);
				
				
				btn1.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				image1.setVisibility(View.VISIBLE);
				image2.setVisibility(View.GONE);
				linear.setVisibility(View.GONE);
				four_ui.setVisibility(View.GONE);
				try{
		        	image1.setBackgroundResource(imagearray[0]);
		        }catch(OutOfMemoryError e){
		        	Toast.makeText(SupportActivity.this, "内存不足，请重新打开此软件", 2000);
		        }
		        image2.destroyDrawingCache();
				
			}
		});
        
        btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn2.setBackgroundResource(R.drawable.single_tag01_on);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.service_tag03);
				btn5.setBackgroundResource(R.drawable.single_tag02);
				
				
				btn2.setTextColor(Color.BLACK);
				btn1.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				image2.setVisibility(View.VISIBLE);
				image1.setVisibility(View.GONE);
				linear.setVisibility(View.GONE);
				four_ui.setVisibility(View.GONE);
				try{
		        	image2.setBackgroundResource(imagearray[1]);
		        }catch(Exception e){
		        	Toast.makeText(SupportActivity.this, "内存不足，请重新打开此软件", 2000);
		        }
		        image1.destroyDrawingCache();
			}
		});

        btn3.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
        		btn3.setBackgroundResource(R.drawable.single_tag01_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn4.setBackgroundResource(R.drawable.service_tag03);
				btn5.setBackgroundResource(R.drawable.single_tag02);
				
				
				btn3.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn4.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				linear.setVisibility(View.VISIBLE);
				image2.setVisibility(View.GONE);
				image1.setVisibility(View.GONE);
				four_ui.setVisibility(View.GONE);
				image1.destroyDrawingCache();
		        image2.destroyDrawingCache();
        	}
        });
        btn4.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
        		btn4.setBackgroundResource(R.drawable.service_tag03_on);
				btn2.setBackgroundResource(R.drawable.single_tag01);
				btn3.setBackgroundResource(R.drawable.single_tag01);
				btn1.setBackgroundResource(R.drawable.single_tag01);
				btn5.setBackgroundResource(R.drawable.single_tag02);
				
				
				btn4.setTextColor(Color.BLACK);
				btn2.setTextColor(Color.WHITE);
				btn3.setTextColor(Color.WHITE);
				btn1.setTextColor(Color.WHITE);
				btn5.setTextColor(Color.WHITE);
				four_ui.setVisibility(View.VISIBLE);
				image2.setVisibility(View.GONE);
				linear.setVisibility(View.GONE);
				image1.setVisibility(View.GONE);
				image1.destroyDrawingCache();
		        image2.destroyDrawingCache();
				
        	}
        });
        btn5.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
        		Intent intent = new Intent();
			  	intent.setClass(SupportActivity.this, HospitalQuery.class);
			  	SupportActivity.this.startActivity(intent);
			  	SupportActivity.this.finish();
			  	image1.destroyDrawingCache();
		        image2.destroyDrawingCache();
		        image1 = null;
		        image2 = null;
        	}
        });
        
        
        expandableView=(ExpandableListView) findViewById(R.id.expandableListView);
		expandableView.setCacheColorHint(0);
		expandableView.setDividerHeight(0);
	  	ExpandableAdapter adapter = new ExpandableAdapter(this); 
	  	expandableView.setAdapter(adapter);
	  	expandableView.setOnGroupExpandListener(new OnGroupExpandListener() {
			
			@Override
			public void onGroupExpand(int groupPosition) {
				// TODO Auto-generated method stub
				  for(int i=0;i<group.length;i++)
			        {
			                if(groupPosition!=i)
			                {
			                	expandableView.collapseGroup(i);
			                }
			        }

			}
		});
	  	expandableView.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
//				if(groupPosition >= 2){
//					leixing.setText(group[groupPosition]);
//					current.setText(group[groupPosition]);
//					setContentView(mainLinear);
//					right_linear.removeAllViews();
//					baodaixinxi_List.removeAllViews();
//            		right_linear.addView(baodan_right);
//            		setContentView(mainLinear);
//					v.setEnabled(true);
//				}else{
//					v.setEnabled(true);
//					baodaixinxi_List.removeAllViews();
//					right_linear.removeAllViews();
//            		right_linear.addView(baodan_right);
//            		setContentView(mainLinear);
//				}
				return false;
			}
		});
	  	expandableView.setOnChildClickListener(new OnChildClickListener(){   
	  	  
            @Override  
            public boolean onChildClick(ExpandableListView arg0, View arg1,   
                    int arg2, int arg3, long arg4) {   
                // TODO Auto-generated method stub   
                //Toast.makeText(PrivateMessage.this,"[Child Click]:"+arg2+":"+arg3,Toast.LENGTH_LONG).show();   
                
                if(childs[arg2][arg3].equals("") || childs[arg2][arg3] == null){
                	text1.setText(group[arg2]);
                	text2.setText("100001");
                	text3.setText("1");
                }else{
                	text1.setText(childs[arg2][arg3]);
                	text2.setText("100001");
                	text3.setText("1");
                }
                return true;   
            }   
               
        });  
        
        
	}
	
	
	public class ExpandableAdapter extends BaseExpandableListAdapter{ 
		private Context context; 

		
		 
		  /* 
		  * 构造函数: ExpandableAdapter
		  * 参数1:context对象 
		  * 参数2:一级列表数据源 
		  * 参数3:二级列表数据源 
		  */ 
		  public ExpandableAdapter(Context context)
		   { 
		  this.context = context; 
		  } 
		  public Object getChild(int groupPosition, int childPosition) 
		  {   
		  return childs[groupPosition][childPosition];
		  } 
		  public long getChildId(int groupPosition, int childPosition) 
		  { 
		  return childPosition; 
		  } 
		
		  //获取二级列表的View对象 
		
		  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
		   ViewGroup parent) 
		  { 
		   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 //获取二级列表对应的布局文件, 并将其各元素设置相应的属性 
		  LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.expandle_child, null);
		  TextView tv=(TextView) linearLayout.findViewById(R.id.child_text);
		  tv.setText(getChild(groupPosition, childPosition).toString());
		 
		return linearLayout; 
		  } 

		
		  public int getChildrenCount(int groupPosition) 
		  { 
		  return childs[groupPosition].length; 
		  } 


		  public Object getGroup(int groupPosition) 
		  { 
		  return group[groupPosition]; 
		  } 

		  public int getGroupCount() 
		  { 
		  return group.length; 
		  } 


		  public long getGroupId(int groupPosition) 
		  { 
			 
			 
		  return groupPosition; 
		  } 

		  //获取一级列表View对象 
		  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		   { 
			  
		  String text = group[groupPosition]; 
		  LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   
		  //获取一级列表布局文件,设置相应元素属性 
		  LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.group, null);
		   TextView textView = (TextView)linearLayout.findViewById(R.id.groupTo); 
		   ImageView i=(ImageView) linearLayout.findViewById(R.id.groupimageView);
		   if(!isExpanded){
			   i.setImageResource(R.drawable.leftadd);
			   
		   }else{
			   for(int ig=0;ig<parent.getChildCount();ig++){
				   if(ig != groupPosition){
					   parent.getChildAt(ig).setEnabled(false);
				   }else{
					   parent.getChildAt(ig).setEnabled(true);
				   }
			   }
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
	
	
	@Override

	 public boolean onKeyDown(int keyCode, KeyEvent event){

	  if(KeyEvent.KEYCODE_BACK==keyCode){
		  		image1.destroyDrawingCache();
		  		image2.destroyDrawingCache();
		  		image1 = null;
		  		image2 = null;
			  	Intent intent = new Intent();
			  	intent.setClass(SupportActivity.this, InsuranceShowMainActivity.class);
			  	SupportActivity.this.startActivity(intent);
			  	SupportActivity.this.finish();
			  	System.gc();
		 
	  }
	  return true;
	}
}
