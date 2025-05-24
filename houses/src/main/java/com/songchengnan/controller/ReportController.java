package com.songchengnan.controller;

import com.songchengnan.pojo.Report;
import com.songchengnan.pojo.Result;
import com.songchengnan.pojo.PageBean;
import com.songchengnan.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    // 新增接口：获取房东被举报的房屋列表//申诉功能用
    @GetMapping("/getReportsByReportee")
    public Result getReportsByReportee(@RequestParam String reportee) {
        List<Report> reports = reportService.getReportsByReportee(reportee);
        // 打印返回的数据
        System.out.println("返回的数据：" + reports);
        return Result.success(reports);
    }


    @PostMapping("/add")
    public Result addReport(@RequestBody Report report) {
        reportService.addReport(report);
        return Result.success();
    }

    @GetMapping("/{reportid}")
    public Result getReportById(@PathVariable Integer reportid) {
        Report report = reportService.getReportById(reportid);
        return Result.success(report);
    }

    // 分页查询所有举报信息
    @GetMapping("/all")
    public Result getAllReports(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer pageSize,
                                @RequestParam(required = false) String keyword) {
        PageBean pageBean = reportService.getAllReports(page, pageSize, keyword);
        return Result.success(pageBean);
    }





    @GetMapping("/getReportsByReporteePaged")
    public Result getReportsByReporteePaged(@RequestParam String reportee,
                                            @RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean pageBean = reportService.getReportsByReporteePaged(reportee, page, pageSize);
        return Result.success(pageBean);
    }



    @DeleteMapping("/delete/{reportid}")
    public Result deleteReport(@PathVariable Integer reportid) {
        reportService.deleteReport(reportid);
        return Result.success();
    }


    // 更新举报状态
    @PutMapping("/updateStatus/{reportid}")
    public Result updateReportStatus(@PathVariable Integer reportid, @RequestParam String status) {
        reportService.updateReportStatus(reportid, status);
        return Result.success();
    }

}