package com.core.webrtclib.component;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Set;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;


public class PeerConnectionClient1 {
	
	private static final String TAG = "PeerConnectionClient";
	
	public class State {
		public static final int 
			NOT_CONNECTED = 1, 
			SIGNING_IN = 2,
			CONNECTED = 3, 
			SIGNING_OUT_WAITING = 4, 
			SIGNING_OUT = 5;
	}

	public interface PeerConnectionClientObserver {
		void onSignedIn(); // Called when we're logged on.
		void onDisconnected();
		void onPeerConnected(int peer_id, String account);
		void onPeerDisconnected(int peer_id);
		void onMessageFromPeer(int peer_id, String message);
		void onMessageSent(int err);
	}
	
	// 变量声明
	final int kDefaultServerPort = 8888;
	
	PeerConnectionClientObserver callback_;
	SocketAddress server_address_;
	String onconnect_data_;
	String control_data_;
	String notification_data_;
	HashMap<String, Integer> peers_ = new HashMap<String, Integer>();//account,peerid
	int state_=State.NOT_CONNECTED;
	int my_id_=-1;
	boolean mIsSending = false;
	String kByeMessage = "BYE";
	
	public static final int SocketType_Control = 1, SocketType_Hanging_get = 2;
	MyHandler myHandler = new MyHandler();
	DataOutputStream control_socket_outputStream;
	DataInputStream control_socket_inputStream;
	DataOutputStream hanging_get_outputStream;
	DataInputStream hanging_get_inputStream;
	
	String server,client_name;
	int serverPort;
	boolean isReConnect = false;
	String mServerUrl;
	
	public int id(){
		return my_id_;
	}
	public boolean is_connected(){
		return my_id_ != -1;
	}
	public HashMap<String,Integer> peers(){
		return peers_;
	}
	
	 public int getPeerIdByAccount(String account)
     {
        return peers_.get(account);
     }


     public String getAccountByPeerId(int peer_id)
     {
    	 if (peers_.containsValue(peer_id)){
    		 Set<String> keySet = peers_.keySet();
    		 for (String string : keySet) {
				if (peer_id == peers_.get(string)) {
					return string;
				}
			}
    	 }
		return null;
     }

	public void registerObserver(PeerConnectionClientObserver callback){
		callback_ = callback;
	}

	public boolean connect(String server, int port, String client_name) {
		Log.d(TAG, "connect() client_name:"+client_name+",ip:"+server+",port:"+port);
		if (TextUtils.isEmpty(server) || TextUtils.isEmpty(client_name)) {
			return false;
		}

		if (state_ != State.NOT_CONNECTED) {
			Log.w(TAG,"The client must not be connected before you can call Connect()");
			return false;
		}
		
		this.server = server;
		this.client_name = client_name;
		this.serverPort = port;
		if (port <= 0)
			serverPort = kDefaultServerPort;
		mServerUrl = "http://"+server+":"+port;
		
		onconnect_data_ = String.format("GET /sign_in?%1$s HTTP/1.0\r\n\r\n",PeerConnectionClient1.this.client_name);
		Log.d(TAG, "connect() state_= siging_ing");
		state_ =PeerConnectionClient1.State.SIGNING_IN;
		//startConnectServer(SocketType_Control);
		sendMsg(mServerUrl+"/sign_in?"+PeerConnectionClient1.this.client_name);
		return true;
	}
	
	int mSendMsgRetryCount = 0;//发送消息重试
	void sendMsg(final String dataUrl,final String requestMethod,final String msg){
		Log.d(TAG, "sendMsg:"+dataUrl+",msg:"+msg);
		new Thread(){
			public void run() {
				mIsSending = true;
				try {
					URL url = new URL(dataUrl);//获取到路径
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod(requestMethod);
					if (requestMethod.equalsIgnoreCase("Post")){
						conn.getOutputStream().write(msg.getBytes());// 输入数据
					}else {
						conn.setRequestMethod("GET");
					}
					int stateCode = conn.getResponseCode();
			        if (stateCode == 200) {
			        	int peerId = conn.getHeaderFieldInt("Pragma", -1);
			            String data = getStringFromStream(conn.getInputStream());
			            onRead(data, peerId);
			            
			        }else {
						Log.w(TAG, "getStreamFromNet ResponseCode:"+conn.getResponseCode());
						myHandler.sendEmptyMessage(myHandler.onDisconnected);
					}
			        mSendMsgRetryCount = 0;
				} catch (Exception e) {
					Log.e(TAG, "sendMsg() error retryCount:"+mSendMsgRetryCount,e);
					if (mSendMsgRetryCount>=3){
						myHandler.sendEmptyMessage(myHandler.onDisconnected);
					}else {
						Log.w(TAG, "sendMsg() retry!");
						sendMsg(dataUrl, requestMethod, msg);
						mSendMsgRetryCount++;
					}
				}
				mIsSending = false;
			};
		}.start();
	}
	
