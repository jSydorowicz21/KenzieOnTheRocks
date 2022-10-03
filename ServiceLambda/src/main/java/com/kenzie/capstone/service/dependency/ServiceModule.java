package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.DrinkService;
import com.kenzie.capstone.service.dao.CachingDrinkDao;
import com.kenzie.capstone.service.dao.NonCachingDrinkDao;

import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Module(
    includes = DaoModule.class
)
public class ServiceModule {

    @Singleton
    @Provides
    @Inject
    public DrinkService provideDrinkService(@Named("DrinkDao") CachingDrinkDao drinkDao) {
        return new DrinkService(drinkDao);
    }
}

