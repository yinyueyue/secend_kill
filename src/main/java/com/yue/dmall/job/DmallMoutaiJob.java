package com.yue.dmall.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author: Yueyue.Yin
 * @description:
 * @date:created in 2022/4/26 10:42
 * @modified by:
 */
@RequiredArgsConstructor
@Component
public class DmallMoutaiJob {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  //@Scheduled(fixedDelay = 3000)
  //@Scheduled(cron = "0/1 0 10 * * ? ")
  public void robMoutai2() throws JsonProcessingException {

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    requestHeaders.add("Refer", "https://static.dmall.com/");
    requestHeaders.add("Origin", "https://static.dmall.com");
    requestHeaders.add("User-Agent",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 15_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148Dmall/5.4.0");
    requestHeaders.add(HttpHeaders.COOKIE,
        "addr=%E9%87%8D%E5%BA%86%E5%B8%82%E6%B8%9D%E5%8C%97%E5%8C%BA%E4%B8%A4%E6%B1%9F%E6%95%B0%E5%AD%97%E7%BB%8F%E6%B5%8E%E4%BA%A7%E4%B8%9A%E5%9B%AD; appMode=online; appVersion=5.4.0; areaId=500112; businessCode=1; community=%E4%B8%A4%E6%B1%9F%E6%95%B0%E5%AD%97%E7%BB%8F%E6%B5%8E%E4%BA%A7%E4%B8%9A%E5%9B%AD; dmTenantId=1; env=app; first_session_time=26170; lat=29.623181; lng=106.491584; originBusinessFormat=1-2-4-8; platform=IOS; platformStoreGroup=; platformStoreGroupKey=46f15935475a8b5ed9c17e8d9735c63d@MTI2Mi02OTM3Mg; recommend=1; risk=0; session_count=698; session_id=91A4267C9B0042CFB5E7DFB905C69C1A; storeGroupKey=e3e20be4b9a70dda5d0cb600a008ba82@MS0xNDI1Mi02OQ; storeId=14252; store_id=14252; tdc=26.17.0.104-3110907-3086141.1651198040000; ticketName=56A48AB637137C0ED62AFF681F45F87BF544A4DF6A46BEA5293626D51F07163A180AC08FA323404A28769032E8AF4D090BA9163560221D7B4A6302748C5C6CCEF0E5674E4168F96286BCA4A416407353CD626455DCC68464D4AC171A5DC206DDDD050557EBCCDDE829BA62682D31DC1554FF5475FD2D40D3104FB9988C49C6E5; token=d85cdcf0-e36a-481b-a872-fc7dae8418dd; userId=143055415; uuid=159b63fd0669585ac1a1d1fce6eb00e2e0493978; venderId=69; vender_id=69; webViewType=wkwebview; track_id=C9CCC2BF4B60000280B1345061857F60; web_session_count=119; console_mode=0; inited=true; apiVersion=5.4.0; appName=com.dmall.dmall; channelId=APPSTORE; cid=1517bfd3f76912509fd; currentTime=1650765520905; deliveryLat=29.622360; deliveryLng=106.491271; dev_type=iPhone; device=iPhone11%2C2; isOpenNotification=0; networkType=2; screen=812*375; sessionId=00568525DEF641D8A9DC751E1D9B9FE5; smartLoading=1; sysVersion=15.4.1; tpc=a_11429; updateTime=1650449030893; version=5.4.0; wifiState=1; xyz=ac; _utm_id=242907240; tempid=415d29c2811d795edbee31d694096828");
    HttpEntity<Object> httpEntity = new HttpEntity<>(null, requestHeaders);

    String url = "https://presale.dmall.com/maotai/qualification?param={param}&token={token}&ticketName=={ticketName}";
    Map<String, Object> reqQueryMap = new HashMap<>();
    reqQueryMap.put("param",
        "{\"vendorId\":\"69\",\"deviceId\":\"159b63fd0669585ac1a1d1fce6eb00e2e0493978\",\"skuId\":\"1052863601\",\"activityId\":\"70281\"}");
    reqQueryMap.put("token", "d85cdcf0-e36a-481b-a872-fc7dae8418dd");
    reqQueryMap.put("ticketName",
        "56A48AB637137C0ED62AFF681F45F87BF544A4DF6A46BEA5293626D51F07163A180AC08FA323404A28769032E8AF4D090BA9163560221D7B4A6302748C5C6CCEF0E5674E4168F96286BCA4A416407353CD626455DCC68464D4AC171A5DC206DDDD050557EBCCDDE829BA62682D31DC1554FF5475FD2D40D3104FB9988C49C6E5");

    ParameterizedTypeReference<Object> resultTypeReference = new ParameterizedTypeReference<>() {
    };

    ResponseEntity<Object> responseEntity = restTemplate
        .exchange(url, HttpMethod.POST, httpEntity, resultTypeReference,
            reqQueryMap);

    System.out.println(objectMapper.writeValueAsString(responseEntity.getBody()));
    ;
  }

  private final static String BASE_URL = "?param={param}&token={token}&ticketName=={ticketName}";
//  @Scheduled(fixedDelay = 3000)
//  @Scheduled(cron = "0/1 0 10 * * ? ")
  public void robMoutai() throws JsonProcessingException, InterruptedException {
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    requestHeaders.add("Refer", "https://static.dmall.com/");
    requestHeaders.add("Origin", "https://static.dmall.com");
    requestHeaders.add("User-Agent",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 15_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148Dmall/5.4.0");
    requestHeaders.add(HttpHeaders.COOKIE,
        "addr=%E9%87%8D%E5%BA%86%E5%B8%82%E6%B8%9D%E5%8C%97%E5%8C%BA%E4%B8%A4%E6%B1%9F%E6%95%B0%E5%AD%97%E7%BB%8F%E6%B5%8E%E4%BA%A7%E4%B8%9A%E5%9B%AD; appMode=online; appVersion=5.4.0; areaId=500112; businessCode=1; community=%E4%B8%A4%E6%B1%9F%E6%95%B0%E5%AD%97%E7%BB%8F%E6%B5%8E%E4%BA%A7%E4%B8%9A%E5%9B%AD; dmTenantId=1; env=app; first_session_time=26170; lat=29.623181; lng=106.491584; originBusinessFormat=1-2-4-8; platform=IOS; platformStoreGroup=; platformStoreGroupKey=46f15935475a8b5ed9c17e8d9735c63d@MTI2Mi02OTM3Mg; recommend=1; risk=0; session_count=698; session_id=91A4267C9B0042CFB5E7DFB905C69C1A; storeGroupKey=e3e20be4b9a70dda5d0cb600a008ba82@MS0xNDI1Mi02OQ; storeId=14252; store_id=14252; tdc=26.17.0.104-3110907-3086141.1651198040000; ticketName=56A48AB637137C0ED62AFF681F45F87BF544A4DF6A46BEA5293626D51F07163A180AC08FA323404A28769032E8AF4D090BA9163560221D7B4A6302748C5C6CCEF0E5674E4168F96286BCA4A416407353CD626455DCC68464D4AC171A5DC206DDDD050557EBCCDDE829BA62682D31DC1554FF5475FD2D40D3104FB9988C49C6E5; token=d85cdcf0-e36a-481b-a872-fc7dae8418dd; userId=143055415; uuid=159b63fd0669585ac1a1d1fce6eb00e2e0493978; venderId=69; vender_id=69; webViewType=wkwebview; track_id=C9CCC2BF4B60000280B1345061857F60; web_session_count=119; console_mode=0; inited=true; apiVersion=5.4.0; appName=com.dmall.dmall; channelId=APPSTORE; cid=1517bfd3f76912509fd; currentTime=1650765520905; deliveryLat=29.622360; deliveryLng=106.491271; dev_type=iPhone; device=iPhone11%2C2; isOpenNotification=0; networkType=2; screen=812*375; sessionId=00568525DEF641D8A9DC751E1D9B9FE5; smartLoading=1; sysVersion=15.4.1; tpc=a_11429; updateTime=1650449030893; version=5.4.0; wifiState=1; xyz=ac; _utm_id=242907240; tempid=415d29c2811d795edbee31d694096828");
    HttpEntity<Object> httpEntity = new HttpEntity<>(null, requestHeaders);

    Map<String, Object> paramsMap = new HashMap<>();
    paramsMap.put("param",
        "{\"vendorId\":\"69\",\"deviceId\":\"159b63fd0669585ac1a1d1fce6eb00e2e0493978\",\"skuId\":\"1052863601\",\"activityId\":\"70281\"}");
    paramsMap.put("token", "d85cdcf0-e36a-481b-a872-fc7dae8418dd");
    paramsMap.put("ticketName",
        "56A48AB637137C0ED62AFF681F45F87BF544A4DF6A46BEA5293626D51F07163A180AC08FA323404A28769032E8AF4D090BA9163560221D7B4A6302748C5C6CCEF0E5674E4168F96286BCA4A416407353CD626455DCC68464D4AC171A5DC206DDDD050557EBCCDDE829BA62682D31DC1554FF5475FD2D40D3104FB9988C49C6E5");

    long start = System.currentTimeMillis();
    Boolean jumpQueueFlag = jumpQueue(httpEntity, paramsMap);
    System.out.println("jumpQueueFlag:" + jumpQueueFlag);
    Thread.sleep(1995);
    Boolean queryQueueFlag = queryQueue(httpEntity, paramsMap);
    System.out.println("queryQueueFlag:" + queryQueueFlag);

    System.out.println("cost:" + (System.currentTimeMillis() - start));
  }

  public Boolean jumpQueue(HttpEntity<Object> httpEntity, Map<String, Object> paramsMap)
      throws JsonProcessingException {

    ParameterizedTypeReference<CommonRespDTO> resultTypeReference = new ParameterizedTypeReference<>() {
    };
    String url = "https://presale.dmall.com/maotai/jumpQueue" + BASE_URL;
    ResponseEntity<CommonRespDTO> responseEntity = restTemplate
        .exchange(url, HttpMethod.POST, httpEntity, resultTypeReference,
            paramsMap);

    return (Boolean) responseEntity.getBody().getData();
  }

  public Boolean queryQueue(HttpEntity<Object> httpEntity, Map<String, Object> paramsMap)
      throws JsonProcessingException {

    ParameterizedTypeReference<CommonRespDTO> resultTypeReference = new ParameterizedTypeReference<>() {
    };
    String url = "https://presale.dmall.com/maotai/queryQueue" + BASE_URL;
    ResponseEntity<CommonRespDTO> responseEntity = restTemplate
        .exchange(url, HttpMethod.POST, httpEntity, resultTypeReference,
            paramsMap);

    return (Boolean) responseEntity.getBody().getData();
  }

  @Data
  public static class CommonRespDTO {

    private String code;
    private Object data;
    private String msg;
    private Long time;
  }

 // @Scheduled(fixedDelay = 3000)
  public void rob2() throws InterruptedException {
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
    requestHeaders.add("Refer", "https://servicewechat.com/wx184ef444ff7e3dca/3/page-frame.html");
    requestHeaders.add("token", "96KLCo5LA2QRPKD76gSfOJ4l9QD2PD8bq9sHqJKv6KwY3S8UNv");
    requestHeaders.add("User-Agent",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 15_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.20(0x1800142e) NetType/WIFI Language/zh_CN");
    HttpEntity<Object> httpEntity = new HttpEntity<>("{\n"
        + "  \"address_id\" : 884,\n"
        + "  \"goods_id\" : [\n"
        + "    4\n"
        + "  ],\n"
        + "  \"num\" : [\n"
        + "    30\n"
        + "  ],\n"
        + "  \"order_from\" : 1,\n"
        + "  \"draw_id\" : 4,\n"
        + "  \"cart_ids\" : [\n"
        + "\n"
        + "  ]\n"
        + "}", requestHeaders);

    ParameterizedTypeReference<LinkedHashMap<String, Object>> resultTypeReference = new ParameterizedTypeReference<>() {
    };
    String url = "https://spirits.cqgzd.net/api/webapi/user/createOrder";
    while (true) {
      ResponseEntity<LinkedHashMap<String, Object>> responseEntity = restTemplate
          .exchange(url, HttpMethod.POST, httpEntity, resultTypeReference);

      LinkedHashMap<String, Object> body = responseEntity.getBody();
      Integer code = (Integer) body.get("code");
      if (code != 7) {
        break;
      }
      System.out.println(body);
      Thread.sleep(200);
    }
  }


}
