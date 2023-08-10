package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

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

    /*
     * 根据id查找菜品*/
    DishVO getByidWithflaver(Long id);

    /*
    * 根据id修改信息，菜品*/
    void updateWithFlavor(DishDTO dishDTO);
    /*
     *
     * 根据id查询菜品
     * 用于套餐管理*/
    List<Dish> getByiDList(Long categoryId);
    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
