package com.wx.po;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.wx.entity.ArmingLog;
import com.wx.entity.Equipment;
import com.wx.entity.HandleEquipmentMoreDisOnline;
import com.wx.entity.ReadMessage;
import com.wx.entity.Sector;
import com.wx.entity.SetMessage;
import com.wx.entity.Sub_Equipment;
import com.wx.service.ServletService;
import com.wx.utils.MqttUtil;
import com.wx.utils.SpringTool;
import com.wx.wx.HandleEquipmentMoreDisOnlineQuery;
import com.wx.wx.InitAccessTokenServlet;
import com.wx.wx.OpenIdList;
import com.wx.wx.Query;
import com.wx.wx.ReadQuery;
import com.wx.wx.SendQuery;

import net.sf.json.JSONObject;
/**
 * 自定义mqtt回调类
 * @author Administrator
 *
 */
public class MyMqttCallback implements MqttCallback{
	
	private static final String [] statu = {"UTF-8","/","该设备未添加","System","Status","set_statu/","——上线","——掉线","Zone","Alarm",":设备",
			"——报警","set_sub_arming/","防区:","AlarmRes","set_sub_statu/",":报警恢复","Offline",
			"online","——掉线","Online","——恢复","State","Armed","ZoneNum","ZoneArm","arming_status","——布防","——撤防","有",
			"个防区，布防状态分别为：","(1 撤防、0 布防)","Tamper","——被撬","TamperRes","——被撬恢复","DcLow","DC","——欠压",
			"DcRes","——欠压恢复","AcLoss","AC","——交流断电","AcRes","——交流恢复","发送给:","set_statu","PANEL/","(",
			")","yyyy-MM-dd HH:mm:ss","主设备:","的对应设备未添加!",":mqtt系统重连","set_sub_alarm/","读取消息：",":"};
	private static ServletService servletService = (ServletService)SpringTool.getBean("servletService");
	private static final Log log = LogFactory.getLog(MyMqttCallback.class);
	
	MqttClient client = null;
	public MyMqttCallback(MqttClient client) {
		this.client = client;
	}
	
