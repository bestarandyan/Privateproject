package com.example.riversystem;
/**
 * river bean
 * @author Administrator
 *
 */
public class River {
	
	private String id;
	private String name;
	private String introduction;
	private int length;
	private String imageUrl;
	
	public River() {
	}
	
	public River(String name,int length, String introduction, 
			String imageUrl) {
		this.name = name;
		this.introduction = introduction;
		this.length = length;
		this.imageUrl = imageUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}


	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	
	

}
