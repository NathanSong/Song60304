package com.songchengnan.mapper;

import com.songchengnan.pojo.Appeal;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AppealMapper {
    // 插入申诉记录
    @Insert("INSERT INTO appeal (reportid, houseid, appealer, appealcontent, appealtype, appealdate, status) " +
            "VALUES (#{reportid}, #{houseid}, #{appealer}, #{appealcontent}, #{appealtype}, NOW(), 'pending')")
    void insertAppeal(Appeal appeal);



    // 获取所有申诉记录 - 分页
    @Select("SELECT * FROM appeal LIMIT #{start}, #{pageSize}")
    List<Appeal> selectAppeals(@Param("start") Integer start, @Param("pageSize") Integer pageSize);
    // 查询申诉总数
    @Select("SELECT COUNT(*) FROM appeal")
    Long countAppeals();



    // 根据 ID 获取申诉记录
    @Select("SELECT * FROM appeal WHERE appealid = #{appealid}")
    Appeal selectAppealById(@Param("appealid") Integer appealid);

    // 更新申诉状态
    @Update("UPDATE appeal SET status = #{status} WHERE appealid = #{appealid}")
    void updateAppealStatus(@Param("appealid") Integer appealid, @Param("status") String status);





}
