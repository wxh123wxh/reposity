package com.wx.service;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wx.client.KeyBox1List;
import com.wx.client.UDPClient;
import com.wx.dao.ServletDao;
import com.wx.entity.Card;
import com.wx.entity.Config;
import com.wx.entity.Key;
import com.wx.entity.KeyBox;
import com.wx.entity.KeyBox1;
import com.wx.entity.KeyBoxInfo;
import com.wx.entity.UnitManager;
import com.wx.entity.Users;
import com.wx.utils.DatabaseBackup;
import com.wx.wx.ServletContextLTest;
import com.wx.entity.Logs;
import com.wx.entity.Manager;
import com.wx.entity.Name;
import com.wx.entity.Team;
import com.wx.entity.TeamManager;
import com.wx.entity.Unit;
import com.wx.entity.UnitCard;


@Service("servletService")
public class ServletServiceImpl implements ServletService{
	
	@Resource(name="servletDao")
	private ServletDao dao ;
	
	public void setDao(ServletDao dao) {
		this.dao = dao;
	}
	
	@Value("${jdbc.username}")//通过注释将properties配置文件的内容注入（先把配置文件加载到spring-mybatis.xml）
	private String username;
	
	@Value("${jdbc.password}")//通过注释将properties配置文件的内容注入
	private String password;
	
	/**
	 * 开启监听信息、命令发送和信息处理线程，并设置监听的端口
	 * @param port 监听的用户端口
	 */
	@Override
	public void connect(int port) {
		UDPClient.connect(port);
	}
	
	/**
	 * 关闭监听信息、命令发送和信息处理线程
	 */
	@Override
	public String stop() {
		return UDPClient.stop();
	}

	/**
	 * 查询所有的箱信息
	 */
	@Override
	public List<KeyBox1> findAllKeyBox() {
		return dao.findAllKeyBox();
	}
	
	/**
	 * 获取初始化完成后的DatabaseBackup对象
	 */
	@Override
	public DatabaseBackup getDatabaseBackup() {
		String mysqlPath = dao.getMysqlDir();//获取数据库的安装路径
		String mysqlBinPath = mysqlPath+"bin";
		DatabaseBackup backup = new DatabaseBackup(mysqlBinPath,username,password);
		return backup;
	}
	
	/**
	 * 通过卡编号查询卡（包括单位卡和普通卡）
	 */
	@Override
	public UnitCard findCardByNumber(String unit_id, String card_number) {
		return dao.findCardByNumber(unit_id, card_number);
	}
	
	/**
	 * 修改单位卡编号,card_number修改后的卡编号，change_number修改前的卡编号，iid卡iid
	 */
	@Transactional
	public int changeUnitCard(String card_number, String change_number, String unit_id,int iid) {
		List<String> maxcs = dao.findKeybox_idByCardIid(unit_id,iid);//通过卡编号查询其可操作的箱
		for(int a=0;a<maxcs.size();a++) {//修改卡编号的同时将修改后的卡编号注册到该卡可操作的所有箱
			UDPClient.sendCardDeploy(maxcs.get(a), 1, iid, card_number);
		}
		return dao.changeUnitCard(card_number, change_number, unit_id);
	}
	
	/**
	 * 通过卡iid删除普通卡
	 */
	@Transactional
	public int deleteCards(int[] iids, String keybox_id) {
		for(int a=0;a<iids.length;a++) {//删除卡的同时把卡注销命令发送到对应的箱
			String card_number = dao.findCardByIid(keybox_id, iids[a]);
			UDPClient.sendCardDeploy(keybox_id, 2, iids[a], card_number);
		}
		return dao.deleteCards(iids, keybox_id);
	}
	
	/**
	 * 修改普通卡编号，card_number修改后的卡编号，change_number修改前的卡编号，iid卡iid
	 */
	@Override
	public int changeCard(String card_number, String keybox_id, String change_number,int iid) {
		UDPClient.sendCardDeploy(keybox_id, 1, iid, card_number);//修改卡编号后下发注册命令到对应的箱
		return dao.changeCard(card_number, keybox_id, change_number);
	}
	
	/**
	 * 通过卡编号查询普通卡，card_number卡编号
	 */
	@Override
	public Card seachCard(String card_number, String keybox_id) {
		return dao.seachCard(card_number, keybox_id);
	}
	
	/**
	 * 查询指定箱下所有的普通卡
	 */
	@Override
	public List<Card> findCard(String keybox_id) {
		return dao.findCard(keybox_id);
	}

	/**
	 * 查询指定箱下所有的普通卡iid编号
	 */
	@Override
	public List<Integer> findIid(String keybox_id) {
		return dao.findIid(keybox_id);
	}
	
