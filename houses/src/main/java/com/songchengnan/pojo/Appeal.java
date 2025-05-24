package com.songchengnan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appeal {
    private Integer appealid; // 申诉ID
    private Integer reportid; // 关联的举报ID
    private Integer houseid; // 关联的房屋ID
    private String appealer; // 申诉人
    private String appealcontent; // 申诉内容
    private String appealtype; // 申诉类型，值为 "举报不实" 或 "已整改"
    private Date appealdate; // 申诉时间
    private String status; // 申诉状态: pending, approved, rejected

}
