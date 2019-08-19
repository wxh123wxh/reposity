package com.wx.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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



public interface ServletDao {
	//对equipment的操作		设备表
	/**
	 * 通过openId查询管理员表中对应的设备的maxc_id,managerId
	 * 通过这个方法可以判断openId是不是管理员，或者查询openId管理的设备的maxc_id集合
	 * @param openId
	 * @return
	 */
	public Open findInfoByOpenId(String openId);
	public void updataNckname(@Param("managerId")String managerId,@Param("nickname")String nickname);
	public Sub_Equipment findSub128(String Maxc_id);//查询设备是否有128子设备
	public Sub_Equipment findSub0(String Maxc_id);//查询设备是否有0号子设备
	/**
	 * 查询子设备和其对应主设备的name
	 * @param maxc_id 主设备maxc_id
	 * @param sub_id  子设备编码
	 * @return
	 */
	public Name getNames(@Param("maxc_id")String maxc_id,@Param("sub_id")Integer sub_id);
	/**
	 * 查询防区和其对应的子设备、主设备name
	 * @param maxc_id  主设备maxc_id
	 * @param sub_id  子设备编码
	 * @param sec_id  防区编码
	 * @return
	 */
	public Name getNames2(@Param("maxc_id")String maxc_id,@Param("sub_id")Integer sub_id,@Param("sec_id")Integer sec_id);
	/**
	 * 删除管理员为openId的所有设备
	 * @param openId
	 */
	public void deleteMenage(String openId);
	/**
	 * 查询设备和指定用户对其设置的setOperate一键操作状态
	 * @param list  设备maxc_id集合
	 * @param start 查询开始位置
	 * @param openId 用户openId
	 * @return
	 */
	public List<Equipment> getEquipment(@Param("list") List<String> list,@Param("start") Integer start,@Param("openId")String openId);
	/**
	 * 查询managerId为glid且maxc_id在list中的设备集合、
	 * @param list  设备maxc_id集合
	 * @param glid 管理员openId
	 * @param start 查询开始位置
	 * @return
	 */
	public List<Equipment> getEquipment3(@Param("list") List<String> list,@Param("glid") String glid,@Param("start") Integer start);
	public Equipment getEquipmentByName(@Param("list") List<String> list,@Param("name")String name);
	public List<Sub_Equipment> getSubEquipmentByNameOrSub_id(@Param("name") String name,@Param("sub_id")int sub_id);
	//查询所用的主设备
	public List<Equipment> findAllDevices(int start);
	//修改设备overtime
	public int chageOverTime(@Param("id")String id,@Param("overtime")int overtime);
	
