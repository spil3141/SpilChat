package com.spil3141.spilchat;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Login extends JFrame {
	//Private
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField m_Nickname;
	private JTextField m_IPAddress;
	private JTextField m_Port;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public Login() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception  e1) {
			e1.printStackTrace();
		} 
		//Window
		setTitle("Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(364, 456);
		setLocationRelativeTo(null);
		//ContentPlane
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		m_Nickname = new JTextField();
		m_Nickname.setBounds(75, 106, 211, 39);
		contentPane.add(m_Nickname);
		m_Nickname.setColumns(10);
		
		JLabel lblName = new JLabel("Nickname");
		lblName.setForeground(Color.WHITE);
		lblName.setBounds(147, 87, 68, 16);
		contentPane.add(lblName);
		
		m_IPAddress = new JTextField();
		m_IPAddress.setColumns(10);
		m_IPAddress.setBounds(75, 175, 211, 39);
		contentPane.add(m_IPAddress);
		
		JLabel lblIpAddress = new JLabel("IP Address");
		lblIpAddress.setForeground(Color.WHITE);
		lblIpAddress.setBounds(144, 157, 74, 16);
		contentPane.add(lblIpAddress);
		
		m_Port = new JTextField();
		m_Port.setColumns(10);
		m_Port.setBounds(75, 260, 211, 39);
		contentPane.add(m_Port);
		
		JLabel lblPortNumer = new JLabel("Port");
		lblPortNumer.setForeground(Color.WHITE);
		lblPortNumer.setBounds(163, 242, 35, 16);
		contentPane.add(lblPortNumer);
		
		JButton m_LoginBtn = new JButton("Login");
		m_LoginBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String nickname = m_Nickname.getText();
				String address = m_IPAddress.getText();
				int port = Integer.parseInt(m_Port.getText());
				login(nickname,address,port);
			}
		});
		m_LoginBtn.setBounds(122, 355, 117, 29);
		contentPane.add(m_LoginBtn);
		
		JLabel lbleg = new JLabel("(eg 192.169.0.2)");
		lbleg.setForeground(Color.WHITE);
		lbleg.setBounds(122, 214, 117, 16);
		contentPane.add(lbleg);
		
		JLabel lbleg_1 = new JLabel("(eg 8080)");
		lbleg_1.setForeground(Color.WHITE);
		lbleg_1.setBounds(147, 297, 68, 16);
		contentPane.add(lbleg_1);
		

		m_Nickname.requestFocusInWindow();
	}
	

	private void login(String nickname, String address, int port) {
		dispose();
		new Client(nickname,address,port);
	}
}
