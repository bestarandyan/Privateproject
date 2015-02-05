package com.qingfengweb.baoqi.propertyInsurance;

import com.qingfengweb.baoqi.hospitalquery.HospitalQuery;

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
	private Button h_backhomebtn;
	private Button homebtn;
	
	private TextView text1,text2,text3;
	
	private LinearLayout zhiye_right,right_linear;
	
	
	private LinearLayout linear ;
	private LinearLayout four_ui;
	
	
	
	private LayoutInflater layout;
	public String[] group=new String[]{"ѧ��ǰ��ͯ","��������Ա","����Ա��ְԱ","һ��ְҵ","ũ��ҵ","��ҵ"};  
	private String[][] childs=new String[][]{
			{},
			{},
			{},
			{"���ء����塢��˾","����Ա��ְԱ","ά�޹���˾��"},
			{"��ֳ����","������","ˮ��ʵ����Ա","ˮ��Ʒ�ӹ���Ա","ˮ��ݾ�Ӫ��","�����洬��Ա","������ҵ","Զ����ҵ","�����洬��Ա","�泡������Ա"},
			{"��ֲҵ","��ũ","ũҵ��ʦ","ũ����","ũҵʵ����Ա","���Թ�","ũҵ����"}};
	private ExpandableListView expandableView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//ȥ���ֻ��ϵı�����
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.l_support);
        layout=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        right_linear=(LinearLayout) findViewById(R.id.right_linearLayout);
        zhiye_right=(LinearLayout) layout.inflate(R.layout.zhiye_right, null);
        right_linear.addView(zhiye_right);
        
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2);
        text3 = (TextView)findViewById(R.id.text3);
        
        btn1 = (Button)findViewById(R.id.btn_1);
        btn2 = (Button)findViewById(R.id.btn_2);
        btn3 = (Button)findViewById(R.id.btn_3);
        h_backhomebtn = (Button)findViewById(R.id.h_backhomebtn);
        homebtn = (Button)findViewById(R.id.h_backHome);
        
        homebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(SupportActivity.this, PropertyInsuranceMainActivity.class);
			  	SupportActivity.this.startActivity(intent);
			  	SupportActivity.this.finish();
			  	System.gc();
			}
		});
        
        h_backhomebtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
			  	intent.setClass(SupportActivity.this, PropertyInsuranceMainActivity.class);
			  	SupportActivity.this.startActivity(intent);
			  	SupportActivity.this.finish();
			  	System.gc();
			}
		});
        
        linear = (LinearLayout)findViewById(R.id.main_relative);
        
        
        
        btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btn1.setBackgroundResource(R.drawable.single_tag01_on);
//				btn2.setBackgroundResource(R.drawable.single_tag01);
//				btn3.setBackgroundResource(R.drawable.single_tag01);
//				
//				
//				btn1.setTextColor(Color.BLACK);
//				btn2.setTextColor(Color.WHITE);
//				btn3.setTextColor(Color.WHITE);
//				linear.setVisibility(View.VISIBLE);
				
			}
		});
        
        btn2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("tag", 0);
				intent.setClass(SupportActivity.this, HospitalQuery.class);
				SupportActivity.this.startActivity(intent);
				SupportActivity.this.finish();
//				btn2.setBackgroundResource(R.drawable.service_tag02_on);
//				btn1.setBackgroundResource(R.drawable.single_tag01);
//				btn3.setBackgroundResource(R.drawable.service_tag02);
//				
//				
//				btn2.setTextColor(Color.BLACK);
//				btn1.setTextColor(Color.WHITE);
//				btn3.setTextColor(Color.WHITE);
//				linear.setVisibility(View.GONE);
			}
		});

        btn3.setOnClickListener(new View.OnClickListener() {
	
        	@Override
        	public void onClick(View v) {
        		
//        		btn3.setBackgroundResource(R.drawable.single_tag01_on);
//				btn2.setBackgroundResource(R.drawable.single_tag01);
//				btn1.setBackgroundResource(R.drawable.single_tag01);
				Intent intent = new Intent();
				intent.putExtra("tag", 1);
				intent.setClass(SupportActivity.this, HospitalQuery.class);
				SupportActivity.this.startActivity(intent);
				SupportActivity.this.finish();
//				
//				btn3.setTextColor(Color.BLACK);
//				btn2.setTextColor(Color.WHITE);
//				btn1.setTextColor(Color.WHITE);
//				linear.setVisibility(View.VISIBLE);
//				four_ui.setVisibility(View.GONE);
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
		  * ���캯��: ExpandableAdapter
		  * ����1:context���� 
		  * ����2:һ���б�����Դ 
		  * ����3:�����б�����Դ 
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
		
		  //��ȡ�����б��View���� 
		
		  public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
		   ViewGroup parent) 
		  { 
		   LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 //��ȡ�����б��Ӧ�Ĳ����ļ�, �������Ԫ��������Ӧ������ 
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

		  //��ȡһ���б�View���� 
		  public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
		   { 
			  
		  String text = group[groupPosition]; 
		  LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   
		  //��ȡһ���б����ļ�,������ӦԪ������ 
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
			  	Intent intent = new Intent();
			  	intent.setClass(SupportActivity.this, PropertyInsuranceMainActivity.class);
			  	SupportActivity.this.startActivity(intent);
			  	SupportActivity.this.finish();
			  	System.gc();
		 
	  }
	  return true;
	}
}
