package com.congnghejava.webbanhang.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "smartphone_details")
public class SmartPhoneDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	private String display;

	@Column(name = "operating_system")
	private String operatingSystem;

	@Column(name = "font_camera")
	private String fontCamera;

	@Column(name = "rear_camera")
	private String rearCamera;

	@Column(name = "chip_name")
	private String chipName;

	@Column(name = "internal_memory")
	private String internalMemory;

	@Column(name = "external_memory")
	private String externalMemory;

	private String sim;

	@Column(name = "battery_capacity")
	private String batteryCapacity;

	public SmartPhoneDetails(Product product, String display, String operatingSystem, String fontCamera,
			String rearCamera, String chipName, String internalMemory, String externalMemory, String sim,
			String batteryCapacity) {
		this.product = product;
		this.display = display;
		this.operatingSystem = operatingSystem;
		this.fontCamera = fontCamera;
		this.rearCamera = rearCamera;
		this.chipName = chipName;
		this.internalMemory = internalMemory;
		this.externalMemory = externalMemory;
		this.sim = sim;
		this.batteryCapacity = batteryCapacity;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getFontCamera() {
		return fontCamera;
	}

	public void setFontCamera(String fontCamera) {
		this.fontCamera = fontCamera;
	}

	public String getRearCamera() {
		return rearCamera;
	}

	public void setRearCamera(String rearCamera) {
		this.rearCamera = rearCamera;
	}

	public String getChipName() {
		return chipName;
	}

	public void setChipName(String chipName) {
		this.chipName = chipName;
	}

	public String getInternalMemory() {
		return internalMemory;
	}

	public void setInternalMemory(String internalMemory) {
		this.internalMemory = internalMemory;
	}

	public String getExternalMemory() {
		return externalMemory;
	}

	public void setExternalMemory(String externalMemory) {
		this.externalMemory = externalMemory;
	}

	public String getSim() {
		return sim;
	}

	public void setSim(String sim) {
		this.sim = sim;
	}

	public String getBatteryCapacity() {
		return batteryCapacity;
	}

	public void setBatteryCapacity(String batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}

}
