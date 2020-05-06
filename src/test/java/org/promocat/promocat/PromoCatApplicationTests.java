package org.promocat.promocat;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.car.CarService;
import org.promocat.promocat.data_entities.car_number.CarNumberRecord;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.promocat.promocat.data_entities.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

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

	@Autowired
	private UserService userService;

	@Autowired
	private CarService carService;

	@Test
	void validUser() throws Exception {
		UserRecord userRecord = new UserRecord();
		userRecord.setId(1L);
		userRecord.setFirst_name("my");
		userRecord.setLast_name("yo");
		userRecord.setTelephone("+7(962)401-15-60");
		userRecord.setBalance(1L);

		List<CarRecord> cars = new ArrayList<>();
		CarRecord car = new CarRecord();
		car.setId(1L);
		car.setColor("blue");
		car.setCar_make("Jigul");
		cars.add(car);

		CarNumberRecord number = new CarNumberRecord();
		userRecord.setCars(cars);
		number.setId(1L);
		number.setNumber("12");
		number.setRegion("12");
		userService.save(userRecord);

		this.mvc.perform(post("/my").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userRecord)))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	void userWithNonValidTelephone() throws Exception {
		UserRecord userRecord = new UserRecord();
		userRecord.setId(1L);
		userRecord.setFirst_name("my");
		userRecord.setLast_name("yo");
		userRecord.setTelephone("+7(962)401--15-60");
		userRecord.setBalance(1L);

		this.mvc.perform(post("/my").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(userRecord)))
				.andDo(print())
				.andExpect(status().is4xxClientError());
	}
}
