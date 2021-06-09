package com.aillean.common.request;

import android.util.Log;

import com.aillean.common.manager.DataManager;
import com.aillean.common.manager.LocalDataManager;
import com.aillean.devices.RfidDeviceModel;
import com.seuic.uhf.EPC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RfidRequest extends BaseDataRequest {

    private RfidDeviceModel mRfidDeviceModel;

    private Map<String, String> mapOfRead = new HashMap<String, String>();

    public String getReadRfidTag() {
        return readRfidTag;
    }

    private String readRfidTag;

    public <T extends DataManager> RfidRequest(LocalDataManager dataManager) {
        super(dataManager);
    }

    public RfidRequest setRfidDeviceModel(RfidDeviceModel rfidDeviceModel) {
        mRfidDeviceModel = rfidDeviceModel;
        return this;
    }

    @Override
    public void execute() throws Exception {
        List<EPC> listOfRead = mRfidDeviceModel.readTags();
        for (EPC item : listOfRead) {
            String id = item.getId();
            if (id != null && !"".equals(id)) {
                //当放开key 不再投递，未读到数据，接着投递读请求。
                mapOfRead.put(id, id);
            }
        }

        StringBuffer strBuffer = new StringBuffer();
        for (Map.Entry<String, String> entry : mapOfRead.entrySet()) {
            strBuffer.append(entry.getKey());
            strBuffer.append("\r\n");
        }
        readRfidTag = strBuffer.toString();
    }
}
