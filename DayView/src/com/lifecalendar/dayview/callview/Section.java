package com.lifecalendar.dayview.callview;

import android.widget.Adapter;

public class Section{
	public int image;
	public String caption;
	public Adapter adapter;
	
	public Section(int image, String caption, Adapter adapter){
		this.image = image;
		this.caption = caption;
		this.adapter = adapter;
	}
	
}