package com.lokou.listenner;

public interface ConfigListenner {
	/**接收运行期间配置内容发现变化的事件**/
	public void recieveChangedKey(String key,String changedValue);
}
