package com.mx.ai.sports.common.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.mx.ai.sports.common.configure.SmsConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 短信发送的工具类
 *
 * @author Mengjiaxin
 * @date 2019/11/12 4:14 下午
 */
@Service
public class SmsUtil {

    @Autowired
    private SmsConfigProperties smsConfigProperties;

    /**
     * 短信验证码发送
     *
     * @return
     * @throws ClientException
     */
    public String sendCode(String mobile, String code) throws ClientException {
        DefaultProfile profile = DefaultProfile.getProfile(smsConfigProperties.getRegionId(), smsConfigProperties.getAccessKeyId(), smsConfigProperties.getAccessKeySecret());
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", smsConfigProperties.getRegionId());
        request.putQueryParameter("PhoneNumbers", mobile);
        request.putQueryParameter("SignName", smsConfigProperties.getSignName());
        request.putQueryParameter("TemplateCode", smsConfigProperties.getTemplateCode());
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        CommonResponse response = client.getCommonResponse(request);
        return response.getData();
    }


}
