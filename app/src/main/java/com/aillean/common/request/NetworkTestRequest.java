package com.aillean.common.request;

import android.util.Log;

import com.aillean.common.manager.CloudDataManager;
import com.aillean.common.manager.DataManager;
import com.aillean.tool.ParamBean;
import com.aillean.tool.SoapLoader;
import com.alibaba.fastjson.JSONObject;

public class NetworkTestRequest extends BaseDataRequest {

    private String strRead;
    private SoapLoader soapLoader;

    public <T extends DataManager> NetworkTestRequest(CloudDataManager dataManager) {
        super(dataManager);
    }

    public String getStrRead() {
        return strRead;
    }

    public void setSoapLoader(SoapLoader soapLoader) {
        this.soapLoader = soapLoader;
    }

    @Override
    public void execute() throws Exception {
        //在此处添加所有 "有可能耗时"的打操作.
        if (soapLoader != null) {
            strRead = JSONObject.toJSONString(soapLoader.Loader());
        } else {
            strRead = "Not Read";
        }
        //请求执行结束(在线程中).
    }

}
