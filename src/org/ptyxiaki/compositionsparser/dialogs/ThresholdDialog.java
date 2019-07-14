package org.ptyxiaki.compositionsparser.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class ThresholdDialog extends JDialog {
	protected JSpinner compositeLcomSpinner = new JSpinner();
	protected JSpinner componentLcomSpinner = new JSpinner();

	protected double compositeLcom;
	protected double componentLcom;

	/**
	 * Create the dialog.
	 */
	public ThresholdDialog() {		
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{434, 0};
		gridBagLayout.rowHeights = new int[]{91, 0, 0, 10, 0, 0, 142, 33, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		{
			JLabel lblComponentLcom = new JLabel("Component LCOM");
			GridBagConstraints gbc_lblComponentLcom = new GridBagConstraints();
			gbc_lblComponentLcom.anchor = GridBagConstraints.SOUTH;
			gbc_lblComponentLcom.insets = new Insets(0, 0, 5, 0);
			gbc_lblComponentLcom.gridx = 0;
			gbc_lblComponentLcom.gridy = 1;
			getContentPane().add(lblComponentLcom, gbc_lblComponentLcom);
		}
		{
			componentLcomSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					componentLcom = (double) componentLcomSpinner.getValue();
				}
			});
			componentLcomSpinner.setModel(new SpinnerNumberModel(0.75, 0.0, 1.0, 0.05));
			GridBagConstraints gbc_componentLcom = new GridBagConstraints();
			gbc_componentLcom.ipadx = 20;
			gbc_componentLcom.insets = new Insets(0, 0, 5, 0);
			gbc_componentLcom.gridx = 0;
			gbc_componentLcom.gridy = 2;
			getContentPane().add(componentLcomSpinner, gbc_componentLcom);
		}
		{
			JLabel lblCompositeLcom = new JLabel("Composite LCOM");
			GridBagConstraints gbc_lblCompositeLcom = new GridBagConstraints();
			gbc_lblCompositeLcom.insets = new Insets(0, 0, 5, 0);
			gbc_lblCompositeLcom.gridx = 0;
			gbc_lblCompositeLcom.gridy = 4;
			getContentPane().add(lblCompositeLcom, gbc_lblCompositeLcom);
		}
		{
			compositeLcomSpinner.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					compositeLcom = (double) compositeLcomSpinner.getValue();
				}
			});
			compositeLcomSpinner.setModel(new SpinnerNumberModel(0.25, 0.0, 1.0, 0.05));
			GridBagConstraints gbc_compositeLcom = new GridBagConstraints();
			gbc_compositeLcom.ipadx = 20;
			gbc_compositeLcom.insets = new Insets(0, 0, 5, 0);
			gbc_compositeLcom.gridx = 0;
			gbc_compositeLcom.gridy = 5;
			getContentPane().add(compositeLcomSpinner, gbc_compositeLcom);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.insets = new Insets(0, 0, 5, 0);
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 0;
			gbc_buttonPane.gridy = 6;
			getContentPane().add(buttonPane, gbc_buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						compositeLcom = (double) compositeLcomSpinner.getValue();
						componentLcom = (double) componentLcomSpinner.getValue();
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	public double getCompositeLcom() {
		return compositeLcom;
	}
	
	public double getComponentLcom() {
		return componentLcom;
	}
}
