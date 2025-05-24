package com.songchengnan.mapper;

import com.songchengnan.pojo.Report;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ReportMapper {



    @Insert("INSERT INTO report (reporter, reportee, reporttitle, reportcontent, reportdate, status, houseid) " +
            "VALUES (#{reporter}, #{reportee}, #{reporttitle}, #{reportcontent}, #{reportdate}, #{status}, #{houseid})")
    void insertReport(Report report);



    @Select("SELECT * FROM report WHERE reportid = #{reportid}")
    Report getReportById(Integer reportid);

    @Select("SELECT COUNT(*) FROM report WHERE (reporter LIKE CONCAT('%', #{keyword}, '%') OR reportee LIKE CONCAT('%', #{keyword}, '%'))")
    Long countReports(@Param("keyword") String keyword); // 修改为支持搜索举报人和被举报人

    @Select("SELECT * FROM report WHERE (reporter LIKE CONCAT('%', #{keyword}, '%') OR reportee LIKE CONCAT('%', #{keyword}, '%')) LIMIT #{start}, #{pageSize}")
    List<Report> pageReports(@Param("start") Integer start, @Param("pageSize") Integer pageSize, @Param("keyword") String keyword); // 修改为支持搜索举报人和被举报人

    @Delete("DELETE FROM report WHERE reportid = #{reportid}")
    void deleteReport(Integer reportid);

    @Update("UPDATE report SET status = #{status} WHERE reportid = #{reportid}")
    void updateReportStatus(@Param("reportid") Integer reportid, @Param("status") String status);

    // 新增方法：根据被举报人查询举报记录
    @Select("SELECT * FROM report WHERE reportee = #{reportee}")
    List<Report> getReportsByReportee(@Param("reportee") String reportee);



    //房东处申诉分页
    // 查询总数
    @Select("SELECT COUNT(*) FROM report WHERE reportee = #{reportee}")
    Long countReportsByReportee(@Param("reportee") String reportee);

    // 分页查询
    @Select("SELECT * FROM report WHERE reportee = #{reportee} LIMIT #{start}, #{pageSize}")
    List<Report> getReportsByReporteePaged(@Param("reportee") String reportee,
                                           @Param("start") Integer start,
                                           @Param("pageSize") Integer pageSize);



}