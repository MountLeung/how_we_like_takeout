package com.howwelike.service;

import com.howwelike.dto.*;
import com.howwelike.result.PageResult;
import com.howwelike.vo.OrderPaymentVO;
import com.howwelike.vo.OrderStatisticsVO;
import com.howwelike.vo.OrderSubmitVO;
import com.howwelike.vo.OrderVO;

public interface OrderService {

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);


    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 用户端历史订单分页查询
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult pageQuery4User(int page, int pageSize, Integer status);

    /**
     * 用户查询订单详情
     * @param id
     * @return
     */
    OrderVO getDetails(Long id);

    /**
     * 用户取消订单
     * @param id
     */
    void userCancel(Long id) throws Exception;

    /**
     * 用户再来一单
     */
    void repetition(Long id);

    /**
     * 商家端取消订单
     * @param ordersCancelDTO
     */
    void adminCancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 商家接单
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 商家拒单
     * @param ordersRejectionDTO
     */
    void reject(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 商家派送订单
     * @param id
     */
    void deliver(Long id);

    /**
     * 商家完成订单
     * @param id
     */
    void complete(Long id);

    /**
     * 条件搜索订单
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计
     * @return
     */
    OrderStatisticsVO statistics();

    /**
     * 客户催单
     * @param id
     */
    void reminder(Long id);
}
