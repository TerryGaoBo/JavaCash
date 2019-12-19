package servlet;

import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-11-25
 * Time: 13:07
 */
@WebServlet("/inbound")
public class GoodsPutAwayServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("bound");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String introduce = req.getParameter("introduce");
        String stock = req.getParameter("stock");
        String unit =  req.getParameter("unit");

        String price =  req.getParameter("price");
        double doublePrice = Double.valueOf(price);
        int realPrice = new Double(100 * doublePrice).intValue();

        String discount =  req.getParameter("discount");

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        try {
            String sql = "insert into " +
                    "goods(name,introduce,stock,unit,price,discount)" +
                    "values(?,?,?,?,?,?)";
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
            resp.sendRedirect("inbound.html");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, ps, null);
        }
    }
}
