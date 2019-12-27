package com.ti4oss.common;
import lombok.Getter;
import lombok.ToString;
/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-06-28
 * Time: 15:45
 */
@Getter
@ToString
public enum AccountType {
    ADMIN(1,"管理员"),CUSTOMER(2,"客户");

    private int flag;
    private String desc;

    AccountType(int flag,String desc) {
        this.flag = flag;
        this.desc = desc;
    }

    public static AccountType valueOf(int flag) {
        for (AccountType accountTypes : values()) {
            if(accountTypes.flag == flag) {
                return accountTypes;
            }
        }
        throw  new RuntimeException("AccountType flag " + flag + " not found.");
    }

}
