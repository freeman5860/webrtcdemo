package com.example.newdemo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioTrack;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import android.app.Activity;
import android.util.Log;

import com.example.newdemo.PeerConnectionClient1.PeerConnectionClientObserver;



public class Condutor implements MainWndCallback{
	private static final String TAG = "Condutor";
	
	private Activity activity;
	
	public final String kAudioLabel = "audio_label";
	public final String kVideoLabel = "video_label";
	public final String kStreamLabel = "stream_label";
	public final int kDefaultServerPort = 8888;
	public final String kServerAddress = "10.10.62.85";
	
	public static final String kSdpMLineIndex = "sdpMLineIndex";
	public static final String kSdpMid = "sdpMid";
	public static final String kcandidate = "candidate";
	public static final String kType = "type";
	public static final String kOffer = "offer";
	
	private PeerConnectionFactory peer_connection_factory_;
	private PeerConnection peer_connection_;
	private PeerConnectionClient1 client_;
	private int peer_id_;
	private Map<String,MediaStream> active_streams_;
	private final PCObserver pcObserver = new PCObserver();
	private final HttpObserver httpObserver = new HttpObserver();
	private final SObserver sdpObserver = new SObserver();
	private MediaConstraints sdpMediaConstraints;
	private UIObserver uiCallBack = null;
	
	public Condutor(Activity act){
		activity = act;
		init();
	}
	
	public void setUIObserver(UIObserver callback){
		uiCallBack = callback;
	}
	
	public void Close(){
		client_.signOut();
		DeletePeerConnection();
	}
	
	public HashMap<String,Integer> getPeers(){
		return client_.peers_;
	}
	
