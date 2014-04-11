package com.example.newdemo;

import java.util.HashMap;

import org.webrtc.PeerConnectionFactory;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TestActivity extends Activity {
	
	private Condutor mCondutor;
	private ListView mList;
	private PeerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PeerConnectionFactory.initializeAndroidGlobals(this);
		
		setContentView(R.layout.activity_test);
		
		mCondutor = new Condutor(this);
		mCondutor.setUIObserver(obs);
		mCondutor.StartLogin(mCondutor.kServerAddress, mCondutor.kDefaultServerPort);
		
		mList = (ListView) findViewById(R.id.peer_list);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				new Thread(){
					@Override
					public void run() {
						Integer peedId = (Integer)mAdapter.getItem(arg2);
						Log.e("hjy", ""+peedId);
						mCondutor.ConnectToPeer((int)peedId);
					}
				}.start();
				
			}
		});
		mAdapter = new PeerAdapter(this, new HashMap<String, Integer>());
		mList.setAdapter(mAdapter);
	}
	
	@Override
	protected void onDestroy() {
		mCondutor.Close();
		
		super.onDestroy();
	}
	
	private UIObserver obs = new UIObserver() {
		
		@Override
		public void onMessage(int msg_id, Object data) {
			switch(msg_id){
			case UIObserver.ON_PEER_CONNECTED:
				mAdapter.addPeer((PeerAccount)data);
				break;
			case UIObserver.ON_PEER_DISCONNECTED:
				mAdapter.removePeer((Integer)data);
				break;
			}
		}
	};
}
