package org.ptyxiaki.compositionsparser.visitors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.ptyxiaki.compositionsparser.datamodel.ProjectElement;
import org.ptyxiaki.compositionsparser.datamodel.UnitElement;

/**
 * A visitor that visits all class instance creations in
 * field declarations in a compilation unit. Searches
 * for object types that exist in the same project and
 * returns a list containing them.
 * @author Georgiou Vasileios AM 1040
 * 
 */
public class VarVisitor extends ASTVisitor {
	private ProjectElement project;
	private List<String> usedClasses;
	private List<UnitElement> interactions;
	
	public VarVisitor() {
		project = null;
		usedClasses = new ArrayList<String>();
		interactions = new ArrayList<UnitElement>();
	}
	
	/**
	 * Parameterized constructor
	 * @param ProjectElement p the project we are working on.
	 */
	public VarVisitor(ProjectElement p){
		project = p;
		usedClasses = new ArrayList<String>();
		interactions = new ArrayList<UnitElement>();
	}
	
	/**
	 * Visitor for class instance creations. 
	 * Reflects composition.
	 */
	@Override
	public boolean visit(FieldDeclaration d) {
			String searchString = new String();
			/* dig into parameterized types */
			if (d.getType().isParameterizedType()) {
				ParameterizedType parType = (ParameterizedType) d.getType();
				/* keep digging until get a simple type */
				while (((Type) parType.typeArguments().get(0)).isParameterizedType()) {
					parType = (ParameterizedType) parType.typeArguments().get(0);
				}
				searchString = parType.typeArguments().get(0).toString() + ".java";
			}
			else if (d.getType().isSimpleType()) {
				searchString = d.getType().toString() + ".java";
			}
			if (project != null) {
				UnitElement u = project.hasUnit(searchString);
				if (u != null) {
					if (!usedClasses.contains(searchString)) {
						usedClasses.add(searchString);
						interactions.add(u);
					}
				}
			}			
		return super.visit(d);
	}

	/**
	 * @return a list of strings containing the class type of objects used in the compilation unit.
	 */
	public List<String> getVars() {
		return (usedClasses);
	}
	
	public List<UnitElement> getInteractions(){
		return this.interactions;
	}
}
