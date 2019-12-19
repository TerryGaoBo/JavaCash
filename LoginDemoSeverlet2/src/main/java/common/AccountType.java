package common;

import lombok.Getter;
import lombok.ToString;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-11-24
 * Time: 13:57
 */
@Getter
@ToString
public enum AccountType {
    ADMIN(1,"管理员"),CUSTOMER(2,"客户");
    private int flg;
    private String desc;
    AccountType(int flg,String desc) {
        this.flg = flg;
        this.desc = desc;
    }
    public static AccountType valueOf(int flg) {
        for (AccountType accountType : values()) {
            if(accountType.flg == flg) {
                return accountType;
            }
        }
        throw new RuntimeException("accountType flg"
                + flg + "not fount!");
    }
}