	/**
	 * 查询maxc_id在list中的设备集合
	 * @param list  设备maxc_id集合
	 * @param start 查询开始位置
	 * @return
	 */
	public List<Equipment> getEquipment5(@Param("list") List<String> list,@Param("start") Integer start);
	/**
	 * 添加设备
	 * @param equipment 设备实体类对象
	 * @return
	 */
	public int addDevice(Equipment equipment);
	/**
	 * 通过maxc_id查询设备的name和online
	 * @param maxc_id 设备maxc_id
	 * @return
	 */
	public Equipment getDeviceOnline(String maxc_id);
	/**
	 * 查询list中的所有设备及用户对其的一键操作状态设置
	 * @param list 设备maxc_id集合
	 * @param openId 用户openId
	 * @return
	 */
	public List<Equipment> getEquipmentAll(@Param("list")List<String> list,@Param("openId")String openId);
	/**
	 * 查询所有的设备maxc_id集合
	 * @return
	 */
	public List<String> findAllMaxc_id();
	/**
	 * 删除maxc_id对应的设备
	 * @param id  主设备maxc_id
	 */
	public void deleteDevice(String id);
	/**
	 * 更新maxc_id对应的设备名称
	 * @param id 主设备maxc_id
	 * @param name 更新后的名称
	 */
	public void updateDevice(@Param("id") String id,@Param("name") String name);
	/**
	 * 查询maxc_id对应的设备的maxc_id（判断该设备算法存在）
	 * @param id  主设备maxc_id
	 * @return
	 */
	public String findDevice(String id);
	/**
	 * 通过设备maxc_Id查询该设备的管理员openID
	 * @param MAXC_id 设备maxc_id
	 * @return
	 */
	public String findManagerByMaxc_id(String MAXC_id);
	/**
	 * 查询maxc_id对应设备的log记录总条数
	 * @param MAXC_id 设备maxc_id
	 * @return
	 */
	public int findLogCount(String MAXC_id);
	/**
	 * 通过maxc_id更新设备online
	 * @param maxc_id 设备maxc_id
	 * @param online 更新后的online值
	 */
	public void changeDeviceOnline(@Param("maxc_id") String maxc_id,@Param("online") int online);
	/**
	 * 通过maxc_id和sub_id、sec_id查询防区状态
	 * @param maxc_id 设备maxc_id
	 * @param sub_id 子设备编码
	 * @param sec_id 防区编码
	 * @return
	 */
	public Sector getSectorState(@Param("maxc_id")String maxc_id,@Param("sub_id")Integer sub_id,@Param("sec_id")Integer sec_id);
	/**
	 * 修改防区状态
	 * @param state 要修改的状态
	 * @param value 要修改的状态值
	 * @param id 防区id
	 */
	public void updateSectorState(@Param("state")String state,@Param("value")Integer value,@Param("id")Long id);
	/**
	 * 查询所有的管理员openId
	 * @return
	 */
	public List<String> getAllOpenId1();
	//对sub_equipment的操作	子设备表
	/**
	 * 查询所有的操作者openId
	 * @return
	 */
	public List<String> getAllOpenId2();
	/**
	 * 通过maxc_id和sub_id查询子设备状态
	 * @param maxc_id 设备maxc_id
	 * @param sub_id 子设备编码
	 * @return
	 */
	public Sub_Equipment getSubState(@Param("maxc_id")String maxc_id,@Param("sub_id")Integer sub_id);
	/**
	 * 查询是否存在子设备 Sub_Equipment==null 则 不存在
	 * @return
	 */
	public Sub_Equipment findWetherSub_Device();
	/**
	 * 修改子设备状态
	 * @param state 要修改的状态
	 * @param value 要修改的状态值
	 * @param id  子设备id
	 */
	public void updateSubState(@Param("state")String state,@Param("value")Integer value,@Param("id")Long id);
	/**
	 * 通过maxc_id更新该设备下所有子设备online
	 * @param maxc_id  设备maxc_id
	 * @param online 更新后的online值
	 */
	public void changeSubDeviceOnline(@Param("maxc_id") String maxc_id,@Param("online") int online);
	/**
	 * 查询maxc_id在list中的设备的子设备和其对应的setOperate一键操作状态集合
	 * @param maxc_id  设备maxc_id
	 * @param openId 用户openId
	 * @return
	 */
	public List<Sub_Equipment> getAllSubEquipment(@Param("maxc_id")String maxc_id,@Param("openId")String openId);
	/**
	 * 查询maxc_id在list中的设备的子设备id集合
	 * @param list  设备maxc_id集合
	 * @return
	 */
	public List<Long> getAllSubEquipmentId(@Param("list") List<String> list);
	/**
	 * 查询maxc_id设备对应的所有子设备和其setoPerate一键操作状态集合
	 * @param maxc_id 主设备maxc_id
	 * @param opendId 用户openId
	 * @param start 查询开始位置
	 * @return
	 */
	public List<Sub_Equipment> getAllSubEquipment3(@Param("maxc_id")String maxc_id,@Param("openId")String opendId,@Param("start")Integer start);
	/**
	 * 查询maxc_id设备对应的所有子设备集合
	 * @param maxc_id 设备maxc_id
	 * @return
	 */
	public List<Long> getAllSubEquipment4(@Param("maxc_id")String maxc_id);
	/**
	 * 查询子设备id对应的子设备
	 * @param id 子设备id
	 * @return
	 */
	public Sub_Equipment getSubEquipment(long id);
	
