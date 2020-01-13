package com.tool.mapper;

import com.tool.bean.po.BaseInfo;
import com.tool.bean.vo.BaseInfoVo;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * moduleInfo的持久层接口
 *
 * @author chenzuyi
 * @version Revision 1.0.0
 * *修改时间         | 修改内容
 */

@Mapper
public interface BaseInfoMapper {

    @Insert("INSERT baseinfo (wlbm,wlmc,wlgg,fbfs,wlgys) values('${wlbm}','${wlmc}','${wlgg}','${fbfs}', '${wlgys}')")
    public void insert(BaseInfo baseInfo);


    @Select("select * from baseinfo where 1=1  ")
    @Results(id = "baseInfoResults", value = {
            @Result(column = "wlbm", property = "wlbm"),
            @Result(column = "wlmc", property = "wlmc"),
            @Result(column = "wlgg", property = "wlgg"),
            @Result(column = "fbfs", property = "fbfs"),
            @Result(column = "wlgys", property = "wlgys")})
    public List<BaseInfo> getListAll();

    @SelectProvider(type = com.tool.sqlProvider.BaseInfoSqlProvider.class, method = "getInfoByBaseInfoVo")
    @ResultMap("baseInfoResults")
    List<BaseInfo> getInfoByBaseInfoVo(BaseInfoVo baseInfoVo);



    @SelectProvider(type = com.tool.sqlProvider.BaseInfoSqlProvider.class, method = "isExitByBmAndGg")
    @ResultMap("baseInfoResults")
    List<BaseInfo> getEntryByStockCode(BaseInfo baseInfo);


    @Delete("delete  from baseinfo where id = '${id}'  ")
     void delBaseInfo(BaseInfoVo baseInfoVo);


    @SelectProvider(type = com.tool.sqlProvider.BaseInfoSqlProvider.class, method = "isExitByBmAndGg")
    @ResultMap("baseInfoResults")
//    @Results(id = "baseInfoResults", value = {
//            @Result(column = "count", property = "count")})
    public List<BaseInfo>  isExitByBmAndGg(BaseInfo baseInfo);


}
