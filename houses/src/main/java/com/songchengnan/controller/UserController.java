package com.songchengnan.controller;

import com.songchengnan.pojo.House;
import com.songchengnan.pojo.Result;
import com.songchengnan.pojo.User;
import com.songchengnan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 普通用户控制器
@CrossOrigin(origins = "http://localhost:8080")// 允许跨域
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // 查询用户信息
    @GetMapping("/userHome/userInfo")
    public Result userInfo(@RequestParam String username){
        User user = userService.userInfo(username);
        return Result.success(user);
    }

    // 更新用户信息
    @PutMapping("/userHome/userInfo")
    public Result update(@RequestBody User user){
        userService.update(user);
        return Result.success();
    }

    // 查询用户房子
    @GetMapping("/userHome/userHouse")
    public Result userHouse(@RequestParam String username){
        List<House> house = userService.userHouse(username);
        return Result.success(house);
    }

    // 查询房子信息
    @GetMapping("/userHome/userhouse/detail")
    public Result userHouseDetail(@RequestParam Integer houseid){
        House house = userService.houseDetail(houseid);
        return Result.success(house);
    }

    // 新增房源
    @PostMapping("/userHome/userhouse/add")
    public Result addHouse(@RequestBody House house){
        userService.addHouse(house);
        return Result.success();
    }

    // 删除房源
    @DeleteMapping("/userHome/userhouse/delete")
    public Result deleteHouse(@RequestParam Integer houseid){
        userService.deleteHouse(houseid);
        return Result.success();
    }

    // 修改房源
    @PutMapping("/userHome/userhouse/update")
    public Result updateHouse(@RequestBody House house){
        userService.updateHouse(house);
        return Result.success();
    }

    // 查询所有房源
    @GetMapping("/userHome/houseShow")
    public Result userHouseShow(){
        List<House> house = userService.houseAll();
        return Result.success(house);
    }

    // 根据小区名模糊查询查询房源
    @GetMapping("/userHome/houseShow/community")
    public Result houseCommunity(@RequestParam String community){
        List<House> house = userService.houseCommunity(community);
        return Result.success(house);
    }

    // 租房
    @PutMapping("/userHome/userhouse/rental")
    public Result rentalHouse(@RequestParam Integer houseid,
                              @RequestParam String tenantname){
        userService.rentalHouse(houseid,tenantname);
        return Result.success();
    }

    // 退租
    @PutMapping("/userHome/userhouse/terminate")
    public Result terminateHouse(@RequestParam Integer houseid){
        userService.terminateHouse(houseid);
        return Result.success();
    }
}