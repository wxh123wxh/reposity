package com.wx.entity;

public class Unit {

	private long id;
	private String name;
	private String unit_number;
	
	public Unit() {
		
	}
	
	public Unit(String name, String unit_number) {
		super();
		this.name = name;
		this.unit_number = unit_number;
	}

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnit_number() {
		return unit_number;
	}
	public void setUnit_number(String unit_number) {
		this.unit_number = unit_number;
	}
	@Override
	public String toString() {
		return "Unit [id=" + id + ", name=" + name + ", unit_number=" + unit_number + "]";
	}
}
