package org.ptyxiaki.compositionsparser.metrics;

import org.ptyxiaki.compositionsparser.datamodel.UnitElement;

/**
 * @author Georgiou Vasileios AM 1040
 *
 */
public class LcomGZ {
	private UnitElement unit;

	public LcomGZ(UnitElement u) {
		this.unit = u;
	}

	/**
	 * LCOM of a composite designed by A. Zarras and V. Georgiou
	 * @return the average of the components's LCOM by Henderson - Sellers
	 */
	public double calculate() {
		double sum = 0;

		for (UnitElement interaction : unit.getComponents())
			if (interaction.getMetric("lcomhs") >= 0)
				sum += interaction.getMetric("lcomhs");
		return (sum / unit.getComponents().size());
	}

}