	void sendMsg(String dataUrl){
		sendMsg(dataUrl, "GET", null);
	}
	
	void onRead(String contentData, int peer_id) {
		Log.d(TAG, "onRead() peer_id:"+peer_id+",state:"+state_+",my_id:"+my_id_+",data:"+contentData);
		if (peer_id != -1) {
			if (my_id_ == -1 || state_ == State.SIGNING_IN) {
				// First response. Let's store our server assigned ID.
				my_id_ = peer_id;
				myHandler.sendEmptyMessage(myHandler.onSignedIn);
				// The body of the response will be a list of already connected
				// peers.
				
				if (!TextUtils.isEmpty(contentData)) {
					String peerList[] = contentData.split("\n");
					int listSize = peerList.length;
					for (int i = 0; i < listSize; i++) {
						Object[] name_id_connected = parseEntry(peerList[i]);
						if (name_id_connected != null) {
							String name = (String) name_id_connected[0];
							if (!TextUtils.isEmpty(name)) {
								Integer id = (Integer) name_id_connected[1];
								// 如果是自身id则忽略
								if(id == my_id_){
									continue;
								}
								Log.d(TAG, "onRead() name:" + name + ",id:" + id);
								peers_.put(name, id);
								myHandler.obtainMessage(myHandler.onPeerConnected, id, 0,name).sendToTarget();
							}
						}
					}
				}
			} else if (state_ == State.SIGNING_OUT) {
				close();
				myHandler.sendEmptyMessage(myHandler.onDisconnected);
			}
		}
		if (state_ == State.SIGNING_IN) {
			Log.d(TAG, "onRead() state_= connected");
			state_ = State.CONNECTED;
			hangingGetConnect();
		}
	}
	
	/**
	 * 从数据流中读取字符串
	 * @param inStream
	 * @return
	 */
	public static String getStringFromStream(InputStream inStream) {
		   BufferedReader reader = new BufferedReader(new InputStreamReader(inStream), 16*1024); //强制缓存大小为16KB，一般Java类默认为8KB
           StringBuilder sb = new StringBuilder();
           try {
        	   String line = null;
               while ((line = reader.readLine()) != null) {  //处理换行符
                   sb.append(line + "\n");  
               }
           } catch (IOException e) {
        	   Log.e(TAG, "getStringFromStream error",e);
           } finally {
               try {
            	   inStream.close();
               } catch (IOException e) {
            	   Log.e(TAG, "getStringFromStream error",e);
               }
           }
           return sb.toString();
	}

	public boolean sendToPeer(int peer_id, String message) {
		if (state_ != State.CONNECTED){
			Log.d(TAG, "sendToPeer() state_ != State.CONNECTED return");
			return false;
		}

		if (!is_connected() || peer_id == -1){
			Log.d(TAG, "sendToPeer() is_connected:"+is_connected()+",peer_id:"+peer_id+",return");
			return false;
		}
		sendMsg(mServerUrl+"/message?peer_id="+my_id_+"&to="+peer_id, "POST", message);
		return true;
	}
	
	public boolean sendHangUp(int peer_id){
		return sendToPeer(peer_id, kByeMessage);
	}

	public boolean isSendingMessage(){
		 return state_ == State.CONNECTED &&  mIsSending;
	}

	public void signOut() {
		Log.d(TAG, "signOut()");
//		if (state_ == State.NOT_CONNECTED || state_ == State.SIGNING_OUT)
//			return;
		Log.d(TAG, "signOut() state_= signing_out");
		state_ = State.SIGNING_OUT;
		if (my_id_ != -1) {
			sendMsg(mServerUrl + "/sign_out?peer_id=" + my_id_);
		} else {
			// Can occur if the app is closed before we finish connecting.
		}
		Log.d(TAG, "signOut() end");
	}
	

	

	
	/**
	 * 解析用户列表
	 * @param entry
	 * @return
	 */
	Object[] parseEntry(String entry) {
		Log.w(TAG, "parseEntry(),entry:"+entry);
		String[] data = entry.split(",");
		if (data.length >= 3) {
			Object[] name_id_connected = new Object[]{
					data[0],Integer.parseInt(data[1]),!data[2].equals("0")
			};
			return name_id_connected;
		}
		return null;
	}
	
	void close() {
		Log.w(TAG, "close()");
		peers_.clear();
		my_id_ = -1;
		Log.d(TAG, "close() state_= not_connected");
		state_ = State.NOT_CONNECTED;
	}
	
