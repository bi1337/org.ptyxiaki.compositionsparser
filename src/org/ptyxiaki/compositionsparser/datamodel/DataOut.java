package org.ptyxiaki.compositionsparser.datamodel;

/**
 * Interface for exporting the parser's data to file.
 * @author hitman
 *
 */
public interface DataOut {
	public void putTable(ProjectElement proj);
	public void putLcomGZ(ProjectElement proj);
	public void close();
}
