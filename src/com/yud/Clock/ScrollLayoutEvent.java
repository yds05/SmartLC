/*
 * Copyright (C) 2011 Life Calendar 
 * Author Yu Deshui <yudeshui2007@gmail.com>
*/
package com.yud.Clock;

import java.util.EventListener;
import java.util.EventObject;

public class ScrollLayoutEvent extends EventObject{

	/**
	 * serial versionUID
	 */
	private static final long serialVersionUID = 1L;

	public ScrollLayoutEvent(ScrollLayout source)
	{
		super(source);
	}
	
	public int getCurScreen()
	{
		return ((ScrollLayout)this.getSource()).getCurScreen();
	}
}


interface ScrollLayoutListener extends EventListener
{
	void OnBoundary(ScrollLayoutEvent e);
}