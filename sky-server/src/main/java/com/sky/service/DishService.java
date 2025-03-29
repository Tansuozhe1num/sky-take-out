package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DishService {


    /**
     * 新增菜品和对应口味
     * @param dishDTO
     */
    public void saveWithFlavour(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /***
     * 菜品批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}
