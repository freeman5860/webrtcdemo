package com.example.newdemo;

import java.util.HashMap;

import org.webrtc.PeerConnectionFactory;

import android.app.Activity;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.core.webrtclib.component.Condutor;
import com.core.webrtclib.component.PeerAccount;
import com.core.webrtclib.component.UIObserver;
import com.core.webrtclib.component.VideoStreamsView;


public class TestActivity extends Activity {

	private Condutor mCondutor;
	private ListView mList;
	private PeerAdapter mAdapter;

	private VideoStreamsView vsv;

	private final boolean isVideoOn = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_test);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		Point displaySize = new Point();
		getWindowManager().getDefaultDisplay().getSize(displaySize);
		vsv = new VideoStreamsView(this, displaySize);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		addContentView(vsv,params);
		vsv.setVisibility(View.GONE);

		abortUnless(PeerConnectionFactory.initializeAndroidGlobals(this),
				"Failed to initializeAndroidGlobals");

		AudioManager audioManager = ((AudioManager) getSystemService(AUDIO_SERVICE));
		// TODO(fischman): figure out how to do this Right(tm) and remove the
		// suppression.
		@SuppressWarnings("deprecation")
		//boolean isWiredHeadsetOn = audioManager.isWiredHeadsetOn();
		boolean isWiredHeadsetOn = true;
		audioManager.setMode(isWiredHeadsetOn ? AudioManager.MODE_IN_CALL
				: AudioManager.MODE_IN_COMMUNICATION);
		audioManager.setSpeakerphoneOn(!isWiredHeadsetOn);
		setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);

		mCondutor = new Condutor(this, isVideoOn);
		if (isVideoOn) {
			mCondutor.setVideoView(vsv);
		}
		mCondutor.setUIObserver(obs);
		mCondutor.StartLogin(mCondutor.kServerAddress,
				mCondutor.kDefaultServerPort);

		mList = (ListView) findViewById(R.id.peer_list);
		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				new Thread() {
					@Override
					public void run() {
						Integer peedId = (Integer) mAdapter.getItem(arg2);
						mCondutor.ConnectToPeer((int) peedId);
					}
				}.start();

			}
		});
		mAdapter = new PeerAdapter(this, new HashMap<String, Integer>());
		mList.setAdapter(mAdapter);
	}

	@Override
	protected void onPause() {
		vsv.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		vsv.onResume();
	}

	@Override
	protected void onDestroy() {
		mCondutor.Close();

		super.onDestroy();
	}

	private UIObserver obs = new UIObserver() {

		@Override
		public void onMessage(int msg_id, Object data) {
			switch (msg_id) {
			case UIObserver.ON_PEER_CONNECTED:
				mAdapter.addPeer((PeerAccount) data);
				break;
			case UIObserver.ON_PEER_DISCONNECTED:
				mAdapter.removePeer((Integer) data);
				break;
			case UIObserver.ON_ADD_STREAM:
				vsv.setVisibility(View.VISIBLE);
				break;
			case UIObserver.ON_FLAG_DISCONNECTED:
				vsv.setVisibility(View.GONE);
				mCondutor.Close();
				// 只维护一次通话，对方挂断就结束了
				finish();
				break;
			}
		}
	};

	// Poor-man's assert(): die with |msg| unless |condition| is true.
	private static void abortUnless(boolean condition, String msg) {
		if (!condition) {
			throw new RuntimeException(msg);
		}
	}
}
