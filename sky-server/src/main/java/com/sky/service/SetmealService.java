package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    /*
    * 新增套餐信息*/
    void save(SetmealDTO setmealDTO);

    /*
    * 套餐管理的分页查询*/
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);
    /*
     * 根据ids
     * 删除套餐*/
    void deleteBatch(List<Long> ids);

    /*
     * 根据id查询套餐
     * 页面回显*/
    SetmealVO getByIdWithDish(Long id);


    /*
    * 修改套餐信息*/
    void update(SetmealDTO setmealDTO);

    /*
    * 修改套餐起售状态*/
    void Status(Integer status, Long id);
}
