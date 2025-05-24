package com.songchengnan.controller;

import com.songchengnan.pojo.BuyRequest;
import com.songchengnan.pojo.Contract;
import com.songchengnan.pojo.PageBean;
import com.songchengnan.pojo.Result;
import com.songchengnan.service.BuyRequestService;
import com.songchengnan.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private BuyRequestService buyRequestService;


    @GetMapping("/contract/all")
    public Result getAllContracts() {
        List<Contract> contracts = contractService.getAllContracts();
        return Result.success(contracts);
    }

    @PostMapping("/contract/submitAppeal")
    public Result submitAppeal(@RequestParam Integer requestid, @RequestParam String status, @RequestParam String order) {
        try {
            BuyRequest buyRequest = buyRequestService.getBuyByRequestId(requestid);
            if (buyRequest == null) {
                return Result.error("申请信息不存在");
            }

            Contract contract = new Contract();
            contract.setHouseid(buyRequest.getHouseid());
            contract.setBuyer(buyRequest.getBuyer());
            contract.setSeller(buyRequest.getLandlordname());
            contract.setContractDate(LocalDateTime.now().toString());
            contract.setStatus(status);
            contract.setOrder(order);
            contract.setAdminstatus("pending"); // 默认管理员状态为 pending

            contractService.addContract(contract);
            buyRequestService.updateBuyRequestStatus(requestid, status);

            return Result.success("申请提交成功，合同已生成");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("申请提交失败：" + e.getMessage());
        }
    }


    @PostMapping("/contract/add")
    public Result addContract(@RequestBody Contract contract) {
        contractService.addContract(contract);
        return Result.success();
    }

    @GetMapping("/contract/{contractid}")
    public Result getContractById(@PathVariable Integer contractid) {
        Contract contract = contractService.getContractById(contractid);
        return Result.success(contract);
    }


    @GetMapping("/contract/user/{username}")
    public Result getContractsByUser(@PathVariable String username) {
        List<Contract> contracts = contractService.getContractsByUser(username);
        return Result.success(contracts);
    }



    @PutMapping("/contract/updateStatus")
    public Result updateContractStatus(@RequestParam Integer contractid, @RequestParam String status, @RequestParam String adminstatus) {
        contractService.updateContractStatus(contractid, status, adminstatus);
        return Result.success();
    }




    @PutMapping("/contract/updateHouseOwner")
    public Result updateHouseOwner(@RequestParam Integer houseid, @RequestParam Integer statue,
                                   @RequestParam String housename, @RequestParam String tenantname) {
        // 如果状态为3（已出售/已购买），则更新tenantname为购买者的名字
        if (statue == 3) {
            contractService.updateHouseOwner(houseid, statue, housename, tenantname);
        } else {
            contractService.updateHouseOwner(houseid, statue, housename, null);
        }
        return Result.success();
    }




    //房东/用户的分页查询
    @GetMapping("/contract/user/{username}/paged")
    public Result getContractsByUserPaged(
            @PathVariable String username,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean pageBean = contractService.getContractsByUserPaged(username, page, pageSize);
        return Result.success(pageBean);
    }


    //管理员分页查询
    @GetMapping("/contract/all/paged")
    public Result getAllContractsPaged(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageBean pageBean = contractService.getAllContractsPaged(page, pageSize);
        return Result.success(pageBean);
    }



}