	/**
	 * 添加普通卡，card_number卡编号，iid卡iid
	 */
	@Override
	public int addCard(String keybox_id,String card_number,String unit_id,int iid,int team_id) {
		UDPClient.sendCardDeploy(keybox_id, 1, iid, card_number);//添加普通卡的同时下发注册命令道对应的箱
		return dao.addCard(keybox_id,card_number,unit_id,iid,team_id);
	}
	
	/**
	 * 恢复数据库，fileUrl要导入数据库的文件路径
	 */
	@Transactional
	public String backData(String fileUrl) {
		String mysqlPath = dao.getMysqlDir();//获取数据库安装路径
		String mysqlBinPath = mysqlPath+"bin";
		DatabaseBackup backup = new DatabaseBackup(mysqlBinPath,username,password);//创建DatabaseBackup对象
		String restore = backup.restore("lock_db",fileUrl);//恢复数据库,lock_db数据库名称
		
		if (restore.equals("恢复成功")) {//恢复成功后查询加载箱的缓存集合
			ArrayList<KeyBox1> list = (ArrayList<KeyBox1>) dao.findAllKeyBox();
	        Collections.sort(list);
			ServletContextLTest.keyboxList = new KeyBox1List(list);
		}
		return restore;
	}
	
	/**
	 * 获取数据库的安装路径
	 */
	@Override
	public String getMysqlAddress() {
		String mysqlPath = dao.getMysqlDir();
		return mysqlPath;
	}
	
	
	/**
	 * 获取用户配置的监听端口，备份间隔天数及其设置时间
	 */
	@Override
	public Config findConfig() {
		Config findConfig = dao.findConfig();
		return findConfig;
	}
	
	
	/**
	 * 获取关于指定钥匙的记录  keyss_id钥匙编号，start查询起始位置，style 0表示实时记录1表示历史记录
	 */
	@Override
	public List<Logs> getLogsByKeyss_id(int keyss_id,int start,String keybox_id,int style) {
		return dao.getLogsByKeyss_id(keyss_id,start,keybox_id,style);
	}
	
	/**
	 * 获取关于指定箱的记录  keybox_id箱maxc，start查询起始位置，style 0表示实时记录1表示历史记录
	 */
	@Override
	public List<Logs> getLogsByKeybox_id(String keybox_id,int start,int style) {
		return dao.getLogsByKeybox_id(keybox_id,start,style);
	}
	
	/**
	 * 获取关于指定班组的记录  team_id班组编号，start查询起始位置，style 0表示实时记录1表示历史记录
	 */
	@Override
	public List<Logs> getLogsByTeam_id(int team_id,int start,String unit_id,int style) {
		return dao.getLogsByTeam_id(team_id,start,unit_id,style);
	}
	
	/**
	 * 获取整个单位的记录 start查询起始位置，style 0表示实时记录1表示历史记录
	 */
	@Override
	public List<Logs> getLogs(int start,int style) {
		return dao.getLogs(start,style);
	}

	/**
	 * 获取箱名称及其所在的单位名称、班组名称
	 */
	@Override
	public Name findName(String keybox_id) {
		return dao.findName(keybox_id);
	}
	
	/**
	 * 通过名称查询单位或者班组管理员
	 */
	@Override
	public List<Manager> findManagerByNama(String unit_id,String name) {
		return dao.findManagerByNama(unit_id,name);
	}
	
	/**
	 * 添加开锁记录
	 */
	@Override
	public int addOpenKeyLog(Logs logs) {
		UDPClient.sendLockOperate(logs.getKeybox_id(), logs.getKeyss_id(),1);//添加开锁记录的同时下发开锁命令
		return dao.addOpenKeyLog(logs);
	}
	
	/**
	 * 查询单位管理员可操作开锁的班组，managername单位管理员名称
	 */
	@Override
	public List<Integer> findUnitManagerAuthByName(String managername, String unit_id) {
		return dao.findUnitManagerAuthByName(managername, unit_id);
	}
	
	/**
	 * 查询指定班组下所有使用者
	 */
	@Override
	public List<Users> findAllUser(String unit_id,int team_id) {
		return dao.findAllUser(unit_id,team_id);
	}
	
	/**
	 * 通过班组管理员名称查询班组管理员，name班组管理员名称，team_id班组编号
	 */
	@Override
	public TeamManager findTeamManagerByName(int team_id, String name,String unit_id) {
		return dao.findTeamManagerByName(team_id, name,unit_id);
	}
	
	/**
	 * 通过钥匙名称和编号查询钥匙      name钥匙名称不为null、""时有效，iid钥匙编号不为-1时有效
	 */
	@Override
	public Key findKeyByNameOrIid(String keybox_id, String name, int iid,String card_id) {
		return dao.findKeyByNameOrIid(keybox_id, name, iid,card_id);
	}
	
