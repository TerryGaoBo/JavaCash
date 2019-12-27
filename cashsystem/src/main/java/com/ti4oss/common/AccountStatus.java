package com.ti4oss.common;
import lombok.Getter;
import lombok.ToString;
/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-06-28
 * Time: 17:00
 */
@Getter
@ToString
public enum AccountStatus {

    UNLOCK(1,"启用"),LOCK(2,"启停");

    private int flag;
    private String desc;
    AccountStatus(int flg,String desc) {
        this.desc = desc;
        this.flag = flg;
    }
    //查找值为flag 的状态 是启用 还是启停
    public static AccountStatus valueOf(int flag) {
        for(AccountStatus accountStatus :values()) {
            if(accountStatus.flag == flag) {
                return accountStatus;
            }
        }
        throw  new RuntimeException("AccountStatus flag " + flag + " not found.");
    }
}
