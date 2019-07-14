package org.ptyxiaki.compositionsparser.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

/**
 * The packageElement for the custom tree data model.
 * Holds a list of UnitElement with all the
 * units in the package. The list should be populated using an
 * AST visitor, or a custom parser.
 * @author Georgiou Vasileios
 *
 */
public class PackageElement {
	private List<UnitElement> units;
	private IPackageFragment currentPackage;
	
	/**
	 * void constructor. Creates an empty list of UnitElement
	 * 
	 */
	public PackageElement(){
		currentPackage = null;
		units = new ArrayList<UnitElement>();
	}
	
	/**
	 * Parameterized constructor. 
	 * @param p the current package working on.
	 */
	public PackageElement(IPackageFragment p){
		this.currentPackage = p;
		this.units = new ArrayList<UnitElement>();
	}
	
	/**
	 * Parameterized constructor.
	 * @param p the current package working on.
	 * @param l the list of compilation units.
	 */
	public PackageElement(IPackageFragment p, List<ICompilationUnit> l){
		this.currentPackage = p;
		//this.units = l;
		/** TODO:
		 * constructor for list
		 */
	}
	
	/**
	 * Parameterized constructor. 
	 * @param p the current package working on.
	 */
	public PackageElement(PackageElement p) {
		this.currentPackage = p.getPackage();
		this.units = new ArrayList<UnitElement>();
	}

	/**
	 * Adds an ICompilationUnit in the current package.
	 * @param u the unit to add.
	 * @return true on success.
	 * @throws Exception if fails to add the unit.
	 */
	public boolean addUnit(ICompilationUnit u) throws Exception {
		try {
			this.units.add(new UnitElement(u));
		}
		catch (Exception e) {
			e.printStackTrace();
			return(false);
		}
		return(true);
	}
	
	/**
	 * Adds an ICompilationUnit in the current package.
	 * @param u the unit to add.
	 * @param metrics HashMap containing the unit's metrics.
	 * @return true on success.
	 * @throws Exception if fails to add the unit.
	 */
	public boolean addUnit(ICompilationUnit u, HashMap<String,Double> metrics) throws Exception {
		try {
			this.units.add(new UnitElement(u, metrics));
		}
		catch (Exception e) {
			e.printStackTrace();
			return(false);
		}
		return(true);
	}
	
	public IPackageFragment getPackage() {
		return(this.currentPackage);
	}
	
	/**
	 * Searches the current package for the name of a compilation unit.
	 * @param string the name of the unit including ".java"
	 * @return UnitElement if the unit exists in the current package, else null.
	 */
	public UnitElement searchUnit(String string) {
		for (UnitElement u : this.units) {
			if (u.getElementName().equals(string)) {
				return (u);
			}
		}
		return null;
	}

	/**
	 * Wrapper for int org.eclipse.jdt.core.IPackageFragment.getKind() 
	 * @return
	 */
	public int getKind() {
		try {
			return(this.currentPackage.getKind());
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public UnitElement getICUnit(ICompilationUnit u) {
		for (UnitElement i : this.units) {
			if (i.getCUnit() == u) return i;
		}
		return null;
	}

	/**
	 * 
	 * @return the List of package's UnitElements
	 */
	public List<UnitElement> getUnitElements() {
		return (this.units);
	}

	/**
	 * 
	 * @return the size of UnitElement List
	 */
	public int getUnitsSize() {
		return this.units.size();
	}
	
}
