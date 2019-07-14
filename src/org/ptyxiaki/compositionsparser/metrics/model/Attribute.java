package org.ptyxiaki.compositionsparser.metrics.model;

/**
 * @author Kapsalis Konstantinos AM 1068
 */

public class Attribute {

	private String name;
	private String type;
	

	public Attribute( String type,String name){
		this.name = new String(name);
		this.type = new String(type);	
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	
	public boolean isEqualTo(Attribute attribute){
		if(this.name.equals(attribute.name) && this.type.equals(attribute.type)){
			return true;
		}
		else return false ;
	}
	
	public boolean equals(Attribute attribute){
		if(this.name.equals(attribute.name) && this.type.equals(attribute.type)){
			return true;
		}
		else return false ;
	}
}
