package com.cashsystem.common;

import lombok.Getter;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-08-04
 * Time: 10:16
 */
@Getter
@ToString
public enum AccountStatus {
    UNLOCK(1,"启用"),LOCK(2,"启停");
    private int flg;
    private String desc;
    AccountStatus(int flg,String desc) {
        this.flg = flg;
        this.desc = desc;
    }
    public static AccountStatus valueOf(int flg) {
        for (AccountStatus accountStatus : values()) {
            if(accountStatus.flg == flg) {
                return accountStatus;
            }
        }
        throw new RuntimeException("accountStatus flg"
        + flg + "not fount!");
    }

}
