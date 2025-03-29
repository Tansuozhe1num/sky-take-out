package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据彩票id查询套餐id
     * @param dishIdx
     * @return
     */
    //select setmeal——idfrom setmeal dish where dish_id in (1234)
    List<Long> getSetmealIdByDishIdx(List<Long> dishIdx);
}