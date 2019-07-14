package org.ptyxiaki.compositionsparser.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;

/**
 * projectElement type used in the custom tree data model.
 * packages holds the list of project's PackageElements
 * @author Georgiou Vasileios
 */
public class ProjectElement {
	private List<PackageElement> packages;
	private IProject currentProject;
	
	/**
	 * Constructor. Creates an empty packageElement list for given project.
	 * @param pr IProject currently working on.
	 */
	public ProjectElement(IProject pr) {
		this.currentProject = pr;
		this.packages = new ArrayList<PackageElement>();
	}
	
	/**
	 * Parameterized constructor.
	 * @param pr IProject currently working on.
	 * @param p a list of packageElements to add in current project.
	 */
	public ProjectElement(IProject pr, List<PackageElement> p) {
		this.currentProject = pr;
		this.packages = p;
	}
	
	/**
	 * Parameterized constructor. Adds a given package to current project.
	 * @param p the IPackageFragment to add as packageElement.
	 * @return true on success.
	 * @throws Exception if fails to add the package.
	 */
	public boolean addPackage(IPackageFragment p) throws Exception {
		try {
			this.packages.add(new PackageElement(p));
		}
		catch (Exception e) {
			e.printStackTrace();
			return(false);
		}
		return(true);
	}
	
	/**
	 * Parameterized constructor. Adds a given package to current project.
	 * @param p the PackageElement to add.
	 * @return true on success.
	 * @throws Exception if fails to add the package.
	 */
	public boolean addPackage(PackageElement p) throws Exception {
		try {
			this.packages.add(p);
		}
		catch (Exception e) {
			e.printStackTrace();
			return(false);
		}
		return(true);
	}
	
	/**
	 * 
	 * @return the project's List of PackageElements
	 */
	public List<PackageElement> getPackages() {
		return(this.packages);
	}
	
	/**
	 * 
	 * @return the current IProject
	 */
	public IProject getProject(){
		return(this.currentProject);
	}
	
	public PackageElement getPackage(IPackageFragment mypackage) {
		for (PackageElement pack : this.packages) {
			if (pack.getPackage() == mypackage) return (pack);
		}
		return null;
	}

	/**
	 * Search on every package on the project for a given unit (ICompilationUnit).
	 * @param string the name of compilation unit to search, including ".java"
	 * @return the UnitElement if the unit exists in project, else null.
	 */
	public UnitElement hasUnit(String string) {
		for (PackageElement pack : this.packages) {
			UnitElement u = pack.searchUnit(string);
			if (u != null) {
				return (u);
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return the size of UnitElement List.
	 */
	public int getUnitsSize() {
		int size = 0;
		for (PackageElement p : this.packages)
			size += p.getUnitsSize();
		return (size);
	}

	/**
	 * 
	 * @return a UnitElement List containing all the units of the project.
	 */
	public ArrayList<UnitElement> getUnits() {
		ArrayList<UnitElement> units = new ArrayList<UnitElement>();
		for (PackageElement p : this.packages)
			units.addAll(p.getUnitElements());
		return units;
	}

	/**
	 * Calls findBadComponents
	 * @param low
	 * @param high
	 * @return A List of bad components
	 */
	public List<BadComponent> getBadComponents(double low, double high) {
		return findBadComponents(low, high);
	}
	
	/**
	 * Find components with lcom >= high which belong to composition
	 * with lcom <= low
	 * @param low
	 * @param high
	 * @return A List of bad components
	 */
	private List<BadComponent> findBadComponents(double low, double high) {
		List<BadComponent> badComponents = new ArrayList<BadComponent>();
		for (UnitElement u : this.getUnits())
			if (u.isComponent() && u.getMetric("lcomhs") >= high) {
				BadComponent badComponent = new BadComponent(u.getCUnit(), u.getMetrics());
				for (UnitElement composite : u.getComposites())
					if ((composite.getMetric("lcomhs")) <= low)
						badComponent.addComposite(composite);
				if (!badComponent.getComposites().isEmpty())
					badComponents.add(badComponent);
			}
		return badComponents;
	}
}
