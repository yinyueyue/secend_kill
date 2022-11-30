package com.yue.dmall.job;

import cn.hutool.crypto.digest.MD5;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.zip.GZIPInputStream;
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
 * @date:created in 2022/5/7 14:03
 * @modified by:
 */
@Component
@RequiredArgsConstructor
public class GuiLvJob {

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  private final static Map<String, Object> params = new HashMap<>();

  static {
    params.put("uid", 20568829);
    params.put("token", "Z8q3mirI_MBlYSFjvvF0Sg==");
    params.put("branchNo", 0);
    params.put("fundAccount", 500085314);
    params.put("appkey", "gl_rn_9bec6a1f78be");
    params.put("signcode", "b5a770fafbc5a832f91432356e68acf8");
    params.put("deviceId", "3d2a443b-9846-3e88-9f0e-b27424c419bc");
    params.put("encrypt", "1");
    params.put("client", "android");
    params.put("version", "2.4.0");
    params.put("pageCount", 30);
    params.put("currentPage", 1);
  }

  //@Scheduled(fixedRate = 10000)
  public void rob() throws IOException, InterruptedException {
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.add("user-agent", "okhttp/3.12.1");
    // requestHeaders.add(HttpHeaders.HOST, "guilvp.gzlex.com");
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    requestHeaders.add(HttpHeaders.ACCEPT_ENCODING, "gzip");

    String url =
        "https://guilvp.gzlex.com/api/product/list?uid={uid}&token={token}&branchNo={branchNo}&fundAccount={fundAccount}&version={version}"
            + "&client={client}&deviceId={deviceId}&encrypt=1&brand={brand}&pageCount=30&currentPage=1"
            + "&appkey={appkey}&signcode={signcode}&timestamp={timestamp}";
    // params.put("signcode", "gl_rn_9bec6a1f78be&signcode=81afa8696d7cfdbfa38804217c315b79 h2");

    params.put("brand", 188);// 188(茶叶),207(粮油)

    params.put("timestamp", String.valueOf(System.currentTimeMillis()));
    String sign = sign(params);
   // params.put("signcode",sign);

    HttpEntity<Object> httpEntity = new HttpEntity<>(null, requestHeaders);

    ParameterizedTypeReference<byte[]> resultTypeReference = new ParameterizedTypeReference<>() {
    };
      ResponseEntity<Object> entity = restTemplate.getForEntity(url, Object.class, params);

    ResponseEntity<byte[]> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, resultTypeReference, params);

