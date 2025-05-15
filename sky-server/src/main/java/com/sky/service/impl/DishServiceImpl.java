package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavourMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishItemVO;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavourMapper dishFlavourMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
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

    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /***
     * 菜品批量删除
     * @param idx
     */
    @Transactional
    public void deleteBatch(List<Long> idx) {
        // 判断当前菜品是否能够删除, 是否存在在售卖的菜品

        for (Long id : idx) {
            Dish dish = dishMapper.getById(id);

            if (dish.getStatus() == 1) {
                // 当前菜品是起售中
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 判断当前菜品能否删除，是否失联了
        List<Long> setmealidx = setmealDishMapper.getSetmealIdByDishIdx(idx);
        if (setmealidx != null && !setmealidx.isEmpty()) {
            // 当前菜品被套餐关联
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

//        // 删除彩屏表中的数据
//        for (Long id : idx) {
//            dishMapper.delectById(id);
//            // 删除失联口味的数据
//            dishFlavourMapper.deleteByDishId(id);
//        }

        // 根据菜品ID批量删除
        dishMapper.deleteByIdx(idx);

        // 删除口味
        dishFlavourMapper.deleteByDishIdx(idx);
    }


    public DishVO getByIdWithFlavour(Long id) {
        // 根据id查询菜品数据
        Dish dish = dishMapper.getById(id);

        // 根据id查询口味数据
        List<DishFlavor> dishFlavors = dishFlavourMapper.getByDishId(id);

        // 查询到的数据封装成VO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavors);

        return dishVO;
    }

    /***
     * 修改菜品口味信息
     * @param dishDTO
     */
    public void updateWithFlavour(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 修改菜品表基本信息
        dishMapper.update(dish); // TODO: 有BUG

        // 删除口味数据
        dishFlavourMapper.deleteByDishId(dishDTO.getId());

        // 插入数据
        List<DishFlavor> flavours = dishDTO.getFlavors();
        if (flavours != null && !flavours.isEmpty()) {
            flavours.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            // 向口味表里面插入n条数据
            dishFlavourMapper.insertBatch(flavours);
        }
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    public void StartorStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavourMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
