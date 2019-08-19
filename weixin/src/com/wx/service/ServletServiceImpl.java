package com.wx.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wx.dao.ServletDao;
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
import com.wx.wx.OpenQuery;

@Service("servletService")
public class ServletServiceImpl implements ServletService{

	@Resource(name="servletDao")
	private ServletDao dao ;
	
	public void setDao(ServletDao dao) {
		this.dao = dao;
	}

	@Transactional
	public int addDevice(Equipment equipment) {
		int addDevice = dao.addDevice(equipment);
		dao.addOneStatus(equipment.getId(), 1, equipment.getManagerId(), 1, 0);//用户添加设备的同时默认为该设备选择好一键撤布防
		return addDevice;
	}

	@Transactional
	public void deleteDevice(String id) {
		dao.deleteDevice(id);
	}

	@Transactional
	public void updateDevice(String id, String name) {
		dao.updateDevice(id, name);
	}

	@Transactional
	public String findDevice(String id) {
		return dao.findDevice(id);
	}

	@Transactional
	public void addSub_Device(Sub_Equipment sub_Equipment) {
		dao.addSub_Device(sub_Equipment);
	}

	@Transactional
	public void deleteSub_Device(Long id) {
		dao.deleteSub_Device(id);
	}

	@Transactional
	public void updateSub_Device(Sub_Equipment sub_Equipment) {
		dao.updateSub_Device(sub_Equipment);
	}
	


	@Transactional
	public void addOperateLog(OperateLog operateLog) {
		int number = dao.countOperateLog(operateLog.getMAXC_id());
		if (number>99) {//添加操作记录的时候先判断该主设备的操作记录数是否超过99条，如果超过删除一条再添加
			dao.deleteOperateLog(operateLog.getMAXC_id());
		}
		dao.addOperateLog(operateLog);
	}

	@Transactional
	public void deleteSector(long id) {
		dao.deleteSector(id);
	}

	@Transactional
	public void updateSector(Sector sector) {
		dao.updateSector(sector);
	}

	@Transactional
	public void addSector(Sector sector) {
		dao.addSector(sector);
	}

	@Transactional
	public List<String> findAllFollowerAndManager(String MAXC_id) {
		List<String> list = dao.findAllFollowerByMaxc_id(MAXC_id);
		String id = dao.findManagerByMaxc_id(MAXC_id);
		list.add(id);
		return list;
	}

	@Transactional
	public void addLog(ArmingLog armingLog) {
		int number = dao.findLogCount(armingLog.getMAXC_id());
		if (number>99) {//添加报警记录的时候先判断该主设备的报警记录数是否超过99条，如果超过删除一条再添加
			dao.deleteLog(armingLog.getMAXC_id());
		}
		dao.addLog(armingLog);
	}

	@Transactional
	public Open findInfoByOpenId(String openId) {
		return dao.findInfoByOpenId(openId);
	}
	@Override
	public Open findidByOpenId(String openId) {
		return dao.findidByOpenId(openId);
	}


	@Transactional
	public Equipment getDeviceOnline(String maxc_id) {
		return dao.getDeviceOnline(maxc_id);
	}

	//通过maxc_id更新设备online,当主设备上线或者掉线时其子设备和防区全部上线或者掉线
	@Transactional
	public void changeDeviceOnline(String maxc_id, int online) {
		dao.changeDeviceOnline(maxc_id, online);
		dao.changeSubDeviceOnline(maxc_id, online);
		List<Long> list = dao.getAllSubEquipment4(maxc_id);
		dao.changeSectorOnline(list, online);
	}


	@Transactional
	public List<Equipment> getEquipment(List<String> list,int start,String openId) {
		return dao.getEquipment(list,start,openId);
	}

	@Transactional
	public List<Sub_Equipment> getAllSubEquipment(String maxc_id,String openId) {
		return dao.getAllSubEquipment(maxc_id,openId);
	}
	@Transactional
	public List<Sub_Equipment> getAllSubEquipment3(String maxc_id,String openId,int start) {
		return dao.getAllSubEquipment3(maxc_id,openId,start);
	}

	@Transactional
	public List<Sector> getAllSector2(Long id) {
		return dao.getAllSector2(id);
	}
	@Transactional
	public List<ArmingLog> getLog2(List<String> list, Integer start) {
		return dao.getLog2(list, start);
	}

	@Transactional
	public List<OperateLog> getOperateLog4(List<String> list, Integer start) {
		return dao.getOperateLog4(list, start);
	}

