package com.aillean.common.request;

import android.util.Log;

import com.aillean.common.manager.CloudDataManager;
import com.aillean.common.manager.DataManager;
import com.aillean.tool.SoapLoader;

public class NetworkTestRequest extends BaseDataRequest{

	private String testString1 = "yangleidejjmorengyou5cm";
	private String testString2 = "pangzidejjmorengyou500cm";
	private SoapLoader soapLoader;

	public <T extends DataManager> NetworkTestRequest(CloudDataManager dataManager) {
		super(dataManager);
	}

	public String getTestString1() {
		return testString1;
	}

	public void setTestString1(String testString1) {
		this.testString1 = testString1;
	}

	public String getTestString2() {
		return testString2;
	}

	public void setTestString2(String testString2) {
		this.testString2 = testString2;
	}

	public SoapLoader getSoapLoader() {
		return soapLoader;
	}

	public void setSoapLoader(SoapLoader soapLoader) {
		this.soapLoader = soapLoader;
	}

	@Override
	public void execute() throws Exception {
		//在此处添加所有 "有可能耗时"的打操作.
		Log.i("aillean", testString1);
		Log.i("aillean", testString2);

		setTestString1("小石头是最可爱的.");

		if (soapLoader != null) {
			setTestString2(soapLoader.Loader());
		}
		//请求执行结束(在线程中).
	}

}
