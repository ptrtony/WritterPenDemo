package org.feng.sockettest.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import net.tsz.afinal.FinalDb;

import com.android.bluetown.bean.MemberUser;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class BackService extends Service {
	private static final String TAG = "BackService";
	private static final long HEART_BEAT_RATE = 10 * 1000;
	public static final String HOST = "xapi.smartparks.cn";// "192.168.1.21";//
	public static final int PORT = 9011;	
	public static final String MESSAGE_ACTION="org.feng.message_ACTION";
	public static final String HEART_BEAT_ACTION="org.feng.heart_beat_ACTION";
	private FinalDb db;
	private String userId;
	
	private ReadThread mReadThread;

	private LocalBroadcastManager mLocalBroadcastManager;

	private WeakReference<Socket> mSocket;

	// For heart Beat
	private Handler mHandler = new Handler();
	private Runnable heartBeatRunnable = new Runnable() {

		@Override
		public void run() {
			if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {				
				db = FinalDb.create(BackService.this);
				List<MemberUser> users = db.findAll(MemberUser.class);
				if (users != null && users.size() != 0) {
					MemberUser user = users.get(0);
					if (user != null) {
						userId = user.getMemberId();
					}
				}
				boolean isSuccess = sendMsg(userId);//就发送一个\r\n过去 如果发送失败，就重新初始化一个socket
				if (!isSuccess) {
					mHandler.removeCallbacks(heartBeatRunnable);
					mReadThread.release();
					releaseLastSocket(mSocket);
					new InitSocketThread().start();
					System.out.println("heart_beat");
				}
			}
			
				mHandler.postDelayed(this, HEART_BEAT_RATE);
		
		}
	};

	private long sendTime = 0L;


	private IBackService.Stub iBackService = new IBackService.Stub() {

		@Override
		public boolean sendMessage(String message) throws RemoteException {
			return sendMsg(message);
		}
		@Override
		public void close(){
			mHandler.removeCallbacks(heartBeatRunnable);
			if(mReadThread!=null){
				mReadThread.release();
			}			
			releaseLastSocket(mSocket);
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		return iBackService;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		new InitSocketThread().start();
		mLocalBroadcastManager=LocalBroadcastManager.getInstance(this);
		
	}
	public boolean sendMsg(String msg) {
		if (null == mSocket || null == mSocket.get()) {
			return false;
		}
		Socket soc = mSocket.get();
		try {
			if (!soc.isClosed() && !soc.isOutputShutdown()) {
				OutputStream os = soc.getOutputStream();
				String message = msg ;
				os.write(message.getBytes());
				os.flush();
				sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void initSocket() {//初始化Socket
		try {
			Socket so = new Socket(HOST, PORT);
			mSocket = new WeakReference<Socket>(so);
			mReadThread = new ReadThread(so);
			mReadThread.start();
			mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
			db = FinalDb.create(BackService.this);
			List<MemberUser> users = db.findAll(MemberUser.class);
			if (users != null && users.size() != 0) {
				MemberUser user = users.get(0);
				if (user != null) {
					userId = user.getMemberId();
				}
			}
			boolean isSuccess = sendMsg(userId);//就发送一个\r\n过去 如果发送失败，就重新初始化一个socket
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close(){
		mHandler.removeCallbacks(heartBeatRunnable);
		mReadThread.release();
		releaseLastSocket(mSocket);
	}

	private void releaseLastSocket(WeakReference<Socket> mSocket) {
		try {
			if (null != mSocket) {
				Socket sk = mSocket.get();
				if(sk!=null){
					if (!sk.isClosed()) {
						sk.close();
					}
				}			
				sk = null;
				mSocket = null;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class InitSocketThread extends Thread {
		@Override
		public void run() {
			super.run();
			initSocket();
		}
	}

	// Thread to read content from Socket
	class ReadThread extends Thread {
		private WeakReference<Socket> mWeakSocket;
		private boolean isStart = true;

		public ReadThread(Socket socket) {
			mWeakSocket = new WeakReference<Socket>(socket);
		}

		public void release() {
			isStart = false;
			releaseLastSocket(mWeakSocket);
		}

		@SuppressLint("NewApi")
		@Override
		public void run() {
			super.run();
			Socket socket = mWeakSocket.get();
			if (null != socket) {
				try {
					InputStream is = socket.getInputStream();
					byte[] buffer = new byte[1024 * 4];
					int length = 0;
					while (!socket.isClosed() && !socket.isInputShutdown()
							&& isStart && ((length = is.read(buffer)) != -1)) {
						if (length > 0) {
							String message = new String(Arrays.copyOf(buffer,
									length)).trim();
							Log.e(TAG, message);
							//收到服务器过来的消息，就通过Broadcast发送出去
							if(message.equals("ok")){//处理心跳回复
								Intent intent=new Intent(HEART_BEAT_ACTION);
								mLocalBroadcastManager.sendBroadcast(intent);
							}else{
								//其他消息回复
								Intent intent=new Intent(MESSAGE_ACTION);
								intent.putExtra("message", message);
								mLocalBroadcastManager.sendBroadcast(intent);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
