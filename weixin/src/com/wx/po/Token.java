package com.wx.po;

/**
 * Token类保存微信接口凭证Token
 * @author Administrator
 *
 */
public class Token {  
    // 接口访问凭证  
    private static String accessToken;  
    // 凭证有效期，单位：秒  
    private int expiresIn;  
  
    public String getAccessToken() {  
        return accessToken;  
    }  
  
    @SuppressWarnings("static-access")
	public void setAccessToken(String accessToken) {  
        this.accessToken = accessToken;  
    }  
  
    public int getExpiresIn() {  
        return expiresIn;  
    }  
  
    public void setExpiresIn(int expiresIn) {  
        this.expiresIn = expiresIn;  
    }  
    
}  