package com.ti4oss.cmd.command.impl;

import com.ti4oss.cmd.command.Command;
import com.ti4oss.cmd.command.annotation.AdminCommand;
import com.ti4oss.cmd.command.annotation.CommandMeta;
import com.ti4oss.cmd.command.annotation.CustomerCommand;
import com.ti4oss.cmd.command.annotation.EntranceCommand;
import com.ti4oss.cmd.command.impl.account.AccountBrowseCommand;
import com.ti4oss.cmd.command.impl.account.AccountPasswordResetCommand;
import com.ti4oss.cmd.command.impl.account.AccountStatusSetCommand;
import com.ti4oss.cmd.command.impl.common.AboutCommand;
import com.ti4oss.cmd.command.impl.common.HelpCommand;
import com.ti4oss.cmd.command.impl.common.QuitCommand;
import com.ti4oss.cmd.command.impl.entrance.LoginCommand;
import com.ti4oss.cmd.command.impl.entrance.RegisterCommand;
import com.ti4oss.cmd.command.impl.goods.GoodsBrowseCommand;
import com.ti4oss.cmd.command.impl.goods.GoodsPutAwayCommand;
import com.ti4oss.cmd.command.impl.goods.GoodsSoldOutCommand;
import com.ti4oss.cmd.command.impl.goods.GoodsUpdateCommand;
import com.ti4oss.cmd.command.impl.order.OrderBrowseCommand;
import com.ti4oss.cmd.command.impl.order.OrderPayCommand;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * Description: 把所有命令 全部搞到一起
 * User: GAOBO
 * Date: 2019-06-29
 * Time: 20:13
 */
public class Commands {
    /**
     * 存放所有命令对象的集合  不能存储相同的元素。
     */
    private static final Set<Command> COMMANDS = new HashSet<>();

    private static final Command CACHED_HELP_COMMAND;
    //key为 命令字符串DL    value为命令对象
    public static final Map<Set<String>,Command> ADMIN_COMMANDS = new HashMap<>();
    public static final Map<Set<String>,Command> CUSTOMER_COMMANDS = new HashMap<>();
    public static final Map<Set<String>,Command> ENTRANCE_COMMANDS = new HashMap<>();

    static {
        Collections.addAll(COMMANDS,
                new LoginCommand(),
                new RegisterCommand(),
                new AboutCommand(),
                new QuitCommand(),
                //----------
                CACHED_HELP_COMMAND = new HelpCommand(),

                new AccountBrowseCommand(),
                new AccountPasswordResetCommand(),
                new AccountStatusSetCommand(),
                new GoodsBrowseCommand(),
                new GoodsPutAwayCommand(),
                new GoodsSoldOutCommand(),
                new GoodsUpdateCommand(),
                new OrderBrowseCommand(),
                new OrderPayCommand()
                );
        for (Command command : COMMANDS) {
            //获得当前命令的class文件
            Class<?> cls = command.getClass();

            AdminCommand adminCommand = cls.getDeclaredAnnotation(AdminCommand.class);
            CustomerCommand customerCommand = cls.getDeclaredAnnotation(CustomerCommand.class);
            EntranceCommand entranceCommand = cls.getDeclaredAnnotation(EntranceCommand.class);

            CommandMeta commandMeta = cls.getDeclaredAnnotation(CommandMeta.class);
            /*if(commandMeta == null) {
                continue;
            }*/
            //将commandMeta的name转化为字符串
            String[] strings = commandMeta.name();
            Set<String> commandCodesKey = new HashSet<>(Arrays.asList(strings));

            if(adminCommand != null) {
                ADMIN_COMMANDS.put(commandCodesKey,command);
            }
            if(customerCommand != null) {
                CUSTOMER_COMMANDS.put(commandCodesKey,command);
            }
            if(entranceCommand != null) {
                ENTRANCE_COMMANDS.put(commandCodesKey,command);
            }
        }
    }

    public static Command getCachedHelpCommand(){
        return CACHED_HELP_COMMAND;
    }

    public static Command getEntranceCommand (String command) {
       return getCommand(command,ENTRANCE_COMMANDS);
    }

    public static Command getAdminCommand (String command) {
        return getCommand(command,ADMIN_COMMANDS);
    }

    public static Command getCustomerCommand (String command) {
        return getCommand(command,CUSTOMER_COMMANDS);
    }


    private static Command getCommand(String command,Map<Set<String>,Command> commandMap) {
        for (Map.Entry<Set<String>, Command> entry : commandMap.entrySet()) {
            if (entry.getKey().contains(command)) {
                return entry.getValue();
            }
        }
        //如果输入是回车什么的  在MAP里面没有  返回入口的命令
        return CACHED_HELP_COMMAND;
    }

}
