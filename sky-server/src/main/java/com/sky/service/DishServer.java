package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

import java.util.List;

public interface DishServer {
    /*
    * 新增菜品和口味*/
    public void saveWithFlavor(DishDTO dishDTO);

    /*
     * 菜品查询分页*/
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
     * 批量删除菜品*/
    void deleteBiatch(List<Long> ids);
}