	/**
	 * 通过钥匙iid修改钥匙名称，iid钥匙iid编号，name不为null、""时有效
	 */
	@Override
	public int changeKeyById(String name,int iid, String keybox_id,String card_id,String usering) {
		if (usering.equals("禁用")) {
			if(card_id.matches("[0-9]{10}")) {
				UDPClient.sendLockDeploy(keybox_id, 3, iid, name,card_id);//修改钥匙名称的同时下发修改命令到对应箱
			}else {//卡号不正确，要0代替
				UDPClient.sendLockDeploy(keybox_id, 3, iid, name,"0000000000");//修改钥匙名称的同时下发修改命令到对应箱
			}
		}
		return dao.changeKeyById(name,iid, keybox_id,card_id);
	}
	
	/**
	 * 通过钥匙iid删除钥匙    iids钥匙iid数组
	 */
	@Transactional
	public int deleteKeyByIds(int[] iids, String keybox_id,String [] card_ids,String [] userings) {
		for(int a=0;a<iids.length;a++) {//删除钥匙的同时下发删除命令
			if(card_ids[a].matches("[0-9]{10}")) {
				UDPClient.sendLockDeploy(keybox_id, 2, iids[a], "0",card_ids[a]);
			}else {
				UDPClient.sendLockDeploy(keybox_id, 2, iids[a], "0","0000000000");
			}
		}
		return dao.deleteKeyByIds(iids, keybox_id);
	}
	
	/**
	 * 查询指定箱下所有的钥匙   
	 */
	@Override
	public List<Key> findKeys(String keybox_id) {
		return dao.findKeys(keybox_id);
	}
	
	/**
	 * 添加钥匙
	 */
	@Override
	public int addKey(Key key) {
		return dao.addKey(key);
	}
	
	/**
	 * 查询指定箱下的多个钥匙，start查询开始的位置
	 */
	@Override
	public List<Key> findKey(String keybox_id, int start) {
		return dao.findKey(keybox_id, start);
	}
	
	/**
	 * 通过箱maxc查询箱    keybox_id箱maxc
	 */
	@Override
	public KeyBoxInfo findKeyBoxById(String keybox_id) {
		return dao.findKeyBoxById(keybox_id);
	}
	
	/**
	 * 通过箱的名称查询箱        name箱的名称
	 */
	@Override
	public KeyBox findKeyBoxByName(String name, int team_id,String unit_id) {
		return dao.findKeyBoxByName(name, team_id,unit_id);
	}
	
	/**
	 * 通过箱的id修改箱的名称        name修改后的箱名称 ，id箱的id
	 */
	@Override
	public int changeKeyBoxById(String name, long id) {
		return dao.changeKeyBoxById(name, id);
	}
	
	/**
	 * 通过maxc地址删除箱，maxcs箱的maxc地址数组
	 */
	@Transactional
	public int deleteKeyBoxByIds(String[] maxcs) {
		return dao.deleteKeyBoxByIds(maxcs);
	}
	
	/**
	 * 查询指定箱的maxc
	 */
	@Override
	public String findMaxc(String maxc) {
		return dao.findMaxc(maxc);
	}
	
	/**
	 * 查询指定班组下所有箱
	 */
	@Override
	public List<KeyBox> findKeyBoxs(int team_id,String unit_id) {
		return dao.findKeyBoxs(team_id,unit_id);
	}
	
	/**
	 * 添加箱
	 * 添加箱的同时查出所属班组可使用的管理员卡，并下发到箱
	 */
	@Transactional
	public long addKeyBox(KeyBox1 keyBox) {
		dao.addKeyBox(keyBox);
		
		int [] arr = new int [149];
		for(int a=0;a<149;a++) {
			arr[a] = a;
		}
		dao.addAllkey(keyBox.getMaxc(),keyBox.getUnit_id(),keyBox.getTeam_id(),arr);
		return keyBox.getId();
	}
	
	/**
	 * 查询指定班组下多个箱
	 */
	@Override
	public List<KeyBox> findKeyBox(int team_id,int start,String unit_id) {
		return dao.findKeyBox(team_id,start,unit_id);
	}
	
	/**
	 * 通过id修改班组管理员的名称和密码，name和password不为null、""时有效
	 */
	@Override
	public int changeTeamManager(String name, String password, long id) {
		return dao.changeTeamManager(name, password, id);
	}
	
