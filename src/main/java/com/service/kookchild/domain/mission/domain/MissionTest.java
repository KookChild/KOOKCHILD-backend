package com.service.kookchild.domain.mission.domain;

import lombok.NoArgsConstructor;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Table(name="missiontest")
public class MissionTest {
	
	@Id
	@Column
	private String title;
	@Column
	private String content;
	@Column
	private int price;
	@Column
	private Date dueDate;
	
	public MissionTest(String title, String content, int price, Date dueDate) {
		this.title = title;
		this.content = content;
		this.price = price;
		this.dueDate = dueDate;
	}

	@Override
	public String toString() {
		return "Mission [title=" + title + ", content=" + content + ", price=" + price + ", dueDate=" + dueDate + "]";
	}
	
	
	
}
