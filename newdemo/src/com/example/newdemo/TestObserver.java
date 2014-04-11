package com.example.newdemo;

import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection.IceConnectionState;
import org.webrtc.PeerConnection.IceGatheringState;
import org.webrtc.PeerConnection.Observer;
import org.webrtc.PeerConnection.SignalingState;

public class TestObserver implements Observer {

	@Override
	public void onAddStream(MediaStream arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDataChannel(DataChannel arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIceCandidate(IceCandidate arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIceConnectionChange(IceConnectionState arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIceGatheringChange(IceGatheringState arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRemoveStream(MediaStream arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignalingChange(SignalingState arg0) {
		// TODO Auto-generated method stub
		
	}

}
