package com.stock.util;


import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HtmlUtil   {

    public static String[] splitByExpression(String expression ,String html)   {

        StringBuilder htmlStr =new StringBuilder();
        String[] resultArray = html.split(expression);
//        <td>2017-03-31</td><td>19.02</td><td>19.25</td><td>19.02</td><td>19.17</td><td>0.15</td><td>0.79</td><td>339,623</td><td>65,095</td><td>1.21</td><td>0.16</td>
        return resultArray;
    }



    public static String replaceHtml(String oldChar ,String html)   {
        StringBuilder htmlStr =new StringBuilder();
        html=html.replace(oldChar,"");
       // System.out.println(html);
       // System.out.println("--------");
        return html;
    }



    public static String getHtmlByExpression(String expression ,String html)   {

        StringBuilder htmlStr =new StringBuilder();
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(html);
        while(matcher.find()) {
            htmlStr.append(matcher.group());
        }
       // System.out.println(htmlStr.toString());
       // System.out.println("--------");
        return htmlStr.toString();
    }
    public static String getHtmlByURL(String u,String charsetName ) throws Exception {

        StringBuilder htmlStr =new StringBuilder();
        URL url = new URL(u);// 根据链接（字符串格式），生成一个URL对象

        HttpURLConnection urlConnection = (HttpURLConnection) url
                .openConnection();// 打开URL

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream(), charsetName));// 得到输入流，即获得了网页的内容
        String line; // 读取输入流的数据，并显示
        while ((line = reader.readLine()) != null) {
            htmlStr.append(line);
        }
       // System.out.println(htmlStr.toString());
       // System.out.println("--------");
        return  htmlStr.toString();
    }
}