	@Transactional
	public List<ArmingLog> getArmingLog(String maxc_id, Integer sub_id,Integer start) {
		return dao.getArmingLog(maxc_id, sub_id,start);
	}

	@Transactional
	public List<OperateLog> getOperateLog3(String maxc_id, Integer sub_id,Integer start) {
		return dao.getOperateLog3(maxc_id, sub_id,start);
	}

	@Transactional
	public Sub_Equipment getSubEquipment(long id) {
		return dao.getSubEquipment(id);
	}

	@Transactional
	public Sector getSectorById(Long id) {
		return dao.getSectorById(id);
	}

	@Transactional
	public void deleteDeviceForFollower(String id, String openId) {
		List<Long> list = dao.getAllSubEquipment4(id);
		dao.deleteMessage(openId,list);//删除用户对指定设备的关注的同时删除他对其子设备的信息接收设置
		
		Open findById2 = dao.findidByOpenId(openId);//判断该openId是否还有关注的设备
		if (findById2==null) {
			dao.deleteOnePassword(openId);//如果该关注者已无关注的设备则删除他的一键操作密码
		}
		dao.deleteRememberPasswordByOpenIdAndSub_id(openId,list);//删除关注者对指定子设备的密码记住情况
		List<String> maxcs = new ArrayList<String>();
		maxcs.add(id);
		dao.deleteOperateLog2(openId,maxcs);//删除这个关注者对指定设备的操作记录
		dao.deleteSetOperateByOpenIdAndMaxc_id(openId,maxcs);//删除关注者对指定设备的一键布防、撤防状态的设置
		dao.deleteDeviceForFollower(id, openId);
	}


	@Transactional
	public List<Follower> getAllFollower(List<String> list, Integer start) {
		return dao.getAllFollower(list, start);
	}

	@Transactional
	public Sub_Equipment getSubState(String maxc_id, Integer sub_id) {
		return dao.getSubState(maxc_id, sub_id);
	}

	@Transactional
	public void updateSubState(String state, Integer value,Long id) {
		dao.updateSubState(state, value,id);
	}

	@Transactional
	public Sector getSectorState(String maxc_id, Integer sub_id, Integer sec_id) {
		return dao.getSectorState(maxc_id, sub_id, sec_id);
	}

	@Transactional
	public void updateSectorState(String state, Integer value, Long id) {
		dao.updateSectorState(state, value, id);
	}

	@Transactional
	public Name getNames(String maxc_id, Integer sub_id) {
		return dao.getNames(maxc_id, sub_id);
	}
	@Transactional
	public Name getNames2(String maxc_id, Integer sub_id, Integer sec_id) {
		return dao.getNames2(maxc_id, sub_id, sec_id);
	}

	@Transactional
	public List<String> getAllOpenId() {
		List<String> list = new ArrayList<String>();
		List<String> allOpenId1 = dao.getAllOpenId1();
		List<String> allOpenId2 = dao.getAllOpenId2();
		
		list.addAll(allOpenId1);
		list.addAll(allOpenId2);
		return list;
	}

	@Transactional
	public SetMessage getMessage(String openId,Long sub_id) {
		SetMessage message = dao.getMessage(openId,sub_id);
		return message;
	}

	@Transactional
	public void setMessage(SetMessage setMessage) {
		dao.setMessage(setMessage);
	}

	@Transactional
	public void insertMessage(SetMessage setMessage) {
		dao.insertMessage(setMessage);
	}

	@Transactional
	public SetMessage getMessageBySub_Id(String maxc_id, Integer sub_id, String openId) {
		return dao.getMessageBySub_Id(maxc_id, sub_id, openId);
	}


	
	@Transactional
	public int addDeviceAll(Equipment equipment, Integer sub_num, Integer sector_num) {
		dao.updataNckname(equipment.getManagerId(),equipment.getNickname());
		dao.addDevice(equipment);
		dao.addOneStatus(equipment.getId(), 1, equipment.getManagerId(), 1, 0);//用户添加设备的同时默认为该设备选择好一键撤布防
		
		int [] arr = new int[sub_num];
		for(int a=0;a<sub_num;a++) {
			arr[a] = a;
		}
		dao.addSub_DeviceAll2(arr,equipment.getId());
		
		if(sub_num>128) {//如果有128号子设备，则设置默认一键布撤防选中128号子设备，否则选中0号子设备
			long id = dao.getId(equipment.getId(), 128);
			dao.addOneStatus(equipment.getId(), 1, equipment.getManagerId(), 1, id);
		}else{
			long id = dao.getId(equipment.getId(), 0);
			dao.addOneStatus(equipment.getId(), 1, equipment.getManagerId(), 1, id);
		}
		
		List<Long> list2 = dao.getAllSubEquipment4(equipment.getId());
		int [] array2 = new int [sector_num];
		for(int a=1;a<=sector_num;a++) {
			array2[a-1] = a;
		}
		int b = dao.addSectorAll2(list2, array2);//子设备数和防区数最小为1，所以不用担心集合或者数组元素为0而添加失败
		return b;
	}

