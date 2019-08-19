package com.wx.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.wx.po.Token;

  
/** 
 * 微信Token线程 
 * 
 * 
 * 2017年3月31日 下午4:59:53 
 */  
public class TokenThread implements Runnable{  
    public static String appid= "";  
    public static String appsecret = "";  
    public static volatile String accessToken = "";
    private Log log = LogFactory.getLog(TokenThread.class);
    
    @Override  
    public void run() { 
    	 while(true){ 
           try{
               	Token token = CommonUtil.getToken(appid, appsecret);//获取accessToken
                //jsapi_ticket = CommonUtil.getJsApiTicket(token.getAccessToken());
                if(null!=token){  
                	accessToken = token.getAccessToken();//保存accessToken
                    log.info(token.getAccessToken());
                    //有效期（秒）减去200秒，乘以1000(毫秒)——也就是在有效期的200秒前去请求新的token  
                    Thread.sleep((token.getExpiresIn() - 200) * 1000);    
                }else{  
                    //等待一分钟，在次请求  
                    Thread.sleep(60*1000);  
                }  
            }catch(InterruptedException e){
            	log.error("关闭TokenThread线程");
                break;//捕获到异常之后，执行break跳出循环。
            }catch(Exception e){  
            	log.error(e.getMessage(),e);
                try{  
                    //等待一分钟，在次请求  
                    Thread.sleep(60*1000);  
                }catch(Exception e1){ 
                	log.error(e.getMessage(),e);
                }  
            }  
        }
    }  
}  