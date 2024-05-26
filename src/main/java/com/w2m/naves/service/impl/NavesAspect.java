package com.w2m.naves.service.impl;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class NavesAspect {

    private static final Logger LOG = LoggerFactory.getLogger(NavesAspect.class);

    /*
        Aspecto para los métodos dentro del servicio de naves los cuales tengan presente un argumento llamado 'id'
     */
    @Before("execution(public * com.w2m.naves.service.impl.NaveServiceImpl.*(..)) && args(id))")
    private void logBefore(JoinPoint joinPoint, Long id) {
        if(id < 0) {
            LOG.info("Se ha recibido un id negativo: {}", id);
        }
    }

}
