package com.songchengnan.service.impl;

import com.songchengnan.mapper.ContractMapper;
import com.songchengnan.mapper.UserMapper;
import com.songchengnan.pojo.Contract;
import com.songchengnan.pojo.PageBean;
import com.songchengnan.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private UserMapper userMapper; // 注入 UserMapper

    @Override
    public void addContract(Contract contract) {
        contractMapper.addContract(contract);
    }

    @Override
    public Contract getContractById(Integer contractid) {
        return contractMapper.getContractById(contractid);
    }

    @Override
    public List<Contract> getContractsByUser(String username) {
        return contractMapper.getContractsByUser(username);
    }

    @Override
    public void updateContractStatus(Integer contractid, String status, String adminstatus) { // 修改方法实现
        contractMapper.updateContractStatus(contractid, status, adminstatus);
    }



    @Override
    public List<Contract> getAllContracts() {
        return contractMapper.getAllContracts();
    }

    @Override
    public void updateHouseOwner(Integer houseid, Integer statue, String housename, String tenantname) {
        // 根据新的 housename 查询用户的电话
        String housephone = userMapper.getUserPhoneByUsername(housename);
        // 如果状态为3（已出售/已购买），则更新tenantname为购买者的名字
        if (statue == 3) {
            contractMapper.updateHouseOwner(houseid, statue, housename, tenantname);
        } else {
            // 其他状态的处理
            contractMapper.updateHouseOwner(houseid, statue, housename, null);
        }
        // 更新 housephone
        contractMapper.updateHousePhone(houseid, housephone);
    }

    //用户/房东分页
    @Override
    public PageBean getContractsByUserPaged(String username, Integer page, Integer pageSize) {
        long total = contractMapper.getContractsCountByUser(username); // 获取总条数
        int start = (page - 1) * pageSize; // 计算起始位置
        List<Contract> rows = contractMapper.getContractsByUserPaged(username, start, pageSize); // 获取当前页数据
        return new PageBean(total, rows);
    }


    //管理员分页
    @Override
    public PageBean getAllContractsPaged(Integer page, Integer pageSize) {
        long total = contractMapper.getAllContractsCount(); // 获取总条数
        int start = (page - 1) * pageSize; // 计算起始位置
        List<Contract> rows = contractMapper.getAllContractsPaged(start, pageSize); // 获取当前页数据
        return new PageBean(total, rows);
    }


}