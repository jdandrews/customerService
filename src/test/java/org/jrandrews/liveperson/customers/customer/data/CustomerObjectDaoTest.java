package org.jrandrews.liveperson.customers.customer.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.jrandrews.liveperson.customers.customer.CustomerFilterExpression;
import org.jrandrews.liveperson.customers.customer.CustomerFilterId;
import org.jrandrews.liveperson.customers.customer.domain.Customer;
import org.jrandrews.liveperson.customers.customer.domain.CustomerId;
import org.jrandrews.liveperson.customers.customer.domain.Name;
import org.jrandrews.liveperson.customers.tenant.TenantId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerObjectDaoTest {
    private static final TenantId TENANT_ID = new TenantId(0);
    private CustomerObjectDao dao;

    @BeforeEach
    void setUp() throws Exception {
        dao = new CustomerDao();
    }

    @Test
    void testCreateAndRetrieveCustomer() {
        Customer customer = new Customer();

        customer.setId(5L);
        customer.setName(new Name("Rocket J Squirrel"));
        customer.setSsn("a private SSN");

        CustomerId id = dao.create(TENANT_ID, customer);

        assertNotNull(id);
        assertNotEquals(5L, id.getId());
        assertEquals(TENANT_ID.getId(), id.getTenantId());

        customer = dao.retrieve(id);
        assertEquals(id.getId(), customer.getId());
        assertEquals("Rocket J Squirrel", customer.getName().getName());
    }

    @Test
    void testUpdateAndRetrieveCustomer() {
        CustomerId id = new CustomerId(TENANT_ID, 5L);

        Customer customer = dao.retrieve(id);
        assertEquals("Eve", customer.getName().getName()); // verify the test data

        customer.setName(new Name("Evelyn"));

        CustomerId updatedId = dao.update(TENANT_ID, customer);
        assertEquals(updatedId, id);
        customer = dao.retrieve(id);
        assertEquals("Evelyn", customer.getName().getName());
    }

    @Test
    void testUpdateNonexistentCustomer() {
        CustomerId id = new CustomerId(TENANT_ID, 21L);

        Customer customer = dao.retrieve(id);
        assertNull(customer);

        assertThrows(UnsupportedOperationException.class, () -> {
            Customer newCustomer = new Customer();

            newCustomer.setId(21L);
            newCustomer.setName(new Name("Rocket J Squirrel"));
            newCustomer.setSsn("a private SSN");

            dao.update(TENANT_ID, newCustomer);
        });
    }

    @Test
    void testCreateAndRetrieveFilter() {
        CustomerFilterExpression ex = new CustomerFilterExpression();
        ex.idList.add(8L);
        ex.idList.add(4L);

        CustomerFilterId id = dao.create(TENANT_ID, ex);

        assertNotNull(id);
        assertEquals(TENANT_ID.getId(), id.getTenantId());

        CustomerFilterExpression savedEx = dao.retrieve(id);
        assertIterableEquals(savedEx.idList, ex.idList);
    }
}
