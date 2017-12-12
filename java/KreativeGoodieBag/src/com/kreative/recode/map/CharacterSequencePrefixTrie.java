package com.kreative.recode.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterSequencePrefixTrie<T> {
	public static class Node<T> {
		private Node<T> parent = null;
		private int parentKey = 0;
		private Map<Integer, Node<T>> children = new HashMap<Integer, Node<T>>();
		private T value = null;
		
		public boolean hasParent() {
			return (this.parent != null);
		}
		
		public Node<T> getParent() {
			return this.parent;
		}
		
		public int getParentKey() {
			return this.parentKey;
		}
		
		public boolean hasChildren() {
			return !this.children.isEmpty();
		}
		
		public boolean hasChild(int key) {
			return this.children.containsKey(key);
		}
		
		public Node<T> getChild(int key) {
			return this.children.get(key);
		}
		
		public void putChild(int key, Node<T> child) {
			Node<T> oldChild = this.children.put(key, child);
			child.parent = this;
			child.parentKey = key;
			if (oldChild != null) {
				oldChild.parent = null;
				oldChild.parentKey = 0;
			}
		}
		
		public void removeChild(int key) {
			Node<T> child = this.children.remove(key);
			if (child != null) {
				child.parent = null;
				child.parentKey = 0;
			}
		}
		
		public boolean hasValue() {
			return (this.value != null);
		}
		
		public T getValue() {
			return this.value;
		}
		
		public void setValue(T value) {
			this.value = value;
		}
		
		public void clearValue() {
			this.value = null;
		}
		
		public boolean isEmpty() {
			return ((value == null) && children.isEmpty());
		}
		
		public int size() {
			int size = ((value == null) ? 0 : 1);
			for (Node<T> child : children.values()) {
				size += child.size();
			}
			return size;
		}
	}
	
	private Node<T> root = new Node<T>();
	
	public void clear() {
		this.root = new Node<T>();
	}
	
	public boolean containsKey(List<Integer> key) {
		Node<T> current = this.root;
		for (int cp : key) {
			if (current.hasChild(cp)) {
				current = current.getChild(cp);
			} else {
				return false;
			}
		}
		return current.hasValue();
	}
	
	public T get(List<Integer> key) {
		Node<T> current = this.root;
		for (int cp : key) {
			if (current.hasChild(cp)) {
				current = current.getChild(cp);
			} else {
				return null;
			}
		}
		return current.getValue();
	}
	
	public Node<T> getRoot() {
		return this.root;
	}
	
	public boolean isEmpty() {
		return this.root.isEmpty();
	}
	
	public void put(List<Integer> key, T value) {
		Node<T> current = this.root;
		for (int cp : key) {
			if (current.hasChild(cp)) {
				current = current.getChild(cp);
			} else {
				Node<T> next = new Node<T>();
				current.putChild(cp, next);
				current = next;
			}
		}
		current.setValue(value);
	}
	
	public void putAll(Map<? extends List<Integer>, ? extends T> m) {
		for (Map.Entry<? extends List<Integer>, ? extends T> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}
	
	public void remove(List<Integer> key) {
		Node<T> current = this.root;
		for (int cp : key) {
			if (current.hasChild(cp)) {
				current = current.getChild(cp);
			} else {
				return;
			}
		}
		current.clearValue();
		while (current.isEmpty() && current.hasParent()) {
			int parentKey = current.getParentKey();
			current = current.getParent();
			current.removeChild(parentKey);
		}
	}
	
	public int size() {
		return this.root.size();
	}
}
