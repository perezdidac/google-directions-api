package com.perezdidac.googledirectionsapi.routes;

public class Duration {

	private String text;
	private double value;

	public Duration(String text, double value) {
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
