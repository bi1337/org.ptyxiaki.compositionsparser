package org.ptyxiaki.compositionsparser.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;

public class BadComponent extends UnitElement {
	private List<UnitElement> badComposites;

	public BadComponent(ICompilationUnit u) {
		super(u);
		badComposites = new ArrayList<UnitElement>();
	}
	
	public BadComponent(ICompilationUnit u, HashMap<String, Double> m) {
		super(u, m);
		badComposites = new ArrayList<UnitElement>();
	}

	/**
	 * Add a unit to the list of this component's composites
	 * @param UnitElement the composite
	 */
	@Override
	public void addComposite(UnitElement u) {
		badComposites.add(u);
	}
	
	/**
	 * @return a list of composites for this component
	 */
	@Override
	public List<UnitElement> getComposites() {
		return badComposites;
	}
}
