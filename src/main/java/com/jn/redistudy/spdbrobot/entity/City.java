package com.jn.redistudy.spdbrobot.entity;

/**
 * Created by Lee on 2018/4/27 8:49.
 */
public class City {


    /**编号*/
    private Integer id;

    /** 省份编号 */
    private Integer province_id;

    /** 城市名称  */
    private String city_name;

    /** 城市描述 */
    private String description;

    public City() {
    }

    public City(Integer id, Integer province_id, String city_name, String description) {
        this.id = id;
        this.province_id = province_id;
        this.city_name = city_name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProvince_id() {
        return province_id;
    }

    public void setProvince_id(Integer province_id) {
        this.province_id = province_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                ", province_id=" + province_id +
                ", city_name='" + city_name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
