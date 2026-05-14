package model;

import java.time.LocalDate;

public class ItemDetails {

    private Long itemId;
    private String description;
    private LocalDate issuedAt;
    private LocalDate expiredAt;

    public ItemDetails() {

    }

    public ItemDetails(Long itemId, String description, LocalDate issuedAt, LocalDate expiredAt) {
        this.itemId = itemId;
        this.description = description;
        this.issuedAt = issuedAt;
        this.expiredAt = expiredAt;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDate issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDate getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDate expiredAt) {
        this.expiredAt = expiredAt;
    }
}
