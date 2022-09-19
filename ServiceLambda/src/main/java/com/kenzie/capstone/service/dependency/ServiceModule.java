package com.kenzie.capstone.service.dependency;

import com.kenzie.capstone.service.DrinkService;
import com.kenzie.capstone.service.dao.DrinkDao;

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
    public DrinkService provideDrinkService(@Named("DrinkDao") DrinkDao drinkDao) {
        return new DrinkService(drinkDao);
    }
}

