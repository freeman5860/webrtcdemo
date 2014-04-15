package com.core.webrtclib.component;

public class PeerAccount {

	private int mPeerId;
	private String mAccount;
	
	protected PeerAccount(){
		
	}
	
	public PeerAccount(int id,String account){
		mPeerId = id;
		mAccount = account;
	}
	
	public int getPeerId() {
		return mPeerId;
	}
	public void setPeerId(int mPeerId) {
		this.mPeerId = mPeerId;
	}
	public String getAccount() {
		return mAccount;
	}
	public void setAccount(String mAccount) {
		this.mAccount = mAccount;
	}
	
	
}
