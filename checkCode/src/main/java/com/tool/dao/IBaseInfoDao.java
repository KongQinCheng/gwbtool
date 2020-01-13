package com.tool.dao;


import com.tool.bean.po.BaseInfo;
import com.tool.bean.vo.BaseInfoVo;

import java.util.List;
import java.util.Map;

public interface IBaseInfoDao {

     void insert(BaseInfo baseInfo) ;

     Map<String,Object> isExitByBmAndGg(BaseInfo baseInfo) ;

     List<BaseInfo> getAll();
     List<BaseInfo> getInfoByBaseInfoVo(BaseInfoVo baseInfoVo);

      void  delBaseInfo(BaseInfoVo baseInfoVo);


}
