package com.aillean.common.action;

import android.content.Context;
import android.util.Log;

import com.aillean.common.eventbus.TestEvent;
import com.aillean.common.eventbus.TestTwoEvent;
import com.aillean.common.manager.CloudDataManager;
import com.aillean.common.request.NetworkTestRequest;
import com.aillean.common.rx.RxCallback;
import com.aillean.tool.SoapLoader;

import io.reactivex.annotations.NonNull;


public class NetworkTestAction {

	private Context mContext;
	private CloudDataManager mCloudDataManager;
	private String ipPort;

	public NetworkTestAction(Context context, CloudDataManager cloudDataManager) {
		this.mContext = context;
		this.mCloudDataManager = cloudDataManager;
	}

	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}

	public void execute() {
		Log.i("aillean", "NetworkTestAction start");
		SoapLoader soapLoader = new SoapLoader();
		soapLoader.setIpPort(ipPort);
		NetworkTestRequest networkTestRequest = new NetworkTestRequest(mCloudDataManager);
		networkTestRequest.setSoapLoader(soapLoader);
		mCloudDataManager.submit(mContext, networkTestRequest,new RxCallback() {
			@Override
			public void onNext(@NonNull Object o) {
				//每一个action对应一个事件, 每一个事情对应一个或者若干个请求,
				//此处为其中的一个request执行完后的回调
				Log.d("aillean", "主线程打印: " + networkTestRequest.getTestString1());

				//此处可以进行数据保存等. 如需要更新到UI. 可以使用EventBus通知.
				mCloudDataManager.getEventbus().post(new TestEvent().setTestString(networkTestRequest.getTestString1()));
				mCloudDataManager.getEventbus().post(new TestTwoEvent().setTestString(networkTestRequest.getTestString2()));
			}
		});
	}
}
