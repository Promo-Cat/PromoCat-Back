package org.promocat.promocat;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.user.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class PromoCatApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void validUser() throws Exception {
		UserRecord userRecord = new UserRecord();
		userRecord.setUser_id(1L);
		userRecord.setFirst_name("my");
		userRecord.setLast_name("yo");
		userRecord.setTelephone("+7(962)401-15-60");
		userRecord.setBalance(1L);

		Gson gson = new Gson();
		String json = gson.toJson(userRecord);
		System.out.println(json);
		this.mvc.perform(post("/my").contentType(MediaType.APPLICATION_JSON).content(json))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void userWithNonValidTelephone() throws Exception {
		UserRecord userRecord = new UserRecord();
		userRecord.setUser_id(1L);
		userRecord.setFirst_name("my");
		userRecord.setLast_name("yo");
		userRecord.setTelephone("+7(962)401--15-60");
		userRecord.setBalance(1L);

		Gson gson = new Gson();
		String json = gson.toJson(userRecord);
		System.out.println(json);
		this.mvc.perform(post("/my").contentType(MediaType.APPLICATION_JSON).content(json))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}
}
