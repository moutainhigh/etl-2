package com.wangjunneil.schedule.activemq;

import com.wangjunneil.schedule.entity.common.Log;
import com.wangjunneil.schedule.service.SysFacadeService;
import com.wangjunneil.schedule.utility.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;

/**
 * Created by yangwanbin on 2016-12-14.
 */
public class MQTransportListener implements org.apache.activemq.transport.TransportListener {

    @Autowired
    private SysFacadeService sysFacadeService;

    private String   destination ;

    @Override
    public void onCommand(Object command) {
        setTransportFlag(true);
    }

    @Override
    public void onException(IOException error) {
        //System.out.println("MQTransportListener====onException====="+ DateTimeUtil.dateFormat(new Date(), "yyyyMMddHHmmss"));
              logEx(error);
    }

    @Override
    public void transportInterupted() {
        setTransportFlag(false);
    }

    @Override
    public void transportResumed() {
       setTransportFlag(true);
    }

    private  void logEx(IOException ex){
        //只记录第一次异常，否则心跳模式将无限制的记录异常
        switch (this.destination){
            case "topicDestinationWaiMaiOrder":   //推送外卖订单的 topic queue
                    if (StaticObj.mqTransportTopicOrder){
                        setTransportFlag(false);
                    }else  return;
                break;
            case "topicDestinationWaiMaiOrderStatus":   //推送订单状态的topic queue
                if (StaticObj.mqTransportTopicOrderStatus){
                    setTransportFlag(false);
                }else  return;
                break;
            case "topicDestinationWaiMaiOrderStatusAll":  //订单状态变，重新推送订单全信息
                if (StaticObj.mqTransportTopicOrderStatusAll){
                    setTransportFlag(false);
                }else  return;
                break;
            case "ekpDestinationException":
                if (StaticObj.mqTransportTopicException){
                    setTransportFlag(false);
                }else return;
            default:
                break;
         }
        Log log = null;
        if (ex == null){
            log = new Log();
            log.setLogId("MQ".concat(DateTimeUtil.dateFormat(DateTimeUtil.now(), "yyyyMMddHHmmssSSS")));
            log.setTitle("MQ服务器连接中断[".concat(this.destination).concat("]"));
            log.setDateTime(log.getDateTime());
            log.setMessage("消息生产者与主机失去连接，请检查MQ服务器是否宕机！");
            sysFacadeService.updSynLog(log);
        }
        else {
            Exception exception = (Exception) ex;
             log = sysFacadeService.functionRtn.apply(ex);
            log.setLogId("MQ".concat(log.getLogId()));
            log.setTitle("MQ服务器异常[".concat(this.destination).concat("]"));
            sysFacadeService.updSynLog(log);
        }
    }

    private void setTransportFlag(boolean boolValue){
        switch (this.destination){
            case "topicDestinationWaiMaiOrder":   //推送外卖订单的 topic queue
                if (StaticObj.mqTransportTopicOrder != boolValue)
                StaticObj.mqTransportTopicOrder = boolValue;
                break;
            case "topicDestinationWaiMaiOrderStatus":   //推送订单状态的topic queue
                if (StaticObj.mqTransportTopicOrderStatus != boolValue)
                    StaticObj.mqTransportTopicOrderStatus = boolValue;
                break;
            case "topicDestinationWaiMaiOrderStatusAll":
                if (StaticObj.mqTransportTopicOrderStatusAll != boolValue)
                    StaticObj.mqTransportTopicOrderStatusAll = boolValue;
                break;
            case "ekpDestinationException":
                if (StaticObj.mqTransportTopicException != boolValue)
                    StaticObj.mqTransportTopicException = boolValue;
                break;
            default:
                break;
        }
    }

    public void setDestination(String destination){
        this.destination = destination;
    }
}
