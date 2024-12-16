package com.example.demo.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;

public class FakeUserRepository implements UserRepository {

	// 단일 테스트는 단일 쓰레드에서 돌아가기 때문에 자료형에 있어 동기화를 고려하지 않아도 된다.
	// private final AtomicLong autoGenerateId = new AtomicLong(0);
	private Long autoGenerateId = 0L;
	private final List<User> data = new ArrayList<>();

	@Override
	public Optional<User> findById(long id) {
		return data.stream()
			.filter(item -> Objects.equals(item.getId(), id))
			.findFirst();
	}

	@Override
	public Optional<User> findByIdAndStatus(long id, UserStatus userStatus) {
		return data.stream().filter(item -> item.getId().equals(id) && item.getStatus() == userStatus).findAny();
	}

	@Override
	public Optional<User> findByEmailAndStatus(String email, UserStatus userStatus) {
		return data.stream().filter(item -> item.getEmail().equals(email) && item.getStatus() == userStatus).findAny();
	}

	@Override
	public User save(User user) {
		if(user.getId() == null || user.getId() == 0 ) {
			User newUser = User.builder()
				.id(autoGenerateId++)
				.email(user.getEmail())
				.nickname(user.getNickname())
				.address(user.getAddress())
				.certificationCode(user.getCertificationCode())
				.status(user.getStatus())
				.lastLoginAt(user.getLastLoginAt())
				.build();
			data.add(newUser);
			return newUser;
		} else {
			data.removeIf(item -> Objects.equals(item.getId(), user.getId()));
			data.add(user);
			return user;
		}
	}

	@Override
	public User getById(long id) {
		return data.stream()
			.filter(item -> Objects.equals(item.getId(), id))
			.findFirst()
			.get();
	}
}
