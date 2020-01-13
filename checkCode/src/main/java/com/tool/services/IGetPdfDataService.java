package com.tool.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IGetPdfDataService {

    Map<String,String> batchImport(String fileName, MultipartFile file) throws Exception;
    
}
