package com.songchengnan.service;

import com.songchengnan.pojo.Contract;
import com.songchengnan.pojo.PageBean;

import java.util.List;

public interface ContractService {
        void addContract(Contract contract);
        Contract getContractById(Integer contractid);
        List<Contract> getContractsByUser(String username);
        void updateContractStatus(Integer contractid, String status, String adminstatus); // 修改方法签名
        void updateHouseOwner(Integer houseid, Integer statue, String housename, String tenantname);
        List<Contract> getAllContracts();

        PageBean getContractsByUserPaged(String username, Integer page, Integer pageSize);
        PageBean getAllContractsPaged(Integer page, Integer pageSize);

}