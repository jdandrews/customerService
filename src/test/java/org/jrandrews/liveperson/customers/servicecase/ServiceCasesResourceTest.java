package org.jrandrews.liveperson.customers.servicecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.SecurityContext;

import org.jrandrews.liveperson.customers.servicecase.ServiceCasesResource.ServiceCasePage;
import org.jrandrews.liveperson.customers.servicecase.domain.ServiceCase;
import org.jrandrews.liveperson.customers.tenant.TenantId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ServiceCasesResourceTest {
    private static final TenantId TENANT_ID = new TenantId(0L);

    @Mock
    SecurityContext identityContext;

    private ServiceCasesResource api;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        api = new ServiceCasesResource();
    }

    @Test
    void testCreateAndExecuteSearch() {
        ServiceCaseFilterExpression filter = new ServiceCaseFilterExpression();
        filter.customerId = 7L;

        ServiceCaseFilterId filterId = api.createSearch(filter, identityContext);

        assertNotNull(filterId);
        assertEquals(TENANT_ID.getId(), filterId.getTenantId());

        ServiceCasePage result = api.getServiceCasesForCustomer(Long.toString(filterId.getId()), identityContext);

        assertNotNull(result);
        assertNotNull(result.getResultList());
        assertEquals(2, result.getResultList().size());

        List<Long> resultIds = result.getResultList().stream().map(ServiceCase::getId).collect(Collectors.toList());
        Collections.sort(resultIds);

        assertTrue(resultIds.contains(1L));
        assertTrue(resultIds.contains(2L));
    }

}
