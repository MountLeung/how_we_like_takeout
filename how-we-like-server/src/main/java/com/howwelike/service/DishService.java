package com.howwelike.service;

import com.howwelike.dto.DishDTO;
import com.howwelike.dto.DishPageQueryDTO;
import com.howwelike.entity.Dish;
import com.howwelike.result.PageResult;
import com.howwelike.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品和对应的口味数据
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 菜品的批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据ID 查询菜品和口味数据
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 根据id修改菜品信息和口味信息
     * @param dishDTO
     */
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 修改菜品的起售停售状态
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据分类ID查询菜品
     * @return
     */
    List<Dish> list(Long categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
