package ru.cft.focusstart.api.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.Objects;

@JsonDeserialize(builder = ProductDto.Builder.class)
public class ProductDto {

    private final Long id;

    private final String title;

    private final long price;

    private final String description;

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {

        private Long id;

        private String title;

        private long price;

        private String description;

        private Builder() {
        }

        private Builder(ProductDto productDto) {
            this.id = productDto.id;
            this.title = productDto.title;
            this.price = productDto.price;
            this.description = productDto.description;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder price(long price) {
            this.price = price;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public ProductDto build() {
            return new ProductDto(this.id, this.title, this.price ,this.description);
        }
    }

    private ProductDto(Long id, String title, long price, String description) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.description = description;
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

    public String getTitle() {
        return title;
    }

    public long getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDto productDto = (ProductDto) o;
        return Objects.equals(id, productDto.id) &&
                Objects.equals(title, productDto.title) &&
                Objects.equals(price, productDto.price) &&
                Objects.equals(description, productDto.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, price, description);
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
