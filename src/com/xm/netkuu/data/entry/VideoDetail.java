package com.xm.netkuu.data.entry;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("film")
public class VideoDetail{
	
	private String name;
	private String director;
	private String actor;
	private String region;// µØÇø
	
	@XStreamAlias("channelid")
	private String channel;
	@XStreamAlias("filmtype")
	private String type;
	@XStreamAlias("publishTime")
	private String publish;
	
	private String languages;
	@XStreamAlias("Contentnumber")
	private Integer count;
	private String adddate;
	public String pinglun1;
	public String pinglun2;
	private String brief;
	private Integer jz;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getActor() {
		return actor;
	}

	public void setActor(String actor) {
		this.actor = actor;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPublish() {
		return publish;
	}

	public void setPublish(String publish) {
		this.publish = publish;
	}

	public String getLanguages() {
		return languages;
	}

	public void setLanguages(String languages) {
		this.languages = languages;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getAdddate() {
		return adddate;
	}

	public void setAdddate(String adddate) {
		this.adddate = adddate;
	}

	public String getPinglun1() {
		return pinglun1;
	}

	public void setPinglun1(String pinglun1) {
		this.pinglun1 = pinglun1;
	}

	public String getPinglun2() {
		return pinglun2;
	}

	public void setPinglun2(String pinglun2) {
		this.pinglun2 = pinglun2;
	}

	public String getBrief() {
		return brief.replaceFirst("##nbsp;", "  ").replace("###160;", "  ");
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public Integer getJz() {
		return jz;
	}

	public void setJz(Integer jz) {
		this.jz = jz;
	}

}
