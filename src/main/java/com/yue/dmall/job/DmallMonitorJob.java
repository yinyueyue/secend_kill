package com.yue.dmall.job;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.net.URLDecoder;
import com.yue.dmall.dto.DmailResDTO;
import com.yue.dmall.dto.DmailResDTO.ItemDetailDTO;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author: Yueyue.Yin
 * @description:
 * @date:created in 2022/9/2 15:26
 * @modified by:
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DmallMonitorJob {

  private final RestTemplate restTemplate;

  // "920844682@qq.com", "1026342113@qq.com", "1263203215@qq.com"
  @Scheduled(cron = "2 0 0,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23 * * ? ")
 // @Scheduled(fixedRate = 10000)
  public void checkStorage() throws Exception {
    //  sendEmail("多点茅台库存更新", "ceshi", toEmails, null, false);

    InputStream in = new BufferedInputStream(new FileInputStream("D://moutai/config.properties"));

    Properties properties = new Properties();
    properties.load(in);
    String[] toEmails = properties.get("emails").toString().split(",");

    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    requestHeaders.add("Refer", "https://static.dmall.com/");
    requestHeaders.add("Origin", "https://static.dmall.com");
    requestHeaders.add("User-Agent",
        "Mozilla/5.0 (iPhone; CPU iPhone OS 15_4_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148Dmall/5.4.0");
    requestHeaders.add(HttpHeaders.COOKIE, properties.get("cookie").toString());
    HttpEntity<Object> httpEntity = new HttpEntity<>(null, requestHeaders);

    String url = "https://presale.dmall.com/maotai/seriesWareList?param={param}&token={token}&ticketName=={ticketName}";
    Map<String, Object> reqQueryMap = new HashMap<>();
    reqQueryMap.put("param", "{\"vendorId\":\"69\"}");
    reqQueryMap.put("token", properties.get("token").toString());
    reqQueryMap.put("ticketName", properties.get("ticketName").toString());

    ParameterizedTypeReference<DmailResDTO> resultTypeReference = new ParameterizedTypeReference<>() {
    };


    for (int i = 0; i < 2; i++) {
      ResponseEntity<DmailResDTO> responseEntity = restTemplate
          .exchange(url, HttpMethod.POST, httpEntity, resultTypeReference, reqQueryMap);
      log.info("库存查询返回：{}", responseEntity);

      if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
        DmailResDTO resDTO = responseEntity.getBody();
        if("991106".equals(resDTO.getCode())){
          sendEmail("多点登录超时", "多点登录超时啦", new String[]{"920844682@qq.com"}, null, false);
          System.exit(0);
          return;
        }

        if ("0000".equals(resDTO.getCode())) {
          StringBuilder content = new StringBuilder();
          List<ItemDetailDTO> wareInfos = resDTO.getData().getWareInfos();
          String sendModel = properties.get("sendModel").toString();
          if (CollectionUtil.isNotEmpty(wareInfos)) {
            if ("storage".equals(sendModel)) {
              for (ItemDetailDTO wareInfo : wareInfos) {
                if (wareInfo.getStock() > 0) {
                  content.append(wareInfo.getName()).append("有库存了，当前库存：").append(wareInfo.getStock())
                      .append(",价格：")
                      .append(new BigDecimal(wareInfo.getPrice())
                          .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)).append(";\n");
                }
              }
              if (content.length() > 0) {
                sendEmail("多点茅台库存更新", content.toString(), toEmails, null, false);
                System.exit(0);
              }
            } else {
              if (wareInfos.size() > 1) {
                for (ItemDetailDTO wareInfo : wareInfos) {
                  content.append(wareInfo.getName()).append("有库存了，当前库存：").append(wareInfo.getStock())
                      .append(",价格：")
                      .append(new BigDecimal(wareInfo.getPrice())
                          .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP)).append(";\n");
                }
                sendEmail("多点茅台库存更新", content.toString(), toEmails, null, false);
                System.exit(0);
              }
            }
          }
        }
      }
    }


  }

  private final JavaMailSender javaMailSender;

  private void sendEmail(String subject, String content, String[] toEmails,
      List<String> ccEmails, boolean isHtml) throws Exception {
    log.debug(
        "Send email[html '{}'] to '{}' with subject '{}' and content={}",
        isHtml,
        toEmails,
        subject,
        content
    );

    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false,
        StandardCharsets.UTF_8.name());
    helper.setTo(toEmails);
    if (CollectionUtil.isNotEmpty(ccEmails)) {
      helper.setCc(ccEmails.toArray(new String[0]));
    }
    helper.setFrom(new InternetAddress("yyytylg@163.com"));
    helper.setSubject(subject);
    helper.setText(content, isHtml);
    helper.setSentDate(new Date());
    javaMailSender.send(mimeMessage);
    log.info("Sent email to User '{}'", toEmails);
  }

  public static void main(String[] args) {
//        String s = new String(Base64Decoder.decode(
//            "eigbO3Njby6esrL5hDDa1f8TcCZnlcH3wdzJKbM4J0y70alP8/qjiH/rXW0k6WFN7jXLFKakutlUnoUqCFh9Hzh0jeFRASvL2QI2l5kuMa6vuZi+6BVr5NyxHrflWdU89Q5iPfmc7iAGQBdukfoTtwZe97Eju2UtkKBtHDRMOjB3CT4iDLw5FOBFLGhYoGQG9KiqEJHdpXmowLUzm8Gj+HtDeD8BqhN86H1Zrff6NESXKgGxk8vRSDcRiocpBewTBI8SWFZNR/cDmdi0qRuhueUQjfB/mvJAWMy6goTvR8/sqf225+Hquag0pN/jF5npxG6Md3vRCGKhW7ufZVl9yg==".replace(" ","+"))
//        , StandardCharsets.UTF_8);
//        System.out.println(s);
    String decode = URLDecoder.decode(
        "eigbO3Njby6esrL5hDDa1f8TcCZnlcH3wdzJKbM4J0y70alP8%2FqjiH%2FrXW0k6WFN7jXLFKakutlUnoUqCFh9Hzh0jeFRASvL2QI2l5kuMa6vuZi%2B6BVr5NyxHrflWdU89Q5iPfmc7iAGQBdukfoTtwZe97Eju2UtkKBtHDRMOjB3CT4iDLw5FOBFLGhYoGQG9KiqEJHdpXmowLUzm8Gj%2BHtDeD8BqhN86H1Zrff6NESXKgGxk8vRSDcRiocpBewTBI8SWFZNR%2FcDmdi0qRuhueUQjfB%2FmvJAWMy6goTvR8%2Fsqf225%2BHquag0pN%2FjF5npxG6Md3vRCGKhW7ufZVl9yg%3D%3D",
        StandardCharsets.UTF_8);
    System.out.println(decode);
    String s = new String(Base64Decoder.decodeStr(decode));
    System.out.println(s);
    System.out.println(new String(Base64.getDecoder().decode(decode)));

  }

}
