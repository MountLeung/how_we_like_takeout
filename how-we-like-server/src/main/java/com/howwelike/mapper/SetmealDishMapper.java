package com.howwelike.mapper;

import com.howwelike.annotation.AutoFill;
import com.howwelike.entity.SetmealDish;
import com.howwelike.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 根据菜品id查询对应的套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);


    /**
     * 批量插入新套餐对应的菜品
     */
    @AutoFill(OperationType.INSERT)
    void insertBatch(List<SetmealDish> setmealDishes);

    @Delete("DELETE from setmeal_dish where setmeal_id = #{id}")
    void deleteBySetmealId(Long id);

    @Select("SELECT * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);
}
