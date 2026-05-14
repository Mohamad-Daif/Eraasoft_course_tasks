package model;

public class Item {

	private Long id;
	
	private String name;
	
	private Double price;

	private Integer totalNumber;

	private Boolean isDeleted;

	private Long userId;

	@Override
	public String toString() {
		return "{" +
				"id=" + id +
				", name='" + name + '\'' +
				", price=" + price +
				", totalNumber=" + totalNumber +
				", isDeleted=" + isDeleted +
				", userId=" + userId +
				'}';
	}

	public Item() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Boolean getDeleted() {
		return isDeleted;
	}

	public void setDeleted(Boolean deleted) {
		isDeleted = deleted;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Item(Long id, String name, Double price, Integer totalNumber, Boolean isDeleted, Long userId) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.totalNumber = totalNumber;
		this.isDeleted = isDeleted;
		this.userId = userId;
	}
}
