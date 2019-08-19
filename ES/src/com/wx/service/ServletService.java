package com.wx.service;

import java.io.IOException;
import java.util.List;


import com.wx.entity.Card;
import com.wx.entity.Config;
import com.wx.entity.Key;
import com.wx.entity.KeyBox;
import com.wx.entity.KeyBox1;
import com.wx.entity.KeyBoxInfo;
import com.wx.entity.UnitManager;
import com.wx.entity.Users;
import com.wx.utils.DatabaseBackup;
import com.wx.entity.Logs;
import com.wx.entity.Manager;
import com.wx.entity.Name;
import com.wx.entity.Team;
import com.wx.entity.TeamManager;
import com.wx.entity.Unit;
import com.wx.entity.UnitCard;

public interface ServletService {
	
	public Unit findUnit();
	public Unit findUnitByName(String name);
	public int addUnit(Unit unit);
	public int deleteUnitByIds(String [] unit_numbers);
	public int changeUnitById(long change_id,String change_name);
	public void connect(int port);
	public String stop();
	
	
	public List<Logs> getLogs(int start,int style);
	public List<Logs> getAllLogs(int style);
	public List<Logs> getLogsByTeam_id(int team_id,int start,String unit_id,int style);
	public List<Logs> getLogsByKeybox_id(String Keybox_id,int start,int style);
	public List<Logs> getLogsByKeyss_id(int keyss_id,int start,String keybox_id,int style);
	public List<Logs> getAllLogsByTeam_id(int team_id,String unit_id,int style);
	public List<Logs> getAllLogsByKeybox_id(String Keybox_id,int style);
	public List<Logs> getAllLogsByKeyss_id(int keyss_id,String keybox_id,int style);
	public int addOpenKeyLog(Logs logs);
	public int addLogs(Logs logs);
	
	public String validAuth(String unit_id,int team_id,String managername);
	

	public List<Team> findTeam(String unit_id,int start);
	public Team findTeamByNameOrIid(String name,String unit_id,int iid);
	public Team findTeamByName(String name,String unit_id);
	public int findTeamCount(String unit_id);
	public int addTeam(Team team);
	public int deleteTeamByIds(int [] iids,String unit_id);

	
	
	public void sendAllCard(String maxc,String unit_id,int team_id);
	public List<Users> sendAllUser(String maxc,String unit_id,int team_id);
	public List<Key> sendAllKey(String maxc);
	public List<KeyBox1> findAllKeyBox();
	public List<KeyBox> findKeyBox(int team_id,int start,String unit_id);
	public List<KeyBox> findKeyBoxs(int team_id,String unit_id);
	public KeyBox findKeyBoxByName(String name,int team_id,String unit_id);
	public long addKeyBox(KeyBox1 keyBox);
	public int deleteKeyBoxByIds(String[] maxcs);
	public int changeKeyBoxById(String name,long id);
	public KeyBoxInfo findKeyBoxById(String keybox_id);
	public int updateKeyBox(KeyBoxInfo keyBox);
	public int updateKeyBoxFaulById(long id,String fault_number);
	public int updateKeyBoxOnlineById(long id,String onlines);
	
	
	
	
	public String findKeyNameByIid(String keybox_id,int iid);
	public List<Key> findKey(String keybox_id,int start);
	public Key findKeyByNameOrIid(String keybox_id,String name,int iid,String card_id);
	public List<Key> findKeys(String keybox_id);
	public int addKey(Key key);
	public int deleteKeyByIds(int [] iids,String keybox_id,String [] card_ids,String [] usering);
	public int changeKeyById(String name,int iid,String keybox_id,String card_id,String usering);
	public int updateKeyStateByIid(String keybox_id,int iid,int keyss_state);
	public List<Key> findKeysByMaxc(String keybox_id);
	public int changekeysState(List<Key> list);
	public String findKeyCard_id(String card_id);
	public int changeKeyUseringById(long id,String usering);
	
	
	
	
	public List<Integer> findUnitManagerAuthByName(String managername,String unit_id);
	
	
	
