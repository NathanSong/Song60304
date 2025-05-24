package com.songchengnan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyRequest {
    private Integer requestid; // 申请ID
    private Integer houseid; // 房屋ID
    private String buyer; // 购买者
    private String buyContent; // 购买申请内容
    private LocalDateTime requestDate; // 申请时间
    private String status; // 用户可见的最终状态
    private String landlordname; // 房东名称
}

