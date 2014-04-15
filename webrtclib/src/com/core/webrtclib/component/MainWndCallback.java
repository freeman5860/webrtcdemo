package com.core.webrtclib.component;

public interface MainWndCallback {
	 void StartLogin(String server, int port); 
	 void DisconnectFromServer();
	 void ConnectToPeer(int peer_id);
	 void DisconnectFromCurrentPeer();
	 void UIThreadCallback(int msg_id, Object data);
}
