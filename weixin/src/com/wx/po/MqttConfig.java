package com.wx.po;

import org.eclipse.paho.client.mqttv3.MqttClient;
/**
 * 保存mqtt客服端实例
 * @author Administrator
 *
 */
public class MqttConfig {

	private static MqttClient mqttClient;

	public static MqttClient getMqttClient() {
		synchronized (MqttConfig.class) {
			return mqttClient;
		}
	}

	public static void setMqttClient(MqttClient mqttClient) {
		synchronized (MqttConfig.class) {
			MqttConfig.mqttClient = mqttClient;
		}
	}
	
}
