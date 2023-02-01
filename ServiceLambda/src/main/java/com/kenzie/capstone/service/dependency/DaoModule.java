package com.kenzie.capstone.service.dependency;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.kenzie.capstone.service.caching.CacheClient;
import com.kenzie.capstone.service.dao.CachingDrinkDao;
import com.kenzie.capstone.service.dao.NonCachingDrinkDao;
import com.kenzie.capstone.service.util.DynamoDbClientProvider;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Provides DynamoDBMapper instance to DAO classes.
 */
@Module
public class DaoModule {

    @Singleton
    @Provides
    @Named("DynamoDBMapper")
    public DynamoDBMapper provideDynamoDBMapper() {
        return new DynamoDBMapper(DynamoDbClientProvider.getDynamoDBClient());
    }

    @Singleton
    @Provides
    @Named("NonCachingDao")
    @Inject
    public NonCachingDrinkDao provideNonCachingDao(@Named("DynamoDBMapper") DynamoDBMapper mapper) {
        return new NonCachingDrinkDao(mapper);
    }

    @Singleton
    @Provides
    @Named("DrinkDao")
    @Inject
    public CachingDrinkDao provideCachingDrinkDao(
            @Named("CacheClient") CacheClient cacheClient,
            @Named("NonCachingDao") NonCachingDrinkDao  nonCachingDrinkDao) {
        return new CachingDrinkDao(cacheClient, nonCachingDrinkDao);
    }
}
