package org.ptyxiaki.compositionsparser.datamodel;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
/**
 * Tree-type data model storing useful information
 * on open projects.
 * @author Georgiou Vasileios
 *
 */
public class Workspace {
	private List<ProjectElement> projects;

	/**
	 * void constructor creates an empty workspace.
	 */
	public Workspace() {
		this.projects = new ArrayList<ProjectElement>();
		System.out.println("Created empty workspace");
		
	}

	
	/**
	 * Parameterized constructor adds a project element in the
	 * tree for every open project in Eclipse workspace.
	 * @param IProject proj list of projects to import.
	 * @throws Exception if fails to add a project on the tree.
	 */
	public Workspace(List<IProject> proj) throws Exception {
		projects = new ArrayList<ProjectElement>();
		for (IProject pr : proj) {
			try {
				this.addProject(new ProjectElement(pr));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds a projectElement into workspace.
	 * @param ProjectElement p the project to add.
	 * @return true on success, false otherwise.
	 * @throws Exception if fails to add the project.
	 */
	public boolean addProject(ProjectElement p) throws Exception {
		try {
			this.projects.add(p);
		}
		catch (Exception e) {
			e.printStackTrace();
			return(false);
		}
		return(true);
	}
	
	/**
	 * 
	 * @return the list of ProjectElement of workspace
	 */
	public List<ProjectElement> getProjects() {
		return(this.projects);
	}
	
	/**
	 * 
	 * @param IProject p
	 * @return the ProjectElement containig p
	 */
	public ProjectElement getProject(IProject p) {
		for (ProjectElement pr : this.projects) {
			if (p == pr.getProject()) return pr;
		}
		return null;
	}

}
