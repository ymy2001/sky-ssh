package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

public interface ReportService {
    /*
    * 统计指定时间营业额*/
    TurnoverReportVO getTurnOver(LocalDate begin, LocalDate end);
    /*
    * 用户统计*/
    UserReportVO getUserStatics(LocalDate begin, LocalDate end);

    /*
    * 指定区间的用户数据*/
    OrderReportVO getordersStatistics(LocalDate begin, LocalDate end);
    /**
     * 查询指定时间区间内的销量排名top10
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
    /**
     * 导出近30天的运营数据报表
     * @param response
     **/
    void exportBusinessData(HttpServletResponse response);
}
