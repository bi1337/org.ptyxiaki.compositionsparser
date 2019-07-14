package org.ptyxiaki.compositionsparser.metrics.model;

/**
 * @author Kapsalis Konstantinos AM 1068
 */

import java.util.ArrayList;
import java.util.List;

public class Class {

	private List<Attribute> attributes;
	private List<Method> methods;
	private List<MethodPair> methodPairs;
	
	public Class( List<Attribute> attributes, List<Method> methods){
		
		this.attributes = attributes;
		this.methods = methods;
		methodPairs = new ArrayList<MethodPair>();
		createMethodPairs();
	}
	
	public void createMethodPairs(){
		if(this.getMethods().size()>=2){
			for(int i=0; i<this.getMethods().size()-1; i++){
				for(int j=i+1; j<this.getMethods().size(); j++){
					MethodPair pair = new MethodPair(this.getMethods().get(i),this.getMethods().get(j));
					methodPairs.add(pair);
				}
			}
		}
	}
	
	public List<MethodPair> getMethodPairs(){
		return methodPairs;
	}
	
	public boolean hasMethodPairs(){
    	if (this.methodPairs.size()>0) return true;
    	return false;
    }
	
	
	public void removeForeignMethods(){

		int counter=0;
		int importantMethod=0;
		
		for(Method method : methods){
			if(method.hasMethods()){	
				for(int j=0; j<method.getMethodInvocations().size(); j++){
					for(int k=0; k<methods.size(); k++){
							if(!method.getMethodInvocations().get(j).equals(methods.get(k))){
								counter++;
							}
							else {
								importantMethod=k;
							}
					}					
					if(counter == methods.size()){
						method.removeMethodInvocation(j);
						j--;
						counter=0;
					}
					else{
						method.replace(j, methods.get(importantMethod) );
						counter=0;
						importantMethod=0;
					}	
				}
			}
		}
	}
	
	public void checkMethodsAttributes(){
		
		for(int i=0;i<methods.size();i++){
			methods.get(i).removeNonAttributes();
		}	
	}
	
	public void clearDuplications(){
		for( Method method : methods ){
			method.removeDuplications();
		}
	}
	
	public List<Method> getMethods(){
		return methods;	
	}
	
	public List<Attribute> getAttributes(){
		return attributes;	
	}	
}
