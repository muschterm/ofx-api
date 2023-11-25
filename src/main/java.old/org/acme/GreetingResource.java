package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Arrays;

@Path("/hello")
public class GreetingResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		var array = new Integer[]{
			1,
			2,
			3,
			4,
			5
		};
		return "Hello from RESTEasy Reactive %s".formatted(Arrays.toString(array));
	}

}
