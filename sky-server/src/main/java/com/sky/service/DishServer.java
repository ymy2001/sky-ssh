package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishServer {
    /*
    * 新增菜品和口味*/
    public void saveWithFlavor(DishDTO dishDTO);
}
