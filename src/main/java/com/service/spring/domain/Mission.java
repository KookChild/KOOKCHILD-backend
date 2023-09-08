package com.service.spring.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="missiontest")
public class Mission {
	
	@Id
	@Column
	private String title;
	@Column
	private String content;
	@Column
	private int price;
	@Column
	private Date dueDate;
	
	public Mission(String title, String content, int price, Date dueDate) {
		this.title = title;
		this.content = content;
		this.price = price;
		this.dueDate = dueDate;
	}
	public Mission() {}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	@Override
	public String toString() {
		return "Mission [title=" + title + ", content=" + content + ", price=" + price + ", dueDate=" + dueDate + "]";
	}
	
	
	
}
