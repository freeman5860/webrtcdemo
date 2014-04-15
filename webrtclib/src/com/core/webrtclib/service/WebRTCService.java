package com.core.webrtclib.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class WebRTCService extends Service {
	
	private static Context mContext = null;
	
	public static Context getContext(){
		return mContext;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
