package com.cashsystem.cmd.impl.goods;

import com.cashsystem.cmd.Subject;
import com.cashsystem.cmd.annotation.AdminCommand;
import com.cashsystem.cmd.annotation.CommandMeta;
import com.cashsystem.cmd.impl.AbstractCommand;
import com.cashsystem.entity.Goods;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-08-04
 * Time: 11:42
 */
@CommandMeta(
        name = "SJSP",
        desc = "上架商品",
        group = "商品信息"
)
@AdminCommand
public class GoodsPutAwayCommand extends AbstractCommand {
    @Override
    public void execute(Subject subject) {
        System.out.println("上架商品");
        System.out.println("请输入商品名称：");
        String name = scanner.nextLine();
        printlnInfo("商品简介：");
        String introduce = scanner.nextLine();
        printlnInfo("商品库存：");
        //强转
        int stock = Integer.parseInt(scanner.nextLine());
        printlnInfo("商品单位：包，个，箱.....");
        String unit = scanner.nextLine();
        printlnInfo("请输入商品价格：单位（元），保留小数点2位");
        int price = new Double(100 * scanner.nextDouble()).intValue();
        System.out.println("请输入商品折扣：75表示75折");
        int discount = scanner.nextInt();

        Goods goods = new Goods();
        goods.setName(name);
        goods.setIntroduce(introduce);
        goods.setStock(stock);
        goods.setUnit(unit);
        goods.setPrice(price);
        goods.setDiscount(discount);

        boolean effect = this.goodsService.putAwayGoods(goods);
        if(effect) {
            System.out.println("上架成功");
        }else {
            System.out.println("上架失败");
        }


    }
}
