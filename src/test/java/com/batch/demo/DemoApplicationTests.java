package com.batch.demo;

import demo.data.dummy.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
		User user = new User();

		ArrayList<User> userList = user.createUser();

		Assertions.assertThat(userList.size()).isEqualTo(100000000);
	}

}
