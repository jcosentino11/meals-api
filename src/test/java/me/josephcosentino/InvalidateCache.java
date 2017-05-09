package me.josephcosentino;

import org.junit.rules.ExternalResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

/**
 * @author Joseph Cosentino.
 */
@Component
public class InvalidateCache extends ExternalResource {

    @Autowired
    private CacheManager cacheManager;

    @Override
    protected void before() {
        cacheManager.getCacheNames()
                .stream()
                .map(cacheManager::getCache)
                .forEach(Cache::clear);
    }

}
