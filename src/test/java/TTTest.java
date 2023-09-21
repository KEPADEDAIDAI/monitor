import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.monitor.MonitorApplication;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.CookieManager;
import java.util.HashMap;

@SpringBootTest(classes = MonitorApplication.class)
@Slf4j
public class TTTest {

    @Test
    void runnnning()
    {
        openPage();
        gitYZM();
    }

    /**
     * 打开页面
     * https://zxjk.sthjt.zj.gov.cn/zxjk/login.jsp
     */
    @Test
    void openPage()
    {
        //https://zxjk.sthjt.zj.gov.cn/zxjk/login.jsp
        HttpResponse response = HttpRequest.get
                ("https://zxjk.sthjt.zj.gov.cn/zxjk/login.jsp").
                execute();
        String body = response.body(); // 显示的网页
//        log.info("[openPage] response.body={}",body);
        String head = response.header("Set-Cookie");
        log.info("[openPage] response.header.Set-Cookie={}",head);

        int status = response.getStatus(); // 状态码
        log.info("[openPage] response.status={}",status);


    }

    /**
     * 获得验证码
     * https://zxjk.sthjt.zj.gov.cn/zxjk/login.do
     * Params
     * fw=getCodeImgGuest
     */
    @Test
    void gitYZM()
    {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("fw","getCodeImgGuest");
        HttpResponse response = HttpRequest.get
                ("https://zxjk.sthjt.zj.gov.cn/zxjk/login.do").
                form(paramMap).
                execute();
        String body = response.body(); // 显示的网页
        log.info("[gitYZM] response.body={}",body);
        int status = response.getStatus(); // 状态码
        log.info("[gitYZM] response.status={}",status);
        try {
            String head = response.header("Set-Cookie");
            log.info("[gitYZM] response.header.Set-Cookie={}",head);
        }catch (Exception ignored) {
            log.warn("error");
        }

    }

    @Test
    void pushYZM()
    {
//        String yzm = "1111";
        // https://zxjk.sthjt.zj.gov.cn/zxjk/login.do
//        HashMap<String, Object> paramMap = new HashMap<>();
//        paramMap.put("captcha2", yzm);
//        HttpResponse response = null;
        String yzm = "{\"captcha2\":\"1111\"}";
        String yzm2 = "captcha2=1111";
        JSONObject param = JSONUtil.createObj();
        param.putOnce("captcha2", yzm);
        HttpResponse res = HttpRequest.
                post("https://zxjk.sthjt.zj.gov.cn/zxjk/login.do?fw=login_g_s")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(yzm2).
                execute();

//        String res = HttpUtil.post("https://zxjk.sthjt.zj.gov.cn/zxjk/login.do?fw=login_g_s",param.toString());
        log.info("111{}111",res.body());
//        log.info(response.header("Date"));
    }

    @Test
    void testOkHttp() throws IOException {
        CookieManager cookieManager = HttpRequest.getCookieManager();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("captcha2","mxp7")
                .build();
        Request request = new Request.Builder()
                .url("https://zxjk.sthjt.zj.gov.cn/zxjk/login.do?fw=login_g_s")
                .method("POST", body)
                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .addHeader("Accept", "*/*")
                .addHeader("Host", "zxjk.sthjt.zj.gov.cn")
                .addHeader("Connection", "keep-alive")
                .addHeader("Referer", "https://zxjk.sthjt.zj.gov.cn/zxjk/login.do?fw=login_g_s")
                .addHeader("Cookie", "JSESSIONID=16E59254A617C3A9A9EB071DE0A179B2")
                .build();
        Response response = client.newCall(request).execute();
        log.info(response.toString());
    }


}
