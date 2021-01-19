package org.jrandrews.liveperson.customers.servicecase.domain;

import org.jrandrews.liveperson.customers.tenant.TenantId;

public class ServiceCaseId {
    private long tenantId;
    private long id;

    public ServiceCaseId(TenantId tenantId, long id) {
        this.tenantId = tenantId.getId();
        this.id = id;
    }

    public long getTenantId() {
        return tenantId;
    }

    public long getId() {
        return id;
    }
}
