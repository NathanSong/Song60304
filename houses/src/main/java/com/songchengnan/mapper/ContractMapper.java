package com.songchengnan.mapper;

import com.songchengnan.pojo.Contract;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ContractMapper {

 // 更新合同状态和管理员状态
 @Update("UPDATE contract SET status = #{status}, adminstatus = #{adminstatus} WHERE contractid = #{contractid}")
 void updateContractStatus(@Param("contractid") Integer contractid, @Param("status") String status, @Param("adminstatus") String adminstatus);

 // 更新房屋所有者信息
 @Update("UPDATE house SET statue = #{statue}, housename = #{housename}, tenantname = #{tenantname} WHERE houseid = #{houseid}")
 void updateHouseOwner(@Param("houseid") Integer houseid, @Param("statue") Integer statue,
                       @Param("housename") String housename, @Param("tenantname") String tenantname);

 @Select("SELECT * FROM contract")
 List<Contract> getAllContracts();

 // 插入合同
 @Insert("INSERT INTO contract (houseid, buyer, seller, contractDate, status, `order`, adminstatus) " +
         "VALUES (#{houseid}, #{buyer}, #{seller}, #{contractDate}, #{status}, #{order}, #{adminstatus})")
 void addContract(Contract contract);

 // 根据合同ID查询合同
 @Select("SELECT * FROM contract WHERE contractid = #{contractid}")
 Contract getContractById(@Param("contractid") Integer contractid);

 // 根据买家或卖家查询合同
 @Select("SELECT * FROM contract WHERE buyer = #{username} OR seller = #{username}")
 List<Contract> getContractsByUser(@Param("username") String username);

 // 更新房屋的电话
 @Update("UPDATE house SET housephone = #{housephone} WHERE houseid = #{houseid}")
 void updateHousePhone(@Param("houseid") Integer houseid, @Param("housephone") String housephone);



 //用户分页
 @Select("SELECT * FROM contract WHERE buyer = #{username} OR seller = #{username} LIMIT #{start}, #{pageSize}")
 List<Contract> getContractsByUserPaged(@Param("username") String username, @Param("start") Integer start, @Param("pageSize") Integer pageSize);


 @Select("SELECT COUNT(*) FROM contract WHERE buyer = #{username} OR seller = #{username}")
 long getContractsCountByUser(@Param("username") String username);



 //管理员分页
 @Select("SELECT * FROM contract LIMIT #{start}, #{pageSize}")
 List<Contract> getAllContractsPaged(@Param("start") Integer start, @Param("pageSize") Integer pageSize);

 @Select("SELECT COUNT(*) FROM contract")
 long getAllContractsCount();


}