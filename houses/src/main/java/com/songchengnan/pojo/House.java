package com.songchengnan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class House {
    Integer houseid; // 房子ID
    Integer statue; // 房子状态：0-未出租，1-已出租，2-需整改，3-已出售，4-已购买
    Integer area; // 房子面积
    Integer floor; // 房子楼层
    Integer price; // 房子租金/月
    String community; // 房子小区
    String address; // 房子地址
    String type; // 房子房型
    String toward; // 房子朝向
    String surrounding; // 房子周边
    String decoration; // 房子装修
    String housename; // 房子拥有者姓名
    String housephone; // 房子拥有者电话
    String tenantname; // 房子租客姓名
    String image; // 房子图片
}
