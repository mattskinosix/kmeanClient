package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class KMeans {
	private ObjectOutputStream out;
	private ObjectInputStream in;
	static JFrame iniziale;
	private String ip;
	private int port;
	static JTabbedPane schermata;
	public void init() throws IOException {
		schermata = new JTabbedPane();
		InetAddress addr = InetAddress.getByName(ip); // ip
		System.out.println("addr = " + addr);
		Socket socket = new Socket(addr, port); // Port
		System.out.println(socket);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		// stream con richieste del client
		System.out.println("OK---connesso");
		iniziale.add(schermata);
		
	}

	public static void main(String[] args) {
		iniziale = new JFrame("KMeans");
		Container c = iniziale.getContentPane();
		iniziale.setLayout(new GridLayout(1, 1));
		System.out.println("OK");
		KMeans k = new KMeans();
		Dialog1 dialog1 = k.new Dialog1(iniziale, "Connessione Server", true);
		dialog1.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(0);
			}
		});
		dialog1.resize(1000, 100);
		dialog1.setVisible(true);
		System.out.println("OK");
		iniziale.setSize(300, 200);
		iniziale.setVisible(true);
		k.new TabbelPane();
	}

	
		public class TabbelPane extends JPanel {
			private JPanelCluster panelDB;
			private JPanelCluster panelFile;

			TabbelPane(){
				panelDB=new JPanelCluster("mine",new Eventa());
				panelFile=new JPanelCluster("store from file",new Eventb());
				schermata.addTab("DB", panelDB);
				schermata.addTab("file",panelFile);
				setVisible(true);
				
			}
			
			class Eventa implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			}
			
			class Eventb implements ActionListener{

				@Override
				public void actionPerformed(ActionEvent e) {
					
					
				}
				
			}
			class JPanelCluster extends JPanel {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				private JLabel labelk = new JLabel();
				private JLabel labelTable = new JLabel();
				private JTextField tableText = new JTextField(20);
				private JTextField kText = new JTextField(10);
				private JTextArea clusterOutput = new JTextArea();
				private JButton executeButton;

				JPanelCluster(String buttonName, java.awt.event.ActionListener a) {
				//	this.setLayout(new BorderLayout());
					JPanel upPanel = new JPanel();
					upPanel.setLayout(new FlowLayout());
					JPanel centralPanel = new JPanel();
					centralPanel.setLayout(new FlowLayout());
					JPanel downPanel = new JPanel();
					downPanel.setLayout(new FlowLayout());
					upPanel.add(labelTable);
					upPanel.add(tableText);
					upPanel.add(labelk);
					upPanel.add(kText);
					centralPanel.add(clusterOutput);
					executeButton=new JButton(buttonName);
					downPanel.add(executeButton);
					executeButton.addActionListener(a);
					this.add(upPanel,BorderLayout.NORTH);
					this.add(centralPanel,BorderLayout.CENTER);
					this.add(downPanel,BorderLayout.SOUTH);
					
					setVisible(true);
				}
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
		}

	

	public class Dialog1 extends JDialog {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;
		JPanel jPanel1 = new JPanel();
		JPanel jPanel2 = new JPanel();
		JButton jButton1 = new JButton();
		JTextField jtext = new JTextField(10);
		JTextField jtext2 = new JTextField(10);
		JLabel label = new JLabel("Insierisci ip");
		JLabel label2 = new JLabel("Insierisci porta");

		public Dialog1(Frame parent, String title, boolean modal) {
			super(parent, title, modal);
			try {
				jbInit();
				pack();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public Dialog1() {
			this(null, "", false);
		}

		private void jbInit() throws Exception {
			jButton1.setText("Invia");
			jButton1.setSize(100, 100);
			getContentPane().setLayout(new FlowLayout());
			getContentPane().add(jPanel1);
			getContentPane().add(jPanel2);
			getContentPane().add(jButton1);
			jPanel1.setLayout(new FlowLayout());
			jPanel1.add(label);
			jPanel1.add(jtext);
			jPanel2.setLayout(new FlowLayout());
			jPanel2.add(label2);
			jPanel2.add(jtext2);
			jButton1.addActionListener(new ServerListener());

		}

		public class ServerListener implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ip = jtext.getText();
				port = Integer.parseInt(jtext2.getText());
				try {
					init();
					dispose();
				} catch (IOException e) {
					e.printStackTrace();

				}
			}

		}

	}

}
