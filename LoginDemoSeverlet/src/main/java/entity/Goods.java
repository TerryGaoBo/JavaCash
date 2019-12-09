package entity;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-11-27
 * Time: 16:09
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
    private Integer buyGoodsNum;

    public double getPrice() {
        return price * 1.0 / 100;
    }

    public int getPrice_fen() {
        return price;
    }
}