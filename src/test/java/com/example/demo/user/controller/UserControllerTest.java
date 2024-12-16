package com.example.demo.user.controller;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;

class UserControllerTest {

	@Test
	void 사용자는_특정_유저의_정보를_소거된체_전달_받을_수_있다() throws Exception {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();
		testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("nice1998@gmail.com")
			.nickname("nice1998")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.build());

		//when
		ResponseEntity<UserResponse> result = testContainer.userController
			.getUserById(1);

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(result.getBody().getNickname()).isEqualTo("nice1998");
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);

	}

	@Test
	void 존재하지_않는_유저의_ID로_정보를_요청하면_404에러를_반환한다() throws Exception {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();
		//when
		//then
		assertThatThrownBy(() -> testContainer.userController
			.getUserById(1)).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() throws Exception {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();
		testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("nice1998@gmail.com")
			.nickname("nice1998")
			.address("Seoul")
			.status(UserStatus.PENDING)
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.build());

		//when
		ResponseEntity<Void> result = testContainer.userController.verifyEmail(1,
			"aaaaa-aaaaa-aaaaa-aaaaa-aaaaa");

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
		assertThat(testContainer.userRepository.getById(1L).getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	void 사용자는_인증_코드가_일치하지_않을때_에러를_반환받는다() throws Exception {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();
		testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("nice1998@gmail.com")
			.nickname("nice1998")
			.address("Seoul")
			.status(UserStatus.PENDING)
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.build());

		//when
		//then
		assertThatThrownBy(() -> testContainer.userController
			.verifyEmail(1, "bbbb"))
			.isInstanceOf(CertificationCodeNotMatchedException.class);
	}

	@Test
	void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고올_수_있다() throws Exception {
		//given
		TestContainer testContainer = TestContainer.builder()
			.clockHolder(() -> 200L)
			.build();
		testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("nice1998@gmail.com")
			.nickname("nice1998")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.lastLoginAt(100L)
			.build());

		//when
		ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("nice1998@gmail.com");

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody().getAddress()).isEqualTo("Seoul");
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(result.getBody().getLastLoginAt()).isEqualTo(200L);
		assertThat(result.getBody().getNickname()).isEqualTo("nice1998");
	}

	@Test
	public void 사용자는_내_정보를_수정할_수_있다() throws Exception {
		//given
		TestContainer testContainer = TestContainer.builder()
			.clockHolder(() -> 200L)
			.build();
		testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("nice1998@gmail.com")
			.nickname("nice1998")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.lastLoginAt(100L)
			.build());

		//when
		ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("nice1998@gmail.com", UserUpdate.builder()
			.address("Pangyo")
			.nickname("test")
			.build());

		//then
		assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(result.getBody().getAddress()).isEqualTo("Pangyo");
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(result.getBody().getLastLoginAt()).isEqualTo(100);
		assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(result.getBody().getNickname()).isEqualTo("test");
	}
}