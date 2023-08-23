package com.sky.service;

import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    /*
    * 统计指定时间营业额*/
    TurnoverReportVO getTurnOver(LocalDate begin, LocalDate end);
    /*
    * 用户统计*/
    UserReportVO getUserStatics(LocalDate begin, LocalDate end);
}
