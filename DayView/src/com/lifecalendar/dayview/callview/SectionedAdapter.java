	

package com.lifecalendar.dayview.callview;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.List;

abstract public class SectionedAdapter extends BaseAdapter {
	abstract protected View getHeaderView(int image, String caption,int index,View convertView,ViewGroup parent);
	
	private List<Section> sections=new ArrayList<Section>();
	private static int TYPE_SECTION_HEADER=0;

	public SectionedAdapter() {
		super();
	}

	public void addSection(int image, String caption, Adapter adapter) {
		sections.add(new Section(image, caption, adapter));
	}

	public Object getItem(int position) {
		for (Section section : this.sections) {
			if (position==0) {
				return(section);
			}
			
			int size=section.adapter.getCount()+1;

			if (position<size) {
				return(section.adapter.getItem(position-1));
			}

			position-=size;
		}
		
		return(null);
	}

	public int getCount() {
		int total=0;
		
		for (Section section : this.sections) {
			total+=section.adapter.getCount()+1; // add one for header
		}
		
		System.out.println("Call View Total Count: " + total);
		
		return(total);
	}

	public int getViewTypeCount() {
		int total=1;	// one for the header, plus those from sections
		
		for (Section section : this.sections) {
			total+=section.adapter.getViewTypeCount();
		}
		
		return(total);
	}

	public int getItemViewType(int position) {
		int typeOffset=TYPE_SECTION_HEADER+1;	// start counting from here
		
		for (Section section : this.sections) {
			if (position==0) {
				return(TYPE_SECTION_HEADER);
			}
			
			int size=section.adapter.getCount()+1;

			if (position<size) {
				return(typeOffset+section.adapter.getItemViewType(position-1));
			}

			position-=size;
			typeOffset+=section.adapter.getViewTypeCount();
		}
		
		return(-1);
	}

	public boolean areAllItemsSelectable() {
		return(false);
	}

	public boolean isEnabled(int position) {
		return(getItemViewType(position)!=TYPE_SECTION_HEADER);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		long startTime = System.nanoTime();
		int sectionIndex=0;
		
		for (Section section : this.sections) {
			if (position==0) {
				
				long endTime = System.nanoTime();
		        long val = (endTime - startTime) / 1000L;
		        System.out.println("Call View Position:" + position + "-" + "Section:" + sectionIndex + "-" + val);
		        
				return(getHeaderView(section.image, section.caption, sectionIndex, convertView, parent));
			}

			int size=section.adapter.getCount()+1;

			if (position<size) {
				
				long endTime = System.nanoTime();
		        long val = (endTime - startTime) / 1000L;
		        System.out.println("Call View Position:" + position + "-" + "Section:" + sectionIndex + "-" + val);
		        
				return(section.adapter.getView(position-1, convertView, parent));
			}

			position-=size;
			sectionIndex++;
		}
		
		return(null);
	}

	@Override
	public long getItemId(int position) {
		return(position);
	}

}