	@Transactional
	public void deleteMenage(String openId) {
		dao.deleteMenage(openId);
	}


	@Transactional
	public List<Equipment> getEquipmentAll(List<String> list,String openId) {
		return dao.getEquipmentAll(list,openId);
	}


	@Transactional
	public OnePassword getOnePassword(String openId) {
		return dao.getOnePassword(openId);
	}

	@Transactional
	public int insertPassword(String openId, String password) {
		return dao.insertPassword(openId, password);
	}

	@Transactional
	public int setPassword(String openId, String password) {
		return dao.setPassword(openId, password);
	}



	@Transactional
	public void deleteOnePassword(String openId) {
		dao.deleteOnePassword(openId);
	}


	@Transactional
	public List<String> findAllMaxc_id() {
		return dao.findAllMaxc_id();
	}

	@Transactional
	public void updateOnePassword(String openId, String remember) {
		dao.updateOnePassword(openId, remember);
	}

	@Transactional
	public RePassword getrePassword(long id, String openId) {
		return dao.getrePassword(id, openId);
	}

	@Transactional
	public int insertOnePassword(String openId, String remember, String password) {
		return dao.insertOnePassword(openId, remember, password);
	}

	@Transactional
	public int insertRememberPassword(String openId, long id, String remember) {
		return dao.insertRememberPassword(openId, id, remember);
	}

	@Transactional
	public void updateRememberPassword(String openId, long id, String remember) {
		dao.updateRememberPassword(openId, id, remember);
	}

	@Transactional
	public int addFolowerForDevice(String[] arr, String gzid, String nickname, String headimgurl, String city) {
		dao.updateFolowerNickname(gzid, nickname);
		int i = dao.addFolowerForDevice(arr, gzid, nickname, headimgurl, city);
		
		for(int a=0;a<arr.length;a++) {//添加设备的同时设置默认一键操作状态
			System.out.println("为多个设备添加操作者:"+arr[a]+":"+arr.length);
			Sub_Equipment sub = dao.findSub128(arr[a]);
			if(sub!=null) {//判断这些设备是否有128号子设备,有设置默认一键操作为128号子设备
				dao.addOneStatus(arr[a], 1, gzid, 1, sub.getId());
			}else {
				Sub_Equipment sub2 = dao.findSub0(arr[a]);
				if (sub2!=null) {//在没有128号子设备的时候判断这些设备是否有0号子设备,有设置默认一键操作为0号子设备
					dao.addOneStatus(arr[a], 1, gzid, 1, sub2.getId());
				}
			}
		}
		return i;
	}

	@Transactional
	public void deleteFollower(String openId, List<String> list) {
		List<Long> sub_list = dao.getAllSubEquipmentId(list);
		dao.deleteMessage(openId,sub_list);//删除用户对指定设备的关注的同时删除他对其子设备的信息接收设置
		
		Open findById2 = dao.findidByOpenId(openId);//判断该openId是否还有关注的设备
		if (findById2==null) {
			dao.deleteOnePassword(openId);//如果该关注者已无关注的设备则删除他的一键操作密码
		}
		dao.deleteRememberPasswordByOpenIdAndSub_id(openId,sub_list);//删除关注者对指定子设备的密码记住情况
		dao.deleteOperateLog2(openId,list);//删除这个关注者对指定设备的操作记录
		dao.deleteSetOperateByOpenIdAndMaxc_id(openId,list);//删除关注者对指定设备的一键布防、撤防状态的设置
		dao.deleteFollower(openId, list);
	}
	@Transactional
	public void deleteFollower3(String openId) {
		dao.deleteFollower3(openId);
	}

	@Transactional
	public List<Long> getAllSubEquipmentId(List<String> list) {
		return dao.getAllSubEquipmentId(list);
	}

	@Transactional
	public void deleteMessage(String openId, List<Long> list) {
		dao.deleteMessage(openId, list);
	}
	@Transactional
	public void deleteMessage2(String openId) {
		dao.deleteMessage2(openId);
	}

