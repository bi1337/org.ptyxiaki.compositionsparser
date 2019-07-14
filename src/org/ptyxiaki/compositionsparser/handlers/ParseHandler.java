package org.ptyxiaki.compositionsparser.handlers;

import java.util.Arrays;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.ptyxiaki.compositionsparser.StaticResources;
import org.ptyxiaki.compositionsparser.datamodel.PackageElement;
import org.ptyxiaki.compositionsparser.datamodel.ProjectElement;
import org.ptyxiaki.compositionsparser.datamodel.UnitElement;
import org.ptyxiaki.compositionsparser.datamodel.Workspace;
import org.ptyxiaki.compositionsparser.dialogs.InfoDialog;
import org.ptyxiaki.compositionsparser.visitors.VarVisitor;

/**
 * Handler to get the job done when the menu is selected.
 * @author Georgiou Vasileios
 *
 */
public class ParseHandler extends AbstractHandler {
	private static final String JDT_NATURE = "org.eclipse.jdt.core.javanature";

	/**
	 * The menu was clicked. Start execution.
	 */
    public Object execute(ExecutionEvent event) throws ExecutionException {
            IWorkspace wsp = ResourcesPlugin.getWorkspace();
            IWorkspaceRoot root = wsp.getRoot();
            // Get all projects in the workspace
            IProject[] projects = root.getProjects();
            /*create custom data model*/
            try {
				StaticResources.workspace = new Workspace(Arrays.asList(projects));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
            // Loop over all projects
            for (ProjectElement project : StaticResources.workspace.getProjects()) {
            	try {
            		if (project.getProject().isNatureEnabled(JDT_NATURE)) {
            			//Get the packages of project
            			analysePackages(project);
            			findInteractions(project);
            		}
            	} catch (CoreException e) {
            		System.out.println("Cannot add project!");
            		e.printStackTrace();
            	}
            }
            InfoDialog info = new InfoDialog("Done parsing workspace.");
            info.setVisible(true);
            return null;
    }

/**
 * Calls the AST visitor and prints the class interactions for every
 * package in the project
 *     
 * @param project the working project
 * @throws JavaModelException
 */

 private void findInteractions(ProjectElement project) throws JavaModelException {

	 /* for every package */
	 for (PackageElement mypackage : project.getPackages()) {
		 if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
			 /* for every compilation unit */
			 for (UnitElement unit : mypackage.getUnitElements()) {
				 CompilationUnit p = parse(unit.getCUnit());
                 VarVisitor visitor = new VarVisitor(project);
                 p.accept(visitor);
                 unit.setComponents(visitor.getInteractions());
                 for (UnitElement u : unit.getComponents())
                	 u.addComposite(unit);
			 }
		 }
	 }
}
 
 	/**
 	 * Check if a package is a java source file and then get its units.
 	 * @param project the current project.
 	 * @throws JavaModelException
 	 */
    private void analysePackages(ProjectElement project) throws JavaModelException {
        IPackageFragment[] packages = JavaCore.create(project.getProject())
                        .getPackageFragments();
        for (IPackageFragment mypackage : packages) {
                if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
                	/*Populate list with all available packages*/
                	try {
						project.addPackage(mypackage);
					} catch (Exception e) {
						System.out.println("Cannot add package!");
						e.printStackTrace();
					}
                	analyseUnits(mypackage, project);
                }
        }
}
    
    /**
     * Go over every java source file in the package and add every unit in the custom tree data model.
     * @param mypackage the current package.
     * @param project the current project.
     * @throws JavaModelException
     */
    private PackageElement analyseUnits(IPackageFragment mypackage, ProjectElement project)
                    throws JavaModelException {
    		PackageElement pack = project.getPackage(mypackage);
            for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
            		try {
						pack.addUnit(unit);
					} catch (Exception e) {
						System.out.println("Cannot add unit!");
						e.printStackTrace();
					}
            }
    return(pack);
    }

    /**
     * Reads a ICompilationUnit and creates the AST DOM for manipulating the
     * Java source file
     *
     * @param unit
     * @return
     */

    private static CompilationUnit parse(ICompilationUnit unit) {
            ASTParser parser = ASTParser.newParser(AST.JLS11);
            parser.setKind(ASTParser.K_COMPILATION_UNIT);
            parser.setSource(unit);
            parser.setResolveBindings(true);
			return (CompilationUnit) parser.createAST(null);
    }

}