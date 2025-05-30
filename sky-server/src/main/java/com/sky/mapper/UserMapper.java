package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /***
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenId(String openid);

    /**
     * 插入数据
     * @param newuser
     */
    void insert(User newuser);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);

    /**
     * 根据动态田间统计用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