	@Transactional
	public void deleteOperateLog2(String openId, List<String> list) {
		dao.deleteOperateLog2(openId, list);
	}
	@Transactional
	public void deleteOperateLog3(String openId) {
		dao.deleteOperateLog3(openId);
	}

	@Transactional
	public List<Equipment> getEquipment3(List<String> list, String glid, Integer start) {
		return dao.getEquipment3(list, glid, start);
	}

	@Transactional
	public List<String> findAllFollowerByMaxc_id(String MAXC_id) {
		return dao.findAllFollowerByMaxc_id(MAXC_id);
	}

	@Transactional
	public List<Equipment> getEquipment5(List<String> list, Integer start) {
		return dao.getEquipment5(list, start);
	}

	@Transactional
	public SetOperate getOneStatus(String maxc_id, String openId, long id) {
		return dao.getOneStatus(maxc_id, openId, id);
	}

	@Transactional
	public void addOneStatus(String maxc_id, int one_arming, String openId, int one_broken, long id) {
		dao.addOneStatus(maxc_id, one_arming, openId, one_broken, id);
	}

	@Transactional
	public void updateOneStatus(String max_id, long id, String openId, String state, int value) {
		dao.updateOneStatus(max_id, id, openId, state, value);
	}

	@Transactional
	public void deleteOneStatus(long id) {
		dao.deleteOneStatus(id);
	}

	@Transactional
	public Sub_Equipment findWetherSub_Device() {
		return dao.findWetherSub_Device();
	}

	@Transactional
	public List<String> findAllFollowerByMaxc_id2(List<String> list) {
		return dao.findAllFollowerByMaxc_id2(list);
	}

	@Transactional
	public Sub_Equipment findSub128(String Maxc_id) {
		return dao.findSub128(Maxc_id);
	}
	
	@Transactional
	public Sub_Equipment findSub0(String Maxc_id) {
		return dao.findSub0(Maxc_id);
	}

	@Transactional
	public long getId(String maxc_id, long sub_id) {
		return dao.getId(maxc_id, sub_id);
	}

	@Transactional
	public Equipment getEquipmentByName(List<String> list, String name) {
		return dao.getEquipmentByName(list, name);
	}

	@Transactional
	public List<Sub_Equipment> getSubEquipmentByNameOrSub_id(String name, int sub_id) {
		return dao.getSubEquipmentByNameOrSub_id(name, sub_id);
	}

	@Transactional
	public List<Equipment> findAllDevices(int start) {
		return dao.findAllDevices(start);
	}

	@Transactional
	public int chageOverTime(String id, int overtime) {
		return dao.chageOverTime(id, overtime);
	}

	@Transactional
	public void deleteSetOperateByOpenId(String openId) {
		dao.deleteSetOperateByOpenId(openId);
	}

	@Transactional
	public void deleteRememberPasswordByOpenId(String openId) {
		dao.deleteRememberPasswordByOpenId(openId);
	}

	@Transactional
	public Open getOpen(String openId) {
		Open open = OpenQuery.get(openId); 
		if (open!=null) {//判断open队列是否已存在该openId对应的open，有则更新最新使用时候
			open.setTime(System.currentTimeMillis());
			return open;
		}else {
			Open findById = dao.findInfoByOpenId(openId);//从管理员表获取open
			if(findById!=null) {
				OpenQuery.add(findById);
				return findById;
			}else {
				Open findById2 = findidByOpenId(openId);//从操作者表获取open
				if (findById2!=null) {
					OpenQuery.add(findById2);
					return findById2;
				}else {
					return null;
				}
			}
		}
	}

	@Transactional
	public void frash(String openId) {
		Open findById = findInfoByOpenId(openId);//获取管理员open
		if(findById!=null) {//重新加载管理员
			OpenQuery.add(findById);
		}else {
			Open findById2 = findidByOpenId(openId);// 操作者open
			if (findById2!=null) {//重新加载关注者
				OpenQuery.add(findById2);
				System.out.println("刷新操作者："+findById2);
			}else {//删除设备后已不再是管理员或者关注者，则在内存中删除
				OpenQuery.delete(openId);
			}
		}
	}

	@Transactional
	public void updataNckname(String managerId, String nickname) {
		dao.updataNckname(managerId, nickname);
	}

	@Transactional
	public void updateFolowerNickname(String openId, String nickname) {
		dao.updateFolowerNickname(openId, nickname);
	}
}
