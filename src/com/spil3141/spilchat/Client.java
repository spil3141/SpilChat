package com.spil3141.spilchat;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;
	//GUI
	private final int WIDTH = 800;
	private final int HEIGHT = 550;
	private JPanel contentPane;
	private JTextField m_Message;
	private JTextArea m_HistoryTextArea;
	private JButton m_SendBtn;
	private DefaultCaret caret; 
	
	//Networking
	private DatagramSocket socket;
	private InetAddress ip;
	private Thread sendT,receiveT;
	private String nickname,address;
	private int port;

	public Client(String nickname, String address, int port) {
		setTitle("Spil Chat Client");
		this.nickname = nickname;
		this.address = address;
		this.port = port;
		this.createWindow();
		this.console("Attempting a connection to : " + address + ":" + port);
		if(!openConnection(address)) {
			System.out.println("Couldn't Connect to Server!");
			console("Connection failed");
		}else{
			System.out.println("Connected to Server!");
			console("Connection Successful");
		}
		String msg = nickname + " Connected from " + address + ":" + port;
		send(("/c/"+ this.nickname).getBytes());
		receive();
	}
	
	private boolean openConnection(String address) {
		try {
			socket = new DatagramSocket();
			this.ip = InetAddress.getByName(address);
		}catch(Exception e) {
			return false;
		}
		return true;
	}

	private void receive() {
		receiveT = new Thread("Receiver Thread"){
			@Override
			public void run() {
				byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data,data.length);
				try {
					socket.receive(packet);
					console(new String(packet.getData()));
				} catch (IOException e) {
					e.printStackTrace();
				}
				String mgs = new String(packet.getData());
			}
		};

	}
	
	private void send(final byte[] DATA) {
		sendT = new Thread("Sending Thread") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(DATA, DATA.length, ip, port);
				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		sendT.start();
	}
	private void createWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{5,760,30, 5};
		gbl_contentPane.rowHeights = new int[]{30, 480,40};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0};
		gbl_contentPane.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		m_HistoryTextArea = new JTextArea();
		m_HistoryTextArea.setEditable(false);
		caret = (DefaultCaret) m_HistoryTextArea.getCaret();
		JScrollPane scroller = new JScrollPane(m_HistoryTextArea);
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.insets = new Insets(0, 0, 5, 5);
		scrollConstraints.fill = GridBagConstraints.BOTH;
		scrollConstraints.gridx = 0;
		scrollConstraints.gridy = 0;
		scrollConstraints.gridwidth = 3;
		scrollConstraints.gridheight = 2;
		contentPane.add(scroller, scrollConstraints);
		
		m_Message = new JTextField();
		m_Message.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyChar() == KeyEvent.VK_ENTER) {
					if(m_Message.getText().compareTo("") != 0)
						updateHistory(m_Message.getText());
				}
			}
		});
		GridBagConstraints gbc_m_Message = new GridBagConstraints();
		gbc_m_Message.insets = new Insets(0, 0, 0, 5);
		gbc_m_Message.fill = GridBagConstraints.HORIZONTAL;
		gbc_m_Message.gridx = 0;
		gbc_m_Message.gridy = 2;
		gbc_m_Message.gridwidth = 2;
		contentPane.add(m_Message, gbc_m_Message);
		m_Message.setColumns(10);
		
		m_SendBtn = new JButton("Send");
		m_SendBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(m_Message.getText().compareTo("") != 0)
					updateHistory(m_Message.getText());
			}
		});
		GridBagConstraints gbc_m_SendBtn = new GridBagConstraints();
		gbc_m_SendBtn.insets = new Insets(0, 0, 0, 5);
		gbc_m_SendBtn.gridx = 2;
		gbc_m_SendBtn.gridy = 2;
		contentPane.add(m_SendBtn, gbc_m_SendBtn);
		setVisible(true);
		
		m_Message.requestFocusInWindow();
	}
	public void updateHistory(String message) {
		console(message);
		m_HistoryTextArea.setCaretPosition(m_HistoryTextArea.getDocument().getLength());
		send(("/m/" + message).getBytes());
		m_Message.setText("");

	}
	public void console(String message) {
		this.m_HistoryTextArea.append(nickname + " : " + message + "\n\r");
	}
}
