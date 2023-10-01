package com.example.monitor;


import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.AESUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/monitor2")
@Slf4j
public class MainController2 {

    static String cookie = "";

    @GetMapping("/login")
    public byte[] login() throws IOException {
        func1();
        File file = func2();
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes,0, inputStream.available());
        return bytes;
    }

    @GetMapping("/login2/{yzm}")
    public String login2(@PathVariable("yzm") String yzm)
    {
        boolean flag = func3(yzm);
        if(!flag)
        {
            return "登录失败，请重试！";
        }
        func4t9();
        return  func10();
    }

    @GetMapping("/reflush")
    public String reflush()
    {
        return func10();
    }

    void func1()
    {
        HttpResponse response = HttpRequest.
                get("https://zxjk.sthjt.zj.gov.cn/zxjk3/login.jsp").execute();
        cookie = response.getCookieStr();
        log.info("cookie={}",cookie);
    }

    // 获得验证码
    File func2()
    {
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("fw", "getCodeImgk");
        byte[] body  = HttpRequest.get
                        ("https://zxjk.sthjt.zj.gov.cn/zxjk3/login.do").
                form(paramMap).execute().bodyBytes();
        String filePath = "./yzm.jpg";
        File file = FileUtil.writeBytes(body, filePath);
//        log.info("[gitYZM] response.body={}",body);
        return file;
    }

    /*
        登录
     */
    boolean func3(String yzm) {
        try {
            String u = "13335916887";
            String p = "xxxxxx";
            String KeyBase = "qet12a";
            String kt1 = k1();
            String kt2 = k2();
            String key = KeyBase + kt2;
            key+="00";
            String c = kt1 + p;
            String c2 = u;
            String c3= yzm;
            String a = AESUtil.aes256ECBPkcs7PaddingEncrypt(c, key);
            String a2 = AESUtil.aes256ECBPkcs7PaddingEncrypt(c2,key);
            String a3 = AESUtil.aes256ECBPkcs7PaddingEncrypt(c3,key);
            if(a==null||a2==null||a3==null)
            {
                log.info("a空,{},{},{},",a,a2,a3);
                return false;
            }
            log.info("准备发送验证码");
            try {
                // https://zxjk.sthjt.zj.gov.cn/zxjk/login.do
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("text/plain");
                RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("captcha",a3)
                        .addFormDataPart("UserName", a2)
                        .addFormDataPart("PassWord", a)
                        .build();
                Request request = new Request.Builder()
                        .url("https://zxjk.sthjt.zj.gov.cn/zxjk3/login.do?fw=login_f&type=aeslogin")
                        .method("POST", body)
                        .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                        .addHeader("Accept", "*/*")
                        .addHeader("Host", "zxjk.sthjt.zj.gov.cn")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Referer", "https://zxjk.sthjt.zj.gov.cn/zxjk3/login.do?fw=login_f&type=aeslogin")
                        .addHeader("Cookie", cookie)
                        .build();
                Response response = client.newCall(request).execute();
            }catch (IOException e)
            {
                log.info("1112 e{}", e.getMessage());
                return false;
            }
        }catch (Exception e)
        {
            log.info("124124124 e{}", e.getMessage());
            return false;
        }

        return true;
    }

    String k1()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
        return  sdf.format(new Date());
    }
    String k2()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return  sdf.format(new Date());
    }

    void func4t9()
    {
        //4自动跳转

        // 5
        HttpResponse response = HttpRequest.get("https://zxjk.sthjt.zj.gov.cn/zxjk3/nav.do?method=navSystem&system=fq&systemgljb=11")
                .header("Cookie",cookie)
                .execute();
        // 6
        response = HttpRequest.get("https://zxjk.sthjt.zj.gov.cn/zxjk3/zxjk/sta.do?fw=main&type=zdxx").execute();

        //7
        try {
            long time = new Date().getTime();
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("multipart/form-data; boundary=--------------------------750448097941970727313075");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("wrlx","2")
                    .addFormDataPart("gljb","11")
                    .addFormDataPart("checklx","zksy_jxywd")
                    .addFormDataPart("ssdq","")
                    .addFormDataPart("xzqy","")
                    .addFormDataPart("sshy","")
                    .addFormDataPart("ysqk","")
                    .addFormDataPart("station","")
                    .addFormDataPart("pfqx","")
                    .addFormDataPart("zdxz","")
                    .addFormDataPart("ywqy","")
                    .addFormDataPart("tsxx","")
                    .build();
            Request request = new Request.Builder()
                    .url("https://zxjk.sthjt.zj.gov.cn/zxjk3/zxjk/sta.do?fw=getStation&RF="+time)
                    .method("POST", body)
                    .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                    .addHeader("Accept", "*/*")
                    .addHeader("Host", "zxjk.sthjt.zj.gov.cn")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------750448097941970727313075")
                    .addHeader("Cookie", cookie)
                    .build();
            Response response2 = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 8
        response = HttpRequest.get("https://zxjk.sthjt.zj.gov.cn/zxjk3/zxjk/sta.do?fw=station&type=curve&wrlx=2&id=11784&sbid=11784&pkid=3674&pktype=fqpk&qyid=9584&qymc=%25E9%2587%2591%25E5%258D%258E%25E6%259D%25B0%25E9%25BE%2599%25E4%25BF%259D%25E6%25B8%25A9%25E6%259D%2590%25E6%2596%2599%25E6%259C%2589%25E9%2599%2590%25E5%2585%25AC%25E5%258F%25B8&pkmc=%25E6%25A0%2587%25E6%258E%2592%25E5%258F%25A3").
        header("Cookie", cookie).execute();
        long time = new Date().getTime();
        // 9
        response = HttpRequest.get("https://zxjk.sthjt.zj.gov.cn/zxjk3/zxjk/sta.do?fw=queryDate&type=hour&jcyz=%25E7%2583%259F%25E5%25B0%2598%252C%25E7%2583%259F%25E5%25B0%2598%25E6%258A%2598%25E7%25AE%2597%25E6%25B5%2593%25E5%25BA%25A6%252C%25E4%25BA%258C%25E6%25B0%25A7%25E5%258C%2596%25E7%25A1%25AB%252CSO2%25E6%258A%2598%25E7%25AE%2597%25E6%25B5%2593%25E5%25BA%25A6%252C%25E6%25B0%25AE%25E6%25B0%25A7%25E5%258C%2596%25E7%2589%25A9%252CNOX%25E6%258A%2598%25E7%25AE%2597%25E6%25B5%2593%25E5%25BA%25A6%252C%25E6%25B0%25A7%25E6%25B0%2594%25E5%2590%25AB%25E9%2587%258F%252C%25E7%2583%259F%25E6%25B0%2594%25E6%25B8%25A9%25E5%25BA%25A6%252C%25E7%2583%259F%25E6%25B0%2594%25E5%258E%258B%25E5%258A%259B%252C%25E7%2583%259F%25E6%25B0%2594%25E6%25B9%25BF%25E5%25BA%25A6%252C%25E7%2583%259F%25E6%25B0%2594%25E6%25B5%2581%25E9%2580%259F%252C%25E7%2583%259F%25E6%25B0%2594%25E6%25B5%2581%25E9%2587%258F&isotherblocked=false&dw=mg%252Fm3%252Cmg%252Fm3%252Cmg%252Fm3%252Cmg%252Fm3%252Cmg%252Fm3%252Cmg%252Fm3%252C%2525%252C%25E2%2584%2583%252CKPa%252C%2525%252Cm%252Fs%252Cm3%252Fs&bt=2023-09-24,17&et=2023-10-01,17&p=yyyy-MM-dd,HH&width=687&height=267&id=11784&sjzt=&sjlx=sysj&timetype=day&wrwpfbz=0&RF=" + time).header("Cookie",cookie).execute();
    }

    String func10()
    {
        long time = new Date().getTime();
        HttpResponse response = HttpRequest.
                get("https://zxjk.sthjt.zj.gov.cn/zxjk3/zxjk/sta.do?fw=queryDate&type=real&jcyz=SO2%25E6%258A%2598%25E7%25AE%2597%25E6%25B5%2593%25E5%25BA%25A6%252CNOX%25E6%258A%2598%25E7%25AE%2597%25E6%25B5%2593%25E5%25BA%25A6&isotherblocked=false&dw=mg%252Fm3%252Cmg%252Fm3&bt=2023-08-01,17&et=&p=yyyy-MM-dd,HH&width=687&height=267&id=11784&sjzt=&sjlx=yssj&timetype=&wrwpfbz=0&RF=" + time).
                header("Cookie",cookie).
                execute();
        String str = response.body();
        log.info(str);
        StringBuffer stringBuffer = new StringBuffer();
        Pattern pa = Pattern.compile("&gt;((\\d+\\.\\d+)|(\\d+)|(\\d+-\\d+-\\d+,\\d+:\\d+:\\d+))&lt");
        List<String> list = new ArrayList<>();
        Matcher ma = pa.matcher(str);
        while(ma.find())
        {
            list.add(ma.group(1));
        }
        if(!(list.size()%4==0) || list.isEmpty())
        {
            return "数据有误 重新登录";
        }
        String tmp = "";
        double so = 0, no = 0;
        int cnt = 0;
        for(int i=1;i< list.size();i+=4)
        {
            if(!tmp.equals(list.get(i).substring(0,13)))
            {
                if(cnt!=0)
                {
                    so/=cnt;
                    no/=cnt;
                    stringBuffer.append(tmp);
                    stringBuffer.append(",共").append(cnt).append("条记录").append("二氧化硫平均值=").append(String.format("%.2f",so))
                            .append("氢氧化物平均值=").append(String.format("%.2f",no)).append("\r\n");
                }
                so = no = cnt = 0;
                tmp = list.get(i).substring(0,13);
            }
            cnt++;
            so += Double.parseDouble(list.get(i+1));
            no += Double.parseDouble(list.get(i+2));
        }
        if(cnt!=0){
            so/=cnt;
            no/=cnt;
            stringBuffer.append(tmp);
            stringBuffer.append(",共").append(cnt).append("条记录").append("二氧化硫平均值=").append(String.format("%.2f",so))
                    .append("氢氧化物平均值=").append(String.format("%.2f",no)).append("\r\n");
        }
        return stringBuffer.toString();
    }
}
