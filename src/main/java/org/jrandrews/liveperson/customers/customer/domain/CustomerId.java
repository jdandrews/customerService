package org.jrandrews.liveperson.customers.customer.domain;

import org.jrandrews.liveperson.customers.tenant.TenantId;

/**
 * Strongly-typed customer ID.
 */
public class CustomerId {
    private long tenantId;
    private long id;

    public CustomerId(TenantId tenant, long id) {
        this.tenantId = tenant.getId();
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long getTenantId() {
        return tenantId;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof CustomerId)) return false;

        CustomerId id = (CustomerId) obj;

        return this.tenantId == id.tenantId && this.id == id.id;
    }
}
