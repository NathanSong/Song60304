package com.songchengnan.service;

import com.songchengnan.pojo.BuyRequest;
import com.songchengnan.pojo.PageBean;

import java.util.List;

public interface BuyRequestService {
    void submitBuyRequest(BuyRequest buyRequest);
    BuyRequest findByHouseIdAndBuyer(Integer houseid, String buyer);

    List<BuyRequest> getBuyRequestsByBuyer(String buyer);
    List<BuyRequest> getBuyRequestsByHouseId(Integer houseid);
    List<BuyRequest> getAllBuyRequests();

    List<BuyRequest> getBuyByLandlordname(String landlordname); // 添加声明
    BuyRequest getBuyByRequestId(Integer requestid);

    void updateBuyRequestStatus(Integer requestid, String status);

    PageBean getBuyRequestsByBuyerPaged(String buyer, Integer page, Integer pageSize);

    PageBean getAllBuyRequestsPaged(Integer page, Integer pageSize);

    PageBean getBuyRequestsByLandlordPaged(String landlordname, Integer page, Integer pageSize);


}
