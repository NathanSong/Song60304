package com.songchengnan.service;

import com.songchengnan.pojo.Report;
import com.songchengnan.pojo.PageBean;

import java.util.List;

public interface ReportService {

    void addReport(Report report);


    // 新增方法：获取房东被举报的房屋列表
    List<Report> getReportsByReportee(String reportee);


    Report getReportById(Integer reportid);

    PageBean getAllReports(Integer page, Integer pageSize, String keyword); // 修改为支持搜索举报人和被举报人

    void deleteReport(Integer reportid);

    void updateReportStatus(Integer reportid, String status);


    // 分页查询被举报人的举报记录
    PageBean getReportsByReporteePaged(String reportee, Integer page, Integer pageSize);




}