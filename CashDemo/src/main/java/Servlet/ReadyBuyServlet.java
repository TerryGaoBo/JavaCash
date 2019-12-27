package Servlet;

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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-12-23
 * Time: 18:11
 */
@WebServlet("/pay")
public class ReadyBuyServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session=req.getSession();
        Account account = (Account) session.getAttribute("user");

        String goodsIDandNum = req.getParameter("goodsIDandNum");
        System.out.println(goodsIDandNum);
        //把所有需要购买的商品 存放至goodsList
        List<Goods> goodsList = new ArrayList<>();

        String[] strings = goodsIDandNum.split(",");

        for (String goodsString : strings) {
            //[0]="1"  [1] = "8";
            String[] str = goodsString.split("-");
            Goods goods = queryGoodsById(Integer.parseInt(str[0]));
            goods.setBuyGoodsNum(Integer.parseInt(str[1]));
            goodsList.add(goods);
        }

        Order order = new Order();
        order.setId(String.valueOf(System.currentTimeMillis()));
        order.setAccount_id(account.getId());
        order.setAccount_name(account.getUsername());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        order.setCreate_time(LocalDateTime.now().format(formatter));

        int totalMoney = 0;
        int actualMoney = 0;

        for (Goods goods : goodsList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setGoodsId(goods.getId());
            orderItem.setGoodsName(goods.getName());
            orderItem.setGoodsIntroduce(goods.getIntroduce());
            orderItem.setGoodsNum(goods.getBuyGoodsNum());
            orderItem.setGoodsUnit(goods.getUnit());
            orderItem.setGoodsPrice(goods.getPriceInt());
            orderItem.setGoodsDiscount(goods.getDiscount());
            order.orderItemList.add(orderItem);

            int currentMoney = goods.getBuyGoodsNum()*goods.getPriceInt();
            totalMoney += currentMoney;
            actualMoney += currentMoney*goods.getDiscount()/100;
        }
        order.setTotal_money(totalMoney);
        order.setActual_amount(actualMoney);
        order.setOrder_status(OrderStatus.PLAYING);

        //先进行展示，当前订单的明细：
        System.out.println(order);

        HttpSession httpSession = req.getSession(true);
        httpSession.setAttribute("order",order);

        httpSession.setAttribute("goodsList",goodsList);

        resp.getWriter().println("<html>");
        resp.getWriter().println("<p>"+"【用户名称】:"+order.getAccount_name()+"</p>");
        resp.getWriter().println("<p>"+"【订单编号】:"+order.getId()+"</p>");
        resp.getWriter().println("<p>"+"【订单状态】:"+order.getOrder_statusDesc().getDesc()+"</p>");
        resp.getWriter().println("<p>"+"【创建时间】:"+order.getCreate_time()+"</p>");

        resp.getWriter().println("<p>"+"编号  "+"名称   "+"数量  "+"单位  "+"单价（元）   "+"</p>");
        resp.getWriter().println("<ol>");
        for (OrderItem orderItem  : order.orderItemList) {
            resp.getWriter().println("<li>" + orderItem.getGoodsName() +" " + orderItem.getGoodsNum()+ " "+
                    orderItem.getGoodsUnit()+" " + orderItem.getGoodsPrice()+"</li>");
        }
        resp.getWriter().println("</ol>");
        resp.getWriter().println("<p>"+"【总金额】:"+order.getTotal_money() +"</p>");
        resp.getWriter().println("<p>"+"【优惠金额】:"+order.getDiscount() +"</p>");
        resp.getWriter().println("<p>"+"【应支付金额】:"+order.getActual_amount() +"</p>");

        //这个标签<a href = > 只会以get方式请求，所以buyGoodsServlet的 doGet方法
        resp.getWriter().println("<a href=\"buyGoodsServlet\">确认</a>");
        //resp.getWriter().println("<form action=\"buyGoodsServlet\" method=\"post\"><button type=\"submit\">确认</button></form>");

        resp.getWriter().println("<a href= \"index.html\">取消</a>");

        resp.getWriter().println("</html>");


    }


    //通过id查找 对应的货物
    private Goods queryGoodsById(Integer id) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = DBUtil.getConnection(true);
            String sql = "select * from goods where id=?";
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
