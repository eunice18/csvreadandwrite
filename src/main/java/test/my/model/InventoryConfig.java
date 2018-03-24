package test.my.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "inventory_config")
public class InventoryConfig {

	private Long id;
	private Integer brandNameColumnNumber;
	private Integer itemIdColumnNumber;
	private Integer inventoryValueColumnNumber;
	private Manufacturer manufacturer;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "inventory_config_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getBrandNameColumnNumber() {
		return brandNameColumnNumber;
	}

	public void setBrandNameColumnNumber(Integer brandNameColumnNumber) {
		this.brandNameColumnNumber = brandNameColumnNumber;
	}

	public Integer getItemIdColumnNumber() {
		return itemIdColumnNumber;
	}

	public void setItemIdColumnNumber(Integer itemIdColumnNumber) {
		this.itemIdColumnNumber = itemIdColumnNumber;
	}

	public Integer getInventoryValueColumnNumber() {
		return inventoryValueColumnNumber;
	}

	public void setInventoryValueColumnNumber(Integer inventoryValueColumnNumber) {
		this.inventoryValueColumnNumber = inventoryValueColumnNumber;
	}

	public Boolean checkAlltheConfigValueAvailablity() {

		if (brandNameColumnNumber != null && itemIdColumnNumber != null
				&& inventoryValueColumnNumber != null) {
			return true;
		}
		return false;

	}

	@JsonIgnore
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "inv_man_id", referencedColumnName = "inv_man_id", nullable = true)
	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

}
