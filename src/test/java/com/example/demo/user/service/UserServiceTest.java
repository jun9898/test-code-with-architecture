package com.example.demo.user.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUUidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;

class UserServiceTest {

	private UserServiceImpl userService;

	@BeforeEach
	void init() {
		FakeMailSender fakeMailSender = new FakeMailSender();
		FakeUserRepository fakeUserRepository = new FakeUserRepository();
		this.userService = UserServiceImpl.builder()
			.uuidHolder(new TestUUidHolder("aaaa-aaaa-aaaa-aaaa"))
			.clockHolder(new TestClockHolder(100L))
			.userRepository(fakeUserRepository)
			.certificationService(new CertificationService(fakeMailSender))
			.build();

		fakeUserRepository.save(User.builder()
			.id(1L)
			.email("nice1998@gmail.com")
			.status(UserStatus.ACTIVE)
			.nickname("nice1998")
			.address("Seoul")
			.lastLoginAt(0L)
			.certificationCode("aaaa-aaaa-aaaa-aaaa")
			.build());

		fakeUserRepository.save(User.builder()
			.id(2L)
			.email("nice1999@gmail.com")
			.status(UserStatus.PENDING)
			.nickname("nice1999")
			.address("Seoul")
			.lastLoginAt(0L)
			.certificationCode("aaaa-aaaa-aaaa-aaaa")
			.build());
	}

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
		User result = userService.create(userCreate);

		//then
		assertThat(result.getId()).isNotNull();
		assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
		assertThat(result.getCertificationCode()).isEqualTo("aaaa-aaaa-aaaa-aaaa");
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
		assertThat(User.getLastLoginAt()).isEqualTo(100L);
	}

	@Test
	void PENDING_상태의_사용자는_인증_코드로_ACTIVE_시킬_수_있다() {
		//given
		//when
		userService.verifyEmail(2L, "aaaa-aaaa-aaaa-aaaa");

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