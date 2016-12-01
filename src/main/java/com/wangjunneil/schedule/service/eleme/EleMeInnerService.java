package com.wangjunneil.schedule.service.eleme;

import com.wangjunneil.schedule.entity.baidu.Body;
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

/**
 * Created by admin on 2016/11/21.
 */
@Service
public class EleMeInnerService {
    private static Logger log = Logger.getLogger(EleMeInnerService.class.getName());

    @Autowired
    private MongoTemplate mongoTemplate;

    public void AddSyncBaiDuOrder(Order order){


    }

    //批量更新订单状态(根据订单号)
    public  int updSyncElemeOrderStastus(String ids,int status){

        Query query = new Query();
        Criteria criteria = new Criteria();
        List<String> listIds = new ArrayList<String>();
        Collections.addAll(listIds, ids.split(","));
        listIds.forEach((id)->{
            criteria.orOperator(new Criteria().where("order_id").is(id));
        });
        query.addCriteria(criteria);
        Update update = new Update().set("order.$.status",status);
        return mongoTemplate.updateMulti(query,update,Order.class).getN();
    }

    //新订单接收
    public String getNewOrder(String eleme_order_ids){

        return null;
    }
    //订单状态变更接收   new_status：订单状态
    public String orderChange(String eleme_order_ids,int new_status){

        return null;
    }
    //退单状态接收  refund_status:退单订单状态
    public String chargeBack(String eleme_order_ids,int refund_status){

        return  null;
    }
    //订单配送状态接收
    public String distributionStatus(String eleme_order_ids,int status_code,int sub_status_code){

        return  null;
    }
}
