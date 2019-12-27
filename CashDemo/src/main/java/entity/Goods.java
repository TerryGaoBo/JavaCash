package entity;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-12-23
 * Time: 13:46
 */
@Data
public class Goods {
    private Integer id;
    private String name;
    private String introduce;
    private Integer stock;
    private String unit;
    private Integer price;
    private Integer discount;
    //用于购买商品后 更新库存
    private Integer buyGoodsNum;
    public double getPrice() {
        return price * 1.0 / 100;
    }

    public int getPriceInt() {
        return price;
    }
}
