package org.ptyxiaki.compositionsparser.datamodel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * UnitElement type used in the custom tree data model.
 * unit holds the compilation unit, metrics holds a list
 * with the calculated metrics
 * @author Georgiou Vasileios
 */

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.ptyxiaki.compositionsparser.metrics.LcomGZ;
import org.ptyxiaki.compositionsparser.metrics.LcomHS;
import org.ptyxiaki.compositionsparser.metrics.Metrics;
import org.ptyxiaki.compositionsparser.visitors.AVisitor;

public class UnitElement {

	private ICompilationUnit unit;
	private HashMap<String, Double> metrics;
	private List<UnitElement> components;
	private List<UnitElement> composites;
	private boolean problematic;

	public UnitElement(ICompilationUnit u, HashMap<String, Double> m) {
		this.unit = u;
		this.metrics = m;
		this.components = new ArrayList<UnitElement>();
		this.composites = new ArrayList<UnitElement>();
		this.problematic = false;
	}

	public UnitElement(ICompilationUnit u) {
		this.unit = u;
		metrics = new HashMap<String, Double>();
		calculateMetrics(u);
		this.components = new ArrayList<UnitElement>();
		this.composites = new ArrayList<UnitElement>();
		this.problematic = false;
	}

	public String getElementName() {
		return (this.unit.getElementName());
	}

	public ICompilationUnit getCUnit() {
		return (this.unit);
	}

	public HashMap<String, Double> getMetrics() {
		return (this.metrics);
	}

	public void setComponents(List<UnitElement> i) {
		this.components = i;
	}

	/**
	 * Calculates the cohesion metrics for a given compilation unit, using AST
	 * parser. Value of -1 represents Interface or method without attributes.
	 * 
	 * @param compilationUnit the unit to examine
	 * @author Kapsalis Konstantinos AM 1068 edited by Georgiou Vasileios AM 1040
	 */
	private void calculateMetrics(ICompilationUnit compilationUnit) {
		try {
			IType type = compilationUnit.findPrimaryType();
			if (!type.isInterface()) {
				ASTParser parser = ASTParser.newParser(AST.JLS3);
				parser.setSource(compilationUnit);
				parser.setKind(ASTParser.K_COMPILATION_UNIT);
				parser.setResolveBindings(true);

				final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
				AVisitor astv = new AVisitor(cu);
				cu.accept(astv);

				Metrics m;

				System.out.println("Class : " + compilationUnit.getElementName());
				m = new LcomHS(astv.clash.getAttributes(), astv.clash.getMethods());
				metrics.put("lcomhs", m.calculate());
			} else {
				metrics.put("lcomhs", -1.0);
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printInteractions() {
		for (UnitElement u : this.components) {
			System.out.println("\t" + u.getElementName() + "," + u.getMetrics());
		}
	}

	public void printUnit() {
		System.out.println(this.unit.getElementName() + "," + this.metrics);
		this.printInteractions();
	}

	public List<UnitElement> getComponents() {
		return components;
	}

	public List<UnitElement> getComposites(){
		return composites;
	}
	/**
	 * 
	 * @param String metric
	 * @return the value of given metric.
	 */
	public double getMetric(String metric) {
		if (metric.equals("lcomgz"))
			this.calculateLcomGZ();
		return metrics.get(metric);
	}

	/**
	 * 
	 * @return true if the composite has components with worse LCOM by Henderson -
	 *         Sellers
	 */
	public boolean hasBadComponents() {
		for (UnitElement component : this.components)
			if ((this.getMetric("lcomhs") < component.getMetric("lcomhs")) 
					&& (component.getMetric("lcomhs") > 0)) {
				this.problematic = true;
				break;
			}
		return this.problematic;
	}

	/**
	 * Calculates the composition's LCOM by A. Zarras - V. Georgiou
	 */
	public void calculateLcomGZ() {
		if (!metrics.containsKey("lcomgz")) {
			if (this.hasBadComponents()) {
				LcomGZ m = new LcomGZ(this);
				metrics.put("lcomgz", m.calculate());
			} else
				metrics.put("lcomgz", 0.0);
		}
	}

	public void addComposite(UnitElement u) {
		this.composites.add(u);
	}

	public boolean isComponent() {
		if (this.composites.size() > 0)
			return true;
		return false;
	}
}
