package com.wangjunneil.schedule.service.eleme;

import com.wangjunneil.schedule.common.Constants;
import com.wangjunneil.schedule.common.ScheduleException;
import com.wangjunneil.schedule.entity.baidu.Data;
import com.wangjunneil.schedule.entity.common.OrderWaiMai;
import com.wangjunneil.schedule.entity.eleme.Order;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/11/21.
 */
@Service
public class EleMeInnerService {
    private static Logger log = Logger.getLogger(EleMeInnerService.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    //insert or update
    public void addSyncOrder(Order order){
        Query  query = new Query(Criteria.where("orderid").is(order.getOrderid()));
        Update update = new Update()
            .set("restaurantid", order.getRestaurantid())
            .set("restaurantnumber", order.getRestaurantnumber())
            .set("restaurantname", order.getRestaurantname())
            .set("userid", order.getUserid())
            .set("username", order.getUsername())
            .set("consignee", order.getConsignee())
            .set("phonelist", order.getPhonelist())
            .set("address", order.getAddress())
            .set("deliverypoiaddress", order.getDeliverypoiaddress())
            .set("deliverygeo", order.getDeliverygeo())
            .set("invoiced", order.getInvoiced())
            .set("invoice", order.getInvoice())
            .set("isbook", order.getIsbook())
            .set("detail", order.getDetail())
            .set("originalprice", order.getOriginalprice())
            .set("servicefee", order.getServicefee())
            .set("deliverfee", order.getDeliverfee())
            .set("hongbao", order.getHongbao())
            .set("totalprice", order.getTotalprice())
            .set("restaurantpart", order.getRestaurantpart())
            .set("elemepart", order.getElemepart())
            .set("packagefee", order.getPackagefee())
            .set("createdat", order.getCreatedat())
            .set("activeat", order.getActiveat())
            .set("refundcode", order.getRefundcode())
            .set("statuscode", order.getStatuscode())
            .set("description", order.getDescription())
            .set("income", order.getIncome())
            .set("delivertime", order.getDelivertime())
            .set("servicerate", order.getServicerate())
            .set("isonlinepaid", order.getIsonlinepaid())
            .set("activitytotal", order.getActivitytotal())
            .set("distribution", order.getDistribution());
        mongoTemplate.upsert(query, update, Order.class);
    }

    //批量更新订单状态(根据订单号)
    public void updSyncElemeOrderStastus(String ids,int status) throws ScheduleException{
        Query query = new Query();
        Criteria criteria = new Criteria();
        List<String> listIds = new ArrayList<String>();
        Collections.addAll(listIds, ids.split(","));
        listIds.forEach((id)->{
            criteria.orOperator(Criteria.where("platformOrderId").is(id),Criteria.where("platform").is("eleme"));
        });
        query.addCriteria(criteria);
        Update update = new Update().set("order.statuscode",status);
        mongoTemplate.updateFirst(query, update, OrderWaiMai.class);
    }

    //批量更新配送状态(根据订单号)
    public int updSyncElemeDeliveryStastus(String ids,int i,int status, int zstatus){
        Query query = new Query();
        Criteria criteria = new Criteria();
        List<String> listIds = new ArrayList<String>();
        Collections.addAll(listIds, ids.split(","));
        listIds.forEach((id)->{
            criteria.orOperator(new Criteria().where("orderid").is(id));
        });
        query.addCriteria(criteria);
        Update update = new Update().set("distribution.records.get("+i+").statuscode",status).set("distribution.records.get(\"+i+\").sub_status_code", zstatus);
        return mongoTemplate.updateMulti(query,update,Order.class).getN();
    }

    //修改整个订单
    public int updateSysOrder(Order order){
        String orderId = String.valueOf(order.getOrderid());
        Query query = new Query(Criteria.where("platformOrderId").is(orderId).and("platform").is(Constants.PLATFORM_WAIMAI_ELEME));
        Update update = new Update().set("order",order);
        return mongoTemplate.updateFirst(query, update, OrderWaiMai.class).getN();
    }

    //多条件查询（完全匹配）
    public List<Order> findBodies(Map<String,Object[]> map) {

        Query query = new Query();
        Criteria criteria = new Criteria();

        map.forEach((s1, s2) -> {
            criteria.andOperator(new Criteria().where(s1).in(s2));
        });

        query.addCriteria(criteria);
        List<Order> bodies = mongoTemplate.find(query,Order.class);
        return  bodies;
    }

    //查询所有
    public List<Order> findAll(){
        Query query = new Query();
        List<Order> bodies = mongoTemplate.find(query,Order.class);
        return  bodies;
    }

}
