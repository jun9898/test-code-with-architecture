package com.example.demo.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.service.port.UserRepository;

@DataJpaTest(showSql = true)
@Sql("/sql/user-repository-test-data.sql")
@ComponentScan(basePackages = "com.example.demo.user.infrastructure")
class UserRepositoryTest {

	// 이렇게 되면 중간 테스트인 격
	@Autowired
	private UserRepository userRepository;

	@Test
	void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
		// given
		// when
		Optional<UserEntity> result = userRepository.findByIdAndStatus(1L ,UserStatus.ACTIVE);

		// then
		assertThat(result).isPresent();
	}

	@Test
	void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
		// given
		// when
		Optional<UserEntity> result = userRepository.findByIdAndStatus(1L ,UserStatus.PENDING);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
		// given
		// when
		Optional<UserEntity> result = userRepository.findByEmailAndStatus("nice1998@gmail.com" ,UserStatus.ACTIVE);

		// then
		assertThat(result).isPresent();
	}

	@Test
	void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
		// given
		// when
		Optional<UserEntity> result = userRepository.findByEmailAndStatus("nice1998@gmail.com" ,UserStatus.PENDING);

		// then
		assertThat(result).isEmpty();
	}

}