package com.ti4oss.cmd.command.impl;

import com.ti4oss.cmd.command.Command;
import com.ti4oss.dao.GoodsDao;
import com.ti4oss.service.AccountService;
import com.ti4oss.service.GoodsService;
import com.ti4oss.service.OrderService;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-06-28
 * Time: 19:59
 */
public abstract class AbstractCommand implements Command {

    public AccountService accountService;
    public GoodsService goodsService;
    public OrderService orderService;
    public AbstractCommand() {
        this.accountService = new AccountService();
        this.goodsService = new GoodsService();
        this.orderService = new OrderService();
    }

    protected final void warningPrintln(String message) {
        System.out.println("【警告】："+ message);
    }
    protected final void errorPrintln(String message) {
        System.out.println("【错误】："+ message);
    }
    protected final void hitPrintln(String message) {
        System.out.println(">> "+ message);
    }
}
