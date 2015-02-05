package guoTeng.LoadIC2ReadCard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class HippoStartupIntentReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 Intent mBootIntent = new Intent(context, PiaoGuanJiaActivity.class); 
	     mBootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	     context.startActivity(mBootIntent); 
	}
	
}
