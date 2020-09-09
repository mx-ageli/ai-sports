package com.mx.ai.sports.system.service.impl;

import cn.jiguang.common.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mx.ai.sports.common.configure.JPushConfigProperties;
import com.mx.ai.sports.common.entity.MsgStatusEnum;
import com.mx.ai.sports.common.entity.MsgTypeEnum;
import com.mx.ai.sports.common.entity.QueryRequest;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.course.entity.Course;
import com.mx.ai.sports.course.service.ICourseStudentService;
import com.mx.ai.sports.system.entity.Message;
import com.mx.ai.sports.system.entity.User;
import com.mx.ai.sports.system.entity.UserMessage;
import com.mx.ai.sports.system.mapper.MessageMapper;
import com.mx.ai.sports.system.service.IMessageService;
import com.mx.ai.sports.system.service.IUserMessageService;
import com.mx.ai.sports.system.service.IUserService;
import com.mx.ai.sports.system.vo.MessageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 消息service
 *
 * @author Mengjiaxin
 * @date 2020/9/8 6:28 下午
 */
@Service
@Slf4j
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Autowired
    private IUserMessageService userMessageService;

    @Autowired
    private IUserService userService;

    @Autowired
    private JPushConfigProperties jPushConfigProperties;

    @Autowired
    private ICourseStudentService courseStudentService;


    @Override
    public IPage<MessageVo> findMyMessage(QueryRequest query, Long userId) {
        Page<MessageVo> page = new Page<>(query.getPageNum(), query.getPageSize());

        return this.baseMapper.findMyMessage(page, userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveSendMessage(Message message, List<Long> userIds) {
        // 先保存消息
        this.save(message);

        List<UserMessage> userMessages = new ArrayList<>();
        userIds.forEach(userId -> {
            UserMessage userMessage = new UserMessage();
            userMessage.setMessageId(message.getMessageId());
            userMessage.setUserId(userId);
            userMessages.add(userMessage);
        });
        return userMessageService.saveBatch(userMessages);
    }


    /**
     * 老师创建课程后给当前学校的所有学生推送消息
     *
     * @param userId 老师Id
     * @param course 课程信息
     * @return
     */
    @Override
    public Boolean courseAddPush(Long userId, Course course) throws AiSportsException {

        User user = userService.getById(userId);
        // 查询这个老师对应的学校下面所有的学生
        List<User> studentList = userService.findStudentBySchoolId(user.getSchoolId());
        // 如果没有学生直接返回
        if (CollectionUtils.isEmpty(studentList)) {
            return Boolean.TRUE;
        }
        // 推送的设备Id
        List<String> deviceIds = studentList.stream().map(User::getDeviceId).distinct().collect(Collectors.toList());
        // 推送的学生Id
        List<Long> userIds = studentList.stream().map(User::getUserId).collect(Collectors.toList());

        Map<String, String> extras = new HashMap<>(2);
        extras.put("courseId", course.getCourseId().toString());
        extras.put("messageType", MsgTypeEnum.COURSE_PUBLISH.value());

        String title = "有新的课程发布啦！";
        String content = title + user.getFullName() + "老师发布了【" + course.getCourseName() + "】课程，快去报名吧！";
        // 推送系统消息
        sendMessage(deviceIds, content, extras);
        // 保存系统消息
        saveSysMessage(course, userIds, title, content, MsgTypeEnum.COURSE_PUBLISH.value());

        return Boolean.TRUE;
    }

    @Override
    public Boolean courseStartPush(Course course) throws AiSportsException {

        User user = userService.getById(course.getUserId());
        // 先查询这次报课的学生有那些
        List<User> studentList = courseStudentService.findUserByCourseId(course.getCourseId());
        // 如果没有学生直接返回
        if (CollectionUtils.isEmpty(studentList)) {
            return Boolean.TRUE;
        }
        // 推送的设备Id
        List<String> deviceIds = studentList.stream().map(User::getDeviceId).distinct().collect(Collectors.toList());
        // 推送的学生Id
        List<Long> userIds = studentList.stream().map(User::getUserId).collect(Collectors.toList());

        Map<String, String> extras = new HashMap<>(2);
        extras.put("courseId", course.getCourseId().toString());
        extras.put("messageType", MsgTypeEnum.COURSE_START.value());

        String title = "课程快要开始了！";
        String content = user.getFullName() + "老师发布的【" + course.getCourseName() + "】课程，马上就要开始了！快去打卡吧！";
        // 推送系统消息
        sendMessage(deviceIds, content, extras);
        // 保存系统消息
        saveSysMessage(course, userIds, title, content, MsgTypeEnum.COURSE_START.value());

        return Boolean.TRUE;
    }

    /**
     * 发送消息
     *
     * @param deviceIds
     * @param content
     * @param extras
     * @return
     * @throws AiSportsException
     */
    public Boolean sendMessage(List<String> deviceIds, String content, Map<String, String> extras) throws AiSportsException {

        try {
            ClientConfig config = ClientConfig.getInstance();

            JPushClient jpushClient = new JPushClient(jPushConfigProperties.getMasterSecret(), jPushConfigProperties.getAppKey(), null, config);

            cn.jpush.api.push.model.Message message = cn.jpush.api.push.model.Message.newBuilder().setMsgContent(content)
                    .addExtras(extras).build();
            PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
                    .setAudience(Audience.registrationId(deviceIds))
                    .setNotification(Notification.newBuilder().setAlert(content).build())
                    .setMessage(message).build();

            PushResult result = jpushClient.sendPush(payload);

            log.info("消息推送成功! content：{}, extras:{}, result:{}", content, extras, result);
        } catch (APIConnectionException e) {
            log.error("推送服务：连接错误. ", e);

            throw new AiSportsException("推送服务：连接错误.");
        } catch (APIRequestException e) {
            log.error("JPush服务器的错误响应。.", e);
            log.error("HTTP Status: " + e.getStatus());
            log.error("Error Code: " + e.getErrorCode());
            log.error("Error Message: " + e.getErrorMessage());
            log.error("Msg ID: " + e.getMsgId());

            throw new AiSportsException("JPush服务器的错误响应.");
        }

        return Boolean.TRUE;
    }

    /**
     * 保存系统消息
     *
     * @param course
     * @param userIds
     * @param title
     * @param content
     */
    private void saveSysMessage(Course course, List<Long> userIds, String title, String content, String messageType) {
        // 保存推送的系统消息
        Message msg = new Message();
        msg.setCourseId(course.getCourseId());
        msg.setContent(content);
        msg.setTitle(title);
        msg.setCreateTime(new Date());
        // 先默认设置为已读
        msg.setStatus(MsgStatusEnum.READ.value());
        msg.setType(messageType);
        // 保存系统推送的消息
        this.saveSendMessage(msg, userIds);
    }
}
