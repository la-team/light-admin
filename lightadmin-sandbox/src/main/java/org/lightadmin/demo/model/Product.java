package org.lightadmin.demo.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.util.Assert;

@Entity
public class Product extends AbstractEntity {

    @NotNull
    @Size(min = 10, max = 100)
    private String name;

    @Basic
    private String type;

    @NotNull
    @Column(name = "product_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @Size(max = 255)
    private String description;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY)
    private BigDecimal price;

    @ElementCollection
    private Map<String, String> attributes;

    @Basic
    private Boolean retired;

    @Temporal(TemporalType.DATE)
    @Column(name = "REL_DATE")
    private Date releaseDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REL_TIME")
    private Date releaseTime;

    @Lob
    @Column(name = "PICTURE")
    //@FileReference.Constraints("jpg,jpeg,png,gif")
    private byte[] picture;

    @Column(name = "UUID_NUM")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID uuid;

    public Product(String name, BigDecimal price) {
        this(name, price, null);
    }

    public Product(String name, BigDecimal price, String description) {
        Assert.hasText(name, "Name must not be null or empty!");
        Assert.isTrue(BigDecimal.ZERO.compareTo(price) < 0, "Price must be greater than zero!");

        this.name = name;
        this.price = price;
        this.description = description;
    }

    public Product() {
    }

    public void setAttribute(String name, String value) {
        Assert.hasText(name);

        if (value == null) {
            this.attributes.remove(value);
        } else {
            this.attributes.put(name, value);
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Boolean getRetired() {
        return retired;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(final byte[] picture) {
        this.picture = picture;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}