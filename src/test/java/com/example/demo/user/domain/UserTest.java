package com.example.demo.user.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.common.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUUidHolder;

class UserTest {

	@Test
	public void User는_UserCreate_객체로_생성할_수_있다() {
		// given
		UserCreate userCreate = UserCreate.builder()
			.email("nice1998@gmail.com")
			.address("Seoul")
			.nickname("nice1998")
			.build();

		// when
		User user = User.from(userCreate, new TestUUidHolder("test"));

		// then
		assertThat(user.getId()).isNull();
		assertThat(user.getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(user.getAddress()).isEqualTo("Seoul");
		assertThat(user.getNickname()).isEqualTo("nice1998");
		assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
		assertThat(user.getCertificationCode()).isEqualTo("test");
	}

	@Test
	public void User는_UserUpdate객체로_업데이트_할_수_있다() {
		// given
		User user = User.builder()
			.id(1L)
			.address("Seoul")
			.email("nice1998@gmail.com")
			.status(UserStatus.ACTIVE)
			.nickname("nice1998")
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.build();

		UserUpdate userUpdate = UserUpdate.builder()
			.address("test")
			.nickname("testNickname")
			.build();

		// when
		user = user.update(userUpdate);

		// then
		assertThat(user.getId()).isEqualTo(1L);
		assertThat(user.getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(user.getAddress()).isEqualTo("test");
		assertThat(user.getNickname()).isEqualTo("testNickname");
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(user.getCertificationCode()).isEqualTo("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa");
	}

	@Test
	public void User는_로그인을_할_수_있고_로그인_시_마지막_로그인_시간이_변경된다() throws Exception {
	    //given
		User user = User.builder()
			.id(1L)
			.address("Seoul")
			.email("nice1998@gmail.com")
			.status(UserStatus.ACTIVE)
			.nickname("nice1998")
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.lastLoginAt(0L)
			.build();

	    //when
		user = user.login(new TestClockHolder(100L));
	    
	    //then
		assertThat(user.getLastLoginAt()).isEqualTo(100L);
	}

	@Test
	public void User는_유효한_인증_코드로_계정을_활성화_할_수_있다() throws Exception {
		//given
		User user = User.builder()
			.id(1L)
			.address("Seoul")
			.email("nice1998@gmail.com")
			.status(UserStatus.PENDING)
			.nickname("nice1998")
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.lastLoginAt(0L)
			.build();

		//when
		user = user.certificate("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa");

		//then
		assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	public void User는_잘못된_인증_코드로_계정을_활성화_할_수_없다() throws Exception {
		//given
		User user = User.builder()
			.id(1L)
			.address("Seoul")
			.email("nice1998@gmail.com")
			.status(UserStatus.PENDING)
			.nickname("nice1998")
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.lastLoginAt(0L)
			.build();

		//when
		//then
		assertThatThrownBy(() -> user.certificate("bbbb-bbbb-bbbb-bbbb-bbbb"))
			.isInstanceOf(CertificationCodeNotMatchedException.class);
	}

}