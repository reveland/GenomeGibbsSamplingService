package com.genome.comparer;

import com.genome.comparer.core.Genome;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Aspect.
 */
@Aspect
public class LoggerAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerAspect.class);

    /**
     * Aspect that log if pooled adjacency is not contains all the adjacency that the genome does.
     */
    @Around("execution(* com.genome.comparer.core.PooledAdjacencies.fingerprint(..))")
    public Object logWarningWhenPooledAdjacencyNotContainsAllAdjacencies(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Genome genome = (Genome) proceedingJoinPoint.getArgs()[0];
        int genome_adjacencies_size = genome.adjacencies.size();
        int[] fingerprint = (int[]) proceedingJoinPoint.proceed();
        if (genome_adjacencies_size != fingerprint.length) {
            LOGGER.warn("Found adjacencies: {}, Number of adjacencies in genome: {}", fingerprint.length, genome_adjacencies_size);
        } else {
            LOGGER.info("Found adjacencies: {}, Number of adjacencies in genome: {}", fingerprint.length, genome_adjacencies_size);
        }
        return fingerprint;
    }
}
