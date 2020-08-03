package com.mx.ai.sports.common.utils;

import com.alibaba.fastjson.JSON;
import com.mx.ai.sports.common.exception.AiSportsException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 渲染工具类
 * @author Mengjiaxin
 * @date 2019-08-20 16:27
 */
public class RenderUtil {

    /**
     * 渲染json对象
     */
    public static void renderJson(HttpServletResponse response, Object jsonObject) throws AiSportsException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(jsonObject));
        } catch (IOException e) {
            throw new AiSportsException("渲染界面错误");
        }
    }
}
