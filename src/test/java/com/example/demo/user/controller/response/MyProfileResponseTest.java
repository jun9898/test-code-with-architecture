package com.example.demo.user.controller.response;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

class MyProfileResponseTest {

	@Test
	public void MyProfileResponse로_응답을_생성할_수_있다() throws Exception {
		//given
		User user = User.builder()
			.address("Seoul")
			.email("nice1998@gmail.com")
			.status(UserStatus.ACTIVE)
			.nickname("nice1998")
			.lastLoginAt(100L)
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.build();

		//when
		MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

		//then
		assertThat(myProfileResponse.getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(myProfileResponse.getNickname()).isEqualTo("nice1998");
		assertThat(myProfileResponse.getAddress()).isEqualTo("Seoul");
		assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
	}

}