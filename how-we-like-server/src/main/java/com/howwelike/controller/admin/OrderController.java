package com.howwelike.controller.admin;

import com.howwelike.dto.OrdersCancelDTO;
import com.howwelike.dto.OrdersConfirmDTO;
import com.howwelike.dto.OrdersPageQueryDTO;
import com.howwelike.dto.OrdersRejectionDTO;
import com.howwelike.result.PageResult;
import com.howwelike.result.Result;
import com.howwelike.service.OrderService;
import com.howwelike.vo.OrderStatisticsVO;
import com.howwelike.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "商家端订单管理相关接口")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PutMapping("/cancel")
    @ApiOperation("商家取消订单")
    public Result adminCancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception{
        orderService.adminCancel(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/confirm")
    @ApiOperation("商家接单")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
       orderService.confirm(ordersConfirmDTO);
       return Result.success();
    }

    @PutMapping("/rejection")
    @ApiOperation("商家拒单")
    public Result reject(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception{
        orderService.reject(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result deliver(@PathVariable Long id){
        orderService.deliver(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable Long id){
        orderService.complete(id);
        return Result.success();
    }

    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics(){
        OrderStatisticsVO orderStatisticsVO = orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> details(@PathVariable Long id){
        OrderVO orderVO = orderService.getDetails(id);
        return Result.success(orderVO);
    }
}
