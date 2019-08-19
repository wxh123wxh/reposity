package com.wx.entity;

public class ReadMessage {

	private String message;
	private String topic;
	
	
	public ReadMessage() {
		
	}
	
	public ReadMessage(String message, String topic) {
		this.message = message;
		this.topic = topic;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	@Override
	public String toString() {
		return "ReadMessage [message=" + message + ", topic=" + topic + "]";
	}
}
