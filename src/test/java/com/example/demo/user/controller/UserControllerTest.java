package com.example.demo.user.controller;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
	@Sql("/sql/user-controller-test-data.sql"),
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private UserJpaRepository userJpaRepository;
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void 사용자는_특정_유저의_정보를_소거된체_전달_받을_수_있다() throws Exception {
		//given
		//when
		//then
		mockMvc.perform(get("/api/users/1"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.email").value("nice1998@gmail.com"))
			.andExpect(jsonPath("$.nickname").value("nice1998"))
			.andExpect(jsonPath("$.address").doesNotExist())
			.andExpect(jsonPath("$.status").value("ACTIVE"));
	}

	@Test
	void 존재하지_않는_유저의_ID로_정보를_요청하면_404에러를_반환한다() throws Exception {
		//given
		//when
		//then
		mockMvc.perform(get("/api/users/100"))
			.andExpect(status().isNotFound())
			.andExpect(content().string("Users에서 ID 100를 찾을 수 없습니다."));
	}

	@Test
	void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() throws Exception {
		//given
		//when
		//then
		mockMvc.perform(
			get("/api/users/2/verify")
			.queryParam("certificationCode", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa"))
			.andExpect(status().isFound());

		UserEntity userEntity = userJpaRepository.findById(2L).get();
		assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
	}

	@Test
	void 사용자는_인증_코드가_일치하지_않을때_에러를_반환받는다() throws Exception {
		//given
		//when
		//then
		mockMvc.perform(
				get("/api/users/2/verify")
					.queryParam("certificationCode", "aaaaaaaa"))
			.andExpect(status().isForbidden());
	}

	@Test
	void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고올_수_있다() throws Exception {
		//given
		//when
		//then
		mockMvc.perform(
				get("/api/users/me")
					.header("EMAIL", "nice1998@gmail.com"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.email").value("nice1998@gmail.com"))
			.andExpect(jsonPath("$.nickname").value("nice1998"))
			.andExpect(jsonPath("$.address").value("Seoul"))
			.andExpect(jsonPath("$.status").value(UserStatus.ACTIVE.name()));
	}

	@Test
	public void 사용자는_내_정보를_수정할_수_있다() throws Exception {
	    //given
		UserUpdate userUpdate = new UserUpdate("nice1998-test", "Seoul-test");
	    //when
	    //then
		mockMvc.perform(
			put("/api/users/me")
				.header("EMAIL", "nice1998@gmail.com")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(userUpdate)))
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.email").value("nice1998@gmail.com"))
			.andExpect(jsonPath("$.nickname").value("nice1998-test"))
			.andExpect(jsonPath("$.address").value("Seoul-test"))
			.andExpect(jsonPath("$.status").value(UserStatus.ACTIVE.name()));
	}
}