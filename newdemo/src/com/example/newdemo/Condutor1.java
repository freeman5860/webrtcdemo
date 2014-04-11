package com.example.newdemo;
//package com.example.newdemo;
//
//import java.util.HashMap;
//
//import org.webrtc.videoengine.VideoCaptureAndroid;
//
//import android.R.integer;
//import android.content.ContentValues;
//import android.text.TextUtils;
//
//import com.helecomm.Customs.Log;
//import com.helecomm.Customs.Helper.DBHelper;
//import com.helecomm.Customs.Model.Contacts;
//import com.helecomm.Customs.Model.ICallBackListener;
//import com.helecomm.WebRTC.Peer_conn_collection.PeerStatus;
//
//public class Condutor extends PeerConnectionClient1 {
//	private static final String TAG = "Condutor";
//	private static Condutor mCondutor;
//	
//	static{
//		mCondutor = new Condutor();
//		//mCondutor.registerObserver(mPeerConnectionClientObserver);
//	}
//	protected Condutor(){
//		Peer_conn_collection.setCallBack(mCollectionCallBackListener);
//		registerObserver(mPeerConnectionClientObserver);
//	}
//	
//	public static Condutor getInstance(){
//		return mCondutor;
//	}
//	
//	public static void testFunction(){
//		String dataString = "HTTP/1.1 200 OK\r\nServer: PeerConnectionTestServer/0.1\r\nCache-Control: no-cache\r\nConnection: close\r\nContent-Type: text/plain\r\nContent-Length: 3127\r\nPragma: 15\r\nAccess-Control-Allow-Origin: *\r\nAccess-Control-Allow-Credentials: true\r\nAccess-Control-Allow-Methods: POST, GET, OPTIONS\r\nAccess-Control-Allow-Headers: Content-Type, Content-Length, Connection, Cache-Control\r\nAccess-Control-Expose-Headers: Content-Length, X-Peer-Id\r\n\r\n001{\r\n   \"messageType\" : \"OFFER\",\r\n   \"offererSessionId\" : \"7a3sODamf09ugx42jj0TR+b6+lJrTpWP\",\r\n   \"sdp\" : \"v=0\r\no=- 694637030 1 IN IP4 127.0.0.1\r\ns=\r\nt=0 0\r\nm=audio 3651 RTP/SAVPF 103 104 0 8 106 105 13 126\r\nc=IN IP4 113.97.182.87\r\na=rtcp:3653 IN IP4 113.97.182.87\r\na=candidate:0 1 udp 2130706432 192.168.1.82 3650 typ host network_name 本地连接 2 username Lmnpb1tzG/UusUtm password CcOMzy+ERx0+XQdlfxNWrIvr generation 0\r\na=candidate:0 2 udp 2130706432 192.168.1.82 3652 typ host network_name 本地连接 2 username IcLvFo3EU2icGS1R password hJQoLT/5jhFXr6UW9iYMqISq generation 0\r\na=candidate:0 2 udp 1912602624 113.97.182.87 3653 typ srflx network_name 本地连接 2 username IcLvFo3EU2icGS1R password hJQoLT/5jhFXr6UW9iYMqISq generation 0\r\na=candidate:0 1 udp 1912602624 113.97.182.87 3651 typ srflx network_name 本地连接 2 username Lmnpb1tzG/UusUtm password CcOMzy+ERx0+XQdlfxNWrIvr generation 0\r\na=candidate:0 1 tcp 1694498816 192.168.1.82 3698 typ host network_name 本地连接 2 username Lmnpb1tzG/UusUtm password CcOMzy+ERx0+XQdlfxNWrIvr generation 0\r\na=candidate:0 2 tcp 1694498816 192.168.1.82 3699 typ host network_name 本地连接 2 username IcLvFo3EU2icGS1R password hJQoLT/5jhFXr6UW9iYMqISq generation 0\r\na=mid:audio\r\na=rtcp-mux\r\na=crypto:0 AES_CM_128_HMAC_SHA1_32 inline:tJXFPAekc7xl5a3hNv2AlaLEKno6txjsXLwrRsAD \r\na=crypto:1 AES_CM_128_HMAC_SHA1_80 inline:hZQBz18kKXuNFdN7rKP1AS+UlLl53hTdEBjEu1C/ \r\na=rtpmap:103 ISAC/16000\r\na=rtpmap:104 ISAC/32000\r\na=rtpmap:0 PCMU/8000\r\na=rtpmap:8 PCMA/8000\r\na=rtpmap:106 CN/32000\r\na=rtpmap:105 CN/16000\r\na=rtpmap:13 CN/8000\r\na=rtpmap:126 telephone-event/8000\r\na=ssrc:3802908145 cname:flf6v00RWrDrUsYW\r\na=ssrc:3802908145 mslabel:local_stream_label\r\na=ssrc:3802908145 label:local_audio_label\r\nm=video 3655 RTP/SAVPF 100 101 102\r\nc=IN IP4 113.97.182.87\r\na=rtcp:3657 IN IP4 113.97.182.87\r\na=candidate:0 1 udp 2130706432 192.168.1.82 3654 typ host network_name 本地连接 2 username x09bdDSSXwKeHuFc password AvUifRU8r1/QtcKDw74eYHsx generation 0\r\na=candidate:0 2 udp 2130706432 192.168.1.82 3656 typ host network_name 本地连接 2 username JdRkdrOJxUg5fUX7 password oF2C2hzolDDUcgT0B2EozYwY generation 0\r\na=candidate:0 1 udp 1912602624 113.97.182.87 3655 typ srflx network_name 本地连接 2 username x09bdDSSXwKeHuFc password AvUifRU8r1/QtcKDw74eYHsx generation 0\r\na=candidate:0 2 udp 1912602624 113.97.182.87 3657 typ srflx network_name 本地连接 2 username JdRkdrOJxUg5fUX7 password oF2C2hzolDDUcgT0B2EozYwY generation 0\r\na=candidate:0 1 tcp 1694498816 192.168.1.82 3700 typ host network_name 本地连接 2 username x09bdDSSXwKeHuFc password AvUifRU8r1/QtcKDw74eYHsx generation 0\r\na=candidate:0 2 tcp 1694498816 192.168.1.82 3701 typ host network_name 本地连接 2 username JdRkdrOJxUg5fUX7 password oF2C2hzolDDUcgT0B2EozYwY generation 0\r\na=mid:video\r\na=rtcp-mux\r\na=crypto:0 AES_CM_128_HMAC_SHA1_80 inline:3/+3+de0QbszlLde4frepHhfVx5P0OF78RMHXT0r \r\na=rtpmap:100 VP8/90000\r\na=rtpmap:101 red/90000\r\na=rtpmap:102 ulpfec/90000\r\n\",\r\n   \"seq\" : 1,\r\n   \"tieBreaker\" : 1499342250\r\n}\r\n\r\n\r\n\r\n";
//		//mCondutor.onHangingGetRead(dataString);
//	}
//	public static final int
//		Buss_Type_Chat = 1,  //通话呼叫
//		Buss_Type_Monitor = 2, //监听呼叫
//		Buss_Type_BreakIn = 3,  //强插呼叫
//		Buss_Type_ForceCall = 4,  //强接呼叫
//		Buss_Type_Meeting = 5, //会议呼叫
//		Buss_Type_VideoLoop = 6,  // 视频轮询
//		Buss_Type_BROADCAST =7 ;//广播
//
//	
//    private static int sys_msg_type = 0;//系统消息类型
//    private static int def_msg_type = 1;//自定义消息类型
//    /**
//     * 语音通话类型
//     */
//    public static final int audio_media_type = 0;
//    /**
//     * 语音加视频通话类型
//     */
//    public static final int audio_video_media_type = 1;
//    /**
//     * 单工 仅外呼，对方无法回话, 对方自动接听
//     */
//    public static final int half_call_type = 0;
//    /**
//     * 双工 电话通话 
//     */
//    public static final int whole_call_type = 1;
//    /**
//     * 双工 自动通话
//     */
//    public static final int whole_auto_call_type = 2;
//
//    /// <summary>
//    /// 接听电话消息
//    /// </summary>
//    //private static string def_msg_answer_phone = "ANSWERPHONE";
//
//    private static String def_msg_call_reached = "CALLREACHED";//收到呼叫 （等待接听）
//
//    /// <summary>
//    /// 发送的消息Message描述
//    /// msg_type + media_type + call_type + message
//    /// 第一个字符代表消息类型  sys_msg_type：vwebrtc系统自己产生的消息 def_msg_type：自己定义的消息
//    /// 第二个字符代表通话类型 语音或视频通话
//    /// 其他字符为消息内容，系统消息按webrtc方式处理
//    /// 自定义消息判断对应的命令
//    /// AnswerPhone : 对方接听电话
//    /// </summary>
//
//
//    // 发送方                          接收方
//    // onsignal send offer
//    //                                 msgfrompeer rev offer  process msg
//    //                                 onsignal answer msg     send connected msg    hint callin
//    //                                 user answer (add stream ??)  send answer msg  
//    //rev answer msg process msg
//    //onsignal seq1 OK (send )  (hint OK)
//    //                                 rev seq1 OK process msg
//    //                                 
//    //                                 send seq2 offser
//    //rev seq2 offer
//    //send seq2 answer
//    // 
//    //                                  send seq2 OK
//    //rev seq2 OK 
//
//    public interface CondutorObserver{
//    	/**
//    	 * 发生错误
//    	 * @param arg0
//    	 * @param arg1
//    	 * @param data
//    	 */
//    	void OnCondutorError(int peerid,int calltype_,int mediatype_);//
//    	/**
//    	 * 单工 可以开始讲话
//    	 * @param arg0
//    	 * @param arg1
//    	 * @param data
//    	 */
//    	void OnCondutorHalfCanTalk(int peerid,int calltype_,int mediatype_,int bussType);//
//    	/**
//    	 * 单工 可以开始听
//    	 * @param arg0
//    	 * @param arg1
//    	 * @param data
//    	 */
//    	void OnCondutorHalfCanListen(int peerid,int calltype_,int mediatype_,int bussType);//
//    	/**
//    	 * 双工 主呼叫方 收到已呼叫到对方 等待接听
//    	 * @param arg0
//    	 * @param arg1
//    	 * @param data
//    	 */
//    	void OnCondutorWholeCallReached(int peerid,int calltype_,int mediatype_,int bussType);//
//    	/**
//    	 * 双工 主呼叫方 收到对方已接听电话
//    	 * @param arg0
//    	 * @param arg1
//    	 * @param data
//    	 */
//    	void OnCondutorWholeCallAnswered(int peerid,int calltype_,int mediatype_ ,String singalingmsg,int bussType);//
//    	/**
//    	 * 接听方 收到有电话呼入
//    	 * @param arg0
//    	 * @param arg1
//    	 * @param data
//    	 */
//    	void OnCondutorCallIn(int peerid,int calltype_,int mediatype_ ,String singalingmsg,int bussType);//
//    	/**
//    	 * 通话已终断 （已挂机）
//    	 * @param arg0
//    	 * @param arg1
//    	 * @param data
//    	 */
//    	void OnCondutorCallClosed(int peerid,int calltype_,int mediatype_,int bussType);//
//    	
//    	/**
//    	 * 用户上线
//    	 * @param peerid
//    	 * @param account
//    	 */
//    	void OnUserOnline(int peerid,String account);
//    	
//    	/**
//    	 * 用户离线
//    	 * @param peerId
//    	 */
//    	void OnUserOffline(int peerId);
//    }
//    
//    private static CondutorObserver mCondutorObserver;
//    
//    public static void setCondutorCallback(CondutorObserver condutorObserver)
//    {
//    	mCondutorObserver= condutorObserver;
//    }
//    
//    /**
//     * 连接到webrtc服务器
//     * @param server
//     * @param port
//     * @param account
//     * @return
//     */
//    public static boolean connectServer(String server,int port,String account) {
//    	ContentValues contentValues = new ContentValues();
//    	contentValues.put(Contacts.Onlined_Status, 0);
//    	DBHelper.getSqLiteDBInstance().update(Contacts.TABLE_NAME, contentValues, null, null);
//		return mCondutor.connect(server, port, account);
//	}
//    
//    /**
//     * 退出
//     */
//    public static void logout(){
//    	mCondutor.signOut();
//    }
//	
//	 /// <summary>
//    /// 呼叫
//    /// </summary>
//    /// <param name="account">呼叫的帐号</param>
//    /// <param name="calltype">0 单工 1双工 2 双工自动</param>
//    /// <param name="mediatype">呼叫类型 0：语音  1：语音加视频 </param>
//    /// <param name="mediatype">mediatype为1语音加视频该参数有效 0：第一个摄像头  1：第二个摄像头 </param>
//    /// <param name="mediatype">mediatype为1语音加视频该参数有效 显示本地视频图像的控件句柄 </param>
//    /// <param name="mediatype">mediatype为1语音加视频该参数有效 显示远程视频图像的控件句柄 </param>
//    /// <returns>failed : false   success : true</returns>
//    public static boolean call(String account, int calltype, int mediatype, int camera, int local_video_ctrl_handle ,  int remote_video_ctrl_handle,int buss_type)
//    {
//    	//Integer peerId = mCondutor.peers().get(account);//TODO can't get peerId by this way
//    	int peerId = Contacts.getPeer_IdByAccount(account);
//    	Log.d(TAG, "call() account："+account+",peerId:"+peerId);
//    	if (peerId != -1)
//    		return call(peerId, calltype, mediatype, camera, local_video_ctrl_handle, remote_video_ctrl_handle, buss_type);
//    	else {
//			return false;
//		}
//    }
//
//
//    /// <summary>
//    /// 呼叫
//    /// </summary>
//    /// <param name="account">呼叫的ID</param>
//    /// <param name="calltype">0 单工 1双工 2 双工自动</param>
//    /// <param name="mediatype">呼叫类型 0：语音  1：语音加视频 </param>
//    /// <param name="mediatype">mediatype为1语音加视频该参数有效 0：第一个摄像头  1：第二个摄像头 </param>
//    /// <param name="mediatype">mediatype为1语音加视频该参数有效 显示本地视频图像的控件句柄 </param>
//    /// <param name="mediatype">mediatype为1语音加视频该参数有效 显示远程视频图像的控件句柄 </param>
//    /// <returns>failed : false   success : true</returns>
//    public static boolean call(int peer_id, int calltype, int mediatype, int camera, int local_video_ctrl_handle, int remote_video_ctrl_handle,int buss_type)
//    {
//    	Log.d(TAG, "call() peerId:"+peer_id+",callType:"+calltype+",mediatype:"+mediatype);
//        if (peer_id < 0){
//            return false;
//        }
//        Peer_conn_collection.AddPeerToConnect(peer_id, calltype, mediatype, camera, local_video_ctrl_handle, remote_video_ctrl_handle,buss_type);
//        return true;
//    }
//    
//  /// <summary>
//    /// 取消呼叫
//    /// </summary>
//    /// <param name="account"></param>
//    /// <returns></returns>
//    public static boolean CancelCall(String account)
//    {
//        return CancelCall(0);
//    }
//
//    /// <summary>
//    /// 取消呼叫
//    /// </summary>
//    /// <param name="peer_id"></param>
//    /// <returns></returns>
//    public static boolean CancelCall(int peer_id)
//    {
//        return true;
//    }
//    
//  /// <summary>
//    /// 接听电话
//    /// </summary>
//    /// <param name="peer_id"></param>
//    /// <param name="camera"></param>
//    /// <returns></returns>
//	public static boolean answerPhone(int peer_id, int camera, int local_video_ctrl_handle, int remote_video_ctrl_handle, String answer_msg) {
//		Log.d(TAG, "answerPhone() peerId:" + peer_id);
//		int calltype = Peer_conn_collection.getCalltype(peer_id);
//		calltype += Peer_conn_collection.getCalltype(peer_id * -1);
//		if (calltype == half_call_type) {
//			sendMsgToPeer(peer_id, sys_msg_type, answer_msg, 1);
//			peer_id = peer_id * -1;
//		} else {
//			sendMsgToPeer(peer_id, sys_msg_type, answer_msg);
//		}
//		Peer_conn_collection.shareLocalStream(peer_id, camera, local_video_ctrl_handle, remote_video_ctrl_handle);
//		return true;
//	}
//
//
//    /// <summary>
//    /// 挂电话
//    /// </summary>
//    /// <param name="peer_id"></param>
//    public static void closePhone(int peer_id)
//    {
//    	Log.d(TAG, "closePhone() peerId:"+peer_id);
//    	Peer_conn_collection.close(peer_id);
//    	//TODO 广播挂电话需要负
//    	Peer_conn_collection.close(peer_id*(-1));
//    }
//    
//    /**
//     * 挂断全部电话
//     */
//    public static void closeAllPhone()
//    {
//    	Log.d(TAG, "closeAllPhone()");
//    	Peer_conn_collection.CloseAll();
//    }
//    
//    public static String getAccountByPeer_Id(int peer_id){
//    	return mCondutor.getAccountByPeerId(peer_id);
//    }
//    
//    public static int getPeer_IdByAccount(String account){
//    	return mCondutor.getPeerIdByAccount(account);
//    }
//    
//    public static HashMap<String,Integer> getPeers(){
//		return mCondutor== null?null: mCondutor.peers();
//	}
//    
//    private PeerConnectionClientObserver mPeerConnectionClientObserver = new PeerConnectionClientObserver() {
//		
//		@Override
//		public void onSignedIn() {
//			Log.d(TAG, "onSignedIn()");
//			ContentValues values = new ContentValues();
//			values.put(Contacts.Peer_Id, 0);
//			values.put(Contacts.Onlined_Status, 0);
//			int rows = DBHelper.getSqLiteDBInstance().update(Contacts.TABLE_NAME, values,null,null);
//			Log.d(TAG, "onSignedIn() init rows:"+rows);
//		}
//		
//		@Override
//		public void onPeerDisconnected(int peer_id) {
//			Log.d(TAG, "onPeerDisconnected() peer_id:"+peer_id);
//			ContentValues values = new ContentValues();
//			values.put(Contacts.Peer_Id, 0);
//			values.put(Contacts.Onlined_Status, 0);
//			int rows = DBHelper.getSqLiteDBInstance().update(Contacts.TABLE_NAME, values, Contacts.Peer_Id + "=?", new String[]{peer_id+""});
//			Log.d(TAG, "onPeerDisconnected() peer_id:"+peer_id+",rows:"+rows);
//			if (mCondutorObserver!=null)
//				mCondutorObserver.OnUserOffline(peer_id);
//		}
//		
//		@Override
//		public void onPeerConnected(int peer_id, String account) {
//			Log.d(TAG, "onPeerConnected() account:"+account + ",peer_id:"+peer_id);
//			Contacts contacts = Contacts.query(new String[]{Contacts._ID,Contacts.Peer_Id}, Contacts.Account +"=?", new String[]{account});
//			if (contacts != null && contacts.peer_id < peer_id){//小于peer_id说明帐户重新登录了
//				contacts.peer_id = peer_id;
//				contacts.onlined_status = 1;
//				contacts.update(new String[]{Contacts.Peer_Id,Contacts.Onlined_Status});
//			}
//			if (mCondutorObserver!=null && contacts != null && contacts.peer_id == peer_id)
//				mCondutorObserver.OnUserOnline(peer_id, account);
//		}
//		
//		@Override
//		public void onMessageSent(int err) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void onMessageFromPeer(int peer_id, String message) {
//			condutor_MessageFromPeer(peer_id,message);
//		}
//		
//		@Override
//		public void onDisconnected() {
//			// TODO Auto-generated method stub
//			
//		}
//	};
//    
//    public static ICallBackListener mCollectionCallBackListener = new ICallBackListener() {
//		
//		@Override
//		public Object excute(int cmdCode, int arg1, int arg2, Object params) {
//			switch (cmdCode) {
//			case Peer_conn_collection.CallbackID.PEER_CONNECTION_ERROR:
//				peer_conn_collection_OnError(arg1);
//				break;
//			case Peer_conn_collection.CallbackID.SEND_MESSAGE_TO_PEER:
//				onSignalingMessage(arg1,(String)params);
//				break;
//			case Peer_conn_collection.CallbackID.NEW_STREAM_ADDED:
//				peer_conn_collection_OnAddStream(arg1);
//				break;
//			case Peer_conn_collection.CallbackID.STREAM_REMOVED:
//				peer_conn_collection_OnRemoveStream(arg1);
//				break;
//			default:
//				break;
//			}
//			return null;
//		}
//		@Override
//		public Object excute(int cmdCode, Object params) {
//			return null;
//		}
//	};
//    
//    /// <summary>
//    /// 发送消息
//    /// </summary>
//    /// <param name="peer_id"></param>
//    /// <param name="msg_type"></param>
//    /// <param name="mediatype"></param>
//    /// <param name="msg"></param>
//    /// <returns></returns>
//	private static boolean sendMsgToPeer(int peer_id, int msg_type, String msg) {
//		return sendMsgToPeer(peer_id, msg_type, msg, 0);
//	}
//    
//	private static boolean sendMsgToPeer(int peer_id, int msg_type, String msg, int half_direction) {
//		int type_peer_id = peer_id;
//        if (half_direction == 1){
//            type_peer_id = peer_id * -1;
//        }
//		int call_type = Peer_conn_collection.getCalltype(type_peer_id);
//		int mediatype = Peer_conn_collection.getMediatype(type_peer_id);
//		int bussType = Peer_conn_collection.getBusstype(type_peer_id);
//		Log.d(TAG, "sendMsgToPeer() peer_id:"+peer_id+",msg_type:"+msg_type+",calltype:"+call_type+",mediatype:"
//				+mediatype+",bussType:"+bussType);
//		if ((mediatype >= 0 && mediatype <= 2) && (call_type >= 0 && call_type <= 2)) {
//			return mCondutor.sendToPeer(peer_id, packetSendMessage(msg_type, mediatype, call_type, bussType, msg,half_direction));
//		} else {
//			Log.w(TAG, "SendMsgToPeer()  error mediatype=" + mediatype);
//			return false;
//		}
//	}
//    
//    /// <summary>
//    /// 打包消息
//    /// </summary>
//    /// <param name="msg_type"></param>
//    /// <param name="media_type"></param>
//    /// <param name="msg"></param>
//    /// <returns></returns>
//    private static String packetSendMessage(int msg_type, int media_type, int call_type,int bussType, String msg,int half_direction)
//    {
//        return ""+msg_type+media_type+call_type+bussType+half_direction+msg;
//    }
//    
//	static void condutor_MessageFromPeer(int peerId, String msg) {
//		Log.d(TAG, "Condutor_MessageFromPeer() peerId:" + peerId + ",account:" + mCondutor.getAccountByPeerId(peerId) + ",msg:" + msg);
//		if (TextUtils.isEmpty(msg) || msg.length() < 0) {
//			Log.d(TAG, "Condutor_MessageFromPeer  error");
//			return;
//		}
//
//		int msg_type_ = Integer.parseInt(msg.substring(0, 1));
//		int mediatype_ = Integer.parseInt(msg.substring(1, 2));
//		int calltype_ = Integer.parseInt(msg.substring(2, 3));
//		int buss_type = Integer.parseInt(msg.substring(3, 4));
//		int halfdirection_ = Integer.parseInt(msg.substring(4, 5));
//		if (msg_type_ == def_msg_type) {
//			String order = msg.substring(5);
//			Log.d(TAG, "Condutor_MessageFromPeer  order:" + order + ",def_msg_call_reached:" + def_msg_call_reached);
//			// 已呼叫到对方，由正在呼叫状态转到等待接听状态
//			if (order.startsWith(def_msg_call_reached)) {
//				Log.d(TAG, "Condutor_MessageFromPeer  def_msg_call_reached");
//				if (mCondutorObserver != null) {
//					Log.d(TAG, "Condutor_MessageFromPeer()  call OnCondutorWholeCallReached()");
//					mCondutorObserver.OnCondutorWholeCallReached(peerId, calltype_, mediatype_, buss_type);
//				}
//			}
//		} else {
//			String msgData = msg.substring(5);
//			int peerId1 = peerId;
//			// 收到的单呼消息 peer_id 以负值存储
//			if (calltype_ == half_call_type && halfdirection_ == 0) {
//				peerId1 *= -1;
//			}
//			Log.d(TAG, "processMessageFromPeer() processMessageFromPeer() peerId:" + peerId1);
//			if (msgData.contains("\"messageType\" : \"SHUTDOWN\"")) {
//				if (Peer_conn_collection.getCallingPeerCount() <= 2) {
//					VideoCaptureAndroid.setStopRunning();
//				}
//			}
//			Peer_conn_collection.processMessageFromPeer(peerId1, calltype_, mediatype_, msgData, buss_type);
//			Log.d(TAG, "processMessageFromPeer() processMessageFromPeer() end");
//			if (calltype_ == half_call_type &&(msgData.contains("\"messageType\" : \"OK\"") && msgData.contains("\"seq\" : 1"))) {
//				// 通知界面层单工听话端 通道建立完成
//				if (mCondutorObserver != null) {
//					mCondutorObserver.OnCondutorHalfCanListen(peerId, calltype_, mediatype_, buss_type);
//				}
//			}
//			// 对方已挂电话
//			else if (msgData.contains("\"messageType\" : \"SHUTDOWN\"") && (msgData.contains("\"seq\" : 3")||msgData.contains("\"seq\" : 2"))) {
//				if (mCondutorObserver != null) {
//					mCondutorObserver.OnCondutorCallClosed(peerId, calltype_, mediatype_, buss_type);
//				}
//			}
//		}
//		Log.d(TAG, "condutor_MessageFromPeer() end");
//	}
//    
//    static void peer_conn_collection_OnError(int peerId)
//    {
//        Log.d(TAG, "peer_conn_collection_OnError() : peerId:"+peerId+",account:"+mCondutor.getAccountByPeerId(peerId));
//        if (mCondutorObserver!=null){
//        	mCondutorObserver.OnCondutorError(peerId, Peer_conn_collection.getCalltype(peerId), Peer_conn_collection.getMediatype(peerId));
//    	}
//    }
//    
//	static void onSignalingMessage(int peerId,
//			String singalingmsg) {
//		Log.d(TAG, "peer_conn_collection_OnSignalingMessage() : peerId:"
//				+ peerId + ",account:" + mCondutor.getAccountByPeerId(peerId));
//		if (singalingmsg.contains("\"messageType\" : \"ERROR\"")) {
//			Log.e(TAG,"peer_conn_collection_OnSignalingMessage() singalingmsg:"	+ singalingmsg);
//			return;
//		}
//		
//		int calltype = Peer_conn_collection.getCalltype(peerId);
//		int mediatype = Peer_conn_collection.getMediatype(peerId);
//		int buss_type = Peer_conn_collection.getBusstype(peerId);
//
//		if (calltype == whole_call_type) {
//			Log.d(TAG,"peer_conn_collection_OnSignalingMessage() whole_call_type");
//			// 有人呼入, 提示接听 （有电话呼入）
//			if (singalingmsg.contains("\"messageType\" : \"ANSWER\"")&& singalingmsg.contains("\"seq\" : 1")) {
//				Log.d(TAG,"peer_conn_collection_OnSignalingMessage() OnCondutorCallIn");
//				// send call reached 发送收到呼叫
//				sendMsgToPeer(peerId, def_msg_type, def_msg_call_reached);
//
//				// 提示有电话呼入
//				if (mCondutorObserver != null) {
//					mCondutorObserver.OnCondutorCallIn(peerId, calltype,mediatype, singalingmsg,buss_type);
//				}
//			} else {
//				sendMsgToPeer(peerId, sys_msg_type, singalingmsg);
//				int peerstatus = Peer_conn_collection.getPeerStatus(peerId);
//				if (singalingmsg.contains("\"messageType\" : \"OK\"")&& 
//						(peerstatus != PeerStatus.kClosing && peerstatus != PeerStatus.kClosed)) {
//					if (singalingmsg.contains("\"seq\" : 1")|| singalingmsg.contains("\"seq\" : 2")) {
//						// 1 打电话给对方，对方已接听，2对方打电话过来，成功建立接听
//						if (mCondutorObserver != null) {
//							Log.d(TAG,"peer_conn_collection_OnSignalingMessage() OnCondutorWholeCallAnswered");
//							mCondutorObserver.OnCondutorWholeCallAnswered(peerId, calltype, mediatype, singalingmsg,buss_type);
//						}
//					}
//				}
//			}
//		} else if (calltype == half_call_type) {
//			int peer_id = peerId;
//			int half_direction = 0;
//			if (peer_id < 0){
//				peer_id *= -1;
//				half_direction = 1;
//			}
//			if (singalingmsg.contains("\"messageType\" : \"ANSWER\"") && singalingmsg.contains("\"seq\" : 1")) {
//				sendMsgToPeer(peer_id, def_msg_type, def_msg_call_reached,half_direction);
//				// 提示有电话呼入
//				if (mCondutorObserver != null) {
//					mCondutorObserver.OnCondutorCallIn(peer_id, calltype, mediatype, singalingmsg,buss_type);
//				}
//			} else {
//				sendMsgToPeer(peer_id, sys_msg_type, singalingmsg,half_direction);
//				//sendMsgToPeer(peerId, sys_msg_type, singalingmsg);
//				if (singalingmsg.contains("\"messageType\" : \"OK\"")&& singalingmsg.contains("\"seq\" : 1")) {
//					// 通知界面层单工讲话端 通道建立完成0
//					if (mCondutorObserver != null) {
//						mCondutorObserver.OnCondutorHalfCanTalk(peerId,	calltype, mediatype,buss_type);
//					}
//				}
//			}
//		} else {
//			sendMsgToPeer(peerId, sys_msg_type, singalingmsg);
//		}
//	}
//    
//	static void peer_conn_collection_OnAddStream(int peerId) {
//		Log.d(TAG, "peer_conn_collection_OnAddStream() : peerId:" + peerId + ",account:" + mCondutor.getAccountByPeerId(peerId));
//	}
//
//    static void peer_conn_collection_OnRemoveStream(int peerId)
//    {
//    	Log.d(TAG, "peer_conn_collection_OnRemoveStream() : peerId:"+peerId+",account:"+mCondutor.getAccountByPeerId(peerId));
//    }
//}
