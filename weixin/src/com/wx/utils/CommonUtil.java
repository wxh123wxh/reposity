package com.wx.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import com.wx.menu.Button;
import com.wx.menu.ClickMenu;
import com.wx.menu.Menu;
import com.wx.menu.ViewMenu;
import com.wx.po.Template;
import com.wx.po.TemplateParam;
import com.wx.po.Token;
import com.wx.po.User;
import com.wx.wx.InitAccessTokenServlet;
import com.wx.wx.WXServlet;

import net.sf.json.JSONObject;

/** 
 * http请求工具类 
 * @author 
 * 
 */  
public class CommonUtil {
	
	private static final Log log = LogFactory.getLog(CommonUtil.class);
    
	public static final String openId_list = "https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN";
    // 凭证获取（GET）——access_token  
    public static final String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";  
    //发送模板消息
    public static final String template_url="https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";  
    //拉入黑名单
    public static final String USERBLACK = "https://api.weixin.qq.com/cgi-bin/tags/members/batchblacklist?access_token=ACCESS_TOKEN"; 
    //获取用户信息
    public static final String USER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    //创建菜单接口
    public static final String MEUN_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";//创建菜单接口
    //删除自定义菜单
    public static final String DELETE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
    //网络授权第一步获取code
    public static final String GET_CODE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    //网页授权第二步获取openID
    public static final String GET_OPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    
    public static final String GET_LS_MEDIAID = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    
    private static final String str[] = {"UTF-8","ACCESS_TOKEN","errcode","errmsg","模板消息发送成功","模板消息发送失败:","40001重发","异步请求取消",
    		"APPID","APPSECRET","expires_in","#00DD00","","yyyy-MM-dd  HH:mm:ss","first","您的设备报警了","#FF3333",
    		"keyword1","#0044BB","keyword2","keyword3","#BBBBBB","请尽快处理","remark","keyword4","headimgurl",
    		"nickname","city","openid","CODE","GET","access_token","OPENID","40001","SECRET"};
    /** 
     * 发送https请求 
     *  
     * @param requestUrl 请求地址 
     * @param requestMethod 请求方式（GET、POST） 
     * @param outputStr 提交的数据 
     * @return rootNode(通过rootNode.get(key)的方式获取json对象的属性值) 
     */  
    public static JsonNode httpsRequest(String requestUrl, String requestMethod, String outputStr) {  
        //ObjectMapper该类的方法可以将Java对象转为json，json转为Java对象
    	ObjectMapper mapper = new ObjectMapper();
    	//JsonNode json对象   json相当于list集合 jsonNode相当于map集合
        JsonNode rootNode = null;  
        StringBuffer buffer = new StringBuffer();
        try {  
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化  
        	TrustManagerFactory tm = TrustManagerFactory.getInstance("X509");
    		tm.init((KeyStore)null);// 用证书授权源和相关的信任材料初始化此工厂
    		TrustManager[] trustManagers = tm.getTrustManagers(); 
            //此类的实例表示安全套接字协议的实现
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");  
            // security.SecureRandom()生成加密安全的随机数
            sslContext.init(null, trustManagers, new java.security.SecureRandom());  
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();  
  
            URL url = new URL(requestUrl);  
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();  
            //设置当此实例为安全 https url 连接创建套接字时使用的 sslsocketfactory。
            conn.setSSLSocketFactory(ssf);  
            //httpUrlConnection.setDoOutput(true);以后就可以使用conn.getOutputStream().write()  
            //httpUrlConnection.setDoInput(true);以后就可以使用conn.getInputStream().read();  
            conn.setDoOutput(true); 
            conn.setDoInput(true);  
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setUseCaches(false);  
            // 设置请求方式（GET/POST）  
            conn.setRequestMethod(requestMethod);  
              
            if("GET".equalsIgnoreCase(requestMethod)) 
            	conn.connect();  
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {  
                OutputStream outputStream = conn.getOutputStream();  
                // 注意编码格式  
                outputStream.write(outputStr.getBytes("UTF-8"));  
                outputStream.close();  
            }  
            // 从输入流读取返回内容  
            InputStream inputStream = conn.getInputStream();//此方法默认已连接https
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");  
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  
            String str = null;  
            while ((str = bufferedReader.readLine()) != null) {  
                buffer.append(str);
            }
            // 释放资源  
            bufferedReader.close();  
            inputStreamReader.close();  
            inputStream.close();  
            conn.disconnect();  
            
            rootNode = mapper.readTree(buffer.toString());
        } catch (Exception e) { 
        	log.error(e.getMessage(),e);
        }  
        return rootNode;  
    }  
    