	@Override//mqtt接到信息时调用
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
		String keyword1 = new String(arg1.getPayload(),statu[0]);//在mqtt信息队列获取一条UTF-8信息
		ReadQuery.addList(new ReadMessage(keyword1, arg0));
	}
	
	public static void hand(String topic,String message) {
		try {
			String[] split = topic.split(statu[1]);//将发送信息的主题以/分割成数组，便于获取设备maxc、子设备编号等信息
			String maxc_id = split[1].substring(0, 17);//前17位为设备maxc地址
			
			if (split[2].equals("OldCmd")||split[2].equals("Center")||split[2].equals("Opt")||split[2].equals("Cfg")||split[3].equals("Analog")) {
				//不处理：老设备的所有命令、中心读取设备状态命令、自己发送的布防撤防和输出命令、配置命令、自己发送的模拟量上报命令
			}else {
				log.info(new StringBuilder().append(statu[56]).append(topic).append(statu[57]).append(message));//打印读取的信息和该信息发送的主题
				Equipment Online = servletService.getDeviceOnline(maxc_id);
				
				if(Online!=null) {//判断该设备存在
					if (Online.getOnline()!=1) {//如果设备状态掉线设置其状态为在线
						servletService.changeDeviceOnline(maxc_id,1);
					}
					panel(message,Online,split,maxc_id);//信息分类处理的方法
				}else {
					log.info(statu[2]);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e); 
		}
		
	}
	
	/**
	 * 对接收的信息进行处理
	 * @param message 接收的信息
	 * @param Online  接收的信息的对应设备实例
	 * @param split   发送过来信息的topic
	 * @param maxc_id 接收的信息对应maxc_id
	 */
	public static void panel(String message,Equipment Online,String[] split,String maxc_id) {
		StringBuilder s = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		JSONObject json = JSONObject.fromObject(message);//将接收的信息转为json格式
		List<String> list = servletService.findAllFollowerAndManager(maxc_id);
		boolean flag = true;//是否加入发送队列
		
		try {
			if (split[2].equals(statu[3])&&split[3].equals(statu[4])) {//设备系统上线、掉线信息System	Status
				HandleEquipmentMoreDisOnline hand = new HandleEquipmentMoreDisOnline();
				if (json.getInt(statu[4])==1) {//Status的值为1，设备上线
					s.append(statu[5]).append(Online.getName()).append(statu[6]);
					SendQuery.addList("SysTimer&"+maxc_id+"F01");//设备上线配置系统时间到设备
					hand.setStatu(1);
				}else {
					s.append(statu[5]).append(Online.getName()).append(statu[7]);
					hand.setStatu(-1);
				}
				if (Online.getOnline()!=json.getInt(statu[4])) {//如果设备当前状态和接收的状态不同，则修改设备当前状态
					servletService.changeDeviceOnline(maxc_id,json.getInt(statu[4]));
				}
				hand.setOverTime(Online.getOvertime());
				hand.setMaxc(maxc_id);
				hand.setName(Online.getName());
				hand.setTime(System.currentTimeMillis());
				HandleEquipmentMoreDisOnlineQuery.add(hand);
				flag = false;
			} else if (split[2].equals(statu[9])&&split[3].equals(statu[8])) {//报警信息 Alarm	Zone
				if (getInt(split[5])==0) {//防区为0表示子设备
					Sub_Equipment subState = servletService.getSubState(maxc_id,getInt(split[4]));
					if (subState.getAlarm()==0) {//子设备状态为非报警，则设置其状态为报警
						servletService.updateSubState(statu[9],1,subState.getId());
					}
					s.append(statu[12]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[11]);
				}else {
					Sector sectorState = servletService.getSectorState(maxc_id,getInt(split[4]),getInt(split[5]));
					if (sectorState.getAlarm()==0) {//防区状态为非报警，则设置其状态为报警
						servletService.updateSectorState(statu[9],1,sectorState.getId());
					}
					s.append(statu[12]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[11]);
				}
			}else if (split[2].equals(statu[14])&&split[3].equals(statu[8])) {//报警恢复信息AlarmRes	Zone
				if (getInt(split[5])==0) {//防区为0表示子设备
					Sub_Equipment subState = servletService.getSubState(maxc_id,getInt(split[4]));
					if (subState.getAlarm()==1) {//子设备状态为报警，则设置其状态为非报警
						servletService.updateSubState(statu[9],0,subState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[16]);
				}else {
					Sector sectorState = servletService.getSectorState(maxc_id,getInt(split[4]),getInt(split[5]));
					if (sectorState.getAlarm()==1) {//防区状态为报警，则设置其状态为非报警
						servletService.updateSectorState(statu[9],0,sectorState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[16]);
				}
			}else if (split[2].equals(statu[9])&&split[3].equals(statu[17])) {//防区掉线信息Alarm	Offline
				if (getInt(split[5])==0) {//防区为0表示子设备
					Sub_Equipment subState = servletService.getSubState(maxc_id,getInt(split[4]));
					if (subState.getOnline()==1) {//子设备状态为在线，则设置其状态为掉线
						servletService.updateSubState(statu[18],0,subState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[19]);
				}else {
					Sector sectorState = servletService.getSectorState(maxc_id,getInt(split[4]),getInt(split[5]));
					if (sectorState.getOnline()==1) {//防区状态为在线，则设置其状态为掉线
						servletService.updateSectorState(statu[18],0,sectorState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[19]);
				}
			}else if (split[2].equals(statu[14])&&split[3].equals(statu[20])) {//防区上线信息AlarmRes	Online
				if (getInt(split[5])==0) {//防区为0表示子设备
					Sub_Equipment subState = servletService.getSubState(maxc_id,getInt(split[4]));
					if (subState.getOnline()==0) {//子设备状态为掉线，则设置其状态为在线
						servletService.updateSubState(statu[18],1,subState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[21]);
				}else {
					Sector sectorState = servletService.getSectorState(maxc_id,getInt(split[4]),getInt(split[5]));
					if (sectorState.getOnline()==0) {//防区状态为掉线，则设置其状态为在线
						servletService.updateSectorState(statu[18],1,sectorState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[21]);
				}
			}else if (split[2].equals(statu[22])&&split[3].equals(statu[23])) {//防区布撤防状态上报State	Armed
				int arm = getInt(json.getString(statu[25]));//获取ZoneArm的值：每1位二进制表示1个防区状态，0布防，1撤防。
				String string = get(arm);
				
				if (getInt(split[5])==0) {//防区为0表示子设备，此时只看arm第一个字节
					Sub_Equipment subState = servletService.getSubState(maxc_id,getInt(split[4]));
					if (subState.getArming_status()==0&&string.charAt(0)=='1') {//如果当前子设备布撤防状态与读取的状态不同，则修改
						servletService.updateSubState(statu[26],1,subState.getId());
					}else if (subState.getArming_status()==1&&string.charAt(0)=='0') {//如果当前子设备布撤防状态与读取的状态不同，则修改
						servletService.updateSubState(statu[26],0,subState.getId());
					} 
					if (string.charAt(0)=='1') {
						s.append(statu[55]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[28]);
					}else {
						s.append(statu[55]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[27]);
					}
				}else {
					Sector sectorState = servletService.getSectorState(maxc_id,getInt(split[4]),getInt(split[5]));
					if (sectorState.getArming_status()!=arm) {//当表示防区状态时，arm为1或者0
						servletService.updateSectorState(statu[26],arm,sectorState.getId());
					}
					if (arm==1) {//单个防区arm为0或者1
						s.append(statu[55]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[28]);
					}else {
						s.append(statu[55]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[27]);
					}
				}
			}else if (split[2].equals(statu[9])&&split[3].equals(statu[32])) {//防区被撬信息Alarm	Tamper
				if (getInt(split[5])==0) {//防区为0表示子设备
					Sub_Equipment subState = servletService.getSubState(maxc_id,getInt(split[4]));
					if (subState.getTamper()==0) {//如果子设备未被撬，则修改其状态为被撬
						servletService.updateSubState(statu[32],1,subState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[33]);
				}else {
					Sector sectorState = servletService.getSectorState(maxc_id,getInt(split[4]),getInt(split[5]));
					if (sectorState.getTamper()==0) {//如果防区未被撬，则修改其状态为被撬
						servletService.updateSectorState(statu[32],1,sectorState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[33]);
				}
			}else if (split[2].equals(statu[14])&&split[3].equals(statu[34])) {//防区被撬恢复AlarmRes	TamperRes
				if (getInt(split[5])==0) {//防区为0表示子设备
					Sub_Equipment subState = servletService.getSubState(maxc_id,getInt(split[4]));
					if (subState.getTamper()==1) {//如果子设备被撬，则修改其状态为未被撬
						servletService.updateSubState(statu[32],0,subState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[35]);
				}else {
					Sector sectorState = servletService.getSectorState(maxc_id,getInt(split[4]),getInt(split[5]));
					if (sectorState.getTamper()==1) {//如果防区被撬，则修改其状态为未被撬
						servletService.updateSectorState(statu[32],0,sectorState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[35]);
				}
			}else if (split[2].equals(statu[9])&&split[3].equals(statu[36])) {//防区欠压信息Alarm	DcLow
				if (getInt(split[5])==0) {//防区为0表示子设备
					Sub_Equipment subState = servletService.getSubState(maxc_id,getInt(split[4]));
					if (subState.getDC()==0) {//如果子设备非欠压，则修改其状态为欠压
						servletService.updateSubState(statu[37],1,subState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[38]);
				}else {
					Sector sectorState = servletService.getSectorState(maxc_id,getInt(split[4]),getInt(split[5]));
					if (sectorState.getDC()==0) {//如果防区非欠压，则修改其状态为欠压
						servletService.updateSectorState(statu[37],1,sectorState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[38]);
				}
			}else if (split[2].equals(statu[14])&&split[3].equals(statu[39])) {//防区欠压恢复AlarmRes	DcRes
				if (getInt(split[5])==0) {//防区为0表示子设备
					Sub_Equipment subState = servletService.getSubState(maxc_id,getInt(split[4]));
					if (subState.getDC()==1) {//如果子设备欠压，则修改其状态为非欠压
						servletService.updateSubState(statu[37],0,subState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[40]);
				}else {
					Sector sectorState = servletService.getSectorState(maxc_id,getInt(split[4]),getInt(split[5]));
					if (sectorState.getDC()==1) {//如果防区欠压，则修改其状态为非欠压
						servletService.updateSectorState(statu[37],0,sectorState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[40]);
				}
			}else if (split[2].equals(statu[9])&&split[3].equals(statu[41])) {//防区交流断电信息Alarm		AcLoss
				if (getInt(split[5])==0) {//防区为0表示子设备
					Sub_Equipment subState = servletService.getSubState(maxc_id,getInt(split[4]));
					if (subState.getAC()==0) {//如果子设备非断电，则修改其状态为断电
						servletService.updateSubState(statu[42],1,subState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[43]);
				}else {
					Sector sectorState = servletService.getSectorState(maxc_id,getInt(split[4]),getInt(split[5]));
					if (sectorState.getAC()==0) {//如果防区非断电，则修改其状态为断电
						servletService.updateSectorState(statu[42],1,sectorState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[43]);
				}
			}else if (split[2].equals(statu[14])&&split[3].equals(statu[44])) {//防区断电恢复AlarmRes	AcRes
				if (getInt(split[5])==0) {//防区为0表示子设备
					Sub_Equipment subState = servletService.getSubState(maxc_id,getInt(split[4]));
					if (subState.getAC()==1) {//如果子设备断电，则修改其状态为非断电
						servletService.updateSubState(statu[42],0,subState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(subState.getName()).append(statu[45]);
				}else {
					Sector sectorState = servletService.getSectorState(maxc_id,getInt(split[4]),getInt(split[5]));
					if (sectorState.getAC()==1) {//如果防区断电，则修改其状态为非断电
						servletService.updateSectorState(statu[42],0,sectorState.getId());
					}
					s.append(statu[15]).append(Online.getName()).append(statu[10]).append(sectorState.getSub_name()).append(statu[13]).append(sectorState.getName()).append(statu[45]);
				}
			}
			
			String sub = s.substring(0, s.indexOf(statu[1]));//  /前表示发送的信息类型:set_statu主设备信息、set_sub_arming子设备报警消息、set_sub_statu子设备状态消息、set_sub_alarm子设备布撤防消息
			String substring = s.substring(s.indexOf(statu[1])+1);// /后表示要发送的信息
			
			if(flag) {
				for(int a =0;a<list.size();a++) {//遍历该信息要发送到的用户集合
					if (!sub.equals(statu[47])) {//如果信息类型不是主设备信息
						SetMessage message2 = servletService.getMessageBySub_Id(maxc_id,getInt(split[4]),list.get(a));
						if (message2!=null) {
							JSONObject object = JSONObject.fromObject(message2);
							int int1 = object.getInt(sub);//获取该用户对指定子设备的当前信息的（1拒收 0接收）状态
							if (int1==0) {//判断对应类型的信息是否接收
								OpenIdList.add(list.get(a),sb.append(statu[48]).append(Online.getName()).append(statu[49]).append(maxc_id).append(statu[50]).append(statu[1]).append(substring).toString());
								sb.setLength(0);
							}
						}else {//未设置拒收状态，则默认接收
							OpenIdList.add(list.get(a),sb.append(statu[48]).append(Online.getName()).append(statu[49]).append(maxc_id).append(statu[50]).append(statu[1]).append(substring).toString());
							sb.setLength(0);
						}
					}else {//主设备信息都接收
						OpenIdList.add(list.get(a),sb.append(statu[48]).append(Online.getName()).append(statu[49]).append(maxc_id).append(statu[50]).append(statu[1]).append(substring).toString());
						sb.setLength(0);
					}
				}
			}
			
			//添加报警记录
			String time = (new SimpleDateFormat(statu[51])).format(new Date());
			ArmingLog armingLog = null;
			
			if (split[2].equals(statu[3])&&split[3].equals(statu[4])) {
				armingLog = new ArmingLog(maxc_id,-1,time,substring);
			}else {
				armingLog = new ArmingLog(maxc_id,getInt(split[4]),time,substring);
			}
			Query.addList(armingLog);
		} catch (Exception e) {
			log.error(e.getMessage()+":"+"设备为添加", e);
		}
	}
	
	@Override
	public void connectionLost(Throwable arg0) {
		while(InitAccessTokenServlet.flag) {//mqtt摧毁状态为未摧毁，则可以重连（隔20s一次）
			try {
				log.error("断线重连");
				Thread.sleep(20000);
				MqttUtil.connect();//如果连接成功则向下执行，否则报错不会执行break
				break;//跳出循环
			} catch (Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		
	}
	
	/**
	 * 获取16进制字符串对应的10进制值
	 * @param s 16进制字符串
	 * @return
	 */
	public static int getInt(String s) {
		String hex = s;
		Integer x = Integer.parseInt(hex,16);
		return x;
	}
	
	
	/**
	 * 判断10进制数每个2进制位上的值为0还是1   
	 * @param state		state：10进制数
	 * @return
	 */
	public static String get(int state) {
		StringBuffer buffer = new StringBuffer();  
		byte b = 0b00000001;
		
		for(int a=0;a<8;a++) {
			if ((state&b)>0) {
				buffer.append("1");
			}else {
				buffer.append("0");
			}
			b = (byte) (b<<1);
		}
		return buffer.toString();
	}
}
