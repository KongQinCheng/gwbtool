package com.tool.controller.controller;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tool.bean.po.BaseInfo;
import com.tool.bean.vo.BaseInfoVo;
import com.tool.dao.IBaseInfoDao;
import com.tool.services.ICheckCodeServices;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

import static com.tool.controller.controller.GetPdfDataController.buildExcelDocument;
import static com.tool.controller.controller.GetPdfDataController.buildExcelFile;
import static com.tool.services.impl.CheckCodeServiceImpl.createTitle;


@RestController
@RequestMapping("/checkCode")
public class CheckCodeController {
 
    @Autowired
    private ICheckCodeServices iCheckCodeServices;

    @Autowired
    IBaseInfoDao iBaseInfoDao;
 
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile[] MultipartFiles, HttpServletResponse response)  throws  Exception {
        String  result ="";
        Map<String,String> resultmap =null;

        MultipartFile file=MultipartFiles[0];
        String fileName = file.getOriginalFilename();

        //获取Excel内容的信息
        List<BaseInfo> list = iCheckCodeServices.batchImport(fileName, file);

        //保存到数据库中--将结果返回
        Map<String, Object> map = iCheckCodeServices.checkInsert(list);

        List<Map<String, Object>> resultList_insert=(List<Map<String, Object>>)map.get("resultList_insert");
        List<Map<String, Object>> resultList_exist=(List<Map<String, Object>>)map.get("resultList_exist");

        //Map 转成  JSONObject 字符串
        com.alibaba.fastjson.JSONObject jsonObj=new JSONObject(map);

        return  jsonObj.toString();
    }



    @PostMapping(value = "/getAll",consumes = "application/json")
    @ResponseBody
    public String getAll(  ) {
        //todo
        List<BaseInfo> all = iCheckCodeServices.getAll();
        String jsonStr = JSON.toJSONString( all );
        return jsonStr;
    }

    @PostMapping(value = "/getByBaseInfoVo",consumes = "application/json")
    @ResponseBody
    public String getByBaseInfoVo(  @RequestBody BaseInfoVo baseInfoVo  ) {
        //todo
        List<BaseInfo> all = iCheckCodeServices.getInfoByBaseInfoVo(baseInfoVo);
        String jsonStr = JSON.toJSONString( all );
        return jsonStr;
    }


    @RequestMapping(value = "/downLoadData")
    @ResponseBody
    public void downLoad(HttpServletResponse response) {
        //todo

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("已经存在的数据");
        createTitle(workbook,sheet);

        List<BaseInfo> list = iBaseInfoDao.getAll();
        try {
            HSSFCellStyle style = workbook.createCellStyle();
            style.setVerticalAlignment(CellStyle.ALIGN_LEFT);

            for (int i = 0; i <list.size() ; i++) {
                HSSFRow row = sheet.createRow(i+1);
                row.setRowStyle(style);
                row.createCell(0).setCellValue(list.get(i).getWlbm());
                row.createCell(1).setCellValue(list.get(i).getWlmc());
                row.createCell(2).setCellValue(list.get(i).getWlgg());
                row.createCell(3).setCellValue(list.get(i).getWlgys());
            }

            String outfileName = "物料编码.xls";
            buildExcelFile(outfileName, workbook);
            //浏览器下载excel
            buildExcelDocument(outfileName,workbook,response);

        } catch (Exception e) {
            e.printStackTrace();
        }
     }

    @RequestMapping(value = "/downLoadDataSearch")
    @ResponseBody
    public void downLoadDataSearch(@RequestBody BaseInfoVo baseInfoVo ,HttpServletResponse response) {
        //todo

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("已经存在的数据");
        createTitle(workbook,sheet);

        List<BaseInfo> list = iBaseInfoDao.getInfoByBaseInfoVo(baseInfoVo);
        try {
            HSSFCellStyle style = workbook.createCellStyle();
            style.setVerticalAlignment(CellStyle.ALIGN_LEFT);

            for (int i = 0; i <list.size() ; i++) {
                HSSFRow row = sheet.createRow(i+1);
                row.setRowStyle(style);
                row.createCell(0).setCellValue(list.get(i).getWlbm());
                row.createCell(1).setCellValue(list.get(i).getWlmc());
                row.createCell(2).setCellValue(list.get(i).getWlgg());
                row.createCell(3).setCellValue(list.get(i).getWlgys());
            }

            String outfileName = "物料编码.xls";
            buildExcelFile(outfileName, workbook);
            //浏览器下载excel
            buildExcelDocument(outfileName,workbook,response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PostMapping(value = "/delBaseInfo",consumes = "application/json")
    @ResponseBody
    public void delBaseInfo(  @RequestBody BaseInfoVo baseInfoVo  ) {
        //todo
        iCheckCodeServices.delBaseInfo(baseInfoVo);

    }

}
