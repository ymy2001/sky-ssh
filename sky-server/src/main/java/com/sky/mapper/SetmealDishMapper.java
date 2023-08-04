package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /*
    * 菜品id查询套餐内id*/
    List<Long> getSetmealDishIds(List<Long> dishIds);

    /*
    * 保存套餐和菜品的关联关系*/
    void insertBatch(List<SetmealDish> setmealDishes);

    /*
    * 根据id删除套餐关联菜品表*/
    @Delete("delete from setmeal_dish where setmeal_id=#{sid}")
    void deleteByid(Long sid);

    /*
    * id查找数据*/
    @Select("select * from sky_take_out.setmeal_dish where id=#{id}")
    List<SetmealDish> getBySetmealId(Long id);

}