	void onMessageFromPeer(int peer_id, String message) {
		if (message.equals(kByeMessage)) {
			myHandler.obtainMessage(myHandler.onPeerDisconnected, peer_id, 0).sendToTarget();
		} else {
			myHandler.obtainMessage(myHandler.onMessageFromPeer,peer_id,0,message).sendToTarget();
		}
	}
	
	int mHangingGetConnectRetryTime=0;
	/**
	 * 发送waitMsg的消息
	 */
	void hangingGetConnect() {
		Log.d(TAG, "hangingGetConnect()");
		new Thread(){
			public void run() {
				try {
					String dataUrl = mServerUrl+"/wait?peer_id="+my_id_;
					URL url = new URL(dataUrl);//获取到路径
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			        conn.setRequestMethod("GET");
			        int stateCode = conn.getResponseCode();
			        if (stateCode == 200) {
			        	int peerId = conn.getHeaderFieldInt("Pragma", -1);
			            String data = getStringFromStream(conn.getInputStream());
			            try {
			            	onHangingGetRead(data, peerId);
						} catch (Exception e) {
							Log.e(TAG, "onHangingGetRead() error",e);
						}
						mHangingGetConnectRetryTime = 0;
						hangingGetConnect();
			        }else {
			        	Log.d(TAG, "hangingGetConnect() http stateCode:"+stateCode);
			        	String data =  getStringFromStream(conn.getErrorStream());
			        	Log.d(TAG, "hangingGetConnect() http stateCode:"+stateCode+",error msg:"+data);
			        	if (stateCode == 500){//重新登录
			        		Log.d(TAG, "hangingGetConnect() state_= not_connected stateCode = 500");
			        		state_ = PeerConnectionClient1.State.NOT_CONNECTED;
			        		connect(server,serverPort,client_name);
			        	}else {
			        		retryHangingGetConnect();
						}
					}
				} catch (Exception e) {
					Log.e(TAG, "hangingGetConnect() error",e);
					retryHangingGetConnect();
				}
			};
		}.start();
	}
	
	private void retryHangingGetConnect() {
		Log.d(TAG, "retryHangingGetConnect() Times:"+mHangingGetConnectRetryTime);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {}
		mHangingGetConnectRetryTime ++;
//		if (mHangingGetConnectRetryTime>4){
//			myHandler.sendEmptyMessage(myHandler.onDisconnected);
//		} else {
		if (NetworkChangeReceiver.hasNetwork()){//有网络重试
			hangingGetConnect();
		}else {
			try {//无网络等待15秒后再试
				Thread.sleep(15000);
				retryHangingGetConnect();
			} catch (InterruptedException e) {}
		}
//		}
	}
	
	public void onHangingGetRead(String msg, int peer_id) {
		Log.d(TAG, "onHangingGetRead() msg:" + msg + "");
		String contentData = msg;
		if (!TextUtils.isEmpty(contentData)) {
			if (peer_id != -1) {
				// Store the position where the body begins.
				if (my_id_ == peer_id) {
					// A notification about a new member or a member that just
					// disconnected.
					String peerList[] = contentData.split("\n");
					int listSize = peerList.length;
					if (listSize > 0) {
						Object[] name_id_connected = parseEntry(peerList[0]);
						if (name_id_connected != null) {
							String name = (String) name_id_connected[0];
							if (!TextUtils.isEmpty(name)) {
								Integer id = (Integer) name_id_connected[1];
								Boolean connected = (Boolean) name_id_connected[2];
								Log.d(TAG, "onHangingGetRead name:" + name
										+ ",id:" + id + ",connected:"
										+ connected);
								if (connected) {
									peers_.put(name, id);
									myHandler.obtainMessage(myHandler.onPeerConnected, id, 0, name).sendToTarget();
								} else {
									peers_.remove(name);
									myHandler.obtainMessage(myHandler.onPeerDisconnected, id, 0).sendToTarget();
								}
							}
						}
					}

				} else {
					onMessageFromPeer(peer_id, contentData);
				}
			}
		}
	}
	
	class MyHandler extends Handler{
		public static final int 
		onSignedIn=1, // Called when we're logged on.
		onDisconnected=2,
		onPeerConnected=3,
		onPeerDisconnected=4,
		onMessageFromPeer=5,
		onMessageSent=6;
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case onSignedIn:
				callback_.onSignedIn();
				break;
			case onDisconnected:
				callback_.onDisconnected();
				break;
			case onPeerConnected:
				callback_.onPeerConnected(msg.arg1, (String)msg.obj);
				break;
			case onPeerDisconnected:
				callback_.onPeerDisconnected(msg.arg1);
				break;
			case onMessageFromPeer:
				callback_.onMessageFromPeer(msg.arg1, (String)msg.obj);
				break;
			case onMessageSent:
				callback_.onMessageSent(msg.arg1);
				break;
			default:
				break;
			}
		};
	};

}
