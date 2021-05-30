package com.congnghejava.webbanhang.payload.response;

import com.congnghejava.webbanhang.models.ProductDetails;

public class SmartPhoneDetailsResponse {
	private String display;

	private String operatingSystem;

	private String fontCamera;

	private String rearCamera;

	private String chipName;

	private String ram;

	private String internalMemory;

	private String sim;

	private String batteryCapacity;

	public SmartPhoneDetailsResponse(ProductDetails details) {
		this.display = details.getDisplay();
		this.operatingSystem = details.getOperatingSystem();
		this.fontCamera = details.getFontCamera();
		this.rearCamera = details.getRearCamera();
		this.chipName = details.getChipName();
		this.ram = details.getRam();
		this.internalMemory = details.getInternalMemory();
		this.sim = details.getSim();
		this.batteryCapacity = details.getBatteryCapacity();
	}

	public String getDisplay() {
		return display;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public String getFontCamera() {
		return fontCamera;
	}

	public String getRearCamera() {
		return rearCamera;
	}

	public String getChipName() {
		return chipName;
	}

	public String getRam() {
		return ram;
	}

	public String getInternalMemory() {
		return internalMemory;
	}

	public String getSim() {
		return sim;
	}

	public String getBatteryCapacity() {
		return batteryCapacity;
	}

}
