package com.tool.services;

import com.tool.bean.po.BaseInfo;
import com.tool.bean.vo.BaseInfoVo;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Map;

public interface ICheckCodeServices {

    List<BaseInfo> batchImport(String fileName, MultipartFile file) throws Exception;

    Map<String, Object> checkInsert(List<BaseInfo> list);

    List<BaseInfo> getAll() ;

    List<BaseInfo>  getInfoByBaseInfoVo(BaseInfoVo baseInfoVo);

    void downLoad();

    void delBaseInfo(BaseInfoVo baseInfoVo);






}
