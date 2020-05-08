package org.promocat.promocat;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.user.UserRecord;
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
	void userWithNonValidTelephone() throws Exception {
		UserRecord userRecord = new UserRecord();
		userRecord.setId(1L);
		userRecord.setName("my");
		userRecord.setTelephone("+7(962)401--15-60");
		userRecord.setBalance(1L);

		this.mvc.perform(post("/addUser").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userRecord)))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}
}
