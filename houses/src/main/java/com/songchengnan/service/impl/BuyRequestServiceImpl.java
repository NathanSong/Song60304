package com.songchengnan.service.impl;

import com.songchengnan.mapper.BuyRequestMapper;
import com.songchengnan.pojo.BuyRequest;
import com.songchengnan.pojo.PageBean;
import com.songchengnan.service.BuyRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BuyRequestServiceImpl implements BuyRequestService {

    @Autowired
    private BuyRequestMapper buyRequestMapper;

    @Override
    public void submitBuyRequest(BuyRequest buyRequest) {
        buyRequest.setRequestDate(LocalDateTime.now());
        buyRequest.setStatus("pending");

        // 根据 houseid 获取房东名称
        String landlordname = buyRequestMapper.getLandlordNameByHouseId(buyRequest.getHouseid());
        if (landlordname != null) {
            buyRequest.setLandlordname(landlordname);
        }

        buyRequestMapper.insert(buyRequest);
    }

    @Override
    public BuyRequest getBuyByRequestId(Integer requestid) {
        return buyRequestMapper.findByRequestId(requestid);
    }

    @Override
    public List<BuyRequest> getBuyByLandlordname(String landlordname) {
        return buyRequestMapper.findByLandlord(landlordname);
    }

    @Override
    public BuyRequest findByHouseIdAndBuyer(Integer houseid, String buyer) {
        return buyRequestMapper.findByHouseIdAndBuyer(houseid, buyer);
    }

    @Override
    public void updateBuyRequestStatus(Integer requestid, String status) {
        buyRequestMapper.updateStatus(requestid, status);
    }

    @Override
    public List<BuyRequest> getBuyRequestsByBuyer(String buyer) {
        return buyRequestMapper.findByBuyer(buyer);
    }

    @Override
    public List<BuyRequest> getBuyRequestsByHouseId(Integer houseid) {
        return buyRequestMapper.findByHouseId(houseid);
    }

    @Override
    public List<BuyRequest> getAllBuyRequests() {
        return buyRequestMapper.findAll();
    }



    //用户分页
    @Override
    public PageBean getBuyRequestsByBuyerPaged(String buyer, Integer page, Integer pageSize) {
        // 计算起始位置
        Integer start = (page - 1) * pageSize;
        // 查询总数
        Long total = buyRequestMapper.countByBuyer(buyer);
        // 分页查询
        List<BuyRequest> buyRequests = buyRequestMapper.findByBuyerPaged(buyer, start, pageSize);
        // 封装PageBean
        return new PageBean(total, buyRequests);
    }

    //房东分页
    @Override
    public PageBean getBuyRequestsByLandlordPaged(String landlordname, Integer page, Integer pageSize) {
        // 计算起始位置
        Integer start = (page - 1) * pageSize;
        // 查询总数
        Long total = buyRequestMapper.countByLandlord(landlordname);
        // 分页查询
        List<BuyRequest> buyRequests = buyRequestMapper.findByLandlordPaged(landlordname, start, pageSize);
        // 封装PageBean
        return new PageBean(total, buyRequests);
    }


    //管理员分页查询
    @Override
    public PageBean getAllBuyRequestsPaged(Integer page, Integer pageSize) {
        // 计算起始位置
        Integer start = (page - 1) * pageSize;
        // 查询总数
        Long total = buyRequestMapper.countAll();
        // 分页查询
        List<BuyRequest> buyRequests = buyRequestMapper.findAllPaged(start, pageSize);
        // 封装PageBean
        return new PageBean(total, buyRequests);
    }







}
