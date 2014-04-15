package com.core.webrtclib.component;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.core.webrtclib.service.WebRTCService;

public class NetworkChangeReceiver {
	public static boolean hasNetwork() {
		if(WebRTCService.getContext() == null){
			return false;
		}
		
		ConnectivityManager cm = (ConnectivityManager) WebRTCService.getContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) { 
			
		} else {
			//如果仅仅是用来判断网络连接　　　　　　 
			//则可以使用 cm.getActiveNetworkInfo().isAvailable(); 
			NetworkInfo[] info = cm.getAllNetworkInfo(); 
			if (info != null) { 
				for (int i = 0; i < info.length; i++) {
				     if (info[i].getState() == NetworkInfo.State.CONNECTED){ 
				    	 return true;
				     } 
				}
			} 
		} 
		return false;
	}
}
