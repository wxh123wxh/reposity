package com.wx.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 通过该类即可在普通工具类里获取spring管理的bean
 * @author wolf
 *
 */
public final class SpringTool implements ApplicationContextAware {
	private static ApplicationContext applicationContext = null;
	private static final Log log = LogFactory.getLog(SpringTool.class);
 
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringTool.applicationContext == null) {
			SpringTool.applicationContext = applicationContext;
			log.info(
					"========ApplicationContext配置成功,在普通类可以通过调用ToolSpring.getAppContext()获取applicationContext对象,applicationContext="
							+ applicationContext + "========");
		}
	}
 
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}
 
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}
}

