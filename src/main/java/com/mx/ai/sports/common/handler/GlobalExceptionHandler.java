package com.mx.ai.sports.common.handler;

import com.mx.ai.sports.common.entity.AiSportsResponse;
import com.mx.ai.sports.common.exception.AiSportsException;
import com.mx.ai.sports.common.exception.FileDownloadException;
import com.mx.ai.sports.common.exception.LimitAccessException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 统一处理请求参数校验
 *
 * @author Mengjiaxin
 * @date 2019-08-20 16:22
 */
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public AiSportsResponse handleException(Exception e) {
        log.error("系统内部异常，异常信息", e);
        return new AiSportsResponse().code(HttpStatus.INTERNAL_SERVER_ERROR).message("系统内部异常");
    }

    @ExceptionHandler(value = AiSportsException.class)
    public AiSportsResponse handleParamsInvalidException(AiSportsException e) {
        log.error("系统错误", e);
        return new AiSportsResponse().code(HttpStatus.INTERNAL_SERVER_ERROR).message(e.getMessage());
    }

    /**
     * http请求方式异常拦截
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public AiSportsResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("Http请求方式错误", e);
        return new AiSportsResponse().code(HttpStatus.METHOD_NOT_ALLOWED).message(e.getMessage());
    }


    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return AiSportsResponse
     */
    @ExceptionHandler(BindException.class)
    public AiSportsResponse validExceptionHandler(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new AiSportsResponse().code(HttpStatus.BAD_REQUEST).message(message.toString());
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return AiSportsResponse
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public AiSportsResponse handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new AiSportsResponse().code(HttpStatus.BAD_REQUEST).message(message.toString());
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public AiSportsResponse handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("MissingServletRequestParameterException", e);
        return new AiSportsResponse().code(HttpStatus.BAD_REQUEST).message(e.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public AiSportsResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        String message = Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage();
        return new AiSportsResponse().code(HttpStatus.BAD_REQUEST).message(message);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public AiSportsResponse handleMethodArgumentNotValidException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException", e);

        return new AiSportsResponse().code(HttpStatus.BAD_REQUEST).message("传入的JSON参数格式有误,具体信息:" + e.getMessage());
    }

    @ExceptionHandler(value = LimitAccessException.class)
    public AiSportsResponse handleLimitAccessException(LimitAccessException e) {
        log.error("LimitAccessException", e);
        return new AiSportsResponse().code(HttpStatus.TOO_MANY_REQUESTS).message(e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public AiSportsResponse handleUnauthorizedException(UnauthorizedException e) {
        log.error("UnauthorizedException", e);
        return new AiSportsResponse().code(HttpStatus.FORBIDDEN).message(e.getMessage());
    }

    @ExceptionHandler(value = ExpiredSessionException.class)
    public AiSportsResponse handleExpiredSessionException(ExpiredSessionException e) {
        log.error("ExpiredSessionException", e);
        return new AiSportsResponse().code(HttpStatus.UNAUTHORIZED).message(e.getMessage());
    }

    @ExceptionHandler(value = FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleFileDownloadException(FileDownloadException e) {
        log.error("FileDownloadException", e);
    }

    @ExceptionHandler(value = HttpMediaTypeNotSupportedException.class)
    public AiSportsResponse handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        log.error("HttpMediaTypeNotSupportedException", e);
        return new AiSportsResponse().code(HttpStatus.UNSUPPORTED_MEDIA_TYPE).message(e.getMessage());
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    public AiSportsResponse handleExpiredJwtException(ExpiredJwtException e) {
        log.error("ExpiredJwtException", e);
        return new AiSportsResponse().code(HttpStatus.UNAUTHORIZED).message("登录认证过期,需要重新登录!");
    }

}
