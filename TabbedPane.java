package client;

import java.io.IOException;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class TabbedPane extends JPanel {

	private JPanelCluster panelDB;
	private JPanelCluster panelFile;

	class JPanelCluster {
		private JTextField tableText = new JTextField(20);
		private JTextField kText = new JTextField(10);
		private JTextArea clusterOutput = new JTextArea();
		private JButton executeButton;
	}

	TabbedPane() {

	}

	private void learningFromDBAction() throws SocketException, IOException, ClassNotFoundException {
		int numerok = Integer.parseInt(panelDB.kText.getText());
		try {
			numerok = new Integer(panelDB.kText.getText()).intValue();
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, e.toString());
			return;
		}
	}

	String nometab = panelDB.tableText.getText();
	
	
}
