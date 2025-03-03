package com.devyummi.www.domain.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devyummi.www.domain.user.dto.UserRequestDTO;
import com.devyummi.www.domain.user.dto.UserResponseDTO;
import com.devyummi.www.domain.user.entity.UserEntity;
import com.devyummi.www.domain.user.entity.UserRoleType;
import com.devyummi.www.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	// 유저 접근 권한 체크
	public Boolean isAccess(String username) {
		// 현재 로그인 되어 있는 유저의 username
		String sessionUsername = SecurityContextHolder.getContext().getAuthentication()
			.getName();
		// 현재 로그인 되어 있는 유저의 role
		String sessionRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
			.iterator().next().getAuthority();

		// 수직적으로 ADMIN이면 무조건 접근 가능
		if ("ROLE_ADMIN".equals(sessionRole)) {
			return true;
		}

		// 수평적으로 특정 행위를 수행할 username에 대해 세션(현재 로그인한) username과 같은지
		if (username.equals(sessionUsername)) {
			return true;
		}

		return false;
	}

	// 유저 한 명 생성
	@Transactional
	public void createOneUser(UserRequestDTO dto) {
		String username = dto.getUsername();
		String password = dto.getPassword();
		String nickname = dto.getNickname();

		// 동일한 username이 있는지 확인
		if (userRepository.existsByUsername(username)) {
			return;
		}

		// 유저에 대한 Entity 생성 : DTO -> Entity 및 추가 정보 set
		UserEntity entity = new UserEntity();
		entity.setUsername(username);
		entity.setPassword(bCryptPasswordEncoder.encode(password));
		entity.setNickname(nickname);
		entity.setRole(UserRoleType.USER);

		// Entity 저장
		userRepository.save(entity);
	}

	// 유저 한 명 읽기
	public UserResponseDTO readOneUser(String username) {
		UserEntity entity = userRepository.findByUsername(username).orElseThrow();

		UserResponseDTO dto = new UserResponseDTO();
		dto.setUsername(entity.getUsername());
		dto.setNickname(entity.getNickname());
		dto.setRole(entity.getRole().toString());

		return dto;
	}

	// 유저 모두 읽기
	public List<UserResponseDTO> readAllUsers() {
		List<UserEntity> list = userRepository.findAll();

		List<UserResponseDTO> dtos = new ArrayList<>();
		for (UserEntity user : list) {
			UserResponseDTO dto = new UserResponseDTO();
			dto.setUsername(user.getUsername());
			dto.setNickname(user.getNickname());
			dto.setRole(user.getRole().toString());

			dtos.add(dto);
		}

		return dtos;
	}

	// 유저 로그인 (로그인 같은 경우 읽기지만, 시큐리티 형식으로 맞춰야 합니다.)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity entity = userRepository.findByUsername(username).orElseThrow();

		return User.builder()
			.username(entity.getUsername())
			.password(entity.getPassword())
			.roles(entity.getRole().toString())
			.build();
	}

	// 유저 한 명 수정
	@Transactional
	public void updateOneUser(UserRequestDTO dto, String username) {
		UserEntity entity = userRepository.findByUsername(username).orElseThrow();

		if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
			entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
		}

		if (dto.getNickname() != null && !dto.getNickname().isEmpty()) {
			entity.setNickname(dto.getNickname());
		}
	}

	// 유저 한 명 삭제
	@Transactional
	public void deleteOneUser(String username) {

		userRepository.deleteByUsername(username);
	}
}
