package com.chinaLife.claimAssistant.thread;

import com.chinaLife.claimAssistant.sc_MyApplication;
import com.chinaLife.claimAssistant.tools.sc_NetworkCheck;

public class sc_ThreadDemo{

	private static sc_ThreadDemo mInstance = null;
	public static boolean isstart = true;

	public sc_ThreadDemo() {
	}
	public static sc_ThreadDemo getInstance() {
		if (mInstance == null) {
			mInstance = new sc_ThreadDemo();
		}
		return mInstance;
	}

	public static void startMyThread() {
		if (sc_MyApplication.getInstance().getSelfHelpFlag() != 1) {
			if (sc_NetworkCheck.IsHaveInternet(sc_MyApplication.getInstance()
					.getContext()) && isstart&&sc_MyApplication.switch_tag&&!sc_MyApplication.getInstance().isUploadon()) {
				//System.out.println("进到方法去了");
				isstart = false;
				getInstance().post();
			}
		}
	}
	public void post() {
		//System.out.println("正在执行三个静态方法");
		sc_GetClaimState.startMyThread();
//		GetMsg.startMyThread();
		sc_GetMessage.startMyThread();
		isstart = true;
		//System.out.println("三个静态方法执行完毕");
		
	}
}
