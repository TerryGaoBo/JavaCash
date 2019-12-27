## 基于字符界面的收银台系统

### 1. 简介

该系统是一款纯字符界面的收银台系统，实现了，商品管理，浏览，下单，支付功能。

### 2. 背景

在掌握了JavaSE的基本知识，以及MySQL数据库，JDBC编程，为了能够将所学知识结合起来，但是又不具备前端知识的情况下，实现的一款收银台系统。

### 3. 意义

+ JavaSE知识的实践
+ 培养数据库编程的能力
+ 锻炼应用已有技术解决实际问题
+ 培养业务分析到技术实现的能力

### 4. 核心需求

+ 登录注册
+ 管理端
    + 用户管理
    + 商品管理
+ 用户端
    + 商品浏览
    + 订单管理
    + 下单支付

### 5. 功能列表

#### 5.1  公共端

```
1. 入口命令
	1.1 登录(DL)
	1.2 注册(ZC)
2. 公共命令
	2.1 帮助信息(BZ)
	2.2 关于系统(GY)
	2.3 退出系统(TC)
```
#### 5.2 管理端

```
1. 帐号信息
	1.1 启停帐号(QTZH)
	1.2 重置密码(CZMM)
	1.3 查看帐号(CKZH)
2. 商品信息
	2.1 上架商品(SJSP)
	2.2 下架商品(XJSP)
	2.3 浏览商品(LLSP)
	2.4 更新商品(GXSP)
3. 公共命令
	3.1 帮助信息(BZ)
	3.2 关于系统(GY)
	3.3 退出系统(TC)
4. 我的信息
	4.1 修改密码(XGMM)
	4.2 个人信息(GRXX)
```

#### 5.3 用户端

```
1. 商品信息
	1.1 浏览商品(LLSP)
2. 公共命令
	2.1 帮助信息(BZ)
	2.2 退出系统(TC)
	2.3 关于系统(GY)
3. 我的订单
	3.1 取消订单(QXDD)
	3.2 支付订单(ZFDD)
	3.3 查看订单(CKDD)
4. 我的信息
	5.1 修改密码(XGMM)
	5.2 个人信息(GRXX)
```

### 6. 涉及技术

+ 集合框架
+ 注解技术
+ Stream式编程
+ MySQL数据库
+ JDBC编程

### 7. 技术实现

#### 7.1 数据库设计

![](./assets/ti4oss_db1.png)

``` sql
drop table if exists `account`;
create table if not exists `account`
(
    id             int primary key auto_increment comment '帐号编号',
    username       varchar(12)   not null comment '帐号',
    password       varchar(128)  not null comment '密码',
    name           varchar(32)   not null comment '姓名',
    account_type   int default 1 not null comment '帐号类型 1 管理员 2 客户',
    account_status int default 1 not null comment '帐号状态 1 启用   2 锁定'
);

-- 商品信息
drop table if exists `goods`;
create table if not exists `goods`
(
    id        int primary key auto_increment comment '商品编号',
    name      varchar(128)              not null comment '商品名称',
    introduce varchar(256) default '暂无' not null comment '商品简介',
    stock     int                       not null comment '商品库存',
    unit      varchar(12)               not null comment '库存单位',
    price     int                       not null comment '商品价格，单位：分',
    discount  int          default 100  not null comment '商品折扣，[0,100]'
);
-- 订单
drop table if exists `order`;
create table if not exists `order`
(
    id            varchar(32) primary key comment '订单编号',
    account_id    int         not null comment '帐号编号',
    account_name  varchar(12) not null comment '帐号',
    create_time   datetime    not null comment '创建时间',
    finish_time   datetime default null comment '完成时间',
    actual_amount int         not null comment '实际金额，单位：分',
    total_money   int         not null comment '总金额，单位：分',
    order_status  int         not null comment '支付状态 1 待支付 2 完成'
);
-- 订单项
drop table if exists `order_item`;
create table if not exists `order_item`
(
    id              int primary key auto_increment comment '订单条目编号',
    order_id        varchar(32)               not null comment '订单编号',
    goods_id        int                       not null comment '商品编号',
    goods_name      varchar(128)              not null comment '商品名称',
    goods_introduce varchar(256) default '暂无' not null comment '商品简介',
    goods_num       int                       not null comment '商品数量',
    goods_unit      varchar(12)               not null comment '库存单位',
    goods_price     int                       not null comment '商品价格，单位：分',
    goods_discount  int          default 100  not null comment '商品折扣，[0,100]'
);

```

数据库设计中`订单表`一条记录对应`订单项表`中n条记录，属于1对n的关系。

#### 7.2 分层设计

![](./assets/ti4oss_chart.png)



#### 7.3：使用datasource的方法建立到MySQL数据的连接

<https://blog.csdn.net/qq_39385118/article/details/81088546> 

<https://blog.csdn.net/bestree007/article/details/8762174> 

### 8. 授课指南

#### 8.1 授课计划

总课时预估：5节

+ 第1节前期准备：需求分析，功能整理，模型抽象，数据库表设计，项目搭建
+ 第2节项目实现：命令行字符界面设计，搭建；实体类设计
+ 第3节项目实现：账号和商品的客户端层，业务层和数据库实现
+ 第4节项目实现：订单的客户端层，业务层和数据库层实现
+ 第5节项目收尾：功能测试，BUG修复，扩展点梳理，考察点梳理

#### 8.2 授课思路

本项目属于一个业务型的项目，功能比较多，但是难度中等。

+ 实体类，数据库访问层，服务层，客户端层分别带领学生代码实现
+ 学生自己实现剩余类似代码
+ 订单管理，下单支付详细讲解和实现

#### 8.3 面试考察点

+ 数据库表设计中每个表之间的关系
+ 订单和子订单关系
+ 订单总金额和实际金额的计算
+ 数据库事务控制

### 9、项目效果

#### 9.1:客户端

![](./assets/1.png)

![](./assets/2.png)

![](./assets/3.png)

![](./assets/4.png)

![](./assets/5.png)

![](./assets/6.png)

![](./assets/7.png)

![](./assets/8.png)

#### 9.2管理员端

![](./assets/ti4osspng/1.png)

![](./assets/ti4osspng/2.png)

![](./assets/ti4osspng/3.png)

![](./assets/ti4osspng/4.png)

![](./assets/ti4osspng/5.png)

![](./assets/ti4osspng/6.png)

![](./assets/ti4osspng/7.png)

![](./assets/ti4osspng/8.png)