package com.holoyolo.app;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PrjApplicationTests {

	@Autowired
	StringEncryptor jasyptStringEncryptor;
	
	@Test
	void contextLoads() {
	}
	
	@Test
	void encryptor() {
		String[] datas = {
				"oracle.jdbc.OracleDriver"
				,"jdbc:oracle:thin:@127.0.0.1:1521/xe"
				,"hr"
				,"hr"
		};
		
		for(String data : datas) {
			String encData = jasyptStringEncryptor.encrypt(data);
			System.out.println(data +" >>> "+encData);
		}
	}


}
