package com.perezdidac.googledirectionsapi;

public class Distance {

	private String text;
	private double value;
	
	public Distance(String text, double value) {
		setText(text);
		setValue(value);
	}
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
	}
	
}
