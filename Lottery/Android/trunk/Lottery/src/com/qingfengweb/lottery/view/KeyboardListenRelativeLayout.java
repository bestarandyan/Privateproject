package com.qingfengweb.lottery.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class KeyboardListenRelativeLayout extends RelativeLayout {

private static final String TAG = KeyboardListenRelativeLayout.class.getSimpleName();

public static final byte KEYBOARD_STATE_SHOW = 1;
public static final byte KEYBOARD_STATE_HIDE = -1;
public static final byte KEYBOARD_STATE_INIT = 0;

private boolean mHasInit = false;
private boolean mHasKeyboard = false;
private int mHeight;

private IOnKeyboardStateChangedListener onKeyboardStateChangedListener;

public KeyboardListenRelativeLayout(Context context) {
  super(context);
}
public KeyboardListenRelativeLayout(Context context, AttributeSet attrs) {
   super(context, attrs);
}

public KeyboardListenRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
  super(context, attrs, defStyle);
}

public void setOnKeyboardStateChangedListener(IOnKeyboardStateChangedListener onKeyboardStateChangedListener) {
  this.onKeyboardStateChangedListener = onKeyboardStateChangedListener;
}

@Override
protected void onLayout(boolean changed, int l, int t, int r, int b) {
  super.onLayout(changed, l, t, r, b);
  if(!mHasInit) {
   mHasInit = true;
   mHeight = b;
   if(onKeyboardStateChangedListener != null) {
    onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_INIT);
   }
  } else {
   mHeight = mHeight < b ? b : mHeight;
  }
  
  if(mHasInit && mHeight > b) {
   mHasKeyboard = true;
   if(onKeyboardStateChangedListener != null) {
    onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_SHOW);
    System.out.println("键盘显示了");
   }
  }
  if(mHasInit && mHasKeyboard && mHeight == b) {
   mHasKeyboard = false;
   if(onKeyboardStateChangedListener != null) {
    onKeyboardStateChangedListener.onKeyboardStateChanged(KEYBOARD_STATE_HIDE);
    System.out.println("键盘隐藏了");
   }
  }
}

public interface IOnKeyboardStateChangedListener {
  public void onKeyboardStateChanged(int state);
}
}