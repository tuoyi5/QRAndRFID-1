package com.aillean.tool;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SoapLoader {
    public String ipPort;

    public String getIpPort() {
        return ipPort;
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    public String Loader() {
        Log.i("aillean", "SoapLoader.Loader()");
        HttpTransportSE httpTransportSE = new HttpTransportSE(ipPort);
        //使用SOAP1.1协议创建Envelop对象，根据服务端WebService的版本号设置SOAP协议的版本号
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //实例化SoapObject对象，第一个参数表示命名空间，第二个参数表示要调用的WebService方法名
        SoapObject soapObject = new SoapObject("http://tempuri.org/ns.xsd/Service.wsdl", "WriteAndRecv");
        //设置调用方法的参数值，如果没有参数，可以省略
        //soapObject.addProperty("theCityCode", cityName);
        envelope.bodyOut = soapObject;
        envelope.dotNet = true;
        try {
            //调用webservice
            httpTransportSE.call(ipPort, envelope);
            //获取服务器响应返回的SOAP消息
            if (envelope.getResponse() != null) {
                SoapObject resultSoap = (SoapObject) envelope.bodyIn;
                String strResult = resultSoap.getProperty(0).toString();

                return strResult;
            }
        } catch (IOException | XmlPullParserException e) {
            Log.e("aillean", "SoapLoader.e: " + e.getMessage());
            e.printStackTrace();
        }

        return "";
    }
}
