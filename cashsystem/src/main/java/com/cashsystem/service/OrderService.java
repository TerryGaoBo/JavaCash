package com.cashsystem.service;

import com.cashsystem.dao.OrderDao;
import com.cashsystem.entity.Order;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-08-10
 * Time: 15:41
 */
public class OrderService {

    public OrderDao orderDao;
    public OrderService() {
        this.orderDao = new OrderDao();
    }

    public boolean commitOrder(Order order) {
        return this.orderDao.commitOrder(order);
    }

    public List<Order> queryOrderByAccount(Integer accountId) {
        return this.orderDao.queryOrderByAccount(accountId);
    }
}
