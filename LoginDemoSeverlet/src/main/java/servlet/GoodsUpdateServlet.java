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
 * Time: 13:08
 */
@WebServlet("/updategoods")
public class GoodsUpdateServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("商品更新函数");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String goodsId = req.getParameter("goodsID");
        String name = req.getParameter("name");
        String introduce = req.getParameter("introduce");
        String stock = req.getParameter("stock");
        String unit = req.getParameter("unit");

        String price =  req.getParameter("price");
        double doublePrice = Double.valueOf(price);
        int realPrice = new Double(100 * doublePrice).intValue();

        String discount = req.getParameter("discount");

        if(goodsId != null  && !goodsId.equals("")){
            Integer id = Integer.valueOf(goodsId);
            Goods goods = getGoods(id);
            if(goods == null) {
                System.out.println("没有该商品！");
                resp.sendRedirect("index.html");
            }else {
                goods.setName(name);
                goods.setIntroduce(introduce);
                goods.setStock(Integer.parseInt(stock));
                goods.setUnit(unit);
                goods.setPrice(realPrice);
                goods.setDiscount(Integer.parseInt(discount));
                boolean effect =  modifyGoods(goods);
                if(effect) {
                    resp.sendRedirect("goodsbrowse.html");
                    System.out.println("商品更新成功");
                }else {
                    System.out.println("商品更新失败");
                }
            }
        }
    }

    //更新商品
    public boolean modifyGoods(Goods goods) {
        Connection connection = null;
        PreparedStatement ps = null;
        boolean effect = false;

        try {
            connection = DBUtil.getConnection();
            String sql = "update goods set name=?," +
                    "introduce=?,stock=?,unit=?," +
                    "price=?,discount=? where id=?";
            ps=connection.prepareStatement(sql);
            ps.setString(1,goods.getName());
            ps.setString(2,goods.getIntroduce());
            ps.setInt(3,goods.getStock());
            ps.setString(4,goods.getUnit());
            ps.setInt(5,goods.getPrice());
            ps.setInt(6,goods.getDiscount());
            ps.setInt(7,goods.getId());
            effect = (ps.executeUpdate()==1);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.close(connection, ps, null);
        }
        return effect;
    }
    public Goods getGoods(int goodsId){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Goods goods = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select * from goods where id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,goodsId);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                goods = this.extractGoods(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return goods;
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
