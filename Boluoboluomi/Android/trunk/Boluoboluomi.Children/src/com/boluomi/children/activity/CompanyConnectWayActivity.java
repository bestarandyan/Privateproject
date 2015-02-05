/**
 * 
 */
package com.boluomi.children.activity;

import com.boluomi.children.R;
import com.boluomi.children.data.UserBeanInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author 刘星星
 * @createDate 2013/8/21
 * 公司联系方式类
 *
 */
public class CompanyConnectWayActivity extends BaseActivity{
	Button backBtn;
	TextView contentTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_companyconnectway);
		backBtn = (Button) findViewById(R.id.backBtn);
		contentTv = (TextView) findViewById(R.id.contentTv);
		String msg = UserBeanInfo.storeDetail.get(0).get("contact").toString();
		contentTv.setText(msg);
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
