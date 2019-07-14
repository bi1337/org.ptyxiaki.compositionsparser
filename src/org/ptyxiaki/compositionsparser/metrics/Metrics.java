package org.ptyxiaki.compositionsparser.metrics;

/**
 * @author Kapsalis Konstantinos AM 1068
 */

import java.util.List;

import org.ptyxiaki.compositionsparser.metrics.model.Attribute;
import org.ptyxiaki.compositionsparser.metrics.model.Class;
import org.ptyxiaki.compositionsparser.metrics.model.Method;
import org.ptyxiaki.compositionsparser.metrics.model.MethodPair;


public abstract class Metrics extends Class{

	
	public Metrics(List<Attribute> attributes, List<Method> methods) {
		super(attributes, methods);
	}
	
	public abstract double calculate();
	
	public double  totalMethodPairs(){
		
		if(this.getMethods().size()<2)return -1;
		else if(this.getMethods().size()==2){
			return 1;
		}
		else if(this.getMethods().size()!=0) {
			return this.getMethods().size()*(this.getMethods().size()-1)/2;
		}
		else return 0;
	}
	
	public int methodPairsThatShareAttributes(){
		int result= 0;
		if(this.getMethodPairs()!=null){
			for(MethodPair methodPair : this.getMethodPairs()){
				if(methodPair.getMember(0).hasTomiBasedOnAttributes(methodPair.getMember(1))){
					result++;
				}
			}
		}
		return result;
	}
	
	public int methodPairsThatDontShareAttributes(){
		int result= 0;
		if(this.getMethodPairs()!=null){
			for(MethodPair methodPair : this.getMethodPairs()){
				if(!methodPair.getMember(0).hasTomiBasedOnAttributes(methodPair.getMember(1))){
					result++;
				}
			}
		}
		return result;
	}	
}