    // 获取服务器响应体编码
    String contentEncoding = responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING);

    // gzip解压服务器的响应体
    String responseStr = unGZip(responseEntity.getBody());

    System.out.println(responseStr);
    GuiLvResDTO guiLvResDTO = objectMapper.readValue(responseStr, GuiLvResDTO.class);

    System.out.println(guiLvResDTO);

  }

  //@Scheduled(fixedRate = 10000)
  public void purchase() throws IOException, InterruptedException {
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.add("user-agent", "okhttp/3.12.1");
    requestHeaders.add(HttpHeaders.HOST, "guilvp.gzlex.com");
    // requestHeaders.add(HttpHeaders.ACCEPT, "application/json,text/plain,*/*");
    requestHeaders.add(HttpHeaders.ACCEPT_ENCODING, "gzip");
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    String url =
        "https://guilvp.gzlex.com/api/transaction/purchase/checkRestrictedArea?uid={uid}&token={token}&branchNo={branchNo}"
            + "&fundAccount={fundAccount}&version={version}&client={client}&deviceId={deviceId}&encrypt={encrypt}"
            + "&appkey={appkey}&signcode={signcode}&timestamp={timestamp}&orderType={orderType}&wineType=0&addressId={addressId}&erpCode={erpCode}";

    params.put("orderType", 11);
    params.put("wineType", 0);
    params.put("addressId", 753687);
    params.put("erpCode", "697473123001");

    //params.put("timestamp", "1652164653465");

    HttpEntity<Object> httpEntity = new HttpEntity<>(null, requestHeaders);

    ResponseEntity<byte[]> responseEntity = restTemplate
        .postForEntity(url, httpEntity, byte[].class, params);

    // 获取服务器响应体编码
    String contentEncoding = responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_ENCODING);

    // gzip解压服务器的响应体
    String responseStr = unGZip(responseEntity.getBody());

    System.out.println(responseStr);
    GuiLvResDTO guiLvResDTO = objectMapper.readValue(responseStr, GuiLvResDTO.class);

    System.out.println(guiLvResDTO);

  }

  public static String unGZip(byte[] bytes) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try (InputStream inputStream = new ByteArrayInputStream(
        bytes); GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream)) {
      byte[] buf = new byte[4096];
      int len = -1;
      while ((len = gzipInputStream.read(buf, 0, buf.length)) != -1) {
        byteArrayOutputStream.write(buf, 0, len);
      }
      return byteArrayOutputStream.toString(StandardCharsets.UTF_8);
    } finally {
      byteArrayOutputStream.close();
    }
  }


  public static void main(String[] args) {
    Map<String  , Object> params1 = new HashMap<>();
    params1.put("uid", 20568829);
    params1.put("token", "Z8q3mirI_MBlYSFjvvF0Sg==");
    params1.put("branchNo", 0);
    params1.put("fundAccount", 500085314);
    params1.put("version", "2.4.0");
    params1.put("client", "android");
    params1.put("deviceId", "3d2a443b-9846-3e88-9f0e-b27424c419bc");
    params1.put("timestamp", "1652231068591");
    params1.put("encrypt", "1");
    // 1881(茶叶),207(粮油)
    params1.put("brand", 188);
    params1.put("pageCount", 30);
    params1.put("currentPage", 1);
    params1.put("appkey", "gl_rn_9bec6a1f78be");
    // https://guilvp.gzlex.com/api/product/list?uid=20568829&token=Z8q3mirI_MBlYSFjvvF0Sg%3D%3D&branchNo=0&fundAccount=500085314
    // &version=2.4.0&client=android&deviceId=3d2a443b-9846-3e88-9f0e-b27424c419bc&timestamp=1652231068591
    // &encrypt=1&brand=188&pageCount=30&currentPage=1&appkey=gl_rn_9bec6a1f78be&signcode=b5a770fafbc5a832f91432356e68acf8
    System.out.println(sign(params1));
    System.out.println(sign2(params1));
    System.out.println(sign3(params1));
    System.out.println(sign4(params1));
    System.out.println(sign5(params1));
    System.out.println(sign6(params1));


  }

  public static String sign(Map<String, Object> params) {
    List<String> keyList = new ArrayList<>(params.keySet());
    Collections.sort(keyList);
    StringBuilder stringBuilder = new StringBuilder();
    for (String key : keyList) {
      stringBuilder.append(",").append(key).append("=")
          .append(params.get(key));
    }// 09509490338641982bd2aca5f3cc2bcf
   // String toSignStr = stringBuilder.toString();
    String toSignStr = stringBuilder.substring(1);
    return MD5.create().digestHex(toSignStr);
  }
  public static String sign2(Map<String, Object> params) {
    List<String> keyList = new ArrayList<>(params.keySet());
    Collections.sort(keyList);
    StringBuilder stringBuilder = new StringBuilder();
    for (String key : keyList) {
      stringBuilder.append(",").append(key).append("=")
          .append(params.get(key.toLowerCase(Locale.ROOT)));
    }// 09509490338641982bd2aca5f3cc2bcf
    // String toSignStr = stringBuilder.toString();
    String toSignStr = stringBuilder.substring(1);
    return MD5.create().digestHex(toSignStr);
  }
  public static String sign3(Map<String, Object> params) {
    List<String> keyList = new ArrayList<>(params.keySet());
    Collections.sort(keyList);
    StringBuilder stringBuilder = new StringBuilder();
    for (String key : keyList) {
      stringBuilder.append(",").append(key).append("=")
          .append(params.get(key.toUpperCase(Locale.ROOT)));
    }// 09509490338641982bd2aca5f3cc2bcf
    // String toSignStr = stringBuilder.toString();
    String toSignStr = stringBuilder.substring(1);
    return MD5.create().digestHex(toSignStr);
  }
  public static String sign4(Map<String, Object> params) {
    List<String> keyList = new ArrayList<>(params.keySet());
    Collections.sort(keyList);
    StringBuilder stringBuilder = new StringBuilder();
    for (String key : keyList) {
      stringBuilder.append(",").append(key).append("=")
          .append(params.get(key));
    }// 09509490338641982bd2aca5f3cc2bcf
    // String toSignStr = stringBuilder.toString();
    String toSignStr = stringBuilder.substring(1);
    return MD5.create().digestHex(toSignStr.toLowerCase(Locale.ROOT));
  }
  public static String sign5(Map<String, Object> params) {
    List<String> keyList = new ArrayList<>(params.keySet());
    Collections.sort(keyList);
    StringBuilder stringBuilder = new StringBuilder();
    for (String key : keyList) {
      stringBuilder.append(",").append(key).append("=")
          .append(params.get(key));
    }// 09509490338641982bd2aca5f3cc2bcf
    // String toSignStr = stringBuilder.toString();
    String toSignStr = stringBuilder.substring(1);
    return MD5.create().digestHex(toSignStr.toUpperCase(Locale.ROOT));
  }

  public static String sign6(Map<String, Object> params) {
    List<String> keyList = new ArrayList<>(params.keySet());
    Collections.sort(keyList);
    //  Collections.reverse(keyList);
    StringBuilder stringBuilder = new StringBuilder();
    for (String key : keyList) {
      stringBuilder
          .append(params.get(key));
    }// 09509490338641982bd2aca5f3cc2bcf
    // String toSignStr = stringBuilder.toString();
    String toSignStr = stringBuilder.toString();
    return MD5.create().digestHex(toSignStr);
  }
  @Data
  public static class GuiLvResDTO {

    private String traceId;
    private Message message;
    private LinkedHashMap<String, Object> data;
  }

  @Data
  public static class Message {

    private Integer code;
  }


}
