package com.example.newdemo;

public interface UIObserver {
	public static final int ON_PEER_CONNECTED = 100021;
	public static final int ON_PEER_DISCONNECTED = ON_PEER_CONNECTED + 1;
	
	
	public void onMessage(int msg_id,Object data);
}
