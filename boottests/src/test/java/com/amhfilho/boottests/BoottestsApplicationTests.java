package com.amhfilho.boottests;

import com.amhfilho.boottests.book.BookResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BoottestsApplicationTests {

    @Autowired
	private BookResource bookResource;

	@Test
	public void contextLoads() {
	    assertThat(bookResource).isNotNull();
	}

}
