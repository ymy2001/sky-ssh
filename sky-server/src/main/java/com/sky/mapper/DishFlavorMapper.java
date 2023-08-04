package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /*
    * 批量插入口味数据*/
    void insertBatch(List<DishFlavor> flavors);

    /*
    * 删除口味，根据dishid*/
    @Delete("delete from sky_take_out.dish_flavor where dish_id=#{dishId}")
    void deleteByDishid(Long dishId);

    /*
    * 根据菜品id查询口味
    * 返回list集合*/
    @Select("select * from sky_take_out.dish_flavor where dish_id=#{DishId}")
    List<DishFlavor> getByDishId(Long DishId);
}
