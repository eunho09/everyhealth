package com.example.everyhealth.aop;

import com.example.everyhealth.security.JwtTokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

@Aspect
@Component
@RequiredArgsConstructor
public class TokenAspect {

    private final JwtTokenGenerator jwtTokenGenerator;

    @Around("@annotation(com.example.everyhealth.aop.ExtractMemberId)")
    public Object getMemberId(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String token = WebUtils.getCookie(request, "jwt").getValue();
        if (token == null) {
            return joinPoint.proceed();
        }

        Long memberId = jwtTokenGenerator.getUserId(token);

        Object[] args = joinPoint.getArgs();
        args[0] = memberId;

        return joinPoint.proceed(args);
    }
}