	public long getId(@Param("maxc_id")String maxc_id,@Param("sub_id")long sub_id);
	/**
	 * 批量添加设备maxc_id下的子设备
	 * @param array 子设备编码集合
	 * @param MAXC_id  主设备maxc_id
	 * @return
	 */
	public int addSub_DeviceAll2(@Param("array")int [] array,@Param("MAXC_id")String MAXC_id);
	/**
	 * 添加子设备
	 * @param sub_Equipment 子设备实例对象
	 */
	public void addSub_Device(Sub_Equipment sub_Equipment);
	/**
	 * 通过子设备id删除子设备
	 * @param id 子设备id
	 */
	public void deleteSub_Device(long id);
	/**
	 * 更新子设备
	 * @param sub_Equipment 子设备实例对象
	 */
	public void updateSub_Device(Sub_Equipment sub_Equipment);
	//对sector的操作			防区表
	/**
	 *查询指定id对应的子设备的所有防区
	 * @param id 子设备id
	 * @return
	 */
	public List<Sector> getAllSector2(long id);
	/**
	 * 通过id查询防区
	 * @param id 防区id
	 * @return
	 */
	public Sector getSectorById(Long id);
	/**
	 * 通过防区id删除防区
	 * @param id 防区id
	 */
	public void deleteSector(long id);
	/**
	 * 更新防区
	 * @param sector
	 */
	public void updateSector(Sector sector);
	/**
	 * 添加防区
	 * @param sector 防区实例对象
	 */
	public void addSector(Sector sector);
	/**
	 * 为list中所有的子设备添加编码在array2中的所有防区
	 * @param list 子设备id集合
	 * @param array2 防区编码集合
	 * @return
	 */
	public int addSectorAll2(@Param("list")List<Long> list,@Param("array2")int[] array2);
	/**
	 * 更新id在list中的所有子设备下所有防区online
	 * @param list 子设备id集合
	 * @param online 更新后的online值
	 */
	public void changeSectorOnline(@Param("list") List<Long> list,@Param("online") int online);
	//对log的操作				报警记录表
	/**
	 * 查询maxc_id设备下编码为sub_id的子设备的Log集合
	 * @param maxc_id 主设备maxc_id
	 * @param sub_id  子设备编码
	 * @param start  查询开始的位置
	 * @return
	 */
	public List<ArmingLog> getArmingLog(@Param("maxc_id")String maxc_id,@Param("sub_id")Integer sub_id,@Param("start")Integer start);
	/**
	 * 查询maxc_id在list中的设备的log集合
	 * @param list 主设备maxc_id集合
	 * @param start 查询开始的位置
	 * @return
	 */
	public List<ArmingLog> getLog2(@Param("list") List<String> list,@Param("start") Integer start);
	/**
	 * 添加log记录
	 * @param armingLog log实例对象
	 */
	public void addLog(ArmingLog armingLog);
	/**
	 * 删除maxc_id对应设备的log记录
	 * @param MAXC_id 设备maxc_id
	 */
	public void deleteLog(String MAXC_id);
	//对operateLog的操作		操作记录表  
	/**
	 * 查询maxc_id在list中的设备的OperateLog集合
	 * @param list  主设备maxc_id集合
	 * @param start 查询开始的位置
	 * @return
	 */
	public List<OperateLog> getOperateLog4(@Param("list") List<String> list,@Param("start") Integer start);
	/**
	 * 查询maxc_id设备下编码为sub_id的子设备的operateLog集合
	 * @param maxc_id 主设备maxc_id
	 * @param sub_id  子设备编码
	 * @param start  查询开始的位置
	 * @return
	 */
	public List<OperateLog> getOperateLog3(@Param("maxc_id")String maxc_id,@Param("sub_id")Integer sub_id,@Param("start")Integer start);
	/**
	 * 添加operateLog记录
	 * @param operateLog operateLog记录实例对象
	 */
	public void addOperateLog(OperateLog operateLog);
	/**
	 * 查询maxc_id对应设备的OperateLog记录总条数
	 * @param MAXC_id 设备maxc_id
	 * @return
	 */
	public int countOperateLog(String MAXC_id);
	/**
	 * 删除maxc_id对应设备的OperateLog记录
	 * @param MAXC_id 设备maxc_id
	 */
	public void deleteOperateLog(String MAXC_id);
	/**
	 * 删除操作者的同时删除其对指定设备的操作记录
	 * @param openId 操作者openId
	 * @param list maxc_id集合
	 */
	public void deleteOperateLog2(@Param("openId")String openId,@Param("list")List<String> list);
	/**
	 * 删除操作者为openId的operateLog记录
	 * @param openId 操作者openId
	 */
	public void deleteOperateLog3(@Param("openId")String openId);
	//对follower的操作		操作者表
	public void updateFolowerNickname(@Param("openId")String openId,@Param("nickname")String nickname);
	
