package com.devyummi.www.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devyummi.www.domain.board.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}
