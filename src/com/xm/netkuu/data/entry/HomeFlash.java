package com.xm.netkuu.data.entry;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("list") 
public class HomeFlash {
	@XStreamImplicit(itemFieldName = "img") 
	private List<String> img;
	@XStreamImplicit(itemFieldName = "url")
	private List<String> url;
	@XStreamImplicit(itemFieldName = "yu")
	private List<String> yu;
	@XStreamImplicit(itemFieldName = "title")
	private List<String> title;
	@XStreamImplicit(itemFieldName = "content")
	private List<String> content;

	public List<String> getImg() {
		return img;
	}

	public void setImg(List<String> img) {
		this.img = img;
	}

	public List<String> getUrl() {
		return url;
	}

	public void setUrl(List<String> url) {
		this.url = url;
	}

	public List<String> getYu() {
		return yu;
	}

	public void setYu(List<String> yu) {
		this.yu = yu;
	}

	public List<String> getTitle() {
		return title;
	}

	public void setTitle(List<String> title) {
		this.title = title;
	}

	public List<String> getContent() {
		return content;
	}

	public void setContent(List<String> content) {
		this.content = content;
	}
}
