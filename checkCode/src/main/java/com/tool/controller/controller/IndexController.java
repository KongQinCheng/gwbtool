package com.tool.controller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
public class IndexController {


    @RequestMapping("/index")
    public String index(HashMap<String, Object> map) {
        return "index";
    }

    /**
     * 本地访问内容地址 ：http://localhost:8090/onefile
     * @param map
     * @return
     */
    @RequestMapping("/onefile")
    public String onefile(HashMap<String, Object> map) {
        return "onefile";
    }

    @RequestMapping("/manyfile")
    public String manyfile(HashMap<String, Object> map) {
        return "manyfile";
    }

    @RequestMapping("/checkcode")
    public String checkcode(HashMap<String, Object> map) {
        return "checkcode";
    }
}
