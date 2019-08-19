package com.wx.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * MySQL数据库的备份与恢复 缺陷：可能会被杀毒软件拦截
 * 
 * @author xxx
 * @version xxx
 */
public class DatabaseBackup {
	private Log log = LogFactory.getLog(DatabaseBackup.class);
	/** MySQL安装目录的Bin目录的绝对路径 */
	private String mysqlBinPath;
	/** 访问MySQL数据库的用户名 */
	private String username;
	/** 访问MySQL数据库的密码 */
	private String password;
	public String getMysqlBinPath() {
		return mysqlBinPath;
	}
	public void setMysqlBinPath(String mysqlBinPath) {
		this.mysqlBinPath = mysqlBinPath;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public DatabaseBackup(String mysqlBinPath, String username, String password) {
		if (!mysqlBinPath.endsWith(File.separator)) {
			mysqlBinPath = mysqlBinPath + File.separator;
		}
		this.mysqlBinPath = mysqlBinPath;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "DatabaseBackup [mysqlBinPath=" + mysqlBinPath + ", username=" + username + ", password=" + password
				+ "]";
	}
	/**
	 * 备份数据库，mysqlBinPath和备份的路径中都不能有空格
	 * 
	 * @param dbname
	 *            要备份的数据库
	 */
	public void backup(String dbname) {
		String format = new SimpleDateFormat("yyyy-MM-dd").format(System.currentTimeMillis());
		String command = "cmd /c " + mysqlBinPath + "mysqldump -u" + username
				+ " -p" + password + " --set-charset=utf8 " + dbname + ">c:\\lock/lock_"+format+".sql";
		try {
			Process p = Runtime.getRuntime().exec(command);
			InputStream is1 = p.getInputStream();
			InputStream is2 = p.getErrorStream();
			
			new Thread(new Runnable() {
	             public void run() {
	                 BufferedReader br = new BufferedReader(new InputStreamReader(is1));
	                 try{
	                	 while(br.readLine() != null) ;
	                 }
	                 catch(Exception e) {
	                	 e.printStackTrace();
	                 }finally {
	                	 try {
							is1.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
	                 }
	             }
	         }).start(); // 启动单独的线程来清空p.getInputStream()的缓冲区
			
			new Thread(new Runnable() {
	             public void run() {
	                 BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
	                 try{
	                	 while(br2.readLine() != null) ;
	                 }
	                 catch(Exception e) {
	                	 e.printStackTrace();
	                 }finally {
	                	 try {
							is2.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
	                 }
	             }
	         }).start(); // 启动单独的线程来清空p.getErrorStream()的缓冲区
			p.waitFor();
			p.destroy();
			log.info("备份数据库到："+"c:\\lock/lock_"+format+".sql");
		}catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	/**
	 * 恢复数据库
	 * 
	 * @param dest
	 *            备份文件的路径
	 * @param dbname
	 *            数据库名
	 */
	public String restore(String dbname,String fileUrl) {
		try {
			String command = "cmd /c "+mysqlBinPath + "mysql -u" + username
					+ " -p" + password + " " + dbname + "<"+fileUrl;
			Process p = Runtime.getRuntime().exec(command);
			InputStream is1 = p.getInputStream();
			InputStream is2 = p.getErrorStream();
			
			new Thread(new Runnable() {
	             public void run() {
	                 BufferedReader br = new BufferedReader(new InputStreamReader(is1));
	                 try{
	                	 while(br.readLine() != null) ;
	                 }
	                 catch(Exception e) {
	                	 e.printStackTrace();
	                 }finally {
	                	 try {
							is1.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
	                 }
	             }
	         }).start(); // 启动单独的线程来清空p.getInputStream()的缓冲区
			
			new Thread(new Runnable() {
	             public void run() {
	                 BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
	                 try{
	                	 while(br2.readLine() != null) ;
	                 }
	                 catch(Exception e) {
	                	 e.printStackTrace();
	                 }finally {
	                	 try {
							is2.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
	                 }
	             }
	         }).start(); // 启动单独的线程来清空p.getErrorStream()的缓冲区
			p.waitFor();
			p.destroy();
			log.info("恢复数据库："+fileUrl);
			return "恢复成功";
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "恢复失败";
		}
	}
}
