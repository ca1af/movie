package com.example.movie.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.HashMap;

@Slf4j
@Aspect
@Component
public class AnnotationBasedAOP {
    public static HashMap<Long, Long> map = new HashMap<>(); // static 사용해서 정적으로 관리
    @Pointcut("@annotation(com.example.movie.common.aop.ExeTimer)")
    private void timer(){}

    @Around("timer()")
    public Object AssumeExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        long totalTimeMillis = stopWatch.getTotalTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();

        log.info("실행 메서드: {}, 실행시간 = {}ms", methodName, totalTimeMillis);
        return proceed;
    }

    @Pointcut("@annotation(com.example.movie.common.aop.CountExeByMovieId)")
    private void count(){};

    @Before("count()")
    public void countMovieIdCall(JoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Object[] arguments = joinPoint.getArgs();
        Long movieId = (Long) arguments[0];

        map.put(movieId, map.getOrDefault(movieId, 0L) + 1);
        // 콜 될때마다 해시맵에다가 호출 횟수를 관리하는거지
    }
}