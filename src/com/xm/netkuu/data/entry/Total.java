package com.xm.netkuu.data.entry;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("root")
public class Total {

	@XStreamImplicit(itemFieldName = "m") 
	private List<Media> media;
	
	@XStreamAlias("l")
	private Length length;
	
	public List<Media> getMedia() {
		return media;
	}

	public void setMedia(List<Media> media) {
		this.media = media;
	}
	
	public Length getLength() {
		return length;
	}

	public void setLength(Length length) {
		this.length = length;
	}

	public int length(){
		return length != null ? length.length : 0;
	}
	
	public static class Media{
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
		private String brief;// f
		@XStreamAlias("g")
		private String channel;//g
		@XStreamAlias("h")
		private String site;//h
		@XStreamAlias("i")
		private String region;// i ÇøÓò
		@XStreamAlias("z")
		private String z;//z
		@XStreamAlias("v")
		private String publish;//v
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
		public String getZ() {
			return z;
		}
		public void setZ(String z) {
			this.z = z;
		}
		public String getPublish() {
			return publish;
		}
		public void setPublish(String publish) {
			this.publish = publish;
		}
	}
}
