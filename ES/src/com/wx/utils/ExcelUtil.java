package com.wx.utils;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.wx.entity.Logs;

public class ExcelUtil {

    /**
     * 导出Excel
     * @param sheetName sheet名称
     * @param title 标题
     * @param values 内容
     * @param wb HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName,String []title,List<Logs> list, HSSFWorkbook wb){

        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if(wb == null){
            wb = new HSSFWorkbook();
        }

        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);

        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式

        //声明列对象
        HSSFCell cell = null;

        //创建标题
        for(int i=0;i<title.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }

        //创建内容
        for(int i=0;i<list.size();i++){
            row = sheet.createRow(i + 1);
            HSSFCell createCell0 = row.createCell(0);
            createCell0.setCellValue(list.get(i).getUnit_name());
            createCell0.setCellStyle(style);
            
            HSSFCell createCell1 = row.createCell(1);
            createCell1.setCellValue(list.get(i).getTeam_name());
            createCell1.setCellStyle(style);
            
            HSSFCell createCell2 = row.createCell(2);
            createCell2.setCellValue(list.get(i).getKeybox_name());
            createCell2.setCellStyle(style);
            
            HSSFCell createCell3 = row.createCell(3);
            createCell3.setCellValue(list.get(i).getUsers_name());
            createCell3.setCellStyle(style);
            
            HSSFCell createCell4 = row.createCell(4);
            createCell4.setCellValue(list.get(i).getManager_name());
            createCell4.setCellStyle(style);
            
            HSSFCell createCell5 = row.createCell(5);
            createCell5.setCellValue(list.get(i).getKeyss_name());
            createCell5.setCellStyle(style);
            
            HSSFCell createCell6 = row.createCell(6);
            createCell6.setCellValue(list.get(i).getStyle());
            createCell6.setCellStyle(style);
            
            HSSFCell createCell7 = row.createCell(7);
            createCell7.setCellValue(list.get(i).getApplication_time());
            createCell7.setCellStyle(style);
            
            HSSFCell createCell8 = row.createCell(8);
            createCell8.setCellValue(list.get(i).getApprove_time());
            createCell8.setCellStyle(style);
            
            HSSFCell createCell9 = row.createCell(9);
            createCell9.setCellValue(list.get(i).getGet_time());
            createCell9.setCellStyle(style);
            
            HSSFCell createCell10 = row.createCell(10);
            createCell10.setCellValue(list.get(i).getCard_id());
            createCell10.setCellStyle(style);
        }
        
        //设置所有列自适应宽度
        for(int i=0;i<title.length;i++){
            sheet.autoSizeColumn(i);
        }
        return wb;
    }
}