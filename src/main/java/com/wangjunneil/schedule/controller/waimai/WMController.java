package com.wangjunneil.schedule.controller.waimai;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.wangjunneil.schedule.activemq.Topic.TopicMessageProducer;
import com.wangjunneil.schedule.activemq.Topic.TopicMessageProducerAsync;
import com.wangjunneil.schedule.common.Constants;
import com.wangjunneil.schedule.common.ScheduleException;
import com.wangjunneil.schedule.entity.common.ParsFormPos2;
import com.wangjunneil.schedule.entity.common.ParsFromPos;
import com.wangjunneil.schedule.entity.common.ParsFromPosInner;
import com.wangjunneil.schedule.service.WMFacadeService;
import com.wangjunneil.schedule.utility.DateTimeUtil;
import com.wangjunneil.schedule.utility.StringUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.Destination;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Created by yangwanbin on 2016-11-14.
 */
@Controller
@MultipartConfig
@RequestMapping("/waimai")
public class WMController {

    private static  Logger logger = Logger.getLogger(WMController.class.getName());

    @Autowired
    private WMFacadeService wmFacadeService;

    @RequestMapping(value = {"/jdhome","/baidu","/eleme","/meituan","/jdhome/317481"})
    public String  appCallback(PrintWriter out,HttpServletRequest request, HttpServletResponse response)throws  Exception{

        String result = "",platform,requestUrl,sid = null;
        StringBuffer sb = new StringBuffer();
        JSONObject jsonObject = null;
        Map<String,String[]> stringMap = new HashMap<>();
        response.setContentType("application/json;charset=utf-8");
        requestUrl = request.getPathInfo().toLowerCase();
        if(requestUrl.indexOf("/jdhome/")> 0){
            sid = Pattern.compile("[^0-9]").matcher(requestUrl).replaceAll("");
            requestUrl = "/waimai/jdhome";
        }

        if(requestUrl.indexOf("/eleme")>0) {
            BufferedReader reader = request.getReader();
            String lines = "";
            while((lines=reader.readLine()) != null){
                lines = new String(lines.getBytes(), "utf-8");
                sb.append(lines);
            }
        }
        switch (requestUrl){
            case "/waimai/baidu":  //百度
                platform = Constants.PLATFORM_WAIMAI_BAIDU;
                stringMap = request.getParameterMap(); //Content-Type: application/x-www-form-urlencoded
                break;
            case "/waimai/jdhome": //京东到家
                String[] strArr = {sid};
                stringMap.put("sid",strArr);
                stringMap.putAll(request.getParameterMap());
                platform = Constants.PLATFORM_WAIMAI_JDHOME;
                break;
            case "/waimai/eleme":  //饿了么
                if(!StringUtil.isEmpty(sb) && sb.length()>0){
                    logger.info("=========饿了么推送数据==========："+sb.toString());
                    jsonObject = JSONObject.parseObject(sb.toString());
                }
                stringMap = request.getParameterMap();
                platform = Constants.PLATFORM_WAIMAI_ELEME;
                break;

            case "/waimai/meituan": //美团
                stringMap = request.getParameterMap();
                platform = Constants.PLATFORM_WAIMAI_MEITUAN;
                break;
            default:
                stringMap = request.getParameterMap();
                platform = null;
                break;
        }
        out.println(wmFacadeService.appReceiveCallBack(stringMap,platform,jsonObject));
        //out.print("{\"message\":\"ok\"}");
        //System.out.print(wmFacadeService.appReceiveCallBack(stringMap,platform));
        return  null;
    }
//region 商户

