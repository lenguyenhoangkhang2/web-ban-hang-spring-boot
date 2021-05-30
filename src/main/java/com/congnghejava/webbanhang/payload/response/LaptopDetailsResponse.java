package com.congnghejava.webbanhang.payload.response;

import com.congnghejava.webbanhang.models.ProductDetails;

public class LaptopDetailsResponse {
	private String cpu;

	private String ram;

	private String hardDrive;

	private String display;

	private String size;

	private String operatingSystem;

	private String design;

	private String graphicsCard;

	public LaptopDetailsResponse(ProductDetails details) {
		this.cpu = details.getCpu();
		this.ram = details.getRam();
		this.hardDrive = details.getHardDrive();
		this.display = details.getDisplay();
		this.size = details.getSize();
		this.operatingSystem = details.getOperatingSystem();
		this.design = details.getDesign();
		this.graphicsCard = details.getGraphicsCard();
	}

	public String getCpu() {
		return cpu;
	}

	public String getRam() {
		return ram;
	}

	public String getHardDrive() {
		return hardDrive;
	}

	public String getDisplay() {
		return display;
	}

	public String getSize() {
		return size;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public String getDesign() {
		return design;
	}

	public String getGraphicsCard() {
		return graphicsCard;
	}

}
