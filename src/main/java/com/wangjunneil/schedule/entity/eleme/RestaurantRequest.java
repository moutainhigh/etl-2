package com.wangjunneil.schedule.entity.eleme;

import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 2016/11/28.
 */
public class RestaurantRequest {
    @SerializedName("restaurant_id")
    private String restaurant_id;
    //开关店铺
    @SerializedName("is_open")
    private String is_open;
    //餐厅地址
    @SerializedName("address_text")
    private String address_text;
    //经纬度用,隔开
    @SerializedName("geo")
    private String geo;
    //配送费
    @SerializedName("agent_fee")
    private String agent_fee;
    //关闭描述
    @SerializedName("close_description")
    private String close_description;
    //配送额外说明
    @SerializedName("deliver_description")
    private String deliver_description;
    //餐厅简介
    @SerializedName("description")
    private String description;
    //餐厅名字
    @SerializedName("name")
    private String name;
    //是否接受预定
    @SerializedName("is_bookable")
    private String is_bookable;
    //餐厅营业时间，多个时间段用逗号隔开
    @SerializedName("open_time")
    private String open_time;
    //餐厅联系号码
    @SerializedName("phone")
    private String phone;
    //餐厅公告
    @SerializedName("promotion_info")
    private String promotion_info;
    //餐厅logo
    @SerializedName("logo_image_hash")
    private String logo_image_hash;
    //是否支持开发票
    @SerializedName("invoice")
    private String invoice;
    //支持的最小发票金额
    @SerializedName("invoice_min_amount")
    private String invoice_min_amount;
    //满xx元免配送费
    @SerializedName("no_agent_fee_total")
    private String no_agent_fee_total;
    //餐厅是否有效
    @SerializedName("is_valid")
    private String is_valid;
    //订单打包费
    @SerializedName("packing_fee")
    private String packing_fee;
    @SerializedName("tp_id")
    private String tp_id;
    @SerializedName("tp_restaurant_id")
    private String tp_restaurant_id;
    @SerializedName("restaurant_ids")
    private String restaurant_ids;

    public String getRestaurant_ids() {
        return restaurant_ids;
    }

    public void setRestaurant_ids(String restaurant_ids) {
        this.restaurant_ids = restaurant_ids;
    }

    public String getRestaurant_id() {
        return restaurant_id;
    }

    public void setRestaurant_id(String restaurant_id) {
        this.restaurant_id = restaurant_id;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public String getAddress_text() {
        return address_text;
    }

    public void setAddress_text(String address_text) {
        this.address_text = address_text;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public String getAgent_fee() {
        return agent_fee;
    }

    public void setAgent_fee(String agent_fee) {
        this.agent_fee = agent_fee;
    }

    public String getClose_description() {
        return close_description;
    }

    public void setClose_description(String close_description) {
        this.close_description = close_description;
    }

    public String getDeliver_description() {
        return deliver_description;
    }

    public void setDeliver_description(String deliver_description) {
        this.deliver_description = deliver_description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIs_bookable() {
        return is_bookable;
    }

    public void setIs_bookable(String is_bookable) {
        this.is_bookable = is_bookable;
    }

    public String getOpen_time() {
        return open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPromotion_info() {
        return promotion_info;
    }

    public void setPromotion_info(String promotion_info) {
        this.promotion_info = promotion_info;
    }

    public String getLogo_image_hash() {
        return logo_image_hash;
    }

    public void setLogo_image_hash(String logo_image_hash) {
        this.logo_image_hash = logo_image_hash;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getInvoice_min_amount() {
        return invoice_min_amount;
    }

    public void setInvoice_min_amount(String invoice_min_amount) {
        this.invoice_min_amount = invoice_min_amount;
    }

    public String getNo_agent_fee_total() {
        return no_agent_fee_total;
    }

    public void setNo_agent_fee_total(String no_agent_fee_total) {
        this.no_agent_fee_total = no_agent_fee_total;
    }

    public String getIs_valid() {
        return is_valid;
    }

    public void setIs_valid(String is_valid) {
        this.is_valid = is_valid;
    }

    public String getPacking_fee() {
        return packing_fee;
    }

    public void setPacking_fee(String packing_fee) {
        this.packing_fee = packing_fee;
    }

    public String getTp_id() {
        return tp_id;
    }

    public void setTp_id(String tp_id) {
        this.tp_id = tp_id;
    }

    public String getTp_restaurant_id() {
        return tp_restaurant_id;
    }

    public void setTp_restaurant_id(String tp_restaurant_id) {
        this.tp_restaurant_id = tp_restaurant_id;
    }
}