    /**
     * 新增门店
     * @parm  jsonStr 商铺信息
     * @param out   响应输出流对象
     * @param request 请求对象
     * @param response  浏览器响应对象
     * @return {code:0,desc:"success",dynamic:"",logId:"",remark:""}
     */
    @RequestMapping(value = "/shop/create",method = RequestMethod.POST,consumes = "application/json;charset=utf-8")
    @ResponseBody
    public String shopCreate(@RequestBody JsonObject jsonStr,PrintWriter out,HttpServletRequest request,HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.shopCreate(jsonStr));
        return  null;
    }

    /**
     * 门店开业
     *
     * @param out   响应输出流对象
     * @param request 请求对象 {baidu:[{shopId:"",platformShopId:""}],jdhome:[{}],meituan:[{}],eleme:[{}]}
     * @param response  浏览器响应对象
     * @return{baidu: [{code:0,desc:"success",dynamic:"",logId:""}],jdhome:[{}],...}]
     */
    @RequestMapping(value = "/shop/open", method = RequestMethod.POST,consumes="application/json;charset=utf-8")
    @ResponseBody
    public String shopOpen(@RequestBody ParsFromPos parsFromPos, PrintWriter out,HttpServletRequest request, HttpServletResponse response) throws ScheduleException {
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.shopOpen(parsFromPos));
        return  null;
    }


    /**
     * 门店歇业
     *
     * @param out   响应输出流对象
     * @param request 请求对象  {baidu:[{shopId:"",platformShopId:""}],jdhome:[{}],meituan:[{}],eleme:[{}]}
     * @param response  浏览器响应对象
     * @return  {baidu: [{code:0,desc:"success",dynamic:"",logId:""}],jdhome:[{}],...}]
     */
    @RequestMapping(value = "/shop/close", method = RequestMethod.POST,consumes = "application/json;charset=utf-8")
    @ResponseBody
    public String shopClose(@RequestBody(required = false) ParsFromPos parsFromPos, PrintWriter out,HttpServletRequest request, HttpServletResponse response) throws SchedulerException {
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.shopClose(parsFromPos));
        return  null;
    }

    /**
     * 查询门店状态
     * @param out   响应输出流对象
     * @param request 请求对象  {baidu:{shopId:""},jdhome:{shopId:""},meituan:{shopId:""},eleme:{shopId:""}}
     * @param response  浏览器响应对象
     * @return  {baidu: [{code:0,desc:"success",remark:"营业中",dynamic:""}],jdhome:[{code:1,desc:"success",remark:"休息中",dynamic:""}],...}
     */
    @RequestMapping(value = "/shop/getShopStatus", method = RequestMethod.POST,consumes = "application/json;charset=utf-8")
    @ResponseBody
    public String getStoreStatus(@RequestBody(required = false) ParsFromPos parsFromPos, PrintWriter out,HttpServletRequest request, HttpServletResponse response) throws SchedulerException {
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.getStoreStatus(parsFromPos));
        return  null;
    }

    /**
     * 修改商户信息
     *
     * @param out   响应输出流对象
     * @param response  浏览器响应对象
     * @return
     */
    @RequestMapping(value = "/shop/update",method = RequestMethod.POST)
    @ResponseBody
    public String shopUpdate( @RequestBody JsonObject jsonObject,PrintWriter out,HttpServletRequest request,HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        //request.getParameter("minBuyFree");
        //request.getParameter("minOrderPrice");
        out.println(wmFacadeService.shopUpdate(jsonObject));
        return null;
    }

