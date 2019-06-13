package br.com.ccrs.logistics.fleet.order.acceptance;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestConfig.class, properties = {"spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL"})
public abstract class AbstractIntTest {
}
