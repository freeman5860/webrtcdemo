package com.example.newdemo;

public interface UIObserver {
	public static final int ON_PEER_CONNECTED = 100021;
	public static final int ON_PEER_DISCONNECTED = ON_PEER_CONNECTED + 1;
	public static final int ON_SINGED_IN = ON_PEER_DISCONNECTED + 1;
	public static final int ON_DISCONNECT = ON_SINGED_IN + 1;
	
	public static final int ON_ADD_STREAM = ON_DISCONNECT + 1;
	public static final int ON_REMOVE_STREAM = ON_ADD_STREAM + 1;
	public static final int ON_SET_VIDEO_SOURCE = ON_REMOVE_STREAM + 1;
	
	public void onMessage(int msg_id,Object data);
}
