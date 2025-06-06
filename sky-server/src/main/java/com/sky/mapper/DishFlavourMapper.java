package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavourMapper {


    void insertBatch(List<DishFlavor> flavours);

    /**
     * 根据菜品数据删除对应口味数据
     * @param dishId
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /***
     * 根据菜品批量删除口味
     * @param idx
     */
    void deleteByDishIdx(List<Long> idx);

    /***
     * 根据id查询口味信息
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
