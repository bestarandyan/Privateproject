package com.zhihuigu.sosoOffice;

import com.zhihuigu.sosoOffice.utils.MessageBox;
import com.zhihuigu.sosoOffice.utils.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class InfoSetActivity extends BaseActivity{
	private Button backBtn;//���ذ�ť
	private TextView textView;//��������
	private EditText editText;//�����
	private Button submitBtn;//�ύ��ť
	private TextView title;//����
	private int type = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.a_infoset);
		backBtn = (Button) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(this);
		textView = (TextView) findViewById(R.id.textView);
		editText = (EditText) findViewById(R.id.NameEt);
		submitBtn = (Button) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		initView();
	}
	private void initView(){
		editText.setText(getIntent().getStringExtra("value"));
		 type = getIntent().getIntExtra("type", 1);
		switch(type){
		case 1:
			title.setText("��ʵ����");
			textView.setText("��ʵ����");
			editText.setHint("������������ʵ����");
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
			break;
		case 2:
			title.setText("�Ա�");
			textView.setText("�Ա�");
			editText.setHint("�����������Ա�");
			break;
		case 3:
			title.setText("����");
			textView.setText("����");
			editText.setHint("��������������");
			break;
		case 4:
			title.setText("��������");
			textView.setText("��������");
			editText.setHint("���������ĵ�������");
			break;
		case 5:
			title.setText("�ֻ�����");
			textView.setText("�ֻ�����");
			editText.setHint("�����������ֻ�����");
			editText.setInputType(InputType.TYPE_CLASS_PHONE);
			break;
		}
	}
	
	
	/**
	 * author by Ring ע��ǰ���ύ��Ϣ������֤ return true ��֤�ɹ���false ��֤ʧ��
	 */
	public boolean textValidate() {
		if (editText.getText().toString().trim().equals("")&&type==5) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.telephone_null),
					this);
			return false;
		} else if (!StringUtils
						.isCellphone(editText.getText().toString().trim())&&type==5) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.phone_error),
					this);
			return false;
		}else if (editText.getText().toString().trim().equals("")&&type==4) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.email_null),
					this);
			return false;
		}  else if (!StringUtils.isEmail(editText.getText().toString().trim())&&type==4) {
			MessageBox.CreateAlertDialog(
					getResources().getString(R.string.email_error),
					this);
			return false;
		}
		return true;
	}
	@Override
	public void onClick(View v) {
		if(v == backBtn){
			finish();
		}else if(v == submitBtn){
			if(textValidate()){
				Intent intent = new Intent();
				intent.putExtra("value", editText.getText().toString());
				intent.putExtra("type", type);
				setResult(RESULT_OK, intent);
				finish();
			}
			
		}
		super.onClick(v);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return true;
	}
}
