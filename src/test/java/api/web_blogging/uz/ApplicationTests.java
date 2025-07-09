package api.web_blogging.uz;

import api.web_blogging.uz.services.SmsSendService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private SmsSendService smsSendService;

	@Test
	void contextLoads() {
//		smsSendService.getToken();
	}

}
