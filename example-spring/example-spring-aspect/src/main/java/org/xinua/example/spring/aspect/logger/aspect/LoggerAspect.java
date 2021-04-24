package org.xinua.example.spring.aspect.logger.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.xinua.example.spring.aspect.logger.annotation.Level;
import org.xinua.example.spring.aspect.logger.annotation.LogError;
import org.xinua.example.spring.aspect.logger.annotation.LogParam;
import org.xinua.example.spring.aspect.logger.annotation.LogResult;
import org.xinua.example.spring.aspect.logger.annotation.LogTime;
import org.xinua.example.spring.aspect.logger.annotation.Logging;
import org.xinua.example.spring.aspect.logger.util.StringUtil;

import java.text.MessageFormat;

@Aspect
@Component
@Lazy(false)
public class LoggerAspect {

    private static final Logger logger = LoggerFactory.getLogger("logger aspect");

    @Around("@annotation(logging)")
    public Object logging(ProceedingJoinPoint joinPoint, Logging logging) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] args = joinPoint.getArgs();

        LogParam[] logParams = logging.logParam();
        LogResult[] logResults = logging.logResult();
        LogError[] logErrors = logging.logError();
        LogTime[] logTimes = logging.logTime();

        String defaultPrefix = joinPoint.getSignature().getDeclaringTypeName() + "." + methodName;

        //打印参数
        if (logParams != null && logParams.length > 0) {
            LogParam logParam = logParams[0];
            String logParamPrefix = StringUtil.notEmpty(logParam.prefix()) ? logParam.prefix() : defaultPrefix;
            log(logParam.level(), MessageFormat.format("{0}   params    :  {1}", logParamPrefix, getParamString(parameterNames, args)));
        }

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();

            //打印返回结果
            if (logResults != null && logResults.length > 0) {
                LogResult logResult = logResults[0];
                String logResultPrefix = StringUtil.notEmpty(logResult.prefix()) ? logResult.prefix() : defaultPrefix;
                log(logResult.level(), MessageFormat.format("{0}   result    :  {1}", logResultPrefix, result));
            }

            //打印耗时
            if (logTimes != null && logTimes.length > 0) {
                logTime(logTimes[0], defaultPrefix, start);
            }

            return result;
        } catch (Throwable throwable) {
            //打印错误信息
            if (logErrors != null && logErrors.length > 0) {
                LogError logError = logErrors[0];
                String logErrorPrefix = StringUtil.notEmpty(logError.prefix()) ? logError.prefix() : defaultPrefix;
                log(logError.level(), MessageFormat.format("{0}   error     :  {1}", logErrorPrefix, throwable.getMessage()));
            }

            //打印耗时
            if (logTimes != null && logTimes.length > 0) {
                logTime(logTimes[0], defaultPrefix, start);
            }

            throw throwable;
        }
    }

    @Before("@annotation(logParam)")
    public void logParam(JoinPoint joinPoint, LogParam logParam) {
        try {
            String prefix = logParam.prefix();
            String methodName = joinPoint.getSignature().getName();
            String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
            Object[] args = joinPoint.getArgs();
            if (StringUtil.isEmpty(prefix)) {
                prefix = joinPoint.getSignature().getDeclaringTypeName() + "." + methodName;
            }
            log(logParam.level(), MessageFormat.format("{0}   params    :  {1}", prefix, getParamString(parameterNames, args)));
        } catch (Exception e) {
        }
    }

    @AfterReturning(pointcut = "@annotation(logResult)", returning = "result")
    public void logResult(JoinPoint joinPoint, LogResult logResult, Object result) {
        try {
            String prefix = logResult.prefix();
            String methodName = joinPoint.getSignature().getName();
            if (StringUtil.isEmpty(prefix)) {
                prefix = joinPoint.getSignature().getDeclaringTypeName() + "." + methodName;
            }
            log(logResult.level(), MessageFormat.format("{0}   result    :  {1}", prefix, result));
        } catch (Exception e) {
        }
    }

    @AfterThrowing(pointcut = "@annotation(logError)", throwing = "ex")
    public void logError(JoinPoint joinPoint, LogError logError, Exception ex) {
        try {
            String prefix = logError.prefix();
            String methodName = joinPoint.getSignature().getName();
            if (StringUtil.isEmpty(prefix)) {
                prefix = joinPoint.getSignature().getDeclaringTypeName() + "." + methodName;
            }
            log(logError.level(), MessageFormat.format("{0}   error     :  {1}", prefix, ex.getMessage()));
        } catch (Exception e) {
        }
    }

    @Around("@annotation(logTime)")
    public Object logTime(ProceedingJoinPoint joinPoint, LogTime logTime) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String prefix = logTime.prefix();
        if (StringUtil.isEmpty(prefix)) {
            prefix = joinPoint.getSignature().getDeclaringTypeName() + "." + methodName;
        }

        long start = System.currentTimeMillis();
        try {
            Object result = joinPoint.proceed();
            logTime(logTime, prefix, start);
            return result;
        } catch (Throwable throwable) {
            logTime(logTime, prefix, start);
            throw throwable;
        }
    }

    private String getParamString(String[] parameterNames, Object[] params) {
        String msg = "";
        if (parameterNames != null && parameterNames.length > 0) {
            StringBuilder sb = new StringBuilder();
            if (params != null && params.length > 0) {
                for (int i = 0; i < parameterNames.length; i++) {
                    sb.append(", ").append(parameterNames[i]).append("=");
                    if (params != null && params.length >= i + 1) {
                        sb.append(params[i]);
                    } else {
                        sb.append("= null");
                    }
                }
            }
            msg = sb.substring(2);
        }
        return msg;
    }

    private void logTime(LogTime logTime, String defaultPrefix, long start) {
        String logTimePrefix = StringUtil.notEmpty(logTime.prefix()) ? logTime.prefix() : defaultPrefix;
        long end = System.currentTimeMillis();
        log(logTime.level(), MessageFormat.format("{0}   costTime  :  {1}", logTimePrefix, String.valueOf(end - start)));
    }

    private void log(Level level, String msg) {
        switch (level) {
            case TRACE:
                logger.trace(msg);
                break;
            case DEBUG:
                logger.debug(msg);
                break;
            case INFO:
                logger.info(msg);
                break;
            case WARN:
                logger.warn(msg);
                break;
            case ERROR:
                logger.error(msg);
                break;
            default:
        }
    }
}