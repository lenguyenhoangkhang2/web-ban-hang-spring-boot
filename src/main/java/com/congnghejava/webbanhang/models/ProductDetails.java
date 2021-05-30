package com.congnghejava.webbanhang.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_details")
public class ProductDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(mappedBy = "details")
	private Product product;

	// LapTop
	private String cpu;

	// LapTop, SmartPhone
	private String ram;

	// LapTop
	@Column(name = "hard_drive")
	private String hardDrive;

	// LapTop, SmartPhone
	private String display;

	// LapTop
	private String size;

	private String graphicsCard;

	// LapTop, SmartPhone
	@Column(name = "operating_system")
	private String operatingSystem;

	// LapTop
	private String design;

	// SmartPhone
	@Column(name = "font_camera")
	private String fontCamera;

	// SmartPhone
	@Column(name = "rear_camera")
	private String rearCamera;

	// SmartPhone
	@Column(name = "chip_name")
	private String chipName;

	// SmartPhone
	@Column(name = "internal_memory")
	private String internalMemory;

	// SmartPhone
	private String sim;

	// SmartPhone
	@Column(name = "battery_capacity")
	private String batteryCapacity;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getRam() {
		return ram;
	}

	public void setRam(String ram) {
		this.ram = ram;
	}

	public String getHardDrive() {
		return hardDrive;
	}

	public void setHardDrive(String hardDrive) {
		this.hardDrive = hardDrive;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getDesign() {
		return design;
	}

	public void setDesign(String design) {
		this.design = design;
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

	public String getGraphicsCard() {
		return graphicsCard;
	}

	public void setGraphicsCard(String graphicsCard) {
		this.graphicsCard = graphicsCard;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
