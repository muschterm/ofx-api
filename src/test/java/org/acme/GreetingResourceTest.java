package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
public class GreetingResourceTest {

	@Test
	public void testHelloEndpoint() {
		given()
			.when().get("/hello")
			.then()
			.statusCode(200)
			.body(is("Hello from RESTEasy Reactive [1, 2, 3, 4, 5]"));
	}

}