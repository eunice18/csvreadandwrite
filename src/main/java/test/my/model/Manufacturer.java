package test.my.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "inventory_manufacturer")
public class Manufacturer {

	private Long id;
	private String name;
	private String ftpLocation;
	private InventoryConfig inventoryConfig;

	private List<PRCMap> prcMaps = new ArrayList<PRCMap>();

	private Date lastFileCreationDate = null;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "inv_man_id")
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

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "manufacturer", cascade = CascadeType.ALL)
	public InventoryConfig getInventoryConfig() {
		return inventoryConfig;
	}

	public void setInventoryConfig(InventoryConfig inventoryConfig) {
		this.inventoryConfig = inventoryConfig;
	}

	public String getFtpLocation() {
		return ftpLocation;
	}

	public void setFtpLocation(String ftpLocation) {
		this.ftpLocation = ftpLocation;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "manufacturer", cascade = CascadeType.ALL)
	public List<PRCMap> getPrcMaps() {
		return prcMaps;
	}

	public void setPrcMaps(List<PRCMap> prcMaps) {
		this.prcMaps = prcMaps;
	}

	public Date getLastFileCreationDate() {
		return lastFileCreationDate;
	}

	public void setLastFileCreationDate(Date lastFileCreationDate) {
		this.lastFileCreationDate = lastFileCreationDate;
	}

}
