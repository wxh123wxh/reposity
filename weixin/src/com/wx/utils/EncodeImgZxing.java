package com.wx.utils;


import java.awt.image.BufferedImage;  
import java.io.File;  
import java.io.IOException;  
import java.io.OutputStream;  
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.zxing.BarcodeFormat;  
import com.google.zxing.EncodeHintType;  
import com.google.zxing.MultiFormatWriter;  
import com.google.zxing.common.BitMatrix;  
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;  
/** 
 * 通过google的zxing实现二维码(加入logo图片) 
 * @author tskk 
 * @version 2015-6-26 13:30:20 
 * */  
@SuppressWarnings("unchecked")
public final class EncodeImgZxing {   
    //二维码颜色  
    private static final int BLACK = 0xFF000000;//0xFFFF0000，红色  
    private static final Log log  = LogFactory.getLog(EncodeImgZxing.class);
    //二维码背景色  
    private static final int WHITE = 0xFFFFFFFF;//0xFF0000FF，蓝色  
    //注：二维码颜色色差大，扫描快，但如果"BLACK'设置为黑色外其他颜色，可能无法扫描  
    //二维码图片宽度  
    private static final int width = 300;  
    //二维码图片高度  
    private static final int height = 300;  
    //二维码格式参数  
	@SuppressWarnings("rawtypes")
	private static HashMap hints=new HashMap();
    static{  
        hints.put(EncodeHintType.CHARACTER_SET,"utf-8");    //指定字符编码为“utf-8”
        hints.put(EncodeHintType.ERROR_CORRECTION,ErrorCorrectionLevel.M);  //指定二维码的纠错等级为中级
        hints.put(EncodeHintType.MARGIN, 2);    //设置图片的边距
    } 
    /** 
     * 绘制二维码 
     * @param contents 二维码内容   
     * @return image 二维码图片 
     * */  
    public static BufferedImage encodeImg(String contents){  
        BufferedImage image = null;  
        try{  
            BitMatrix matrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);  
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
            int width = matrix.getWidth();  
            int height = matrix.getHeight();  
            for(int x = 0; x < width; x++){  
                for(int y =0;y < height; y++){  
                    image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);  
                }  
            }  
        }catch(Exception e){  
            log.error("生成二维码失败"+e.getMessage());  
        }  
        return image;  
    }  
      
    /** 
     * 二维码输出到文件 
     *  @param contents 二维码内容 
     * @param format 图片格式 
     * @param file 输出文件 
     * */  
    public static void writeToFile(String contents,String format,File file){  
        BufferedImage image = encodeImg(contents);  
        try {  
            ImageIO.write(image, format, file);  
        } catch (IOException e) {  
        	log.error("二维码写入文件失败"+e.getMessage());  
        }  
    }  
    /** 
     * 二维码流式输出 
     *  @param contents 二维码内容 
     * @param format 图片格式 
     * @param stream 输出流 
     * */  
    public static void writeToStream(String contents,String format,OutputStream stream){  
        BufferedImage image = encodeImg(contents);  
        try {  
            ImageIO.write(image, format, stream);  
        } catch (IOException e) {  
        	log.error("二维码写入流失败"+e.getMessage());  
        }  
    }  
}  