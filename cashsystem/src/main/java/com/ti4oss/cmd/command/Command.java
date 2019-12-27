package com.ti4oss.cmd.command;

import java.io.Console;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-06-28
 * Time: 19:49
 */
public interface Command {

    //每个命令都会创建Scanner对象 在这里直接创建
    Scanner scanner = new Scanner(System.in);
    //统一调用此方法
    void execute(Subject subject);
}