    /**
	 * https请求上传临时素材
	 * filePath  文件路径
	 * requestUrl 请求地址
	 */
	public static String uploadjh(String filePath,String requestUrl) throws Exception {
		File file = new File(filePath);
		if(!file.isFile()||!file.exists()) {//上传的东西不是文件或者上传的文件不存在时报错
			throw new Exception("文件不存在");
		}
		
		//设置https请求参数
		URL url = new URL(requestUrl);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
		conn.setRequestMethod("POST");
		conn.setUseCaches(false);//post方式时false忽略缓存
		
		//设置请求头
		conn.setRequestProperty("Connection", "Keep-Alive");
		conn.setRequestProperty("Cache-Control", "no-cache");
		conn.setRequestProperty("Charset", "UTF-8");
		
		//设置边界
		String BOUNDARY = "-------"+System.currentTimeMillis();
		conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+BOUNDARY);
				
		//组装from表单的curl格式
		OutputStream out = conn.getOutputStream();
		out.write(("--" + BOUNDARY + "\r\n").getBytes("utf-8"));
		out.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n", file.getName()).getBytes("utf-8"));  
		//application/octet-stream 表示任意文件类型
		out.write("Content-Type: application/octet-stream \r\n\r\n".getBytes());
				
		//输出正文信息
		byte [] b = new byte[1024];
		int len = 0;
		FileInputStream fi = new FileInputStream(file);
		DataInputStream inp = new DataInputStream(fi);
		while((len=inp.read(b))!=-1) {
			out.write(b,0,len);
		}
		fi.close();
		//输出尾部                                主要这个\r\n前不要加东西，可能导致后面出错
		byte [] foot = ("\r\n--"+BOUNDARY+"\r\n").getBytes("utf-8");
		out.write(foot);
	
		//刷新缓存并关闭输出流
		out.flush();
		out.close();
		
		//通过输入流获取返回的信息
		InputStream input = conn.getInputStream();
		InputStreamReader ir = new InputStreamReader(input);
		BufferedReader br = new BufferedReader(ir);
		
		//将返回的数据加入缓存
		StringBuilder sbb = new StringBuilder();
		String line = null;
		while((line = br.readLine()) != null) {
			sbb.append(line);
		}
		
		//关闭输入流并关闭https连接
		br.close();
		ir.close();
		input.close();
		conn.disconnect();
		
