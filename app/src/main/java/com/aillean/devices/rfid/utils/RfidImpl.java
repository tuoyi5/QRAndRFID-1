package com.aillean.devices.rfid.utils;

import android.text.TextUtils;
import android.util.Log;

import com.aillean.common.eventbus.RfidConnectInfoEvent;
import com.seuic.sleduhf.UhfDevice;

import org.greenrobot.eventbus.EventBus;

public class RfidImpl {
	private static String TAG = RfidImpl.class.getSimpleName();

	public static String open(final UhfDevice uhfDevice){
		String mc = "";
		try{
			for (int i = 0; i <3; i++) {

				Thread.sleep(800);

				mc = uhfDevice.getSledAddress();
				Log.i(TAG, "open: mc:" + mc);

				if (!TextUtils.isEmpty(mc)) {
					break;
				}
			}
			if (!TextUtils.isEmpty(mc)) {
				EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("设备打开成功"));
			} else {
				EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("设备打开失败"));
			}
		} catch (Exception ex){
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("设备打开失败"));
		}
		return mc;
	}

	public static boolean startSearchTag(final UhfDevice uhfDevice){
		boolean flg = uhfDevice.inventoryStart();
		if (flg) {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("开始寻卡操作成功"));
		} else {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("开始寻卡操作失败"));
		}
		return flg;
	}

	public static boolean stopSearchTag(final UhfDevice uhfDevice){
		boolean flg = uhfDevice.inventoryStop();
		if (flg) {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("停止寻卡操作成功"));
		} else {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("停止寻卡操作失败"));
		}
		return flg;
	}

	public static void setAccessPwd(final UhfDevice uhfDevice, RfidTag tag, String newPwd){
		byte[] epc = BaseUtil.getHexByteArray(tag.getId());
		byte[] btPassword = new byte[16];
		BaseUtil.getHexByteArray(tag.getAccessPwd(), btPassword, btPassword.length);
		int bank = 0;
		int offset = 4;
		int len = 4;
		boolean flg=uhfDevice.writeTagData(epc,btPassword,bank,offset,len,BaseUtil.getHexByteArray(newPwd.trim()));
		if (flg) {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("修改访问密码成功"));
		} else {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("修改访问密码失败"));
		}
	}

	public static void deleteAccessPwd(final UhfDevice uhfDevice, RfidTag tag){
		byte[] epc=BaseUtil.getHexByteArray(tag.getId());
		byte[] btPassword = new byte[16];
		BaseUtil.getHexByteArray(tag.getAccessPwd(), btPassword, btPassword.length);
		int bank = 0;
		int offset = 4;
		int len = 4;
		boolean flg = uhfDevice.writeTagData(epc,btPassword,bank,offset,len,BaseUtil.getHexByteArray("00000000"));
		if (flg) {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("删除访问密码成功"));
		} else {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("删除访问密码失败"));
		}
	}

	public static void setKillPwd(final UhfDevice uhfDevice, RfidTag tag){
		byte[] epc=BaseUtil.getHexByteArray(tag.getId());
		byte[] btPassword = new byte[16];
		BaseUtil.getHexByteArray(tag.getAccessPwd(), btPassword, btPassword.length);
		int bank=0;
		int offset=0;
		int len=4;
		boolean flg =false;
		try {
			flg = uhfDevice.writeTagData(epc, btPassword, bank, offset, len, BaseUtil.getHexByteArray(tag.getKillPwd().trim()));
		} catch (Exception e){e.printStackTrace();}

		if (flg) {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("修改销毁标签密码成功"));
		} else {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("修改销毁标签密码失败"));
		}
	}

	public static void deleteKillPwd(final UhfDevice uhfDevice, RfidTag tag){
		byte[] epc = BaseUtil.getHexByteArray(tag.getId());
		byte[] btPassword = new byte[16];
		BaseUtil.getHexByteArray (tag.getAccessPwd(), btPassword, btPassword.length);
		int bank = 0;
		int offset = 0;
		int len = 4;
		boolean flg = false;
		try {
			flg = uhfDevice.writeTagData(epc, btPassword, bank, offset, len, BaseUtil.getHexByteArray(tag.getKillPwd().trim()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (flg) {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("删除销毁标签密码成功"));
		} else {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("删除销毁标签密码失败"));
		}
	}

	public static String read(final UhfDevice uhfDevice, RfidTag tag){
		if (tag.getOffset()<0||tag.getLength() > getMaxLength(tag.getMemoryRegion())||
			(tag.getOffset()+tag.getLength()) > getMaxLength(tag.getMemoryRegion())){
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("写入数据溢出错误"));
			return null;
		}
		byte [] data=new byte[64];
		boolean flg= uhfDevice.readTagData(BaseUtil.getHexByteArray(tag.getId()),BaseUtil.getHexByteArray(tag.getAccessPwd()),
			tag.getMemoryRegion(),tag.getOffset(),tag.getLength(),data);
		if (flg) {
			String code = BaseUtil.getHexString(data, data.length);
			return code;
		} else {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("读取数据失败"));
		}
		return null;
	}

	public static boolean write(final UhfDevice uhfDevice, RfidTag tag, String content){
		if (tag.getOffset() < 0 || tag.getLength() > getMaxLength(tag.getMemoryRegion())||
			(tag.getOffset() + tag.getLength()) > getMaxLength(tag.getMemoryRegion())) {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("写入数据溢出错误"));
			return false;
		}
		boolean flg=uhfDevice.writeTagData (BaseUtil.getHexByteArray(tag.getId()),BaseUtil.getHexByteArray(tag.getAccessPwd()),
			tag.getMemoryRegion(),tag.getOffset(),tag.getLength(),content.getBytes());
		if (flg) {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("写入数据成功"));
			return true;
		} else {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("写入数据失败"));
		}
		return false;
	}

	public static void erase(final UhfDevice uhfDevice, RfidTag tag){
		if (tag.getOffset()<0||tag.getLength()>getMaxLength(tag.getMemoryRegion())||
			(tag.getOffset()+tag.getLength())>getMaxLength(tag.getMemoryRegion())){
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("写入数据溢出错误"));
			return;
		}
		byte [] data=new byte[64];
		boolean flg=uhfDevice.writeTagData(BaseUtil.getHexByteArray(tag.getId()),BaseUtil.getHexByteArray(tag.getAccessPwd()),
			tag.getMemoryRegion(),tag.getOffset(),tag.getLength(),data);
		if (flg) {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("写入数据成功"));
		} else {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("写入数据失败"));
		}
	}

	public static void lockTag (final UhfDevice uhfDevice, RfidTag tag, int lockCode){
		int lg;
		if (lockCode==0||lockCode==2){//可写操作，也就是解锁
			switch (tag.getMemoryRegion()){
				case 0:
					lg=0;
					break;
				case 1:
					lg=1;
					break;
				case 2:
					lg=2;
					break;
				case 3:
					lg=3;
					break;
			}
		}else {//锁标签 操作
			switch (tag.getMemoryRegion()){
				case 0:
					lg=10;
					break;
				case 1:
					lg=11;
					break;
				case 2:
					lg=12;
					break;
				case 3:
					lg=13;
					break;
			}
		}
		boolean flg = uhfDevice.lockTag(BaseUtil.getHexByteArray(tag.getId()),
			BaseUtil.getHexByteArray(tag.getAccessPwd()),lockCode);
		if (flg) {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("锁定标签成功"));
		} else {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("锁定标签失败"));
		}
	}

	public static void killTag (final UhfDevice uhfDevice, RfidTag tag){
		boolean flg = uhfDevice.killTag(BaseUtil.getHexByteArray(tag.getId()),
			BaseUtil.getHexByteArray(tag.getAccessPwd()));
		if (flg) {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("销毁标签成功"));
		} else {
			EventBus.getDefault().post(new RfidConnectInfoEvent().setMessage("销毁标签失败"));
		}
	}

	private static int getMaxLength(int paramBank) {
		switch (paramBank) {
			case 1:
				return 10;
			case 0:
				return 4;
			case 2:
				return 6;
			case 3:
				break ;
		}return 32;

	}
}
