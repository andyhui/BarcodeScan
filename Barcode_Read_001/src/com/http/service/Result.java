package com.http.service;

import java.util.Arrays;

public class Result {
	private String[] types;
	private String formatted_address;
	
	public String[] getTypes() {
		return types;
	}
	
	public void setTypes(String[] types) {
		this.types = types;
	}
	
	public String getFormatted_address() {
		return formatted_address;
	}
	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
	
	@Override
	public String toString() {
		return "Result [types=" + Arrays.toString(types)
				+ ", formatted_address=" + formatted_address + "]";
	}

}
