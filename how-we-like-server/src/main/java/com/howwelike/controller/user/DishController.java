package com.howwelike.controller.user;

import com.howwelike.constant.StatusConstant;
import com.howwelike.entity.Dish;
import com.howwelike.result.Result;
import com.howwelike.service.DishService;
import com.howwelike.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    private static final String EMPTY_DISH_LIST_FLAG = "EMPTY_LIST";

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {

        // 查询redis中是否存在菜品数据
        String key = "dish_" + categoryId;

        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);

        // 如果存在, 直接返回，无需查询数据库

        if (list!=null && list.size()>0){
            return Result.success(list);
        }

        // 如果不存在, 查询数据库，将查询到的结果放入redis
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);

        list = dishService.listWithFlavor(dish);
        if (list == null || list.isEmpty()) {
            // 给空列表缓存设置一个较短的有效时间, 缓解缓存穿透问题
            redisTemplate.opsForValue().set(key, EMPTY_DISH_LIST_FLAG, 5L, TimeUnit.SECONDS);
        } else {
            // 设置有效缓存永不过期, 缓解缓存击穿和缓存雪崩问题
            redisTemplate.opsForValue().set(key, list);
        }

        return Result.success(list);
    }

}
