package com.aillean.common.model;

import android.content.Context;

import com.aillean.common.manager.CloudDataManager;

import org.greenrobot.eventbus.EventBus;

public class DataBundle {
	//此类为与UI解偶的控制类, 用于处理事件发起,
	//用于Manager的初始化,管理.
	//用于一些数据模型的初始化,管理.
	private Context appContext;
	private CloudDataManager mCloudDataManager;
	private EventBus eventBus;

	//private AilleanModel ailleanModel; 这样.

	public DataBundle(Context appContext) {
		appContext = appContext;
		//ailleanModel = new AilleanModel();
		eventBus = new EventBus();
		mCloudDataManager = new CloudDataManager(eventBus);
	}

	public Context getAppContext() {
		return appContext;
	}

	public void setAppContext(Context appContext) {
		this.appContext = appContext;
	}

	public CloudDataManager getCloudDataManager() {
		return mCloudDataManager;
	}

	public void setCloudDataManager(CloudDataManager cloudDataManager) {
		mCloudDataManager = cloudDataManager;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	/*	public AilleanModel getAilleanModel() {
		return ailleanModel;
	}

	public void setAilleanModel(AilleanModel ailleanModel) {
		this.ailleanModel = ailleanModel;
	}

	public AilleanModel getAilleanModel() {
		return ailleanModel;
	}

	public void setAilleanModel(AilleanModel ailleanModel) {
		this.ailleanModel = ailleanModel;
	}*/


}
