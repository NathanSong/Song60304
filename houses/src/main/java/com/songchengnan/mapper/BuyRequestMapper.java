package com.songchengnan.mapper;

import com.songchengnan.pojo.BuyRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BuyRequestMapper {

    // 插入购买申请
    @Insert("INSERT INTO buyrequest (houseid, buyer, buyContent, requestDate, status, landlordname) " +
            "VALUES (#{houseid}, #{buyer}, #{buyContent}, #{requestDate}, #{status}, #{landlordname})")
    void insert(BuyRequest buyRequest);

    // 根据申请ID查询
    @Select("SELECT * FROM buyrequest WHERE requestid = #{requestid}")
    BuyRequest findByRequestId(@Param("requestid") Integer requestid);

    // 根据房东名称查询购买申请（按申请时间倒序）
    @Select("SELECT br.requestid, br.houseid, br.buyer, br.buyContent, br.requestDate, br.status, br.landlordname, " +
            "h.housename AS housename " +
            "FROM buyrequest br " +
            "LEFT JOIN house h ON br.houseid = h.houseid " +
            "WHERE br.landlordname = #{landlordname} " +
            "ORDER BY br.requestDate DESC")
    List<BuyRequest> findByLandlord(@Param("landlordname") String landlordname);

    // 根据房屋ID查询房东名称
    @Select("SELECT housename FROM house WHERE houseid = #{houseid}")
    String getLandlordNameByHouseId(@Param("houseid") Integer houseid);

    // 根据房屋ID和买家查询购买申请
    @Select("SELECT * FROM buyrequest WHERE houseid = #{houseid} AND buyer = #{buyer}")
    BuyRequest findByHouseIdAndBuyer(@Param("houseid") Integer houseid, @Param("buyer") String buyer);

    // 更新购买申请状态
    @Update("UPDATE buyrequest SET status = #{status} WHERE requestid = #{requestid}")
    void updateStatus(@Param("requestid") Integer requestid, @Param("status") String status);

    // 根据买家查询购买申请
    @Select("SELECT * FROM buyrequest WHERE buyer = #{buyer}")
    List<BuyRequest> findByBuyer(@Param("buyer") String buyer);

    // 根据房屋ID查询购买申请
    @Select("SELECT * FROM buyrequest WHERE houseid = #{houseid}")
    List<BuyRequest> findByHouseId(@Param("houseid") Integer houseid);

    // 查询所有购买申请
    @Select("SELECT * FROM buyrequest")
    List<BuyRequest> findAll();

    //用户分页
    @Select("SELECT * FROM buyrequest WHERE buyer = #{buyer} LIMIT #{start}, #{pageSize}")
    List<BuyRequest> findByBuyerPaged(@Param("buyer") String buyer, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    @Select("SELECT COUNT(*) FROM buyrequest WHERE buyer = #{buyer}")
    Long countByBuyer(@Param("buyer") String buyer);




    // 根据房东名称分页查询购买申请
    @Select("SELECT * FROM buyrequest WHERE landlordname = #{landlordname} ORDER BY requestDate DESC LIMIT #{start}, #{pageSize}")
    List<BuyRequest> findByLandlordPaged(@Param("landlordname") String landlordname, @Param("start") Integer start, @Param("pageSize") Integer pageSize);

    // 根据房东名称查询购买申请总数
    @Select("SELECT COUNT(*) FROM buyrequest WHERE landlordname = #{landlordname}")
    Long countByLandlord(@Param("landlordname") String landlordname);




    //管理员分页
    @Select("SELECT COUNT(*) FROM buyrequest")
    Long countAll();

    @Select("SELECT * FROM buyrequest LIMIT #{start}, #{pageSize}")
    List<BuyRequest> findAllPaged(@Param("start") Integer start, @Param("pageSize") Integer pageSize);





}