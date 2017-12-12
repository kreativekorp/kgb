package com.kreative.recode.gui;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class EncodingList extends JList {
	private static final long serialVersionUID = 1L;
	
	private final SortedMap<String,Charset> charsets;
	private final List<String> listData;
	
	public EncodingList() {
		this.charsets = new TreeMap<String,Charset>(String.CASE_INSENSITIVE_ORDER);
		for (Charset charset : Charset.availableCharsets().values()) {
			this.charsets.put(charset.displayName(), charset);
			this.charsets.put(charset.name(), charset);
			for (String name : charset.aliases()) {
				this.charsets.put(name, charset);
			}
		}
		this.listData = new ArrayList<String>();
		this.listData.addAll(this.charsets.keySet());
		setListData(this.listData.toArray());
		addListSelectionListener(new ListSelectionListener() {
			private boolean selecting = false;
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!selecting) {
					selecting = true;
					setEncoding(getEncoding());
					selecting = false;
				}
			}
		});
	}
	
	public Charset getEncoding() {
		Object o = this.getSelectedValue();
		if (o == null) return null;
		else return charsets.get(o.toString());
	}
	
	public void setEncoding(Charset charset) {
		ListSelectionModel selection = this.getSelectionModel();
		selection.clearSelection();
		if (charset != null) {
			boolean scrolled = false;
			int o = listData.indexOf(charset.displayName());
			if (o >= 0) {
				selection.addSelectionInterval(o, o);
				if (!scrolled) {
					this.ensureIndexIsVisible(o);
					scrolled = true;
				}
			}
			o = listData.indexOf(charset.name());
			if (o >= 0) {
				selection.addSelectionInterval(o, o);
				if (!scrolled) {
					this.ensureIndexIsVisible(o);
					scrolled = true;
				}
			}
			for (String name : charset.aliases()) {
				o = listData.indexOf(name);
				if (o >= 0) {
					selection.addSelectionInterval(o, o);
					if (!scrolled) {
						this.ensureIndexIsVisible(o);
						scrolled = true;
					}
				}
			}
		}
	}
	
	public void setEncodingName(String name) {
		if (name == null) setEncoding(null);
		else setEncoding(charsets.get(name));
	}
}
