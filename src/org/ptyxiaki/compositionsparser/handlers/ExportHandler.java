
package org.ptyxiaki.compositionsparser.handlers;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.ptyxiaki.compositionsparser.StaticResources;
import org.ptyxiaki.compositionsparser.datamodel.DataOut;
import org.ptyxiaki.compositionsparser.datamodel.ExcelOut;
import org.ptyxiaki.compositionsparser.datamodel.PackageElement;
import org.ptyxiaki.compositionsparser.datamodel.ProjectElement;
import org.ptyxiaki.compositionsparser.datamodel.TextOut;
import org.ptyxiaki.compositionsparser.datamodel.UnitElement;
import org.ptyxiaki.compositionsparser.dialogs.InfoDialog;

public class ExportHandler extends AbstractHandler {
	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";
	private static DataOut output;

	/**
	 * Export the problematic composites on a text file.
	 * @param ProjectElement project the Project we are working on.
	 */
	private void printProblematicComposites(ProjectElement project) {
		DataOut outfile = null;
		try {
			outfile = new TextOut("problematic", project.getProject().getLocation().toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (PackageElement p : project.getPackages()) {
			for (UnitElement u : p.getUnitElements()) {
				if (u.hasBadComponents())
					((TextOut) outfile).putUnit(u);
			}
		}
		outfile.close();
	}

	@Execute
	public Object execute(ExecutionEvent event) throws ExecutionException {
		InfoDialog info;
		
		if (StaticResources.workspace != null) {
			for (ProjectElement proj : StaticResources.workspace.getProjects()) {
				try {
					if (proj.getProject().isNatureEnabled(JDT_NATURE)) {
						IJavaProject javaProject = JavaCore.create(proj.getProject());
						output = new TextOut(javaProject.getElementName(), proj.getProject().getLocation().toString());
						DataOut eout = new ExcelOut(javaProject.getElementName(),
								proj.getProject().getLocation().toString());
						for (PackageElement p : proj.getPackages()) {
							for (UnitElement u : p.getUnitElements()) {
								((TextOut) output).putUnit(u);
								for (UnitElement i : u.getComponents()) {
									((TextOut) output).putInteraction(i);
								}
							}
						}
						eout.putTable(proj);
						printProblematicComposites(proj);
					}
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				((TextOut) output).close();
			}
			info = new InfoDialog("Data written to file.");
		}
		else {
			info = new InfoDialog("Error! Please parse the workspace first!");
		}
		info.setVisible(true);
		return null;
	}
}