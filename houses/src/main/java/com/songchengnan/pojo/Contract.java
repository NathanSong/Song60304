package com.songchengnan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contract {
    Integer contractid; // 合同ID
    Integer houseid; // 房屋ID
    String buyer; // 买家
    String seller; // 卖家
    String contractDate; // 合同日期
    String status; // 合同状态
    String order; // 用户申请内容
    String adminstatus; // 管理员审核状态
}