package com.ti4oss.cmd.command.impl.account;

import com.ti4oss.cmd.command.Subject;
import com.ti4oss.cmd.command.annotation.AdminCommand;
import com.ti4oss.cmd.command.annotation.CommandMeta;
import com.ti4oss.cmd.command.impl.AbstractCommand;
import com.ti4oss.entity.Account;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-06-29
 * Time: 20:01
 */
@CommandMeta(
        name = {"CKZH"},
        desc = "查看账号",
        group = "账号信息"
)
@AdminCommand
public class AccountBrowseCommand extends AbstractCommand {
    @Override
    public void execute(Subject subject) {
        System.out.println("账户浏览");
        List<Account> accountList = this.accountService.queryAllAccount();
        if(accountList.isEmpty()) {
            System.out.println("暂且没有账号存在");
        }else {
            System.out.println("------------------账号信息列表---------------------");
            System.out.println("|  编号  |  姓名  |  账号  |  密码  |  类型  | 状态 |");
            for (Account account: accountList ) {

               String str =  new StringBuilder().append("| ").append(account.getId()).append("  ")
                        .append("| ").append(account.getName()).append(" ")
                        .append("| ").append(account.getUsername()).append(" ")
                        .append("| ").append("******").append(" ")
                        .append("| ").append(account.getAccountType().getDesc()).append(" ")
                        .append("| ").append(account.getAccountStatus().getDesc()).append(" ")
                        .append("| ").toString();
                System.out.println(str);
            }
            System.out.println("--------------------------------------------");
        }
    }
}
