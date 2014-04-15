package com.example.newdemo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.core.webrtclib.component.PeerAccount;

public class PeerAdapter extends BaseAdapter {
	
	private Context mContext;
	private List<String> mKeyList = new LinkedList<String>();
	private HashMap<String,Integer> mData;
	private LayoutInflater mInflater;
	
	public PeerAdapter(Context c,HashMap<String,Integer> data){
		mContext = c;
		mData = data;
		generateList();
		mInflater = LayoutInflater.from(mContext);
	}
	
	public void addPeer(PeerAccount peer){
		if(mData.containsKey(peer.getAccount())){
			return;
		}

		mData.put(peer.getAccount(), peer.getPeerId());
		generateList();
		
		notifyDataSetChanged();
	}
	
	public void removePeer(int peerId){
		if(!mData.containsValue(peerId)){
			return;
		}
		
		String account = null;
		Iterator<Entry<String, Integer>> iterator = mData.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<String,Integer> entry = iterator.next();
			Integer id = entry.getValue();
			if(id == peerId){
				account = entry.getKey();
				break;
			}
		}
		mData.remove(account);
		generateList();
		
		notifyDataSetChanged();
	}
	
	private void generateList(){
		assert(mKeyList != null);
		mKeyList.clear();
		
		Iterator<Entry<String,Integer>> iter = mData.entrySet().iterator();
		while(iter.hasNext()){
			Entry<String,Integer> entry = iter.next();
			mKeyList.add(entry.getKey());
		}
	}

	@Override
	public int getCount() {
		return mKeyList.size();
	}

	@Override
	public Object getItem(int arg0) {
		String key = mKeyList.get(arg0);
		return mData.get(key);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		
		ViewHolder holder = null;
		if(convertview == null){
			convertview = mInflater.inflate(R.layout.item_peer, null);
			holder = new ViewHolder();
			holder.tv = (TextView)convertview.findViewById(R.id.tv_peer);
			convertview.setTag(holder);
		}else{
			holder = (ViewHolder) convertview.getTag();
		}
		
		holder.tv.setText(mKeyList.get(position));
		
		return convertview;
	}
	
	private class ViewHolder{
		public TextView tv;
	}

}
