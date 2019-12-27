package com.cashsystem.service;

import com.cashsystem.dao.GoodsDao;
import com.cashsystem.entity.Goods;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: GAOBO
 * Date: 2019-08-06
 * Time: 15:45
 */
public class GoodsService {

    public GoodsDao goodsDao;
    public GoodsService() {
        this.goodsDao = new GoodsDao();
    }

    public List<Goods> quarryAllGoods() {
        return this.goodsDao.quarryAllGoods();
    }
    //上架商品
    public boolean putAwayGoods(Goods goods) {
        return this.goodsDao.putAwayGoods(goods);
    }
    //通过goodsId 拿到对应的货物
    public  Goods getGoods(int goodsId){
        return this.goodsDao.getGoods(goodsId);
    }
    //更新商品
    public boolean modifyGoods(Goods goods) {
        return this.goodsDao.modifyGoods(goods);
    }
    public boolean updateAfterPay(Goods goods,int goodsNum){
        return this.goodsDao.updateAfterPay(goods,goodsNum);
    }
}
