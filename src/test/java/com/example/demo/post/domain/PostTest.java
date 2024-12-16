package com.example.demo.post.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

class PostTest {

	@Test
	public void PostCreate으로_게시물을_만들_수_있다() {
		// given
		ClockHolder clockHolder = new TestClockHolder(100L);

		PostCreate postCreate = PostCreate.builder()
			.writerId(1L)
			.content("helloworld")
			.build();

		User writer = User.builder()
			.address("Seoul")
			.email("nice1998@gmail.com")
			.status(UserStatus.ACTIVE)
			.nickname("nice1998")
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.build();

		// when
		Post post = Post.from(writer, postCreate, clockHolder);

		// then
		assertThat(post.getContent()).isEqualTo("helloworld");
		assertThat(post.getWriter().getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(post.getWriter().getNickname()).isEqualTo("nice1998");
		assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
		assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
		assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa");

	}

	@Test
	public void PostUpdate로_게시물을_수정할_수_있다() {
		// given
		ClockHolder clockHolder = new TestClockHolder(100L);

		PostCreate postCreate = PostCreate.builder()
			.writerId(1L)
			.content("helloworld")
			.build();

		User writer = User.builder()
			.address("Seoul")
			.email("nice1998@gmail.com")
			.status(UserStatus.ACTIVE)
			.nickname("nice1998")
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.build();

		Post from = Post.from(writer, postCreate, clockHolder);

		PostUpdate postUpdate = PostUpdate.builder()
			.content("hello")
			.build();

		// when
		Post update = from.update(postUpdate, clockHolder);

		// then
		assertThat(update.getContent()).isEqualTo("hello");
	}

}