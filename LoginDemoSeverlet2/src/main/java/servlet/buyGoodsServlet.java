package servlet;

import common.OrderStatus;
import entity.Account;
import entity.Goods;
import entity.Order;
import entity.OrderItem;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-12-07
 * Time: 20:19
 */
@WebServlet("/buyGoodsServlet")
public class buyGoodsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("===============");
        HttpSession session=req.getSession();
        Order order = (Order) session.getAttribute("order");
        List<Goods> goodsList = (List<Goods>) session.getAttribute("goodsList");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        order.setFinish_time(LocalDateTime.now().format(formatter));

        order.setOrder_status(OrderStatus.OK);


        boolean effect = commitOrder(order);

        if(effect) {//插入订单和订单项成功
            for (Goods goods : goodsList) {
                System.out.println("数量："+goods.getBuyGoodsNum());
                boolean isUpdate =
                        updateAfterPay(goods,goods.getBuyGoodsNum());
                if(isUpdate) {
                    System.out.println("库存更新成功！");

                }else {
                    System.out.println("库存更新失败！");
                }
            }
            resp.sendRedirect("buyGoodsSuccess.html");
        }
    }
    //購買成功后，更新库存
    private boolean updateAfterPay(Goods goods,int goodsBuyNum){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean effect = false;
        try {
            connection = DBUtil.getConnection(true);
            String sql = "update goods set stock=? where id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,
                    goods.getStock()-goodsBuyNum);
            preparedStatement.setInt(2,goods.getId());

            if(preparedStatement.executeUpdate() == 1){
                effect = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return effect;
    }
    private boolean commitOrder(Order order) {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = DBUtil.getConnection(false);
            String insertOrderSql = "insert into `order`" +
                    "(id, account_id, create_time, finish_time, " +
                    "actual_amount, total_money, order_status, " +
                    "account_name) values (?,?,now(),now(),?,?,?,?)";
            String insertOrderItemSql = "insert into order_item(order_id, goods_id, goods_name," +
                    "goods_introduce, goods_num, goods_unit," +
                    " goods_price, goods_discount) " +
                    "values (?,?,?,?,?,?,?,?)";

            ps = connection.prepareStatement(insertOrderSql);
            ps.setString(1,order.getId());
            ps.setInt(2,order.getAccount_id());
            ps.setInt(3,order.getActual_amount_fen());
            //ps.setInt(4,order.getTotal_money_fen());
            ps.setInt(4,order.getTotal_money_fen());

            ps.setInt(5,order.getOrder_statusDesc().getFlg());
            ps.setString(6,order.getAccount_name());

            if(ps.executeUpdate() == 0) {
                throw new RuntimeException("插入订单失败");
            }
            //开始插入订单项
            ps = connection.prepareStatement(
                    insertOrderItemSql);
            for (OrderItem orderItem : order.orderItemList) {
                ps.setString(1,orderItem.getOrderId());
                ps.setInt(2,orderItem.getGoodsId());
                ps.setString(3,orderItem.getGoodsName());
                ps.setString(4,orderItem.getGoodsIntroduce());
                ps.setInt(5,orderItem.getGoodsNum());
                ps.setString(6,orderItem.getGoodsUnit());
                ps.setInt(7,orderItem.getGoodsPrice_fen());
                ps.setInt(8,orderItem.getGoodsDiscount());
                //将每一项preparedStatement 缓存好
                ps.addBatch();
            }
            //批量操作数据库
            int[] effects = ps.executeBatch();
            for (int i : effects) {
                if(i == 0) {
                    throw new RuntimeException("插入订单明细失败！");
                }
            }
            //手动提交事务
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();

            if(connection != null) {
                try {
                    //回滚
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            return false;
        }finally {
            DBUtil.close(connection, ps,null);
        }
        return true;
    }
}
