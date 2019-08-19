package com.wx.wx;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wx.entity.ArmingLog;
import com.wx.entity.OperateLog;
import com.wx.service.ServletService;
import com.wx.utils.SpringTool;

/**
 * 添加记录线程用来把报警和操作记录添加到数据库
 * @author Administrator
 *
 */
public class LogQuery implements Runnable{

	private static final ServletService servletService = (ServletService)SpringTool.getBean("servletService");
	private int count = 0;
	private static Log log = LogFactory.getLog(LogQuery.class);

	@SuppressWarnings("static-access")
	@Override
	public void run() {
		log.info("添加记录信息线程开启");
		while(!Thread.currentThread().interrupted()) {
			Object o = Query.get();
			try {
				if (o!=null) {
					count++;
					if (o instanceof OperateLog) {
						OperateLog operateLog = (OperateLog)o;
						servletService.addOperateLog(operateLog);
					}else if (o instanceof ArmingLog) {
						ArmingLog armingLog = (ArmingLog)o;
						servletService.addLog(armingLog);
					}
				}else {
					Thread.sleep(1000);
				}
				if (count>=10) {//每添加10条休眠100毫秒
					count = 0;
					Thread.sleep(100);
				}
			} catch (Exception e) {
				if (e instanceof InterruptedException) {//关闭软件时中断线程，退出循环
					log.error("关闭LogQuery线程");
					break;
				}
				log.error(e.getMessage()+o.toString(),e);
			}
		}
	}
	

}
