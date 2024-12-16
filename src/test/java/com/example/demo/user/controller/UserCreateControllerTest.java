package com.example.demo.user.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;

class UserCreateControllerTest {

	@Test
	public void 사용자는_회원_가입을_할_수_있고_회원가입_된_사용자는_PENDING_상태이다() throws Exception {
		//given
		TestContainer container = TestContainer.builder()
			.clockHolder(() -> 200L)
			.uuidHolder(() -> "aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.build();

		//when
		ResponseEntity<UserResponse> result = container.userCreateController.createUser(UserCreate.builder()
			.nickname("nice1998")
			.address("Seoul")
			.email("nice1998@gmail.com")
			.build());

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getNickname()).isEqualTo("nice1998");
		assertThat(result.getBody().getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(result.getBody().getLastLoginAt()).isEqualTo(0L);
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);
	}

}