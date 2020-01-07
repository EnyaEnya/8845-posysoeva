package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonDeserialize(builder = CustomerDto.Builder.class)
public class CustomerDto {

    private final Long id;

    private final String firstName;

    private final String lastName;

    private final String email;

    private final String phone;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Long id;

        private String firstName;

        private String lastName;

        private String email;

        private String phone;

        private Builder() {
        }

        private Builder(CustomerDto customerDto) {
            this.id = customerDto.id;
            this.firstName = customerDto.firstName;
            this.lastName = customerDto.lastName;
            this.email = customerDto.email;
            this.phone = customerDto.phone;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public CustomerDto build() {
            return new CustomerDto(this.id, this.firstName, this.lastName, this.email, this.phone);
        }
    }

    private CustomerDto(Long id, String firstName, String lastName, String email, String phone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDto customerDto = (CustomerDto) o;
        return Objects.equals(id, customerDto.id) &&
                Objects.equals(firstName, customerDto.firstName) &&
                Objects.equals(lastName, customerDto.lastName)&&
                Objects.equals(email, customerDto.email)&&
                Objects.equals(phone, customerDto.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phone);
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", first name='" + firstName + '\'' +
                ", last name='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
