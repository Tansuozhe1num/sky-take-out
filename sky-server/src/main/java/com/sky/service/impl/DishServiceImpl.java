package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavourMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavourMapper dishFlavourMapper;
    /***
     * 新增菜品
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavour(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 向菜品表插入一条数据
        dishMapper.insert(dish);

        // 获取主键值
        Long dishId = dish.getId();

        // 向口味插入n条数据
        List<DishFlavor> flavours = dishDTO.getFlavors();
        if (flavours != null && !flavours.isEmpty()) {
            flavours.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // 向口味表里面插入n条数据
            dishFlavourMapper.insertBatch(flavours);
        }
    }
}
