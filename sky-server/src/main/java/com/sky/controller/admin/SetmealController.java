package com.sky.controller.admin;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/*
* 套餐管理接口*/
@RestController
@Api(tags = "套餐管理相关接口")
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;
    /*
    * 新增套餐*/
    @ApiOperation("新增套餐")
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO){
        log.info("新增套餐{}",setmealDTO);
        setmealService.save(setmealDTO);
        return Result.success();
    }

    /*
    * 套餐分页查询*/
    @ApiOperation("套餐分页查询接口")
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询{}",setmealPageQueryDTO);
        PageResult pageResult=setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }
    /*
    * 根据ids
    * 删除套餐*/
    @ApiOperation("删除套餐接口")
    @DeleteMapping
    public Result deleteByids(@RequestParam List<Long> ids){
        //@RequestParam接收路径请求
        log.info("删除套餐{}",ids);
        setmealService.deleteBatch(ids);
        return Result.success();
    }
    /*
    * 根据id查询套餐
    * 页面回显*/
    @ApiOperation("套餐id查询回显")
    @GetMapping("/{id}")
    public Result<SetmealVO> getByid(@PathVariable Long id){
        log.info("id查询套餐：{}",id);
        SetmealVO setmealVO=setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /*
    * 修改套餐*/
    @ApiOperation("修改套餐接口")
    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐：{}",setmealDTO);
        setmealService.update(setmealDTO);
        return Result.success();
    }
    /*
    * 停售起售状态*/
    @ApiOperation("起售状态")
    @PostMapping("/status/{status}")
    public Result status(@PathVariable Integer status, Long id){
        log.info("修改起售状态{}，{}",status,id);
        setmealService.Status(status,id);
        return Result.success();
    }
}
