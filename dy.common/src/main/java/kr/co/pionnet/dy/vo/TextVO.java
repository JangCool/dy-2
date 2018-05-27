package kr.co.pionnet.dy.vo;

public class TextVO {
	
	public String dataType;
	public int hash;
    public String text;
    
	public TextVO(String dataType, int hash, String txt) {		
		this.dataType = dataType;
		this.hash = hash;
		this.text = txt;		
	}
	
}