	private void init(){
		sdpMediaConstraints = new MediaConstraints();
	    sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
	        "OfferToReceiveAudio", "true"));
	    sdpMediaConstraints.mandatory.add(new MediaConstraints.KeyValuePair(
	        "OfferToReceiveVideo", "false"));
		
		client_ = new PeerConnectionClient1();
		peer_id_ = -1;
		active_streams_ = new HashMap<String, MediaStream>();
		client_.registerObserver(httpObserver);
	}
	
	private boolean InitializePeerConnection(){
		assert(peer_connection_ == null);
		assert(peer_connection_factory_ == null);
		
		peer_connection_factory_ = new PeerConnectionFactory();
		if(peer_connection_factory_ == null){
			Log.e(TAG,"Failed to initialize PeerConnectionFactory");
			DeletePeerConnection();
			return false;
		}
		
		MediaConstraints pcCon = new MediaConstraints();
		pcCon.mandatory.add(new MediaConstraints.KeyValuePair(
	        "", ""));
		pcCon.mandatory.add(new MediaConstraints.KeyValuePair(
	        "", ""));
		List<PeerConnection.IceServer> iceServers = new LinkedList<PeerConnection.IceServer>();
		iceServers.add(new PeerConnection.IceServer("stun:stun.l.google.com:19302", "", ""));
		iceServers.add(new PeerConnection.IceServer("turn:23.251.136.90:3478?transport=udp","1397189064:02945980","s4r4zdeH40pO942jwB4+fjhKs6M="));
		
		peer_connection_ = peer_connection_factory_.createPeerConnection(iceServers, pcCon, pcObserver);
		
		if( peer_connection_ == null){
			Log.e(TAG,"CreatePeerConnection Failed!");
			DeletePeerConnection();
		}
	
		AddStreams();
		return peer_connection_ != null;
	}
	
	private void DeletePeerConnection() {
		if(peer_connection_ != null){
			peer_connection_.dispose();
			peer_connection_ = null;
		}
		  
		if(peer_connection_factory_ != null){
			peer_connection_factory_.dispose();
			peer_connection_factory_ = null;
		}
		  
		active_streams_.clear();
		peer_id_ = -1;
	}
	
	private void AddStreams(){
		Log.i(TAG,"AddStreams");
		if(active_streams_.containsKey(kStreamLabel)){
			return;
		}
		
		AudioTrack audio_track = peer_connection_factory_.createAudioTrack(kAudioLabel);
		
		//TODO 视频
		
		MediaStream stream = peer_connection_factory_.createLocalMediaStream(kStreamLabel);
		stream.addTrack(audio_track);
		if(!peer_connection_.addStream(stream, new MediaConstraints())){
			Log.e(TAG, "Adding stream to PeerConnection failed");
		}
		
		active_streams_.put(stream.label(), stream);
		
	}
	
	/**
	 * PeerConnection 的回调
	 **/
	private class PCObserver implements PeerConnection.Observer{
		@Override 
	    public void onIceCandidate(final IceCandidate candidate){
	      Log.i(TAG, "onIceCandidate");
	      activity.runOnUiThread(new Runnable() {
	          public void run() {
	            JSONObject json = new JSONObject();
	            jsonPut(json, kSdpMLineIndex, candidate.sdpMLineIndex);
	            jsonPut(json, kSdpMid, candidate.sdpMid);
	            jsonPut(json, kcandidate, candidate.sdp);
	            sendMessage(json);
	          }
	        });
	    }

	    @Override public void onError(){
	    	Log.i(TAG, "onError");
	    	activity.runOnUiThread(new Runnable() {
	            public void run() {
	              throw new RuntimeException("PeerConnection error!");
	            }
	          });
	    }

	    @Override public void onSignalingChange(
	        PeerConnection.SignalingState newState) {
	    	Log.i(TAG, "onSignalingChange");
	    }

	    @Override public void onIceConnectionChange(
	        PeerConnection.IceConnectionState newState) {
	    	Log.i(TAG, "onIceConnectionChange");
	    }

	    @Override public void onIceGatheringChange(
	        PeerConnection.IceGatheringState newState) {
	    	Log.i(TAG, "onIceGatheringChange");
	    }

	    @Override public void onAddStream(final MediaStream stream){
	    	Log.i(TAG, "onAddStream");
	    	activity.runOnUiThread(new Runnable() {
	            public void run() {
	              assert(stream.audioTracks.size() <= 1 &&
	                  stream.videoTracks.size() <= 1);
	            }
	        });
	    }

	    @Override public void onRemoveStream(final MediaStream stream){
	    	Log.i(TAG, "onRemoveStream");
	    	activity.runOnUiThread(new Runnable() {
	            public void run() {
	              stream.videoTracks.get(0).dispose();
	            }
	          });
	    }

	    @Override public void onDataChannel(final DataChannel dc) {
	    	Log.i(TAG, "onDataChannel");
	    	activity.runOnUiThread(new Runnable() {
	            public void run() {
	              throw new RuntimeException(
	                  "AppRTC doesn't use data channels, but got: " + dc.label() +
	                  " anyway!");
	            }
	          });
	    }
	}
	
	/**
	 * PeerConnectionClient的回调
	 */
	private class HttpObserver implements PeerConnectionClientObserver {
		
		@Override
		public void onSignedIn() {
			Log.d(TAG, "onSignedIn() init");
			// TODO SwitchToPeerList
		}
		
		@Override
		public void onPeerDisconnected(final int peer_id) {
			Log.d(TAG, "onPeerDisconnected() peer_id:"+peer_id);
			
			if(peer_id == peer_id_){
				Log.d(TAG, "Our peer disconnected");
			}else{
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if(uiCallBack != null){
							uiCallBack.onMessage(UIObserver.ON_PEER_DISCONNECTED, peer_id);
						}
					}
				});
			}
		}
		
		@Override
		public void onPeerConnected(final int peer_id, final String account) {
			Log.d(TAG, "onPeerConnected() account:"+account + ",peer_id:"+peer_id);
			
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					if(uiCallBack != null){
						uiCallBack.onMessage(UIObserver.ON_PEER_CONNECTED, new PeerAccount(peer_id,account));
					}
				}
			});
			
		}
		
		@Override
		public void onMessageSent(int err) {
			Log.d(TAG,"onMessageSent");
		}
		
		@Override
		public void onMessageFromPeer(int peer_id, String message) {
			Log.d(TAG,"onMessageFromPeer" + message);
			
			assert(peer_id_ == peer_id || peer_id_ == -1);
			assert(message != null);
			
			if(peer_connection_ == null){
				assert(peer_id_ == -1);
				peer_id_ = peer_id;
				
				if(!InitializePeerConnection()){
					Log.e(TAG,"Failed to initialize our PeerConnection instance");
					client_.signOut();
					return;
				}
			}else if(peer_id != peer_id_){
				assert(peer_id_ != -1);
				Log.e(TAG,"Received a message from unknown peer while already in a conversation with a different peer.");
				return;
			}
			
			try {
		        JSONObject json = new JSONObject(message);
		        if(!json.has(kType)){
		        	// 有type
		        	if(!json.has(kSdpMid) || !json.has(kSdpMLineIndex) ||
		        			!json.has(kcandidate)){
		        		Log.e(TAG, "Can't parse received message." );
		        		return;
		        	}
		        	IceCandidate candidate = new IceCandidate(
				              (String) json.get(kSdpMid),
				              json.getInt(kSdpMLineIndex),
				              (String) json.get(kcandidate));
		        	peer_connection_.addIceCandidate(candidate);
		        	return;
		        }else{
		        	String type = (String) json.get("type");
		        	if(!json.has("sdp")){
		        		Log.e(TAG,"Can't parse received session description message.");
		        		return;
		        	}
		        	
		        	SessionDescription sdp = new SessionDescription(
				              SessionDescription.Type.fromCanonicalForm(type),
				              preferISAC((String) json.get("sdp")));
		        	
		        	peer_connection_.setRemoteDescription(sdpObserver, sdp);
		        	
		        	if(kOffer.equals(type)){
		        		peer_connection_.createAnswer(sdpObserver, sdpMediaConstraints);
		        	}
		        }
		      } catch (JSONException e) {
		        throw new RuntimeException(e);
		      }
		}
		
		@Override
		public void onDisconnected() {
			Log.d(TAG,"onDisconnected");
			
			DeletePeerConnection();
			
			//TODO SwitchToConnectUI
		}
	};
	
	private class SObserver implements SdpObserver{

		@Override
		public void onCreateFailure(final String error) {
			Log.i(TAG, "onCreateFailure");
			activity.runOnUiThread(new Runnable() {
		          public void run() {
		            throw new RuntimeException("createSDP error: " + error);
		          }
		        });
		}

		@Override
		public void onCreateSuccess(final SessionDescription origSdp) {
			activity.runOnUiThread(new Runnable() {
		          public void run() {
		        	  Log.i(TAG, "onCreateSuccess" + origSdp.type);
		        	  SessionDescription sdp = new SessionDescription(
		        			  origSdp.type, preferISAC(origSdp.description));
		        	  JSONObject json = new JSONObject();
		              jsonPut(json, "type", sdp.type.canonicalForm());
		              jsonPut(json, "sdp", sdp.description);
		              sendMessage(json);
		        	  peer_connection_.setLocalDescription(sdpObserver, sdp);
		          }
		        });
		}

		@Override
		public void onSetFailure(final String error) {
			Log.i(TAG, "onSetFailure");
			activity.runOnUiThread(new Runnable() {
		          public void run() {
		            throw new RuntimeException("setSDP error: " + error);
		          }
		        });
		}

		@Override
		public void onSetSuccess() {
			Log.i(TAG, "onSetSuccess");
			activity.runOnUiThread(new Runnable() {
		          public void run() {
		            if (client_.is_connected()) {
		              if (peer_connection_.getRemoteDescription() != null) {
		                // We've set our local offer and received & set the remote
		                // answer, so drain candidates.
		            	  Log.e("hjy", "drainRemoteCandidates");
		            	  //TODO drainRemoteCandidates();
		              }
		            } else {
		              if (peer_connection_.getLocalDescription() == null) {
		                // We just set the remote offer, time to create our answer.
		                peer_connection_.createAnswer(sdpObserver, sdpMediaConstraints);
		              } else {
		                // Sent our answer and set it as local description; drain
		                // candidates.
		            	  Log.e("hjy", "drainRemoteCandidates 2");
		            	  //TODO drainRemoteCandidates();
		              }
		            }
		          }
		        });
		}
		
	};

	@Override
	public void StartLogin(String server, int port) {
		if (client_.is_connected())
		    return;
		client_.connect(server, port, Defaults.GetPeerName());
	}

	@Override
	public void DisconnectFromServer() {
		if (client_.is_connected())
			client_.signOut();
	}

	@Override
	public void ConnectToPeer(int peer_id) {
		assert(peer_id_ == -1);
		assert(peer_id != -1);

		if (peer_connection_ != null) {
		    Log.e(TAG,"Error We only support connecting to one peer at a time");
		    return;
		}

		if (InitializePeerConnection()) {
		    peer_id_ = peer_id;
		    peer_connection_.createOffer(sdpObserver, sdpMediaConstraints);
		} else {
		   Log.e(TAG,"Error Failed to initialize PeerConnection");
		}
	}

	@Override
	public void DisconnectFromCurrentPeer() {
		Log.i(TAG,"DisconnectFromCurrentPeer");
		if (peer_connection_ != null) {
		    client_.sendHangUp(peer_id_);
		    DeletePeerConnection();
		}
	}

	@Override
	public void UIThreadCallback(int msg_id, Object data) {
		
	}
	
	// Mangle SDP to prefer ISAC/16000 over any other audio codec.
	private String preferISAC(String sdpDescription) {
	    String[] lines = sdpDescription.split("\n");
	    int mLineIndex = -1;
	    String isac16kRtpMap = null;
	    Pattern isac16kPattern =
	        Pattern.compile("^a=rtpmap:(\\d+) ISAC/16000[\r]?$");
	    for (int i = 0;
	         (i < lines.length) && (mLineIndex == -1 || isac16kRtpMap == null);
	         ++i) {
	      if (lines[i].startsWith("m=audio ")) {
	        mLineIndex = i;
	        continue;
	      }
	      Matcher isac16kMatcher = isac16kPattern.matcher(lines[i]);
	      if (isac16kMatcher.matches()) {
	        isac16kRtpMap = isac16kMatcher.group(1);
	        continue;
	      }
	    }
	    if (mLineIndex == -1) {
	      Log.d(TAG, "No m=audio line, so can't prefer iSAC");
	      return sdpDescription;
	    }
	    if (isac16kRtpMap == null) {
	      Log.d(TAG, "No ISAC/16000 line, so can't prefer iSAC");
	      return sdpDescription;
	    }
	    String[] origMLineParts = lines[mLineIndex].split(" ");
	    StringBuilder newMLine = new StringBuilder();
	    int origPartIndex = 0;
	    // Format is: m=<media> <port> <proto> <fmt> ...
	    newMLine.append(origMLineParts[origPartIndex++]).append(" ");
	    newMLine.append(origMLineParts[origPartIndex++]).append(" ");
	    newMLine.append(origMLineParts[origPartIndex++]).append(" ");
	    newMLine.append(isac16kRtpMap).append(" ");
	    for (; origPartIndex < origMLineParts.length; ++origPartIndex) {
	      if (!origMLineParts[origPartIndex].equals(isac16kRtpMap)) {
	        newMLine.append(origMLineParts[origPartIndex]).append(" ");
	      }
	    }
	    lines[mLineIndex] = newMLine.toString();
	    StringBuilder newSdpDescription = new StringBuilder();
	    for (String line : lines) {
	      newSdpDescription.append(line).append("\n");
	    }
	    return newSdpDescription.toString();
	  }
	  
	// Put a |key|->|value| mapping in |json|.
	private static void jsonPut(JSONObject json, String key, Object value) {
	    try {
	      json.put(key, value);
	    } catch (JSONException e) {
	      throw new RuntimeException(e);
	    }
	}
	  
	// Send |json| to the underlying AppEngine Channel.
	private void sendMessage(JSONObject json) {
		  client_.sendToPeer(peer_id_, json.toString());
	}
	
}
