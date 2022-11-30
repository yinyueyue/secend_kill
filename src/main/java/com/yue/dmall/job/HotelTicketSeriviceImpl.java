package com.yue.dmall.job;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ImmutableMap;
import java.sql.Struct;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author: Yueyue.Yin
 * @description:
 * @date:created in 2022/8/3 14:46
 * @modified by:
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class HotelTicketSeriviceImpl implements HotelTicketSerivice {

  private final RestTemplate restTemplate;

 // @Async
  @Override
  public void robTicket(String submitUrl, String queryUrl, String mobile,
      Set<String> successMobiles) {
    log.debug(Thread.currentThread().getName());

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.add("Refer", "https://ticket.gzhotelgroup.com/ticket-bk.html");
    requestHeaders.add("Origin", "https://ticket.gzhotelgroup.com");
    requestHeaders.add("User-Agent",
        "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.5060.134 Mobile Safari/537.36");
    HttpEntity<Object> httpEntity = new HttpEntity<>(null, requestHeaders);
    ParameterizedTypeReference<LinkedHashMap<String, Object>> resultTypeReference = new ParameterizedTypeReference<>() {
    };
    try {
      requestHeaders.setContentType(MediaType.TEXT_HTML);

      ParameterizedTypeReference<String> resultTypeReferenceHtml = new ParameterizedTypeReference<>() {
      };
      ResponseEntity<String> responseEntity = restTemplate
          .exchange(submitUrl, HttpMethod.POST, httpEntity, resultTypeReferenceHtml,
              ImmutableMap.of("mobile", mobile));

      String objectHtml = responseEntity.getBody();
      int start = objectHtml.indexOf("var");
      int end = objectHtml.indexOf("};",start);
      String respStr = objectHtml.substring(start, end);
  //    respStr.indexOf("token: '")+8;

      int tokenStart = respStr.indexOf("token: '") + 8;
      String token = respStr.substring(tokenStart, 36+tokenStart);

      int referStart = respStr.indexOf("refer: '") + 8;
      String refer = respStr.substring(referStart, 28+referStart);

      requestHeaders.setContentType(MediaType.APPLICATION_JSON);
      Object code = 0;
      if (Objects.equals(code, 0)) {


        //查询状态
        ResponseEntity<LinkedHashMap<String, Object>> queryRespEntity = restTemplate
            .exchange(queryUrl, HttpMethod.POST, httpEntity, resultTypeReference,
                ImmutableMap.of("mobile", mobile));

        Map<String, Object> queryResp = queryRespEntity.getBody();
        Object status = queryResp.get("data");
        if (Objects.equals(status, 1)) {
          log.info("{}：抢券成功！！！！", mobile);
          successMobiles.add(mobile);
        }

      }
    } catch (Exception e) {
      log.error("执行出错:", e);
    }

  }
//jsonp_007230819870252381({"result":{"csessionid":"01tPQXdZjh6BXNdhvaPpfpDcTWocGK3tjK9nMRLJAXxpezQboFHTR4ULq4G8yxPHz9X0KNBwm7Lovlpxjd_P_q4JsKWYrT3W_NKPr8w6oU7K_2ULlmb7gz1CLdF0Xs6_FjPMqDvQo0pEVbhSjSW3HVJmBkFo3NEHBv0PZUm6pbxQU","code":0,"value":"05KHPFxXbRWE4JT6ypJA_3UAwFu3qb5k-YlxCQG1TEyxu1y_jYDVRR_VaHTGa79l-1eZBLCgGbERqB8v4y_hEHnREG-ArDrtDCOlh0C28soogUT2775tY7ExjUyXAK6HCjNk2KJMSBaMfKGl9wISYAtgrkRqJ8OULTM7PfgDRS41H9JS7q8ZD7Xtz2Ly-b0kmuyAKRFSVJkkdwVUnyHAIJzeNbsE3O0i9LL7Z-cUH-oyhqCWMd5IdG_zjYDMG78waBOF33MttdlYrQH7V14NMYIe3h9VXwMyh6PgyDIVSG1W_x9ckmZHYhzEpMH7CRNidIdE0UI5VVl-pTG1AR4XnM0MRvD44VY1aBoVm7o-LRSo9Xn3PwWhk0ny154MarZRLFmWspDxyAEEo4kbsryBKb9Q"},"success":true});

  public static void main(String[] args) {

    String s = "var requestInfo = {\n"
        + "\n"
        + "        type: 'POST', // 'GET' å\u0092\u008C 'POST'\n"
        + "        url: 'https://ticket.gzhotelgroup.com/api/Ticket/took?m=18602319333', // 'https://www.taobao.com/detail'\n"
        + "        args: 'm=18602319333',\n"
        + "        data: '', //a=1&b=2&c=3...\n"
        + "        token: 'f26f6c70-5a9e-48b4-afa7-9c8a16eafe33',\n"
        + "        refer: 'gZF/6LSqGOMyhd+g4B8cN/TyIlY=',\n"
        + "        headers: {\"Content-Type\":\"text/html\"},";
    int tokenStart = s.indexOf("token: '") + 8;
    String substring = s.substring(tokenStart, 36+tokenStart);
    System.out.println(substring);

    int referStart = s.indexOf("refer: '") + 8;
    String substring2 = s.substring(referStart, 28+referStart);
    System.out.println(substring2);

  }
}
