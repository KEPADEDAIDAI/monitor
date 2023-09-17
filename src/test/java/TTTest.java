import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.example.monitor.MonitorApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
}
