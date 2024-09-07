package com.smartagro.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class alldata {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String temperature;
	private String humidity;
	private String soilmoisture;
	private String nh3;
	private String co2;
	private String benzene;
	private Date date = new Date();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getSoilmoisture() {
		return soilmoisture;
	}
	public void setSoilmoisture(String soilmoisture) {
		this.soilmoisture = soilmoisture;
	}
	public String getNh3() {
		return nh3;
	}
	public void setNh3(String nh3) {
		this.nh3 = nh3;
	}
	public String getCo2() {
		return co2;
	}
	public void setCo2(String co2) {
		this.co2 = co2;
	}
	public String getBenzene() {
		return benzene;
	}
	public void setBenzene(String benzene) {
		this.benzene = benzene;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	
	

}
