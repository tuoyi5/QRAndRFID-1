package com.aillean.common.request;

import com.aillean.common.manager.CloudDataManager;
import com.aillean.common.manager.DataManager;
import com.aillean.tool.SoapLoader;
import com.alibaba.fastjson.JSONObject;

public class NetworkDataRequest extends BaseDataRequest {

    private String strRead;
    private SoapLoader soapLoader;

    public <T extends DataManager> NetworkDataRequest(CloudDataManager dataManager) {
        super(dataManager);
    }

    public String getStrRead() {
        return strRead;
    }

    public NetworkDataRequest setSoapLoader(SoapLoader soapLoader) {
        this.soapLoader = soapLoader;
        return this;
    }

    @Override
    public void execute() throws Exception {
        if (soapLoader != null) {
            strRead = JSONObject.toJSONString(soapLoader.Loader());
        } else {
            strRead = "Not Read";
        }
    }

}
