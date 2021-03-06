package com.wangjunneil.schedule.entity.baidu;
import com.wangjunneil.schedule.common.Constants;
import com.wangjunneil.schedule.utility.StringUtil;
import  java.util.UUID;

/**
 * Created by yangwanbin on 2016-11-15.
 */
public class SysParams {
    private String cmd;      //命令
    private String timestamp;   //请求时间戳
    private String version = "3"; //api版本号,当前为2
    private String ticket;   //请求唯一标识
    private  String source;   //合作方帐号
    private  String sign;    //签名
    private String encrypt;  //是否加密,如AES,可为空
    private  Object body;    //请求参数
    private  String secret;

    public  void  setCmd(String cmd){
        this.cmd = cmd;
    }

    public String getCmd(){
        return  this.cmd;
    }

    public String getTimestamp(){
        if(StringUtil.isEmpty(this.timestamp)){
          this.timestamp = String.valueOf((int)(System.currentTimeMillis() / 1000));
        }
        return  timestamp;
    }

    public void setTimestamp(String timestamp){
        this.timestamp = timestamp;
    }

    public String getVersion(){
        return  this.version;
    }

    public  void setTicket(String ticket){

        this.ticket = ticket;
    }

    public String getTicket(){
        if(StringUtil.isEmpty(ticket)){ return UUID.randomUUID().toString().toUpperCase();}
        return  this.ticket;
    }

    public void setSource(String source){
       this.source = source;
    }

    public String getSource(){
        if(StringUtil.isEmpty(this.source)){
            this.source = Constants.BAIDU_SOURCE;
        }
        return  this.source;
    }

    public  void setSign(String sign){
         this.sign = sign;
    }

    public  String getSign(){
        return this.sign;
    }

    public void setEncrypt(String encrypt){
        this.encrypt = encrypt;
    }

    public String getEncrypt(){
        if(StringUtil.isEmpty(this.secret)){this.secret = Constants.BAIDU_SECRET;}
        return this.encrypt == null?"":this.encrypt;
    }

    public  void setBody(Object object){
       this.body = object;
    }

    public Object getBody(){
        return this.body;
    }

    public void  setSecret(String secret){
        this.secret = secret;
    }

    public String getSecret(){
        return StringUtil.isEmpty(this.secret)?Constants.BAIDU_SECRET:this.secret;
    }

}
