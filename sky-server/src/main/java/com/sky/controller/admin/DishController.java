package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishServer;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/*
*
* 菜品管理*/
@RestController
@Api(tags = "菜品相关接口")
@RequestMapping("/admin/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishServer dishServer;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DishService dishService;
    /*
    * 新增菜品*/
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        //构造dish_id
        String key="dish_"+dishDTO.getCategoryId();
        log.info("新增菜品{}",dishDTO);
        dishServer.saveWithFlavor(dishDTO);
        //清理缓存数据
        redisTemplate.delete(key);
        return Result.success();
    }

    /*
    * 菜品查询分页*/
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询{}",dishPageQueryDTO);
        PageResult pageResult=dishServer.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /*
    * 批量删除菜品*/
    @DeleteMapping()
    @ApiOperation("菜品批量删除")
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品批量删除{}",ids);
        dishServer.deleteBiatch(ids);
        //所有的菜品缓存数据清理
        Set keys = redisTemplate.keys("dish_");
        redisTemplate.delete(keys);
        return Result.success();
    }
    /*
    * 根据id查找菜品*/
    @GetMapping("/{id}")
    @ApiOperation("根据id查找菜品")
    public Result<DishVO> getByid(@PathVariable Long id){
        log.info("查询id{}",id);
        DishVO dishVO=dishServer.getByidWithflaver(id);
        return Result.success(dishVO);
    }
    /*
    * 修改菜品*/
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO);
        dishServer.updateWithFlavor(dishDTO);
        //所有的菜品缓存数据清理
        Set keys = redisTemplate.keys("dish_");
        redisTemplate.delete(keys);

        return Result.success();
    }
    /*
    *
    * 根据id查询菜品
    * 用于套餐管理*/
    @GetMapping("/list")
    @ApiOperation("分类id查询菜品")
    public Result<List<Dish>> getByiDList(Long categoryId){
        log.info("查询菜品{}",categoryId);
        List<Dish> list=dishServer.getByiDList(categoryId);
        return Result.success(list);
    }
    private void clearnCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }
    /**
     * 菜品起售停售
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id){
        dishService.startOrStop(status,id);
        return Result.success();
    }
}
