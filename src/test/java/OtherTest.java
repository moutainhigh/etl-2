import com.alibaba.fastjson.JSONObject;
import com.wangjunneil.schedule.service.EleMeFacadeService;
import com.wangjunneil.schedule.utility.DateTimeUtil;
import com.wangjunneil.schedule.utility.StringUtil;
import eleme.openapi.sdk.api.entity.order.OOrder;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016-11-15.
 */
public class OtherTest {


    public static void main(String[] args) {
       /* String aa = "body={\"errno\":0,\"error\":\"success\"}&cmd=resp.order.status.push&encrypt=&secret=46c4d76525131b37&source=30916&ticket=F72CCE4E-AA98-48EB-AA7C-CB1D052237C3&timestamp=1502504214&version=3";
        System.out.print(StringUtil.getMD5(aa));*/
            /*String promo = "5.76";
            String v1="18.18"; String v2="157.6";
            BigDecimal b = new BigDecimal(promo);
            BigDecimal b1 = new BigDecimal(v1);
            BigDecimal b2 = new BigDecimal(v2);*/
            String aa = "15618243416";
            System.out.print(aa.substring(0,11));
    }

}
