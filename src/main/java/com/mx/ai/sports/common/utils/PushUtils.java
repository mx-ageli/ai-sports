package com.mx.ai.sports.common.utils;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.ServiceHelper;
import cn.jiguang.common.connection.NettyHttpClient;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jiguang.common.resp.ResponseWrapper;
import cn.jpush.api.JPushClient;
import cn.jpush.api.device.OnlineStatus;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * 极光推送工具类
 * @author Mengjiaxin
 * @date 2020/3/10 3:11 下午
 */
@Slf4j
public class PushUtils {

    private static final String APP_KEY = "c7610c3e196114d160f3e8ac";

    private static final String MASTER_SECRET = "92a46aae6d26194e8f82a79c";

    private static final String REGISTRATION_ID1 = "160a3797c856951a397";
    private static final String REGISTRATION_ID2 = "0a04ad7d8b4";

    public static final String ALERT = "Ai体育";

    private static JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY);

    public static void testGetUserOnlineStatus() {
        try {
            Map<String, OnlineStatus> result =  jpushClient.getUserOnlineStatus(REGISTRATION_ID1, REGISTRATION_ID2);

            log.info(result.get(REGISTRATION_ID1).toString());
            log.info(result.get(REGISTRATION_ID2).toString());
        } catch (APIConnectionException e) {
            log.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            log.error("Error response from JPush server. Should review and fix it. ", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
        }
    }

    // 使用 NettyHttpClient 异步接口发送请求
    public static void testSendPushWithCallback() {
        ClientConfig clientConfig = ClientConfig.getInstance();
        String host = (String) clientConfig.get(ClientConfig.PUSH_HOST_NAME);
        final NettyHttpClient client = new NettyHttpClient(ServiceHelper.getBasicAuthorization(APP_KEY, MASTER_SECRET),
                null, clientConfig);
        try {
            URI uri = new URI(host + clientConfig.get(ClientConfig.PUSH_PATH));
            PushPayload payload = buildPushObject_all_alias_alert();
            client.sendRequest(HttpMethod.POST, payload.toString(), uri, new NettyHttpClient.BaseCallback() {
                @Override
                public void onSucceed(ResponseWrapper responseWrapper) {
                    log.info("Got result: " + responseWrapper.responseContent);
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static PushPayload buildPushObject_all_alias_alert() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias("alias1"))
                .setNotification(Notification.alert(ALERT))
                .build();
    }

    public static void main(String[] args) {
        testSendPushWithCallback();

    }



}
