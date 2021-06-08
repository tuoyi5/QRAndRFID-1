package com.aillean.common.action;

import android.content.Context;
import android.util.Log;

import com.aillean.common.manager.DataManager;
import com.aillean.common.manager.LocalDataManager;

public class QrAction <T extends DataManager> extends DataAction {


	public QrAction(Context context, LocalDataManager dataManager) {
		super(context, dataManager);
	}



	public void execute() {
		Log.i("aillean", "QrAction start");

	}

}