		//从返回的信息中获取上传文件后返回的媒体id
		String mediaId = null;
		JSONObject object = JSONObject.fromObject(sbb.toString());
		mediaId = object.getString("media_id");
		return mediaId;
	}
    
    /**
	 * Get请求
	 * @param url	请求路径
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject doGetStr(String url){
		//创造一个http客户端
		CloseableHttpClient httpClient = HttpClients.createDefault();  
		//创建一个http的get请求客户端
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			//执行http请求并从http请求客户端获取httpResponse对象
			HttpResponse httpResponse = httpClient.execute(httpGet);
			//从httpResponse中解析出HttpEntity对象
			HttpEntity entity = httpResponse.getEntity();
			if(entity!=null) {
				//从HttpEntity对象中解析出返回的数据
				String string = EntityUtils.toString(entity,str[0]);
				jsonObject = JSONObject.fromObject(string);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return jsonObject;
	}
	
	/**
	 * POST请求
	 * @param url	请求路径
	 * @param outStr 请求参数
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static JSONObject doPostStr(String url,String outStr){
		//创造一个http客户端
		CloseableHttpClient httpClient = HttpClients.createDefault();  
		//创建一个http的post请求客户端
		HttpPost httpPost = new HttpPost(url);
		JSONObject jsonObject = null;
		try {
			//封装要上传的数据
			httpPost.setEntity(new StringEntity(outStr, str[0]));
			//执行http请求并从http请求客户端获取httpResponse对象
			HttpResponse response = httpClient.execute(httpPost);
			//从httpResponse中解析出HttpEntity对象
			HttpEntity httpEntity = response.getEntity();
			if (httpEntity!=null) {
				//从HttpEntity对象中解析出返回的数据
				String string = EntityUtils.toString(httpEntity,str[0]);
				jsonObject = JSONObject.fromObject(string);
			}
		}catch (IOException e) {
			log.error(e.getMessage(),e);
		}
		return jsonObject;
	}
	
	/**
	 * 异步请求发送模板信息，因为同步需要等待回应
	 * @param token 微信接口凭证
	 * @param template 模板信息实例
	 * @return
	 */
	public static void acynPost(String token,Template template){
	    String url = template_url.replace(str[1], token);//设置请求路径
	    String outStr = template.toJSON().toString();//获取json格式模板信息
		//获取一个异步http请求客户端
		CloseableHttpAsyncClient httpclient = InitAccessTokenServlet.httpclient;
		httpclient.start();//开启客户端
		HttpPost httpPost = new HttpPost(url);//获取一个http的post请求对象
		try {
			httpPost.setEntity(new StringEntity(outStr, str[0]));//封装上传的数据
			httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {//添加异步处理任务,并执行
				@Override
				public void completed(HttpResponse response) {//如果该任务完成
					try {
						HttpEntity entity = response.getEntity();
						if (entity!=null) {
							//解析返回的数据
							String string = EntityUtils.toString(entity,str[0]);
							JSONObject fromObject = JSONObject.fromObject(string);
							//获取返回码和返提示信息
							int errorCode=toInt(fromObject.get(str[2]).toString()); 
				            String errorMessage=fromObject.get(str[3]).toString();  
				            
				            if(errorCode==0){//如果返回的错误码为0表示发送成功，打印发送的模板信息主体内容，否则打印错误码和错误信息及发送到的用户openiD 
				            	log.info(str[4]+template.getTemplateParamList().get(3));  
				            }else{  
				            	log.error(new StringBuilder().append(str[5]).append(errorCode).append(errorMessage).append(template.getToUser()).toString());  
				            } 
				            
				            if (errorCode!=0) {
				            	Thread.sleep(1000);
				            	CommonUtil.acynPost2(TokenThread.accessToken, template);
				            	log.info(str[6]);
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage(),e);
					}
				}
				
				@Override
				public void cancelled() {//异步请求取消
					log.error(str[7]);
				}

				@Override
				public void failed(Exception arg0) {//异步请求失败
					try {
						Thread.sleep(1000);
						CommonUtil.acynPost2(TokenThread.accessToken, template);
					} catch (InterruptedException e) {
						log.error("异步请求,发送模板信息失败",arg0);
					}
				}
				
			});
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 异步请求发送模板信息(当请求模板信息失败时调用这个方法)
	 * @param token 微信接口凭证
	 * @param template 模板信息实例
	 * @return
	 */
	public static void acynPost2(String token,Template template){
		String url = template_url.replace(str[1], token);
		String outStr = template.toJSON().toString();
		
		CloseableHttpAsyncClient httpclient = InitAccessTokenServlet.httpclient;
		httpclient.start();
		HttpPost httpPost = new HttpPost(url);
		try {
			httpPost.setEntity(new StringEntity(outStr, str[0]));
			httpclient.execute(httpPost, new FutureCallback<HttpResponse>() {
				@Override
				public void completed(HttpResponse response) {
					try {
						HttpEntity entity = response.getEntity();
						if (entity!=null) {
							String string = EntityUtils.toString(entity,str[0]);
							JSONObject fromObject = JSONObject.fromObject(string);
							
							int errorCode=toInt(fromObject.get(str[2]).toString()); 
							String errorMessage=fromObject.get(str[3]).toString();  
							
							if(errorCode==0){  
								log.info(str[4]);  
							}else{  
								log.info(new StringBuilder().append(str[5]).append(errorCode).append(errorMessage).append(template.getToUser()).toString());  
							} 
						}
					} catch (IOException e) {
						log.error(e.getMessage(),e);
					}
				}
				
				@Override
				public void cancelled() {
					log.error("重发模板信息取消");
				}

				@Override
				public void failed(Exception arg0) {
					log.error("重发模板信息失败");
				}
			});
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
  
    /** 
     * 获取接口访问凭证 access_toeken
     * @param appid 凭证 
     * @param appsecret 密钥 
     * @return 
     */  
	 public static Token getToken(String appid, String appsecret) {  
	     Token token = null;  
	     String requestUrl = token_url.replace(str[8], appid).replace(str[9], appsecret);//设置请求路径
	     // 发起GET请求获取凭证  
	     JsonNode rootNode = httpsRequest(requestUrl, str[30], null);
	     if (null != rootNode&&!rootNode.has(str[2])) {//有返回值 ，且返回值不是错误信息
	         token = new Token();  
	         token.setAccessToken(rootNode.get(str[31]).getTextValue());//获取accessToken  
	         token.setExpiresIn(toInt(rootNode.get(str[10]).toString()));//获取凭证有效时间
	     }else {
	    	 log.info("获取token失败");
	     }
	     return token;  
	 }  
	 
   /**
    * 组装报警模板信息
    * @param deviceId 设备name和maxc_id（最大3个设备的name和maxc_id组合）
    * @param content 模板内容
    * @param toUser  发送给的用户openId
    * @param color  主体内容颜色：#FF3333红色 	#0044BB蓝色	#00BB32绿色 	#1D0369黄色	#0E0E0E黑色
    * @return
    */
    public static Template geTemplate(String deviceId,String content,String toUser,String color) {
    	Template tem=new Template();
    	
		tem.setTemplateId(WXServlet.TEMPLATE_ID_PJ);
		tem.setTopColor(str[11]);
		tem.setUrl(str[12]);  
		
		String time = (new SimpleDateFormat(str[13])).format(new Date());
		List<TemplateParam> paras=new ArrayList<TemplateParam>();  
		paras.add(new TemplateParam(str[14],str[15],color));  
		paras.add(new TemplateParam(str[17],deviceId,color));  
		paras.add(new TemplateParam(str[19],time,color));
		paras.add(new TemplateParam(str[20],content,color));
		paras.add(new TemplateParam(str[23],str[22],color)); 
		          
		tem.setTemplateParamList(paras);
		tem.setToUser(toUser);
		return tem;
    }
    /**
     * 组装添加用户的模板信息
     * @param first 固定模板头
     * @param nickname 要添加的用户昵称
     * @param fromUserName 发送给要添加用户的管理员
     * @param remark 固定模板尾
     * @param url 发送的模板信息点击后跳转的路径
     * @return
     */
    public static Template geTemplateAddUser(String first,String nickname,String fromUserName,String remark,String url) {
    	String keyword2 = (new SimpleDateFormat(str[13])).format(new Date());
    	Template tem=new Template(); 
    	
		tem.setTemplateId(WXServlet.TEMPLATE_ID_ADD_USER);  
		tem.setTopColor(str[11]);
		tem.setUrl(url);  
		
		List<TemplateParam> paras=new ArrayList<TemplateParam>();  
		paras.add(new TemplateParam(str[14],first,str[16]));  
		paras.add(new TemplateParam(str[17],nickname,str[18]));  
		paras.add(new TemplateParam(str[19],keyword2,str[18]));
		paras.add(new TemplateParam(str[23],remark,str[21])); 
		          
		tem.setTemplateParamList(paras);
		tem.setToUser(fromUserName);
		return tem;
    }
    
    
    /**
     * string转int
     * @param str
     * @return
     */
    public static Integer toInt(String str){  
        if(str == null || str.equals("")){  
            return null;  
        }  
        return Integer.valueOf(str);  
    } 
    
    /**
     * 获取用户信息
     * @param access_token  微信接口凭证
     * @param openid 用户openId
     * @return
     */
    public static User getUserInfo(String access_token,String openid) {
    	User user = new User();
    	String url = USER_INFO.replace(str[1], access_token).replace(str[32], openid);
    	JsonNode node = null;
    	int count = 3;
    	
    	while(node==null||node.has(str[2])){
    		try {
				Thread.sleep(100);
				node = httpsRequest(url, str[30], null);
				count--;
			} catch (InterruptedException e) {
				log.error(e.getMessage(),e);
			}
			if (count<=0) {
				break;
			}
    	}
    	
    	if (node==null||node.has(str[2])) {//如果获取user信息失败，设置openId为40001，用于在调用的地方判断是否失败
    		user.setOpenid(str[33]);
    		log.error("请求用户信息设备");
		}else {
			user.setHeadimgurl(node.get(str[25]).getTextValue());
			user.setNickname(filter(node.get(str[26]).getTextValue()));
			user.setCity(node.get(str[27]).getTextValue());
			user.setOpenid(node.get(str[28]).getTextValue());
		}
		return user;
    }
    
    /**
	 * 组装菜单
	 * @return
	 */
    public static String initMenu() {
		//设置回调各个页面的路径
		String REDIRECT_URI = "http://ajbtest.com/weixin/public/qrcode.jsp";
		String REDIRECT_URI2 = "http://ajbtest.com/weixin/user/user.jsp";
		String REDIRECT_URI3 = "http://ajbtest.com/weixin/device/device.jsp";
		String REDIRECT_URI4 = "http://ajbtest.com/weixin/operate/device_broken.jsp";
		String REDIRECT_URI5 = "http://ajbtest.com/weixin/operate/device_arming.jsp";
		String REDIRECT_URI6 = "http://ajbtest.com/weixin/operate/device_information.jsp";
		String REDIRECT_URI7 = "http://ajbtest.com/weixin/log/device_state.jsp";
		String REDIRECT_URI8 = "http://ajbtest.com/weixin/log/armingLog.jsp";
		String REDIRECT_URI9 = "http://ajbtest.com/weixin/log/operateLog.jsp";
		String REDIRECT_URI10 = "http://ajbtest.com/weixin/operate/onePassword.jsp";
		
		String APPID= "wx0ca24712802afe2c";
		String SCOPE = "snsapi_base";
		//设置各个页面的菜单路径
		String url = CommonUtil.GET_CODE.replace("APPID", APPID).replace("REDIRECT_URI", REDIRECT_URI).replace("SCOPE", SCOPE);
		String url2 = CommonUtil.GET_CODE.replace("APPID", APPID).replace("REDIRECT_URI", REDIRECT_URI2).replace("SCOPE", SCOPE);
		String url3 = CommonUtil.GET_CODE.replace("APPID", APPID).replace("REDIRECT_URI", REDIRECT_URI3).replace("SCOPE", SCOPE);
		String url4 = CommonUtil.GET_CODE.replace("APPID", APPID).replace("REDIRECT_URI", REDIRECT_URI4).replace("SCOPE", SCOPE);
		String url5 = CommonUtil.GET_CODE.replace("APPID", APPID).replace("REDIRECT_URI", REDIRECT_URI5).replace("SCOPE", SCOPE);
		String url6 = CommonUtil.GET_CODE.replace("APPID", APPID).replace("REDIRECT_URI", REDIRECT_URI6).replace("SCOPE", SCOPE);
		String url7 = CommonUtil.GET_CODE.replace("APPID", APPID).replace("REDIRECT_URI", REDIRECT_URI7).replace("SCOPE", SCOPE);
		String url8 = CommonUtil.GET_CODE.replace("APPID", APPID).replace("REDIRECT_URI", REDIRECT_URI8).replace("SCOPE", SCOPE);
		String url9 = CommonUtil.GET_CODE.replace("APPID", APPID).replace("REDIRECT_URI", REDIRECT_URI9).replace("SCOPE", SCOPE);
		String url10 = CommonUtil.GET_CODE.replace("APPID", APPID).replace("REDIRECT_URI", REDIRECT_URI10).replace("SCOPE", SCOPE);
		
		Menu menu = new Menu();//菜单对象
		
		Button button1 = new Button();//第一个一级菜单
		button1.setName("管理");

		ViewMenu viewMenu = new ViewMenu();//第一个一级菜单的第一个子菜单
		viewMenu.setName("生成二维码");
		viewMenu.setType("view");
		viewMenu.setUrl(url);
		
		ClickMenu clickMenu1 = new ClickMenu();
		clickMenu1.setName("添加用户");
		clickMenu1.setType("scancode_waitmsg");
		clickMenu1.setKey("adduser");
		
		ClickMenu clickMenu2 = new ClickMenu();
		clickMenu2.setName("添加设备");
		clickMenu2.setType("scancode_waitmsg");
		clickMenu2.setKey("adddevice");
		
		ViewMenu viewMenu1 = new ViewMenu();
		viewMenu1.setName("用户管理");
		viewMenu1.setType("view");
		viewMenu1.setUrl(url2);
		
		ViewMenu viewMenu2 = new ViewMenu();
		viewMenu2.setName("设备管理");
		viewMenu2.setType("view");
		viewMenu2.setUrl(url3);
		
		button1.setSub_button(new Button[] {viewMenu,clickMenu1,viewMenu1,clickMenu2,viewMenu2});//设置第一个一级菜单的子菜单
		
		Button button2 = new Button();//第二个一级菜单
		button2.setName("操作");
		
		ViewMenu viewMenu8 = new ViewMenu();
		viewMenu8.setName("消息设置");
		viewMenu8.setType("view");
		viewMenu8.setUrl(url6);
		
		ViewMenu viewMenu3 = new ViewMenu();
		viewMenu3.setName("设备撤防");
		viewMenu3.setType("view");
		viewMenu3.setUrl(url4);
		
		ViewMenu viewMenu4 = new ViewMenu();
		viewMenu4.setName("设备布防");
		viewMenu4.setType("view");
		viewMenu4.setUrl(url5);
		
		ViewMenu viewMenu10 = new ViewMenu();
		viewMenu10.setName("一键设置");
		viewMenu10.setType("view");
		viewMenu10.setUrl(url10);
		
		button2.setSub_button(new Button[] {viewMenu10,viewMenu8,viewMenu3,viewMenu4});//设置第2个一级菜单的子菜单
		
		Button button3 = new Button();//第三个一级菜单
		button3.setName("查询");
		
		ViewMenu viewMenu5 = new ViewMenu();
		viewMenu5.setName("报警记录");
		viewMenu5.setType("view");
		viewMenu5.setUrl(url8);
		
		ViewMenu viewMenu7 = new ViewMenu();
		viewMenu7.setName("操作记录");
		viewMenu7.setType("view");
		viewMenu7.setUrl(url9);
		
		ViewMenu viewMenu6 = new ViewMenu();
		viewMenu6.setName("设备状态");
		viewMenu6.setType("view");
		viewMenu6.setUrl(url7);
		
		button3.setSub_button(new Button[] {viewMenu5,viewMenu7,viewMenu6});//设置第3个一级菜单的子菜单
		
		menu.setButton(new Button[] {button1,button2,button3});//设置一级菜单
		return JSONObject.fromObject(menu).toString();
	}
    
    /**
   	 * 创建菜单
   	 * @param accessToken
   	 * @param meun
   	 * @return
   	 */
    public static JsonNode CreateMenu(String accessToken,String meun) {
		String url = MEUN_URL.replace("ACCESS_TOKEN", accessToken);
		JsonNode node = httpsRequest(url, "POST", meun);
		return node;
	}
   	
   	/**
   	 * 删除自定义菜单
   	 * @param accessToken
   	 * @return
   	 *//*
   	public static JsonNode deletemenu(String accessToken) {
   		String url = DELETE_MENU.replace("ACCESS_TOKEN", accessToken);
   		JsonNode node = httpsRequest(url, "get", null);
   		return node;
   	}
   	*/
    
   	/**
   	 * 通过code获取openID 用http请求，https请求得不到值
   	 * @param APPID   
   	 * @param SECRET
   	 * @param CODE
   	 * @return
   	 */
   	public static String getOpenId(String APPID,String SECRET,String CODE) {
   		String url = GET_OPENID.replace(str[8], APPID).replace(str[34], SECRET).replace(str[29], CODE);
   		JSONObject object = doGetStr(url);
   		String openId = null;
   		
   		if (object!=null&&object.has(str[28])) {
   			openId = object.get(str[28]).toString();
		}else {
			log.error("网络授权失败，通过code获取openId失败");
		}
   		return openId;
   	}
   	
   	/**
   	 * 为字符串右边补0使其达到指定长度
   	 * @param str   要在右边补0的字符串
   	 * @param strLength	补0后的字符串长度
   	 * @return
   	 */
   	public static String addZeroForNum(String str, int strLength) {
   		int strLen = str.length();
   		StringBuffer sb = new StringBuffer();
   		sb.append(str);
   		
   		while (strLen < strLength) {
   			sb.append("0");//右补0
   			strLen++;
   		}
   		return sb.toString();
   	}
   	
   	/**
   	 * java过滤emoji字符，表情修改为"",emoji字符utf8不能正常显示
   	 * @param str
   	 * @return
   	 */
   	public static String filter(String str) {
		if(str.trim().isEmpty()){
			return str;
		}
		String pattern = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]";
		String reStr = "";
		Pattern emoji = Pattern.compile(pattern);
		Matcher  emojiMatcher = emoji.matcher(str);
		str = emojiMatcher.replaceAll(reStr);
		return str;
	}
   	
   	
   	/**
	 * 通过二分法在对象集合中查找指定对象（对象必须实现comparable接口，重写compareTo方法）
	 * @param list 排序好的对象集合
	 * @param key  要查找的对象
	 * @return
	 */
	public static <T> Object BinarySearch(List<? extends Comparable<? super T>> list, T key) {
		 int low = 0;
	     int high = list.size()-1;

	     while (low <= high) {
	         int mid = (low + high) >>> 1;
	         Comparable<? super T> midVal = list.get(mid);
	         int cmp = midVal.compareTo(key);

	         if (cmp < 0)
	             low = mid + 1;
	         else if (cmp > 0)
	             high = mid - 1;
	         else
	             return midVal; // key found
	     }
	     return null;  // key not found
    }
}  