package com.ti4oss.service;

import com.ti4oss.dao.BaseDao;
import com.ti4oss.dao.OrderDao;
import com.ti4oss.entity.Order;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-07-02
 * Time: 19:27
 */
public class OrderService extends BaseDao {


    private GoodsService goodsService;

    private OrderDao orderDao;

    public OrderService() {
        this.goodsService = new GoodsService();
        this.orderDao = new OrderDao();
    }

    public List<Order> queryOrderByAccount(Integer accountId) {
        return this.orderDao.queryOrderByAccount(accountId);
    }

    /* public Order queryOrderById(String orderId) {
        return this.orderDao.queryOrder(orderId);
    }*/

    public boolean commitOrder(Order order) {
        //提交订单
        return this.orderDao.insertOrder(order);
    }


}
