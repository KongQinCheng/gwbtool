package com.tool.sqlProvider;

import com.tool.bean.po.BaseInfo;
import com.tool.bean.vo.BaseInfoVo;
import org.apache.ibatis.jdbc.SQL;

public class BaseInfoSqlProvider {

    public String isExitByBmAndGg(BaseInfo baseInfo) {
        return new SQL() {
            {
                SELECT(" a.* ");
                FROM(" baseinfo a ");
                WHERE(" 1=1  ");

                String gg = baseInfo.getWlgg();

                if (gg!=null && gg.length()>0) {
                    if (gg.indexOf("～")>-1) {
                        String[] splitVal = gg.split("～");
                        for (int i = 0; i < splitVal.length; i++) {
                            WHERE(" a.wlgg like '%" + splitVal[i] + "%'");
                        }
                    }else {
                        WHERE(" a.wlgg like '%" + gg + "%'");
                    }
                }
            }
        }.toString();
    }

    public String getInfoByBaseInfoVo(BaseInfoVo baseInfoVo) {
        return new SQL() {
            {
                SELECT(" a.* ");
                FROM(" baseinfo a ");
                WHERE(" 1=1  ");
                if (!"".equals(baseInfoVo.getWlbm())){
                    WHERE(" a.wlbm like '%" + baseInfoVo.getWlbm() + "%'");
                }
                String gg = baseInfoVo.getWlgg();
                if (gg!=null && gg.indexOf("～")>-1) {
                    String[] splitVal = gg.split("～");
                    for (int i = 0; i < splitVal.length; i++) {
                        WHERE(" a.wlgg like '%" + splitVal[i] + "%'");
                    }
                }
                if (!"".equals(baseInfoVo.getWlmc())){
                    WHERE(" a.wlmc like '%" + baseInfoVo.getWlmc() + "%'");
                }

            }
        }.toString();
    }

}
