package com.booking.toggles;

import com.booking.exceptions.FeatureDisabledException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class FeaturesAspect {
    @Around("@within(featureAssociation) || @annotation(featureAssociation)")
    public Object checkAspect(ProceedingJoinPoint joinPoint, FeatureAssociation featureAssociation) throws Throwable {
        if (featureAssociation.value().isActive()) {
            return joinPoint.proceed();
        } else {
            throw new FeatureDisabledException();
        }
    }
}