	/**
	 * 通过iid删除班组管理员
	 */
	@Transactional
	public int deleteTeamManager(int[] iids, int team_id,String unit_id) {
		List<Integer> list = dao.findUnitCardByManager(iids,unit_id,team_id);//查询要删除的班组管理员们被分配的所有授权卡iid
		
		if (list.size()>0) {//如果这些班组管理员有被分配授权卡
			dao.deleteUnitCardsAuth(list, unit_id);//断开这些授权卡的分配状态，即设置这些授权卡为未分配
			dao.deleteTeamCardByCards(list,unit_id);//清空这些授权卡可使用的班组信息，因为这些卡现在是未分配状态
			
			List<Card> keyboxCard = dao.findKeyboxCardByCardIids(unit_id, list);//查询这些授权卡可使用的箱
			for(int a=0;a<keyboxCard.size();a++) {
				UDPClient.sendCardDeploy(keyboxCard.get(a).getKeybox_id(), 2, keyboxCard.get(a).getIid(), keyboxCard.get(a).getCard_number());//下发删除命令删除对应箱中的这些授权卡
			}
			dao.deleteKeyBoxCardByCards(list,unit_id);//清空这些授权卡可使用的箱信息
		}
		return dao.deleteTeamManager(iids, team_id,unit_id);
	}
	
	/**
	 * 查询指定班组的所有班组管理员iid
	 */
	@Override
	public List<Integer> findAllTeamManagerIid(String unit_id, int team_id) {
		return dao.findAllTeamManagerIid(unit_id, team_id);
	}
	
	/**
	 * 添加单位管理员
	 */
	@Override
	public int addTeamManager(TeamManager teamManager) {
		return dao.addTeamManager(teamManager);
	}
	
	/**
	 * 查询指定班组下的多个班组管理员
	 */
	@Override
	public List<TeamManager> findTeamManager(int team_id, int start,String unit_id) {
		return dao.findTeamManager(team_id, start,unit_id);
	}
	
	/**
	 * 通过名称查询使用者     name使用者名称
	 */
	@Override
	public Users findUsersByName(String name, int team_id,String unit_id) {
		return dao.findUsersByName(name, team_id,unit_id);
	}
	
	/**
	 * 通过id修改使用者名称        id使用者id， name修改后的名称
	 */
	@Override
	public int changeUser(long id, String name) {
		return dao.changeUser(id, name);
	}
	
	/**
	 * 通过iid删除使用者      iids使用者iid数组
	 */
	@Transactional
	public int deleteUsers(int[] iids, int team_id,String unit_id) {
		List<KeyBox> list = dao.findKeyBoxs(team_id, unit_id);
		for(int a=0;a<list.size();a++) {
			for(int b=0;b<iids.length;b++) {
				UDPClient.sendUserDeploy(list.get(a).getMaxc(), 2, iids[b], "0");//将删除命令下发到该班组下所有的箱
			}
		}
		return dao.deleteUsers(iids, team_id,unit_id);
	}
	
	/**
	 * 添加使用者
	 */
	@Transactional
	public int addUsers(int iid, int team_id, String name, String unit_id) {
		List<KeyBox> list = dao.findKeyBoxs(team_id, unit_id);
		for(int a=0;a<list.size();a++) {
			UDPClient.sendUserDeploy(list.get(a).getMaxc(), 1, iid, name);//将添加钥用户下发到班组下所有的箱
		}
		return dao.addUsers(iid, team_id, name, unit_id);
	}
	
	/**
	 * 查询指定班组下多个使用者
	 */
	@Override
	public List<Users> findUser(String unit_id,int team_id, int start) {
		return dao.findUser(unit_id,team_id, start);
	}
	
	/**
	 * 通过名称查询班组
	 */
	@Override
	public Team findTeamByName(String name, String unit_id) {
		return dao.findTeamByName(name, unit_id);
	}
	
	/**
	 * 通过id修改班组名称
	 */
	@Override
	public int changeTeamById(long id, String change_name) {
		return dao.changeTeamById(id, change_name);
	}
	
	/**
	 * 通过iid删除班组      
	 */
	@Transactional
	public int deleteTeamByIds(int[] iids,String unit_id) {
		return dao.deleteTeamByIds(iids,unit_id);
	}
	
	/**
	 * 查询指定单位管理员所能操作开锁的所有班组 ，unit_manager_id指定的单位管理员编号
	 */
	@Override
	public List<Integer> findAllUnitManagerAuth(int unit_manager_id, String unit_id) {
		return dao.findAllUnitManagerAuth(unit_manager_id, unit_id);
	}

