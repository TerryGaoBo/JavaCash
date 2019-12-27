package com.ti4oss.dao;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-06-30
 * Time: 14:22
 */
public class BaseDao {

    private static volatile DataSource dataSource;

    //采用基本实现
    private DataSource getDataSource() {
        if (dataSource == null) {
            synchronized(DataSource.class) {
                if (dataSource == null) {
                    dataSource = new MysqlDataSource();//MySql数据源
                   String host = "127.0.0.1";
                   String port = "3306";
                    ((MysqlDataSource) dataSource).setUrl("jdbc:mysql://" + (host + ":" + port) + "/check_stand");
                    ((MysqlDataSource) dataSource).setUser("root");
                    ((MysqlDataSource) dataSource).setPassword("111111");
                }
            }
        }
        return dataSource;
    }

    protected Connection getConnection(boolean autoCommit) throws SQLException {
        //获取连接
        Connection connection = this.getDataSource().getConnection();
        //如果true  每写一条语句 自动进行提交
        connection.setAutoCommit(autoCommit);
        return connection;
    }

    protected void closeResource(ResultSet resultSet, PreparedStatement statement, Connection connection) {
        //结果 -> 命令 -> 连接
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public String getSql(String sqlName) {
        System.out.println("=====sqlName:"+sqlName);
            //InputStream 是字节流
            try (InputStream in = this.getClass()
                    .getClassLoader()
                    //这个方法是用来获取配置文件的，方法传入的参数是一个路径
                    .getResourceAsStream("script/" + sqlName.substring(1) + ".sql");
                 // 从1 开始提取的原因是：sqlName: @query_order_by_account 去掉@符号
            ) {
                if (in == null) {
                    throw new RuntimeException("load sql " + sqlName + " failed");
                } else {
                    //InputStreamReader :字节流 通向字符流的桥梁
                    try (InputStreamReader isr = new InputStreamReader(in);
                         //BufferedReader -> 从字符输入流中读取文本并缓冲字符
                         BufferedReader reader = new BufferedReader(isr)) {

                        StringBuilder stringBuilder = new StringBuilder();

                        stringBuilder.append(reader.readLine());

                        String line;
                        while (( line = reader.readLine()) != null) {
                            stringBuilder.append(" ").append(line);
                        }

                        //System.out.println("value:" + stringBuilder.toString());
                        return stringBuilder.toString();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("load sql " + sqlName + " failed");
            }
        }

        /*

         * Optional.ofNullable 该方法会判断传入的参数是否为 null，如果为 null 的话，返回的就是 Optional.empty()。
         *
         * orElseGet:当 Optional.ofNullable(SQL_CACHE.get(sqlName)) 为空时 ，触发 -> {这里的内容}
         * */

    /*
    lambda表达式写法
        //private static final Map<String, String> SQL_CACHE = new ConcurrentHashMap<>();
        return Optional.ofNullable(SQL_CACHE.get(sqlName)).orElseGet(() -> {
            //InputStream 是字节流
            try (InputStream in = this.getClass()
                    .getClassLoader()
                    //这个方法是用来获取配置文件的，方法传入的参数是一个路径
                    .getResourceAsStream("script/" + sqlName.substring(1) + ".sql");
                 // 从1 开始提取的原因是：sqlName: @query_order_by_account 去掉@符号
            ) {
                if (in == null) {
                    throw new RuntimeException("load sql " + sqlName + " failed");
                } else {
                    //InputStreamReader :字节流 通向字符流的桥梁
                    try (InputStreamReader isr = new InputStreamReader(in);
                         //BufferedReader -> 从字符输入流中读取文本并缓冲字符
                         BufferedReader reader = new BufferedReader(isr);) {
                        //lines方法--》BufferedReader中的line组成的Stream
                        String value = reader.lines()
                                .reduce((s1, s2) -> s1 + " " + s2)//为什么加空格 因为不加这里就是换行符了
                                .orElseThrow(() -> new RuntimeException("load sql " + sqlName + " failed"));
                        //System.out.println("value:" + value);
                        SQL_CACHE.put(sqlName, value);
                        return value;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("load sql " + sqlName + " failed");
            }
        });*/
}
