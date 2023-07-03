package com.vinoxm.infoc.dao;

import com.vinoxm.infoc.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface AuthDao extends BaseDao {
    @Select("select id,user_name userName,password from admin_user where user_name=#{userName}")
    User selectByUserName(@Param("userName") String userName);

    @Insert("insert into admin_user (user_name,password) values(#{userName},#{password})")
    void insertOneUser(User user);

}
