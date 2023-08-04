package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /*
    * 插入套餐信息*/
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /*
    * 套餐分页查找*/
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /*
    * 根据id查找套餐起售状态*/
    @Select("select * from setmeal where id=#{id};")
    Setmeal getByid(Long id);

    /*
    * 根据id删除套餐*/
    @Delete("delete from setmeal where id=#{sid}")
    void deleteByid(Long sid);

    /*
    * id查找数据*/
    @Select("select * from setmeal where id=#{id}")
    Setmeal getById(Long id);

    /*
    * 更新套餐表*/
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);
}