	/**
	 * 通过操作者openId查询其可操作的设备maxc_id集合
	 * @param openId
	 * @return
	 */
	public Open findidByOpenId(String openId);
	/**
	 * 查询maxc_id的设备的操作者集合
	 * @param list  设备maxc_id集合
	 * @param start 查询开始位置
	 * @return
	 */
	public List<Follower> getAllFollower(@Param("list") List<String> list,@Param("start")Integer start);
	/**
	 * 通过操作者openId和操作的设备maxc_id删除对应的follower
	 * @param id  主设备maxc_id
	 * @param openId 操作者openID
	 */
	public void deleteDeviceForFollower(@Param("id")String id,@Param("openId")String openId);
	/**
	 * 删除openId对list集合中maxc_id对应的设备的操作
	 * @param openId 操作者openId
	 * @param list 主设备maxc_id集合
	 */
	public void deleteFollower(@Param("openId")String openId,@Param("list")List<String> list);
	/**
	 * 删除openId所有可操作的设备
	 * @param openId 操作者openId
	 */ 
	public void deleteFollower3(@Param("openId")String openId);
	/**
	 * 查询maxc_id设备的所有操作者
	 * @param MAXC_id 设备maxc_id
	 * @return
	 */
	public List<String> findAllFollowerByMaxc_id(String MAXC_id);
	/**
	 * 查询list中maxc_id设备的所有操作者
	 * @param list 设备maxc_id集合
	 * @return
	 */
	public List<String> findAllFollowerByMaxc_id2(List<String> list);
	/**
	 * 为操作者分配设备
	 * @param id  主设备maxc_id集合
	 * @param gzid 操作者openId
	 * @param nickname  操作者昵称
	 * @param headimgurl 操作者头像地址
	 * @param city 操作者所在城市
	 * @return
	 */
	public int addFolowerForDevice(@Param("id")String []id, @Param("gzid")String gzid,@Param("nickname") String nickname,@Param("headimgurl") String headimgurl, @Param("city")String city);
	//对setMessage的操作		消息接收设置表
	/**
	 * 通过openId和子设备id获取setMessage记录（用户关于某个子设备的消息接收设置）
	 * @param openId 用户openId
	 * @param sub_id 子设备id
	 * @return
	 */
	public SetMessage getMessage(@Param("openId")String openId,@Param("sub_id")Long sub_id);
	/**
	 * 修改setMessage记录（用户关于某个子设备的消息接收设置）
	 * @param setMessage 
	 */
	public void setMessage(SetMessage setMessage);
	/**
	 * 添加setMessage记录（用户关于某个子设备的消息接收设置）
	 * @param setMessage
	 */
	public void insertMessage(SetMessage setMessage);
	/**
	 * 查询openID对maxc_id下sub_id号子设备的消息接收设置
	 * @param maxc_id 设备maxc_id
	 * @param sub_id  子设备编码
	 * @param openId  用户openId
	 * @return
	 */
	public SetMessage getMessageBySub_Id(@Param("maxc_id")String maxc_id,@Param("sub_id")Integer sub_id,@Param("openId")String openId);
	/**
	 * 删除openId对list中的子设备的消息接收设置
	 * @param openId 用户openId
	 * @param list 子设备id集合
	 */
	public void deleteMessage(@Param("openId")String openId,@Param("list")List<Long> list);
	/**
	 * 删除openId对所有子设备的消息接收设置
	 * @param openId 用户openId
	 */
	public void deleteMessage2(@Param("openId")String openId);
	//对onePassword的操作		一键操作密码表
	/**
	 * 删除openId的一键操作密码
	 * @param openId 用户openId
	 */
	public void deleteOnePassword(String openId);
	/**
	 * 获取指定用户的一键操作密码及状态
	 * @param openId 用户openId
	 * @return
	 */
	public OnePassword getOnePassword(String openId);
	/**
	 * 修改用户的一键操作密码状态
	 * @param openId 用户openId
	 * @param remember 状态值
	 */
	public void updateOnePassword(@Param("openId")String openId,@Param("remember")String remember);
	/**
	 * 添加用户的一键操作密码
	 * @param openId 用户openId
	 * @param password 一键操作密码
	 * @return
	 */
	public int insertPassword(@Param("openId")String openId,@Param("password")String password);
	/**
	 * 添加用户的一键操作密码和密码的记住状态
	 * @param openId 用户openId
	 * @param remember 状态值
	 * @param password 一键操作密码
	 * @return
	 */
	public int insertOnePassword(@Param("openId")String openId,@Param("remember")String remember,@Param("password")String password);
	/**
	 * 设置用户的一键操作密码
	 * @param openId 用户openId
	 * @param password 一键操作密码
	 * @return
	 */
	public int setPassword(@Param("openId")String openId,@Param("password")String password);
	//对rememberPassword的操作 记住布撤防操作密码表
	/**
	 * 添加openId对某个子设备的布撤防操作密码记住状态
	 * @param openId 用户openId
	 * @param id 子设备id
	 * @param remember 状态值
	 * @return
	 */
	public int insertRememberPassword(@Param("openId")String openId,@Param("id")long id,@Param("remember")String remember);
	/**
	 * 修改openId对某个子设备的布撤防操作密码记住状态
	 * @param openId 用户openId
	 * @param id 子设备id
	 * @param remember 状态值
	 */
	public void updateRememberPassword(@Param("openId")String openId,@Param("id")long id,@Param("remember")String remember);
	/**
	 * 查询用户对某个子设备的布撤防操作密码及是否记住
	 * @param id 子设备id
	 * @param openId 用户openId
	 * @return
	 */
	public RePassword getrePassword(@Param("id")long id,@Param("openId")String openId);
	/**
	 * 删除关注者对指定子设备的密码记住情况
	 * @param openId 关注者openId
	 * @param sub_ids 子设备id集合
	 */
	public void deleteRememberPasswordByOpenIdAndSub_id(@Param("openId")String openId,@Param("sub_ids")List<Long> sub_ids);
	/**
	 * 删除关注者对设备的密码记住情况
	 * @param openId 关注者openId
	 * @param sub_ids 子设备id集合
	 */
	public void deleteRememberPasswordByOpenId(String openId);
	
