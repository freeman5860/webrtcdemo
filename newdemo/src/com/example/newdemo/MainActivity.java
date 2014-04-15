//package com.example.newdemo;
//
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.webrtc.AudioTrack;
//import org.webrtc.DataChannel;
//import org.webrtc.IceCandidate;
//import org.webrtc.MediaConstraints;
//import org.webrtc.MediaStream;
//import org.webrtc.PeerConnection;
//import org.webrtc.PeerConnectionFactory;
//import org.webrtc.SdpObserver;
//import org.webrtc.SessionDescription;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Point;
//import android.media.AudioManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.WindowManager;
//
//import com.example.newdemo.PeerConnectionClient1.PeerConnectionClientObserver;
//
//public class MainActivity extends Activity implements MainWndCallback{
//	private static final String TAG = "MainActivity";
//	
//	private final String kAudioLabel = "audio_label";
//	private final String kVideoLabel = "video_label";
//	private final String kStreamLabel = "stream_label";
//	private final int kDefaultServerPort = 8888;
//	private final String kServerAddress = "10.10.62.85";
//	//private final String kServerAddress = "10.10.62.145";
//	
//	private static final String kSdpMLineIndex = "sdpMLineIndex";
//    private static final String kSdpMid = "sdpMid";
//    private static final String kcandidate = "candidate";
//    private static final String kType = "type";
//    private static final String kOffer = "offer";
//	
//	private PeerConnectionFactory peer_connection_factory_;
//	private PeerConnection peer_connection_;
//	private PeerConnectionClient1 client_;
//	private int peer_id_;
//	private String server_;
//	private Map<String,MediaStream> active_streams_;
//	private final PCObserver pcObserver = new PCObserver();
//	private final HttpObserver httpObserver = new HttpObserver();
//	private final SObserver sdpObserver = new SObserver();
//	private MediaConstraints sdpMediaConstraints;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//	    Point displaySize = new Point();
//	    getWindowManager().getDefaultDisplay().getSize(displaySize);
//
//	    abortUnless(PeerConnectionFactory.initializeAndroidGlobals(this),
//	        "Failed to initializeAndroidGlobals");
//
//	    AudioManager audioManager =
//	        ((AudioManager) getSystemService(AUDIO_SERVICE));
//	    @SuppressWarnings("deprecation")
//	    boolean isWiredHeadsetOn = audioManager.isWiredHeadsetOn();
//	    audioManager.setMode(isWiredHeadsetOn ?
//	        AudioManager.MODE_IN_CALL : AudioManager.MODE_IN_COMMUNICATION);
//	    audioManager.setSpeakerphoneOn(!isWiredHeadsetOn);
//		
//		setContentView(R.layout.activity_main);
//		
//		init();
//		
//		findViewById(R.id.btn).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//StartLogin(kServerAddress, kDefaultServerPort);
//				startActivity(new Intent(MainActivity.this,TestActivity.class));
//			}
//		});
//		
//		findViewById(R.id.call).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				//ConnectToPeer(1);
//			}
//		});
//	}
//	
//	@Override
//	protected void onDestroy() {
//		disconnectAndExit();
//		super.onDestroy();
//	}
//	
//	private void disconnectAndExit(){
//		if (peer_connection_ != null) {
//			peer_connection_.dispose();
//			peer_connection_ = null;
//	      }
//	      if (client_ != null) {
//	    	  client_.signOut();
//	    	  client_ = null;
//	      }
//	      if (peer_connection_factory_ != null) {
//	    	  peer_connection_factory_.dispose();
//	    	  peer_connection_factory_ = null;
//	      }
//	}
//
//	private void init(){
//		sdpMediaConstraints = new MediaConstraints();
//	    sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
//	        "OfferToReceiveAudio", "true"));
//	    sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
//	        "OfferToReceiveVideo", "false"));
//		
//		client_ = new PeerConnectionClient1();
//		peer_id_ = -1;
//		active_streams_ = new HashMap<String, MediaStream>();
//		client_.registerObserver(httpObserver);
//	}
//	
//	// Poor-man's assert(): die with |msg| unless |condition| is true.
//	private static void abortUnless(boolean condition, String msg) {
//		if (!condition) {
//			throw new RuntimeException(msg);
//		}
//	}
//
//	private boolean InitializePeerConnection(){
//		assert(peer_connection_ == null);
//		assert(peer_connection_factory_ == null);
//		
//		peer_connection_factory_ = new PeerConnectionFactory();
//		if(peer_connection_factory_ == null){
//			Log.e(TAG,"Failed to initialize PeerConnectionFactory");
//			DeletePeerConnection();
//			return false;
//		}
//		
//		MediaConstraints pcCon = new MediaConstraints();
//		pcCon.mandatory.add(new MediaConstraints.KeyValuePair(
//	        "", ""));
//		pcCon.mandatory.add(new MediaConstraints.KeyValuePair(
//	        "", ""));
//		List<PeerConnection.IceServer> iceServers = new LinkedList<PeerConnection.IceServer>();
//		iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302", "", ""));
//		iceServers.add(new PeerConnection.IceServer("turn:23.251.136.90:3478?transport=udp","1397189064:02945980","s4r4zdeH40pO942jwB4+fjhKs6M="));
//		
//		peer_connection_ = peer_connection_factory_.createPeerConnection(iceServers, pcCon, pcObserver);
//		
//		if( peer_connection_ == null){
//			Log.e(TAG,"CreatePeerConnection Failed!");
//			DeletePeerConnection();
//		}
//	
//		AddStreams();
//		return peer_connection_ != null;
//	}
//	
//	private void DeletePeerConnection() {
//		  peer_connection_ = null;
//		  peer_connection_factory_ = null;
//		  active_streams_.clear();
//		  peer_id_ = -1;
//	}
//	
//	private void AddStreams(){
//		Log.i(TAG,"AddStreams");
//		if(active_streams_.containsKey(kStreamLabel)){
//			return;
//		}
//		
//		AudioTrack audio_track = peer_connection_factory_.createAudioTrack(kAudioLabel);
//		
//		
//		//TODO 视频
//		
//		MediaStream stream = peer_connection_factory_.createLocalMediaStream(kStreamLabel);
//		stream.addTrack(audio_track);
//		if(!peer_connection_.addStream(stream, new MediaConstraints())){
//			Log.e(TAG, "Adding stream to PeerConnection failed");
//		}
//		
//		active_streams_.put(stream.label(), stream);
//		
//	}
//	
//	/**
//	 * PeerConnection 的回调
//	 **/
//	private class PCObserver implements PeerConnection.Observer{
//		@Override 
//	    public void onIceCandidate(final IceCandidate candidate){
//	      Log.i(TAG, "onIceCandidate");
//	      runOnUiThread(new Runnable() {
//	          public void run() {
//	            JSONObject json = new JSONObject();
//	            jsonPut(json, kSdpMLineIndex, candidate.sdpMLineIndex);
//	            jsonPut(json, kSdpMid, candidate.sdpMid);
//	            jsonPut(json, kcandidate, candidate.sdp);
//	            sendMessage(json);
//	          }
//	        });
//	    }
//
//	    @Override public void onError(){
//	    	Log.i(TAG, "onError");
//	    	runOnUiThread(new Runnable() {
//	            public void run() {
//	              throw new RuntimeException("PeerConnection error!");
//	            }
//	          });
//	    }
//
//	    @Override public void onSignalingChange(
//	        PeerConnection.SignalingState newState) {
//	    	Log.i(TAG, "onSignalingChange");
//	    }
//
//	    @Override public void onIceConnectionChange(
//	        PeerConnection.IceConnectionState newState) {
//	    	Log.i(TAG, "onIceConnectionChange");
//	    }
//
//	    @Override public void onIceGatheringChange(
//	        PeerConnection.IceGatheringState newState) {
//	    	Log.i(TAG, "onIceGatheringChange");
//	    }
//
//	    @Override public void onAddStream(final MediaStream stream){
//	    	Log.i(TAG, "onAddStream");
//	    	runOnUiThread(new Runnable() {
//	            public void run() {
//	              abortUnless(stream.audioTracks.size() <= 1 &&
//	                  stream.videoTracks.size() <= 1,
//	                  "Weird-looking stream: " + stream);
//	            }
//	          });
//	    }
//
//	    @Override public void onRemoveStream(final MediaStream stream){
//	    	Log.i(TAG, "onRemoveStream");
//	    	runOnUiThread(new Runnable() {
//	            public void run() {
//	              stream.videoTracks.get(0).dispose();
//	            }
//	          });
//	    }
//
//	    @Override public void onDataChannel(final DataChannel dc) {
//	    	Log.i(TAG, "onDataChannel");
//	    	runOnUiThread(new Runnable() {
//	            public void run() {
//	              throw new RuntimeException(
//	                  "AppRTC doesn't use data channels, but got: " + dc.label() +
//	                  " anyway!");
//	            }
//	          });
//	    }
//	}
//	
//	/**
//	 * PeerConnectionClient的回调
//	 */
//	private class HttpObserver implements PeerConnectionClientObserver {
//		
//		@Override
//		public void onSignedIn() {
//			/*
//			Log.d(TAG, "onSignedIn()");
//			ContentValues values = new ContentValues();
//			values.put(Contacts.Peer_Id, 0);
//			values.put(Contacts.Onlined_Status, 0);
//			int rows = DBHelper.getSqLiteDBInstance().update(Contacts.TABLE_NAME, values,null,null);
//			*/
//			Log.d(TAG, "onSignedIn() init");
//		}
//		
//		@Override
//		public void onPeerDisconnected(int peer_id) {
//			Log.d(TAG, "onPeerDisconnected() peer_id:"+peer_id);
//			/*
//			ContentValues values = new ContentValues();
//			values.put(Contacts.Peer_Id, 0);
//			values.put(Contacts.Onlined_Status, 0);
//			int rows = DBHelper.getSqLiteDBInstance().update(Contacts.TABLE_NAME, values, Contacts.Peer_Id + "=?", new String[]{peer_id+""});
//			Log.d(TAG, "onPeerDisconnected() peer_id:"+peer_id+",rows:"+rows);
//			if (mCondutorObserver!=null)
//				mCondutorObserver.OnUserOffline(peer_id);*/
//		}
//		
//		@Override
//		public void onPeerConnected(int peer_id, String account) {
//			Log.d(TAG, "onPeerConnected() account:"+account + ",peer_id:"+peer_id);
//			/*Contacts contacts = Contacts.query(new String[]{Contacts._ID,Contacts.Peer_Id}, Contacts.Account +"=?", new String[]{account});
//			if (contacts != null && contacts.peer_id < peer_id){//小于peer_id说明帐户重新登录了
//				contacts.peer_id = peer_id;
//				contacts.onlined_status = 1;
//				contacts.update(new String[]{Contacts.Peer_Id,Contacts.Onlined_Status});
//			}
//			if (mCondutorObserver!=null && contacts != null && contacts.peer_id == peer_id)
//				mCondutorObserver.OnUserOnline(peer_id, account);*/
//		}
//		
//		@Override
//		public void onMessageSent(int err) {
//			Log.d(TAG,"onMessageSent");
//		}
//		
//		@Override
//		public void onMessageFromPeer(int peer_id, String message) {
//			//condutor_MessageFromPeer(peer_id,message);
//			Log.d(TAG,"onMessageFromPeer" + message);
//			
//			try {
//		        JSONObject json = new JSONObject(message);
//		        if(!json.has(kType)){
//		        	// 有type
//		        	if(!json.has(kSdpMid) || !json.has(kSdpMLineIndex) ||
//		        			!json.has(kcandidate)){
//		        		Log.e(TAG, "Can't parse received message." );
//		        		return;
//		        	}
//		        	IceCandidate candidate = new IceCandidate(
//				              (String) json.get(kSdpMid),
//				              json.getInt(kSdpMLineIndex),
//				              (String) json.get(kcandidate));
//		        	peer_connection_.addIceCandidate(candidate);
//		        	return;
//		        }else{
//		        	String type = (String) json.get("type");
//		        	if(!json.has("sdp")){
//		        		Log.e(TAG,"Can't parse received session description message.");
//		        		return;
//		        	}
//		        	
//		        	SessionDescription sdp = new SessionDescription(
//				              SessionDescription.Type.fromCanonicalForm(type),
//				              preferISAC((String) json.get("sdp")));
//		        	
//		        	peer_connection_.setRemoteDescription(sdpObserver, sdp);
//		        	
//		        	if(type == kOffer){
//		        		peer_connection_.createAnswer(sdpObserver, sdpMediaConstraints);
//		        	}
//		        }
//		      } catch (JSONException e) {
//		        throw new RuntimeException(e);
//		      }
//		}
//		
//		@Override
//		public void onDisconnected() {
//			Log.d(TAG,"onDisconnected");
//		}
//	};
//	
//	private class SObserver implements SdpObserver{
//
//		@Override
//		public void onCreateFailure(final String error) {
//			Log.i(TAG, "onCreateFailure");
//			runOnUiThread(new Runnable() {
//		          public void run() {
//		            throw new RuntimeException("createSDP error: " + error);
//		          }
//		        });
//		}
//
//		@Override
//		public void onCreateSuccess(final SessionDescription origSdp) {
//			runOnUiThread(new Runnable() {
//		          public void run() {
//		        	  Log.i(TAG, "onCreateSuccess" + origSdp.type);
//		        	  SessionDescription sdp = new SessionDescription(
//		        			  origSdp.type, preferISAC(origSdp.description));
//		        	  JSONObject json = new JSONObject();
//		              jsonPut(json, "type", sdp.type.canonicalForm());
//		              jsonPut(json, "sdp", sdp.description);
//		              sendMessage(json);
//		        	  peer_connection_.setLocalDescription(sdpObserver, sdp);
//		          }
//		        });
//		}
//
//		@Override
//		public void onSetFailure(final String error) {
//			Log.i(TAG, "onSetFailure");
//			runOnUiThread(new Runnable() {
//		          public void run() {
//		            throw new RuntimeException("setSDP error: " + error);
//		          }
//		        });
//		}
//
//		@Override
//		public void onSetSuccess() {
//			Log.i(TAG, "onSetSuccess");
//			runOnUiThread(new Runnable() {
//		          public void run() {
//		            if (client_.is_connected()) {
//		              if (peer_connection_.getRemoteDescription() != null) {
//		                // We've set our local offer and received & set the remote
//		                // answer, so drain candidates.
//		            	  Log.e("hjy", "drainRemoteCandidates");
//		            	  //TODO drainRemoteCandidates();
//		              }
//		            } else {
//		              if (peer_connection_.getLocalDescription() == null) {
//		                // We just set the remote offer, time to create our answer.
//		                peer_connection_.createAnswer(sdpObserver, sdpMediaConstraints);
//		              } else {
//		                // Sent our answer and set it as local description; drain
//		                // candidates.
//		            	  Log.e("hjy", "drainRemoteCandidates 2");
//		            	  //TODO drainRemoteCandidates();
//		              }
//		            }
//		          }
//		        });
//		}
//		
//	};
//
//	@Override
//	public void StartLogin(String server, int port) {
//		if (client_.is_connected())
//		    return;
//		server_ = server;
//		client_.connect(server, port, Defaults.GetPeerName());
//	}
//
//	@Override
//	public void DisconnectFromServer() {
//		if (client_.is_connected())
//			client_.signOut();
//	}
//
//	@Override
//	public void ConnectToPeer(int peer_id) {
//		assert(peer_id_ == -1);
//		assert(peer_id != -1);
//
//		if (peer_connection_ != null) {
//		    Log.e(TAG,"Error We only support connecting to one peer at a time");
//		    return;
//		}
//
//		if (InitializePeerConnection()) {
//		    peer_id_ = peer_id;
//		    peer_connection_.createOffer(sdpObserver, sdpMediaConstraints);
//		} else {
//		   Log.e(TAG,"Error Failed to initialize PeerConnection");
//		}
//	}
//
//	@Override
//	public void DisconnectFromCurrentPeer() {
//		Log.i(TAG,"DisconnectFromCurrentPeer");
//		if (peer_connection_ != null) {
//		    client_.sendHangUp(peer_id_);
//		    DeletePeerConnection();
//		}
//	}
//
//	@Override
//	public void UIThreadCallback(int msg_id, Object data) {
//		// TODO Auto-generated method stub
//		
//	}
//	
//	// Mangle SDP to prefer ISAC/16000 over any other audio codec.
//	  private String preferISAC(String sdpDescription) {
//	    String[] lines = sdpDescription.split("\n");
//	    int mLineIndex = -1;
//	    String isac16kRtpMap = null;
//	    Pattern isac16kPattern =
//	        Pattern.compile("^a=rtpmap:(\\d+) ISAC/16000[\r]?$");
//	    for (int i = 0;
//	         (i < lines.length) && (mLineIndex == -1 || isac16kRtpMap == null);
//	         ++i) {
//	      if (lines[i].startsWith("m=audio ")) {
//	        mLineIndex = i;
//	        continue;
//	      }
//	      Matcher isac16kMatcher = isac16kPattern.matcher(lines[i]);
//	      if (isac16kMatcher.matches()) {
//	        isac16kRtpMap = isac16kMatcher.group(1);
//	        continue;
//	      }
//	    }
//	    if (mLineIndex == -1) {
//	      Log.d(TAG, "No m=audio line, so can't prefer iSAC");
//	      return sdpDescription;
//	    }
//	    if (isac16kRtpMap == null) {
//	      Log.d(TAG, "No ISAC/16000 line, so can't prefer iSAC");
//	      return sdpDescription;
//	    }
//	    String[] origMLineParts = lines[mLineIndex].split(" ");
//	    StringBuilder newMLine = new StringBuilder();
//	    int origPartIndex = 0;
//	    // Format is: m=<media> <port> <proto> <fmt> ...
//	    newMLine.append(origMLineParts[origPartIndex++]).append(" ");
//	    newMLine.append(origMLineParts[origPartIndex++]).append(" ");
//	    newMLine.append(origMLineParts[origPartIndex++]).append(" ");
//	    newMLine.append(isac16kRtpMap).append(" ");
//	    for (; origPartIndex < origMLineParts.length; ++origPartIndex) {
//	      if (!origMLineParts[origPartIndex].equals(isac16kRtpMap)) {
//	        newMLine.append(origMLineParts[origPartIndex]).append(" ");
//	      }
//	    }
//	    lines[mLineIndex] = newMLine.toString();
//	    StringBuilder newSdpDescription = new StringBuilder();
//	    for (String line : lines) {
//	      newSdpDescription.append(line).append("\n");
//	    }
//	    return newSdpDescription.toString();
//	  }
//	  
//	// Put a |key|->|value| mapping in |json|.
//	  private static void jsonPut(JSONObject json, String key, Object value) {
//	    try {
//	      json.put(key, value);
//	    } catch (JSONException e) {
//	      throw new RuntimeException(e);
//	    }
//	  }
//	  
//	// Send |json| to the underlying AppEngine Channel.
//	  private void sendMessage(JSONObject json) {
//		  client_.sendToPeer(peer_id_, json.toString());
//	  }
//}
