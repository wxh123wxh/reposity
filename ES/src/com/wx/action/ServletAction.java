package com.wx.action;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wx.client.KeyBox1List;
import com.wx.client.UDPClient;
import com.wx.entity.Card;
import com.wx.entity.Config;
import com.wx.entity.Key;
import com.wx.entity.KeyBox;
import com.wx.entity.KeyBox1;
import com.wx.entity.KeyBoxInfo;
import com.wx.entity.Logs;
import com.wx.entity.Manager;
import com.wx.entity.Name;
import com.wx.entity.UnitManager;
import com.wx.entity.Users;
import com.wx.entity.Team;
import com.wx.entity.TeamManager;
import com.wx.entity.Unit;
import com.wx.entity.UnitCard;
import com.wx.service.ServletService;
import com.wx.utils.ExcelUtil;
import com.wx.wx.ServletContextLTest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 配置ip前设置到班组的授权卡要通过配置卡命令下发到箱
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/ESServlet")
public class ServletAction {

	private Log log = LogFactory.getLog(ServletAction.class);
	
	@Resource(name="servletService")
	private ServletService service;
	public void setService(ServletService service) {
		this.service = service;
	}
	
	//===========对单位的操作
	/**
	 * 通过id修改单位name
	 * @param change_name 修改后的单位name
	 * @param change_id 要修改的单位id
	 * @return
	 */
	@RequestMapping(value = "/changeUnitById", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeUnitById(String change_name,long change_id) {
		Unit unit = service.findUnitByName(change_name);//查询要修改成的单位名称是否已存在
		
		if (unit!=null) {
			return JSONObject.fromObject("{\"data\":\"已存在\"}");
		}else {
			int i = service.changeUnitById(change_id,change_name);
			
			if(i>=0) {
				return JSONObject.fromObject("{\"data\":\"修改成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"修改失败\"}");
			}
		}
	}
	/**
	 * 添加单位
	 * @param name 要添加的单位name
	 * @return
	 */
	@RequestMapping(value = "/addUnit", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addUnit(String name) {
		Unit unit = service.findUnitByName(name);//查询要添加的单位是否存在
		
		if (unit!=null) {
			return JSONObject.fromObject("{\"data\":\"已存在\"}");
		}else {
			String uuid = getMACAddress();//获取uuid
			Unit u = new Unit(name,uuid);
			int i = service.addUnit(u);
			
			if(i>=1) {
				return JSONObject.fromObject("{\"data\":\"添加成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"添加失败\"}");
			}
		}
	}
	
	/**
	 * 通过name查询单位
	 * @param name 要查询的单位name
	 * @return
	 */
	@RequestMapping(value = "/findUnitByName", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findUnitByName(String name) {
		Unit unit = service.findUnitByName(name);
		
		if (unit==null) {
			return JSONObject.fromObject("{\"data\":\"不存在\"}");
		}else {
			return JSONObject.fromObject(unit);
		}
	}
	
	/**
	 * 查询该软件所在的单位
	 * 因为只能存在一个单位，只要在库中查出即可
	 * @return
	 */
	@RequestMapping(value = "/findUnit", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findUnit() {
		Unit findUnit = service.findUnit();
		
		if (findUnit!=null) {
			return JSONObject.fromObject(findUnit);
		}else {
			return JSONObject.fromObject("{\"data\":\"0\"}");
		}
	}
	/**
	 * 通过id删除单位
	 * @param ids 单位id数组
	 * @return
	 */
	@RequestMapping(value = "/deleteUnitByIds", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteUnitByIds(String [] unit_numbers) {
		int i = service.deleteUnitByIds(unit_numbers);

		if(i>=0) {
			//删除单位后清空箱的缓存
			KeyBox1List list = ServletContextLTest.keyboxList;
			list.clear();
			return JSONObject.fromObject("{\"data\":\"删除成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"删除失败\"}");
		}
	}
	
	//===========对班组的操作
	/**
	 * 通过iid删除班组
	 * @param iids 班组iid数组
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/deleteTeamByIds", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteTeamByIds(int [] iids,String unit_id) {
		int i = service.deleteTeamByIds(iids,unit_id);
		
		if(i>=0) {
			//删除班组后清除箱的缓存中对应班组下的所有箱
			Arrays.sort(iids);
			KeyBox1List list = ServletContextLTest.keyboxList;
			list.remove(iids);
			return JSONObject.fromObject("{\"data\":\"删除成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"删除失败\"}");
		}
	}
	
	/**
	 * 修改单位管理员可操作开锁的班组
	 * @param unit_manager_id 单位管理员编号
	 * @param iids 班组iid数组
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/addUnitManagerAuth", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addUnitManagerAuth(int unit_manager_id,Integer [] iids,String unit_id) {
		List<Integer> auths = service.findAllUnitManagerAuth(unit_manager_id,unit_id);//查询单位管理员可操作的班组
		List<Integer> list = new ArrayList<Integer>();
		
		for(int a=0;a<iids.length;a++) {
			if (auths.contains(iids[a])) {//对比修改前后可操作的班组数组，把修改后没有的保留在auths数组中，然后统一到数据库删除
				auths.remove(iids[a]);
			}else if(iids[a]!=-1){//对比修改前后可操作的班组数组，把修改后新增的加入到list数组之后统一添加到数据库
				list.add(iids[a]);
			}
		}
		int i = service.addUnitManagerAuth(unit_manager_id,list,auths,unit_id);
		
		if (i!=9999) {
			return JSONObject.fromObject("{\"data\":\"设置成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"没有改动或设置失败\"}");
		}
	}
	
	/**
	 * 添加班组
	 * @param name 班组name
	 * @param unit_id 单位唯一编号
	 * @param iid 班组iid
	 * @return
	 */
	@RequestMapping(value = "/addTeam", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addTeam(String name,String unit_id,int iid) {
		Team team = service.findTeamByNameOrIid(name,unit_id,iid);//查询要添加的班组名称或者iid是否已存在
		
		if (team!=null) {
			return JSONObject.fromObject("{\"data\":\"名称或编号已存在\"}");
		}else {
			int count = service.findTeamCount(unit_id);//查询班组总数
			
			if (count>=32) {
				return JSONObject.fromObject("{\"data\":\"限制最多32个\"}");
			}else {
				Team u = new Team(unit_id,name,iid);
				int i = service.addTeam(u);
				
				if(i>=1) {
					return JSONObject.fromObject(u);
				}else {
					return JSONObject.fromObject("{\"data\":\"添加失败\"}");
				}
			}
		}
	}
	
	/**
	 * 查询班组并标识是否可被指定单位管理员操作
	 * @param unit_id 单位唯一编号
	 * @param start 查询开始的位置
	 * @param unit_manager_id 单位管理员编号
	 * @return
	 */
	@RequestMapping(value = "/findTeamByAuth", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findTeamByAuth(String unit_id,int start,int unit_manager_id) {
		List<Team> findTeam = service.findTeam(unit_id,start*20);//查询单位下班组
		List<Integer> auths = service.findAllUnitManagerAuth(unit_manager_id,unit_id);//查询指定单位管理员可操作的班组
		
		for(int a=0;a<findTeam.size();a++) {
			if (auths.contains(findTeam.get(a).getIid())) {
				findTeam.get(a).setFlage(true);//如果该班组在管理员的可操作班组数组中标记为true
			}
		}
		return JSONArray.fromObject(findTeam);
	}
	
	/**
	 * 查询班组
	 * @param start 查询开始位置
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/findTeam", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findTeam(int start,String unit_id) {
		List<Team> findTeam = service.findTeam(unit_id,start*20);
		return JSONArray.fromObject(findTeam);
	}
	
	//===========对站点的操作
	/**
	 * 通过maxc查询箱
	 * @param keybox_id 箱maxc
	 * @param unit_id 单位唯一编号
	 * @param team_id 班组编号
	 * @return
	 */
	@RequestMapping(value = "/findKeyBoxById", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findKeyBoxById(String keybox_id,String unit_id,int team_id) {
		KeyBoxInfo findKeyBox = service.findKeyBoxById(keybox_id);
		return JSONObject.fromObject(findKeyBox);
	}
	
	/**
	 * 通过名称查询箱
	 * @param name 箱名称
	 * @param team_id 所在班组编号
	 * @param unit_id单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/findKeyBoxByName", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findKeyBoxByName(String name,int team_id,String unit_id) {
		KeyBox findKeyBox = service.findKeyBoxByName(name,team_id,unit_id);
		if (findKeyBox!=null) {
			return JSONObject.fromObject(findKeyBox);
		}else {
			return JSONObject.fromObject("{\"data\":\"不存在\"}");
		}
	}
	
	/**
	 * 通过id修改箱name
	 * @param name 箱name
	 * @param id 箱id
	 * @param team_id 班组编号
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/changeKeyBoxById", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeKeyBoxById(String name,long id,int team_id,String unit_id) {
		KeyBox findKeyBox = service.findKeyBoxByName(name,team_id,unit_id);//判断要修改成的箱名称是否已存在
		
		if (findKeyBox!=null) {
			return JSONObject.fromObject("{\"data\":\"已存在\"}");
		}else {
			int i = service.changeKeyBoxById(name,id);
			
			if (i>=0) {
				return JSONObject.fromObject("{\"data\":\"修改成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"修改失败\"}");
			}
		}
	}
	/**
	 * 通过maxc删除箱
	 * @param maxcs 箱maxc数组
	 * @return
	 */
	@RequestMapping(value = "/deleteKeyBoxByIds", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteKeyBoxByIds(String[] maxcs) {
		int i = service.deleteKeyBoxByIds(maxcs);
		
		if(i>=1) {
			//删除箱后删除其在内存中的缓存
			KeyBox1List list = ServletContextLTest.keyboxList;
			list.remove(maxcs);
			return JSONObject.fromObject("{\"data\":\"删除成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"删除失败\"}");
		}
	}
	
	/**
	 * 添加箱
	 * @param name 箱name
	 * @param team_id 班组编号
	 * @param maxc 箱maxc
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/addKeyBox", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addKeyBox(String name,int team_id,String maxc,String unit_id) {
		int flage = 0;
		String findMaxc = service.findMaxc(maxc);//查询整个箱数据库中指定箱
		List<KeyBox> list = service.findKeyBoxs(team_id,unit_id);//查询指定单位、班组下所有箱
		
		for(int a=0;a<list.size();a++) {
			if (list.get(a).getName().equals(name)) {//判断要添加的箱的名称是否已存在
				flage = 1;
				break;
			}
		}
		
		if (findMaxc!=null) {//判断指定箱是否已存在
			flage = 2;
		}
		
		if (flage==0) {
			if (list.size()>=64) {//判断班组下箱是否达到极限
				return JSONObject.fromObject("{\"data\":\"限制最多64个\"}");
			}else {
				KeyBox1 keyBox = new KeyBox1(team_id,unit_id,name,maxc);
				long i = service.addKeyBox(keyBox);
								
				if (i>=0) {
					//添加箱后将其加入缓存
					KeyBox1List keyboxList = ServletContextLTest.keyboxList;
					keyboxList.add(new KeyBox1(i, team_id, unit_id, maxc,"掉线",""));
					return JSONObject.fromObject("{\"data\":\"添加成功\"}");
				}else {
					return JSONObject.fromObject("{\"data\":\"添加失败\"}");
				}
			}
		}else if(flage==1){
			return JSONObject.fromObject("{\"data\":\"名称已存在\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"maxc地址已存在\"}");
		}
	}
	
	/**
	 * 配置箱ip等参数,并设置back为0，单设备回复是back为1，这时页面的定时back方法停止，并返回配置成功
	 * @return
	 */
	@RequestMapping(value = "/updateKeyBox", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject updateKeyBox(String [] arr,String keybox_id,String unit_id,int team_id) {
		KeyBox1List list = ServletContextLTest.keyboxList;
		KeyBox1 keyBox1 = list.get(keybox_id);
		long millis = System.currentTimeMillis();
		long time = keyBox1.getOperateTime();//获取上一次操作（获取远程记录、清空远程记录、获取远程所有钥匙状态）的时间
		
		if (keyBox1!=null&&millis-time>60*1000) {
			KeyBoxInfo keyBox = new KeyBoxInfo(keybox_id, arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], Integer.parseInt(arr[8]), Integer.parseInt(arr[9]), Integer.parseInt(arr[10]), Integer.parseInt(arr[11]), Integer.parseInt(arr[12]), Integer.parseInt(arr[13]), Integer.parseInt(arr[14]), Integer.parseInt(arr[15]), team_id, unit_id);
			ServletContextLTest.keyboxList.updateGetAllBack(keybox_id, 2);//设置back为0
			service.updateKeyBox(keyBox);
			list.updateLogTime(keybox_id, millis);
			return JSONObject.fromObject("{\"data\":\"命令已发送\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"不要频繁操作,请等待60s\"}");
		}
	}
	
	/**
     * 返回配置ip结果，配置ip后页面定时检测back的值，当back为1时配置成功
     * @return
     */
    @RequestMapping(value = "/getBack", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getBack(String keybox_id) throws Exception {
    	KeyBox1List list = ServletContextLTest.keyboxList;
    	KeyBox1 keyBox1 = ServletContextLTest.keyboxList.get(keybox_id);
    	int getBack = keyBox1.getGetAllBack();//获取指定箱的当前back状态
    	log.info("当前GetAllBack:"+getBack);
    	
    	if (getBack==1) {
    		list.updateLogTime(keybox_id, keyBox1.getOperateTime()-1000*30);//命令成功下一次发送的等待时间缩短30秒
    		return JSONObject.fromObject("{\"data\":\"配置成功\"}");
		}else {
			return JSONObject.fromObject("{\"check\":\"配置失败\",\"length\":"+getBack+"}");
		}
    }

	
	/**
	 * 查询班组下的所有箱
	 * @param team_id 班组编号
	 * @param start 查询开始的位置
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/findKeyBox", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findKeyBox(int team_id,int start,String unit_id) {
		List<KeyBox> findKeyBox = service.findKeyBox(team_id,start*20,unit_id);
		return JSONArray.fromObject(findKeyBox);
	}
	
	//===========对单位卡的操作
	/**
	 * 通过iid删除授权卡
	 * @param iids 授权卡iid数组
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/deleteUnitCards", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteUnitCards(int [] iids,String unit_id) {
		List<Integer> list = new ArrayList<Integer>();  
		for (int i = 0; i < iids.length; i++) { //将iids数组的值转到集合中
		    list.add(iids[i]);  
		}  
		int i = service.deleteUnitCards(list,unit_id);
		
		if(i>=1) {
			return JSONObject.fromObject("{\"data\":\"删除成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"删除失败\"}");
		}
	}
	
	/**
	 * 添加授权卡
	 * @param card_number 授权卡卡号
	 * @param unit_id 单位唯一编号
	 * @param iid 授权卡编号
	 * @return
	 */
	@RequestMapping(value = "/addUnitCard", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addUnitCard(String card_number,String unit_id,int iid) {
		int count = service.findAllUnitCardCount(unit_id);//查出单位卡的总数
		List<UnitCard> list = service.findCardByNumberOrIid(unit_id,card_number,iid);//查询卡号为card_number或者编号为iid的所有卡
		
		int flag = 0;
		if(list.size()>0) {//大于0表示该卡号或者编号已存在
			flag = 1;
		}
		
		if (count>=80) {
			return JSONObject.fromObject("{\"data\":\"授权卡最多80张\"}");
		}else if (flag==1) {
			return JSONObject.fromObject("{\"data\":\"卡号或编号已存在\"}");
		}else {
			int i = service.addUnitCard(card_number,unit_id,iid);
			
			if(i>0) {
				return JSONObject.fromObject("{\"data\":\"添加成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"添加失败\"}");
			}
		}
	}
	
	/**
	 * 查询单位下授权卡
	 * @param unit_id 单位唯一编号
	 * @param start 查询开始的位置
	 * @return
	 */
	@RequestMapping(value = "/findUnitCard", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findUnitCard(String unit_id,int start) {
		List<UnitCard> findCard = service.findUnitCard(unit_id,start*20);
		return JSONArray.fromObject(findCard);
	}
	
	/**
	 * 通过卡号查询授权卡
	 * @param card_number 授权卡卡号
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/findUnitCardByNumber", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findUnitCardByNumber(String card_number,String unit_id) {
		UnitCard findCard = service.findUnitCardByNumber(card_number,unit_id);
		
		if (findCard==null) {
			return JSONObject.fromObject("{\"data\":\"不存在\"}");
		}else {
			return JSONObject.fromObject(findCard);
		}
	}
	
	/**
	 * 授权卡修改授权人
	 * @param iid 授权卡编号
	 * @param auth_id 修改后的管理员编号
	 * @param auth_name 修改后的管理员name
	 * @param card_number 授权卡卡号
	 * @param style 管理员类型unit或team
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/changeAuth", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeAuth(int iid,int auth_id,String auth_name,String card_number,int style,String unit_id) {
		int i = service.changeAuth(iid,auth_id,auth_name,card_number,style,unit_id);
		
		if (i>=0) {
			return JSONObject.fromObject("{\"data\":\"设置成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"设置失败\"}");
		}
	}
	
	/**
	 * 修改单位下指定卡号的卡的卡号
	 * @param change_number修改前的卡号
	 * @param unit_id单位唯一编码
	 * @param card_number修改后的卡号
	 * @param iid	卡的iid编号
	 * @return
	 */
	@RequestMapping(value = "/changeUnitCard", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeUnitCard(String card_number,String unit_id,String change_number,int iid) {
		UnitCard list = service.findCardByNumber(unit_id, card_number);
		
		if (list!=null) {//判断修改后的卡号是否已存在
			return JSONObject.fromObject("{\"data\":\"编号已存在\"}");
		}else {
			int i = service.changeUnitCard(card_number,change_number,unit_id,iid);
			if (i>=0) {
				return JSONObject.fromObject("{\"data\":\"修改成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"修改失败\"}");
			}
		}
	}
	//===========对普通卡的操作
	/**
	 * 删除普通卡
	 * @param iids 普通卡iid数组
	 * @param keybox_id 普通卡所在箱maxc地址
	 * @param unit_id 所在单位唯一编码
	 * @param team_id 所在班组iid
	 * @return
	 */
	@RequestMapping(value = "/deleteCards", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteCards(int [] iids,String keybox_id,String unit_id,int team_id) {
		KeyBox1List list = ServletContextLTest.keyboxList;
		long millis = System.currentTimeMillis();
		
		if (list.get(keybox_id)!=null&&millis-list.get(keybox_id).getLastTime()<=60*1000) {//判断keybox_id对应的箱是否在线
			int i = service.deleteCards(iids,keybox_id);
			if(i>=1) {
				return JSONObject.fromObject("{\"data\":\"删除成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"删除失败\"}");
			}
		}else {
			return JSONObject.fromObject("{\"data\":\"设备掉线，不能操作\"}");
		}
	}
	
	/**
	 * 修改普通卡的卡号
	 * @param card_number修改后的卡号
	 * @param keybox_id所在箱maxc
	 * @param change_number修改前的卡号
	 * @param unit_id所在单位唯一编码
	 * @param team_id所在班组
	 * @param iid	卡iid编号
	 * @return
	 */
	@RequestMapping(value = "/changeCard", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeCard(String card_number,String keybox_id,String change_number,String unit_id,int team_id,int iid) {
		KeyBox1List list2 = ServletContextLTest.keyboxList;
		long millis = System.currentTimeMillis();
		
		if (list2.get(keybox_id)!=null&&millis-list2.get(keybox_id).getLastTime()<=60*1000) {//判断keybox_id对应的箱是否在线
			UnitCard list = service.findCardByNumber(unit_id, card_number);
			if (list!=null) {//判断修改后的卡号是否已存在
				return JSONObject.fromObject("{\"data\":\"卡号已存在\"}");
			}else {
				int i = service.changeCard(card_number,keybox_id,change_number,iid);
				if (i>=0) {
					return JSONObject.fromObject("{\"data\":\"修改成功\"}");
				}else {
					return JSONObject.fromObject("{\"data\":\"修改失败\"}");
				}
			}
		}else {
			return JSONObject.fromObject("{\"data\":\"设备掉线，不能操作\"}");
		}
	}
	
	/**
	 * 查询普通卡
	 * @param card_number 普通卡卡号
	 * @param keybox_id所在箱maxc
	 * @param unit_id所在单位唯一编码
	 * @param team_id所在班组
	 * @return
	 */
	@RequestMapping(value = "/seachCard", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject seachCard(String card_number,String keybox_id,String unit_id,int team_id) {
		Card findCard = service.seachCard(card_number,keybox_id);//通过卡号查询普通卡
		
		if (findCard!=null) {
			return JSONObject.fromObject(findCard);
		}else {
			return JSONObject.fromObject("{\"data\":\"查询不到\"}");
		}
	}
	
	/**
	 * 查询箱下所有的普通卡
	 * @param keybox_id箱maxc
	 * @param unit_id单位唯一编码
	 * @param team_id班组
	 * @return
	 */
	@RequestMapping(value = "/findCard", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findCard(String keybox_id,String unit_id,int team_id) {
		List<Card> cards = service.findCard(keybox_id);
		return JSONArray.fromObject(cards);
	}
	
	/**
	 * 添加普通卡
	 * @param card_number普通卡卡号
	 * @param keybox_id所在箱maxc
	 * @param unit_id所在单位唯一编码
	 * @param iid普通卡编码
	 * @param team_id所在班组
	 * @return
	 */
	@RequestMapping(value = "/addCard", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addCard(String card_number,String keybox_id,String unit_id,int iid,int team_id) {
		UnitCard list = service.findCardByNumber(unit_id,card_number);
		List<Integer> findIid = service.findIid(keybox_id);
		
		if (list!=null) {//判断要添加的卡号是否已存在
			return JSONObject.fromObject("{\"data\":\"卡号已存在\"}");
		}else {
			if (findIid.size()>=3) {
				return JSONObject.fromObject("{\"data\":\"最多3张普通卡\"}");
			}else if(!findIid.contains(iid)){//判断要添加的卡iid是否已存在
				int i = service.addCard(keybox_id,card_number,unit_id,iid,team_id);
				
				if(i>=0) {
					return JSONObject.fromObject("{\"data\":\"添加成功\"}");
				}else {
					return JSONObject.fromObject("{\"data\":\"添加失败\"}");
				}
			}else {
				return JSONObject.fromObject("{\"data\":\"编号已存在\"}");
			}
		}
	}
	
	//===========对钥匙的操作
	/**
	 * 查询所有开锁时可选择的使用者
	 * @param team_id 班组编号
	 * @param style unit、team区分管理员类型
	 * @param unit_id 单位唯一编号
	 * @param managername 管理员名称  单位下唯一
	 * @return
	 */
	@RequestMapping(value = "/getLockAuth", method = RequestMethod.POST)
	@ResponseBody
	public Object getLockAuth(int team_id,String style,String unit_id,String managername) {
		if (style.equals("unit")) {//判断开锁的人是单位管理员
			List<Integer> list = service.findUnitManagerAuthByName(managername,unit_id);//通过单位管理员名称查询其可操作的班组
			
			if (list.contains(team_id)) {//判断钥匙所在班组在单位管理员的操作权限班组内
				List<Users> allUser = service.findAllUser(unit_id,team_id);
				return JSONArray.fromObject(allUser);
			}else {
				return JSONObject.fromObject("{\"data\":\"no\"}");
			}
		}else {//判断开锁的人是班组管理员
			List<Users> allUser = service.findAllUser(unit_id,team_id);
			return JSONArray.fromObject(allUser);
		}
	}
	
	/**
	 * 修改钥匙名称
	 * @param keybox_id 钥匙所在箱maxc
	 * @param name 修改后的钥匙名称
	 * @param iid 钥匙在箱下编号
	 * @param unit_id 单位唯一编号
	 * @param team_id 班组编号
	 * @return
	 */
	@RequestMapping(value = "/changeKeyById", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeKeyById(String old_name,int iid,String keybox_id,String unit_id,int team_id,String old_card_id,String new_name,String new_card,String usering) {
		KeyBox1List list2 = ServletContextLTest.keyboxList;
		long millis = System.currentTimeMillis();
		
		if (list2.get(keybox_id)!=null&&millis-list2.get(keybox_id).getLastTime()<=60*1000) {//判断keybox_id对应箱在线
			int i = -2;
			if (new_card!=""&&new_name!="") {
				Key key = service.findKeyByNameOrIid(keybox_id, new_name, -1,null);//当iid为-1时通过名称查询钥匙
				String findKeyCard_id = service.findKeyCard_id(new_card);
				
				if (key==null&&findKeyCard_id==null) {
					i = service.changeKeyById(new_name,iid,keybox_id,new_card,usering);
				}else {
					return JSONObject.fromObject("{\"data\":\"名称或卡id已存在\"}");
				}
			}else if (new_card!="") {
				String findKeyCard_id = service.findKeyCard_id(new_card);
				if (findKeyCard_id==null) {
					i = service.changeKeyById(old_name,iid,keybox_id,new_card,usering);
				}else {
					return JSONObject.fromObject("{\"data\":\"卡id已存在\"}");
				}
			}else {
				Key key = service.findKeyByNameOrIid(keybox_id, new_name, -1,null);//当iid为-1时通过名称查询钥匙
				if (key==null) {
					i = service.changeKeyById(new_name,iid,keybox_id,old_card_id,usering);
				}else {
					return JSONObject.fromObject("{\"data\":\"名称已存在\"}");
				}
			}
			
			if(i>=0) {
				return JSONObject.fromObject("{\"data\":\"修改成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"修改失败\"}");
			}
		}else {
			return JSONObject.fromObject("{\"data\":\"设备掉线，不能操作\"}");
		}
	}
	
	/**
	 * 通过钥匙iid删除钥匙
	 * @param iids 钥匙iid数组
	 * @param keybox_id 钥匙所在箱maxc
	 * @param unit_id 单位唯一编号
	 * @param team_id 班组编号
	 * @return
	 */
	@RequestMapping(value = "/deleteKeyByIds", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteKeyByIds(int [] iids,String keybox_id,String unit_id,int team_id,String [] card_ids,String [] userings) {
		KeyBox1List list2 = ServletContextLTest.keyboxList;
		long millis = System.currentTimeMillis();
		log.info("card:"+card_ids[0]);
		if (list2.get(keybox_id)!=null&&millis-list2.get(keybox_id).getLastTime()<=60*1000) {//判断keybox_id对应箱在线
			int i = service.deleteKeyByIds(iids, keybox_id,card_ids,userings);
			
			if(i>=1) {
				return JSONObject.fromObject("{\"data\":\"删除成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"删除失败\"}");
			}
		}else {
			return JSONObject.fromObject("{\"data\":\"设备掉线，不能操作\"}");
		}
	}
	
	/**
	 * 通过名称或者编号查询钥匙
	 * @param keybox_id 钥匙所在箱maxc
	 * @param name 钥匙名称
	 * @param iid 钥匙编号
	 * @param unit_id 单位唯一编号
	 * @param team_id 班组编号
	 * @return
	 */
	@RequestMapping(value = "/findkeyByName", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findkeyByName(String keybox_id,String name,int iid,String unit_id,int team_id,String card_id) {
		Key key = service.findKeyByNameOrIid(keybox_id,name,iid,card_id);//通过名称或者编号查询钥匙
		
		if (key==null) {
			return JSONObject.fromObject("{\"data\":\"不存在\"}");
		}else {
			return JSONObject.fromObject(key);
		}
	}
	
	/**
	 * 添加钥匙
	 * @param name 钥匙名称
	 * @param keybox_id 所在箱maxc
	 * @param iid 钥匙编号
	 * @param unit_id 单位唯一编号
	 * @param team_id 班组编号
	 * @return
	 */
	@RequestMapping(value = "/addKey", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addKey(String name,String keybox_id,int iid,String unit_id,int team_id,String card_id) {
		List<Key> list = service.findKeys(keybox_id);//查询箱下所有钥匙
		int flag = 0;
		for(int a=0;a<list.size();a++) {
			if (list.get(a).getIid()==iid) {//判断钥匙iid是否已存在
				flag = 1;
				break;
			}
			if (list.get(a).getName().equals(name)) {//判断钥匙名称是否已存在
				flag = 2;
				break;
			}
		}
		
		if (flag==0) {
			if (list.size()>=149) {//判断钥匙是否达到上限
				return JSONObject.fromObject("{\"data\":\"最多149把钥匙\"}");
			}else {
				String findKeyCard_id = service.findKeyCard_id(card_id);
				if(findKeyCard_id!=null) {
					return JSONObject.fromObject("{\"data\":\"卡id已存在\"}");
				}else {
					Key u = new Key(iid,keybox_id,name,unit_id,team_id,card_id);
					int i = service.addKey(u);
					
					if(i>=1) {
						return JSONObject.fromObject("{\"data\":\"添加成功\"}");
					}else {
						return JSONObject.fromObject("{\"data\":\"添加失败\"}");
					}
				}
			}
		}else if (flag==1) {
			return JSONObject.fromObject("{\"data\":\"编号已存在\"}");
		}else{
			return JSONObject.fromObject("{\"data\":\"名称已存在\"}");
		}
	}
	
	/**
	 * 查询钥匙
	 * @param start 查询开始的地方
	 * @param keybox_id 所在箱maxc
	 * @param unit_id 单位唯一编号
	 * @param team_id 班组编号
	 * @return
	 */
	@RequestMapping(value = "/findKey", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findKey(int start,String keybox_id,String unit_id,int team_id) {
		List<Key> findKey = service.findKey(keybox_id,start*20);
		return JSONArray.fromObject(findKey);
	}
	
	/**
	 * 查询钥匙
	 * @param start 查询开始的地方
	 * @param keybox_id 所在箱maxc
	 * @param unit_id 单位唯一编号
	 * @param team_id 班组编号
	 * @return
	 */
	@RequestMapping(value = "/usering", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject usering(long id,String usering,String card_id,String keybox_id,int iid,String name) {
		if (usering.equals("启用")) {
			usering = "禁用";
			UDPClient.sendLockDeploy(keybox_id, 1, iid, name, card_id);
		}else {
			usering = "启用";
			UDPClient.sendLockDeploy(keybox_id, 2, iid, name, card_id);
		}
		int i = service.changeKeyUseringById(id,usering);
		if (i>=0) {
			return JSONObject.fromObject("{\"data\":\"设置成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"设置失败\"}");
		}
	}
	
	//===========对logs的操作
	/**
	 * 查询各层开锁记录
	 * @param start 查询开始的位置
	 * @param keyss_id 钥匙编号
	 * @param keybox_id 箱maxc
	 * @param team_id 班组编号
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/findLog", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findLog(int start,int keyss_id,String keybox_id,int team_id,String unit_id,int style) {
		List<Logs> list = null;
		
		if (keyss_id!=-1) {//钥匙编号存在查询钥匙的实时记录
			list = service.getLogsByKeyss_id(keyss_id,start*20,keybox_id,style);
		}else if (keybox_id!=null) {//箱编号存在查询箱的实时记录
			list = service.getLogsByKeybox_id(keybox_id,start*20,style);
		}else if (team_id!=-1) {//班组编号存在查询班组实时记录
			list = service.getLogsByTeam_id(team_id,start*20,unit_id,style);
		}else {//查询整个单位实时记录
			list = service.getLogs(start*20,style);
		}
		return JSONArray.fromObject(list);
	}
	
	/**
	 * 开锁
	 * @param users_iid 使用者编号
	 * @param keyss_iid 钥匙编号
	 * @param manager_name 管理员name
	 * @param users_name 使用者name
	 * @param keyss_name 钥匙name
	 * @param keybox_id 箱maxc
	 * @param team_id 班组编号
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/addLog", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addLog(int users_iid,int keyss_iid,String manager_name,String users_name,String keyss_name,String keybox_id,int team_id,String unit_id) {
		KeyBox1List list2 = ServletContextLTest.keyboxList;
		long millis = System.currentTimeMillis();
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String approve_time = format.format(new Date());
		Name name = service.findName(keybox_id);
		
		List<Manager> Manager = service.findManagerByNama(unit_id, manager_name);//通过名称查询单位或者班组管理员
		Logs logs = new Logs(keyss_iid, users_iid, users_name, Manager.get(0).getIid(), manager_name, "--", approve_time, "--", "远程开锁", name.getUnit_name(), name.getTeam_name(), name.getKeybox_name(), keyss_name, unit_id, team_id, keybox_id, "--", 0);
		
		if (list2.get(keybox_id)!=null&&millis-list2.get(keybox_id).getLastTime()<=60*1000) {//判断keybox_id对应箱在线
			int i = service.addOpenKeyLog(logs);//发送开锁命令并添加记录
			if (i>=0) {
				return JSONObject.fromObject("{\"data\":\"开锁命令发送成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"开锁命令发送失败\"}");
			}
		}else {
			return JSONObject.fromObject("{\"data\":\"设备掉线，不能操作\"}");
		}
	}
	
	//===========对单位管理员的操作
	/**
	 * 通过name查询单位管理员
	 * @param name 单位管理员name
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/findUnitManagerByName", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findUnitManagerByName(String name,String unit_id) {
		UnitManager unitManager = service.findUnitManagerByName(unit_id,name);//通过名称查询单位管理员
	
		if (unitManager!=null) {
			return JSONObject.fromObject(unitManager);
		}else {
			return JSONObject.fromObject("{\"data\":\"不存在\"}");
		}
	}
	
	/**
	 * 通过id修改单位管理员name和password
	 * @param name 修改后单位管理员name
	 * @param password 修改后单位管理员password
	 * @param id 单位管理员id
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/changeUnitManager", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeUnitManager(String name,String password,long id,String unit_id) {
		List<Manager> list = service.findManagerByNama(unit_id, name);//通过名称查询单位或者班组管理员
		
		if (list.size()==0) {//表示该名称不存在
			int i = service.changeUnitManager(name,password,id);
			
			if (i>=0) {
				return JSONObject.fromObject("{\"data\":\"修改成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"修改失败\"}");
			}
		}else {
			return JSONObject.fromObject("{\"data\":\"名称已存在\"}");
		}
	}
	
	/**
	 * 通过iid删除单位管理员
	 * @param iids 单位管理员iid数组
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/deleteUnitManager", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteUnitManager(int [] iids,String unit_id) {
		int i = service.deleteUnitManager(iids,unit_id);//通过iid删除单位管理员
		
		if (i>0) {
			return JSONObject.fromObject("{\"data\":\"删除成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"删除失败\"}");
		}
	}
	
	/**
	 * 添加单位管理员
	 * @param name 单位管理员name
	 * @param password 单位管理员password
	 * @param unit_id 单位唯一编号
	 * @param iid 单位管理员iid
	 * @return
	 */
	@RequestMapping(value = "/addUnitManager", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addUnitManager(String name,String password,String unit_id,int iid) {
		List<Manager> list = service.findUnitManagerByNamaOrIid(unit_id,name,iid);//在单位管理员中查看要添加的编号是否已存在，在单位和班组管理员中查看要添加的名称是否已存在
		
		if (list.size()==0) {
			int count = service.findUnitManagerCount(unit_id);//查询单位管理员总数
			
			if (count>=32) {
				return JSONObject.fromObject("{\"data\":\"已存在32个管理员\"}");
			}else {
				UnitManager unitManager = new UnitManager(iid,unit_id, name,password);
				int i = service.addUnitManager(unitManager);
				
				if (i>=1) {
					return JSONObject.fromObject("{\"data\":\"添加成功\"}");
				}else {
					return JSONObject.fromObject("{\"data\":\"添加失败\"}");
				}
			}
		}else{
			return JSONObject.fromObject("{\"data\":\"名称或编号重复\"}");
		}
	}
	
	/**
	 * 查询单位下的单位管理员
	 * @param unit_id 单位唯一编号
	 * @param start 查询开始的位置
	 * @return
	 */
	@RequestMapping(value = "/findUnitManager", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findUnitManager(String unit_id,int start) {
		List<UnitManager> list = service.findUnitManager(unit_id,start*20);
		return JSONArray.fromObject(list);
	}
	
	/**
	 * 查询所有还未分配授权卡的管理员(包括单位管理员和班组管理员)
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/findAllManager", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findAllManager(String unit_id) {
		List<Manager> manager = service.findAllManager(unit_id);//查询所有管理员
		List<String> hashCard = service.findAllManagerHashCard(unit_id);//查询所有已授权卡的管理员
		Iterator<Manager> iterator = manager.iterator();
		
		while(iterator.hasNext()) {//如果已分配授权卡则将其在所有管理员的集合中去掉
			if(hashCard.contains(iterator.next().getName())) {
				iterator.remove();
			}
		}
		return JSONArray.fromObject(manager);//返回没有分配授权卡的管理员
	}
	
	//===========对班组管理员的操作
	/**
	 * 通过name查询班组管理员
	 * @param name 管理员name
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/findTeamByName", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findTeamByName(String name,String unit_id) {
		Team team = service.findTeamByName(name,unit_id);//通过名称查询班组
		
		if (team==null) {
			return JSONObject.fromObject("{\"data\":\"不存在\"}");
		}else {
			return JSONObject.fromObject(team);
		}
	}
	
	/**
	 * 通过id修改班组管理员name
	 * @param change_name 修改后的name
	 * @param id 班组管理员id
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/changeTeamById", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeTeamById(String change_name,long id,String unit_id) {
		Team team = service.findTeamByName(change_name,unit_id);
		
		if (team!=null) {
			return JSONObject.fromObject("{\"data\":\"已存在\"}");
		}else {
			int i = service.changeTeamById(id,change_name);
			
			if(i>=0) {
				return JSONObject.fromObject("{\"data\":\"修改成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"修改失败\"}");
			}
		}
	}
	/**
	 * 通过name查询班组管理员
	 * @param name 管理员name
	 * @param team_id 所在班组编号
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/findTeamManagerByName", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findTeamManagerByName(String name,int team_id,String unit_id) {
		TeamManager teamManager = service.findTeamManagerByName(team_id,name,unit_id);
	
		if (teamManager!=null) {
			return JSONObject.fromObject(teamManager);
		}else {
			return JSONObject.fromObject("{\"data\":\"不存在\"}");
		}
	}
	
	/**
	 * 通过id修改班组管理员name和password
	 * @param name 修改后的name
	 * @param password 修改后的password
	 * @param id 班组管理员id
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/changeTeamManager", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeTeamManager(String name,String password,long id,String unit_id) {
		List<Manager> list = service.findManagerByNama(unit_id, name);//查询该管理员名称是否已存在
		
		if (list.size()==0) {
			int i = service.changeTeamManager(name,password,id);
			
			if (i>=0) {
				return JSONObject.fromObject("{\"data\":\"修改成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"修改失败\"}");
			}
		}else {
			return JSONObject.fromObject("{\"data\":\"名称已存在\"}");
		}
	}
	
	/**
	 * 通过iid删除班组管理员
	 * @param iids 班组管理员iid数组
	 * @param team_id 所在班组编号
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/deleteTeamManager", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteTeamManager(int [] iids,int team_id,String unit_id) {
		int i = service.deleteTeamManager(iids,team_id,unit_id);
		
		if (i>0) {
			return JSONObject.fromObject("{\"data\":\"删除成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"删除失败\"}");
		}
	}
	
	/**
	 * 添加班组管理员
	 * @param iid 班组管理员编号
	 * @param name 班组管理员名称
	 * @param password 班组管理员密码
	 * @param team_id 所在班组编号
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/addTeamManager", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addTeamManager(int iid,String name,String password,int team_id,String unit_id) {
		List<Manager> list = service.findManagerByNama(unit_id,name);//查询该管理员名称是否已存在
		List<Integer> list2 = service.findAllTeamManagerIid(unit_id,team_id);//查询指定班组下所有的班组管理员编号
	
		if (list.size()==0) {
			if (list2.size()>=3) {
				return JSONObject.fromObject("{\"data\":\"已存在3个管理员\"}");
			}else if(!list2.contains(iid)){//该班组管理员编号没有被使用
				TeamManager teamManager = new TeamManager(iid,team_id, name,password,unit_id);
				int i = service.addTeamManager(teamManager);
				
				if (i>=1) {
					return JSONObject.fromObject("{\"data\":\"添加成功\"}");
				}else {
					return JSONObject.fromObject("{\"data\":\"添加失败\"}");
				}
			}else {
				return JSONObject.fromObject("{\"data\":\"编号重复\"}");
			}
		}else{
			return JSONObject.fromObject("{\"data\":\"名称重复\"}");
		}
	}
	
	/**
	 * 查询某班组下的管理员
	 * @param team_id 班组编号
	 * @param start 查询开始位置
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/findTeamManager", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findTeamManager(int team_id,int start,String unit_id) {
		List<TeamManager> list = service.findTeamManager(team_id,start*20,unit_id);
		return JSONArray.fromObject(list);
	}
	
	//===========对使用者的操作
	/**
	 * 通过名称查询使用者
	 * @param team_id 所在班组编号
	 * @param name 使用者name
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/searchUser", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject searchUser(int team_id,String name,String unit_id) {
		Users users = service.findUsersByName(name,team_id,unit_id);
	
		if (users!=null) {
			return JSONObject.fromObject(users);
		}else {
			return JSONObject.fromObject("{\"data\":\"不存在\"}");
		}
	}
	
	/**
	 * 通过id修改使用者name
	 * @param name 修改后的使用者name
	 * @param id 使用者id
	 * @param team_id 所在班组编号
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/changeUser", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeUser(String name,long id,int team_id,String unit_id,int iid) {
		Users user = service.findUsersByName(name,team_id,unit_id);//通过名称查询使用者
		
		if (user!=null) {
			return JSONObject.fromObject("{\"data\":\"已存在\"}");
		}else {
			int i = service.changeUser(id,name);
			if(i>=0) {//修改用户名称后把它更新到该班组下的每一个箱
				List<KeyBox> list = service.findKeyBoxs(team_id, unit_id);
				for(int a=0;a<list.size();a++) {
					UDPClient.sendUserDeploy(list.get(a).getMaxc(), 3, iid, name);
				}
				return JSONObject.fromObject("{\"data\":\"修改成功\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"修改失败\"}");
			}
		}
	}
	
	/**
	 * 通过iid删除使用者
	 * @param iids 使用者iid数组
	 * @param team_id 所在班组编号
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/deleteUsers", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteUsers(int [] iids,int team_id,String unit_id) {
		int i = service.deleteUsers(iids,team_id,unit_id);
		
		if (i>=0) {
			return JSONObject.fromObject("{\"data\":\"删除成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"删除失败\"}");
		}
	}
	
	/**
	 * 添加使用者
	 * @param team_id 所在班组编号
	 * @param name 使用者name
	 * @param iid 使用者iid
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject addUser(int team_id,String name,int iid,String unit_id) {
		List<Users> allUser = service.findAllUser(unit_id,team_id);//查询班组下所有使用者
		
		int flag = 0;
		for(int a=0;a<allUser.size();a++) {
			if (allUser.get(a).getName().equals(name)) {//要添加的名称已存在
				flag = 1;
			}
			if (allUser.get(a).getIid()==iid) {//要添加的iid已存在
				flag = 2;
			}
		}
		
		if (flag==0) {
			if(allUser.size()>=64) {
				return JSONObject.fromObject("{\"data\":\"使用者最多64个\"}");
			}else {
				int i = service.addUsers(iid,team_id,name,unit_id);
				
				if (i>=1) {
					return JSONObject.fromObject("{\"data\":\"添加成功\"}");
				}else {
					return JSONObject.fromObject("{\"data\":\"添加失败\"}");
				}
			}
		}else if(flag==1){
			return JSONObject.fromObject("{\"data\":\"名称已存在\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"编号已存在\"}");
		}
	}
	
	/**
	 * 查询班组下使用者
	 * @param team_id 所在班组编号
	 * @param start 查询开始的位置
	 * @param unit_id 单位唯一编号
	 * @return
	 */
	@RequestMapping(value = "/findUser", method = RequestMethod.POST)
	@ResponseBody
	public JSONArray findUser(int team_id,int start,String unit_id) {
		List<Users> findTeam = service.findUser(unit_id,team_id,start*20);
		return JSONArray.fromObject(findTeam);
	}
	
	//===========对配置和登陆、弹出等等操作
	 /**
     * jsp文件上传通过ssm框架自动解析到指定文件夹，
     * @param file
     * @return
     * @throws IOException 
     */
    @RequestMapping(value="/upload",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject upload(MultipartFile file,HttpServletRequest request) throws IOException{
        String path = "c:\\lock/backData";
        String fileName = file.getOriginalFilename();  
        File dir = new File(path,fileName);
        
        if(!dir.exists()){
            dir.mkdirs();
        }
        //MultipartFile自带的解析方法
        file.transferTo(dir);
        //恢复指定数据库文件
        String back = service.backData("c:\\lock/backData/"+fileName);
        return JSONObject.fromObject("{\"data\":\""+back+"\"}");
    }
    
    /**
     * 修改自动备份的时间
     * @param day 每隔多少天
     * @return
     */
	@RequestMapping(value = "/setConfigTime", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject setConfigTime(int day) {
		long millis = System.currentTimeMillis();
		int i = service.setConfigTime(millis,day);//更新数据库中备份间隔天数及设置时间
		
		ServletContextLTest.config.setIntervalDay(day);//更新缓存中备份间隔天数
		ServletContextLTest.config.setSetTime(millis);//更新缓存中设置备份间隔时的时间
		
		if (i>=0) {
			return JSONObject.fromObject("{\"data\":\"设置成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"设置失败\"}");
		}
	}
	/**
     * 修改监听端口
     * @param port 要监听的端口
     * @return
     */
	@RequestMapping(value = "/setConfigPort", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject setConfigPort(int port) {
		int i = service.setConfigPort(port);//更新数据库中的监听端口
		ServletContextLTest.config.setReceivePort(port);//更新缓存中的监听端口
		
		if (i>=0) {
			return JSONObject.fromObject("{\"data\":\"设置成功\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"设置失败\"}");
		}
	}
	
	/**
	 * 查询备份间隔天数
	 * @return
	 */
	@RequestMapping(value = "/findConfig", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findConfig() {
		Config copytime = service.findConfig();//获取数据库中保存的配置信息
		
		if (copytime!=null) {
			return JSONObject.fromObject(copytime);
		}else{
			return JSONObject.fromObject("{\"data\":\"\"}");
		}
	}
	
	/**
	 * 通过id或name修改管理员或调试者passwrod
	 * @param password 修改后的password
	 * @param id 单位或班组管理员id
	 * @param style 修改的管理员类型 level、unit、team 调试者、单位管理员、班组管理员
	 * @param name 调试者name
	 * @return
	 */
	@RequestMapping(value = "/changeLevel", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject changeLevel(String password,long id,String style,String name) {
		 int i = 0;
		 if (style.equals("level")) {
			 i = service.changeLevel(password,name);
		 }else if (style.equals("unit")) {
			 i = service.changeUnit(password,id);
		 }else if(style.equals("team")){
			 i = service.changeTeam(password,id);
		 }
		 if (i>=0) {
			 return JSONObject.fromObject("{\"data\":\"修改成功\"}");
		 }else {
			 return JSONObject.fromObject("{\"data\":\"修改失败\"}");
		 }
	}
	
	
	
	/**
	 * 通过班组管理员name查询班组管理员
	 * @param team_id 班组编号
	 * @param unit_id 单位唯一编号
	 * @param managername 班组管理员name
	 * @return
	 */
	@RequestMapping(value = "/validAuth", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject findUnit(int team_id,String unit_id,String managername) {
		String name = service.validAuth(unit_id,team_id,managername);//查询指定名称的班组管理员
		if (name!=null) {
			return JSONObject.fromObject("{\"data\":\"ok\"}");
		}else {
			return JSONObject.fromObject("{\"data\":\"no\"}");
		}
	}
	
	/**
	 * 获取箱的远程操作记录
	 * @param maxc 箱maxc地址
	 * @return
	 */
	@RequestMapping(value = "/getRemotesLogs", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getRemotesLogs(String keybox_id,int style) {
		int b = validTime(keybox_id, 1);
		
		if(b==0) {
			return JSONObject.fromObject("{\"data\":\"设备掉线，不能操作\"}");
		}else if (b==1) {
			return JSONObject.fromObject("{\"data\":\"不要频繁操作，请等待60s\"}");
		}else {
			ServletContextLTest.keyboxList.updateGetAllBack(keybox_id, 2);
			UDPClient.sendGetOperateLog(keybox_id,style);
			return JSONObject.fromObject("{\"data\":\"命令已发送\"}");
		}
	}
	
	/**
	 * 获取箱的远程所有钥匙状态
	 * @param  maxc 箱maxc地址
	 * @return
	 */
	@RequestMapping(value = "/getRemotesKeyState", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getRemotesKeyState(String keybox_id) {
		int b = validTime(keybox_id, 1);
		
		if(b==0) {
			return JSONObject.fromObject("{\"data\":\"设备掉线，不能操作\"}");
		}else if (b==1) {
			return JSONObject.fromObject("{\"data\":\"不要频繁操作，请等待60s\"}");
		}else {
			ServletContextLTest.keyboxList.updateGetAllBack(keybox_id, 2);
			UDPClient.sendGetLockState(keybox_id);
			return JSONObject.fromObject("{\"data\":\"命令已发送\"}");
		}
	}
	
	/**
	 * 删除远程用户、卡或者钥匙
	 * @param  maxc 箱maxc地址
	 * @return
	 */
	@RequestMapping(value = "/deleteRemotes", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject deleteRemotes(String keybox_id,String element) {
		int b = validTime(keybox_id, 1);
		
		if (b==0) {
			return JSONObject.fromObject("{\"data\":\"设备掉线，不能操作\"}");
		}else if (b==1) {
			return JSONObject.fromObject("{\"data\":\"不要频繁配置,请等待60s\"}");
		}else {
			ServletContextLTest.keyboxList.updateGetAllBack(keybox_id, 2);
			if (element.equals("User")) {//判断要删除的是所有远程用户
				UDPClient.sendUserDeploy(keybox_id, 2,65535 , "");
			}else if (element.equals("Card")) {//判断要删除的是所有远程卡
				UDPClient.sendCardDeploy(keybox_id, 2, 65535, "00000000");
			}else {////判断要删除的是所有远程钥匙
				UDPClient.sendLockDeploy(keybox_id, 2, 65535, "","0000000000");
			}
			return JSONObject.fromObject("{\"data\":\"命令已发送\"}");
		}
	}
	/**
	 * 把箱能使用的所有用户下发到箱
	 * @param  maxc 箱maxc地址
	 * @return
	 */
	@RequestMapping(value = "/sendAllUser", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject sendAllUser(String keybox_id,String unit_id,int team_id) {
		int b = validTime(keybox_id, 1);
		
		if(b==0) {
			return JSONObject.fromObject("{\"data\":\"设备掉线，不能操作\"}");
		}else if (b==1) {
			return JSONObject.fromObject("{\"data\":\"不要频繁操作，请等待60s\"}");
		}else {
			List<Users> sendAllUser = service.sendAllUser(keybox_id,unit_id,team_id);//下发所有使用者到箱
			ServletContextLTest.keyboxList.updateGetAllBack(keybox_id, sendAllUser.size()+1);
			for(int a=0;a<sendAllUser.size();a++) {
				UDPClient.sendUserDeploy(keybox_id, 1, sendAllUser.get(a).getIid(), sendAllUser.get(a).getName());
			}
			return JSONObject.fromObject("{\"data\":\"命令已发送\"}");
		}
	}
	/**
	 * 把箱的所有钥匙下发到箱
	 * @param  maxc 箱maxc地址
	 * @return
	 */
	@RequestMapping(value = "/sendAllKey", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject sendAllKey(String keybox_id,String unit_id,int team_id) {
		int b = validTime(keybox_id, 1);
		
		if(b==0) {
			return JSONObject.fromObject("{\"data\":\"设备掉线，不能操作\"}");
		}else if (b==1) {
			return JSONObject.fromObject("{\"data\":\"不要频繁操作，请等待60s\"}");
		}else {
			List<Key> sendAllKey = service.sendAllKey(keybox_id);//下发所有使用者到箱
			ServletContextLTest.keyboxList.updateGetAllBack(keybox_id, sendAllKey.size()+1);
			
			for(int a=0;a<sendAllKey.size();a++) {
				String regex = "\\d{10}";
				boolean bb = Pattern.matches(regex, sendAllKey.get(a).getCard_id());
				
				if(bb) {//默认前30个钥匙位启动状态，但是card_id并没有配置，所有设为0
					UDPClient.sendLockDeploy(keybox_id, 1, sendAllKey.get(a).getIid(), sendAllKey.get(a).getName(),sendAllKey.get(a).getCard_id());
				}else {
					UDPClient.sendLockDeploy(keybox_id, 1, sendAllKey.get(a).getIid(), sendAllKey.get(a).getName(),"0000000000");
				}
			}
			return JSONObject.fromObject("{\"data\":\"命令已发送\"}");
		}
	}
	/**
	 * 把箱能使用的所有卡下发到箱
	 * @param  maxc 箱maxc地址
	 * @return
	 */
	@RequestMapping(value = "/sendAllCard", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject sendAllCard(String keybox_id,String unit_id,int team_id) {
		int b = validTime(keybox_id, 1);
		
		if(b==0) {
			return JSONObject.fromObject("{\"data\":\"设备掉线，不能操作\"}");
		}else if (b==1) {
			return JSONObject.fromObject("{\"data\":\"不要频繁操作，请等待60s\"}");
		}else {
			service.sendAllCard(keybox_id,unit_id,team_id);//下发所有可使用的卡到箱
			return JSONObject.fromObject("{\"data\":\"命令已发送\"}");
		}
	}
	
	/**
	 * 登录软件
	 * @param name 管理员name
	 * @param password 管理员password
	 * @param remember 是否记住mm
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject login(String name,String password,int remember,HttpServletRequest request) {
		Config Level = service.findLevel(name);

		if (Level!=null&&name.equals(Level.getName())) {//判断为调试者
			if (password.equals(Level.getPassword())) {
				HttpSession session = request.getSession();
				session.setAttribute("user", name);
				session.setAttribute("id", 0);
				session.setAttribute("style", "level");
				
				if (Level.getRemember()!=remember) {//判断是否记住密码
					service.updataLevel(Level.getId(),remember);
				}
				return JSONObject.fromObject("{\"url\":\"/ES/index.jsp\"}");
			}else {
				return JSONObject.fromObject("{\"data\":\"密码错误\"}");
			}
		}else {
			UnitManager unitManager = service.getUnitPassword(name);
			if (unitManager!=null&&name.equals(unitManager.getName())) {//判断是否为单位管理员
				if (password.equals(unitManager.getPassword())) {
					HttpSession session = request.getSession();
					session.setAttribute("user", name);
					session.setAttribute("id", unitManager.getId());
					session.setAttribute("style", "unit");
					
					if (unitManager.getRemember()!=remember) {
						service.updataUnitManager(unitManager.getId(),remember);
					}
					return JSONObject.fromObject("{\"url\":\"/ES/index.jsp\"}");
				}else {
					return JSONObject.fromObject("{\"data\":\"密码错误\"}");
				}
			}else {
				TeamManager teamPassword = service.getTeamPassword(name);
				if (teamPassword!=null&&name.equals(teamPassword.getName())) {//判断是否为班组管理员
					if (password.equals(teamPassword.getPassword())) {
						HttpSession session = request.getSession();
						session.setAttribute("user", name);
						session.setAttribute("id", teamPassword.getId());
						session.setAttribute("style", "team");
						
						if (teamPassword.getRemember()!=remember) {
							service.updataTeamManager(teamPassword.getId(),remember);
						}
						return JSONObject.fromObject("{\"url\":\"/ES/index.jsp\"}");
					}else {
						return JSONObject.fromObject("{\"data\":\"密码错误\"}");
					}
				}else {
					return JSONObject.fromObject("{\"data\":\"名称错误\"}");
				}
			}
			
		}
	}
	
	/**
	 * 获取管理员密码
	 * @param name 单位或班组管理员name
	 * @return
	 */
	@RequestMapping(value = "/getPassword", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject getPassword(String name) {
		Config Level = service.findLevel(name);
		
		if (Level!=null&&name.equals(Level.getName())&&Level.getRemember()==1) {//判断为调试者且记住密码
			return JSONObject.fromObject(Level);
		}else {
			UnitManager unitManager = service.getUnitPassword(name);
			if (unitManager!=null&&name.equals(unitManager.getName())&&unitManager.getRemember()==1) {//判断为单位管理员且记住密码
				return JSONObject.fromObject(unitManager);
			}else {
				TeamManager teamPassword = service.getTeamPassword(name);
				if (teamPassword!=null&&name.equals(teamPassword.getName())&&teamPassword.getRemember()==1) {//判断为班组管理员且记住密码
					return JSONObject.fromObject(teamPassword);
				}else {
					return JSONObject.fromObject("{\"data\":\"0\"}");
				}
			}
		}
	}
	
	/**
	 * 退出登录
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/exit", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject exit(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String sessionUser = null;
		
		try {
			sessionUser = (String) session.getAttribute("user");
			if(sessionUser!=null){//退出登陆，销毁sess保存的信息
				session.removeAttribute("user");
				session.removeAttribute("id");
				session.removeAttribute("style");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return JSONObject.fromObject("{\"url\":\"/ES/login.jsp\"}");
	}
	
	
	/**
     * 导出组态数据
     * @return
     */
    @RequestMapping(value = "/copyData", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject copyData(HttpServletRequest request,HttpServletResponse response) throws Exception {
    	try {
    		File file = new File("C:\\lock/","copyData");
    		
    		if (file.exists()) {
    			FileUtils.deleteDirectory(file);
    		}
    		file.mkdir();
    		service.copyData();
    		return JSONObject.fromObject("{\"data\":\"导出成功\"}");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return JSONObject.fromObject("{\"data\":\"导出失败\"}");
		}
    }
	
	/**
     * 导出报表
     * @return
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ResponseBody
    public void export(HttpServletRequest request,HttpServletResponse response,int team_id,String keybox_id,int keyss_id,String unit_id,int style) throws Exception {
    	//获取数据
    	List<Logs> list = null;
    	if (keyss_id!=-1) {//查询指定钥匙的记录，然后导出
			list = service.getAllLogsByKeyss_id(keyss_id,keybox_id,style);
    	}else if (!keybox_id.equals("0")) {//查询指定箱的记录，然后导出
			list = service.getAllLogsByKeybox_id(keybox_id,style);
    	}else if (team_id!=-1) {//查询指定班组的记录，然后导出
			list = service.getAllLogsByTeam_id(team_id,unit_id,style);
		}else {//查询整个单位的记录，然后导出
			list = service.getAllLogs(style);
		}
    	//excel标题
    	String[] title = {"单位名称","班组名称","站点名称","使用人员","审批人员","钥匙名称","操作方式","申请时间","钥匙取走时间","钥匙归还时间","用户卡号"};
    	
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(new Date());
    	//excel文件名
    	String fileName = "钥匙操作记录"+time+".xls";
    	//sheet名
    	String sheetName = "sheet1";
    	
    	//创建HSSFWorkbook 
    	HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, title, list, null);
    	try {
    		//响应到客户端
    		setResponseHeader(response, fileName);
    		OutputStream os = response.getOutputStream();
    		wb.write(os);
    		os.flush();
    		os.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
    }

    //发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            fileName = new String(fileName.getBytes());
            
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("GBK"),"ISO-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
	
	
	
	  //获取MAC地址的方法  
	public String getMACAddress(){ 
		//下面代码是把mac地址拼装成String  
		StringBuffer sb = new StringBuffer();  
		
		try {
			InetAddress ia = InetAddress.getLocalHost();//获取本地IP对象  
			//获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。  
			byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();  
			
			for(int i=0;i<mac.length;i++){
				//mac[i] & 0xFF 是为了把byte转化为正整数  
				String s = Integer.toHexString(mac[i] & 0xFF); 
				sb.append(s.length()==1?0+s:s);  
			}  
		} catch (Exception e) {
			log.info("获取mac失败");
		}
        //把字符串所有小写字母改为大写成为正规的mac地址并返回  
        return sb.toString()+System.currentTimeMillis();  
    }  
	
	/**
	 * 验证箱在线并且距离上一次操作超过1分钟
	 * @param keybox_id 要验证的箱
	 * @return 0掉线	1时间不够    2超过时间
	 */
	public int validTime(String keybox_id,int len) {
		KeyBox1List list = ServletContextLTest.keyboxList;
		KeyBox1 keyBox1 = list.get(keybox_id);
		long millis = System.currentTimeMillis();
		
		if (keyBox1!=null&&millis-keyBox1.getLastTime()<=60*1000) {//判断箱是否在线
			long time = keyBox1.getOperateTime();//获取上一次操作（获取远程记录、清空远程记录、获取远程所有钥匙状态）的时间
			if (millis-time>60*1000*len) {//距上一次操作超过len分钟
				list.updateLogTime(keybox_id,millis);
				return 2;
			}else {
				return 1;
			}
		}else {
			return 0;
		}
	}
}
