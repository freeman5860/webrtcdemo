package com.core.webrtclib.component;

import java.util.UUID;

public class Defaults {
	public static String GetPeerName() {
		  String ret = "User";
		  ret += '@';
		  ret += UUID.randomUUID();
		  return ret;
	}
}
