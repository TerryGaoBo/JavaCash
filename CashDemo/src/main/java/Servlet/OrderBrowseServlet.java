package Servlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import common.OrderStatus;
import entity.Account;
import entity.Order;
import entity.OrderItem;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-12-23
 * Time: 18:43
 */
@WebServlet("/orderbrowse")
public class OrderBrowseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        Writer writer = resp.getWriter();

        HttpSession session=req.getSession();
        Account account = (Account) session.getAttribute("user");

        List<Order> orders = queryOrderByAccount(account.getId());

        if(orders.isEmpty()) {
            System.out.println("订单链表为空");
        }else {
            ObjectMapper mapper = new ObjectMapper();
            PrintWriter  pw     = resp.getWriter();
            //将list转化为json字符串，并将json数据填充到字符输出流中
            mapper.writeValue(pw, orders);
            System.out.println(pw.toString());
            writer.write(pw.toString());
        }
    }
    //查询订单  数据拔出来写入List
    public List<Order> queryOrderByAccount(Integer accountId) {
        List<Order> orderList = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBUtil.getConnection(false);

            String sql = this.getSql("@query_order_by_account");

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,accountId);

            resultSet = preparedStatement.executeQuery();

            Order order = null;

            while (resultSet.next()) {
                //第一次进入 先生成一张订单
                if(order == null) {
                    order = new Order();
                    this.extractOrder(order,resultSet);
                    orderList.add(order);
                }
                //拿到了当前的orderId
                String orderId = resultSet.getString("order_id");
                //只用当订单信息不同的时候 我们才会生成一个订单
                //订单对象 只有一个 因为其中包含了 很多的订单信息
                //如果为每一个订单信息 都生成一个订单时不合理的

                if (!orderId.equals(order.getId())) {
                    order = new Order();
                    this.extractOrder(order,resultSet);
                    orderList.add(order);
                }

                //往当前订单内添加 具体的订单项 orderItem
                OrderItem orderItem = this.extractOrderItem(resultSet);
                order.getOrderItemList().add(orderItem);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            if(connection != null) {
                try {
                    //回滚
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return orderList;
    }

    public void extractOrder(Order order, ResultSet resultSet) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        order.setId(resultSet.getString("order_id"));
        order.setAccount_id(resultSet.getInt("account_id"));
        order.setAccount_name(resultSet.getString("account_name"));
        order.setCreate_time(resultSet.getString("create_time"));

        Timestamp finishTime = resultSet.getTimestamp("finish_time");

        if (finishTime != null) {
            order.setFinish_time(finishTime.toLocalDateTime().format(formatter));
        }

        order.setActual_amount(resultSet.getInt("actual_amount"));
        order.setTotal_money(resultSet.getInt("total_money"));

        order.setOrder_status(OrderStatus.valueOf(resultSet.getInt("order_status")));



    }


    public OrderItem extractOrderItem(ResultSet resultSet) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(resultSet.getInt("item_id"));
        orderItem.setGoodsId(resultSet.getInt("goods_id"));
        orderItem.setGoodsName(resultSet.getString("goods_name"));
        orderItem.setGoodsIntroduce(resultSet.getString("goods_introduce"));
        orderItem.setGoodsNum(resultSet.getInt("goods_num"));
        orderItem.setGoodsUnit(resultSet.getString("goods_unit"));


        orderItem.setGoodsPrice(resultSet.getInt("goods_price"));
        orderItem.setGoodsDiscount(resultSet.getInt("goods_discount"));
        return orderItem;
    }

    private String getSql(String sqlName) {
        System.out.println("=====sqlName:"+sqlName);
        //InputStream 是字节流
        try (InputStream in = this.getClass()
                .getClassLoader()
                //这个方法是用来获取配置文件的，方法传入的参数是一个路径
                .getResourceAsStream("script/" + sqlName.substring(1) + ".sql");
             // 从1 开始提取的原因是：sqlName: @query_order_by_account 去掉@符号
        ) {
            if (in == null) {
                throw new RuntimeException("load sql " + sqlName + " failed");
            } else {
                //InputStreamReader :字节流 通向字符流的桥梁 读的是字节
                try (InputStreamReader isr = new InputStreamReader(in);
                     //BufferedReader -> 从字符输入流中读取文本并缓冲字符
                     BufferedReader reader = new BufferedReader(isr)) {

                    StringBuilder stringBuilder = new StringBuilder();

                    stringBuilder.append(reader.readLine());

                    String line;
                    while (( line = reader.readLine()) != null) {
                        stringBuilder.append(" ").append(line);
                    }

                    //System.out.println("value:" + stringBuilder.toString());
                    return stringBuilder.toString();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("load sql " + sqlName + " failed");
        }
    }
}
