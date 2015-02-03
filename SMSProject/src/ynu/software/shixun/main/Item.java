package ynu.software.shixun.main;

public class Item{
	String num;
	String text;
	long _id;
	boolean learned;
	
	public Item(String num,String text,long _id,boolean learned) {
		this.num=num;
		this.text=text;
		this._id=_id;
		this.learned=learned;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub		
		return this.num;
	}
	
}