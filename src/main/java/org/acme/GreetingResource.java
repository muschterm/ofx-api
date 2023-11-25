package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {
		var array = new int[]{
			1,
			2,
			3
		};
		return "Hello from RESTEasy Reactive";
	}

}
