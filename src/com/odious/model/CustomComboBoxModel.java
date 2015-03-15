package com.odious.model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class CustomComboBoxModel extends AbstractListModel implements ComboBoxModel {
	List<String> list = new ArrayList<String>();
	String selection = null;
	
	public CustomComboBoxModel(List<String> list) {
		this.list = list;
	}
	
	@Override
	public Object getElementAt(int index) {
		return list.get(index);
	}
	
	@Override
	public int getSize() {
		return list.size();
	}
	
	@Override
	public void setSelectedItem(Object anItem) {
		selection = (String) anItem;
	}
	
	@Override
	public Object getSelectedItem() {
		return selection;
	}
}
