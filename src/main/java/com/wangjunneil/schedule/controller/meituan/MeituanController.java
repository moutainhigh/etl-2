package com.wangjunneil.schedule.controller.meituan;

import com.alibaba.fastjson.JSONObject;
import com.sankuai.meituan.waimai.opensdk.vo.FoodParam;
import com.wangjunneil.schedule.common.ScheduleException;
import com.wangjunneil.schedule.service.MeiTuanFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author liuxin
 * @since 2016-11-17.
 */
@Controller
@RequestMapping("/mt")
public class MeituanController {


    @Autowired
    private MeiTuanFacadeService mtFacadeService;


    //测试开店接口
    @RequestMapping(value = "/mtapi/openshop",method = RequestMethod.GET)
    public String openShop(PrintWriter out,HttpServletRequest resq, HttpServletResponse resp) throws ScheduleException{
        resp.setContentType("text/html;charset=utf-8");
        String code = "2063064";
        String params = mtFacadeService.openShop(code);
        out.println(params);
        out.close();
        return null;
    }

    //测试关店接口
    @RequestMapping(value = "/mtapi/closeshop",method = RequestMethod.GET)
    public String closeShop(PrintWriter out,HttpServletRequest resq, HttpServletResponse resp) throws ScheduleException{
        resp.setContentType("text/html;charset=utf-8");
        String code = "2063064";
        String params = mtFacadeService.closeShop(code);
        out.println(params);
        out.close();
        return params;
    }

    //测试商家确认订单接口
    @RequestMapping(value = "/mtapi/confirmOrder",method = RequestMethod.GET)
    public String confirmOrder(PrintWriter out,HttpServletRequest resq, HttpServletResponse resp) throws Exception{
        resp.setContentType("text/html;charset=utf-8");
        long order_id = 2815913527L;
        String params = mtFacadeService.getConfirmOrder(order_id);
        out.println(params);
        out.close();
        return null;
    }

    //测试商家取消订单接口
    @RequestMapping(value = "/mtapi/cancelOrder",method = RequestMethod.GET)
    public String cancelOrder(PrintWriter out,HttpServletRequest resq, HttpServletResponse resp) throws Exception{
        resp.setContentType("text/html;charset=utf-8");
        long order_id = 2704807726L;
        String reason = "因为菜买完了，所以暂不配送";
        String reason_code = "1001";
        String params = mtFacadeService.getCancelOrder(order_id,reason,reason_code);
        out.println(params);
        out.close();
        return null;
    }

    //测试商品批量上架接口
    @RequestMapping(value = "/mtapi/upFrame",method = RequestMethod.GET)
    public String upFrame(PrintWriter out,HttpServletRequest resq, HttpServletResponse resp) throws Exception
    {
        resp.setContentType("text/html;charset=utf-8");
        String app_poi_code = "7777";
        String params="";
        for(int i=0;i<2;i++){
            String arr[]={"food_code01112","food_code0111"};
            params =params+ mtFacadeService.upFrame(app_poi_code, arr[i])+",";
        }
        out.println(params.substring(0,params.length()-1));
        return null;
    }

    //测试商品批量下架接口
    @RequestMapping(value = "/mtapi/downFrame",method = RequestMethod.GET)
    public String downFrame(PrintWriter out,HttpServletRequest resq, HttpServletResponse resp) throws Exception
    {
        resp.setContentType("text/html;charset=utf-8");
        String app_poi_code = "7777";
        String params="";
        for (int i=0;i<2;i++){
            String arr[]={"food_code01112","food_code0111"};
            params = mtFacadeService.downFrame(app_poi_code,arr[i])+",";
        }
        out.println(params.substring(0,params.length()-1));
        return null;
    }

    //查询所有菜品信息,列表展示
    @RequestMapping(value = "/mtapi/foodList",method = RequestMethod.GET)
    public String foodList(PrintWriter out,HttpServletRequest resq,HttpServletResponse resp) throws Exception {
        resp.setContentType("text/html,charset=utf-8");
        String app_poi_code = "6666";
        String foodCode = "test_poi_01";
        FoodParam params = mtFacadeService.foodList(app_poi_code, foodCode);
        out.println(params);
        out.close();
        return null;
    }

    //查询门店信息
    @RequestMapping(value = "/mtapi/poiMyget",method = RequestMethod.GET)
    public String poiMyget(PrintWriter out,HttpServletRequest resq,HttpServletResponse resp) throws Exception {
        resp.setContentType("text/html;charset=utf-8");
        String app_poi_code = "7777";
        String params = mtFacadeService.findShopStatus(app_poi_code);
        out.println(params);
        out.close();
        return null;
    }

    //新增商品接口
    @RequestMapping(value = "/mtapi/createFood",method = RequestMethod.GET)
    public String createFood(PrintWriter out,HttpServletRequest resq, HttpServletResponse resp) throws Exception
    {
        resp.setContentType("text/html;charset=utf-8");
        FoodParam param = new FoodParam();
        param.setApp_poi_code("6666");
        //param.setApp_food_code("test_poi_03");
        param.setName("菜菜阿1233");

        param.setDescription("今天创建的");
        param.setPrice(15.5F);
        param.setMin_order_count(1);
        param.setUnit("份");
        param.setBox_num(1.0F);
        param.setBox_price(1.0F);
        param.setCategory_name("测试");
        param.setIs_sold_out(0);
        String params = mtFacadeService.createFood(param);
        out.println(params);
        out.close();
        return null;
    }

    //获取订单信息添加数据库
    @RequestMapping(value = "/mtapi/newOrder",method = RequestMethod.GET)
    public JSONObject newOrder(HttpServletRequest req, HttpServletResponse resp)throws Exception{
        resp.setContentType("text/html;charset=utf-8");
        long orderId = 2699113370L;
        //OrderDetailParam rtnJson = mtFacadeService.newOrder(orderId);
        return  null;
    }

}
