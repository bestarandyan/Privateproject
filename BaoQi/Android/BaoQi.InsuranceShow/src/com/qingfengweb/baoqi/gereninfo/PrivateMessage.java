package com.qingfengweb.baoqi.gereninfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.qingfengweb.baoqi.insuranceShow.InsuranceShowMainActivity;
import com.qingfengweb.baoqi.insuranceShow.R;
import com.qingfengweb.baoqi.mytask.MyTaskMainActivity;

public class PrivateMessage extends Activity{
private ExpandableListView expandableView;
private  ArrayAdapter<CharSequence> adapter1;
private  ArrayAdapter<CharSequence> adapter2;
private  ArrayAdapter<CharSequence> adapter3;
private  ArrayAdapter<CharSequence> adapter4;
private  ArrayAdapter<CharSequence> adapter5;
private TextView t1,t2,t3,t4,t5,t6,t7;
private TextView leixing;
private LinearLayout baodaixinxi_List,baodanxinxi_item_list,baodan_right,right_linear;
private LinearLayout detail_info,worn_info;
private RelativeLayout mainLinear;
private Spinner spinner1,spinner2,spinner3,spinner4,spinner5;
private Button query_btn,clear_btn;
private Button shouye,current;
private LayoutInflater layout;
public String[] group=new String[]{"通知类清单","保单管理","新单报表","新单成交信息报表","个人工作轨迹报表","个人工作收入查询"};  
private String[][] childs=new String[][]{{"应付费清单","付费成功清单","应收费清单","收费成功清单","失效保单通知清单","收费未成功清单"},
		{"保单查询","新契约状态查询","理赔状态查询"},
		{},
		{},
		{},
		{}};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gereninfo);
		layout=(LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		mainLinear=(RelativeLayout) findViewById(R.id.main_relative);
		right_linear=(LinearLayout) findViewById(R.id.right_linearLayout);
		baodanxinxi_item_list=(LinearLayout) layout.inflate(R.layout.list_item, null);
		baodan_right=(LinearLayout) layout.inflate(R.layout.baodan_right, null);
		detail_info=(LinearLayout) layout.inflate(R.layout.detail_info, null);
		baodaixinxi_List=(LinearLayout) baodan_right.findViewById(R.id.baodanxinxi_view);
		worn_info=(LinearLayout) baodan_right.findViewById(R.id.list_null);
		right_linear.addView(baodan_right);
		setContentView(mainLinear);
		
		
		
		query_btn=(Button) baodan_right.findViewById(R.id.query_privateInfo);
		clear_btn=(Button) baodan_right.findViewById(R.id.clear_privateInfo);
		spinner1=(Spinner) baodan_right.findViewById(R.id.spinner1);
		spinner2=(Spinner) baodan_right.findViewById(R.id.spinner2);
		spinner3=(Spinner) baodan_right.findViewById(R.id.spinner3);
		spinner4=(Spinner) baodan_right.findViewById(R.id.spinner4);
		//spinner5=(Spinner) baodan_right.findViewById(R.id.spinner5);
		leixing=(TextView) baodan_right.findViewById(R.id.leixing_text);
		
		t1=(TextView) baodanxinxi_item_list.findViewById(R.id.text01);
		t2=(TextView) baodanxinxi_item_list.findViewById(R.id.text02);
		t3=(TextView) baodanxinxi_item_list.findViewById(R.id.text03);
		t4=(TextView) baodanxinxi_item_list.findViewById(R.id.text04);
		t5=(TextView) baodanxinxi_item_list.findViewById(R.id.text05);
		t6=(TextView) baodanxinxi_item_list.findViewById(R.id.text06);
		t7=(TextView) baodanxinxi_item_list.findViewById(R.id.text07);
		shouye=(Button) findViewById(R.id.p_backhome_btn);
		current=(Button) findViewById(R.id.success_info_btn);
		adapter1=ArrayAdapter.createFromResource(getApplicationContext(), R.array.nian,android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter1);
		adapter2=ArrayAdapter.createFromResource(getApplicationContext(), R.array.yue,android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);
		adapter3=ArrayAdapter.createFromResource(getApplicationContext(), R.array.nian,android.R.layout.simple_spinner_item);
		adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner3.setAdapter(adapter3);
		adapter4=ArrayAdapter.createFromResource(getApplicationContext(), R.array.yue,android.R.layout.simple_spinner_item);
		adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner4.setAdapter(adapter4);
		adapter5=ArrayAdapter.createFromResource(getApplicationContext(), R.array.qijian,android.R.layout.simple_spinner_item);
		adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	//	spinner5.setAdapter(adapter5);
		shouye.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(PrivateMessage.this,InsuranceShowMainActivity.class);
				startActivity(intent);
			}
		});
		query_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				baodaixinxi_List.removeAllViews();
				baodaixinxi_List.addView(baodanxinxi_item_list);
				setContentView(mainLinear);
				
			}
		});
		clear_btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				baodaixinxi_List.removeAllViews();
				baodaixinxi_List.addView(worn_info);
				setContentView(mainLinear);
			}
		});
		expandableView=(ExpandableListView) findViewById(R.id.expandableListView);
		expandableView.setCacheColorHint(0);
		expandableView.setDividerHeight(0);
	  	ExpandableAdapter viewAdapter = new ExpandableAdapter(this); 
	  	expandableView.setAdapter(viewAdapter); 
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
				if(groupPosition >= 2){
					leixing.setText(group[groupPosition]);
					current.setText(group[groupPosition]);
					setContentView(mainLinear);
					right_linear.removeAllViews();
					baodaixinxi_List.removeAllViews();
            		right_linear.addView(baodan_right);
            		setContentView(mainLinear);
					v.setEnabled(true);
				}else{
					v.setEnabled(true);
					baodaixinxi_List.removeAllViews();
					right_linear.removeAllViews();
            		right_linear.addView(baodan_right);
            		setContentView(mainLinear);
				}
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
                	leixing.setText(group[arg2]);
                	current.setText(group[arg2]);
                	right_linear.removeAllViews();
                	baodaixinxi_List.removeAllViews();
            		right_linear.addView(baodan_right);
            		setContentView(mainLinear);
                }else{
                	leixing.setText(childs[arg2][arg3]);
                	current.setText(childs[arg2][arg3]);
                	right_linear.removeAllViews();
                	baodaixinxi_List.removeAllViews();
            		right_linear.addView(baodan_right);
            		setContentView(mainLinear);
                }
                setContentView(mainLinear);
                return true;   
            }   
               
        });  
	  	t1.setOnClickListener(new textListener());
	  	t2.setOnClickListener(new textListener());
	  	t3.setOnClickListener(new textListener());
	  	t4.setOnClickListener(new textListener());
	  	t5.setOnClickListener(new textListener());
	  	t6.setOnClickListener(new textListener());
	  	t7.setOnClickListener(new textListener());
	}
   class textListener implements View.OnClickListener{

	@Override
	public void onClick(View v) { 
		// TODO Auto-generated method stub
		right_linear.removeAllViews();
		right_linear.addView(detail_info);
		setContentView(mainLinear);
	}
	   
   }
	/*
	 * @author 刘星星
	 * Expandable 控件的使用 
	 * 
	 */
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			Intent intent = new Intent(PrivateMessage.this,InsuranceShowMainActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
}
