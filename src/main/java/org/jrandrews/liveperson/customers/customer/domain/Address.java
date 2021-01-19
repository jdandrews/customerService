package org.jrandrews.liveperson.customers.customer.domain;

import javax.annotation.Generated;

public class Address {
    // do the research: this needs to be fleshed out
    private String street1;
    private String street2;
    private String city;
    private String state;
    private String postalCode;

    @Generated("SparkTools")
    private Address(Builder builder) {
        this.street1 = builder.street1;
        this.street2 = builder.street2;
        this.city = builder.city;
        this.state = builder.state;
        this.postalCode = builder.postalCode;
    }

    public Address(String street, String street2, String city, String state, String postalCode) {
        this.street1 = street;
        this.street2 = street2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street1;
    }

    public String getStreet2() {
        return street2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Creates builder to build {@link Address}.
     * 
     * @return created builder
     */
    @Generated("SparkTools")
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link Address}.
     */
    @Generated("SparkTools")
    public static final class Builder {
        private String street1;
        private String street2;
        private String city;
        private String state;
        private String postalCode;

        private Builder() {
        }

        public Builder withStreet1(String street1) {
            this.street1 = street1;
            return this;
        }

        public Builder withStreet2(String street2) {
            this.street2 = street2;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withState(String state) {
            this.state = state;
            return this;
        }

        public Builder withPostalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }

}
