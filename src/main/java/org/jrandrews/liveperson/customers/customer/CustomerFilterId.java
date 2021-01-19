package org.jrandrews.liveperson.customers.customer;

import org.jrandrews.liveperson.customers.tenant.TenantId;

public class CustomerFilterId {
    private long tenantId;
    private long id;

    public CustomerFilterId(TenantId tenantId, long id) {
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
