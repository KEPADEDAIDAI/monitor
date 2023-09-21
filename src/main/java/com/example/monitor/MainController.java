package com.example.monitor;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("/monitor")
@Slf4j
public class MainController {

    static String cookie = "";

    @GetMapping("/login")
    public byte[] login() throws IOException {
        openPage();
        File file = gitYZM();
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes,0, inputStream.available());
        return bytes;
    }

    @GetMapping("/login2/{yzm}")
    public String login2(@PathVariable("yzm") String yzm)
    {
        pushYZM(yzm);
        return step1();
    }

    /**
     * 打开页面
     * https://zxjk.sthjt.zj.gov.cn/zxjk/login.jsp
     */
    void openPage()
    {
        //https://zxjk.sthjt.zj.gov.cn/zxjk/login.jsp
        HttpResponse response = HttpRequest.get
                        ("https://zxjk.sthjt.zj.gov.cn/zxjk/login.jsp").
                execute();
//        String body = response.body(); // 显示的网页
//        log.info("[openPage] response.body={}",body);
        String head = response.header("Set-Cookie");
        log.info("[openPage] response.header.Set-Cookie={}",head);

        int status = response.getStatus(); // 状态码
        log.info("[openPage] response.status={}",status);
        cookie = response.getCookieStr();

    }

    /**
     * 获得验证码
     * https://zxjk.sthjt.zj.gov.cn/zxjk/login.do
     * Params
     * fw=getCodeImgGuest
     */
    File gitYZM()
    {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("fw","getCodeImgGuest");
        byte[] body  = HttpRequest.get
                        ("https://zxjk.sthjt.zj.gov.cn/zxjk/login.do").
                form(paramMap).execute().bodyBytes();
        String filePath = "./yzm.jpg";
        File file = FileUtil.writeBytes(body, filePath);
//        log.info("[gitYZM] response.body={}",body);
        return file;

    }


    void pushYZM(String yzm)
    {
        try {
            // https://zxjk.sthjt.zj.gov.cn/zxjk/login.do
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("text/plain");
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("captcha2",yzm)
                    .build();
            Request request = new Request.Builder()
                    .url("https://zxjk.sthjt.zj.gov.cn/zxjk/login.do?fw=login_g_s")
                    .method("POST", body)
                    .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                    .addHeader("Accept", "*/*")
                    .addHeader("Host", "zxjk.sthjt.zj.gov.cn")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Referer", "https://zxjk.sthjt.zj.gov.cn/zxjk/login.do?fw=login_g_s")
                    .addHeader("Cookie", cookie)
                    .build();
            Response response = client.newCall(request).execute();
            log.info("1111" + response.toString());
        }catch (IOException ignored)
        {

        }

    }

    String step1()
    {
        HttpResponse response = HttpRequest.
                get("https://zxjk.sthjt.zj.gov.cn/zxjk/nav.do?method=navSystem2&system=fq&systemgljb=11")
                .header("Cookie",cookie)
                .execute();
        log.info("2222" + response.body());
        response = HttpRequest.
                get("https://zxjk.sthjt.zj.gov.cn/zxjk/zxjk/sta.do?fw=main&type=zdxx")
                .header("Cookie",cookie).execute();

        log.info("3333" + response.body());
        long time = new Date().getTime();
        try{
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
                    .url("https://zxjk.sthjt.zj.gov.cn/zxjk/zxjk/sta.do?fw=getStation&RF="+time)
                    .method("POST", body)
                    .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                    .addHeader("Accept", "*/*")
                    .addHeader("Host", "zxjk.sthjt.zj.gov.cn")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Content-Type", "multipart/form-data; boundary=--------------------------750448097941970727313075")
                    .addHeader("Cookie", cookie)
                    .build();
            Response response2 = client.newCall(request).execute();
            log.info("4444444{}",response2.toString());
        }
        catch (IOException ignored){}






//        HashMap<String,Object> map = new HashMap<>();
//        map.put("wrlx","2");
//        map.put("glib","11");
//        map.put("checklx", "zksy_jxywd");
//        map.put("ssdq","");
//        map.put("xzqy","");
//        map.put("sshy","");
//        map.put("ysqk","");
//        map.put("station","");
//        map.put("pfqx","");
//        map.put("zdxz","");
//        map.put("ywqy","");
//        map.put("tsxx","");
//        response = HttpRequest.post("https://zxjk.sthjt.zj.gov.cn/zxjk/zxjk/sta.do?fw=getStation&RF=" + time).
//                form(map).
//                execute();
//
//        log.info("4444" + response.body());
        response = HttpRequest.
                get("https://zxjk.sthjt.zj.gov.cn/zxjk/zxjk/sta.do?fw=station&type=curve&wrlx=2&id=3877&sbid=3877&pkid=1004&pktype=fqpk&qyid=3382&qymc=%25E6%259D%25AD%25E5%25B7%259E%25E5%258D%258E%25E7%2594%25B5%25E5%258D%258A%25E5%25B1%25B1%25E5%258F%2591%25E7%2594%25B5%25E6%259C%2589%25E9%2599%2590%25E5%2585%25AC%25E5%258F%25B8&pkmc=1%2523%25E6%25A0%2587%25E6%258E%2592%25E5%258F%25A3").header("Cookie",cookie).execute();
        time = new Date().getTime();

        response = HttpRequest.
                get("https://zxjk.sthjt.zj.gov.cn/zxjk/zxjk/sta.do?fw=queryDate&type=hour&j" +
                        "cyz=%25E7%2583%259F%25E5%25B0%2598%252C%25E7%2583%259F%25E5%25B0%2598" +
                        "%25E6%258A%2598%25E7%25AE%2597%25E6%25B5%2593%25E5%25BA%25A6%252C%25E4%" +
                        "25BA%258C%25E6%25B0%25A7%25E5%258C%2596%25E7%25A1%25AB%252CSO2%25E6%258A%25" +
                        "98%25E7%25AE%2597%25E6%25B5%2593%25E5%25BA%25A6%252C%25E6%25B0%25AE%25E6%25B0%2" +
                        "5A7%25E5%258C%2596%25E7%2589%25A9%252CNOX%25E6%258A%2598%25E7%25AE%2597%25E6%25B5%" +
                        "2593%25E5%25BA%25A6%252C%25E6%25B0%25A7%25E6%25B0%2594%25E5%2590%25AB%25E9%2587%258F%252C%25E7%2583%259F%25E6%25B0%2594%25E6%25B8%25A9%25E5%25BA%25A6%252C%25E7%2583%259F%25E6%25B0%2594%25E5%258E%258B%25E5%258A%259B%252C%25E7%2583%259F%25E6%25B0%2594%25E6%25B9%25BF%25E5%25BA%25A6%252C%25E7%2583%259F%25E6%25B0%2594%25E6%25B5%2581%25E9%2580%259F%252C%25E7%2583%259F%25E6%25B0%2594%25E6%25B5%2581%25E9%2587%258F&isotherblocked=false&dw=mg%252Fm3%252Cmg%252Fm3%252Cmg%252Fm3%252Cmg%252Fm3%252Cmg%252Fm3%252Cmg%252Fm3%252C%2525%252C%25E2%2584%2583%252CKPa%252C%2525%252Cm%252Fs%252Cm3%252Fs&bt=2023-09-03,23&et=2023-09-10,23&p=yyyy-MM-dd,HH&width=948&height=369&id=3877&sjzt=&sjlx=sysj&timetype=day&wrwpfbz=0&RF="+time)
                .header("Cookie",cookie).execute();

        log.info("5555" + response.body());
        return response.body();

    }
}
