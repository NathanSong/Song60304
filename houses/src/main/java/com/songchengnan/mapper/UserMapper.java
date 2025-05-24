package com.songchengnan.mapper;

import com.songchengnan.pojo.House;
import com.songchengnan.pojo.User;
import org.apache.ibatis.annotations.*;
import java.util.List;


@Mapper
public interface UserMapper {



    // 查询用户房子
    @Select("select * from house where housename = #{username} or tenantname = #{username}")
    List<House> userHouse(String username);

    // 查询用户的电话
    @Select("SELECT userphone FROM user WHERE username = #{username}")
    String getUserPhoneByUsername(String username);

    // 更新用户信息
    @Update("update user set  password = #{password},number = #{number}," +
            "userphone = #{userphone} where username = #{username}")
    void update(User user);

    // 查询用户信息
    @Select("select * from user where username = #{username}")
    User userInfo(String username);



    @Select("select * from house where houseid = #{houseid}")
    House houseDetail(Integer houseid);

    @Insert("insert into house(statue,area,floor,price,community,address,type,toward,surrounding,decoration,housename,housephone,tenantname,image) " +
            "values(#{statue},#{area},#{floor},#{price},#{community},#{address},#{type},#{toward},#{surrounding},#{decoration},#{housename},#{housephone},#{tenantname},#{image})")
    void addHouse(House house);







    @Select("select * from house where statue != 3")
    List<House> houseAll();




    @Delete("delete from house where houseid = #{houseid}")
    void deleteHouse(Integer houseid);







    // 模糊查询
    @Select("select * from house where community like concat('%',#{community},'%')")
    List<House> houseCommunity(String community);

    @Update("update house set statue = 1, tenantname = #{tenantname} where houseid = #{houseid}")
    void rentalHouse(Integer houseid, String tenantname);

    @Update("update house set statue = 0,tenantname = '' where houseid = #{houseid}")
    void terminateHouse(Integer houseid);

    // 根据房东名称查询名下房源
    @Select("SELECT * FROM house WHERE housename = #{housename}")
    List<House> findByHousename(String housename);


    // 根据房东名称查询房屋
    @Select("SELECT * FROM house WHERE housename = #{housename}")
    List<House> findHousesByOwner(String housename);



    // 更新房屋状态
    @Update("UPDATE house SET statue = #{statue}, area = #{area}, floor = #{floor}, price = #{price}, " +
            "community = #{community}, address = #{address}, type = #{type}, toward = #{toward}, " +
            "surrounding = #{surrounding}, decoration = #{decoration}, housename = #{housename}, " +
            "housephone = #{housephone}, tenantname = #{tenantname}, image = #{image} " +
            "WHERE houseid = #{houseid}")
    void updateHouse(House house);



    @Select("SELECT * FROM house WHERE housename = #{housename} AND reporttitle LIKE CONCAT('%', #{reporttitle}, '%') AND reportcontent LIKE CONCAT('%', #{reportcontent}, '%')")
    House findHouseByReportInfo(@Param("housename") String housename, @Param("reporttitle") String reporttitle, @Param("reportcontent") String reportcontent);





        // 根据举报 ID 获取房屋
        @Select("SELECT * FROM house WHERE houseid = (SELECT houseid FROM report WHERE reportid = #{reportid})")
        House selectHouseByReportId(@Param("reportid") Integer reportid);

    // 更新房屋状态
    @Update("UPDATE house SET statue = #{status} WHERE houseid = #{houseid}")
    void updateHouseStatus(@Param("houseid") Integer houseid, @Param("status") Integer status);


}