	/**
	 * 修改单位管理员可操作的班组，list：单位管理员新增的可操作班组iid集合，auths：单位管理员要去除的班组iid数组
	 */
	@Transactional
	public int addUnitManagerAuth(int unit_manager_id, List<Integer> list, List<Integer> auths,String unit_id) {
		int i = 9999;
		UnitCard unitCard = dao.findUnitCardByManager_id(unit_manager_id,unit_id,-1);//查询该单位管理员是否绑定卡（单位管理员所属班组为-1）
		
		if (list.size()!=0) {
			i = dao.addUnitManagerAuth(unit_manager_id, list,unit_id);//添加单位管理员可操作的班组权限
			if (unitCard!=null) {//如果绑定了授权卡添加这些班组及其下箱对该授权卡的使用权限
				dao.addTeamCard(list,unit_id,unitCard.getIid());//添加这些班组对该授权卡的使用权限
				List<String> list2 = dao.findAllkeybox_number(list,unit_id);
				if (list2.size()>0) {
					dao.addKeyBoxCard(list2,unit_id,unitCard.getIid());//添加这些班组下箱对该授权卡的使用权限
					for(int a=0;a<list2.size();a++) {//把添加这些卡的使用权限下发到箱
						UDPClient.sendCardDeploy(list2.get(a), 1, unitCard.getIid(), unitCard.getCard_number());
					}
				}
			}
		}
		if (auths.size()!=0) {
			i = dao.deleteUnitManagerAuth(unit_manager_id,auths,unit_id);
			if (unitCard!=null) {//如果绑定了授权卡删除这些班组及其下箱对该授权卡的使用权限
				dao.deleteTeamCardByTeams(auths,unitCard.getIid(),unit_id);//删除这些班组对该授权卡的使用权限
				List<String> list2 = dao.findAllkeybox_number(auths,unit_id);
				if (list2.size()>0) {
					dao.deleteKeyBoxCardByKeyBoxs(list2,unitCard.getIid(),unit_id);//删除这些班组下箱对该授权卡的使用权限
					for(int a=0;a<list2.size();a++) {//把删除这些卡的使用权限下发到箱
						UDPClient.sendCardDeploy(list2.get(a), 2, unitCard.getIid(), unitCard.getCard_number());
					}
				}
			}
		}
		return i;
	}
	
	/**
	 * 通过名称或者iid查询班组
	 */
	@Override
	public Team findTeamByNameOrIid(String name, String unit_id,int iid) {
		return dao.findTeamByNameOrIid(name, unit_id,iid);
	}
	
	/**
	 * 查询指定单位下班组的个数
	 */
	@Override
	public int findTeamCount(String unit_id) {
		return dao.findTeamCount(unit_id);
	}
	
	/**
	 * 添加班组
	 */
	@Override
	public int addTeam(Team team) {
		return dao.addTeam(team);
	}
	
	/**
	 * 查询单位下多个班组 
	 */
	@Override
	public List<Team> findTeam(String unit_id, int start) {
		return dao.findTeam(unit_id, start);
	}
	
	/**
	 * 通过名称查询单位管理员
	 */
	@Override
	public UnitManager findUnitManagerByName(String unit_id, String name) {
		return dao.findUnitManagerByName(unit_id, name);
	}
	
	/**
	 * 修改单位管理员的名称和密码，当name和password不为null、""时有效
	 */
	@Override
	public int changeUnitManager(String name,String password, long id) {
		return dao.changeUnitManager(name,password, id);
	}
	
	/**
	 * 删除单位管理员
	 */
	@Transactional
	public int deleteUnitManager(int[] iids,String unit_id) {
		List<Integer> list = dao.findUnitCardByManager(iids,unit_id,-1);//单位管理员所属班组为-1，查询多个单位管理员所使用的所有授权卡iid
		
		if (list.size()>0) {//如果这些管理员有分配的授权卡
			dao.deleteUnitCardsAuth(list,unit_id);//删除这些授权卡对单位管理员的配置，设置这些授权卡位未授权
			dao.deleteTeamCardByCards(list,unit_id);//删除班组对这些授权卡的使用
			
			List<Card> keyboxCard = dao.findKeyboxCardByCardIids(unit_id, list);
			for(int a=0;a<keyboxCard.size();a++) {//把这些卡的删除命令下发到对应箱
				UDPClient.sendCardDeploy(keyboxCard.get(a).getKeybox_id(), 2, keyboxCard.get(a).getIid(), keyboxCard.get(a).getCard_number());
			}
			dao.deleteKeyBoxCardByCards(list,unit_id);//删除箱对这些授权卡的使用
		}
		dao.deleteUnitManagerAuth2(iids,unit_id);//同时删除该单位管理员对班组的操作权限
		return dao.deleteUnitManager(iids,unit_id);
	}
	
	/**
	 * 查询单位管理员的名称(包括单位管理员名和班组管理员名)或者编号是否已存在
	 */
	@Override
	public List<Manager> findUnitManagerByNamaOrIid(String unit_id, String name, int iid) {
		return dao.findUnitManagerByNamaOrIid(unit_id, name, iid);
	}
	
	/**
	 * 查询单位管理员的总数
	 */
	@Override
	public int findUnitManagerCount(String unit_id) {
		return dao.findUnitManagerCount(unit_id);
	}
	
