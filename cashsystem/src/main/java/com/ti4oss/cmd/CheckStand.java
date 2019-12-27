package com.ti4oss.cmd;

import com.ti4oss.cmd.command.Command;
import com.ti4oss.cmd.command.Subject;
import com.ti4oss.cmd.command.impl.AbstractCommand;
import com.ti4oss.cmd.command.impl.Commands;
import com.ti4oss.entity.Account;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-06-28
 * Time: 18:25
 */
public class CheckStand extends AbstractCommand {

    public static void main(String[] args) {
        Subject subject = new Subject();
        new CheckStand().execute(subject);
    }


    @Override
    public void execute(Subject subject) {
        //System.out.println("帮助命令"); 先把进入程序的命令进行缓存
        Commands.getCachedHelpCommand().execute(subject);
        while(true) {
            System.out.println(">>");
            String line = scanner.nextLine();
            String commandCode = line.trim().toUpperCase();
            Account account = subject.getAccount();
            if(account == null) {
                Commands.getEntranceCommand(commandCode).execute(subject);
            }else {
                //System.out.println("account != null");
                switch (account.getAccountType()) {
                    case ADMIN:
                        Commands.getAdminCommand(commandCode).execute(subject);
                        break;
                    case CUSTOMER:
                        Commands.getCustomerCommand(commandCode).execute(subject);
                        break;
                    default:
                }
            }
        }
    }
}
