package com.yue.dmall.job;

import cn.hutool.core.collection.ConcurrentHashSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author: Yueyue.Yin
 * @description:
 * @date:created in 2022/8/3 13:55
 * @modified by:
 */
@Slf4j
@RequiredArgsConstructor
//@Component
public class HotelTicketJob {

  private final HotelTicketSerivice hotelTicketSerivice;

  private final static String URL = "https://ticket.gzhotelgroup.com/api/Ticket/took?m={mobile}";
  private final static String QUERY_URL = "https://ticket.gzhotelgroup.com/api/Ticket/query?m={mobile}";


  private static final String[] MOBILES = new String[]{"18602319333", "13368317402", "15215119611",
      "16623630379", "18580852967"};

//,"15825941660","17373051445","19115360901","15736368812"

  //记录抢券成功的号码
  Set<String> successMobiles = new ConcurrentHashSet<>();


 // @Scheduled(cron = "0/2 * 13 * * ? ")
  @Scheduled(fixedRate = 600000)
  public void robTicket() {
//    if (LocalDateTime.now().isBefore(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0)))) {
//      log.warn("还没有到抢券时间!");
//      return;
//    }
    if (LocalDateTime.now().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 30, 0)))) {
      log.info("抢券时间结束，任务执行完毕，退出程序");
      System.exit(0);
    }

    log.debug(Thread.currentThread().getName());
    if (successMobiles.size() == MOBILES.length) {
      log.info("全部抢券成功，任务执行完毕，退出程序");
      System.exit(0);
    }
    for (String mobile : MOBILES) {
      if (successMobiles.contains(mobile)) {
        continue;
      }
      hotelTicketSerivice.robTicket(URL, QUERY_URL, mobile, successMobiles);
    }

  }
}
