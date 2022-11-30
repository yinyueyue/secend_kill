package com.yue.dmall.job;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author: Yueyue.Yin
 * @description:
 * @date:created in 2022/5/21 9:51
 * @modified by:
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class IMoutaiJob {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;
  final static List<CityInfo> cityDict = new ArrayList<>();

  @AllArgsConstructor
  @Data
  public static class CityInfo {

    private String code;
    private String name;
    private String lng;
    private String lat;
  }

  static {
    cityDict.add(new CityInfo("500105000", "江北区", "106.533034", "29.575962"));
    cityDict.add(new CityInfo("500112000", "渝北区", "106.524717", "29.652278"));
    cityDict.add(new CityInfo("500107000", "九龙坡区", "106.489769", "29.503986"));
    cityDict.add(new CityInfo("500106000", "沙坪坝区", "106.462821", "29.559070"));
    cityDict.add(new CityInfo("500108000", "南岸区", "106.601150", "29.573963"));
    cityDict.add(new CityInfo("500103000", "渝中区", "106.526849", "29.541132"));
    cityDict.add(new CityInfo("500231000", "垫江县", "107.296112", "30.257718"));
    cityDict.add(new CityInfo("500109000", "北碚区", "106.441352", "29.830373"));
    cityDict.add(new CityInfo("500109000", "涪陵区", "107.264955", "29.750280"));
  }

  //@Scheduled(fixedRate = 10000 * 60 * 5)
  //@Scheduled(cron = "0 0/1 * * * ? ")
  public void queryStorage() throws Exception {

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
    requestHeaders.add("Refer", "https://fe.moutai519.com.cn/");
    requestHeaders.add("Origin", "https://fe.moutai519.com.cn");
    requestHeaders.add("User-Agent",
        "Mozilla/5.0 (Linux; Android 9; LLD-AL10 Build/HONORLLD-AL10; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/79.0.3945.116 Mobile Safari/537.36 moutaiapp/1.2.0 device-id/e0958d2e5db71063ac8ac89bf6e99dd3");
    requestHeaders.add(HttpHeaders.COOKIE,
        "MT-Token-Wap=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtdCIsImV4cCI6MTY1NDUyMjcxMCwidXNlcklkIjoxMDczMjU4Mzc1LCJkZXZpY2VJZCI6ImNsaXBzX0xVNThSWGNVY1JVblF5QVRkaEp6RjNVUktCNThUaThmS2s4dFQzb2VMRW89IiwiaWF0IjoxNjUxOTMwNzEwfQ.M0Cbco9ktpKMgmGGIk41aSkBOXQ_RCEayqCqm44HsY8; MT-Device-ID-Wap=clips_LU58RXcUcRUnQyATdhJzF3URKB58Ti8fKk8tT3oeLEo=; YX_SUPPORT_WEBP=1");
    Map<String, Object> paramsMap = new HashMap<>();

    paramsMap.put("hot", true);
    paramsMap.put("itemCode", "10193");

    paramsMap.put("province", "500000000");

    HttpEntity<String> httpEntity = new HttpEntity<>(objectMapper.writeValueAsString(paramsMap),
        requestHeaders);

    String url = "https://h5.moutai519.com.cn/xhr/front/mall/item/purchaseInfo";

    ParameterizedTypeReference<LinkedHashMap<String, Object>> resultTypeReference = new ParameterizedTypeReference<>() {
    };
    cityDict.forEach(cityInfo -> {
      paramsMap.put("city", cityInfo.getCode());
      paramsMap.put("lng", cityInfo.getLng());
      paramsMap.put("lat", cityInfo.getLat());

      long start = System.currentTimeMillis();
      ResponseEntity<LinkedHashMap<String, Object>> responseEntity = restTemplate
          .exchange(url, HttpMethod.POST, httpEntity, resultTypeReference,
              paramsMap);

      try {
        System.out.println(objectMapper.writeValueAsString(responseEntity.getBody()));
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      }
      if (responseEntity.getStatusCode() == HttpStatus.OK) {
        LinkedHashMap<String, Object> resp = responseEntity.getBody();
        LinkedHashMap<String, Object> data = (LinkedHashMap<String, Object>) resp.get("data");
//        LinkedHashMap<String, Object> purchaseInfo = (LinkedHashMap<String, Object>) data
//            .get("purchaseInfo");
        Object shopInfoObj = data.get("shopInfo");

        if (shopInfoObj != null) {
          try {
            System.out.println(LocalDateTime.now() + "有库存啦：" + cityInfo.getName() +objectMapper.writeValueAsString(shopInfoObj)+ ",cost:" + (
                System.currentTimeMillis() - start));
            LinkedHashMap<String, Object> shopInfoMap = (LinkedHashMap<String, Object>) shopInfoObj;
            String shopId = shopInfoMap.get("shopId").toString();
            String shopName = shopInfoMap.get("shopName").toString();
            //Map<String,Object> orderParamMap


          } catch (JsonProcessingException e) {
            e.printStackTrace();
          }
        } else {
          System.out.println(LocalDateTime.now() + "没有库存：" + cityInfo.getName() + ",cost:" + (
              System.currentTimeMillis() - start));
        }
      }
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });


  }

}