	/**
	 * 添加单位管理员
	 */
	@Override
	public int addUnitManager(UnitManager unitManager) {
		return dao.addUnitManager(unitManager);
	}
	
	/**
	 * 查询单位下多个单位管理员
	 */
	@Override
	public List<UnitManager> findUnitManager(String unit_id,int start) {
		return dao.findUnitManager(unit_id,start);
	}
	
	/**
	 * 查询所有的管理员（包括单位管理员和班组管理员）
	 */
	@Override
	public List<Manager> findAllManager(String unit_id) {
		return dao.findAllManager(unit_id);
	}
	
	/**
	 * 查询所有已分配授权卡的管理员
	 */
	@Override
	public List<String> findAllManagerHashCard(String unit_id) {
		return dao.findAllManagerHashCard(unit_id);
	}
	
	/**
	 * 通过单位卡iid删除单位卡
	 */
	@Transactional
	public int deleteUnitCards(List<Integer> iids,String unit_id) {
		dao.deleteTeamCardByCards(iids, unit_id);//删除班组对这张卡的使用
		List<Card> keyboxCard = dao.findKeyboxCardByCardIids(unit_id, iids);//查询这张卡可使用的所有箱
		for(int a=0;a<keyboxCard.size();a++) {//删除卡命令下发到对应的箱
			UDPClient.sendCardDeploy(keyboxCard.get(a).getKeybox_id(), 2, keyboxCard.get(a).getIid(), keyboxCard.get(a).getCard_number());
		}
		dao.deleteKeyBoxCardByCards(iids, unit_id);//删除箱对这张卡的使用
		return dao.deleteUnitCards(iids,unit_id);
	}
	
	/**
	 * 查询所有的单位卡总数
	 */
	@Override
	public int findAllUnitCardCount(String unit_id) {
		return dao.findAllUnitCardCount(unit_id);
	}
	
	/**
	 * 查询卡编号是否已存在（包括单位卡和普通卡），或者单位卡iid是否存在
	 */
	@Override
	public List<UnitCard> findCardByNumberOrIid(String unit_id, String card_number,int iid) {
		return dao.findCardByNumberOrIid(unit_id, card_number,iid);
	}
	
	/**
	 * 添加单位卡
	 */
	@Override
	public int addUnitCard(String card_number, String unit_id,int iid) {
		return dao.addUnitCard(card_number, unit_id,iid);
	}
	
	/**
	 * 查询单位下的多个单位卡
	 */
	@Override
	public List<UnitCard> findUnitCard(String unit_id, int start) {
		return dao.findUnitCard(unit_id, start);
	}
	
	/**
	 * 通过卡编号查询单位卡
	 */
	@Override
	public UnitCard findUnitCardByNumber(String card_number, String unit_id) {
		return dao.findUnitCardByNumber(card_number, unit_id);
	}
	
	/**
	 * 把单位卡重新分配管理员
	 */
	@Transactional
	public int changeAuth(int iid, int auth_id, String auth_name,String card_number,int style,String unit_id) {
		List<String> maxcs = dao.findKeybox_idByCardIid(unit_id, iid);//查询这张卡可使用的箱
		for(int a=0;a<maxcs.size();a++) {//把删除卡命令下发到对应的箱
			UDPClient.sendCardDeploy(maxcs.get(a), 2, iid, card_number);
		}
		dao.deleteTeamCardByCard(iid,unit_id);//删除班组对这张卡的使用
		dao.deleteKeyBoxCardByCard(iid,unit_id);//删除箱对这张卡的使用
	
		if (style==-1) {//style表示班组iid -1为不属于如何班组即单位管理员
			List<Integer> list = dao.findAllUnitManagerAuth(auth_id,unit_id);//查询这个单位管理员可操作的班组
			if (list.size()>0) {
				dao.addTeamCard(list,unit_id,iid);//把这张卡分配到这些班组
				List<String> list2 = dao.findAllkeybox_number(list,unit_id);//查询这些班组下所有的箱
				
				if (list2.size()>0) {
					dao.addKeyBoxCard(list2,unit_id,iid);//把这张卡分配到这些箱
				}
				for(int a=0;a<list2.size();a++) {//把这张卡下发到这些箱
					UDPClient.sendCardDeploy(list2.get(a), 1, iid, card_number);
				}
			}
		}else{//班组管理员
			List<Integer> list = new ArrayList<Integer>();
			list.add(style);
			dao.addTeamCard(list,unit_id,iid);//把这张卡分配到这些班组
			
			List<String> list2 = dao.findAllkeybox_number(list,unit_id);//查询这些班组下所有的箱
			if (list2.size()>0) {
				dao.addKeyBoxCard(list2,unit_id,iid);//把这张卡分配到这些箱
			}
			for(int a=0;a<list2.size();a++) {//把这张卡下发到这些箱
				UDPClient.sendCardDeploy(list2.get(a), 1, iid, card_number);
			}
		}
		return dao.changeAuth(iid, auth_id, auth_name,unit_id,style);//修改卡分配的管理员
	}
	
