package com.holoyolo.app;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PrjApplicationTests {

	@Autowired
	StringEncryptor jasyptStringEncryptor;
	
	//@Test
	void contextLoads() {
	}
	
	@Test
	void test() {
		String test = jasyptStringEncryptor.encrypt("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");
		System.out.println(test);
	}
	


}
