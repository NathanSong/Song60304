package com.songchengnan.controller;

import com.songchengnan.pojo.House;
import com.songchengnan.pojo.Result;
import com.songchengnan.pojo.User;
import com.songchengnan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 房东控制器
@CrossOrigin(origins = "http://localhost:8080")// 允许跨域
@RestController
public class LandlordController {

    @Autowired
    private UserService userService;

    // 房东查询个人信息
    @GetMapping("/landlordHome/landlordInfo")
    public Result landlordInfo(@RequestParam String username){
        User user = userService.userInfo(username);
        return Result.success(user);
    }

    // 更新用户信息
    @PutMapping("/landlordHome/landlordInfo")
    public Result update(@RequestBody User user){
        userService.update(user);
        return Result.success();
    }

    // 房东查询我的房屋
    @GetMapping("/landlord/userHouse")
    public Result landlordUserHouse(@RequestParam String username){
        List<House> house = userService.userHouse(username);
        return Result.success(house);
    }

    // 房东查询房子信息
    @GetMapping("/landlord/userhouse/detail")
    public Result landlordHouseDetail(@RequestParam Integer houseid){
        House house = userService.houseDetail(houseid);
        return Result.success(house);
    }

    // 房东新增房源
    @PostMapping("/landlord/userhouse/add")
    public Result landlordAddHouse(@RequestBody House house){
        userService.addHouse(house);
        return Result.success();
    }

    // 房东删除房源
    @DeleteMapping("/landlord/userhouse/delete")
    public Result landlordDeleteHouse(@RequestParam Integer houseid){
        userService.deleteHouse(houseid);
        return Result.success();
    }

    // 房东修改房源
    @PutMapping("/landlord/userhouse/update")
    public Result landlordUpdateHouse(@RequestBody House house){
        userService.updateHouse(house);
        return Result.success();
    }



    // 房东查询所有房源（包括其他房东的房源）
    @GetMapping("/landlord/houseShow")
    public Result landlordHouseShow() {
        List<House> allHouses = userService.getAllHouses(); // 获取所有房源
        return Result.success(allHouses);
    }


    // 房东根据小区名模糊查询自己的房源
    @GetMapping("/landlord/houseShow/community")
    public Result houseCommunity(@RequestParam String community){
        List<House> house = userService.houseCommunity(community);
        return Result.success(house);
    }

    @PutMapping("/userhouse/update")
    public Result updateHouse(@RequestBody House house) {
        userService.updateHouse(house);
        return Result.success();
    }



}