	//对SetOperate的操作		一键布撤防操作状态表
	/**
	 * 查询openId对由maxc_id和id确定的子设备（或0代表的设备）的一键操作状态设置
	 * @param maxc_id 设备maxc_id
	 * @param openId 用户openId
	 * @param id 子设备id
	 * @return
	 */
	public SetOperate getOneStatus(@Param("maxc_id")String maxc_id,@Param("openId")String openId,@Param("id")long id);
	/**
	 * 添加用户对某个设备（或子设备）的一键操作状态设置
	 * @param maxc_id 设备maxc_id
	 * @param one_arming 一键布防操作状态
	 * @param openId 用户openId
	 * @param one_broken 一键撤防操作状态
	 * @param id 子设备id
	 */
	public void addOneStatus(@Param("maxc_id")String maxc_id,@Param("one_arming")int one_arming,@Param("openId")String openId,@Param("one_broken")int one_broken,@Param("id")long id);
	/**
	 * 修改用户对某个子设备（或设备）的一个操作状态设置
	 * @param max_id 设备maxc_id
	 * @param id 子设备id
	 * @param openId 用户openId
	 * @param state 要修改的一键操作状态
	 * @param value 要修改成的值
	 */
	public void updateOneStatus(@Param("maxc_id")String max_id,@Param("id")long id,@Param("openId")String openId,@Param("state")String state,@Param("value")Integer value);
	/**
	 * 删除关注者的一键布防、撤防状态的设置
	 * @param openId 关注者openId
	 */
	public void deleteSetOperateByOpenId(String openId);
	
	/**
	 * 删除关注者对指定设备的一键布防、撤防状态的设置
	 * @param openId 关注者openId
	 * @param list 	   主设备maxc集合
	 */
	public void deleteSetOperateByOpenIdAndMaxc_id(@Param("openId")String openId, @Param("list")List<String> list);
	
	/**
	 * 删除某个子设备的一键操作状态设置
	 * @param id 子设备id
	 */
	public void deleteOneStatus(@Param("id")long id);
}
