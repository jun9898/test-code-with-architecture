package com.example.demo.user.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.port.UserRepository;

@SpringBootTest

@SqlGroup({
	@Sql("/sql/user-repository-test-data.sql"),
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserRepositoryTest {

	// 이렇게 되면 중간 테스트인 격
	@Autowired
	private UserRepository userRepository;

	@Test
	void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() {
		// given
		// when
		Optional<User> result = userRepository.findByIdAndStatus(1L ,UserStatus.ACTIVE);

		// then
		assertThat(result).isPresent();
	}

	@Test
	void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
		// given
		// when
		Optional<User> result = userRepository.findByIdAndStatus(1L ,UserStatus.PENDING);

		// then
		assertThat(result).isEmpty();
	}

	@Test
	void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() {
		// given
		// when
		Optional<User> result = userRepository.findByEmailAndStatus("nice1998@gmail.com" ,UserStatus.ACTIVE);

		// then
		assertThat(result).isPresent();
	}

	@Test
	void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() {
		// given
		// when
		Optional<User> result = userRepository.findByEmailAndStatus("nice1998@gmail.com" ,UserStatus.PENDING);

		// then
		assertThat(result).isEmpty();
	}

}