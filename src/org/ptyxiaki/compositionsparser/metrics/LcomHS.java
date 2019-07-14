package org.ptyxiaki.compositionsparser.metrics;

/**
 * @Author Georgiou Vasileios AM 1040
 */
import java.util.List;

import org.ptyxiaki.compositionsparser.metrics.model.Attribute;
import org.ptyxiaki.compositionsparser.metrics.model.Method;

public class LcomHS extends Metrics {

	public LcomHS(List<Attribute> attributes, List<Method> methods) {
		super(attributes, methods);
	}

	@Override
	public double calculate() {
		double ma = 0;
		if ((this.getMethods().size() == 0) || (this.getAttributes().size() == 0))
			return -1;
		for (Attribute a : this.getAttributes()) {
			for (Method m : this.getMethods()) {
				if (m.hasAccessToAttribute(a)) {
					ma++;
				}
			}
		}
		double lcomHS = 1 - (ma / (this.getMethods().size() * this.getAttributes().size()));
//		System.out.print("ma : " + ma + " methods : " + this.getMethods().size() + " attributes : " + this.getAttributes().size() + "\n");
//		System.out.println("LcomHS : " + lcomHS);
		return lcomHS;
	}

}
