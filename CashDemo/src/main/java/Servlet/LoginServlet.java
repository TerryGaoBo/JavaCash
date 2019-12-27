package Servlet;

import entity.Account;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-12-22
 * Time: 15:23
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        Writer writer = resp.getWriter();

        Connection connection = null;
        PreparedStatement ps  = null;
        ResultSet rs = null;


        try {
            String sql = "select id,username,password " +
                    "from account where username=?and password=?";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2,password);
            rs = ps.executeQuery();
            Account user = new Account();
            while(rs.next()){
                Integer id = rs.getInt("id");
                user.setId(id);
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
            }
            if(user.getId()==null) {
                writer.write("<h2>没有该用户：" + username + "</h2>");
            }else if(!password.equals(user.getPassword())){
                writer.write("<h2>用户名或密码错误："+username + "</h2>");
            }else {
                writer.write("<h2>登录成功："+username + "</h2>");
                HttpSession session = req.getSession(true);
                session.setAttribute("user", user);
                resp.sendRedirect("index.html");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, ps, rs);
        }
    }

}
