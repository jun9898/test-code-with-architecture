package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;

@SpringBootTest
@SqlGroup({
	@Sql("/sql/user-service-test-data.sql"),
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserServiceTest {

	@Autowired
	private UserService userService;
	// 가짜객체로 설정해줘야함
	@MockBean
	private JavaMailSender mailSender;

	@Test
	void getByEmail은_ACTIVE_상태인_유저를_찾아올_수_있다() {
	    //given
		String email = "nice1998@gmail.com";

	    //when
		User result = userService.getByEmail(email);

	    //then
		assertThat(result.getNickname()).isEqualTo("nice1998");
	}

	@Test
	void getByEmail은_PENDING_상태인_유저를_찾아올_수_없다() {
		//given
		String email = "nice1999@gmail.com";

		//when
		//then
		assertThatThrownBy(() -> {
			userService.getByEmail(email);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void getById는_ACTIVE_상태인_유저를_찾아올_수_있다() {
		//given
		//when
		User result = userService.getById(1L);

		//then
		assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	void getById는_PENDING_상태인_유저를_찾아올_수_있다() {
		//given
		//when
		//then
		assertThatThrownBy(() -> {
			userService.getById(2L);
		}).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void create는_유저를_생성할_수_있다() {
		//given
		UserCreate userCreate = UserCreate.builder()
			.email("nice2000@gmail.com")
			.nickname("nice2000")
			.address("경기도")
			.build();

		//when
		BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));
		User result = userService.create(userCreate);

		//then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
		// TODO: DI와 DIP를 이용해 Holder를 작성, 분리해야할듯함
		// assertThat(result.getCertificationCode()).isEqualTo(oh);
	}

	@Test
	void update는_유저_정보를_변경할_수_있다() {
		//given
		UserUpdate userUpdate = UserUpdate.builder()
			.nickname("닉네임 입니다")
			.address("배고파요")
			.build();

		//when
		User result = userService.update(1L, userUpdate);

		//then
		assertThat(result.getNickname()).isEqualTo("닉네임 입니다");
		assertThat(result.getAddress()).isEqualTo("배고파요");
	}

	@Test
	void user를_로그인_시키면_마지막_로그인_시간이_업데이트_된다() {
		//given
		//when
		userService.login(1L);

		//then
		User User = userService.getById(1L);
		// TODO: DI와 DIP를 이용해 Holder를 작성, 분리해야할듯함
		// assertThat(User.getLastLoginAt()).isEqualTo(...);
	}

	@Test
	void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
		//given
		//when
		userService.verifyEmail(2L, "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

		//then
		User User = userService.getById(2L);
		assertThat(User.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() {
		//given
		//when
		//then
		assertThatThrownBy(() -> {
			userService.verifyEmail(2L, "test");
		}).isInstanceOf(CertificationCodeNotMatchedException.class);
	}
}