package com.aillean.common.manager;

import org.greenrobot.eventbus.EventBus;

public class CloudDataManager extends DataManager{
	//可以一些缓存管理,等可以这边进行.
	public EventBus eventbus;

	public CloudDataManager(EventBus eventbus) {
		this.eventbus = eventbus;
	}

	public EventBus getEventbus() {
		return eventbus;
	}

	public void setEventbus(EventBus eventbus) {
		this.eventbus = eventbus;
	}
}



