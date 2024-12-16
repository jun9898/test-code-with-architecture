package com.example.demo.post.controller;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;

class PostCreateControllerTest {

	@Test
	public void 사용자는_게시글을_등록할_수_있다() throws Exception {
		//given
		TestContainer testContainer = TestContainer.builder()
			.uuidHolder(() -> "aaaaa_aaaaa_aaaaa_aaaaa_aaaaa")
			.clockHolder(() -> 100L)
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

		PostCreate postCreateDto = PostCreate.builder()
			.content("test content")
			.writerId(1L)
			.build();

		//when
		ResponseEntity<PostResponse> post = testContainer.postCreateController.createPost(postCreateDto);

		//then
		assertThat(post.getStatusCode().value()).isEqualTo(HttpStatusCode.valueOf(201).value());
		assertThat(post.getBody()).isNotNull();
		assertThat(post.getBody().getContent()).isEqualTo("test content");
		assertThat(post.getBody().getWriter().getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(post.getBody().getWriter().getNickname()).isEqualTo("nice1998");
		assertThat(post.getBody().getWriter().getLastLoginAt()).isEqualTo(100L);
		assertThat(post.getBody().getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);

	}

}