package com.songchengnan.service.impl;

import com.songchengnan.mapper.AppealMapper;
import com.songchengnan.mapper.UserMapper;
import com.songchengnan.pojo.Appeal;
import com.songchengnan.pojo.House;
import com.songchengnan.pojo.PageBean;
import com.songchengnan.service.AppealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AppealServiceImpl implements AppealService {

    @Autowired
    private AppealMapper appealMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public void submitAppeal(Appeal appeal) throws Exception {
        try {
            // 确保 appealtype 是 "举报不实" 或 "已整改"
            if (!"举报不实".equals(appeal.getAppealtype()) && !"已整改".equals(appeal.getAppealtype())) {
                appeal.setAppealtype("举报不实"); // 默认值
            }
            // 确保 houseid 不为空
            if (appeal.getHouseid() == null) {
                throw new Exception("houseid 不能为空");
            }
            appealMapper.insertAppeal(appeal);
        } catch (Exception e) {
            log.error("提交申诉失败：{}", e.getMessage(), e);
            throw new Exception("提交申诉失败", e);
        }
    }

    @Override
    public PageBean getAppealList(Integer page, Integer pageSize) throws Exception {
        try {
            // 计算起始位置
            Integer start = (page - 1) * pageSize;
            // 查询总数
            Long total = appealMapper.countAppeals();
            // 分页查询
            List<Appeal> appealList = appealMapper.selectAppeals(start, pageSize);
            // 封装PageBean
            return new PageBean(total, appealList);
        } catch (Exception e) {
            log.error("获取申诉列表失败：{}", e.getMessage(), e);
            throw new Exception("获取申诉列表失败", e);
        }
    }


    @Override
    public void handleAppeal(Integer appealid, String status) throws Exception {
        try {
            // 更新申诉状态
            appealMapper.updateAppealStatus(appealid, status);

            // 如果申诉通过，更新房屋状态
            if ("approved".equals(status)) {
                // 获取申诉记录
                Appeal appeal = appealMapper.selectAppealById(appealid);
                if (appeal != null) {
                    // 获取关联的房屋
                    House house = userMapper.houseDetail(appeal.getHouseid());
                    if (house != null) {
                        // 根据 tenantname 更新房屋状态
                        if (house.getTenantname() != null && !house.getTenantname().isEmpty()) {
                            // 如果有租客，状态设为 1（已出租）
                            userMapper.updateHouseStatus(house.getHouseid(), 1);
                        } else {
                            // 如果没有租客，状态设为 0（未出租）
                            userMapper.updateHouseStatus(house.getHouseid(), 0);
                        }
                        log.info("房屋状态已更新：houseid={}, statue={}", house.getHouseid(), house.getStatue());
                    } else {
                        log.error("未找到与申诉关联的房屋：houseid={}", appeal.getHouseid());
                    }
                } else {
                    log.error("未找到申诉记录：appealid={}", appealid);
                }
            }
        } catch (Exception e) {
            log.error("处理申诉失败：{}", e.getMessage(), e);
            throw new Exception("处理申诉失败", e);
        }
    }
}