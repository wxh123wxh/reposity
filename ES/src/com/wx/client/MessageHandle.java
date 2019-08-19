package com.wx.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wx.entity.Key;
import com.wx.entity.KeyBox1;
import com.wx.entity.Logs;
import com.wx.entity.Name;
import com.wx.entity.UnitCard;
import com.wx.service.ServletService;
import com.wx.structure.KeyStateChangeMessage;
import com.wx.structure.SwingCardLogMessage;
import com.wx.structure.UserOperateLogMessage;
import com.wx.utils.SpringContextUtil;
import com.wx.wx.ServletContextLTest;

/**
 * 接收信息的数据处理
 * @author Administrator
 *
 */
public class MessageHandle {
	
	private static ServletService servlet = (ServletService)SpringContextUtil.getBean("servletService");
	private static Log log = LogFactory.getLog(MessageHandle.class);
	/**
	 * 修改指定箱的所有钥匙状态
	 * @param data 钥匙状态数组，每个元素表示4把钥匙状态
	 * @param maxc 箱maxc地址
	 */
	public static void updateAllKeyState(byte[] data,String maxc) {
		List<Key> changeList = new ArrayList<Key>();
		List<Key> keys = servlet.findKeysByMaxc(maxc);//查询箱下所有钥匙
		
		for(int a=0;a<keys.size();a++) {//遍历钥匙，找到在数组中的对应位置
			int iid = keys.get(a).getIid();
			int x = iid/4;
			int y = iid%4;
			int state = 4;
			
			if (y==0) {//每一个字节代表4个钥匙的状态，分别从高到底
				state = data[x]&0b00000011;//字节最低两位
			}else if (y==1) {
				state = (data[x]&0b00001100)>>>2;//字节中低两位
			}else if (y==2) {
				state = (data[x]&0b00110000)>>>4;//字节中高两位
			}else {
				state = (data[x]&0b11000000)>>>6;//字节最高两位
			}
			
			if (state!=keys.get(a).getKeyss_state()&&state!=4) {//钥匙状态和上报的不同则修改
				keys.get(a).setKeyss_state(state);
				changeList.add(keys.get(a));
			}
		}
		
		if (changeList.size()>0) {//统一修改钥匙状态
			servlet.changekeysState(changeList);
		}
	}
	
