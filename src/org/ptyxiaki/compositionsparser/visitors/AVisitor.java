package org.ptyxiaki.compositionsparser.visitors;

/**
 * @author Kapsalis Konstantinos AM 1068
 */

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.ptyxiaki.compositionsparser.metrics.model.*;
import org.ptyxiaki.compositionsparser.metrics.model.Class;

public class AVisitor extends ASTVisitor{

	protected CompilationUnit cu;
	public Class clash;
	protected List<Attribute> attributes;
	protected List<Method> methods;
	
	
	public AVisitor(CompilationUnit cu){
		
		this.cu = cu;
		clash = null;
		attributes = new ArrayList<Attribute>();
		methods = new ArrayList<Method>();
		
	}
	
	public boolean visit(FieldDeclaration node) {		
		String tempType = node.getType().resolveBinding().getQualifiedName().toString();
		List<VariableDeclarationFragment> fragments = node.fragments();
		
		//for(int i=0; i<fragments.size(); i++){
		for (VariableDeclarationFragment f : fragments) {
			String name = f.getName().toString().trim();
			//String name = l.get(i).toString().trim();
			Attribute attribute = new Attribute(tempType ,name);
			attributes.add(attribute);	
		}
		return false; 
	}

	public boolean visit(MethodDeclaration node) {
		
		List<String> parameterType = new ArrayList<String>();
		List<String> argumentName = new ArrayList<String>();
		String[][] parameters = null;
		
		final List<Method> methodInvocations = new ArrayList<Method>();
		
		if(!node.isConstructor()){
			String name = node.getName().toString();
			String returnType=null;
			IMethodBinding methodBinding = node.resolveBinding();
            ITypeBinding type = methodBinding.getReturnType();
            returnType = type.getQualifiedName();

            if(node.parameters().size()!=0){
            	for(int i=0; i<node.parameters().size(); i++){
            	}
            	for(int i=0; i<node.parameters().size(); i++){
            		String[] temp = new String[2];
            		temp = node.parameters().get(i).toString().split(" ");
            		argumentName.add(temp[1].trim());
            	}
            	
        		IMethodBinding binding = node.resolveBinding();
        		node.parameters();
        		if (binding != null) {
        			ITypeBinding[] argumentTypes = binding.getParameterTypes();
            		
        			for(ITypeBinding argumentType : argumentTypes){
    					parameterType.add(argumentType.getQualifiedName());
        			}
        			if(parameterType.size()>0){
        				parameters = new String[parameterType.size()][2];
        				for(int i=0;i<parameterType.size();i++){
        					parameters[i][0] = parameterType.get(i);
        					parameters[i][1] = argumentName.get(i);		
        				}
        			}
        		}
            }
            else{
            	parameters = new String[0][0];
            }		
            final Method methodDeclaration = new Method(returnType,name,parameters);
            methods.add(methodDeclaration);            	
            	
            Block block = node.getBody();
                
            if(block!=null){
                
            	block.accept(new ASTVisitor(){
                public boolean visit(SimpleName node) {
                			
                	Attribute attributeUse;		
                	ITypeBinding classBinding = node.resolveTypeBinding();
                	
                		if(!node.isDeclaration()){
                			if (classBinding != null){
                				attributeUse = new Attribute(classBinding.getQualifiedName(), node.toString());
                					
                				for(Attribute fieldAttribute : attributes){
                    					
                					if(attributeUse.isEqualTo(fieldAttribute)){
                						methodDeclaration.addAttributesThatUses(attributeUse);	
                    				}
                				}
                			}
                		}
                		return true;
                }	
                });		

                block.accept(new ASTVisitor() {
                    public boolean visit(MethodInvocation node) {
                    	String[][] parameters = null;
                    	List<String> arguments = new ArrayList<String>();
                        String returnType = null;
                        String name = node.getName().toString();
                        
                        IMethodBinding binding = node.resolveMethodBinding();
                        if (binding != null) {
                        	
                            ITypeBinding type = binding.getReturnType();
                        	returnType = type.getQualifiedName().toString();
                            ITypeBinding[] argumentTypes = binding.getParameterTypes();
                            
                            if(argumentTypes.length!=0){
                            	for(ITypeBinding argumentType : argumentTypes){
                        			arguments.add(argumentType.getQualifiedName().toString());
                            	}
                            	parameters = new String[arguments.size()][2];
                            	
                            	for(int i=0;i<arguments.size();i++){
                            		parameters[i][0] = arguments.get(i).trim();
                            		parameters[i][1] = "";		
                            	}
                            }
                            else{
                            	parameters = new String[0][0];
                            }
                        }
                        Method methodInvocation = new Method(returnType,name,parameters);
                    	methodInvocations.add(methodInvocation);
                        return true;
                    }
                });
                methodDeclaration.addMethodsInvocations(methodInvocations);
            }
		}
		return true;
	}
	
	
	public void endVisit(CompilationUnit cu){
		
		cu = this.cu;
		clash = new Class(attributes,methods);
		clash.removeForeignMethods();
		clash.clearDuplications();
	}
}
