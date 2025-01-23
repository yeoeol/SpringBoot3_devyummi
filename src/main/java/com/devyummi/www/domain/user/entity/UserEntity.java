package com.devyummi.www.domain.user.entity;

import java.util.ArrayList;
import java.util.List;

import com.devyummi.www.domain.board.entity.BoardEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;
	private String password;

	private String nickname;

	@Enumerated(EnumType.STRING)
	private UserRoleType role;

	@OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BoardEntity> boardEntityList = new ArrayList<>();

	// 유저에 대해 새로운 글을 추가할 때 : 추가할 글을 받아서 연관관계에 매핑해줌
	public void addBoardEntity(BoardEntity entity) {
		entity.setUserEntity(this);
		boardEntityList.add(entity);
	}

	// 유저에 대해 기존 글을 삭제할 때 : 삭제할 글을 받아서 연관관계에서 뺌
	public void removeBoardEntity(BoardEntity entity) {
		entity.setUserEntity(null);
		boardEntityList.remove(entity);
	}
}
