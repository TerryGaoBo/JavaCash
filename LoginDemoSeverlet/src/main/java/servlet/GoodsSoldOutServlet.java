package servlet;

import entity.Goods;
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
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-11-25
 * Time: 13:07
 */
@WebServlet("/delGoods")
public class GoodsSoldOutServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String str = req.getParameter("id");
        System.out.println("下架："+str);
        if(str != null  && !str.equals("")){
            Integer id = Integer.valueOf(str);
            Goods goods = queryGoodsById(id);
            if(goods == null) {
                System.out.println("没有该商品！");
            }else {
                boolean effect = deleteGoods(id);
                if (effect) {
                    System.out.println("商品成功下架");
                } else {
                    System.out.println("商品下架失败，稍后重试");
                }
            }
        }
    }

    //通过id查找 对应的货物
    public Goods queryGoodsById(Integer id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DBUtil.getConnection(true);
            String sql = "select id, name, introduce, stock, unit, price, discount from goods where  id =?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return extractGoods(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, ps, rs);
        }
        return null;
    }

    //下架商品
    public boolean deleteGoods(Integer goodsId) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DBUtil.getConnection(true);
            String sql = "delete  from goods where  id=?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, goodsId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection, ps, rs);
        }
        return false;
    }

    //将集合汇中的数据进行解析
    private Goods extractGoods(ResultSet resultSet) throws SQLException {
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
