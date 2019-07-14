package org.ptyxiaki.compositionsparser.handlers;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.swing.JFrame;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.ptyxiaki.compositionsparser.StaticResources;
import org.ptyxiaki.compositionsparser.datamodel.BadComponent;
import org.ptyxiaki.compositionsparser.datamodel.DataOut;
import org.ptyxiaki.compositionsparser.datamodel.ExcelOut;
import org.ptyxiaki.compositionsparser.datamodel.PackageElement;
import org.ptyxiaki.compositionsparser.datamodel.ProjectElement;
import org.ptyxiaki.compositionsparser.datamodel.TextOut;
import org.ptyxiaki.compositionsparser.datamodel.UnitElement;
import org.ptyxiaki.compositionsparser.dialogs.InfoDialog;
import org.ptyxiaki.compositionsparser.dialogs.ThresholdDialog;

public class ExtremesHandler extends AbstractHandler {
	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		InfoDialog info = new InfoDialog("Parse Workspace first!");
		ThresholdDialog dialog = new ThresholdDialog();
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setModal(true);
		dialog.setVisible(true);
		if (StaticResources.workspace != null) {
			for (ProjectElement proj : StaticResources.workspace.getProjects()) {
				try {
					if (proj.getProject().isNatureEnabled(JDT_NATURE)) {
						IJavaProject javaProject = JavaCore.create(proj.getProject());
						List<BadComponent> badComponents = proj.getBadComponents(dialog.getCompositeLcom(), dialog.getComponentLcom());
						DataOut eout = new ExcelOut(javaProject.getElementName() + "_extremes",
								proj.getProject().getLocation().toString());
						((ExcelOut) eout).putExtremes(badComponents);
						info = new InfoDialog("Extremes written to file.");
					}
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					info = new InfoDialog("Error! CoreException!");
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					info = new InfoDialog("Error! FileNotFoundException!");
					e.printStackTrace();
				}
			}
		}
		info.setVisible(true);
		return null;
	}

}
