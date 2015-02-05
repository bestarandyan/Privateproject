package com.qingfengweb.baoqi.collectInfo;

import com.qingfengweb.baoqi.propertyInsurance.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CommInfoActivity extends Activity{
private ListView commList;
private String jibing[]={"门诊或住院病例","意外事故证明","伤残鉴定报告"};
private String yiwai[]={"门诊或住院病例","费用收据原件","费用清单","意外事故证明"};
private String fayuanxuangao[]={"被保人身份证明","受益人身份证明","受益人与事故者关系证明","意外事故证明",
		"死亡证明（户口注销、殓葬证明）"};
private String commArray[]=null;
private String zhenDizhi=null;
/*private String peikuang[]={"","","","","","","","",""};
private String peikuang[]={"","","","","","","","",""};
private String peikuang[]={"","","","","","","","",""};
private String peikuang[]={"","","","","","","","",""};
private String peikuang[]={"","","","","","","","",""};
private String peikuang[]={"","","","","","","","",""};
private String peikuang[]={"","","","","","","","",""};
private String peikuang[]={"","","","","","","","",""};
private String peikuang[]={"","","","","","","","",""};
private String peikuang[]={"","","","","","","","",""};
private String peikuang[]={"","","","","","","","",""};
private String peikuang[]={"","","","","","","","",""};*/
public int value;
@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.comminfo);
		commList=(ListView) findViewById(R.id.commListView);
		zhenDizhi=getIntent().getStringExtra("putString");
		Intent intent=getIntent();
		 value=intent.getIntExtra("btnvalue", 0);
		switch(value){
		case 1:
			commArray = jibing;
			break;
		case 2:
			commArray = yiwai;
			break;
		case 3:
			commArray = fayuanxuangao;
			break;
		
		
		}
		ListAdapter adapter = new ListAdapter();
		
		commList.setAdapter(adapter);
		commList.setCacheColorHint(0);
		///commList.setDividerHeight(0);
		
	}

  class ListAdapter extends BaseAdapter{
	  

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commArray.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return commArray[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater layout = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		convertView = (LinearLayout) layout.inflate(R.layout.btnlistchild, null);
		TextView text = (TextView) convertView.findViewById(R.id.listText);
		text.setText(commArray[position]);
		Button btn=(Button) convertView.findViewById(R.id.list_btn_camera);
		btn.setTag(position);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent i = new Intent(CommInfoActivity.this, CameraPreview.class);
				i.putExtra("btnvalue", value);
				i.putExtra("commDiZhi", zhenDizhi);
				startActivityForResult(i, 1);
				finish();
				
			}
		});
		return convertView;
	}
	  
  }
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	// TODO Auto-generated method stub
	
	switch(keyCode){
	case KeyEvent.KEYCODE_BACK:
		Intent intent = new Intent(this,CollCarInfo.class);
		startActivity(intent);
		finish();
		break;
	}
	return super.onKeyDown(keyCode, event);
}
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
}

/*@Override
public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	// TODO Auto-generated method stub
	
}
@Override
public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
	
}*/
}