	/**
	 * 添加刷卡操作记录
	 * @param search  缓存中的箱信息
	 * @param message 接收的刷卡信息
	 */
	public static void addCardLog(KeyBox1 search,SwingCardLogMessage message) {
		log.info("进入添加刷卡操作记录方法");
		UnitCard findCard = servlet.findCardByNumber(search.getUnit_id(), message.getCard_number());//通过编号查询卡
		log.info("findCard:"+findCard.toString());
		
		if (findCard!=null){//如果卡存在
			String keyname = servlet.findKeyNameByIid(search.getMaxc(),message.getKey_iid());//查询钥匙名称
			Name name = servlet.findName(search.getMaxc());//查询箱所在单位、班组及箱的名称
			char c = message.getTime().charAt(1);//表示时间的字符串的第二个字符，即年份的第二位
			log.info("c:"+c);
			if (message.getStyle()==1) {//申请刷卡
				if (c=='1') {//>2100表示历史记录
					String time = message.getTime().replaceFirst("1", "0");
					servlet.addLogs(new Logs(message.getKey_iid(), -1, "--", -1, "--", time, "--", "--", "申请刷卡开锁", name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), message.getCard_number(), 1));
				}else {
					servlet.addLogs(new Logs(message.getKey_iid(), -1, "--", -1, "--", message.getTime(), "--", "--", "申请刷卡开锁", name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), message.getCard_number(), 0));
					ServletContextLTest.keyboxList.updateLogNumber(search.getMaxc(), search.getLogNumber()-1);
				}
			}else if (message.getStyle()==2) {//授权刷卡
				UnitCard unitCard = servlet.findUnitCardByNumber(message.getCard_number(), search.getUnit_id());
				log.info("unitCard:"+unitCard.toString());
				
				if (c=='1') {//>2100表示历史记录
					String time = message.getTime().replaceFirst("1", "0");
					servlet.addLogs(new Logs(message.getKey_iid(), -1, "--", unitCard.getAuth_id(), unitCard.getAuth_name(), "--", time, "--", "授权刷卡开锁", name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), message.getCard_number(), 1));
				}else {
					log.info("添加记录前");
					servlet.addLogs(new Logs(message.getKey_iid(), -1, "--", unitCard.getAuth_id(), unitCard.getAuth_name(), "--", message.getTime(), "--", "授权刷卡开锁", name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), message.getCard_number(), 0));
					log.info("添加记录后");
					ServletContextLTest.keyboxList.updateLogNumber(search.getMaxc(), search.getLogNumber()-1);
				}
			}
		}
	}
	
	
	/**
	 * 添加用户操作记录
	 * @param search  缓存中的箱信息
	 * @param message 接收的刷卡信息
	 */
	public static void addUserLog(KeyBox1 search,UserOperateLogMessage message) {
		Name name = servlet.findName(search.getMaxc());
		String keyname = servlet.findKeyNameByIid(search.getMaxc(),message.getKey_iid());
		char c = message.getTime().charAt(1);
		
		if (message.getStyle()==2) {//授权开锁
			String ManagerName = null;
			if (message.getUser_iid()<32) {//0——31单位管理员
				ManagerName = servlet.findUnitManagerNameByIid(search.getUnit_id(),message.getUser_iid());
			}else {//班组管理员
				ManagerName = servlet.findTeamManagerNameByIid(search.getUnit_id(),search.getTeam_id(),message.getUser_iid());
			}
			
			if (ManagerName!=null) {
				if (c=='1') {//年份第二个为1表示年大于2100
					String time = message.getTime().replaceFirst("1", "0");
					servlet.addLogs(new Logs(message.getKey_iid(), -1, "--", message.getUser_iid(), ManagerName, "--", time, "--", "授权密码开锁", name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), "--", 1));
				}else {
					servlet.addLogs(new Logs(message.getKey_iid(), -1, "--", message.getUser_iid(), ManagerName, "--", message.getTime(), "--", "授权密码开锁", name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), "--", 0));
					ServletContextLTest.keyboxList.updateLogNumber(search.getMaxc(), search.getLogNumber()-1);
				}
			}
		}else if (message.getStyle()==4||message.getStyle()==1) {
			String userName = servlet.findUserNamebyIid(search.getUnit_id(),search.getTeam_id(),message.getUser_iid());
			String style = "";
			
			if (message.getStyle()==4) {
				style = "申请归还钥匙";
			}else {
				style = "申请开锁";
			}
			if (userName!=null) {
				if (c=='1') {//年份第二个为1表示年大于2100
					String time = message.getTime().replaceFirst("1", "0");
					servlet.addLogs(new Logs(message.getKey_iid(), message.getUser_iid(), userName, -1, "--", time, "--", "--", style, name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), "--", 1));
				}else {
					servlet.addLogs(new Logs(message.getKey_iid(), message.getUser_iid(), userName, -1, "--", message.getTime(), "--", "--", style, name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), "--", 0));
					ServletContextLTest.keyboxList.updateLogNumber(search.getMaxc(), search.getLogNumber()-1);
				}
			}
		}
	}
	
	/**
	 * 添加钥匙状态改变记录
	 * @param search  缓存中的箱信息
	 * @param message 接收的刷卡信息
	 */
	public static void updataKeyState(KeyBox1 search,KeyStateChangeMessage message) {
		Name name = servlet.findName(search.getMaxc());
		String keyname = servlet.findKeyNameByIid(search.getMaxc(),message.getKey_iid());
		char c = message.getTime().charAt(1);
		
		if (keyname!=null) {//钥匙存在
			if (message.getStyle()==1) {//取走钥匙
				if (c=='1') {//年份第二个为1表示年大于2100
					String time = message.getTime().replaceFirst("1", "0");
					servlet.addLogs(new Logs(message.getKey_iid(), -1, "--", -1, "--", "--", time, "--", "取走钥匙", name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), "--", 1));
				}else {
					servlet.addLogs(new Logs(message.getKey_iid(), -1, "--", -1, "--", "--", message.getTime(), "--", "取走钥匙", name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), "--", 0));
					servlet.updateKeyStateByIid(search.getMaxc(), message.getKey_iid(), 3-message.getStyle());
					ServletContextLTest.keyboxList.updateLogNumber(search.getMaxc(), search.getLogNumber()-1);
				}
			}else if (message.getStyle()==2) {//归还钥匙
				if (c=='1') {//年份第二个为1表示年大于2100
					String time = message.getTime().replaceFirst("1", "0");
					servlet.addLogs(new Logs(message.getKey_iid(), -1, "--", -1, "--", "--", "--", time, "归还钥匙", name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), "--", 1));
				}else {
					servlet.addLogs(new Logs(message.getKey_iid(), -1, "--", -1, "--", "--", "--", message.getTime(), "归还钥匙", name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyname, search.getUnit_id(), search.getTeam_id(), search.getMaxc(), "--", 0));
					servlet.updateKeyStateByIid(search.getMaxc(), message.getKey_iid(), 3-message.getStyle());
					ServletContextLTest.keyboxList.updateLogNumber(search.getMaxc(), search.getLogNumber()-1);
				}
			}
		}
	}
}