	public String findUnitManagerNameByIid(String unit_id,int iid);
	public List<UnitManager> findUnitManager(String unit_id,int start);
	public UnitManager findUnitManagerByName(String unit_id,String name);
	public int changeUnitManager(String name,String password,long id);
	public int deleteUnitManager(int[] ids,String unit_id);
	public int updataUnitManager(long id,int remember);
	public int updataTeamManager(long id,int remember);
	public UnitManager getUnitPassword(String name);
	public TeamManager getTeamPassword(String name);
	
	
	
	public Name findName(String keybox_id);
	
	
	
	public int setConfigTime(long millis,int day);
	public Config findLevel(String name);
	public int updataLevel(long id,int remember);
	public int changeLevel(String password,String name);
	public String findTeamManagerNameByIid(String unit_id,int team_id,int iid);
	

	public UnitCard findCardByNumber(String unit_id,String card_number);
	public List<UnitCard> findCardByNumberOrIid(String unit_id,String card_number,int iid);
	public List<Integer> findIid(String keybox_id);
	public int deleteCards(int[] iids,String keybox_id);
	public int deleteUnitCards(List<Integer> iids,String unit_id);
	public int addUnitCard(String card_number,String unit_id,int iid);
	public int findAllUnitCardCount(String unit_id);
	public List<UnitCard> findUnitCard(String unit_id,int start);
	public UnitCard findUnitCardByNumber(String card_number,String unit_id);
	public List<Manager> findAllManager(String unit_id);
	public List<String> findAllManagerHashCard(String unit_id);
	public List<Manager> findUnitManagerByNamaOrIid(String unit_id,String name,int iid);
	public List<Integer> findAllUnitManagerAuth(int unit_manager_id,String unit_id);
	
	
	
	public List<Manager> findManagerByNama(String unit_id,String name);
	
	public List<Integer> findAllTeamManagerIid(String unit_id,int team_id);
	
	public int findUnitManagerCount(String unit_id);
	public int addUnitManager(UnitManager unitManager);
	public int changeAuth(int iid,int auth_id,String auth_name,String card_number,int style,String unit_id);
	public int addUnitManagerAuth(int unit_manager_id,List<Integer> list,List<Integer> auths,String unit_id);
	public int changeTeamById(long id,String change_name);
	public List<Users> findUser(String unit_id,int team_id,int start);
	public List<Users> findAllUser(String unit_id,int team_id);
	public int addUsers(int iid,int team_id,String name,String unit_id);
	
	public int deleteUsers(int[] iids,int team_id,String unit_id);
	public Users findUsersByName(String name,int team_id,String unit_id);
	public int changeUser(long id,String name);
	public int changeUnit(String password,long id);
	public int changeTeam(String password,long id);
	public List<TeamManager> findTeamManager(int team_id,int start,String unit_id);
	public int addTeamManager(TeamManager teamManager);
	public int deleteTeamManager(int[] iids,int team_id,String unit_id);
	public int changeTeamManager(String name,String password,long id);
	public TeamManager findTeamManagerByName(int team_id,String name,String unit_id);
	public Config findConfig();
	public int setConfigPort(int port);
	public String findUserNamebyIid(String unit_id,int team_id,int iid);
	
	public String getMysqlAddress();
	public DatabaseBackup getDatabaseBackup();
	public String backData(String fileUrl);
	public List<Card> findCard(String keybox_id);
	public int addCard(String keybox_id,String card_number,String unit_id,int iid,int team_id);
	public Card seachCard(String card_number,String keybox_id);
	
	
	public int changeCard(String card_number,String keybox_id,String change_number,int iid);
	public int changeUnitCard(String card_number,String change_number,String unit_id,int iid);
	public List<String> findKeybox_idByCardIid(String unit_id,int iid);
	public List<String> findKeybox_idByCardIids(String unit_id,List<Integer> iids);
	public List<Card> findKeyboxCardByCardIids(String unit_id,List<Integer> iids);
	
	
	
	
	
	public void copyData() throws IOException ;
	public String findMaxc(String maxc);
	
}
