package org.ptyxiaki.compositionsparser.datamodel;

import java.util.ArrayList;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.nio.file.Path;

public class TextOut implements DataOut {
	PrintWriter writer;
	
	public TextOut(String filename, String wd) throws FileNotFoundException, UnsupportedEncodingException {
		Path path = Paths.get(wd,filename+".txt");
		writer = new PrintWriter(path.toString());
	}
	
	public void put(String s) {
		writer.println(s);
	}
	
	public void putUnit(UnitElement u) {
		writer.println(u.getElementName()+"|"+u.getMetrics());
	}
	
	public void putInteraction(UnitElement i) {
		writer.println("\t"+i.getElementName()+"|"+i.getMetrics());
	}
	
	@Override
	public void putTable(ProjectElement proj) {
		ArrayList<UnitElement> units = null;
		units = new ArrayList<UnitElement>();
		for (PackageElement p : proj.getPackages()) {
			for (UnitElement u : p.getUnitElements()) {
				units.add(u);
			}
		}
		
		int[][] interactionMatrix = new int[units.size()][units.size()];
		for (int i=0; i< units.size(); i++)
			for (int j=0; j<units.size(); j++)
				interactionMatrix[i][j]=0;
		
		for (PackageElement p : proj.getPackages()) {
			for (UnitElement u : p.getUnitElements()) {
				List<UnitElement> i = u.getComponents();
				for (UnitElement j : i) {
					interactionMatrix[units.indexOf(u)][units.indexOf(j)]=1;
				}
			}
		}
			writer.println();
			writer.print("\t");
			for (int i=0; i<units.size(); i++)
				writer.print(units.get(i).getElementName());
			writer.println();
			for (int i=0; i< units.size(); i++) {
				writer.print(units.get(i).getElementName());
				for (int j=0; j<units.size(); j++)
					writer.print("\t"+interactionMatrix[i][j]);
				writer.println();
			}
			close();
	}
	
	@Override
	public void close() {
		writer.close();
	}

	@Override
	public void putLcomGZ(ProjectElement proj) {
		// TODO Auto-generated method stub
		
	}
	
}
