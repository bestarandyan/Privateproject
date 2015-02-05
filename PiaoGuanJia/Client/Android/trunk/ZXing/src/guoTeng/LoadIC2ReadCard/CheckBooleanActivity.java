package guoTeng.LoadIC2ReadCard;

import com.google.zxing.client.android.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CheckBooleanActivity extends Activity{
	private LockLayer lockLayer = null;
	View lock = null;
	private Button backBtn;
	private TextView checkTv;
	private TextView cancleTv;
	private DetailOrderActivity orderActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);      
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
	    lock = View.inflate(this, R.layout.a_checkboolean, null);
        lockLayer = new LockLayer(this);
        lockLayer.setLockView(lock);
        lockLayer.lock();
        ExitApplication.getInstance().addActivity(CheckBooleanActivity.this);
        backBtn = (Button) lock.findViewById(R.id.backBtn);
        checkTv = (TextView) lock.findViewById(R.id.checkTv);
        cancleTv = (TextView) lock.findViewById(R.id.cancleTv);
        checkTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for(int i=0;i<ExitApplication.getInstance().activities.size();i++){
					if(ExitApplication.getInstance().activities.get(i).getClass() == DetailOrderActivity.class){
						ExitApplication.getInstance().activities.get(i).finish();
					}
				}
				Intent intent = new Intent(CheckBooleanActivity.this,DetailCheckingActivity.class);
				intent.putExtra("displayContents", getIntent().getStringExtra("displayContents").toString());
			    startActivity(intent);
			    finish();
			}
		});
        backBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        cancleTv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	protected void onDestroy() {
		lockLayer.unlock();
		super.onDestroy();
	}
	
	}
