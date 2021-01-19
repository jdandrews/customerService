package org.jrandrews.liveperson.customers.customer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.SecurityContext;

import org.jrandrews.liveperson.customers.customer.CustomersResource.CustomerPage;
import org.jrandrews.liveperson.customers.customer.domain.Customer;
import org.jrandrews.liveperson.customers.tenant.TenantId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CustomersResourceTest {
    private static final TenantId TENANT_ID = new TenantId(0);

    @Mock
    SecurityContext identityContext;

    CustomersResource api;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        api = new CustomersResource();
    }

    @Test
    void testCreateAndExecuteSearchAdmin() {
        Mockito.when(identityContext.isUserInRole("admin")).thenReturn(true);

        CustomerFilterExpression filter = new CustomerFilterExpression();
        filter.idList = Arrays.asList(1L, 3L, 5L);

        CustomerFilterId filterId = api.createSearch(filter, identityContext);

        assertNotNull(filterId);
        assertEquals(TENANT_ID.getId(), filterId.getTenantId());

        CustomerPage result = api.getCustomers(Long.toString(filterId.getId()), 1, identityContext);

        assertNotNull(result);
        assertNotNull(result.getResultList());
        assertEquals(3, result.getResultList().size());

        List<Long> resultIds = result.getResultList().stream().map(Customer::getId).collect(Collectors.toList());
        Collections.sort(resultIds);

        assertIterableEquals(filter.idList, resultIds);

        // verify no masking
        assertFalse(result.getResultList().get(0).getSsn().contains("*"));
    }

    @Test
    void testCreateAndExecuteSearchNotAdmin() {
        Mockito.when(identityContext.isUserInRole("admin")).thenReturn(false);
        Mockito.when(identityContext.isUserInRole("piiEnabled")).thenReturn(true);

        CustomerFilterExpression filter = new CustomerFilterExpression();
        filter.idList = Arrays.asList(1L, 3L, 5L);

        CustomerFilterId filterId = api.createSearch(filter, identityContext);

        assertNotNull(filterId);
        assertEquals(TENANT_ID.getId(), filterId.getTenantId());

        CustomerPage result = api.getCustomers(Long.toString(filterId.getId()), 1, identityContext);

        assertNotNull(result);
        assertNotNull(result.getResultList());
        assertEquals(3, result.getResultList().size());

        List<Long> resultIds = result.getResultList().stream().map(Customer::getId).collect(Collectors.toList());
        Collections.sort(resultIds);

        assertIterableEquals(filter.idList, resultIds);

        // verify masking
        assertTrue(result.getResultList().get(0).getSsn().contains("*"));
    }
}
