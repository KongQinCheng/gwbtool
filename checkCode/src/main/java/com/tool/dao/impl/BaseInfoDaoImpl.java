package com.tool.dao.impl;

import com.tool.bean.po.BaseInfo;
import com.tool.bean.vo.BaseInfoVo;
import com.tool.dao.IBaseInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tool.mapper.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BaseInfoDaoImpl implements IBaseInfoDao {


    @Autowired
    BaseInfoMapper baseInfoMapper;

    @Override
    public void insert(BaseInfo baseInfo) {
        baseInfoMapper.insert(baseInfo);
    }


    @Override
    public Map<String, Object> isExitByBmAndGg(BaseInfo baseInfo) {
        Map<String, Object> map = new HashMap<>();
        String wlbm = "";
        List<BaseInfo> exitByBmAndGg = baseInfoMapper.isExitByBmAndGg(baseInfo);
        if (exitByBmAndGg.size() == 0) {
            map.put("isExit", "0");
        } else {

            //特殊处理如下情况：后面插入的的数据 比前面的数据好一些属性
            //G13灯头～3004铝-厚0.3mm～1.2mm绝缘板-本色～带NL字样～无LOGO
            //G13灯头～3004铝-厚0.3mm～1.2mm绝缘板-本色～无LOGO

            boolean checkFlag = false;

            for (int i = 0; i < exitByBmAndGg.size(); i++) {//必须全部都不一样才设置不一样
                BaseInfo baseInfo1 = exitByBmAndGg.get(i);
                if (check2gg(baseInfo.getWlgg(), baseInfo1.getWlgg())) {
                    checkFlag = true;
                    break;
                }
            }
            if (checkFlag) {
                for (int i = 0; i < exitByBmAndGg.size(); i++) {
                    wlbm += exitByBmAndGg.get(i).getWlbm() + ",";
                }
                map.put("isExit", "n");
                if (wlbm.length() >= -1) {
                    map.put("wlbm", wlbm.substring(0, wlbm.length() - 1));
                }
            } else {
                map.put("isExit", "0");
            }


        }
        return map;
    }

    @Override
    public List<BaseInfo> getAll() {

        List<BaseInfo> list=   baseInfoMapper.getListAll();
        return list;
    }

    @Override
    public List<BaseInfo> getInfoByBaseInfoVo(BaseInfoVo baseInfoVo) {
        List<BaseInfo> list=   baseInfoMapper.getInfoByBaseInfoVo(baseInfoVo);
        return list;
    }

    @Override
    public void delBaseInfo(BaseInfoVo baseInfoVo) {
        baseInfoMapper.delBaseInfo(baseInfoVo);
    }

    /***
     * 传入两个规格的字符串，判断是否完全一致
     * @return
     */
    public static boolean check2gg(String wlgg1, String wlgg2) {

        String[] split1 = null;
        String[] split2 = null;
        if (wlgg1.indexOf("～") > -1) {
            split1 = wlgg1.split("～");
        }else {
            split1=new String[]{wlgg1};
        }
        if (wlgg2.indexOf("～") > -1) {
            split2 = wlgg2.split("～");
        }else {
            split2=new String[]{wlgg2};
        }

        if (split1.length == split2.length) {
          return true;
         }

        return false;
    }


}
