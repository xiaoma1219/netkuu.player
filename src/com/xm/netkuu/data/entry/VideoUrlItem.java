package com.xm.netkuu.data.entry;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class VideoUrlItem {
	@XStreamAlias("a")
	private String name;
	@XStreamAlias("b")
	private String items;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItems() {
		return items;
	}
	
	public String[] getItemsArray(){
		items = items.substring(0, items.length() - 2);
		String[] array = items.split(",");
		for(int i = 0; i < array.length; i++){
			array[i] = array[i].replace("\n", "");
		}
		return array;
	}

	public void setItems(String items) {
		this.items = items;
	}

}
