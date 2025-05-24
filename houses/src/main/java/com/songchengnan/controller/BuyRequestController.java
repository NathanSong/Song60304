package com.songchengnan.controller;

import com.songchengnan.pojo.BuyRequest;
import com.songchengnan.pojo.PageBean;
import com.songchengnan.pojo.Result;
import com.songchengnan.service.BuyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/buy")
public class BuyRequestController {

    @Autowired
    private BuyRequestService buyRequestService;



    //房东购买申请管理页面
    @GetMapping("/requests/byLandlord")
    public List<BuyRequest> getBuyRequestsByLandlord(@RequestParam String landlordname) {
        return buyRequestService.getBuyByLandlordname(landlordname);
    }
    // 更新购买申请状态
 



    @PostMapping("/request")
    public Result submitBuyRequest(@RequestBody BuyRequest buyRequest) {
        buyRequestService.submitBuyRequest(buyRequest);
        return Result.success();
    }

    @GetMapping("/requests/{buyer}")
    public Result getBuyRequestsByBuyer(@PathVariable String buyer) {
        return Result.success(buyRequestService.getBuyRequestsByBuyer(buyer));
    }

    @GetMapping("/requests/house/{houseid}")
    public Result getBuyRequestsByHouseId(@PathVariable Integer houseid) {
        return Result.success(buyRequestService.getBuyRequestsByHouseId(houseid));
    }

    @PutMapping("/request/{requestid}/status")
    public Result updateStatus(@PathVariable Integer requestid, @RequestParam String status) {
        buyRequestService.updateBuyRequestStatus(requestid, status);
        return Result.success();
    }

    @GetMapping("/requests")
    public Result getAllBuyRequests() {
        return Result.success(buyRequestService.getAllBuyRequests());
    }

    @GetMapping("/requests/paged")
    public Result getBuyRequestsByBuyerPaged(@RequestParam String buyer,
                                             @RequestParam(defaultValue = "1") Integer page,
                                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean pageBean = buyRequestService.getBuyRequestsByBuyerPaged(buyer, page, pageSize);
        return Result.success(pageBean);
    }

    //管理员分页查询
    @GetMapping("/requests/admin/paged")
    public Result getAllBuyRequestsPaged(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean pageBean = buyRequestService.getAllBuyRequestsPaged(page, pageSize);
        return Result.success(pageBean);
    }

    //房东分页查询
    @GetMapping("/requests/byLandlord/paged")
    public Result getBuyRequestsByLandlordPaged(@RequestParam String landlordname,
                                                @RequestParam(defaultValue = "1") Integer page,
                                                @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean pageBean = buyRequestService.getBuyRequestsByLandlordPaged(landlordname, page, pageSize);
        return Result.success(pageBean);
    }




}
