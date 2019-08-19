package com.wx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wx.entity.Card;
import com.wx.entity.Config;
import com.wx.entity.Key;
import com.wx.entity.KeyBox;
import com.wx.entity.KeyBox1;
import com.wx.entity.KeyBoxInfo;
import com.wx.entity.UnitManager;
import com.wx.entity.Users;
import com.wx.entity.Logs;
import com.wx.entity.Manager;
import com.wx.entity.Name;
import com.wx.entity.Team;
import com.wx.entity.TeamManager;
import com.wx.entity.Unit;
import com.wx.entity.UnitCard;

public interface ServletDao {
	
	public Unit findUnit();//在单位表中查出所有单位信息，但单位只能有一个
	public Unit findUnitByName(String name);//通过名称查询单位
	public int addUnit(Unit unit);//添加单位
	public int deleteUnitByIds(@Param("unit_numbers")String [] unit_numbers);
	public int changeUnitById(@Param("id")long change_id,@Param("name")String change_name);
	
	
	public int addTeam(Team team);
	public List<Team> findTeam(@Param("unit_id")String unit_id,@Param("start")int start);
	public int deleteTeamByIds(@Param("iids")int [] iids,@Param("unit_id")String unit_id);
	
	
	public int updataTeamManager(@Param("id")long id,@Param("remember")int remember);
	public String validAuth(@Param("unit_id")String unit_id,@Param("team_id")int team_id,@Param("managername")String managername);
	public TeamManager getTeamPassword(String name);
	
	
	
	public List<KeyBox1> findAllKeyBox();
	public List<KeyBox> findKeyBox(@Param("team_id")int team_id,@Param("start")int start,@Param("unit_id")String unit_id);
	public List<KeyBox> findKeyBoxs(@Param("team_id")int team_id,@Param("unit_id")String unit_id);
	public KeyBox findKeyBoxByName(@Param("name")String name,@Param("team_id")int team_id,@Param("unit_id")String unit_id);
	public int addKeyBox(KeyBox1 keyBox);
	public int deleteKeyBoxByIds(@Param("maxcs")String[] maxcs);
	public int changeKeyBoxById(@Param("name")String name,@Param("id")long id);
	public KeyBoxInfo findKeyBoxById(String keybox_id);
	public int updateKeyBox(KeyBoxInfo keyBox);
	public int updateKeyBoxFaulById(@Param("id")long id,@Param("fault_number")String fault_number);
	public int updateKeyBoxOnlineById(@Param("id")long id,@Param("onlines")String onlines);
	
	
	
	public List<Key> findKey(@Param("keybox_id")String keybox_id,@Param("start")int start);
	public Key findKeyByNameOrIid(@Param("keybox_id")String keybox_id,@Param("name")String name,@Param("iid")int iid,@Param("card_id")String card_id);
	public List<Key> findKeys(String keybox_id);
	public List<Key> sendAllKey(String keybox_id);
	public int addKey(Key key);
	public int addAllkey(@Param("keybox_id")String keybox_id,@Param("unit_id")String unit_id,@Param("team_id")int team_id,@Param("arr")int [] arr);
	public int deleteKeyByIds(@Param("iids")int [] iids,@Param("keybox_id")String keybox_id);
	public int changeKeyById(@Param("name")String name,@Param("iid")int iid,@Param("keybox_id")String keybox_id,@Param("card_id")String card_id);
	public int updateKeyStateByIid(@Param("keybox_id")String keybox_id,@Param("iid")int iid,@Param("keyss_state")int keyss_state);
	public List<Key> findKeysByMaxc(@Param("keybox_id")String keybox_id);
	public int changekeysState(@Param("list")List<Key> list);
	public String findKeyNameByIid(@Param("keybox_id")String keybox_id,@Param("iid")int iid);
	public String findKeyCard_id(String card_id);
	public int changeKeyUseringById(@Param("id")long id,@Param("usering")String usering);
	
	
	
	
	public List<Integer> findUnitManagerAuthByName(@Param("managername")String managername,@Param("unit_id")String unit_id);
	public List<UnitManager> findUnitManager(@Param("unit_id")String unit_id,@Param("start")int start);
	public UnitManager findUnitManagerByName(@Param("unit_id")String unit_id,@Param("name")String name);
	public int changeUnitManager(@Param("name")String name,@Param("password")String password,@Param("id")long id);
	public int deleteUnitManager(@Param("iids")int[] iids,@Param("unit_id")String unit_id);
	public int updataUnitManager(@Param("id")long id,@Param("remember")int remember);
	public UnitManager getUnitPassword(String name);
	public String findUnitManagerNameByIid(@Param("unit_id")String unit_id,@Param("iid")int iid);
	
	
	public List<Logs> getLogs(@Param("start")int start,@Param("style")int style);
	public List<Logs> getAllLogs(int style);
	public List<Logs> getLogsByTeam_id(@Param("team_id")int team_id,@Param("start")int start,@Param("unit_id")String unit_id,@Param("style")int style);
	public List<Logs> getLogsByKeybox_id(@Param("keybox_id")String Keybox_id,@Param("start")int start,@Param("style")int style);
	public List<Logs> getLogsByKeyss_id(@Param("keyss_id")int keyss_id,@Param("start")int start,@Param("keybox_id")String keybox_id,@Param("style")int style);
	public List<Logs> getAllLogsByTeam_id(@Param("team_id")int team_id,@Param("unit_id")String unit_id,@Param("style")int style);
	public List<Logs> getAllLogsByKeybox_id(@Param("keybox_id")String Keybox_id,@Param("style")int style);
	public List<Logs> getAllLogsByKeyss_id(@Param("keyss_id")int keyss_id,@Param("keybox_id")String keybox_id,@Param("style")int style);
	public int addOpenKeyLog(Logs logs);
	public int addLogs(Logs logs);
	
	
	public Name findName(String keybox_id);
	
	
	
