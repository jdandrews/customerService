package org.jrandrews.liveperson.customers.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.SecurityContext;

import org.jrandrews.liveperson.customers.customer.domain.Address;
import org.jrandrews.liveperson.customers.customer.domain.ContactInfo;
import org.jrandrews.liveperson.customers.customer.domain.ContactInfo.Context;
import org.jrandrews.liveperson.customers.customer.domain.Customer;
import org.jrandrews.liveperson.customers.customer.domain.CustomerId;
import org.jrandrews.liveperson.customers.customer.domain.Name;
import org.jrandrews.liveperson.customers.tenant.TenantId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CustomerResourceTest {
    private static final TenantId TENANT_ID = new TenantId(0);

    @Mock
    SecurityContext identityContext;

    CustomerResource api;
    
    @BeforeEach
	public void setUp() {
    	MockitoAnnotations.openMocks(this);
    	api = new CustomerResource();
	}

    @Test
	void testGetCustomerAdmin() {
        when(identityContext.isUserInRole("admin")).thenReturn(true);

        Customer customer = api.getCustomer("3", identityContext);
        
        assertNotNull(customer);
        assertEquals(3L, customer.getId());
        assertEquals("345-67-8901", customer.getSsn());
	}

    @Test
	void testGetCustomerPiiEnabled() {
        when(identityContext.isUserInRole("piiEnabled")).thenReturn(true);

        Customer customer = api.getCustomer("3", identityContext);
        
        assertNotNull(customer);
        assertEquals(3L, customer.getId());
        assertEquals("***-**-****", customer.getSsn());
	}

	@Test
	void testCreateAndRetrieveCustomer() {
        when(identityContext.isUserInRole("admin")).thenReturn(true);

        Customer customer = new Customer();

        customer.setId(5L);
        customer.setName(new Name("Bullwinkle Moose"));
        customer.setSsn("a private SSN");
        customer.addContactInfo(ContactInfo.builder()
        		.withAddress(Address.builder()
        			.withStreet1("148 Bonnie Meadow Road")
        			.withCity("New Rochelle")
        			.withState("New York")
        			.build())
        		.withTelephone("817-555-1212")
        		.build());
        CustomerId id = api.createCustomer(customer, identityContext);
        
        assertNotNull(id);
        assertEquals(id.getTenantId(), TENANT_ID.getId());
        
        Customer savedCustomer = api.getCustomer(Long.toString(id.getId()), identityContext);
        
        assertNotNull(savedCustomer);

        assertEquals(id.getId(), savedCustomer.getId());
        assertNotNull(savedCustomer.getContactInfoList());
        assertEquals(1, savedCustomer.getContactInfoList().size());
        assertNotNull(savedCustomer.getContactInfoList().get(0).getAddress());
        assertEquals("148 Bonnie Meadow Road", savedCustomer.getContactInfoList().get(0).getAddress().getStreet());
	}

	@Test
	void testUpdateExistingCustomer() {
		Customer customer = api.getCustomer("7", identityContext);
		assertNotNull(customer);
		assertEquals(0, customer.getContactInfoList().size());
		
		customer.addContactInfo(ContactInfo.builder()
				.withAddress(Address.builder()
						.withStreet1("221B Baker Street")
						.withCity("London")
						.withState("UK")
						.build())
				.withContext(Context.HOME)
				.withTelephone("call the operator")
				.build());
		
		api.updateCustomer(customer, identityContext);
		
		customer = api.getCustomer("7", identityContext);
		
		assertEquals(1, customer.getContactInfoList().size());
		assertEquals("221B Baker Street", customer.getContactInfoList().get(0).getAddress().getStreet());
	}

	@Test
	void testUpdateNewCustomer() {
		assertThrows(UnsupportedOperationException.class, () -> {
			Customer customer = new Customer();

			customer.setId(733L);
			customer.setName(new Name("Bullwinkle Moose"));
			customer.setSsn("a private SSN");
			customer.addContactInfo(ContactInfo.builder()
					.withAddress(Address.builder()
							.withStreet1("148 Bonnie Meadow Road")
							.withCity("New Rochelle")
							.withState("New York")
							.build())
					.withTelephone("817-555-1212")
					.build());

			api.updateCustomer(customer, identityContext);
		});
	}

}
