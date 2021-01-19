package org.jrandrews.liveperson.customers.servicecase.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.jrandrews.liveperson.customers.customer.domain.CustomerId;
import org.jrandrews.liveperson.customers.servicecase.ServiceCaseFilterExpression;
import org.jrandrews.liveperson.customers.servicecase.ServiceCaseFilterId;
import org.jrandrews.liveperson.customers.servicecase.domain.ServiceCaseId;
import org.jrandrews.liveperson.customers.tenant.TenantId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServiceCaseDaoTest {
    private static final TenantId TENANT_ID = new TenantId(0L);
    ServiceCaseDao dao;

    @BeforeEach
    void setUp() throws Exception {
        dao = new ServiceCaseDao();
    }

    @Test
    void testRetrieveServiceCaseId() {
        assertNotNull(dao.retrieve(new ServiceCaseId(TENANT_ID, 2)));
        assertNull(dao.retrieve(new ServiceCaseId(TENANT_ID, 3)));
    }

    @Test
    void testRetrieveCustomerId() {
        List<ServiceCaseId> result = dao.retrieveCaseIdsFor(new CustomerId(TENANT_ID, 7));
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testCreateAndRetrieveFilter() {
        ServiceCaseFilterExpression ex = new ServiceCaseFilterExpression();
        ex.customerId = 7L;

        ServiceCaseFilterId id = dao.create(TENANT_ID, ex);

        assertNotNull(id);
        assertEquals(TENANT_ID.getId(), id.getTenantId());

        ServiceCaseFilterExpression savedEx = dao.retrieve(id);
        assertEquals(7L, savedEx.customerId);
    }
}