	public Config findLevel(String name);
	public int updataLevel(@Param("id")long id,@Param("remember")int remember);
	public int changeLevel(@Param("password")String password,@Param("name")String name);


	
	public int deleteUnitCards(@Param("iids")List<Integer> iids,@Param("unit_id")String unit_id);
	public int addUnitCard(@Param("card_number")String card_number,@Param("unit_id")String unit_id,@Param("iid")int iid);
	public int findAllUnitCardCount(String unit_id);
	public List<UnitCard> findUnitCard(@Param("unit_id")String unit_id,@Param("start")int start);
	public UnitCard findUnitCardByNumber(@Param("card_number")String card_number,@Param("unit_id")String unit_id);
	public UnitCard findCardByNumber(@Param("unit_id")String unit_id,@Param("card_number")String card_number);
	public List<UnitCard> findCardByNumberOrIid(@Param("unit_id")String unit_id,@Param("card_number")String card_number,@Param("iid")int iid);
	
	
	
	public int deleteCards(@Param("iids")int[] iids,@Param("keybox_id")String keybox_id);
	public int changeCard(@Param("card_number")String card_number,@Param("keybox_id")String keybox_id,@Param("change_number")String change_number);
	public String findCardByIid(@Param("keybox_id")String keybox_id,@Param("iid")int iid);
	
	
	public List<Manager> findAllManager(String unit_id);
	public List<String> findAllManagerHashCard(String unit_id);
	public List<Manager> findUnitManagerByNamaOrIid(@Param("unit_id")String unit_id,@Param("name")String name,@Param("iid")int iid);
	public List<Manager> findManagerByNama(@Param("unit_id")String unit_id,@Param("name")String name);
	public int findUnitManagerCount(String unit_id);
	public List<Integer> findAllTeamManagerIid(@Param("unit_id")String unit_id,@Param("team_id")int team_id);
	
	public int addUnitManager(UnitManager unitManager);
	public int changeAuth(@Param("iid")int iid,@Param("auth_id")int auth_id,@Param("auth_name")String auth_name,@Param("unit_id")String unit_id,@Param("team_id")int team_id);
	public Team findTeamByNameOrIid(@Param("name")String name,@Param("unit_id")String unit_id,@Param("iid")int iid);
	public Team findTeamByName(@Param("name")String name,@Param("unit_id")String unit_id);
	
	public int deleteUnitCardsAuth(@Param("iids")List<Integer> iids,@Param("unit_id")String unit_id);
	
