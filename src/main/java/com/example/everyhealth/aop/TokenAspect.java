package com.example.everyhealth.aop;

import com.example.everyhealth.security.JwtTokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Stream;

@Aspect
@Component
@RequiredArgsConstructor
public class TokenAspect {

    private final JwtTokenGenerator jwtTokenGenerator;

    @Around("execution(* *(.., @com.example.everyhealth.aop.ExtractMemberId (*), ..))")
    public Object getMemberId(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = WebUtils.getCookie(request, "access_token").getValue();
        if (token == null) {
            return joinPoint.proceed();
        }


        Long memberId = jwtTokenGenerator.getUserId(token);

        Object[] args = joinPoint.getArgs();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = signature.getMethod().getParameters();

        for (int i = 0; i < parameters.length; i++){
            if (parameters[i].isAnnotationPresent(ExtractMemberId.class)){
                args[i] = memberId;
                break;
            }
        }

        return joinPoint.proceed(args);
    }
}
