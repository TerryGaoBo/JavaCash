package servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Goods;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-11-25
 * Time: 13:06
 */
@WebServlet("/goods")
public class GoodsBrowseServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        Writer writer = resp.getWriter();

        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Goods> list = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            String sql = "select id,name,introduce,stock,unit," +
                    "price,discount from goods";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Goods goods = this.extractGoods(rs);
                if(goods != null) {
                    list.add(goods);
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            PrintWriter  pw     = resp.getWriter();
            //将list转化为json字符串，并将json数据填充到字符输出流中
            mapper.writeValue(pw, list);
            //System.out.println(pw.toString());
            writer.write(pw.toString());

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, rs);
        }
    }
    public Goods extractGoods(ResultSet resultSet) throws SQLException {
        Goods goods = new Goods();
        goods.setId(resultSet.getInt("id"));
        goods.setName(resultSet.getString("name"));
        goods.setIntroduce(resultSet.getString("introduce"));
        goods.setStock(resultSet.getInt("stock"));
        goods.setUnit(resultSet.getString("unit"));
        goods.setPrice(resultSet.getInt("price"));
        goods.setDiscount(resultSet.getInt("discount"));
        return goods;
    }
}
