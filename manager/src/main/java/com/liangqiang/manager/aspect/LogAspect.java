package com.liangqiang.manager.aspect;

import com.liangqiang.manager.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class LogAspect {

    public static final String ERROR_PATH_METHOD_NAME = "getErrorPath";
    private static final String HEAD = "HEAD";
    private static final String REQUEST_START_TIME = "REQUEST_START_TIME";
    private static final String METHOD_NAME = "METHOD_NAME";
    private static final String PARAMETERS = "PARAMETERS";

    public void init(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) return;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        request.setAttribute(LogAspect.REQUEST_START_TIME, System.currentTimeMillis());
        request.setAttribute(LogAspect.METHOD_NAME, joinPoint.getSignature().getName());

        Object[] pointArgs = joinPoint.getArgs();
        List<Object> argList = new ArrayList<>();
        for (Object object : pointArgs) {
            if (!(object instanceof BindingResult)) {
                argList.add(object);
            }
        }
        request.setAttribute(LogAspect.PARAMETERS, argList.toString());
        String methodName = (String) request.getAttribute(LogAspect.METHOD_NAME);
        ServletRequestAttributes attributes = (ServletRequestAttributes) requestAttributes;
        if (!ignoreRequest(methodName, attributes.getRequest().getMethod())) {
            String url = attributes.getRequest().getRequestURL().toString();
            String httpMethod = attributes.getRequest().getMethod();
            log.info(String.format("request init: url[%s],httpMethod[%s],request[%s]", url, httpMethod, argList));
        }
    }

    public Object doTask(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    public void finishTask(Object ret) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) return;

        String response = "";
        if (Objects.nonNull(ret)) {
            response = ret.toString();
        }

        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        String methodName = (String) request.getAttribute(LogAspect.METHOD_NAME);
        String parameters = (String) request.getAttribute(LogAspect.PARAMETERS);
        Object startTime = request.getAttribute(LogAspect.REQUEST_START_TIME);
        request.removeAttribute(LogAspect.METHOD_NAME);
        request.removeAttribute(LogAspect.PARAMETERS);
        request.removeAttribute(LogAspect.REQUEST_START_TIME);
        if (ignoreRequest(methodName, methodName)) return;

        long requestStartTime = Objects.nonNull(startTime) ? (Long) startTime : System.currentTimeMillis();
        long requestFinishTime = System.currentTimeMillis();
        long cost = requestFinishTime - requestStartTime;

        HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();

        String url = servletRequest.getRequestURL().toString();
        String httpMethod = servletRequest.getMethod();
        String remoteHost = IPUtils.getIpAddress(servletRequest);
        int remotePort = servletRequest.getRemotePort();

        log.info(String.format("request cost: [%d],url[%s],httpMethod[%s],remoteHost[%s],remotePort[%d]," +
                "request[%s],response[%s]\n", cost, url, httpMethod, remoteHost, remotePort, parameters, response));
    }

    private boolean ignoreRequest(String methodName, String method) {
        return ERROR_PATH_METHOD_NAME.equalsIgnoreCase(methodName) || HEAD.equalsIgnoreCase(method);
    }


    @Pointcut("execution(public * com.liangqiang.manager.controller..*.*(..))")
    public void pointCutMethod() {
    }

    @Before("pointCutMethod()")
    public void doBefore(JoinPoint joinPoint) {
        init(joinPoint);
    }

    @Around("pointCutMethod()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        return doTask(joinPoint);
    }

    @AfterReturning(returning = "ret", pointcut = "pointCutMethod()")
    public void doAfterReturning(Object ret) {
        finishTask(ret);
    }
}
