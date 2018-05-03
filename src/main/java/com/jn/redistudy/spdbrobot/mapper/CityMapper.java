package com.jn.redistudy.spdbrobot.mapper;

import com.jn.redistudy.spdbrobot.entity.City;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Lee on 2018/4/27 9:05.
 */
public interface CityMapper {

    @Select("select * from city")
    public List<City> GetAll();


}
