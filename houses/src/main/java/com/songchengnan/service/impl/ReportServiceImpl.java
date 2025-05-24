package com.songchengnan.service.impl;

import com.songchengnan.mapper.ReportMapper;
import com.songchengnan.mapper.UserMapper;
import com.songchengnan.pojo.House;
import com.songchengnan.pojo.Report;
import com.songchengnan.pojo.PageBean;
import com.songchengnan.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired  // 注入UserMapper来操作房屋数据
    private UserMapper userMapper;

    //申诉功能用
    @Override
    public List<Report> getReportsByReportee(String reportee) {
        log.info("查询被举报人 {} 的举报记录", reportee);
        List<Report> reports = reportMapper.getReportsByReportee(reportee);
        return reports;
    }


    @Override
    public void addReport(Report report) {
        log.info("新增举报信息");
        report.setReportdate(new Date());
        report.setStatus("pending"); // 默认状态为审核中
        reportMapper.insertReport(report);
    }


    @Override
    public Report getReportById(Integer reportid) {
        log.info("查询举报信息");
        return reportMapper.getReportById(reportid);
    }

    @Override
    public PageBean getAllReports(Integer page, Integer pageSize, String keyword) {
        log.info("分页查询所有举报信息");
        // 1. 查询总记录数
        Long count = reportMapper.countReports(keyword);
        // 2. 分页查询，获取列表数据
        Integer start = (page - 1) * pageSize;
        List<Report> reportList = reportMapper.pageReports(start, pageSize, keyword);
        // 3. 封装PageBean对象
        PageBean pageBean = new PageBean(count, reportList);
        return pageBean;
    }

    @Override
    public void deleteReport(Integer reportid) {
        log.info("删除举报信息");
        reportMapper.deleteReport(reportid);
    }



    @Override
    public PageBean getReportsByReporteePaged(String reportee, Integer page, Integer pageSize) {
        // 计算起始位置
        Integer start = (page - 1) * pageSize;
        // 查询总数
        Long total = reportMapper.countReportsByReportee(reportee);
        // 分页查询
        List<Report> reportList = reportMapper.getReportsByReporteePaged(reportee, start, pageSize);
        // 封装PageBean
        return new PageBean(total, reportList);
    }





    @Override
    public void updateReportStatus(Integer reportid, String status) {
        log.info("更新举报状态: reportid = {}, status = {}", reportid, status);
        // 1. 更新举报状态
        reportMapper.updateReportStatus(reportid, status);
        // 2. 如果是通过状态，需要处理被举报的房屋状态
        if ("approved".equals(status)) {
            log.info("处理通过状态的举报");
            // 获取举报详情
            Report report = reportMapper.getReportById(reportid);
            log.info("举报详情: {}", report);
            if (report != null && report.getHouseid() != null) {
                // 根据房屋ID更新房屋状态
                House house = userMapper.houseDetail(report.getHouseid());
                if (house != null) {
                    house.setStatue(2); // 设置整改状态
                    userMapper.updateHouse(house); // 更新房屋状态
                    log.info("房屋状态更新为需整改: houseid = {}", house.getHouseid());
                } else {
                    log.warn("未找到匹配的房屋");
                }
            } else {
                log.warn("未找到对应的举报信息或房屋ID");
            }
        }
    }



}