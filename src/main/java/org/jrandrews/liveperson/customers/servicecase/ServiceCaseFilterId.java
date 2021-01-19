package org.jrandrews.liveperson.customers.servicecase;

import org.jrandrews.liveperson.customers.tenant.TenantId;

public class ServiceCaseFilterId {
    private long tenantId;
    private long id;

    public ServiceCaseFilterId(TenantId tenantId, long id) {
        this.tenantId = tenantId.getId();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getTenantId() {
        return tenantId;
    }
}
