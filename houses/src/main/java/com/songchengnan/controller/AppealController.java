package com.songchengnan.controller;

import com.songchengnan.pojo.Appeal;
import com.songchengnan.pojo.PageBean;
import com.songchengnan.pojo.Result;
import com.songchengnan.service.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
@RequestMapping("/appeal")
public class AppealController {

    @Autowired
    private AppealService appealService;

    // 提交申诉
    @PostMapping("/submitAppeal")
    public Result submitAppeal(@RequestBody Appeal appeal) {
        // 参数校验
        if (appeal.getHouseid() == null || appeal.getReportid() == null || appeal.getAppealer() == null || appeal.getAppealcontent() == null) {
            return Result.error("参数缺失，请检查 houseid、reportid、appealer 和 appealcontent");
        }
        try {
            appealService.submitAppeal(appeal);
            return Result.success("申诉提交成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("提交申诉失败：" + e.getMessage());
        }
    }

    // 获取申诉列表 - 分页查询
    @GetMapping("/getAppealList")
    public Result getAppealList(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            PageBean pageBean = appealService.getAppealList(page, pageSize);
            return Result.success(pageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取申诉列表失败：" + e.getMessage());
        }
    }






    // 处理申诉
    @PostMapping("/handleAppeal")
    public Result handleAppeal(@RequestBody Map<String, Object> params) {
        try {
            Integer appealid = (Integer) params.get("appealid");
            String status = (String) params.get("status");
            appealService.handleAppeal(appealid, status);
            return Result.success("操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("处理申诉失败：" + e.getMessage());
        }
    }
}