	/**
	 * 通过名称查询单位
	 */
	@Override
	public Unit findUnitByName(String name) {
		return dao.findUnitByName(name);
	}
	
	/**
	 * 通过id修改单位名称
	 */
	@Override
	public int changeUnitById(long change_id, String change_name) {
		return dao.changeUnitById(change_id, change_name);
	}
	
	/**
	 * 通过单位编号删除单位
	 */
	@Transactional
	public int deleteUnitByIds(String[] unit_numbers) {
		return dao.deleteUnitByIds(unit_numbers);
	}
	
	/**
	 * 通过名称修改调试者的密码
	 */
	@Override
	public int changeLevel(String password,String name) {
		return dao.changeLevel(password,name);
	}
	
	/**
	 * 通过id修改单位管理员的密码
	 */
	@Override
	public int changeUnit(String password, long id) {
		return dao.changeUnit(password, id);
	}
	
	/**
	 * 通过id修改班组管理员的密码
	 */
	@Override
	public int changeTeam(String password,long id) {
		return dao.changeTeam(password,id);
	}
	
	/**
	 * 添加单位
	 */
	@Override
	public int addUnit(Unit unit) {
		return dao.addUnit(unit);
	}
	
	/**
	 * 查出当前所在单位
	 */
	@Override
	public Unit findUnit() {
		return dao.findUnit();
	}
	
	/**
	 * 查询指定名称的班组管理员是否存在
	 */
	@Override
	public String validAuth(String unit_id, int team_id, String managername) {
		return dao.validAuth(unit_id, team_id, managername);
	}
	
	/**
	 * 通过名称查询调试者
	 */
	@Override
	public Config findLevel(String name) {
		return dao.findLevel(name);
	}
	
	/**
	 * 通过id修改调试者remember
	 */
	@Override
	public int updataLevel(long id, int remember) {
		return dao.updataLevel(id, remember);
	}
	
	/**
	 * 获取指定名称的单位管理员密码
	 */
	@Override
	public UnitManager getUnitPassword(String name) {
		return dao.getUnitPassword(name);
	}
	
	/**
	 * 通过id修改单位管理员的remember
	 */
	@Override
	public int updataUnitManager(long id, int remember) {
		return dao.updataUnitManager(id, remember);
	}
	
	/**
	 * 通过名称获取班组管理员密码
	 */
	@Override
	public TeamManager getTeamPassword(String name) {
		return dao.getTeamPassword(name);
	}
	
	/**
	 * 通过id修改班组管理员的remember
	 */
	@Override
	public int updataTeamManager(long id, int remember) {
		return dao.updataTeamManager(id, remember);
	}

	/**
	 * 配置箱的ip、端口等参数
	 */
	@Override
	public int updateKeyBox(KeyBoxInfo keyBox) {
		UDPClient.sendSetIpConfige(keyBox, 1);//下发ip配置命令
		return dao.updateKeyBox(keyBox);
	}

	/**
	 * 设置监听端口
	 */
	@Override
	public int setConfigPort(int port) {
		return dao.setConfigPort(port);
	}

	/**
	 * 设置备份间隔天数和修改时间
	 */
	@Override
	public int setConfigTime(long millis, int day) {
		return dao.setConfigTime(millis, day);
	}

	/**
	 * 通过id修改箱的子设备故障状态
	 */
	@Override
	public int updateKeyBoxFaulById(long id, String fault_number) {
		return dao.updateKeyBoxFaulById(id, fault_number);
	}

	/**
	 * 通过id修改箱的在线状态
	 */
	@Override
	public int updateKeyBoxOnlineById(long id, String onlines) {
		return dao.updateKeyBoxOnlineById(id, onlines);
	}

	/**
	 * 通过钥匙iid修改其状态
	 */
	@Override
	public int updateKeyStateByIid(String keybox_id,int iid, int keyss_state) {
		return dao.updateKeyStateByIid(keybox_id,iid,keyss_state);
	}

	/**
	 * 查询指定箱下的所有钥匙
	 */
	@Override
	public List<Key> findKeysByMaxc(String keybox_id) {
		return dao.findKeysByMaxc(keybox_id);
	}

	/**
	 * 通过id修改钥匙状态
	 * list：钥匙对象集合，包含钥匙状态和id
	 */
	@Transactional
	public int changekeysState(List<Key> list) {
		return dao.changekeysState(list);
	}

