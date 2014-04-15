package com.core.webrtclib.component;

public interface UIObserver {
	public static final int ON_PEER_CONNECTED = 100021;
	public static final int ON_PEER_DISCONNECTED = ON_PEER_CONNECTED + 1;
	public static final int ON_SINGED_IN = ON_PEER_DISCONNECTED + 1;
	public static final int ON_DISCONNECT = ON_SINGED_IN + 1;
	
	public static final int ON_ADD_STREAM = ON_DISCONNECT + 1;
	public static final int ON_REMOVE_STREAM = ON_ADD_STREAM + 1;
	
	public static final int ON_FLAG_CREATE = ON_REMOVE_STREAM + 1;
	public static final int ON_FLAG_CHECKING = ON_FLAG_CREATE + 1;
	public static final int ON_FLAG_CONNECTED = ON_FLAG_CHECKING + 1;
	public static final int ON_FLAG_DISCONNECTED = ON_FLAG_CONNECTED + 1;
	public static final int ON_FLAG_CLOSED = ON_FLAG_DISCONNECTED + 1;
	
	public void onMessage(int msg_id,Object data);
}
