package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.DrinkRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface UserRepository extends CrudRepository<DrinkRecord, String> {


}
