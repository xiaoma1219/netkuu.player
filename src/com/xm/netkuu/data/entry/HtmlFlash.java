package com.xm.netkuu.data.entry;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("root")
public class HtmlFlash {
	
	@XStreamImplicit(itemFieldName = "a") 
	private List<HtmlFlashItem> items;
	
	public List<HtmlFlashItem> getItems() {
		return items;
	}

	public void setItems(List<HtmlFlashItem> items) {
		this.items = items;
	}

	public static class HtmlFlashItem{
		
		@XStreamAsAttribute
		@XStreamAlias("href")
		private String href;

		@XStreamAlias("img")
		private Image img;
		
		private String vid = null;
		private String host = null;
		
		public String getVid(){
			if(vid == null){
				split();
			}
			return vid;
		}
		
		public String getHost(){
			if(host == null){
				split();
			}
			return host;
		}
		
		private void split(){
			String[] strs = href.split("?info=");
			host = strs[0];
			if(strs.length > 1)
				vid = strs[1];
		}
		
		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}

		public String getImg() {
			return img.original == null ? img.src : img.original;
		}

		public void setImg(Image img) {
			this.img = img;
		}

		public static class Image{
			
			@XStreamAsAttribute
			@XStreamAlias("src")
			public String src;
			
			@XStreamAsAttribute
			@XStreamAlias("data-original")
			public String original;
		}
	}
}
