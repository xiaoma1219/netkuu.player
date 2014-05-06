package com.xm.netkuu.data.entry;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ListFlash {

	@XStreamAlias("img")
	private String img;

	@XStreamAlias("url")
	private String url;	

	@XStreamAlias("yu")
	private String yu;

	@XStreamAlias("title")
	private String title;

	@XStreamAlias("content")
	private String content;

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getYu() {
		return yu;
	}

	public void setYu(String yu) {
		this.yu = yu;
	}

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
}
