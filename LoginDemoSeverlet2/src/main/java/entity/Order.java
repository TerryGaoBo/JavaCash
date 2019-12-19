package entity;

import common.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-12-07
 * Time: 12:01
 */
@Data
public class Order {
    private String id;
    private Integer account_id;
    private String account_name;
    private String create_time;
    private String finish_time;
    private Integer actual_amount;
    private Integer total_money;
    private OrderStatus order_status;
    //存放订单项的 list
    public List<OrderItem> orderItemList = new ArrayList<>();

    public double getTotal_money() {
        return total_money * 1.0 / 100;
    }

    public int getTotal_money_fen() {
        return total_money;
    }

    public double getActual_amount() {
        return actual_amount * 1.0/100;
    }

    public OrderStatus getOrder_statusDesc() {
        return order_status;
    }

    public String getOrder_status() {
        return order_status.getDesc();
    }
    public int getActual_amount_fen() {
        return actual_amount;
    }



    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("【订单信息】*************************************").append("\n");
        sb.append("\t").append("【用户名称】：").append(this.getAccount_name()).append("\n");
        sb.append("\t").append("【订单编号】：").append(this.getId()).append("\n");
        sb.append("\t").append("【订单状态】：").append(this.getOrder_status()).append("\n");
        sb.append("\t").append("【创建时间】：").append( this.getCreate_time()).append("\n");
        if (this.getOrder_statusDesc().equals(OrderStatus.OK.getDesc())) {
            sb.append("\t").append("【完成时间】：")
                    .append( this.getFinish_time()).append("\n");
        }
        sb.append("【订单明细】*************************************").append("\n");
        sb.append("\t编号   名称     数量     单位     单价（元）").append("\n");
        int i = 0;
        for (OrderItem orderItem : this.getOrderItemList()) {
            sb.append("\t").append(++i).append(".  ")
                    .append(orderItem.getGoodsName()).append("   ")
                    .append(orderItem.getGoodsNum()).append("   ")
                    .append(orderItem.getGoodsUnit()).append("  ")
                    .append(orderItem.getGoodsPrice()).append("  ")
                    .append("\n");
        }
        sb.append("【订单金额】*************************************").append("\n");
        sb.append("\t").append("【总金额】：").append( this.getTotal_money())
                .append(" 元 ").append("\n");
        sb.append("\t").append("【优惠金额】：").append( this.getDiscount() )
                .append(" 元 ").append("\n");
        sb.append("\t").append("【应支付金额】：").append( this.getActual_amount() )
                .append(" 元 ").append("\n");
        return sb.toString();
    }


    //优惠
    public double getDiscount() {
        return (this.getTotal_money_fen() - this.getActual_amount_fen())*1.00 / 100;
    }
}
