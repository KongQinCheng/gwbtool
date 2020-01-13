package com.tool.services.impl;

import com.tool.services.IGetPdfDataService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GetPdfDataServiceImpl implements IGetPdfDataService {


    @Override
    public Map<String,String> batchImport(String fileName, MultipartFile file) throws Exception {
        Map<String,String> resultmap =new HashMap<String, String>();
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
        Sheet sheet2 = wb.getSheetAt(1);

        String moreContent  =sheet.getRow(2).getCell(1).getStringCellValue();

        //Test:U:230.03V I:0.0261A P:4.7700W PF:0.7933 Freq:50.00Hz
        moreContent =moreContent.replace("Test:","");
        String strarry[] = moreContent.split(" ");


        String UvalueMore =strarry[0].split(":")[1];
        int  Uvlaue =(int) Double.parseDouble( UvalueMore.replace("V",""));
        String Ivalue =strarry[1].split(":")[1].replace("A","");

        String Pvalue =strarry[2].split(":")[1].replace("W","");
        String PFvalue =strarry[3].split(":")[1];

        String name  =sheet.getRow(3).getCell(1).getStringCellValue().replace("NAME: ","");
        String  Total =sheet.getRow(9).getCell(5).getStringCellValue();
        String  TotalCBCP =sheet.getRow(7).getCell(5).getStringCellValue();

        String kvalue ="";
        double  kvalued =0.0;
        if(!isNumeric(Total)){  //异常处理 ，有时候PDF生成的时候回是在第5列，而不是在第六列
            Total =sheet.getRow(9).getCell(4).getStringCellValue();
        }
        if(!isNumeric(TotalCBCP)){  //异常处理 ，有时候PDF生成的时候回是在第5列，而不是在第六列
            TotalCBCP =sheet.getRow(7).getCell(4).getStringCellValue();
        }


        if(isNumeric(Total)&& isNumeric(TotalCBCP) ){  //异常处理 ，有时候PDF生成的时候回是在第5列，而不是在第六列
              kvalued =  Double.parseDouble(TotalCBCP)/Double.parseDouble(Total) ;
              kvalued=((int)(kvalued*100))/100.0;
              kvalue = kvalued+"";
        }else {
            TotalCBCP="本数据存在问题请手动处理";
            Total="本数据存在问题请手动处理";
            kvalue ="本数据存在问题请手动处理";
        }

        String  peiguanjiaodu =sheet.getRow(33).getCell(1).getStringCellValue().split(":")[1].replace("DEG","").replace(" ","");

        //sheet 内容
        String  peiguan90 =sheet2.getRow(12).getCell(12).getStringCellValue();

        resultmap.put("name",name);
        resultmap.put("Uvlaue",Uvlaue+"");
        resultmap.put("Ivalue",Ivalue);
        resultmap.put("Pvalue",Pvalue);
        resultmap.put("PFvalue",PFvalue);

        resultmap.put("Total",Total);
        resultmap.put("TotalCBCP",TotalCBCP);
        resultmap.put("kvalue",kvalue);

        resultmap.put("peiguanjiaodu",peiguanjiaodu);
        resultmap.put("peiguan90",peiguan90);

        return resultmap;
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

    public static void main(String[] args) {
        double db = 13.4314;
        db=((int)(db*100))/100.0; //注意：使用100.0 而不是 100
        System.out.println(db);
    }





}
