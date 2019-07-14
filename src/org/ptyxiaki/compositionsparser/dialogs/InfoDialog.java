package org.ptyxiaki.compositionsparser.dialogs;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import java.awt.Window.Type;

public class InfoDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	/**
	 * Create the dialog.
	 */
	public InfoDialog(String s) {
		setType(Type.POPUP);
		setTitle("Info");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(InfoDialog.class.getResource("/javax/swing/plaf/metal/icons/ocean/info.png")));
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JTextPane textPane = new JTextPane();
			textPane.setEditable(false);
			contentPanel.add(textPane);
			textPane.setText(s);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseReleased(MouseEvent arg0) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
