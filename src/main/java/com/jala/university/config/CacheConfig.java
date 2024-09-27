package com.jala.university.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${cache.users.info.ttl:24}")
    private long cacheUsersInfoTtl;

    @Value("${cache.users.info.ttl:100}")
    private long cacheUsersInfoMaxSize;

    public static final String USERS_INFO_CACHE = "USERS_INFO_CACHE";

    @Bean
    public CacheManager cacheManager() {
        List<CaffeineCache> caches = new ArrayList<>();
        caches.add(buildCache(USERS_INFO_CACHE, cacheUsersInfoTtl, TimeUnit.HOURS, cacheUsersInfoMaxSize));
        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(caches);
        log.info("CacheManager configured with cache: {}", USERS_INFO_CACHE);
        return manager;
    }
    private static CaffeineCache buildCache(String name, long ttl, TimeUnit ttlUnit, long size) {
        log.debug("Creating cache '{}': TTL = {} {}, Max Size = {}", name, ttl, ttlUnit, size);
        return new CaffeineCache(name, Caffeine.newBuilder()
                .expireAfterWrite(ttl, ttlUnit)
                .maximumSize(size)
                .build());
    }
}