package com.songchengnan.service;

import com.songchengnan.pojo.Appeal;
import com.songchengnan.pojo.PageBean;

import java.util.List;

public interface AppealService {
    // 提交申诉
    void submitAppeal(Appeal appeal) throws Exception;


    // 获取申诉列表 - 分页查询
    PageBean getAppealList(Integer page, Integer pageSize) throws Exception;

    // 处理申诉
    void handleAppeal(Integer appealid, String status) throws Exception;
}
