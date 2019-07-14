package org.ptyxiaki.compositionsparser.metrics.model;

/**
 * @author Kapsalis Konstantinos AM 1068
 * edited by Georgiou Vasileios AM 1040
 */

import java.util.ArrayList;
import java.util.List;

public class Method{

	private String name;
	private String type;
	private String[][] arguments;
	private List<Method> methodInvocations;
	private List<Attribute> attributesThatUses;
	
	public Method(String type ,String name ,String[][] arguments){
		
		this.name = name;
		this.type = type;
		this.arguments = arguments;
		methodInvocations = new ArrayList<Method>();
		attributesThatUses = new ArrayList<Attribute>();
	}
	
	
	public boolean hasTomiBasedOnMethods(Method methodos){
		
		boolean tomi = false;
		if(this.hasMethods() && this.methodInvocations.contains(methodos)){
			tomi=true;
		}
		if(methodos.hasMethods() && methodos.methodInvocations.contains(this)){
			tomi=true;
		}
		return tomi;
	}
	
	public boolean hasTomiBasedOnAttributes(Method methodos) {

		boolean tomi = false;
		if (this.hasAttributes() && methodos.hasAttributes()) {

//				for(int j=0; j<methodos.attributesThatUses.size(); j++){
//					if(this.attributesThatUses.contains(methodos.attributesThatUses.get(j))){
//						tomi=true;
//					}
//				}

			for (Attribute a : this.attributesThatUses)
				for (Attribute b : methodos.getAttributesThatUses())
					if (a.getName().equals(b.getName())) {
						tomi = true;
					}
		}
		return tomi;
	}
	
	
	public int numberOfCommonAttributes(Method method){
		
		int result = 0;
		List<Attribute> mat = method.getAttributesThatUses();
		if(this.hasAttributes() && method.hasAttributes()){
			for(Attribute attribute : attributesThatUses){
				for(Attribute m : mat){
					if(m.isEqualTo(attribute)){
						result++;
					}
				}
			}
		}
		return result;
	}
	
	public void removeNonAttributes(){
		if(attributesThatUses.size()!=0){
			for(Attribute attribute: attributesThatUses){
				if(arguments!=null){
					for(int j=0; j<arguments.length; j++){
				
						if( attribute.getType().equals(arguments[j][0])
							&& attribute.getName().equals(arguments[j][1]) ){
							attributesThatUses.remove(attribute);
						}
					}
				}
			}
		}
	}
	
	public String getName(){
		return name;
	}
	
	public String[][] getArguments(){
		return arguments;
	}
	
	public List<Method> getMethodInvocations(){
    	return methodInvocations;
    }
	
	public List<Attribute> getAttributesThatUses(){
		return attributesThatUses;
    }
	
	public void addMethodsInvocations(List<Method> methodsInvoked){	
		methodInvocations.addAll(methodsInvoked);								
	}
	
	public void addAttributesThatUses(Attribute variable){
		attributesThatUses.add(variable);
	}
	
	public boolean hasMethods(){
    	if (this.methodInvocations.size()>0) return true;
    	return false;
    }
	
	public boolean hasAttributes(){
		if (attributesThatUses.size()>0)return true;
		return false;
	}
	
	public boolean hasArguments(){
		if (arguments.length>0)return true;
		return false;
	}
    
    public void removeMethodInvocation(int position){
    	this.methodInvocations.remove(position);		
    }
    
    public void removeDuplications(){
    	
    	for(int i=0;i<methodInvocations.size()-1;i++){
    		for(int j=i+1; j<methodInvocations.size(); j++){
    			if(methodInvocations.get(i).equals(methodInvocations.get(j))){
    				methodInvocations.remove(j);
    				j--;
    			}
    		}
    	}
    	for(int i=0;i<attributesThatUses.size()-1;i++){
    		for(int j=i+1; j<attributesThatUses.size(); j++){
    			if(attributesThatUses.get(i).isEqualTo(attributesThatUses.get(j))){
    				attributesThatUses.remove(j);
    				j--;
    			}
    		}
    	}	
    }
	
    public void replace(int place ,Method method ){
		this.methodInvocations.remove(place);
		this.methodInvocations.add(place, method);	
	}
    
	public boolean equals(Method method){
		
		boolean check = false;
		if(this.name.equals(method.name) && this.type.equals(method.type)){
			if(this.hasArguments() && method.hasArguments()){
				if(this.arguments.length == method.arguments.length){
					for(int i=0;i<arguments.length;i++){
						if(this.arguments[i][0].equals(method.arguments[i][0])){
								check = true;
						}
					}
				}
			}
			else if(this.arguments!=null && method.arguments!=null){
				check = true;
			}
		}
		return check;
	}
	
	public void getAllSubtreeAttributes(List<Method> metwpoAnazitisis, List<Attribute> totalAttributes,int numOfAttributes){
		
		if(this.hasMethods()){
			for(Method methodInvocation : methodInvocations){
				
				if(methodInvocation.hasAttributes() && !metwpoAnazitisis.contains(methodInvocation)){
					for(Attribute a : methodInvocation.getAttributesThatUses()){
						if(!totalAttributes.contains(a)){
							totalAttributes.add(a);
						}
					}
					if(totalAttributes.size()==numOfAttributes){
						return;
					}
					metwpoAnazitisis.add(methodInvocation);
					methodInvocation.getAllSubtreeAttributes(metwpoAnazitisis, totalAttributes,numOfAttributes);
				}
			}
		}
	}
		
	public void getAllSubtreeMethodInvocations(List<Method> metwpoAnazitisis, List<Method> totalMethods,int numOfMethods){
		if(this.hasMethods()){
			for(Method methodInvocation : methodInvocations){
				if(!metwpoAnazitisis.contains(methodInvocation)){
					totalMethods.add(methodInvocation);
					metwpoAnazitisis.add(methodInvocation);
					methodInvocation.getAllSubtreeMethodInvocations(metwpoAnazitisis, totalMethods, numOfMethods);
				}
			}
		}
	}


	public boolean hasAccessToAttribute(Attribute a) {
		for (Attribute b : this.attributesThatUses) {
			if (a.getName().equals(b.getName())) {
				return true;
			}
		}
		return false;
	}
	
}
