package guoTeng.LoadIC2ReadCard;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class MainActivity  extends Activity {
    /** Called when the activity is first created. */
	//选项图片列表
	private GridView gridView;
	private Button calcleBtn;
	private Handler mHandler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);      
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);      
        setContentView(R.layout.l_piaoguanjiashow);
        ExitApplication.getInstance().addActivity(MyApplication.getInstance().getPgjActivity());
        ExitApplication.getInstance().addActivity(MainActivity.this);
        InitView();
        mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
    }
    public void disableHomeKey(){
	   this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
	   }

	Runnable mDisableHomeKeyRunnable = new Runnable() {
		   public void run() {
		   disableHomeKey();
		   }
	};
		   
    @Override
	protected void onResume() {
    	mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
		super.onResume();
	}
	/**
     * 初始化布局文件
     */
    public void InitView(){
    	gridView = (GridView)this.findViewById(R.id.gridview);
    	gridView.setAdapter(new ImageAdapter(this));	
        gridView.setOnItemClickListener(gridlistener);
        calcleBtn = (Button) findViewById(R.id.cancleUser);
        calcleBtn.setOnClickListener(new CancleBtnLinstener());
    }
    /**
     * GridView的点击事件
     */
    private OnItemClickListener gridlistener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent();
			switch (arg2) {
			case 0:
				intent.setClass(MainActivity.this, LoadIC2ReadCard.class);
				intent.putExtra("activity_flag", 1);
				MainActivity.this.startActivity(intent);
				MainActivity.this.finish();
				break;
			case 1:
				intent.setClass(MainActivity.this, LoadIC2ReadCard.class);
				intent.putExtra("activity_flag", 2);
				MainActivity.this.startActivity(intent);
				MainActivity.this.finish();
				break;
			case 2:
				intent.setClass(MainActivity.this, HistoryTicket.class);
				MainActivity.this.startActivity(intent);
				MainActivity.this.finish();
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:return true;
		case KeyEvent.KEYCODE_CALL:return true;
		case KeyEvent.KEYCODE_SYM: return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
		case KeyEvent.KEYCODE_VOLUME_UP: return true;
		case KeyEvent.KEYCODE_STAR: return true;
		case KeyEvent.KEYCODE_CAMERA: return true;
		case KeyEvent.KEYCODE_POWER: return true;
		}
		return super.onKeyDown(keyCode, event);
	}
   private class CancleBtnLinstener implements View.OnClickListener{

		public void onClick(View v) {
			// TODO Auto-generated method stub
			final EditText et = new EditText(MainActivity.this);
			et.setTransformationMethod(PasswordTransformationMethod.getInstance());
			AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
			ab.setTitle("请输入注销用户的权限命令：");
			ab.setIcon(android.R.drawable.ic_dialog_alert);
			ab.setView(et);
			ab.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					if(et.getText().toString().equals("123")){
						while(true){
							ExitApplication.getInstance().onTerminate(); 
							finish();
							android.os.Process.killProcess(android.os.Process.myPid());  
							System.gc();  
							System.exit(0);  
						}
					}else{
						Toast.makeText(MainActivity.this,"此命令不容许通过。。。", 5000).show();
						return;
					}
					
				}
			});
			ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					return;
				}
			});
			ab.show();
		}
   }
}
