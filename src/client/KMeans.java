package client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
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

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
		iniziale.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(0);
			}
		});
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
		KMeans k = new KMeans();
		Dialog1 dialog1 = k.new Dialog1(iniziale, "Connessione Server", true);
		dialog1.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(0);
			}
		});
		dialog1.setSize(1000, 100);
		dialog1.setVisible(true);
		iniziale.setSize(500, 600);
		iniziale.setVisible(true);
		k.new TabbelPane();
	}

	
		public class TabbelPane extends JPanel {
			private JPanelCluster panelDB;
			private JPanelCluster panelFile;

			TabbelPane(){
				panelDB=new JPanelCluster("mine",new EventFromDb());
				panelFile=new JPanelCluster("store from file",new EventFromFile());
				ImageIcon icon = new ImageIcon("database.png");
				Image image= icon.getImage();
				Image newimage=image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
				icon.setImage(newimage);
				schermata.addTab("DB",icon, panelDB);
				icon = new ImageIcon("file.png");
				image= icon.getImage();
				newimage=image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
				icon.setImage(newimage);
				schermata.addTab("file",icon,panelFile);
				setVisible(true);
				
			}
			
			class EventFromDb implements ActionListener{
				String nometab;
				int k;
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						out.writeObject(0);
						nometab=panelDB.tableText.getText();
						out.writeObject(nometab);
						String ris=(String) in.readObject();
						if(ris.equals("OK")) {
							out.writeObject(1);
							k=Integer.parseInt(panelDB.kText.getText());
							out.writeObject(k);
							ris=(String)in.readObject();
							if(ris.equals("OK")) {
								panelDB.clusterOutput.setText((String)in.readObject());
							}else {
								panelDB.clusterOutput.setText(ris);
							}
						}else {						
							panelDB.clusterOutput.setText(ris);
						}
					} catch (IOException | ClassNotFoundException e1) {
						e1.printStackTrace();
						panelDB.clusterOutput.setText("Si e' verificato un errore.");
					}
					//rivedere eccezioni e eventuale tastp salvafile
				}
				
			}
			
			class EventFromFile implements ActionListener{
				String nomefile;
				@Override
				public void actionPerformed(ActionEvent e) {
						try {
							out.writeObject(3);
							nomefile=panelFile.tableText.getText();
							out.writeObject(nomefile);
							String ok= (String) in.readObject();
							if(ok.equals("OK")) {
								panelFile.clusterOutput.setText((String)in.readObject());
							}else {
								panelFile.clusterOutput.setText("Nome file errato,controlla di non aver inserito l'estensione");
							}
						} catch (IOException | ClassNotFoundException e1) {
							e1.printStackTrace();
						}
				}
				
			}
			class JPanelCluster extends JPanel {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				private JLabel labelk = new JLabel("K");
				private JLabel labelTable = new JLabel("Table");
				private JTextField tableText = new JTextField(20);
				private JTextField kText = new JTextField(10);
				private JTextArea clusterOutput = new JTextArea();
				private JButton executeButton;

				JPanelCluster(String buttonName, java.awt.event.ActionListener a) {
					this.setLayout(new BorderLayout());
					JPanel upPanel = new JPanel();
					upPanel.setLayout(new FlowLayout());
					JPanel centralPanel = new JPanel();
					centralPanel.setLayout(new FlowLayout());
					JPanel downPanel = new JPanel();
					downPanel.setLayout(new FlowLayout());
					upPanel.add(labelTable);
					upPanel.add(tableText);
					if(buttonName.equals("mine")) {
						upPanel.add(labelk);
						upPanel.add(kText);
					}
					centralPanel.add(clusterOutput);
					executeButton=new JButton(buttonName);
					downPanel.add(executeButton);
					executeButton.addActionListener(a);
					this.add(upPanel,BorderLayout.NORTH);
					this.add(centralPanel,BorderLayout.CENTER);
					this.add(downPanel,BorderLayout.SOUTH);
					JScrollPane sp=new JScrollPane(clusterOutput);
					this.add(sp);
					setVisible(true);
				}
			}

			/*private void learningFromDBAction() throws SocketException, IOException, ClassNotFoundException {
				int numerok = Integer.parseInt(panelDB.kText.getText());
				try {
					numerok = new Integer(panelDB.kText.getText()).intValue();
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(this, e.toString());
					return;
				}
				
				
			
			}*/
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
