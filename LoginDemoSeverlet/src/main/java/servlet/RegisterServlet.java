package servlet;

import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-11-24
 * Time: 16:37
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            String sql = "insert into account(username,password," +
                    "account_type,account_status) values(?,?,?,?)";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setInt(3,1);
            ps.setInt(4,1);
            resultSet = ps.getGeneratedKeys();
            ps.executeUpdate();
            /*if (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                ps.setInt(1,id);
            }*/
            resp.sendRedirect("login.html");
        } catch (Exception e) {
            //LOG.error("用户注册出错", e);
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, ps, null);
        }
    }
}