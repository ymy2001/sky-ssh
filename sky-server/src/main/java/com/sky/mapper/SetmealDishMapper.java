package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /*
    * 菜品id查询套餐内id*/
    List<Long> getSetmealDishIds(List<Long> dishIds);

    /*
    * 保存套餐和菜品的关联关系*/
    void insertBatch(List<SetmealDish> setmealDishes);
}
