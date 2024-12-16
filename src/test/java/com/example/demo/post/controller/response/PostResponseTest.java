package com.example.demo.post.controller.response;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

class PostResponseTest {

	@Test
	public void Post으로_응답을_생성할_수_있다() throws Exception {
	    //given
		Post post = Post.builder()
			.content("helloworld")
			.writer(User.builder()
				.address("Seoul")
				.email("nice1998@gmail.com")
				.status(UserStatus.ACTIVE)
				.nickname("nice1998")
				.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
				.build())
			.build();

	    //when
		PostResponse postResponse = PostResponse.from(post);

	    //then
		assertThat(post.getContent()).isEqualTo("helloworld");
		assertThat(post.getWriter().getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(post.getWriter().getNickname()).isEqualTo("nice1998");
		assertThat(post.getWriter().getAddress()).isEqualTo("Seoul");
		assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);

	}

}