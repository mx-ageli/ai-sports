package com.mx.ai.sports.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mx.ai.sports.AiSportsApplication;
import com.mx.ai.sports.course.entity.Run;
import com.mx.ai.sports.course.entity.RunLocation;
import com.mx.ai.sports.course.entity.RunPosition;
import com.mx.ai.sports.course.service.IRunLocationService;
import com.mx.ai.sports.course.service.IRunPositionService;
import com.mx.ai.sports.course.service.IRunService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AiSportsApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RunLocationUpdateTest {


    @Autowired
    private IRunLocationService runLocationService;

    @Autowired
    private IRunPositionService runPositionService;

    /**
     * 初始化课程数据
     */
    @Test
    public void updateRunLocation() {


        List<RunPosition> runPositionList = runPositionService.list();

        Map<Long, List<RunPosition>> runPositionMap = runPositionList.stream().collect(Collectors.groupingBy(RunPosition::getRunId));

        List<RunLocation> runLocationList = new ArrayList<>();

        runPositionMap.forEach((runId, positionList) ->{

            RunLocation location = new RunLocation();
            location.setRunId(runId);
            location.setLocation(JSON.toJSONString(runPositionList));

            runLocationList.add(location);
            if(runLocationList.size() > 200){
                runLocationService.save(location);
            }

        });


    }


}
