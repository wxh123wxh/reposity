package com.wx.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.wx.entity.ArmingLog;
import com.wx.entity.Equipment;
import com.wx.entity.Follower;
import com.wx.entity.Name;
import com.wx.entity.OnePassword;
import com.wx.entity.Open;
import com.wx.entity.OperateLog;
import com.wx.entity.RePassword;
import com.wx.entity.Sector;
import com.wx.entity.SetMessage;
import com.wx.entity.SetOperate;
import com.wx.entity.Sub_Equipment;
import com.wx.po.User;
import com.wx.service.ServletService;
import com.wx.utils.CommonUtil;
import com.wx.utils.EncodeImgZxing;
import com.wx.utils.MqttUtil;
import com.wx.utils.TokenThread;
import com.wx.wx.Query;
import com.wx.wx.SendQuery;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping("/servlet")
public class ServletAction {

	private static final String statu [] = {"添加","已添加","{\"openId\":\"OPENID\"}","40001","back","jpg","image/OPENID.jpg","{\"date\":\"非管理员不能进行此操作\"}","openId","redirect:/device/device.jsp",
			"redirect:/public/error.jsp","","{\"check\":\"此编号设备已有管理员\"}","{\"check\":\"该名称已被使用\"}","{\"check\":\"yes\"}","{\"check\":\"非管理员不能进行此操作\"}",
			"{\"check\":\"您不是管理员\"}","******","{\"check\":\"no\"}","redirect:/device/SubDevice.jsp","id","yyyy-MM-dd HH:mm:ss","zid","redirect:/device/sector.jsp",
			"{\"back\":\"设置成功\"}","{\"back\":\"设置失败\"}","{\"back\":\"修改成功\"}","{\"back\":\"修改失败\"}","00","0","{\"check\":\"该名称已存在！\",\"length\":LENGTH}",
			"{\"check\":\"该编号已存在！\",\"length\":LENGTH}","{\"check\":\"yes\",\"length\":LENGTH}","{\"check\":\"您不是管理员，不能进行此操作！\",\"length\":0}",
			"{\"check\":\"不是管理员，没有此权限！\",\"length\":0}","非管理员不能进行此操作","message","GZID","GLID","ID","MAXC_ID","NAME","SUB_ID","ZID","SEC_ID","PASSWORD",
			"CLIENTID","Z_NAME","SUB_NAME","OPENID","LENGTH","SEC_NAME","{\"url\":\"/weixin/user/addUser.jsp?gzid=","{\"url\":\"/weixin/user/device.jsp?openId=","{\"url\":\"/weixin/user/user.jsp?openId=",
			"{\"url\":\"/weixin/device/device.jsp?openId=","{\"url\":\"/weixin/device/SubDevice.jsp?openId=","{\"url\":\"/weixin/operate/broken.jsp?openId=","{\"url\":\"/weixin/operate/arming_sector.jsp?openId=",
			"{\"url\":\"/weixin/operate/broken_sector.jsp?openId=","{\"url\":\"/weixin/device/sector.jsp?openId=","{\"url\":\"/weixin/operate/device_arming.jsp?openId=",
			"{\"url\":\"/weixin/operate/arming.jsp?openId=","{\"url\":\"/weixin/operate/device_broken.jsp?openId=","主设备:","&","F01/Opt/Arm/","F01/Opt/Disarm/","/00","下子设备:",
			"的防区:","{\"openId\":\"","\",\"password\":\"","\"}",":布防",":撤防","&id=","&name=","&glid=","&back=back\"}","&sub_id=","&zid=","image/",".jpg","PANEL/MAXC_IDF01/#","UNSUB/PANEL/MAXC_IDF01/#",
			"{\"back\":\"添加成功\"}","{\"back\":\"添加失败\"}"};
	private Log log = LogFactory.getLog(ServletAction.class);
	
	@Resource(name="servletService")
	private ServletService service;
	public void setService(ServletService service) {
		this.service = service;
	}
	
	/**
	 * 获取管理员open open包含openId和其对应的maxc_id集合
	 * @param openId    管理员openId
	 * @return
	 */
	public Open findById(String openId){
		Open open1 = service.findInfoByOpenId(openId);
		return open1;
	}
	
	/**
	 * 获取操作者open open包含openId和其对应的maxc_id集合
	 * @param openId    操作者openId
	 * @return
	 */
	public Open findById2(String openId){
		Open open2 = service.findidByOpenId(openId);
		return open2;
	}
	
	/**
	 * 通过openId获取对应的open（open中包含与openId对应的maxc_id集合）
	 * @param openId
	 * @return
	 */
	public Open getOpen(String openId) {
		return service.getOpen(openId);
	}
	/**
	 * 当openid对应的device有添加或者删除时重新获取对应的open
	 * @param openId  
	 */
	public void frash(String openId) {
		service.frash(openId);
	}
	
	/**
	 * 查询glid下操作者可添加操作的所有device
	 * @param glid    管理员openId
	 * @param gzid    操作者openId
	 * @param start   查询记录的开始位置
	 * @return
	 */
	@RequestMapping(value = "/findAllDeviceForUser", method = RequestMethod.POST)
	@ResponseBody
	public Object findAllDeviceForUser(String gzid,String glid,int start) {
		List<Equipment> equipment_list =  new ArrayList<Equipment>();
		List<String> list_open = new ArrayList<String>();
		Open open = getOpen(glid);//获取管理员可操作的设备maxc集合
		Open open2 = getOpen(gzid);//获取关注者可操作的设备maxc集合
		
		if (open!=null&&open.getMaxc_id_list().size()>0) {
			if (open2!=null&&open2.getMaxc_id_list().size()>0) {
				for(int a=0;a<open.getMaxc_id_list().size();a++) {
					if (!open2.getMaxc_id_list().contains(open.getMaxc_id_list().get(a))) {
						list_open.add(open.getMaxc_id_list().get(a));//将管理员maxc集合中关注者没有的添加到指定集合
					}
				}
			}else {//如果关注者没有可操作的maxc则整个管理员的maxc集合都可添加
				for(int a=0;a<open.getMaxc_id_list().size();a++) {
					list_open.add(open.getMaxc_id_list().get(a));
				}
			}
			equipment_list = service.getEquipment5(list_open,start*10);
		}
		
		if (equipment_list.size()<1) {
			return JSONObject.fromObject(statu[2].replace(statu[71], glid));
		}
		return JSONArray.fromObject(equipment_list);
	}


