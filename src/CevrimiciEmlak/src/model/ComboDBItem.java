package model;

public class ComboDBItem {
	public int ElementID;
	public String Element;
	public ComboDBItem(int ElementID, String Element){
		this.ElementID = ElementID;
		this.Element = Element;
	}
	public String toString(){
		return Element;
	}
	public int toInt(){
		return ElementID;
	}
}