	/**
	 * 通过iid查询使用者名称
	 */
	@Override
	public String findUserNamebyIid(String unit_id, int team_id, int iid) {
		return dao.findUserNamebyIid(unit_id, team_id, iid);
	}

	/**
	 * 通过iid查询班组管理员名称
	 */
	@Override
	public String findTeamManagerNameByIid(String unit_id, int team_id, int iid) {
		return dao.findTeamManagerNameByIid(unit_id, team_id, iid);
	}

	/**
	 * 通过iid查询单位管理员名称
	 */
	@Override
	public String findUnitManagerNameByIid(String unit_id, int iid) {
		return dao.findUnitManagerNameByIid(unit_id, iid);
	}

	/**
	 * 通过钥匙iid查询钥匙名称
	 */
	@Override
	public String findKeyNameByIid(String keybox_id, int iid) {
		return dao.findKeyNameByIid(keybox_id, iid);
	}


	/**
	 * 查询指定卡可操作的所有箱        iid卡iid编号
	 */
	@Override
	public List<String> findKeybox_idByCardIid(String unit_id, int iid) {
		return dao.findKeybox_idByCardIid(unit_id,iid);
	}

	/**
	 * 查询指定卡可操作的所有箱        iids卡iid编号数组
	 */
	@Override
	public List<String> findKeybox_idByCardIids(String unit_id, List<Integer> iids) {
		return dao.findKeybox_idByCardIids(unit_id, iids);
	}

	/**
	 * 查询指定授权卡iid的箱可操作授权卡对象（KeyboxCard对象）
	 */
	@Override
	public List<Card> findKeyboxCardByCardIids(String unit_id, List<Integer> iids) {
		return dao.findKeyboxCardByCardIids(unit_id, iids);
	}

	/**
	 * 查询单位下所有记录 style 为0表示实时记录 1表示历史记录
	 */
	@Override
	public List<Logs> getAllLogs(int style) {
		return dao.getAllLogs(style);
	}

	/**
	 * 查询指定班组所有记录 style 为0表示实时记录 1表示历史记录
	 */
	@Override
	public List<Logs> getAllLogsByTeam_id(int team_id, String unit_id,int style) {
		return dao.getAllLogsByTeam_id(team_id,unit_id,style);
	}

	/**
	 * 查询指定箱所有记录 style 为0表示实时记录 1表示历史记录
	 */
	@Override
	public List<Logs> getAllLogsByKeybox_id(String Keybox_id,int style) {
		return dao.getAllLogsByKeybox_id(Keybox_id,style);
	}

	/**
	 * 查询指定钥匙所有记录 style 为0表示实时记录 1表示历史记录
	 */
	@Override
	public List<Logs> getAllLogsByKeyss_id(int keyss_id, String keybox_id,int style) {
		return dao.getAllLogsByKeyss_id(keyss_id, keybox_id,style);
	}

	/**
	 * 备份数据文件到C:/lock/copyData文件夹
	 */
	@Transactional
	public void copyData() throws IOException {
		Unit unit = dao.findUnit();
		File file2 = new File("C:\\lock/copyData/"+unit.getUnit_number()+".txt");
		file2.createNewFile();
		dao.copyData();
	}

	/**
	 * 添加记录
	 */
	@Override
	public int addLogs(Logs logs) {
		return dao.addLogs(logs);
	}

	/**
	 * 下发指定箱可以使用的卡到箱
	 */
	@Transactional
	public void sendAllCard(String maxc,String unit_id,int team_id) {
		List<UnitCard> list = dao.findCardByTeam(team_id,unit_id);//查询这个班组所有可使用的单位卡
		List<Card> findCard = findCard(maxc);//查询这个箱所有可使用的普通卡
		
		ServletContextLTest.keyboxList.updateGetAllBack(maxc,list.size()+findCard.size()+1);
		
		for(int a=0;a<list.size();a++) {//下发单位卡添加命令到箱
			UDPClient.sendCardDeploy(maxc, 1, list.get(a).getIid(), list.get(a).getCard_number());
		}
		
		for(int a=0;a<findCard.size();a++) {//下发普通卡添加命令到箱
			UDPClient.sendCardDeploy(maxc, 1, findCard.get(a).getIid(), findCard.get(a).getCard_number());
		}
	}

	/**
	 * 下发箱所有可以使用的user到箱
	 */
	@Override
	public List<Users> sendAllUser(String maxc,String unit_id,int team_id) {
		return dao.findAllUser(unit_id, team_id);
	}

	@Override
	public List<Key> sendAllKey(String maxc) {
		return dao.sendAllKey(maxc);
	}

	@Override
	public String findKeyCard_id(String card_id) {
		return dao.findKeyCard_id(card_id);
	}

	@Override
	public int changeKeyUseringById(long id, String usering) {
		return dao.changeKeyUseringById(id, usering);
	}
}
