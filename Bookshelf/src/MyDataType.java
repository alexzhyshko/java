
public class MyDataType {
	String nameAuthor;
	String owner;
	String next;
	String taken;
	String exp;
	String status;
	
	public MyDataType(String text1, String text2, String text3, String text4, String text5, String text6) {
		this.nameAuthor = text1;
		this.owner = text2;
		this.taken = text3;
		this.next = text4;
		this.exp = text5;
		this.status= text6;
	}
	public String getNameAuthor() {
		return this.nameAuthor;
	}
	public String getOwner() {
		return this.owner;
	}
	public String getNext() {
		return this.next;
	}
	public String getTaken() {
		return this.taken;
	}
	public String getExpires() {
		return this.exp;
	}
	public String getStatus() {
		return this.status;
	}
}
