package com.wx.wx;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;

import com.wx.entity.Equipment;
import com.wx.entity.Open;
import com.wx.po.User;
import com.wx.service.ServletService;
import com.wx.utils.CommonUtil;
import com.wx.utils.Sha1Util;
import com.wx.utils.SpringTool;
import com.wx.utils.TokenThread;
import com.wx.utils.XmlUtil;

//findOpenId和adddevice的返回值要处理

@WebServlet("/wxTest.do")
public class WXServlet extends HttpServlet {
	
	public static final String MESSAGE_SCANCODE = "scancode_push";
	public static final String MESSAGE_SCANCODE_WAIT = "scancode_waitmsg";
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_VIEW = "VIEW";
	public static final String TEMPLATE_ID_PJ = "MEuu5PrKCiYOdNA_fKMRMTR8v5nlL5MOchBXy93ckaA";
	public static final String TEMPLATE_ID_ADD_USER = "rlABrfaA34QmUZrFVDzNfynuOLI7eR5d52UTBKLvsoo";
	private String token = "TOKEN";
	
	private static final String str[] = {"UTF-8","ToUserName","FromUserName","MsgType","Content","MsgId","CreateTime","Status","Event",
			"感谢您的关注！","取消关注删除信息","EventKey","adduser","ScanResult","40001","该用户已是一个管理员，不能再添加！","请重新添加其他用户","","CHANGE/",
			"ADD/","/","非管理员没有此权限！","adddevice"," ","该设备已有管理员！","添加成功！","添加失败！","UNSUB/PANEL/MAXC_IDF01/#","MAXC_ID"};
	private Log log = LogFactory.getLog(WXServlet.class);
	private static final long serialVersionUID = 1L;
	
