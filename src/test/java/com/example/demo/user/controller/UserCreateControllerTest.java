package com.example.demo.user.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
	@Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class UserCreateControllerTest {

	@Autowired
	private MockMvc mockMvc;
	private final ObjectMapper objectMapper = new ObjectMapper();
	@MockBean
	private JavaMailSender mailSender;

	@Test
	public void 사용자는_내_정보를_등록할_수_있다() throws Exception {
		//given
		UserCreate userCreate = new UserCreate("nice1998@gmail.com", "nice1998", "Seoul");
		BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));
		//when
		//then
		mockMvc.perform(
				post("/api/users")
					.header("EMAIL", "nice1998@gmail.com")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(userCreate)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").value(1))
			.andExpect(jsonPath("$.email").value("nice1998@gmail.com"))
			.andExpect(jsonPath("$.nickname").value("nice1998"))
			.andExpect(jsonPath("$.address").doesNotExist())
			.andExpect(jsonPath("$.status").value(UserStatus.PENDING.name()));
	}

}