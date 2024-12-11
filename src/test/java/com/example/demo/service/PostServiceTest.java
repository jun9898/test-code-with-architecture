package com.example.demo.service;

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

import com.example.demo.exception.CertificationCodeNotMatchedException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.UserStatus;
import com.example.demo.model.dto.PostCreateDto;
import com.example.demo.model.dto.PostUpdateDto;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.PostEntity;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;

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
		PostCreateDto postCreateDto = PostCreateDto.builder()
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
		PostUpdateDto postUpdateDto = PostUpdateDto.builder()
			.content("test")
			.build();

		//when
		PostEntity result = postService.update(1L, postUpdateDto);

		//then
		assertThat(result.getContent()).isEqualTo("test");
	}
}
