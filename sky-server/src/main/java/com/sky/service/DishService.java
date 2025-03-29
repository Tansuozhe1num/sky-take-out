package com.sky.service;

import com.sky.dto.DishDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;


public interface DishService {


    /**
     * 新增菜品和对应口味
     * @param dishDTO
     */
    public void saveWithFlavour(DishDTO dishDTO);
}
