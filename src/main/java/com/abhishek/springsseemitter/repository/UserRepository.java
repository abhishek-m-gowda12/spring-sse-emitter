package com.abhishek.springsseemitter.repository;

import com.abhishek.springsseemitter.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}