	public int findTeamCount(String unit_id);
	public List<Integer> findAllUnitManagerAuth(@Param("unit_manager_id")int unit_manager_id,@Param("unit_id")String unit_id);
	public int addUnitManagerAuth(@Param("unit_manager_id")int unit_manager_id,@Param("list")List<Integer> list,@Param("unit_id")String unit_id);
	public int deleteUnitManagerAuth(@Param("unit_manager_id")int unit_manager_id,@Param("list")List<Integer> auths,@Param("unit_id")String unit_id);
	public int deleteUnitManagerAuth2(@Param("iids")int[] iids,@Param("unit_id")String unit_id);
	public int changeTeamById(@Param("id")long id,@Param("name")String change_name);
	public List<Users> findUser(@Param("unit_id")String unit_id,@Param("team_id")int team_id,@Param("start")int start);
	public List<Users> findAllUser(@Param("unit_id")String unit_id,@Param("team_id")int team_id);
	public int addUsers(@Param("iid")int iid,@Param("team_id")int team_id,@Param("name")String name,@Param("unit_id")String unit_id);
	public int deleteUsers(@Param("iids")int[] iids,@Param("team_id")int team_id,@Param("unit_id")String unit_id);
	public int changeUser(@Param("id")long id,@Param("name")String name);
	public int changeUnit(@Param("password")String password,@Param("id")long id);
	public int changeTeam(@Param("password")String password,@Param("id")long id);
	public Users findUsersByName(@Param("name")String name,@Param("team_id")int team_id,@Param("unit_id")String unit_id);
	public List<TeamManager> findTeamManager(@Param("team_id")int team_id,@Param("start")int start,@Param("unit_id")String unit_id);
	public List<UnitCard> findCardByTeam(@Param("team_id")int team_id,@Param("unit_id")String unit_id);
	public String findUserNamebyIid(@Param("unit_id")String unit_id,@Param("team_id")int team_id,@Param("iid")int iid);
	
	
	
	public int addTeamManager(TeamManager teamManager);
	public int deleteTeamManager(@Param("iids")int[] iids,@Param("team_id")int team_id,@Param("unit_id")String unit_id);
	public int changeTeamManager(@Param("name")String name,@Param("password")String password,@Param("id")long id);
	public TeamManager findTeamManagerByName(@Param("team_id")int team_id,@Param("name")String name,@Param("unit_id")String unit_id);
	public Config findConfig();
	public int setConfigTime(@Param("millis")long millis,@Param("day")int day);
	public int setConfigPort(@Param("port")int port);
	
	public String getMysqlDir();
	
	public List<Card> findCard(String keybox_id);
	public List<Integer> findIid(String keybox_id);
	public int addCard(@Param("keybox_id")String keybox_id,@Param("card_number")String card_number,@Param("unit_id")String unit_id,@Param("iid")int iid,@Param("team_id")int team_id);
	public Card seachCard(@Param("card_number")String card_number,@Param("keybox_id")String keybox_id);
	public int changeUnitCard(@Param("card_number")String card_number,@Param("change_number")String change_number,@Param("unit_id")String unit_id);
	
	public int deleteTeamCardByCard(@Param("iid")int iid,@Param("unit_id")String unit_id);
	public int deleteKeyBoxCardByCard(@Param("card_iid")int card_iid,@Param("unit_id")String unit_id);
	public int deleteTeamCardByCards(@Param("list")List<Integer> list,@Param("unit_id")String unit_id);
	public int deleteKeyBoxCardByCards(@Param("list")List<Integer> list,@Param("unit_id")String unit_id);
	public int deleteTeamCardByTeams(@Param("list")List<Integer> list,@Param("card_iid")int card_iid,@Param("unit_id")String unit_id);
	public int deleteKeyBoxCardByKeyBoxs(@Param("list")List<String> list,@Param("card_iid")int card_iid,@Param("unit_id")String unit_id);
	public int deleteKeyBoxCardByKeyBox(@Param("unit_id")String unit_id,@Param("keybox_id")String maxc);
	
	
	public int addTeamCard(@Param("list")List<Integer> list,@Param("unit_id")String unit_id,@Param("iid")int iid);
	public List<String> findAllkeybox_number(@Param("list")List<Integer> list,@Param("unit_id")String unit_id);
	public int addKeyBoxCard(@Param("list")List<String> list,@Param("unit_id")String unit_id,@Param("iid")int iid);
	public int addKeyBoxCard2(@Param("unit_id")String unit_id,@Param("maxc")String maxc,@Param("list")List<UnitCard> list);
	public String findTeamManagerNameByIid(@Param("unit_id")String unit_id,@Param("team_id")int team_id,@Param("iid")int iid);
	public List<String> findKeybox_idByCardIid(@Param("unit_id")String unit_id,@Param("iid")int iid);
	public List<String> findKeybox_idByCardIids(@Param("unit_id")String unit_id,@Param("iid")List<Integer> iids);
	public List<Card> findKeyboxCardByCardIids(@Param("unit_id")String unit_id,@Param("list")List<Integer> iids);
	
	
	public String findMaxc(String maxc);
	public List<Integer> findUnitCardByManager(@Param("iids")int [] iids,@Param("unit_id")String unit_id,@Param("team_id")int team_id);
	public UnitCard findUnitCardByManager_id(@Param("auth_id")int auth_id,@Param("unit_id")String unit_id,@Param("team_id")int team_id);

	
	public void copyData();
	public String findAllKeyBox(String maxc);
}
