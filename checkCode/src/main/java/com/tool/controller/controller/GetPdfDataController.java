package com.tool.controller.controller;


import com.tool.services.IGetPdfDataService;
import net.sf.json.JSONArray;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/upload")
public class GetPdfDataController {
 
    @Autowired
    private IGetPdfDataService iGetPdfDataService;
 
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile[] MultipartFiles, HttpServletResponse response) {
        String  result ="";
        Map<String,String> resultmap =null;

        if(MultipartFiles != null && MultipartFiles.length != 0){
            if(null != MultipartFiles && MultipartFiles.length > 0){

                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("统计表");
                createTitle(workbook,sheet);

                //遍历并保存文件
                for (int i = 0; i <MultipartFiles.length ; i++) {
                    MultipartFile file=MultipartFiles[i];

                    String fileName = file.getOriginalFilename();
                    try {

                        resultmap = iGetPdfDataService.batchImport(fileName, file);

                        HSSFCellStyle style = workbook.createCellStyle();
                        style.setVerticalAlignment(CellStyle.ALIGN_LEFT);

                        HSSFRow row = sheet.createRow(i+1);
                        row.setRowStyle(style);

                        row.createCell(0).setCellValue( i+1+"");
                        row.createCell(1).setCellValue( resultmap.get("name"));
                        row.createCell(2).setCellValue( resultmap.get("Uvlaue"));

                        row.createCell(3).setCellValue( resultmap.get("Pvalue"));
                        row.createCell(4).setCellValue( resultmap.get("Ivalue"));

                        row.createCell(5).setCellValue( resultmap.get("PFvalue"));
                        row.createCell(6).setCellValue( resultmap.get("Total"));
                        row.createCell(7).setCellValue( resultmap.get("TotalCBCP"));
                        row.createCell(8).setCellValue( resultmap.get("kvalue"));
                        row.createCell(9).setCellValue( resultmap.get("peiguan90"));
                        row.createCell(10).setCellValue( resultmap.get("peiguanjiaodu"));


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                String outfileName = "配光汇总.xls";

                //生成excel文件
                try {
                    buildExcelFile(outfileName, workbook);
                    //浏览器下载excel
                    buildExcelDocument(outfileName,workbook,response);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }


        return  result;
    }


    @PostMapping("/upload2")
    public String upload2(@RequestParam("file") MultipartFile[] MultipartFiles) {
        String result = "";
        Map<String, String> resultmap = null;
        List<Map<String,String>> list= new ArrayList<Map<String,String>>();
        try {
            if(MultipartFiles != null && MultipartFiles.length != 0){
                if(null != MultipartFiles && MultipartFiles.length > 0) {

                    for (int i = 0; i < MultipartFiles.length; i++) {
                        MultipartFile file = MultipartFiles[i];
                        String fileName = file.getOriginalFilename();
                        resultmap = iGetPdfDataService.batchImport(fileName, file);
                        list.add(resultmap);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray json =  JSONArray.fromObject(list);
        result=json.toString();
        return result;
    }



    //创建表头
    private void createTitle(HSSFWorkbook workbook,HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        sheet.setAutobreaks(true);
        row.setHeight( (short) 1000);
        sheet.setDefaultColumnWidth(10);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(1,35*256);




        //设置为居中加粗
        HSSFCellStyle style = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(HSSFColor.LIME.index);

        style.setTopBorderColor(HSSFColor.BLACK.index);
        style.setRightBorderColor(HSSFColor.BLACK.index);
        style.setBottomBorderColor(HSSFColor.BLACK.index);
        style.setLeftBorderColor(HSSFColor.BLACK.index);
        style.setVerticalAlignment(CellStyle.ALIGN_LEFT);
        style.setWrapText(true);

        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 16);//设置字体大小

        HSSFCell cell;
        cell = row.createCell(0);
        cell.setCellValue("NO.");
        cell.setCellStyle(style);


        cell = row.createCell(1);
        cell.setCellValue("Model Description");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("配光Input\n" +
                "Voltage\n" +
                "(V)");
        cell.setCellStyle(style);


        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("配光Input\n" +
                "Wattage\n" +
                "(W)");

        cell = row.createCell(4);
        cell.setCellStyle(style);
        cell.setCellValue("配光Input\n" +
                "Current\n" +
                "(A)");

        cell = row.createCell(5);
        cell.setCellValue("配光PF");
        cell.setCellStyle(style);

        cell = row.createCell(6);
        cell.setCellValue("配光Φ_Total(lm)");
        cell.setCellStyle(style);

        cell = row.createCell(7);
        cell.setCellValue("配光CBCP");
        cell.setCellStyle(style);

        cell = row.createCell(8);
        cell.setCellValue("配光K值");
        cell.setCellStyle(style);

        cell = row.createCell(9);
        cell.setCellValue("配光90°Φ_Total(lm)");
        cell.setCellStyle(style);

        cell = row.createCell(10);
        cell.setCellValue("配光角度");
        cell.setCellStyle(style);
    }


    //生成excel文件
    public static void buildExcelFile(String filename,HSSFWorkbook workbook) throws Exception{
        FileOutputStream fos = new FileOutputStream(filename);
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    //浏览器下载excel
    public static void buildExcelDocument(String filename, HSSFWorkbook workbook, HttpServletResponse response) throws Exception{
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename, "utf-8"));
        OutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }











}
