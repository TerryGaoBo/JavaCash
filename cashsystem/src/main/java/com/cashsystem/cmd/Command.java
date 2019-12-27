package com.cashsystem.cmd;

import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-08-04
 * Time: 11:47
 */
public interface Command {
    Scanner scanner = new Scanner(System.in);

    void execute(Subject subject);
}
