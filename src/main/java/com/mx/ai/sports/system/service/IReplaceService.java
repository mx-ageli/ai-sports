package com.mx.ai.sports.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mx.ai.sports.system.entity.Replace;
import com.mx.ai.sports.system.vo.ReplaceVo;

/**
 * 版本更新service
 * @author Mengjiaxin
 * @date 2020/9/9 2:29 下午
 */
public interface IReplaceService extends IService<Replace> {


    ReplaceVo findByLast(String type);

}
