/**
 * 
 */
package com.qingfengweb.baoqi.propertyInsurance;

import com.qingfengweb.baoqi.ext.SalsemanshipAdapter;
import com.qingfengweb.baoqi.propertyInsurance.ext.InstructionAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * @author QingFeng
 *
 */
public class SalesmanshipActivity extends Activity {
	private GridView gridView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.salsemanship_list_layout);
		gridView = (GridView)findViewById(R.id.gridview);
		gridView.setAdapter(new SalsemanshipAdapter(this));	
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long id) {
				Intent intent=new Intent(SalesmanshipActivity.this,SalesmanshipitemActivity.class);
				SalesmanshipActivity.this.startActivity(intent);
				SalesmanshipActivity.this.finish();
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		super.onKeyDown(keyCode, event);
		if(KeyEvent.KEYCODE_BACK==keyCode){
			Intent intent=new Intent(this,PropertyInsuranceMainActivity.class);
			this.startActivity(intent);
			this.finish();
		}
		
		return true;
	}
				
	
}
