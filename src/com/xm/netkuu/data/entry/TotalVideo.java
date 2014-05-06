package com.xm.netkuu.data.entry;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("root")
public class TotalVideo {
	@XStreamImplicit(itemFieldName = "m") 
	private List<VideoItem> items; //m
	
	@XStreamAlias("l")
	private Length length;
	
	public List<VideoItem> getItems() {
		return items;
	}

	public void setItems(List<VideoItem> items) {
		this.items = items;
	}

	public void setLength(Length length) {
		this.length = length;
	}

	public int length(){
		return length != null ? length.length : 0;
	}
	
	public int size(){
		return items == null ? 0 : items.size();
	}

	public static class VideoItem{
		@XStreamAlias("a")
		private String name;// a		

		@XStreamAlias("b")
		private String vid;// b		

		@XStreamAlias("c")
		private String actor;// c
		
		@XStreamAlias("d")
		private String director;// d
		
		@XStreamAlias("e")
		private String catalog;// e
		
		@XStreamAlias("f")
		private String brief; // f
		
		@XStreamAlias("g")
		private String channel;// g
		
		@XStreamAlias("h")
		private String site;// h
		
		@XStreamAlias("i")
		private String region;// i
		
		@XStreamAlias("z")
		private Integer z; //z unknown	
		
		@XStreamAlias("v")
		private String adddate;// v	
		
		@XStreamAlias("pl")
		private String shortBrief; // pl
		
		@XStreamAlias("u")
		private Integer rate;// u

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getVid() {
			return vid;
		}

		public void setVid(String vid) {
			this.vid = vid;
		}

		public String getActor() {
			return actor;
		}

		public void setActor(String actor) {
			this.actor = actor;
		}

		public String getDirector() {
			return director;
		}

		public void setDirector(String director) {
			this.director = director;
		}

		public String getCatalog() {
			return catalog;
		}

		public void setCatalog(String catalog) {
			this.catalog = catalog;
		}

		public String getBrief() {
			return brief;
		}

		public void setBrief(String brief) {
			this.brief = brief;
		}

		public String getChannel() {
			return channel;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}

		public String getSite() {
			return site;
		}

		public void setSite(String site) {
			this.site = site;
		}

		public String getRegion() {
			return region;
		}

		public void setRegion(String region) {
			this.region = region;
		}

		public Integer getZ() {
			return z;
		}

		public void setZ(Integer z) {
			this.z = z;
		}

		public String getAdddate() {
			return adddate;
		}

		public void setAdddate(String adddate) {
			this.adddate = adddate;
		}

		public String getShortBrief() {
			return shortBrief;
		}

		public void setShortBrief(String shortBrief) {
			this.shortBrief = shortBrief;
		}

		public Integer getRate() {
			return rate;
		}

		public void setRate(Integer rate) {
			this.rate = rate;
		}
	}
}