	/**
	 * 为多个设备添加操作者
	 * @param gzid 操作者openId
	 * @param glid 管理员openId
	 * @param arr  设备maxc_id集合
	 * @return
	 */
	@RequestMapping(value = "/addFolowerForDevice", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addFolowerForDevice(String gzid,String glid,String [] arr) {
		User user = CommonUtil.getUserInfo(TokenThread.accessToken, gzid);
		Open open = findById(gzid);//判断是否为管理员
		
		if (open!=null||user.getOpenid().equals("40001")) {//如果是管理员或者请求用户信息失败，则不可添加
			return JSONObject.fromObject(statu[87]);
		}else {
			service.addFolowerForDevice(arr, gzid, user.getNickname(), user.getHeadimgurl(), user.getCity());
			frash(gzid);//有添加删除设备的情况下刷新关注者缓存
			return JSONObject.fromObject(statu[86]);
		}
	}

	/**
	 * 通过java类生成二维码 并把用户openId存入二维码
	 * @param code    点击菜单页面时产生的唯一code码
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/getOpenId", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getOpenId(String code,HttpServletRequest req) {
		String openId = CommonUtil.getOpenId(TokenThread.appid, TokenThread.appsecret, code);
		
		if (openId!=null) {
			String contents = openId;  
			String format = statu[5]; //***此处如果格式为"gif"，则logo图片为黑色，其他格式ok  
			String realPath = req.getSession().getServletContext().getRealPath("/");//获取当前项目路径
			//生成二维码  
			StringBuilder sb = new StringBuilder();
			File img = new File(sb.append(realPath).append(statu[82]).append(openId).append(statu[83]).toString());  
			EncodeImgZxing.writeToFile(contents, format, img); //通过工具类生成二维码 
		}else {
			openId = "";
		}
		return JSONObject.fromObject(statu[2].replace(statu[49], openId));
	}
	
	/**
	 * 通过用户openId查询其对应所有设备的操作者
	 * @param code	   点击菜单页面时产生的唯一code码
	 * @param openId 点击菜单页面的使用着openid
	 * @param start  查询的开始位置
	 * @return
	 */
	@RequestMapping(value = "/findAllFollower", method = RequestMethod.POST)
	@ResponseBody
	public Object findAllFollower(String code,String openId,int start) {
		String id = null;
		if (code!=null) {
			id = CommonUtil.getOpenId(TokenThread.appid, TokenThread.appsecret, code);
		}else {
			id = openId;
		}
		
		List<Follower> list = new ArrayList<Follower>();
		if (id!=null) {//判断获取openId成功
			Open findById = getOpen(id);//获取管理员openId和maxc集合,不能通过getOpen方法获取，因为这个方法可以获取关注者信息
			
			if (findById!=null&&findById.getStyle()==1) {//如果是管理员
				List<Follower> allFollower = service.getAllFollower(findById.getMaxc_id_list(),start*10);//获取指定maxc的多个关注者
				if (allFollower.size()>0) {
					for(int a=0;a<allFollower.size();a++) {
						allFollower.get(a).setGlid(id);//为某个关注者设置管理员openId
						list.add(allFollower.get(a));
					}
				}else{//没有关注者返回管理员openId
					return statu[2].replace(statu[49], id);
				}
			}
		}
		return JSONArray.fromObject(list);
	}
	
	/**
	 * 管理员删除操作者openId对设备maxc_id的操作权限
	 * @param id  设备maxc_id
	 * @param glid	管理员openId
	 * @param openId  用户openId
	 * @return
	 */
	@RequestMapping(value = "/deleteDeviceForFollower", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteDeviceForFollower(String id,String glid,String openId) {
		StringBuilder sb = new StringBuilder();
		Open findById = getOpen(glid);
		
		if (findById!=null&&findById.getStyle()==1) {
			service.deleteDeviceForFollower(id,openId);
			frash(openId);//有设备的增删都要刷新
			return JSONObject.fromObject(sb.append(statu[53]).append(openId).append(statu[78]).append(glid).append(statu[73]).toString());
		}else {
			return JSONObject.fromObject(statu[7]);
		}
	} 
	
	/**
	 * 管理员删除操作者openId对其下所有设备的操作权限,(因为关注者可能关注不同的管理员的设备)
	 * @param openId  用户openId
	 * @param glid	 管理员openId
	 * @return
	 */
	@RequestMapping(value = "/deleteOpen", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteOpen(String openId,String glid) {
		StringBuilder sb = new StringBuilder();
		Open findById = getOpen(glid);//查询管理员
		
		if (findById!=null&&findById.getStyle()==1) {//判断管理员是否存在
			service.deleteFollower(openId,findById.getMaxc_id_list());//删除关注者对指定管理员的所有设备的操作（即该管理员删除其关注者）
			frash(openId);
			return JSONObject.fromObject(sb.append(statu[54]).append(glid).append(statu[73]).toString().toString());
		}else {
			return JSONObject.fromObject(statu[7]);
		}
	} 
	
	/**
	 * 查询操作者在对应管理员下可操作的所有设备
	 * @param openId 操作者openId
	 * @param glid	管理员openId
	 * @param start 查询开始的位置
	 * @return
	 */
	@RequestMapping(value = "/findFollowerDevice", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findFollowerDevice(String openId,String glid,int start) {
		List<Equipment> list = new ArrayList<Equipment>();
		Open open = getOpen(openId);//查询关注者
		
		if (open!=null&&open.getStyle()==0) {//判断关注者存在
			List<Equipment> equipment = service.getEquipment3(open.getMaxc_id_list(),glid,start*10);//查询关注者关注的所有设备中管理员为指定用户的设备
			for(int a=0;a<equipment.size();a++) {
				equipment.get(a).setOpenId(glid);//设置管理员openId
				list.add(equipment.get(a));
			}
		}
		return JSONArray.fromObject(list);
	} 
	
	/**
	 * 通过openId查询所有的device
	 * @param code     点击菜单页面生成的唯一code码
	 * @param openId   点击菜单页面的用户openId
	 * @param start    查询开始的位置
	 * @return
	 */
	@RequestMapping(value = "/findAllDevice", method = RequestMethod.POST)
	@ResponseBody
	public Object findAllDevice(String code,String openId,int start) {
		String id = null;
		
		if (code!=null) {
			id = CommonUtil.getOpenId(TokenThread.appid, TokenThread.appsecret, code);
		}else {
			id = openId;
		}
		
		if (id!=null) {
			Open open = getOpen(id);//获取该用户openId和maxc集合
			if (open!=null) {//判断该用户是否存在
				List<Equipment> equipment = service.getEquipment(open.getMaxc_id_list(),start*10,id);
				if (equipment.size()<1) {
					return JSONObject.fromObject(statu[2].replace(statu[49], id));
				}else {
					for(int a=0;a<equipment.size();a++) {
						equipment.get(a).setOpenId(id);//设置用户openId
					}
					return JSONArray.fromObject(equipment);
				}
			}
		}else {
			id = "";
		}
		return JSONObject.fromObject(statu[2].replace(statu[49], id));
	} 
	
	/**
	 * 删除设备
	 * @param id   设备maxc_id
	 * @param openId   用户openId
	 * @return
	 */
	@RequestMapping(value = "/deleteDevice", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteDevice(String id,String openId) {
		StringBuilder sb = new StringBuilder();
		Open findById = getOpen(openId);
		
		if(findById!=null&&findById.getStyle()==1) {
			service.deleteDevice(id);
			SendQuery.addList(statu[85].replace(statu[40], id));//删除设备同时取消mqtt对该设备的订阅
			frash(openId);
			
			List<String> list = service.findAllFollowerByMaxc_id(id);
			for(int a=0;a<list.size();a++) {
				frash(list.get(a));
			}
			return JSONObject.fromObject(sb.append(statu[55]).append(openId).append(statu[73]).toString());
		}else {
			return JSONObject.fromObject(statu[7]);
		}
	} 
	
	/**
	 * 用户更新设备名称
	 * @param id 设备maxc_id
	 * @param openId 用户openId
	 * @param name 设备要更新的名称
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateDevice", method = RequestMethod.POST)
	public String updateDevice(String id,String openId,String name,RedirectAttributes req) {
		Open findById = getOpen(openId);
		
		if (findById!=null&&findById.getStyle()==1) {//判断为管理员可更新设备名称
			service.updateDevice(id,name);
			req.addAttribute(statu[8], openId);
			return statu[9];
		}else {
			req.addAttribute(statu[36], statu[35]);
			return statu[10];
		}
	} 
	
	/**
	 * 添加设备
	 * @param id   设备maxc_id
	 * @param openId  用户openId
	 * @param name  设备名称
	 * @param sub_num 主设备数目
	 * @param sector_num 防区数目
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/addDevice", method = RequestMethod.POST)
	public String addDevice(String id,String openId,String name,int sub_num,int sector_num,RedirectAttributes req) {
		String ID = CommonUtil.addZeroForNum(id, 17);//将maxc其后补0成17个字符
		Equipment equipment = new Equipment();
		equipment.setManagerId(openId);
		User user = CommonUtil.getUserInfo(TokenThread.accessToken, openId);
    	
		if(!user.getOpenid().equals("40001")) {
			equipment.setId(ID);
			equipment.setName(name);
			equipment.setNickname(user.getNickname());
			service.addDeviceAll(equipment,sub_num,sector_num);
			
			MqttUtil.subscribe2(ID);//添加设备的同时订阅该设备
			frash(openId);
		}
		req.addAttribute(statu[8], openId);
		return statu[9];
	} 
	
	/**
	 * 检测设备id是否存在、当设备id不存在时判断对应的name是否已被使用，为添加做检测
	 * @param id  要检测的设备maxc_id
	 * @param openId 用户openId
	 * @param name 要check的名称
	 * @return
	 */
	@RequestMapping(value = "/checkName", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject checkName(String id,String openId,String name) {
		String list = service.findDevice(CommonUtil.addZeroForNum(id, 17));
		String string = null;
		
		if(list!=null) {//判断设备存在
			string = statu[12];
		}else {
			Open findById = getOpen(openId);
			if (findById!=null&&findById.getStyle()==1) {//判断是管理员
				Equipment equipment = service.getEquipmentByName(findById.getMaxc_id_list(),name);
				if (equipment!=null) {//名称已存在
					string = statu[13];
				}else {//名称补存在
					string = statu[14];
				}
			}else if(findById2(openId)!=null){//关注者不能添加
				string = statu[15];
			}else {//openId不存在，则是第一次添加添加后即为管理员
				string = statu[14];
			}
		}
		return JSONObject.fromObject(string);
	} 
	
	/**
	 * 判断要更新的设备名称是否已存在
	 * @param oldname 设备更新前的名称
	 * @param openId  用户openId
	 * @param name    设备要更新的名称
	 * @return
	 */
	@RequestMapping(value = "/checkNameUpdate", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject checkNameUpdate(String oldname,String openId,String name) {
		String string = null;
		Open findById = getOpen(openId);
		
		if (findById!=null&&findById.getStyle()==1) {//判断是否为管理员
			Equipment equipment = service.getEquipmentByName(findById.getMaxc_id_list(),name);
			if (equipment!=null) {//名称已存在
				string = statu[13];
			}else {//名称不存在
				string = statu[14];
			}
		}else {
			string = statu[16];
		}
		return JSONObject.fromObject(string);
	}
	/**
	 * 查询对应maxc_id下所有的子设备    
	 * @param id 父设备maxc_id
	 * @param openId 用户openId
	 * @param start 查询开始的位置
	 * @return
	 */
	@RequestMapping(value = "/findSubDevice", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findSubDevice(String id,String openId,int start) {
		List<Sub_Equipment> list = new ArrayList<>();
		list = service.getAllSubEquipment3(id,openId,start*10);
		Open findById = findById(openId);
		
		if(findById==null) {//判断是否为管理员，不是则把密码隐藏
			for(int b=0;b<list.size();b++) {
				list.get(b).setfPassword(list.get(b).getPassword());//保存真实密码
				list.get(b).setPassword(statu[17]);//非管理员隐藏密码
			}
		}else {
			for(int b=0;b<list.size();b++) {
				list.get(b).setfPassword(list.get(b).getPassword());//保存真实密码
			}
		}
		return JSONArray.fromObject(list);
	} 
	
	/**
	 * 通过子设备id查询设备
	 * @param id 子设备id
	 * @param openId 用户openId 通过openId判断是否为管理员，不是隐藏密码
	 * @return
	 */
	@RequestMapping(value = "/findSubDeviceById", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findSubDeviceById(long id,String openId) {
		Sub_Equipment subEquipment = service.getSubEquipment(id);
		Open findById = findById(openId);
		
		if(findById==null) {//判断是否为管理员
			subEquipment.setfPassword(subEquipment.getPassword());//保存真实密码
			subEquipment.setPassword(statu[17]);//非管理员隐藏密码
		}
		return JSONObject.fromObject(subEquipment);
	} 
	
	
	/**
	 * 通过用户openId和子设备id查询其是否记住该子设备密码，及其记住的密码
	 * @param id	子设备id
	 * @param openId 用户openId
	 * @return
	 */
	@RequestMapping(value = "/getPassword", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getPassword(long id,String openId) {
		RePassword rePassword = service.getrePassword(id,openId);
		return JSONObject.fromObject(rePassword);
		
	} 
	
	/**
	 * 通过子设备id删除子设备
	 * @param id  子设备id
	 * @param openId 用户openId
	 * @return
	 */
	@RequestMapping(value = "/deleteSubDevice", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteSubDevice(long id,String openId) {
		StringBuilder sb = new StringBuilder();
		Open findById = getOpen(openId);
		
		if (findById!=null&&findById.getStyle()==1) {//判断为管理员
			service.deleteSub_Device(id);
			service.deleteOneStatus(id);//删除子设备的同时删除所有用户对其设置的一键布防、撤防状态
			return JSONObject.fromObject(sb.append(statu[56]).append(openId).append(statu[73]).toString());
		}else {//非管理员不能删除
			return JSONObject.fromObject(statu[7]);
		}
	} 
	
	/**
	 * 添加或更新子设备时检测name和sub_id是否已经存在
	 * @param name   子设备名称
	 * @param zId   设备maxc_id
	 * @param openId 用户openId
	 * @param oldname 子设备老名称
	 * @param sub_id 子设备编码
	 * @return
	 */
	@RequestMapping(value ="/checkNameSub", method=RequestMethod.POST)
	@ResponseBody
	public JSONObject checkNameSub(String name,String zId,String openId,String oldname,int sub_id) {
		Open findById = getOpen(openId);
		List<Sub_Equipment> list = new ArrayList<Sub_Equipment>();
		String string = null;
		int flag = 0;
		
		if (findById!=null&&findById.getStyle()==1) {//判断为管理员
			list = service.getAllSubEquipment(zId,openId);
			for(int a=0;a<list.size();a++) {
				if (list.get(a).getName().equals(name)) {
					flag = 1;
					break;
				}
				if (list.get(a).getSub_id()==sub_id) {
					flag = 2;
					break;
				}
			}
			if (flag==1) {
				string = statu[30].replace(statu[50], list.size()+statu[11]);
			}else if (flag==2) {
				string = statu[31].replace(statu[50], list.size()+statu[11]);
			}else {
				string = statu[32].replace(statu[50], list.size()+statu[11]);
			}
		}else {//非管理员不能进行此操作
			string = statu[33];
		}
		return JSONObject.fromObject(string);
	} 
	
	/**
	 * 通过子设备id更新子设备
	 * @param id 要更新的子设备id
	 * @param openId 用户openID(管理员openiD 操作者openid进不了这个方法)
	 * @param zId 主设备maxc_id
	 * @param name  更新的名称
	 * @param password 更新的密码
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateSubDevice", method = RequestMethod.POST)
	public String updateSubDevice(long id,String openId,String zId,String name,String password,RedirectAttributes req) {
		Sub_Equipment sub_Equipment = new Sub_Equipment();
		sub_Equipment.setPassword(password);
		sub_Equipment.setName(name);
		sub_Equipment.setId(id);
		service.updateSub_Device(sub_Equipment);
		
		req.addAttribute(statu[8], openId);
		req.addAttribute(statu[20], zId);
		return statu[19];
	} 
	
	/**
	 * 添加子设备
	 * @param sub_id 子设备编码
	 * @param openId 用户openId（管理员openId，操作者进不了这个方法）
	 * @param name 子设备名称
	 * @param password 子设备密码
	 * @param MAXC_id 主设备maxc_id
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/addSubDevice", method = RequestMethod.POST)
	public String addSubDevice(Integer sub_id,String openId,String name,String password,String MAXC_id,RedirectAttributes req) {
		Sub_Equipment sub = new Sub_Equipment();
		sub.setSub_id((sub_id));
		sub.setMAXC_id(MAXC_id);
		sub.setName(name);
		sub.setPassword(password);
		
		Sub_Equipment sub_Equipment = service.findWetherSub_Device();
		if (sub_Equipment==null) {//不存在子设备时设置子设备id为1，想把id重新从1开始，但是发现没有必要
			sub.setId(1l);
		}
		service.addSub_Device(sub);
		
		req.addAttribute(statu[8], openId);
		req.addAttribute(statu[20], MAXC_id);
		return statu[19];
	} 
	/**
	 * 子设备布防
	 * @param password 子设备密码
	 * @param sub_id 子设备编码
	 * @param MAXC_id 主设备maxc_id
	 * @param openId 用户openID
	 * @param remember  不等于not时进行添加或者更新 rememberPassword表记录
	 * @param method 不等于""时添加rememberPassword表记录
	 * @param id
	 */
	@RequestMapping(value = "/Arming", method = RequestMethod.POST)
	@ResponseBody
	public void Arming(String password,Integer sub_id,String MAXC_id,String openId,String remember,String method,long id) {
		StringBuilder sb = new StringBuilder();
		try {
			String time = (new SimpleDateFormat(statu[21])).format(new Date());//获取指定格式日期
			Name names = service.getNames(MAXC_id,sub_id);//获取指定子设备及其主设备的名称
			Open open = getOpen(openId);
			
			if (open!=null) {
				OperateLog operateLog = new OperateLog(MAXC_id, sub_id, openId, time, sb.append(statu[64]).append(names.getName()).append(statu[69]).append(names.getSub_name()).append(statu[74]).toString(), open.getNickname());
				sb.setLength(0);
				SendQuery.addList(sb.append(password).append(statu[65]).append(CommonUtil.addZeroForNum(MAXC_id, 17)).append(statu[66]).append(toHex3(sub_id)).append(statu[68]).toString());
				Query.addList(operateLog);//把记录添加到记录队列
			}else {
				log.error("获取用户信息失败导致子设备布防失败");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		sb.setLength(0);
		rememberPassword(openId,id,remember,method);
	} 
	
	/**
	 * 防区布防
	 * @param sec_id 防区编码
	 * @param password 子设备密码
	 * @param sub_id 子设备编码
	 * @param MAXC_id 主设备maxc_id
	 * @param openId 用户openId
	 * @param remember	不等于not时进行添加或者更新 rememberPassword表记录
	 * @param method 不等于""时添加rememberPassword表记录
	 * @param id 子设备id
	 */
	@RequestMapping(value = "/sector_Arming", method = RequestMethod.POST)
	@ResponseBody
	public void sector_Arming(Integer sec_id,String password,Integer sub_id,String MAXC_id,String openId,String remember,String method,long id) {
		StringBuilder sb = new StringBuilder();
		try {
			String time = (new SimpleDateFormat(statu[21])).format(new Date());//获取指定格式日期
			Name names = service.getNames2(MAXC_id,sub_id,sec_id);//获取指定防区名称及其所属子设备和主设备名称
			Open open = getOpen(openId);
			
			if (open!=null) {	
				OperateLog operateLog = new OperateLog(MAXC_id, sub_id, openId, time, sb.append(statu[64]).append(names.getName()).append(statu[69]).append(names.getSub_name()).append(statu[70]).append(names.getSec_name()).append(statu[74]).toString(), open.getNickname());
				Query.addList(operateLog);
				sb.setLength(0);
				SendQuery.addList(sb.append(password).append(statu[65]).append(CommonUtil.addZeroForNum(MAXC_id, 17)).append(statu[66]).append(toHex3(sub_id)).append("/").append(toHex2(sec_id)).toString());
			}else {
				log.error("获取用户信息失败导致防区布防失败");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		sb.setLength(0);
		rememberPassword(openId, id, remember, method);
	} 
	
	
	/**
	 * 子设备撤防
	 * @param openId 用户openId
	 * @param sub_id 子设备编码
	 * @param MAXC_id 主设备maxc_id
	 * @param password 子设备password
	 * @param remember	不等于not时进行添加或者更新 rememberPassword表记录
	 * @param method 不等于""时添加rememberPassword表记录
	 * @param id 子设备id
	 */
	@RequestMapping(value = "/disarming", method = RequestMethod.POST)
	@ResponseBody
	public void disarming(String openId,Integer sub_id,String MAXC_id,String password,String remember,String method,long id) {
		StringBuilder sb = new StringBuilder();
		try {
			String time = (new SimpleDateFormat(statu[21])).format(new Date());
			Name names = service.getNames(MAXC_id,sub_id);
			Open open = getOpen(openId);
			
			if (open!=null) {
				OperateLog operateLog = new OperateLog(MAXC_id, sub_id, openId, time, sb.append(statu[64]).append(names.getName()).append(statu[69]).append(names.getSub_name()).append(statu[75]).toString(), open.getNickname());
				Query.addList(operateLog);
				sb.setLength(0);
				SendQuery.addList(sb.append(password).append(statu[65]).append(CommonUtil.addZeroForNum(MAXC_id, 17)).append(statu[67]).append(toHex3(sub_id)).append(statu[68]).toString());
			}else {
				log.error("获取用户信息失败导致子设备撤防失败");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		sb.setLength(0);
		rememberPassword(openId,id,remember,method);
	} 
	
	/**
	 * 防区 撤防
	 * @param sec_id  防区编码
	 * @param password 子设备password
	 * @param sub_id 子设备编码
	 * @param MAXC_id 主设备maxc_id
	 * @param openId 用户openId
	 * @param remember	不等于not时进行添加或者更新 rememberPassword表记录
	 * @param method 不等于""时添加rememberPassword表记录
	 * @param id 子设备id
	 */
	@RequestMapping(value = "/sector_disArming", method = RequestMethod.POST)
	@ResponseBody
	public void sector_disArming(Integer sec_id,String password,Integer sub_id,String MAXC_id,String openId,String remember,String method,long id) {
		StringBuilder sb = new StringBuilder();
		try {
			String time = (new SimpleDateFormat(statu[21])).format(new Date());
			Name names = service.getNames2(MAXC_id,sub_id,sec_id);
			Open open = getOpen(openId);
			
			if (open!=null) {
				OperateLog operateLog = new OperateLog(MAXC_id, sub_id, openId, time, sb.append(statu[64]).append(names.getName()).append(statu[69]).append(names.getSub_name()).append(statu[70]).append(names.getSec_name()).append(statu[75]).toString(), open.getNickname());
				Query.addList(operateLog);
				sb.setLength(0);
				SendQuery.addList(sb.append(password).append(statu[65]).append(CommonUtil.addZeroForNum(MAXC_id, 17)).append(statu[67]).append(toHex3(sub_id)).append("/").append(toHex2(sec_id)).toString());
			}else {
				log.error("获取用户信息失败导致防区 撤防失败");
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		sb.setLength(0);
		rememberPassword(openId, id, remember, method);
	} 
	
	/**
	 * 通过子设备id查询其下所有防区
	 * @param id 子设备id
	 * @return
	 */
	@RequestMapping(value = "/findAllSector", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findAllSector(Long id) {
		List<Sector> allSector = service.getAllSector2(id);
		return JSONArray.fromObject(allSector);
	} 
	
	/**
	 * 通过防区id查询该防区的信息
	 * @param id 防区id
	 * @return
	 */
	@RequestMapping(value = "/findSerctorById", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findSerctorById(Long id) {
		Sector sector = service.getSectorById(id);
		return JSONObject.fromObject(sector);
	} 
	
	/**
	 * 通过防区id删除防区
	 * @param id 防区id
	 * @param openId 用户openId
	 * @return
	 */
	@RequestMapping(value = "/deleteSector", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteSector(Long id,String openId) {
		StringBuilder sb = new StringBuilder();
		Open findById = getOpen(openId);
		
		if (findById!=null&&findById.getStyle()==1) {//判断是否为管理员
			service.deleteSector(id);
			return JSONObject.fromObject(sb.append(statu[60]).append(openId).append(statu[73]).toString());
		}else {
			return JSONObject.fromObject(statu[7]);
		}
	} 
	
	/**
	 * 添加或更新防区时检查sec_id和名称
	 * @param name 添加新防区的名称或要更新成的名称
	 * @param zId 子设备id
	 * @param openId 用户openId
	 * @param sec_id  添加新防区的编码
	 * @param oldname 更新前的名称
	 * @return
	 */
	@RequestMapping(value = "/checkNameSector", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject checkNameSector(String name,Long zId,String openId,Integer sec_id,String oldname) {
		String string = null;
		Open findById = findById(openId);
		List<Sector> list = new ArrayList<Sector>();
		
		if (findById!=null&&findById.getStyle()==1) {//判断是否为管理员
			list = service.getAllSector2(zId);
			int flag = 0;
			
			for(int a=0;a<list.size();a++) {
				if (list.get(a).getName().equals(name)) {//名称重复
					flag = 1;
				}
				if (list.get(a).getSec_id()==sec_id) {//编号重复
					flag = 2;
				}
			}
			
			if (flag==1) {
				string = statu[30].replace(statu[50], list.size()+statu[11]);
			}else if (flag==2) {
				string = statu[31].replace(statu[50], list.size()+statu[11]);
			}else{
				string = statu[32].replace(statu[50], list.size()+statu[11]);
			}
		}else {
			string = statu[33];
		}
		return JSONObject.fromObject(string);
	}
	
	/**
	 * 更新防区
	 * @param name  更新后的防区名称
	 * @param id    要更新的防区id
	 * @param openId 用户openId
	 * @param zId   子设备id
	 * @param maxc_id 设备maxc_id
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/updateSector", method = RequestMethod.POST)
	public String updateSector(String name,Long id,String openId,Integer zId,String maxc_id,RedirectAttributes req) {
		Sector sector = new Sector();
		sector.setId(id);
		sector.setName(name);
		service.updateSector(sector);
		
		req.addAttribute(statu[20], zId);//设置属性
		req.addAttribute(statu[8], openId);
		req.addAttribute(statu[22], maxc_id);
		return statu[23];//重定向
	} 
	
	/**
	 * 添加防区
	 * @param name 添加的防区名称
	 * @param openId 管理员openId
	 * @param sec_id 防区编码
	 * @param sub_id 子设备id
	 * @param maxc_id 设备maxc_id
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/addSector", method = RequestMethod.POST)
	public String addSector(String name,String openId,Integer sec_id,Long sub_id,String maxc_id,RedirectAttributes req) {
		Sector sector = new Sector();
		sector.setSec_id(sec_id);
		sector.setSub_id(sub_id);
		sector.setName(name);
		
		service.addSector(sector);
		req.addAttribute(statu[20], sub_id);
		req.addAttribute(statu[8], openId);
		req.addAttribute(statu[22], maxc_id);
		return statu[23];
	} 
	
	/**
	 * 查询报警记录     code和openId只要一个即可
	 * @param code 点击菜单页面生成的code码
	 * @param openId 用户openId
	 * @param start 查询开始的位置
	 * @return
	 */
	@RequestMapping(value = "/armingLog", method = RequestMethod.POST)
	@ResponseBody
	public Object armingLog(String code,String openId,int start) {
		String id = null;
		if (code!=null) {//code不为null通过接口获取openId，否则jsp页面传递openId
			id = CommonUtil.getOpenId(TokenThread.appid, TokenThread.appsecret, code);
		}else {
			id = openId;
		}
		
		List<ArmingLog> list = new ArrayList<ArmingLog>();
		if (id!=null) {
			Open open2 = getOpen(id);
			if (open2!=null) {
				list = service.getLog2(open2.getMaxc_id_list(),start*10);//获取指定maxc设备的报警记录
				for(int a=0;a<list.size();a++) {//为每条记录设置当前使用者openId
					list.get(a).setOpenId(id);
				}
			}
			if (list.size()<1) {
				return JSONObject.fromObject(statu[2].replace(statu[49], id));
			}
		}
		return JSONArray.fromObject(list);
	}
	
	/**
	 * 查询操作记录     code和openId只要一个即可
	 * @param code 点击菜单页面生成的code码
	 * @param openId 用户openId
	 * @param start 查询开始的位置
	 * @return
	 */
	@RequestMapping(value = "/operateLog", method = RequestMethod.POST)
	@ResponseBody
	public Object operateLog(String code,String openId,int start) {
		String id = null;
		if (code!=null) {//code不为null通过接口获取openId，否则jsp页面传递openId
			id = CommonUtil.getOpenId(TokenThread.appid, TokenThread.appsecret, code);
		}else {
			id = openId;
		}
		
		List<OperateLog> list = new ArrayList<OperateLog>();
		if (id!=null) {
			Open open2 = getOpen(id);
			if (open2!=null) {
				list = service.getOperateLog4(open2.getMaxc_id_list(),start*10);//获取指定maxc的操作记录
				for(int b=0;b<list.size();b++) {//为每条记录设置当前使用者openId
					list.get(b).setCzid(id);
				}
			}
			if (list.size()<1) {
				return JSONObject.fromObject(statu[2].replace(statu[49], id));
			}
		}
		return JSONArray.fromObject(list);
	} 
	
	/**
	 * 获取主设备或子设备的报警记录
	 * @param id 主设备maxc_id
	 * @param sub_id 子设备编码 查询主设备时为-1
	 * @param start 查询开始的位置
	 * @return
	 */
	@RequestMapping(value = "/getRZ", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray getRZ(String id,int sub_id,int start) {
		List<ArmingLog> list = service.getArmingLog(id,sub_id,start*10);
		return JSONArray.fromObject(list);
	} 
	
	/**
	 * 查询子设备操作记录
	 * @param id 主设备maxc_id
	 * @param sub_id 子设备编码
	 * @param start 查询开始的位置
	 * @return
	 */
	@RequestMapping(value = "/getsub_RZ", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray getsub_RZ(String id,int sub_id,int start) {
		List<OperateLog> list = service.getOperateLog3(id,sub_id,start*10);
		return JSONArray.fromObject(list);
	} 
	
	/**
	 * 查询用户对某个子设备的消息设置
	 * @param openId 用户openId
	 * @param sub_id 子设备id
	 * @return
	 */
	@RequestMapping(value = "/getMessage", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getMessage(String openId,Long sub_id) {
		SetMessage message = service.getMessage(openId,sub_id);
		return JSONObject.fromObject(message);
	} 
	
	/**
	 * 修改id对应的setMessage记录
	 * @param data jsp上传的数据由 "set_sub_arming","set_sub_statu","set_sub_alarm"的值组成
	 * @param id setMessage表的记录id
	 */
	@RequestMapping(value = "/setMessage", method = RequestMethod.POST)
	@ResponseBody
	public void setMessage(String data,Long id) {
		String[] split = data.split(statu[11]);
		SetMessage setMessage = new SetMessage(id, Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		service.setMessage(setMessage);
	} 
	
	/**
	 * 添加openiD对某个主设备的sertMessage设置
	 * @param data  jsp上传的数据由 "set_sub_arming","set_sub_statu","set_sub_alarm"的值组成
	 * @param openId 用户openId
	 * @param sub_id 子设备id
	 */
	@RequestMapping(value = "/insertMessage", method = RequestMethod.POST)
	@ResponseBody
	public void insertMessage(String data,String openId,long sub_id) {
		String[] split = data.split(statu[11]);
		SetMessage setMessage = new SetMessage(openId,sub_id,Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]));
		service.insertMessage(setMessage);
	} 
	
	/**
	 * 添加或者修改设备setOperate记录的布防状态   一键操作时布防是否被选中的状态
	 * @param id 主设备maxc_id
	 * @param one_arming  一键布防状态
	 * @param openId 用户openId
	 * @return
	 */
	@RequestMapping(value = "/addOneArming", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addOneArming(String id,Integer one_arming,String openId) {
		StringBuilder sb = new StringBuilder();
		SetOperate setOperate = service.getOneStatus(id,openId,0);//获取指定用户一键操作状态
		
		if (setOperate==null) {//如果未设置一键布撤防操作状态，则添加
			service.addOneStatus(id,one_arming,openId,0,0);
		}else if (setOperate.getOne_arming()!=one_arming) {//如果设置的一键状态与当前不同，则修改
			service.updateOneStatus(id,0,openId,"one_arming",one_arming);
		}
		return JSONObject.fromObject(sb.append(statu[61]).append(openId).append(statu[73]).toString());
	} 
	
	/**
	 * 添加或者修改子设备setOperate记录的布防状态  一键操作时布防是否被选中的状态
	 * @param id 子设备id
	 * @param one_arming  一键布防状态
	 * @param openId   用户openId
	 * @param maxc_id  主设备maxc_id
	 * @param name 主设备名称
	 * @return
	 */
	@RequestMapping(value = "/addSubOneArming", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addSubOneArming(long id,Integer one_arming,String openId,String maxc_id,String name) {
		StringBuilder sb = new StringBuilder();
		SetOperate setOperate = service.getOneStatus(maxc_id,openId,id);
		
		if (setOperate==null) {//如果未设置一键操作状态，则添加
			service.addOneStatus(maxc_id,one_arming,openId,0,id);
		}else if (setOperate.getOne_arming()!=one_arming) {//如果设置的一键布防状态与当前不同，则修改
			service.updateOneStatus(maxc_id,id,openId,"one_arming",one_arming);
		}
		return JSONObject.fromObject(sb.append(statu[62]).append(openId).append(statu[76]).append(maxc_id).append(statu[77]).append(name).append(statu[73]).toString());
	} 
	
	
	/**
	 * 添加或者修改设备setOperate记录的撤防状态  一键操作时撤防是否被选中的状态
	 * @param id  主设备maxc_id
	 * @param one_broken  一键撤防状态
	 * @param openId 用户openId
	 * @return
	 */
	@RequestMapping(value = "/addOneBroken", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addOneBroken(String id,Integer one_broken,String openId) {
		StringBuilder sb = new StringBuilder();
		SetOperate setOperate = service.getOneStatus(id,openId,0);
		
		if (setOperate==null) {//如果未设置一键操作状态，则添加
			service.addOneStatus(id,0,openId,one_broken,0);
		}else if (setOperate.getOne_broken()!=one_broken) {//如果设置的一键撤防状态与当前不同，则修改
			service.updateOneStatus(id,0,openId,"one_broken",one_broken);
		}
		return JSONObject.fromObject(sb.append(statu[63]).append(openId).append(statu[73]).toString());
	} 
	
	
	/**
	 * 添加或者修改子设备setOperate记录的撤防状态  一键操作时撤防是否被选中的状态
	 * @param id  子设备id
	 * @param one_broken  一键撤防状态
	 * @param openId  用户openId
	 * @param maxc_id 主设备maxc_id
	 * @param name   主设备名称
	 * @return
	 */
	@RequestMapping(value = "/addSubOneBroken", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addSubOneBroken(long id,Integer one_broken,String openId,String maxc_id,String name) {
		StringBuilder sb = new StringBuilder();
		SetOperate setOperate = service.getOneStatus(maxc_id,openId,id);
		
		if (setOperate==null) {//如果未设置一键操作状态，则添加
			service.addOneStatus(maxc_id,0,openId,one_broken,id);
		}else if (setOperate.getOne_broken()!=one_broken) {//如果设置的一键撤防状态与当前不同，则修改
			service.updateOneStatus(maxc_id,id,openId,"one_broken",one_broken);
		}
		return JSONObject.fromObject(sb.append(statu[57]).append(openId).append(statu[76]).append(maxc_id).append(statu[77]).append(name).append(statu[73]).toString());
	} 
	
	/**
	 * 查询openId对应的一键操作密码
	 * @param openId 用户openId
	 * @return
	 */
	@RequestMapping(value = "/getPasswordAll", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getPasswordAll(String openId) {
		OnePassword onePassword = service.getOnePassword(openId);
		return JSONObject.fromObject(onePassword);
	} 
	
	/**
	 * 添加或更新用户对应的一键操作密码
	 * @param remember  不等于not时进行添加或者更新 onePassword表记录
	 * @param method 不等于""时添加onePassword表记录
	 * @param openId 用户openId
	 * @param onePassword 一键操作密码
	 */
	@RequestMapping(value = "/rememberOnePassword", method = RequestMethod.POST)
	@ResponseBody
	public void rememberOnePassword(String remember,String openId,String onePassword,String method) {
		if(!remember.equals("not")) {
			if(method=="") {
				service.insertOnePassword(openId,remember,onePassword);
			}else {
				service.updateOnePassword(openId,remember);
			}
		}
	} 
	
	/**
	 * 添加或更新用户对应的记住密码情况
	 * @param openId 用户openId
	 * @param id 子设备id
	 * @param remember  不等于not时进行添加或者更新 rememberPassword表记录
	 * @param method 不等于""时添加rememberPassword表记录
	 */
	@RequestMapping(value = "/rememberPassword", method = RequestMethod.POST)
	@ResponseBody
	public void rememberPassword(String openId,long id,String remember,String method) {
		if(!remember.equals("not")) {
			if(method=="") {
				service.insertRememberPassword(openId,id,remember);
			}else {
				service.updateRememberPassword(openId,id,remember);
			}
		}
	} 
	
	/**
	 * 设备一键布防
	 * @param openId 用户openId
	 * @param remember  不等于not时进行添加或者更新 onePassword表记录
	 * @param method 不等于""时添加onePassword表记录
	 * @param onePassword 一键操作密码
	 */
	@RequestMapping(value = "/oneArming", method = RequestMethod.POST)
	@ResponseBody
	public void oneArming(String openId,String remember,String onePassword,String method) {
		StringBuilder sb = new StringBuilder();
		try {
			String time = (new SimpleDateFormat(statu[21])).format(new Date());
			Open open = getOpen(openId);
			
			if (open!=null) {
				List<Equipment> equipmentAll = service.getEquipmentAll(open.getMaxc_id_list(),openId);
				for(int a=0;a<equipmentAll.size();a++) {//遍历该用户可操作的主设备
					if(equipmentAll.get(a).getOne_arming()==1) {//如果该主设备的一键布防状态为开启状态
						for(int b=0;b<equipmentAll.get(a).getList().size();b++) {//遍历这个主设备下的子设备
							SetOperate setOperate = service.getOneStatus(equipmentAll.get(a).getId(), openId, equipmentAll.get(a).getList().get(b).getId());
							if (setOperate!=null&&setOperate.getOne_arming()==1) {//如果这个子设备设置的一键布防状态为开启
								OperateLog operateLog = new OperateLog(equipmentAll.get(a).getId(), equipmentAll.get(a).getList().get(b).getSub_id(), openId, time, sb.append(statu[64]).append(equipmentAll.get(a).getName()).append(statu[69]).append(equipmentAll.get(a).getList().get(b).getName()).append(statu[74]).toString(), open.getNickname());
								sb.setLength(0);
								
								SendQuery.addList(sb.append(equipmentAll.get(a).getList().get(b).getPassword()).append(statu[65]).append(CommonUtil.addZeroForNum(equipmentAll.get(a).getId(), 17)).append(statu[66]).append(toHex3(equipmentAll.get(a).getList().get(b).getSub_id())).append(statu[68]).toString());
								Query.addList(operateLog);//添加一键布命令
								sb.setLength(0);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		rememberOnePassword(remember, openId,onePassword,method);
	} 
	
	/**
	 * 子设备一键布防
	 * @param openId 用户openId
	 * @param maxc_id 主设备maxc_id
	 * @param name  主设备名称
	 * @param emember  不等于not时进行添加或者更新 onePassword表记录
	 * @param method 不等于""时添加onePassword表记录
	 * @param onePassword 一键操作密码
	 */
	@RequestMapping(value = "/oneSubArming", method = RequestMethod.POST)
	@ResponseBody
	public void oneSubArming(String openId,String maxc_id,String name,String remember,String onePassword,String method) {
		StringBuilder sb = new StringBuilder();
		try {
			List<Sub_Equipment> allSubEquipment = service.getAllSubEquipment(maxc_id,openId);
			String time = (new SimpleDateFormat(statu[21])).format(new Date());
			
			for(int a=0;a<allSubEquipment.size();a++) {//遍历该用户可操作的子设备，及该用户对这些子设备的一键操作设置
				if (allSubEquipment.get(a).getOne_arming()==1) {//如果该子设备的一键布防状态为开启状态
					Open open = getOpen(openId);
					
					if (open!=null) {
						OperateLog operateLog = new OperateLog(maxc_id, allSubEquipment.get(a).getSub_id(), openId, time, sb.append(statu[64]).append(name).append(statu[69]).append(allSubEquipment.get(a).getName()).append(statu[74]).toString(), open.getNickname());
						sb.setLength(0);
						
						SendQuery.addList(sb.append(allSubEquipment.get(a).getPassword()).append(statu[65]).append(CommonUtil.addZeroForNum(maxc_id, 17)).append(statu[66]).append(toHex3(allSubEquipment.get(a).getSub_id())).append(statu[68]).toString());
						Query.addList(operateLog);
						sb.setLength(0);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		rememberOnePassword(remember, openId,onePassword,method);
	} 
	
	/**
	 * 设备一键撤防
	 * @param openId  用户openId
	 * @param remember   不等于not时进行添加或者更新 onePassword表记录
	 * @param method   不等于""时添加onePassword表记录
	 * @param onePassword  一键操作密码
	 */
	@RequestMapping(value = "/oneBroken", method = RequestMethod.POST)
	@ResponseBody
	public void oneBroken(String openId,String remember,String onePassword,String method) {
		StringBuilder sb = new StringBuilder();
		try {
			String time = (new SimpleDateFormat(statu[21])).format(new Date());
			Open open = getOpen(openId);
			
			if (open!=null) {//判断该用户存在，其实不必判断，能到这个方法就一定存在
				List<Equipment> equipmentAll = service.getEquipmentAll(open.getMaxc_id_list(),openId);
				for(int a=0;a<equipmentAll.size();a++) {//遍历该用户可使用的主设备
					if(equipmentAll.get(a).getOne_broken()==1) {//如果该主设备的一键撤防状态为开启
						for(int b=0;b<equipmentAll.get(a).getList().size();b++) {//遍历该主设备下的所有子设备
							SetOperate setOperate = service.getOneStatus(equipmentAll.get(a).getId(), openId, equipmentAll.get(a).getList().get(b).getId());
							if (setOperate!=null&&setOperate.getOne_broken()==1) {//如果这个子设备的一键撤防状态为开启，就把该子设备的撤防命令加入队列
								OperateLog operateLog = new OperateLog(equipmentAll.get(a).getId(), equipmentAll.get(a).getList().get(b).getSub_id(), openId, time, sb.append(statu[64]).append(equipmentAll.get(a).getName()).append(statu[69]).append(equipmentAll.get(a).getList().get(b).getName()).append(statu[75]).toString(), open.getNickname());
								sb.setLength(0);
								
								SendQuery.addList(sb.append(equipmentAll.get(a).getList().get(b).getPassword()).append(statu[65]).append(CommonUtil.addZeroForNum(equipmentAll.get(a).getId(), 17)).append(statu[67]).append(toHex3(equipmentAll.get(a).getList().get(b).getSub_id())).append(statu[68]).toString());
								Query.addList(operateLog);
								sb.setLength(0);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		rememberOnePassword(remember,openId,onePassword,method);
	} 
	
	/**
	 * 子设备一键撤防
	 * @param openId  用户openId
	 * @param maxc_id  主设备maxc_id
	 * @param name  主设备名称
	 * @param remember  不等于not时进行添加或者更新 onePassword表记录
	 * @param onePassword  一键操作密码
	 * @param method  不等于""时添加onePassword表记录
	 */
	@RequestMapping(value = "/oneSubBroken", method = RequestMethod.POST)
	@ResponseBody
	public void oneSubBroken(String openId,String maxc_id,String name,String remember,String onePassword,String method) {
		StringBuilder sb = new StringBuilder();
		try {
			List<Sub_Equipment> allSubEquipment = service.getAllSubEquipment(maxc_id,openId);
			String time = (new SimpleDateFormat(statu[21])).format(new Date());
			
			for(int a=0;a<allSubEquipment.size();a++) {//遍历该用户可操作的子设备，及该用户对这些子设备的一键操作设置
				if (allSubEquipment.get(a).getOne_broken()==1) {//如果这个子设备的一键撤防状态为开启,就把该子设备的撤防命令加入队列
					Open open = getOpen(openId);
					
					if (open!=null) {
						OperateLog operateLog = new OperateLog(maxc_id, allSubEquipment.get(a).getSub_id(), openId, time, sb.append(statu[64]).append(name).append(statu[69]).append(allSubEquipment.get(a).getName()).append(statu[75]).toString(), open.getNickname());
						sb.setLength(0);
						
						SendQuery.addList(sb.append(allSubEquipment.get(a).getPassword()).append(statu[65]).append(CommonUtil.addZeroForNum(maxc_id, 17)).append(statu[67]).append(toHex3(allSubEquipment.get(a).getSub_id())).append(statu[68]).toString());
						Query.addList(operateLog);
						sb.setLength(0);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		rememberOnePassword(remember,openId,onePassword,method);
	} 
	
	/**
	 * 获取用户一键操作密码
	 * @param code    点击菜单时生成的code码
	 * @param openId  用户opendID
	 * @return
	 */
	@RequestMapping(value = "/getOnePassword", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getOnePassword(String code,String openId) {
		String id = null;
		if (code!=null) {//code存在则通过code获取用户openId，否则jsp传递openId
			id = CommonUtil.getOpenId(TokenThread.appid, TokenThread.appsecret, code);
		}else {
			id = openId;
		}
		
		OnePassword onePassword = null;
		if (id!=null) {
			onePassword = service.getOnePassword(id);
		}else {
			id = "";
		}
		
		StringBuilder sb = new StringBuilder();
		if (onePassword!=null) {
			return JSONObject.fromObject(sb.append(statu[71]).append(onePassword.getOpenId()).append(statu[72]).append(onePassword.getPassword()).append(statu[73]).toString());
		}else {
			return JSONObject.fromObject(statu[2].replace(statu[49], id));
		}
	} 
	
	/**
	 * 添加用户一键操作密码
	 * @param openId  用户openId
	 * @param password 用户一键操作密码
	 * @return
	 */
	@RequestMapping(value = "/insertPassword", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject insertPassword(String openId,String password) {
		int b = service.insertPassword(openId,password);
		if (b>0) {
			return JSONObject.fromObject(statu[24]);
		}else {
			return JSONObject.fromObject(statu[25]);
		}
	} 
	
	/**
	 * 修改用户一键操作密码
	 * @param openId  用户openId
	 * @param password  一键操作密码
	 * @return
	 */
	@RequestMapping(value = "/setPassword", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject setPassword(String openId,String password) {
		int b = service.setPassword(openId,password);
		if (b>=0) {
			return JSONObject.fromObject(statu[26]);
		}else {
			return JSONObject.fromObject(statu[27]);
		}
	} 
	
	/**
	 * 查询所用设备
	 * @param start
	 * @return
	 */
	@RequestMapping(value = "/findAllDevices", method = RequestMethod.POST)
	@ResponseBody
	public Object findAllDevices(int start) {
		List<Equipment> equipment = service.findAllDevices(start*10);
		if (equipment!=null) {
			return JSONArray.fromObject(equipment);
		}else {
			return JSONObject.fromObject("{\"data\":\"设置失败\"}");
		}
	} 
	
	/**
	 *修改设备overtime
	 * @param start
	 * @return
	 */
	@RequestMapping(value = "/chageOverTime", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject chageOverTime(String id,int overtime) {
		int i = service.chageOverTime(id,overtime);
		if (i>=0) {
			return JSONObject.fromObject("{\"data\":\"设置成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"设置失败\"}");
		}
	} 
	
	/**
	 * 把整形转为3位16进制字符串不足补0
	 * @param n
	 * @return
	 */
	@SuppressWarnings("static-access")
	public String toHex3(int n) {
		Integer x = n;
		String hex = x.toHexString(x);
		if (hex.length()==1) {
			hex = statu[28]+hex;
		}else if (hex.length()==2) {
			hex = statu[29]+hex;
		}
		return hex;
	}
	
	/**
	 * 把整形转为2位16进制字符串不足补0
	 * @param n
	 * @return
	 */
	@SuppressWarnings("static-access")
	public String toHex2(int n) {
		Integer x = n;
		String hex = x.toHexString(x);
		if (hex.length()==1) {
			hex = statu[29]+hex;
		}
		return hex;
	}
	
	
}
