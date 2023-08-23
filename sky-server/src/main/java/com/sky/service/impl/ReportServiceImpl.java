package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.models.auth.In;
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
import java.util.stream.Collectors;

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

    /*
    * 统计指定区间的订单数据*/
    @Override
    public OrderReportVO getordersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList=new ArrayList<>();//存放b-e之间的日期
        //开始到结束的时间,添加到列表
        while (!begin.equals(end)){
            //计算指定日期
            begin=begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> orderCountList=new ArrayList<>();
        List<Integer> ValidOrderCountList=new ArrayList<>();
        //遍历集合查询订单数
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            //查询订单总数
            // select count(id) from orders where order_time > ? and order_time < ?
            Integer count = getCount(beginTime, endTime, null);
            //有效订单数
            //select count(id) from orders where order_time > ? and order_time < ? and status = ?
            Integer validcount = getCount(beginTime, endTime, Orders.COMPLETED);
            orderCountList.add(count);
            ValidOrderCountList.add(validcount);

        }
        //时间区间内订单总数
        Integer totalCount = orderCountList.stream().reduce(Integer::sum).get();
        //有效订单数
        Integer validOrderCount = ValidOrderCountList.stream().reduce(Integer::sum).get();

        Double orderCompletionRate= 0.0;
        if (validOrderCount!=0){
            orderCompletionRate=(double) (validOrderCount/totalCount);
        }
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(ValidOrderCountList,","))
                .totalOrderCount(totalCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }
    /*
    * 统计订单数量*/
    private Integer getCount(LocalDateTime begin, LocalDateTime end, Integer status){
        Map map=new HashMap();
        map.put("begin",begin);
        map.put("end",end);
        map.put("status",status);
        return orderMapper.countByMap(map);
    }
    /**
     * 查询指定时间区间内的销量排名top10
     * @param begin
     * @param end
     * @return
     * */
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end){
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSalesTop10(beginTime, endTime);

        String nameList = StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList()),",");
        String numberList = StringUtils.join(goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList()),",");

        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

}
