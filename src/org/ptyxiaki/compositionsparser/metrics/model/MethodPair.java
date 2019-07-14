package org.ptyxiaki.compositionsparser.metrics.model;

/**
 * @author Kapsalis Konstantinos AM 1068
 */

import java.util.ArrayList;
import java.util.List;

public class MethodPair {

	private Method methodA;
	private Method methodB;

	public MethodPair(Method a, Method b){
		methodA = a;
		methodB = b;
	}
	
	public List<Method> getMethodPair(){
		List<Method> pair = new ArrayList<Method>();
		pair.add(methodA);
		pair.add(methodB);
		return pair;
	}
	
	
	public Method getMember(int member){
		//if(member==0)return methodA;
		//else return methodB;
		return (member > 0 ? methodB : methodA);
	}
	
	
	public boolean shareAttribute(Attribute attribute){
		if(methodA.hasAttributes() && methodB.hasAttributes()){
			if(methodA.getAttributesThatUses().contains(attribute) 
					&& methodB.getAttributesThatUses().contains(attribute)){
				return true;
			}
		}
		return false;	
	}
	
	
	public boolean isEqualTo(MethodPair methodPair){
		if(this.methodA.equals(methodPair.methodA) && this.methodB.equals(methodPair.methodB)){
			return true;
		}
		else if(this.methodA.equals(methodPair.methodB) && this.methodB.equals(methodPair.methodA)){
			return true;
		}
		return false;
	}

}
