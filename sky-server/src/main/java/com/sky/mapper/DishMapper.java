package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /*
     * 新增菜品*/
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /*
     * 菜品查询分页*/
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /*
     * 主键查询菜品*/
    @Select("select * from dish where id=#{id}")
    Dish getByid(Long id);

    /*
    * 主键删id*/
    @Delete("delete from dish where id=#{id}")
    void deleteByid(Long id);

    /*
    *根据id动态修改
    * 更新菜品表*/
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /*
    * 使用动态查询*/
    List<Dish> getByiDList(Dish dish);

    /*
    * 查询菜品id*/
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{id}")
    List<Dish> getBySetmealId(Long id);
}
