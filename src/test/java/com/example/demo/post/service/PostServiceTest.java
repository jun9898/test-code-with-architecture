package com.example.demo.post.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.example.demo.common.service.port.ClockHolder;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakePostRepository;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUUidHolder;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserService;

class PostServiceTest {

	private PostService postService;

	@BeforeEach
	void init() {
		ClockHolder clockHolder = new TestClockHolder(100L);
		FakePostRepository fakePostRepository = new FakePostRepository();
		FakeUserRepository fakeUserRepository = new FakeUserRepository();
		this.postService = PostService.builder()
			.postRepository(fakePostRepository)
			.userRepository(fakeUserRepository)
			.clockHolder(clockHolder)
			.build();

		User user1 = fakeUserRepository.save(User.builder()
			.id(1L)
			.email("nice1998@gmail.com")
			.status(UserStatus.ACTIVE)
			.nickname("nice1998")
			.address("Seoul")
			.lastLoginAt(0L)
			.certificationCode("aaaa-aaaa-aaaa-aaaa")
			.build());

		User user2 = fakeUserRepository.save(User.builder()
			.id(2L)
			.email("nice1999@gmail.com")
			.status(UserStatus.PENDING)
			.nickname("nice1999")
			.address("Seoul")
			.lastLoginAt(0L)
			.certificationCode("aaaa-aaaa-aaaa-aaaa")
			.build());

		fakePostRepository.save(Post.builder()
			.id(1L)
			.content("helloworld")
			.createdAt(100L)
			.modifiedAt(0L)
			.writer(user1)
			.build());
	}

	@Test
	void getById는_존재하는_게시글을_가져온다() {
		//given
		//when
		Post result = postService.getPostById(1L);

		//then
		assertThat(result.getContent()).isEqualTo("helloworld");
		assertThat(result.getWriter().getEmail()).isEqualTo("nice1998@gmail.com");
	}

	@Test
	void PostCreateDto를_이용하여_게시글을_생성할_수_있다() {
		//given
		PostCreate postCreateDto = PostCreate.builder()
			.writerId(1L)
			.content("test")
			.build();

		//when
		Post result = postService.create(postCreateDto);

		//then
		assertThat(result.getContent()).isEqualTo("test");
		assertThat(result.getWriter().getEmail()).isEqualTo("nice1998@gmail.com");
		assertThat(result.getCreatedAt()).isEqualTo(100L);
	}

	@Test
	void PostUpdateDto를_이용하여_게시글을_수정할_수_있다() {
		//given
		PostUpdate postUpdateDto = PostUpdate.builder()
			.content("test")
			.build();

		//when
		Post result = postService.update(1L, postUpdateDto);

		//then
		assertThat(result.getContent()).isEqualTo("test");
		assertThat(result.getModifiedAt()).isEqualTo(100L);
	}
}
