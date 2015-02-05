package guoTeng.LoadIC2ReadCard;

import com.google.zxing.client.android.R;

import android.app.Activity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.LayoutAnimationController.AnimationParameters;

public class LockLayer {
	private Activity mActivty;  
    private WindowManager mWindowManager;  
    private View mLockView;  
    private LayoutParams mLockViewLayoutParams;  
      
    public LockLayer(Activity act) {  
        mActivty = act;  
        init();  
    }  
  
    private void init(){  
        mWindowManager = mActivty.getWindowManager();  
        mLockViewLayoutParams = new LayoutParams();  
        mLockViewLayoutParams.width = LayoutParams.MATCH_PARENT;  
        mLockViewLayoutParams.height = LayoutParams.MATCH_PARENT;  
        //实现关键  
        mLockViewLayoutParams.type = LayoutParams.TYPE_SYSTEM_ERROR;  
        //apktool value，这个值具体是哪个变量还请网友帮忙  
        mLockViewLayoutParams.flags = 1280;  
    }  
    public void lock() {  
        if(mLockView!=null){  
            mWindowManager.addView(mLockView, mLockViewLayoutParams);  
        }  
    }  
    public void unlock() {  
        if(mWindowManager!=null){  
            mWindowManager.removeView(mLockView);  
        }  
    }  
    public void setLockView(View v){  
        mLockView = v;  
    }  

}
