package com.yue.dmall.job;

import java.util.Set;

/**
 * @author: Yueyue.Yin
 * @description:
 * @date:created in 2022/8/3 14:56
 * @modified by:
 */
public interface HotelTicketSerivice {

  void robTicket(String submitUrl,String queryUrl,String mobile, Set<String> successMobiles);
}
