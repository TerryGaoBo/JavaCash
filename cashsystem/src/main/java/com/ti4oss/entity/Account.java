package com.ti4oss.entity;
import com.ti4oss.common.AccountStatus;
import com.ti4oss.common.AccountType;
import lombok.Data;
/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-06-26
 * Time: 13:51
 */
@Data
public class Account {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private AccountType accountType;//账户类型
    private AccountStatus accountStatus;//账户状态
}
