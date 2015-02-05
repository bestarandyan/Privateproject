package com.vnc.draw.content;

import com.vnc.draw.activity.MyApplication;
import com.vnc.draw.activity.SendToServiceToDraw;

public class SetNewSocket {
	public static void getNewSocket(){
		if(MyApplication.getInstance().getSocket().isOutputShutdown() || MyApplication.getInstance().getSocket().isClosed()
				|| !MyApplication.getInstance().getSocket().isConnected()){
			MyApplication.getInstance().setSendToService(new SendToServiceToDraw() );
		}
	}

}
