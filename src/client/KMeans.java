package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * 
 * @author Mirko. Classe che rappresenta un client che comunica con un server.
 *
 */
@SuppressWarnings("serial")
class KMeans {
	/**
	 * Oggetto che indica il flusso in uscita dal client.
	 */
	private ObjectOutputStream out;
	/**
	 * Oggetto che indica il in entrata nel client.
	 */
	private ObjectInputStream in;
	/**
	 * Oggetto di tipo JFrame che indica il Frame principale.
	 */
	static JFrame iniziale;
	/**
	 * Oggetto di tipo stringa che indica l'indirizzo ip del server al quale ci si
	 * vuol collegare.
	 */
	private String ip;
	/**
	 * Indica la porta del server.
	 */
	private int port;
	/**
	 * Oggetto di tipo JtabbedPane, e indica la struttura interna del frame.
	 */
	static JTabbedPane schermata;

	/**
	 * Inizializza la componente grafica della interfaccia grafica istanziando un
	 * oggetto della classe JtabbedPane e aggiungendolo al container della JFrame.
	 * Inoltre avvia la richiesta di connessione al Server ed inizializza i flussi
	 * di comunicazione (membri dato in e out).
	 * 
	 * @throws IOException
	 *             Eccezzione sollevata per problemi dell I/O.
	 */
	private void init() throws IOException {
		schermata = new JTabbedPane();
		InetAddress addr = InetAddress.getByName(ip); // ip
		Socket socket = new Socket(addr, port); // Port
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		// stream con richieste del client
		iniziale.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				try {
					socket.close();
				} catch (IOException e) {
				}
				System.exit(0);
			}
		});
		iniziale.add(schermata);

	}

	/**
	 * Metodo principale che permette di richiamare la finestra di dialogo dialog
	 * per poter reperire le informazioni relative all'indirizzo ip e la port. La
	 * aggiunge al frame principale.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		iniziale = new JFrame("KMeans");
		iniziale.setLayout(new GridLayout(1, 1));
		KMeans k = new KMeans();
		Dialog1 dialog = k.new Dialog1(iniziale, "Connessione Server", true);
		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(0);

			}
		});
		dialog.setSize(1000, 100);
		dialog.setVisible(true);
		iniziale.setSize(500, 600);
		iniziale.setVisible(true);
		k.new TabbelPane();
	}

	/**
	 * Inner class di KMeans, indica la struttura interna del frame e estende
	 * JPanel.
	 * 
	 * @author Mirko.
	 *
	 */
	private class TabbelPane extends JPanel {
		/**
		 * Oggetto di tipo JPanelCluster, indica la struttura interna al frame per
		 * quando riguarda l'utilizzo del database.
		 */
		private JPanelCluster panelDB;
		/**
		 * Oggetto di tipo JPanelCluster ,indica la struttura internda al frame per
		 * quanto riguarda l'utilizzo dei file.
		 */
		private JPanelCluster panelFile;

		/**
		 * Costruttore di classe, definisce il layout di un JPanelCluster.
		 */
		TabbelPane() {
			panelDB = new JPanelCluster("mine", new EventFromDb());
			panelFile = new JPanelCluster("store from file", new EventFromFile());
			ImageIcon icon = new ImageIcon("database.png");
			Image image = icon.getImage();
			Image newimage = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
			icon.setImage(newimage);
			schermata.addTab("DB", icon, panelDB);
			icon = new ImageIcon("file.png");
			image = icon.getImage();
			newimage = image.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
			icon.setImage(newimage);
			schermata.addTab("file", icon, panelFile);
			setVisible(true);
			// panelDB.getRootPane().setDefaultButton(panelDB.executeButton);
			// panelFile.getRootPane().setDefaultButton(panelFile.executeButton);
		}

		/**
		 * Inner class di TabbelPane, indica un ascoltatore utilizzato per intercettare
		 * eventi riguardanti il database.
		 * 
		 * @author Mirko.
		 *
		 */
		private class EventFromDb implements ActionListener {
			/**
			 * Nome della tabella del database,
			 */
			private String nometab;
			/**
			 * Numero di cluster da generare.
			 */
			private int k;

			@Override
			/**
			 * Override del metodo actionPerformed della superclasse, permette di iniviare
			 * al server il nome della tabella e il numero di cluster voluti.
			 */
			public void actionPerformed(ActionEvent e) {
				try {
					out.writeObject(0);
					nometab = panelDB.tableText.getText();
					out.writeObject(nometab);
					String ris = (String) in.readObject();
					if (ris.equals("OK")) {
						out.writeObject(1);
						k = Integer.parseInt(panelDB.kText.getText());
						out.writeObject(k);
						ris = (String) in.readObject();
						if (ris.equals("OK")) {
							panelDB.clusterOutput.setText((String) in.readObject());
						} else {
							panelDB.clusterOutput.setText(ris);
						}
					} else {
						panelDB.clusterOutput.setText(ris);
					}
				} catch (IOException | ClassNotFoundException e1) {
					e1.printStackTrace();
					panelDB.clusterOutput.setText("Si e' verificato un errore.");
				}
			}
		}

		/**
		 * Inner class di TabbelPane indica la struttura interna al frame per quando
		 * riguarda l'utilizzo dei file.
		 * 
		 * @author Mirko.
		 *
		 */
		private class EventFromFile implements ActionListener {
			/**
			 * Indica il nome del file.
			 */
			private String nomefile;

			@Override
			/**
			 * Override del metodo actionPerformed della superclasse, permette di iniviare
			 * al server il nome della tabella e il numero di cluster voluti.
			 */
			public void actionPerformed(ActionEvent e) {
				try {
					out.writeObject(3);
					nomefile = panelFile.tableText.getText();
					out.writeObject(nomefile);
					String ok = (String) in.readObject();
					if (ok.equals("OK")) {
						panelFile.clusterOutput.setText((String) in.readObject());
					} else {
						panelFile.clusterOutput.setText("Nome file errato,controlla di non aver inserito l'estensione");
					}
				} catch (IOException | ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}

		}

		/**
		 * Inner Class di TabbelPane, indica la struttura di una singola tab di
		 * TabbelPane.
		 * 
		 * @author Mirko.
		 *
		 */
		private class JPanelCluster extends JPanel {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			/**
			 * Oggetto di tipo JLabel.
			 */
			private final JLabel labelk = new JLabel("K");
			/**
			 * Oggetto di tipo JLabel.
			 */
			private final JLabel labelTable = new JLabel("Table");
			/**
			 * Oggetto di tipo JTextField.
			 */
			private JTextField tableText = new JTextField(20);
			/**
			 * Oggetto di tipo JTextField, permette di inserire il nome della tabella o il
			 * nome del file(che viene indicato con lo stesso nome della tabella).
			 */
			private JTextField kText = new JTextField(10);
			/**
			 * Oggetto di tipo JTextField, verranno visualizzati gli output del server.
			 */
			private JTextArea clusterOutput = new JTextArea();
			/**
			 * Oggetto di tipo JButton, permette di creare un evento che verrà poi
			 * intercettato da i rispettivi listener.
			 */
			private JButton executeButton;

			/**
			 * Costruttore di classe, permette di definire il layout.
			 * 
			 * @param buttonName
			 *            Indica il nome del JButton da inserire e da visualizzare.
			 * @param a
			 *            Riferimento ad un oggetti di tipo ActionListener che permette di
			 *            associare il rispettivo listener al bottone con nome passato come
			 *            paramtero.
			 */
			private JPanelCluster(String buttonName, java.awt.event.ActionListener a) {
				this.setLayout(new BorderLayout());
				clusterOutput.setEditable(false);
				JPanel upPanel = new JPanel();
				upPanel.setLayout(new FlowLayout());
				JPanel centralPanel = new JPanel();
				centralPanel.setLayout(new FlowLayout());
				JPanel downPanel = new JPanel();
				downPanel.setLayout(new FlowLayout());
				upPanel.add(labelTable);
				upPanel.add(tableText);
				if (buttonName.equals("mine")) {
					upPanel.add(labelk);
					upPanel.add(kText);
				}
				centralPanel.add(clusterOutput);
				executeButton = new JButton(buttonName);
				downPanel.add(executeButton);
				executeButton.addActionListener(a);
				this.add(upPanel, BorderLayout.NORTH);
				this.add(centralPanel, BorderLayout.CENTER);
				this.add(downPanel, BorderLayout.SOUTH);
				JScrollPane sp = new JScrollPane(clusterOutput);
				this.add(sp);
				setVisible(true);
			}
		}

	}

	/**
	 * Inner class di JPannelCluster, indica la classe dialog utile per inserire
	 * port e ip.
	 * 
	 * @author Mirko.
	 *
	 */
	private class Dialog1 extends JDialog {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;
		private JPanel jPanel1 = new JPanel();
		private JPanel jPanel2 = new JPanel();
		private JButton jButton1 = new JButton();
		private JTextField jtext = new JTextField(10);
		private JTextField jtext2 = new JTextField(10);
		private final JLabel label = new JLabel("Insierisci ip");
		private final JLabel label2 = new JLabel("Insierisci porta");

		private Dialog1(Frame parent, String title, boolean modal) {
			super(parent, title, modal);
			try {
				jbInit();
				pack();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			getRootPane().setDefaultButton(jButton1);
		}

		private class ServerListener implements ActionListener {

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
