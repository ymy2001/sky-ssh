package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;

public interface SetmealService {
    /*
    * 新增套餐信息*/
    void save(SetmealDTO setmealDTO);

    /*
    * 套餐管理的分页查询*/
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
}