//endregion
//region 商品
    /**
     * 新增菜品分类
     * @param jsonObj 菜品分类
     * @param out 响应输出流队形
     * @param request 请求对象
     * @param
     * @return {"baidu":{code:0,desc:"success",dynamic:"",logId:"",remark:""},"jdhome":{}...}
     */
    @RequestMapping(value = "/dish/category/create",method = RequestMethod.POST,consumes = "application/json;charset=utf-8")
    @ResponseBody
    public String dishCategoryCreate(@RequestBody JsonObject jsonObj,PrintWriter out,HttpServletRequest request,HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.dishCategoryCreate(jsonObj));
        return  null;
    }

    /**
     * 新增菜品
     * @param jsonObj 菜品信息
     * @param out 响应输出流队形
     * @param request 请求对象
     * @param
     * @return  {"baidu":{code:0,desc:"success",dynamic:"",logId:"",remark:""},"jdhome":{}...}
     */
    @RequestMapping(value = "/dish/create",method = RequestMethod.POST,consumes = "application/json;charset=utf-8")
    @ResponseBody
    public String dishCreate(@RequestBody JsonObject jsonObj,PrintWriter out,HttpServletRequest request,HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.dishCreate(jsonObj));
        return null;
    }

    /**
     * 修改菜品
     * @param parsFromPosInner 菜品信息
     * @param out 响应输出流队形
     * @param request 请求对象
     * @param
     * @return  {"baidu":{code:0,desc:"success",dynamic:"",logId:"",remark:""},"jdhome":{}...}
     */
    @RequestMapping(value = "/dish/update",method = RequestMethod.POST,consumes = "application/json;charset=utf-8")
    @ResponseBody
    public String dishUpdate(@RequestBody ParsFromPosInner parsFromPosInner, PrintWriter out, HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.dishUpdate(parsFromPosInner));
        return null;
    }

    /**
     * 菜品查看
     *@param parsFromPos
     * @param out   响应输出流对象
     * @param request {baidu:[{shopId:"",platformShopId:"",dishId:"",platformDishId:""}],jdhome:[],eleme:[],meituan:[]}
     * @param response  浏览器响应对象
     * @return
     */
    @RequestMapping(value = {"/dish/get"},method = RequestMethod.POST,consumes = "application/json;charset=utf-8")
    @ResponseBody
    public String dishGet(@RequestBody ParsFromPos parsFromPos,PrintWriter out, HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.dishGet(parsFromPos));
        return null;
    }
    /**
     * 菜品上架
     *@param parsFormPos2
     * @param out   响应输出流对象
     * @param request {baidu:[{shopId:"",platformShopId:"",dishId:"",platformDishId:""}],jdhome:[],eleme:[],meituan:[]}
     * @param response  浏览器响应对象
     * @return
     */
    @RequestMapping(value = "/dish/online", method = RequestMethod.POST,consumes="application/json;charset=utf-8" )
    @ResponseBody
    public String dishOnline(@RequestBody ParsFormPos2 parsFormPos2, PrintWriter out, HttpServletRequest request, HttpServletResponse response)  {
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.dishOnline(parsFormPos2));
        return  null;
    }

    /**
     * 菜品下架
     *@param parsFormPos2
     * @param out   响应输出流对象
     * @param request {baidu:[{shopId:"",platformShopId:"",dishId:"",platformDishId:""}],jdhome:[],eleme:[],meituan:[]}
     * @param response  浏览器响应对象
     * @return
     */
    @RequestMapping(value = "/dish/offline", method = RequestMethod.POST,consumes = "application/json;charset=utf-8")
    @ResponseBody
    public String dishOffline(@RequestBody ParsFormPos2 parsFormPos2, PrintWriter out,HttpServletRequest request, HttpServletResponse response)  {
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.dishOffline(parsFormPos2));
        return  null;
    }

    /**
     * 批量查询门店商品状态
     * @param parsFormPos2
     * @param out 响应输出流对象
     * @param request {baidu:[{shopId:"",dishId:""},{}...],jdhome:[{shopId:""}],eleme:[{shopId:""}],meituan:[{shopId:""}]}
     * @param response 浏览器响应对象
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/dish/getStatus",method = RequestMethod.POST,consumes = "application/json;charset=utf-8")
    public String getDishStatus(@RequestBody ParsFormPos2 parsFormPos2, PrintWriter out,HttpServletRequest request, HttpServletResponse response){
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.getDishStatus(parsFormPos2));
        return null;
    }

//endregion


//region 订单

    /**
     * 获取订单【消息型】 平台推送的订单
     *
     * @param out   响应输出流对象
     * @param response  浏览器请求对象
     * @return
     */
    @RequestMapping(value = {"/baidu/order/new","/jdhome/317481/djsw/newOrder"}, method = {RequestMethod.GET,RequestMethod.POST})
    public String orderPost(PrintWriter out, HttpServletRequest request,HttpServletResponse response) {
        String result = null, requestUrl, platform = null, sid = null;
        requestUrl = request.getPathInfo().toLowerCase();
        if (requestUrl.indexOf("/jdhome/") > 0) {
            //sid = requestUrl.split("\\/").length>2?requestUrl.split("\\/")[2]:null;
            sid = Pattern.compile("[^0-9]").matcher(requestUrl).replaceAll("");
            requestUrl = "/waimai/jdhome";
        }
        Map<String,String[]> stringMap = new HashMap<String,String[]>();
        switch (requestUrl) {
            case "/waimai/baidu/order/new": //百度
                platform = Constants.PLATFORM_WAIMAI_BAIDU;
                response.setContentType("text/html; charset=utf-8");
                stringMap = request.getParameterMap();
                break;
            case "/waimai/jdhome": //京东到家
                platform = Constants.PLATFORM_WAIMAI_JDHOME;
                response.setContentType("text/html; charset=utf-8");
                String[] strArr = {sid};
                stringMap.put("sid",strArr);
                stringMap.putAll(request.getParameterMap());
                break;
            default:
                response.setContentType("text/html; charset=utf-8");
                stringMap = request.getParameterMap();
                break;
        }
        if (!StringUtil.isEmpty(platform)) {
            result = wmFacadeService.orderPost(stringMap, platform);
        }
        out.println(result);
        return  null;
    }
    /**
     * 订单状态推送【消息型】 平台推送订单的状态变更
     *
     * @param out   响应输出流对象
     * @param request  浏览器请求对象
     * @return
     */
    @RequestMapping(value = {"/baidu/order/status","/meituan/order/status","/djsw/userCancelOrder","/djsw/deliveryOrder","/djsw/applyCancelOrder",
        "/djsw/finishOrder","/djsw/orderWaitOutStore"},method = {RequestMethod.GET,RequestMethod.POST})
    public String orderStatus(PrintWriter out,HttpServletRequest request,HttpServletResponse response){
        String platform = null;
        String requestUrl = request.getPathInfo().toLowerCase();
        if (requestUrl.indexOf("/djsw/") > 0) {
            requestUrl = "/waimai/djsw";
        }
        switch (requestUrl){
            case "/waimai/baidu/order/status"://百度
                platform = Constants.PLATFORM_WAIMAI_BAIDU;
                response.setContentType("text/html; charset=utf-8");
                break;
            case "/waimai/djsw": //京东到家
                response.setContentType("text/html; charset=utf-8");
                platform = Constants.PLATFORM_WAIMAI_JDHOME;
                break;
            case "/waimai/meituan/order/status": //美团
                response.setContentType("text/html; charset=utf-8");
                platform = Constants.PLATFORM_WAIMAI_MEITUAN;
                break;
            default:break;
        }
         out.println( wmFacadeService.orderStatus(request.getParameterMap(),platform));
        return  null;
    }

    /**
     * 配送订单状态推送【消息型】 平台推送配送订单状态
     *
     * @param out   响应输出流对象
     * @param request  浏览器请求对象
     * @return
     */
    @RequestMapping(value = {"/meituan/order/delivery","/djsw/pushDeliveryStatus"},method = {RequestMethod.GET,RequestMethod.POST})
    public String orderDelivery(PrintWriter out,HttpServletRequest request,HttpServletResponse response){
        response.setContentType("text/html;charset=utf-8");
        String platform = null;
        String requestUrl = request.getPathInfo().toLowerCase();
        if (requestUrl.indexOf("/djsw/") > 0) {
            requestUrl = "/waimai/djsw";
        }
        switch (requestUrl){
            case "/waimai/meituan/order/delivery": //美团
                response.setContentType("text/html; charset=utf-8");
                platform = Constants.PLATFORM_WAIMAI_MEITUAN;
                break;
            case "/waimai/djsw"://京东
                response.setContentType("text/html; charset=utf-8");
                platform = Constants.PLATFORM_WAIMAI_JDHOME;
                break;
            default:break;
        }
        out.println( wmFacadeService.orderDelivery(request.getParameterMap(), platform));
        return  null;
    }

    /**
     * 查询订单状态
     * @param out   响应输出流对象
     * @param request 请求对象  {baidu:{"platformOrderId":"","shopId":""},jdhome:{"platformOrderId":"","shopId":""},meituan:{"platformOrderId":""},eleme:{"platformOrderId":""}}
     * @param response  浏览器响应对象
     * @return  {baidu:{code:0,desc:"success",platformOrderId:"",orderStatus:""},jdhome:{code:0,desc:"success",platformOrderId:"",orderStatus:""},...}
     */
    @RequestMapping(value = "/shop/getOrderStatus", method = RequestMethod.POST,consumes = "application/json;charset=utf-8")
    @ResponseBody
    public String getOrderStatus(@RequestBody(required = false) ParsFromPos parsFromPos, PrintWriter out,HttpServletRequest request, HttpServletResponse response) throws SchedulerException {
        response.setContentType("application/json;charset=utf-8");
        out.println(wmFacadeService.getOrderStatus(parsFromPos));
        return  null;
    }

    /**
     * 获取订单【主动抓取】 抓取平台订单
     *
     * @param out   响应输出流对象
     * @param response  浏览器响应对象
     *
     *
     * @return
     */
    @RequestMapping(value = "/orderGet.php", method = RequestMethod.GET)
    public String orderGet(PrintWriter out, HttpServletResponse response) throws SchedulerException {
        out.println("");
        return  null;
    }

    /**
     * 确认订单
     * @param out   响应输出流对象
     * @param response  浏览器响应对象
     * @request request 浏览器请求对象 约定格式：{baidu:{platformOrderId:"0001,0002",shopId:""},jdhome:{platformOrderId:"0003,0004",shopId:""},meituan:{},eleme:{}}
     * @return
     */
    @RequestMapping(value = "/order/confirm", method = RequestMethod.POST,consumes="application/json;charset=utf-8")
    public String orderConfirm(@RequestBody ParsFromPos parsFromPos, PrintWriter out,HttpServletRequest request, HttpServletResponse response) throws SchedulerException {
        //response.setContentType("application/json;charset=uft-8");
        String reponseStr = wmFacadeService.orderConfirm(parsFromPos);
        out.println(reponseStr);
        return  null;
    }

    /**
     * 取消订单
     *
     * @param out   响应输出流对象
     * @request request 浏览器请求对象 约定格式：{baidu:{platOrderId:"0001,0002",shopId:"",reason:"",reasonCode:""},jdhome:{platOrderId:"0003,0004",shopId:"",reason:"",reasonCode:""},meituan:{},eleme:{}}
     * @param response  浏览器响应对象
     * @return
     */
    @RequestMapping(value = "/order/cancel", method = RequestMethod.POST,consumes="application/json;charset=utf-8")
    @ResponseBody
    public String orderCancel(@RequestBody ParsFromPos parsFromPos,PrintWriter out,HttpServletRequest request, HttpServletResponse response) throws SchedulerException {
        //response.setContentType("application/json;charset=uft-8");
        out.println(wmFacadeService.orderCancel(parsFromPos));
        return  null;
    }


    /**
     * 获取平台code authToken
     * @param out
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = {"/eleme/callBack"})
    public String getCodeAndAuthToken(PrintWriter out,HttpServletRequest request, HttpServletResponse response){
        String result = "",platform,requestUrl;
        Map<String,String[]> stringMap = new HashMap<>();
        response.setContentType("application/json;charset=utf-8");
        requestUrl = request.getPathInfo().toLowerCase();
        switch (requestUrl){
            case "/waimai/eleme/callback":  //饿了么
                stringMap = request.getParameterMap();
                platform = Constants.PLATFORM_WAIMAI_ELEME;
                break;
            default:
                stringMap = request.getParameterMap();
                platform = null;
                break;
        }
        out.println(wmFacadeService.getAuthToken(stringMap,platform));
        return  null;
    }



//endregion

    //region  Other
    /**
     * 获取供应商信息
     * @param out   响应输出流对象
     * @param response  浏览器响应对象
     * @return
     */
    @RequestMapping(value = "/baidu/getSupplier",method = RequestMethod.GET)
    public String getSupplierList(PrintWriter out,HttpServletRequest request,HttpServletResponse response){
        response.setContentType("application/json;charset=uft-8");
        out.println(wmFacadeService.getSupplier());
        return null;
    }

    @Autowired
    @Qualifier("topicMessageProducerWaiMaiOrder")
    private TopicMessageProducer topicMessageProducerWaiMaiOrder;

    @Autowired
    @Qualifier("topicDestinationWaiMaiOrder")
    private Destination topicDestinationWaiMaiOrder;

    @Autowired
    @Qualifier("topicMessageProducerWaiMaiOrderAsync")
    private TopicMessageProducerAsync topicDestinationWaiMaiOrderAsync;

    @RequestMapping(value = "/test1",method = RequestMethod.GET)
    public String test1(PrintWriter out,HttpServletRequest request, HttpServletResponse response){
        try {
            response.setContentType("text/html; charset=utf-8");
        //  topicMessageProducerWaiMaiOrder.sendMessage(topicDestinationWaiMaiOrder,"杨大山,你辛苦了:"+ DateTimeUtil.dateFormat(new Date(), "yyyyMMddHHmmss"),"6666");
           topicDestinationWaiMaiOrderAsync.init("杨大山,你辛苦了1:"+ DateTimeUtil.dateFormat(new Date(), "yyyyMMddHHmmss"),"6666" );
          new Thread(topicDestinationWaiMaiOrderAsync).start();
            topicDestinationWaiMaiOrderAsync.init("杨大山,你辛苦了2:"+ DateTimeUtil.dateFormat(new Date(), "yyyyMMddHHmmss"),"6665" );
            new Thread(topicDestinationWaiMaiOrderAsync).start();
            out.println("测试MQ");
        }catch (Exception ex){

        }
        return null;
    }

    //endregion

    //备注：需要提供接口用于中台系统下发门店编码对照信息


}
