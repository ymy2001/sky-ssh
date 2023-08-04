package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishServer;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServerImpl implements DishServer {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /*
     * 新增菜品&&还有口味*/
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);//拷贝数据，保证一致
        //菜品表插入一条数据
        dishMapper.insert(dish);
        Long dishId = dish.getId();
        //口味表插入数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {dishFlavor.setDishId(dishId);});
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /*
     * 菜品查询分页*/
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }


    /*
     * 批量删除菜品*/
    @Override
    @Transactional
    public void deleteBiatch(List<Long> ids) {
        //判断当前菜品是否能删除、起售，关联，
        for (Long id : ids) {
            Dish dish=dishMapper.getByid(id);
            if (dish.getStatus()== StatusConstant.ENABLE){
                //起售
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        List<Long> setmealDishIds = setmealDishMapper.getSetmealDishIds(ids);
        if (setmealDishIds!=null && setmealDishIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //删除时需要同时删除口味
        for (Long id : ids) {
            dishMapper.deleteByid(id);
            //删除口味表
            dishFlavorMapper.deleteByDishid(id);
        }

    }
    /*
     * 根据id查找菜品*/
    @Override
    public DishVO getByidWithflaver(Long id) {
        //id查询菜品
        Dish byid = dishMapper.getByid(id);
        //查询口味
        List<DishFlavor> dishFlavors=dishFlavorMapper.getByDishId(id);
        //封装数据
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(byid,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /*
     * 根据id修改信息，菜品*/
    @Override
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //修改基本信息
        dishMapper.update(dish);
        //删除原有数据
        dishFlavorMapper.deleteByDishid(dishDTO.getId());
        //重新插入数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null && flavors.size()>0){
            flavors.forEach(dishFlavor -> {dishFlavor.setDishId(dishDTO.getId());});
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /*
     *
     * 根据id查询菜品
     * 用于套餐管理*/
    @Override
    public List<Dish> getByiDList(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.getByiDList(dish);
    }
}
