package com.redpig.common.spring;

import com.redpig.common.utils.Utils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * Spring Bean获取工具类
 * @author 邓小林
 * 创建时间：2016年4月12日
 */
@Component
public class AppCtxHolder implements ApplicationContextAware {
	private static ApplicationContext appCtx;
	
	private AppCtxHolder() {}
	
	public static <T> T getBean(Class<T> clazz) {
		checkContext();
		return appCtx.getBean(clazz);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		checkContext();
		return (T) appCtx.getBean(name);
	}
	
	private static void checkContext() throws RuntimeException {
		if (Utils.isNull(appCtx)) {
			throw new RuntimeException("没有Spring上下文");
		}
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		appCtx = applicationContext;
	}

	public static ApplicationContext getAppCtx() {
		return appCtx;
	}
}
