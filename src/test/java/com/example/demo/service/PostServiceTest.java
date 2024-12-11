package com.example.demo.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import com.example.demo.post.service.PostService;

@SpringBootTest
@SqlGroup({
	@Sql("/sql/post-service-test-data.sql"),
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {

	@Autowired
	private PostService postService;

	@Test
	void getById는_존재하는_게시글을_가져온다() {
		//given
		//when
		PostEntity result = postService.getPostById(1L);

		//then
		assertThat(result.getContent()).isEqualTo("helloworld");
		assertThat(result.getWriter().getEmail()).isEqualTo("kok202@naver.com");
	}

	@Test
	void PostCreateDto를_이용하여_게시글을_생성할_수_있다() {
		//given
		PostCreate postCreateDto = PostCreate.builder()
			.writerId(1L)
			.content("test")
			.build();

		//when
		PostEntity result = postService.create(postCreateDto);

		//then
		assertThat(result.getContent()).isEqualTo("test");
		assertThat(result.getWriter().getEmail()).isEqualTo("kok202@naver.com");
	}

	@Test
	void PostUpdateDto를_이용하여_게시글을_수정할_수_있다() {
		//given
		PostUpdate postUpdateDto = PostUpdate.builder()
			.content("test")
			.build();

		//when
		PostEntity result = postService.update(1L, postUpdateDto);

		//then
		assertThat(result.getContent()).isEqualTo("test");
	}
}
