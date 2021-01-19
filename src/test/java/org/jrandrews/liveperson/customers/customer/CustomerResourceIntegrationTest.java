package org.jrandrews.liveperson.customers.customer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import io.quarkus.security.identity.SecurityIdentity;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class CustomerResourceIntegrationTest {
    @InjectMock
    SecurityIdentity identity;

    // @Test()
    // This works on API auth, but doesn't inject the behavior into the security context.
    // TODO: fix this.
	public void testGetCustomerUnmasked() {
        when(identity.hasRole("admin")).thenReturn(true);
        Set<String> roles = new HashSet<>();
        roles.add("admin");
        when(identity.getRoles()).thenReturn(roles);

        String expected = 
				"{\"id\":4,\"ssn\":\"456-78-9012\",\"name\":{\"name\":\"Doug\"},\"contactInfoList\":[]}";

		given()
			.pathParam("id", "4")
			.when().get("/customer/{id}")
			.then()
				.statusCode(200)
				.body(is(expected));
	}

	@Test
	public void testGetCustomerMasked() {
        when(identity.hasRole("piiEnabled")).thenReturn(true);
		String expected = 
				"{\"id\":4,\"ssn\":\"***-**-****\",\"name\":{\"name\":\"Doug\"},\"contactInfoList\":[]}";
		given()
			.pathParam("id", "4")
			.when().get("/customer/{id}")
			.then()
				.statusCode(200)
				.body(is(expected));
	}
}