	@Override
	public void init() throws ServletException {
		String menu = CommonUtil.initMenu();
		while(true) {
			JsonNode createMenu = CommonUtil.CreateMenu(TokenThread.accessToken, menu);
			try {
				if (createMenu!=null&&createMenu.get("errcode").toString().equals("0")) {
					break;
				}else {
					log.error("创建菜单失败"+createMenu);
					Thread.sleep(3000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override//公众号接入验证，固定写法
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String signature = req.getParameter("signature");
		String timestamp = req.getParameter("timestamp");
		String nonce = req.getParameter("nonce");
		String echostr = req.getParameter("echostr");
		
		String [] str = {timestamp,nonce,token};
		Arrays.sort(str);
		StringBuilder sBuilder = new StringBuilder();
		for(int a=0;a<str.length;a++) {
			sBuilder.append(str[a]);
		}
		
		String sha1 = Sha1Util.getSha1(sBuilder.toString());
		
		if(signature.equals(sha1)) {
			resp.getWriter().print(echostr);
		}
		
	}
	
	@SuppressWarnings("unused")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ServletService servletService = (ServletService)SpringTool.getBean("servletService");
		try {
			req.setCharacterEncoding(str[0]);
			resp.setCharacterEncoding(str[0]);
			
			Map<String, String> map = XmlUtil.xmlToMap(req);
			String toUserName = map.get(str[1]);
			String fromUserName = map.get(str[2]);
			String msgType = map.get(str[3]);
			String content = map.get(str[4]);
			String msgId = map.get(str[5]);
			String createTime = map.get(str[6]);
			
			if (map.containsKey(str[7])/*&&!map.get(str[7]).equals("success")*/) {
				log.info("接收消息："+map);
			}
			if(MESSAGE_TEXT.equals(msgType)) {
				if("1".equals(content)) {
					resp.getWriter().print(XmlUtil.message(XmlUtil.return1(), toUserName, msgId, msgType, fromUserName));
				}else if("2".equals(content)) {
					resp.getWriter().print(XmlUtil.message(XmlUtil.return2(), toUserName, msgId, msgType, fromUserName));
				}else if("?".equals(content)||"？".equals(content)) {
					resp.getWriter().print(XmlUtil.message(XmlUtil.moren(), toUserName, msgId, msgType, fromUserName));
				}
			}else if(MESSAGE_EVENT.equals(msgType)) {
				String eventType = map.get(str[8]);
				if(MESSAGE_SUBSCRIBE.equals(eventType)) { //订阅时（关注时）触发
					resp.getWriter().print(XmlUtil.message(str[9], toUserName, msgId, MESSAGE_TEXT, fromUserName));
				}else if (MESSAGE_UNSUBSCRIBE.equals(eventType)) {//取消订阅
					Open open = servletService.findInfoByOpenId(fromUserName);
					if (open!=null) {//判断为管理员
						List<String> list = servletService.findAllFollowerByMaxc_id2(open.getMaxc_id_list());
						servletService.deleteMenage(fromUserName);//删除设备
						for(int a=0;a<open.getMaxc_id_list().size();a++) {//取消对设备的订阅
							SendQuery.addList(str[27].replace(str[28], open.getMaxc_id_list().get(a)));
						}
						for(int a=0;a<list.size();a++) {//删除权限订阅的管理员的所有关注着对应的open缓存
							OpenQuery.delete(list.get(a));
						}
					}else {
						servletService.deleteFollower3(fromUserName);//删除关注着
						servletService.deleteMessage2(fromUserName);
						servletService.deleteOperateLog3(fromUserName);
						servletService.deleteRememberPasswordByOpenId(fromUserName);//删除关注者对子设备的密码记住情况
						servletService.deleteSetOperateByOpenId(fromUserName);//删除关注者的一键布防、撤防状态的设置
					}
					OpenQuery.delete(fromUserName);//删除权限订阅的管理员对应的open缓存
					servletService.deleteOnePassword(fromUserName);
					log.info(str[10]);
				}else if (MESSAGE_SCANCODE_WAIT.equals(eventType)) {//扫码推送事件
					String key = map.get(str[11]);
					if(key.equals(str[12])) {//添加关注者
						Open open = servletService.findInfoByOpenId(fromUserName);//查询操作添加的人是否为管理员
						
						if(open!=null) {//判断操作添加的人是否为管理员
							String openId = map.get(str[13]);//获取扫码结果（要添加的openid）
							Open boo = servletService.findInfoByOpenId(openId);//查询要被添加的openId是否为管理员
							StringBuilder sb = new StringBuilder();
							
							User user = CommonUtil.getUserInfo(TokenThread.accessToken, openId);
							if(!user.getOpenid().equals("40001")) {
								if (boo!=null) {//如果要被添加的是管理员
									OpenIdList.add(fromUserName, sb.append(str[18]).append(user.getNickname()).toString());
								}else {
									OpenIdList.add(fromUserName, sb.append(str[19]).append(user.getOpenid()).append(str[20]).append(user.getNickname()).toString());
								}
							}else {
								log.error("获取用户信息失败，导致不能添加关注者");
							}
						}else {
							resp.getWriter().print(XmlUtil.message(str[21], toUserName, msgId, MESSAGE_TEXT, fromUserName));
						}
					}else if (key.equals(str[22])) {
						String string = map.get(str[13]);
						String[] split = string.split(str[23]);
						//split[1]
						String list = servletService.findDevice(split[1]);
						if(list!=null&&!list.equals("")) {
							resp.getWriter().print(XmlUtil.message(str[24], toUserName, msgId, MESSAGE_TEXT, fromUserName));
						}else {
							Equipment equipment = new Equipment();
							equipment.setManagerId(fromUserName);
							equipment.setId(split[1]);
							equipment.setName(split[1]);
							//还没设置完
							int all = servletService.addDeviceAll(equipment,10,8);
							if(all>0) {
								resp.getWriter().print(XmlUtil.message(str[25], toUserName, msgId, MESSAGE_TEXT, fromUserName));
							}else {
								resp.getWriter().print(XmlUtil.message(str[26], toUserName, msgId, MESSAGE_TEXT, fromUserName));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		} 
	}
}
