package com.devyummi.www.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.devyummi.www.domain.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	boolean existsByUsername(String username);
	Optional<UserEntity> findByUsername(String username);

	void deleteByUsername(String username);
}
