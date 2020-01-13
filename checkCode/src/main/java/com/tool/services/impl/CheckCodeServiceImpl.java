package com.tool.services.impl;

import com.tool.bean.po.BaseInfo;
import com.tool.bean.vo.BaseInfoVo;
import com.tool.dao.IBaseInfoDao;
import com.tool.services.ICheckCodeServices;
import com.tool.services.IGetPdfDataService;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.midi.Soundbank;
import javax.swing.plaf.PanelUI;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.tool.controller.controller.GetPdfDataController.buildExcelDocument;
import static com.tool.controller.controller.GetPdfDataController.buildExcelFile;

@Service
public class CheckCodeServiceImpl implements ICheckCodeServices {

    @Autowired
    IBaseInfoDao iBaseInfoDao;


    @Override
    public  List<BaseInfo> batchImport(String fileName, MultipartFile file) throws Exception {
        List<BaseInfo> list =new ArrayList<>();
        boolean notNull = false;

        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)$")) {

        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
            Workbook wb = null;
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }

        //第一个 Sheet 的内容
        Sheet sheet = wb.getSheetAt(0);

        System.out.println("sheet页面总数"+wb.getNumberOfSheets());
        for (int i = 0; i <wb.getNumberOfSheets() ; i++) {
            System.out.println("页面名称："+   wb.getSheetAt(i).getSheetName());
        }


        int  lineLength = sheet.getPhysicalNumberOfRows();


        for (int i = 1; i <lineLength ; i++) {
            BaseInfo baseInfo=new BaseInfo();
            if(sheet.getRow(i).getCell(0)!=null){
                sheet.getRow(i).getCell(0).setCellType(Cell.CELL_TYPE_STRING); //物料编码
                baseInfo.setWlbm(sheet.getRow(i).getCell(0).getStringCellValue()); //物料编码
            }


            if(sheet.getRow(i).getCell(1)!=null){
                sheet.getRow(i).getCell(1).setCellType(Cell.CELL_TYPE_STRING); //物料名称
                baseInfo.setWlmc(sheet.getRow(i).getCell(1).getStringCellValue()); //物料名称
            }


            if(sheet.getRow(i).getCell(2)!=null){
                sheet.getRow(i).getCell(2).setCellType(Cell.CELL_TYPE_STRING); //物料规格
                baseInfo.setWlgg(sheet.getRow(i).getCell(2).getStringCellValue()); //物料规格
            }



            if(sheet.getRow(i).getCell(3)!=null){
                sheet.getRow(i).getCell(3).setCellType(Cell.CELL_TYPE_STRING); //发布方式
                baseInfo.setFbfs(sheet.getRow(i).getCell(3).getStringCellValue()); //发布方式
            }



            if(sheet.getRow(i).getCell(4)!=null){
                sheet.getRow(i).getCell(4).setCellType(Cell.CELL_TYPE_STRING); //物料供应商
                baseInfo.setWlgys(sheet.getRow(i).getCell(4).getStringCellValue()); //物料供应商
            }

            list.add(baseInfo);

        }

        return list;
    }

    @Override
    public  Map<String, Object> checkInsert(List<BaseInfo> list) {

        Map<String, Object> returnMap =new HashMap<>();
        List<Map<String, Object>> resultList_insert=new ArrayList<>();
        List<Map<String, Object>> resultList_exist=new ArrayList<>();

        returnMap.put("resultList_insert",resultList_insert);
        returnMap.put("resultList_exist",resultList_exist);

        //检查数据库中是否已经存在
        for (int i = 0; i <list.size() ; i++) {
            BaseInfo baseInfo = list.get(i);
            Map<String, Object> resultmap = new HashMap<>();
            if (baseInfo.getWlmc()==null&&baseInfo.getWlgg()==null){
                continue;
            }
            String wlbm = baseInfo.getWlbm();
            String wlgg = baseInfo.getWlgg();

            Map<String, Object> exitByBmAndGg = iBaseInfoDao.isExitByBmAndGg(baseInfo);
            //数据库内不存在的情况下，根据不同的类型，生成不通的编码

            if (exitByBmAndGg.get("isExit").equals("0")) {  //数据不存在
                //根据类型生成编码之后，保存到数据中。
//                wlbm = getWlbbByWlmc(baseInfo.getWlmc());

                resultmap.put("isNew", true);

                baseInfo.setWlbm(wlbm);
                iBaseInfoDao.insert(baseInfo);

                resultmap.put("index", i);
                resultmap.put("wlbm", wlbm);
                resultmap.put("wlgg", wlgg);
                resultList_insert.add(resultmap);
                returnMap.put("resultList_insert",resultList_insert);

            }
            if (exitByBmAndGg.get("isExit").equals("n")) {  //数据存在 --找出相同的数据
                wlbm = exitByBmAndGg.get("wlbm").toString();
                resultmap.put("isNew", false);
                resultmap.put("index", i);
                resultmap.put("wlbm", wlbm);
                resultmap.put("wlgg", wlgg);
                resultList_exist.add(resultmap);
                returnMap.put("resultList_exist",resultList_exist);
            }
        }
        return returnMap;
    }

    @Override
    public List<BaseInfo> getAll() {
        List<BaseInfo> list = iBaseInfoDao.getAll();
        return list;
    }

    @Override
    public List<BaseInfo> getInfoByBaseInfoVo(BaseInfoVo baseInfoVo) {
        List<BaseInfo> list = iBaseInfoDao.getInfoByBaseInfoVo(baseInfoVo);
        return list;
    }

    @Override
    public void downLoad() {

    }

    @Override
    public void delBaseInfo(BaseInfoVo baseInfoVo) {
        iBaseInfoDao.delBaseInfo(baseInfoVo);
    }



    /***
     * 根据物料名称生成物料编码
     * @param wlmc
     * @return
     */
    public String  getWlbbByWlmc(String wlmc){
        return "eee";
    }


    /**
     * 判断字符串是否为数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    //创建表头
    public static void createTitle(HSSFWorkbook workbook, HSSFSheet sheet){
        HSSFRow row = sheet.createRow(0);
        sheet.setAutobreaks(true);
        row.setHeight( (short) 1000);
        sheet.setDefaultColumnWidth(10);
        //设置列宽，setColumnWidth的第二个参数要乘以256，这个参数的单位是1/256个字符宽度
        sheet.setColumnWidth(0,30*256);
        sheet.setColumnWidth(1,30*256);
        sheet.setColumnWidth(2,60*256);
        sheet.setColumnWidth(3,30*256);



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
        cell.setCellValue("物料编号");
        cell.setCellStyle(style);


        cell = row.createCell(1);
        cell.setCellValue("物料名称");
        cell.setCellStyle(style);

        cell = row.createCell(2);
        cell.setCellValue("物料规格");
        cell.setCellStyle(style);

        cell = row.createCell(3);
        cell.setCellStyle(style);
        cell.setCellValue("物料供应商");



    }

    public static void main(String[] args) {
        double db = 13.4314;
        db=((int)(db*100))/100.0; //注意：使用100.0 而不是 100
        System.out.println(db);
    }




}
