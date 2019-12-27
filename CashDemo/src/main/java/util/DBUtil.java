package util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-12-22
 * Time: 15:31
 */
public class DBUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/cash";
    /**
     * 数据库用户名
     */
    private static final String USERNAME = "root";

    /**
     * 数据库密码
     */
    private static final String PASSWORD = "111111";
    /**
     * 数据库连接池
     */
    private static volatile DataSource DATASOURCE;
    /**
     * 获取数据库连接池：单例获取
     * @return
     */
    private static DataSource getDataSource(){
        // 双重校验锁
        if(DATASOURCE == null){
            synchronized (DBUtil.class){
                if(DATASOURCE == null){
                    DATASOURCE = new MysqlDataSource();
                    ((MysqlDataSource) DATASOURCE).setUrl(URL);
                    ((MysqlDataSource) DATASOURCE).setUser(USERNAME);
                    ((MysqlDataSource) DATASOURCE).setPassword(PASSWORD);
                }
            }
        }
        return DATASOURCE;
    }

    /**
     * 根据数据库连接池获取连接
     * @return
     */
    public static Connection getConnection(boolean autoCommit){
        try {
            //从池子里获取连接
            Connection connection = getDataSource().getConnection();
            //如果true  每写一条语句 自动进行提交
            connection.setAutoCommit(autoCommit);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("获取数据库连接失败");
        }
    }
    public static void close(Connection connection,
                             PreparedStatement preparedStatement, ResultSet resultSet){
        try {
            if(connection != null)
                connection.close();
            if(preparedStatement != null)
                preparedStatement.close();
            if(resultSet != null)
                resultSet.close();
        }catch (Exception e){
            throw new RuntimeException("数据库释放资源出错", e);
        }
    }
}
