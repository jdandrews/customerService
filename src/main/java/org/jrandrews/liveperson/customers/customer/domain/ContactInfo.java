package org.jrandrews.liveperson.customers.customer.domain;

import javax.annotation.Generated;

public class ContactInfo {
    public enum Context {
        HOME, OFFICE, SKI_CHALET, MARINA, LOVE_SHACK;
    }

    private Context context;
    private Address address;
    private String telephone;

    @Generated("SparkTools")
    private ContactInfo(Builder builder) {
        this.context = builder.context;
        this.address = builder.address;
        this.telephone = builder.telephone;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * Creates builder to build {@link ContactInfo}.
     * 
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link ContactInfo}.
     */
    @Generated("SparkTools")
    public static final class Builder {
        private Context context;
        private Address address;
        private String telephone;

        private Builder() {
        }

        public Builder withContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder withAddress(Address address) {
            this.address = address;
            return this;
        }

        public Builder withTelephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public ContactInfo build() {
            return new ContactInfo(this);
        }
    }
}
