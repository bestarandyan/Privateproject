package guoTeng.LoadIC2ReadCard;

import com.qingfengweb.content.DbContent;
import com.qingfengweb.db.ProjectDBHelper;
import com.qingfengweb.db.TicketBean;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

public class PiaoGuanJiaActivity extends Activity {
	private ProjectDBHelper dbHelper;
	private SQLiteDatabase database;
	private Handler mHandler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);      
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.first);
        ExitApplication.getInstance().addActivity(PiaoGuanJiaActivity.this);
        MyApplication.getInstance().setPgjActivity(this);
        dbHelper  = new ProjectDBHelper(PiaoGuanJiaActivity.this);
        Thread thread = new Thread(myRunnable);
        thread.start();
        
    }
	Runnable myRunnable = new Runnable(){
		public void run() {
			// TODO Auto-generated method stub
			try {
				mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
				createTable();
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.what = 0;
			myHandler.sendMessage(msg);
		}
		   
	   };
   Handler myHandler = new Handler(){

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch(msg.what){
		case 0:
			mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
			Intent intent = new Intent(PiaoGuanJiaActivity.this,Login.class);
			startActivity(intent);
			finish();
			break;
		}
		super.handleMessage(msg);
	}
	   
   };
   
  @Override
protected void onResume() {
	// TODO Auto-generated method stub
	  mHandler.postDelayed(mDisableHomeKeyRunnable, 200);
	super.onResume();
}
private void createTable(){
	  database = dbHelper.getWritableDatabase();
		 boolean boo=tabIsExist(DbContent.HISTORY_TICKET,database);
		 database.close();
		 TicketBean ticketBean = new TicketBean();
			if(boo==false){
				database = dbHelper.getWritableDatabase();
				database.execSQL(ticketBean.sql);
				database.close();
			}/*else{
				database = dbHelper.getWritableDatabase();
				database.execSQL("drop table "+DbContent.HISTORY_TICKET);
				database.execSQL(ticketBean.sql);
				database.close();
		 }*/
  }
   public boolean tabIsExist(String tabName, SQLiteDatabase db){
       boolean result = false;
       if(tabName == null){
               return false;
       }
       Cursor cursor = null;
       try {
               String sql = "select count(*) as c from sqlite_master where type ='table' and name ='"+tabName.trim()+"' ";
               cursor = db.rawQuery(sql, null);
               if(cursor.moveToNext()){
                       int count = cursor.getInt(0);
                       if(count>0){
                               result = true;
                       }
               }
               cursor.close();
               db.close();
               
       } catch (Exception e) {
               // TODO: handle exception
       }                
       return result;
}
   @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:return true;
		case KeyEvent.KEYCODE_MENU:return true;
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
   public void disableHomeKey()
   {
   this.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
   }

   Runnable mDisableHomeKeyRunnable = new Runnable() {

	   public void run() {
	   disableHomeKey();

	   }
	   };

}