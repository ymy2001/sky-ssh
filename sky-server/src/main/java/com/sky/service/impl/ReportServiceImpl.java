package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    /*
     * 统计指定时间营业额*/
    @Override
    public TurnoverReportVO getTurnOver(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList =new ArrayList<>();
        //开始到结束的时间,添加到列表
        while (!begin.equals(end)){
            //计算指定日期
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        List<Double> turnOver=new ArrayList<>();
        for (LocalDate date : dateList) {
            //查询每个日期对应的营业额
            //select sum(amount) from aoders where order_time>? and order_time <? and status=5
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map=new HashMap<>();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover=orderMapper.sumByMap(map);
            turnover=turnover==null?0.0:turnover;
            turnOver.add(turnover);

        }
        return TurnoverReportVO.builder()
                .dateList(StringUtils
                        .join(dateList,","))
                .turnoverList(StringUtils.join(turnOver,","))
                .build();
    }
    /*
     * 用户统计*/
    @Override
    public UserReportVO getUserStatics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList<>();//存放b-e之间的日期
        //开始到结束的时间,添加到列表
        while (!begin.equals(end)){
            //计算指定日期
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        //select count(id) from user where create_time<? and create_time>?
        List<Integer> newUserList=new ArrayList<>();
        //select count(id) from user where create_time<? and
        List<Integer> totalUserList=new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Map map=new HashMap<>();
            map.put("begin",endTime);
            Integer totalUser=userMapper.countByMap(map);
            map.put("begin",beginTime);
            Integer newUser=userMapper.countByMap(map);
            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }


        return UserReportVO.builder().dateList(StringUtils
                .join(dateList,",")).totalUserList(StringUtils
                .join(totalUserList,",")).newUserList(StringUtils
                .join(newUserList,",")).build();
    }
}
