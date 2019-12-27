package com.cashsystem.cmd;

import com.cashsystem.entity.Account;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-08-04
 * Time: 11:49
 */
public class Subject {
    private Account account;

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount(){
        return this.account;
    }

}
