package com.yue.dmall.dto;

import java.util.List;
import lombok.Data;

/**
 * @author: Yueyue.Yin
 * @description:
 * @date:created in 2022/9/2 15:32
 * @modified by:
 */
@Data
public class DmailResDTO {

  private String code;

  private MoutaiActivityDTO data;

  private String msg;


  @Data
  public static class MoutaiActivityDTO{

    private Long activityId;

    private List<ItemDetailDTO> wareInfos;

  }

  @Data
  public static class ItemDetailDTO{
    private String name;
    private Integer price;
    private Integer stock;
  }

}
