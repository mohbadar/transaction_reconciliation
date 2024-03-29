package com.tutuka.lib.json.date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class ParentCar {
	public String color;
	ChildCar childCar = new ChildCar();

	public ParentCar(String color) {
		super();
		this.color = color;
	}

	public String getColor() {
		return color;
	}

	public ChildCar getChildCar() {
		return childCar;
	}

	public void setChildCar(ChildCar childCar) {
		this.childCar = childCar;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public ParentCar() {
		this.color = " ";
	}

}
