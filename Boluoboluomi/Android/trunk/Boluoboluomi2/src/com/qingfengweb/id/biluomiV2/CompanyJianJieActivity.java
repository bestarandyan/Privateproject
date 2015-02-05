/**
 * 
 */
package com.qingfengweb.id.biluomiV2;

import com.qingfengweb.id.biluomiV2.R;
import com.qingfengweb.data.UserBeanInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author qingfeng
 *
 */
public class CompanyJianJieActivity extends BaseActivity{
	Button backBtn;
	TextView contentTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_companyjianjie);
		backBtn = (Button) findViewById(R.id.backBtn);
		contentTv = (TextView) findViewById(R.id.contentTv);
		String msg = UserBeanInfo.storeDetail.get(0).get("introduce").toString();
		contentTv.setText(msg);
		
		backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
