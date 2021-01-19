package org.jrandrews.liveperson.customers.servicecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import javax.ws.rs.core.SecurityContext;

import org.jrandrews.liveperson.customers.servicecase.domain.ServiceCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ServiceCaseResourceTest {
    @Mock
    SecurityContext identityContext;

    private ServiceCaseResource api;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        api = new ServiceCaseResource();
    }

    @Test
    void testGetServiceCase() {
        ServiceCase serviceCase = api.getServiceCase("2", identityContext);
        assertNotNull(serviceCase);
        assertEquals(2L, serviceCase.getId());
    }

}
