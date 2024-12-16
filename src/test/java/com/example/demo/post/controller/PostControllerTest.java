package com.example.demo.post.controller;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.common.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

class PostControllerTest {

	@Test
	void 사용자는_게시물을_단건_조회할수_있다 () throws Exception {
		//given
		TestContainer testContainer = TestContainer.builder()
			.uuidHolder(() -> "aaaaa_aaaaa_aaaaa_aaaaa_aaaaa")
			.clockHolder(() -> 100L)
			.build();

		User user = testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("nice1998@gmail.com")
			.nickname("nice1998")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.lastLoginAt(100L)
			.build());

		testContainer.postRepository.save(Post.builder()
			.id(1L)
			.writer(user)
			.createdAt(100L)
			.modifiedAt(100L)
			.content("test content")
			.build());

		//when
		ResponseEntity<PostResponse> postById = testContainer.postController.getPostById(1L);

		//then
		assertThat(postById.getStatusCode().value()).isEqualTo(HttpStatusCode.valueOf(200).value());
		assertThat(postById.getBody()).isNotNull();
		assertThat(postById.getBody().getId()).isEqualTo(1L);
		assertThat(postById.getBody().getCreatedAt()).isEqualTo(100L);
		assertThat(postById.getBody().getModifiedAt()).isEqualTo(100L);
		assertThat(postById.getBody().getContent()).isEqualTo("test content");
		assertThat(postById.getBody().getWriter().getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(postById.getBody().getWriter().getNickname()).isEqualTo("nice1998");
		assertThat(postById.getBody().getWriter().getLastLoginAt()).isEqualTo(100L);
		assertThat(postById.getBody().getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가난다() throws Exception {
		//given
		TestContainer testContainer = TestContainer.builder()
			.build();

		//when
		//then
		assertThatThrownBy(() -> testContainer.postController
			.getPostById(1L))
			.isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void 사용자는_게시물을_수정할_수_있다() throws Exception {
		//given
		TestContainer testContainer = TestContainer.builder()
			.uuidHolder(() -> "aaaaa_aaaaa_aaaaa_aaaaa_aaaaa")
			.clockHolder(() -> 100L)
			.build();

		User user = testContainer.userRepository.save(User.builder()
			.id(1L)
			.email("nice1998@gmail.com")
			.nickname("nice1998")
			.address("Seoul")
			.status(UserStatus.ACTIVE)
			.certificationCode("aaaaa-aaaaa-aaaaa-aaaaa-aaaaa")
			.lastLoginAt(100L)
			.build());

		testContainer.postRepository.save(Post.builder()
			.id(1L)
			.writer(user)
			.createdAt(100L)
			.modifiedAt(100L)
			.content("test content")
			.build());

		PostUpdate postUpdateDto = PostUpdate.builder()
			.content("자바조아용")
			.build();

		//when
		ResponseEntity<PostResponse> result = testContainer.postController.updatePost(1L,
			postUpdateDto);

		//then
		assertThat(result.getStatusCode().value()).isEqualTo(HttpStatusCode.valueOf(200).value());
		assertThat(result.getBody()).isNotNull();
		assertThat(result.getBody().getId()).isEqualTo(1L);
		assertThat(result.getBody().getCreatedAt()).isEqualTo(100L);
		assertThat(result.getBody().getModifiedAt()).isEqualTo(100L);
		assertThat(result.getBody().getContent()).isEqualTo("자바조아용");
		assertThat(result.getBody().getWriter().getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(result.getBody().getWriter().getNickname()).isEqualTo("nice1998");
		assertThat(result.getBody().getWriter().getLastLoginAt()).isEqualTo(100L);
		assertThat(result.getBody().getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
	}
}