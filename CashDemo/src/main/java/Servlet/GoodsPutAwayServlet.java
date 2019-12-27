package Servlet;

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
 * Date: 2019-12-23
 * Time: 13:47
 */
@WebServlet("/inbound")
public class GoodsPutAwayServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");


        String name = req.getParameter("name");
        String introduce = req.getParameter("introduce");
        String stock = req.getParameter("stock");
        String unit =  req.getParameter("unit");

        String price =  req.getParameter("price");
        double doublePrice = Double.valueOf(price);
        //存储到数据为不是小数的
        int realPrice = new Double(100 * doublePrice).intValue();

        String discount =  req.getParameter("discount");

        Connection connection = null;
        PreparedStatement ps  = null;

        try {
            String sql = "insert into goods(name,introduce,stock,unit,price,discount)values(?,?,?,?,?,?)";
            connection = DBUtil.getConnection(true);
            ps = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,name);
            ps.setString(2,introduce);
            ps.setInt(3,Integer.parseInt(stock));
            ps.setString(4,unit);
            ps.setInt(5,realPrice);
            ps.setInt(6,Integer.parseInt(discount));
            ps.executeUpdate();
            resp.sendRedirect("index.html");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
