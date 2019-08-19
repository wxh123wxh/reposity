package com.wx.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.wx.po.TestMessage;


/**
 * 
 * @author Administrator
 *
 */
public class XmlUtil {

	/**
	 * 把微信公众号推送的xml格式消息解析成map
	 * @param req 	HttpServletRequest对象
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> xmlToMap(HttpServletRequest req) throws IOException, DocumentException{
		Map<String, String> map = new HashMap<String,String>();
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding("utf-8");
		
		InputStream in = req.getInputStream();
		Document read = saxReader.read(in);
		read.setXMLEncoding("utf-8");
		
		Element root = read.getRootElement();
		List<Element> list = root.elements();
		
		getEliments(list, map);
		in.close();
		return map;
	}
	
	/**
	 * 递归把xml消息中的所有元素及其值加入到map集合
	 * @param sonEliment
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	public static void getEliments(List<Element> sonEliment,Map< String, String> map){
		for(Element element :sonEliment){
			if(element.elements().size()!=0){
				getEliments(element.elements(), map);
			}else{
				map.put(element.getName(),element.getText());
			}
		}
	}
	
	/**
	 * 将设置的TestMessage对象转化成xml格式，用于微信公众号事件推送
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TestMessage textMessage) {
		XStream x = new XStream();
		//XStream.alias()将序列化中的类全量名称，用别名替换。
		x.alias("xml", textMessage.getClass());
		String xml = x.toXML(textMessage);
		return xml;
	}
	
	/**
	 * 微信公众号自动回复用户输入的1
	 * @return
	 */
	public static String return1() {
		String s = "感谢您的使用！";
		return s;
	}
	
	/**
	 * 微信公众号自动回复用户输入的2
	 * @return
	 */
	public static String return2() {
		String s = "祝您生活愉快！";
		return s;
	}
	
	/**
	 * 微信公众号自动回复用户输入的？
	 * @return
	 */
	public static String moren() {
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎关注，请按照菜单操作:\r\n");
		sb.append("1、感谢\r\n");
		sb.append("2、祝福\r\n");
		sb.append("?、调出此菜单");
		return sb.toString();
	}
	
	/**
	 * 将要推送的信息封装成微信公众号固定格式的TestMessage对象
	 * @param content	推送的消息内容
	 * @param toUserName	推送到的用户openId
	 * @param msgId			消息id，文本消息没有，媒体消息就是mediaId
	 * @param msgType		消息类型，event
	 * @param fromUserName	开发者微信号
	 * @return
	 */
	public static String message(String content,String toUserName,String msgId,String msgType,String fromUserName) {
		TestMessage t = new TestMessage();
		t.setContent(content);
		t.setCreateTime(new Date().getTime()+"");
		t.setFromUserName(toUserName);
		t.setMsgId(msgId);
		t.setMsgType(msgType);
		t.setToUserName(fromUserName);
		return XmlUtil.textMessageToXml(t);
	}
	
	
}
