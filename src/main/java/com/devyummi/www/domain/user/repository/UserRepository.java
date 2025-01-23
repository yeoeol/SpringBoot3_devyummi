package com.devyummi.www.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devyummi.www.domain.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
