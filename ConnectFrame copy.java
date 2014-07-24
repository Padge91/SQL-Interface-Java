import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;

import java.sql.*;

@SuppressWarnings("serial")
public class ConnectFrame extends JFrame implements ActionListener{
	
	private JButton goButton;
	private JButton cancelButton;
	
	private JLabel passwordLabel;
	private JLabel usernameLabel;
	private JLabel dataSourceLabel;
	
	private JTextField passwordField;
	private JTextField usernameField;
	private JTextField dataSourceField;
	
	private String username, password, dataSource;
	
	private BoxLayout layout;
	private BoxLayout Xlayout1;
	private BoxLayout Xlayout2;
	private BoxLayout Xlayout3;
	private BoxLayout Xlayout4;
	
	private JPanel mainPanel;
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JPanel panel4;
	
	private MainFrame m;
	
	public ConnectFrame(MainFrame main) {
		
		try {
			Class.forName("org.hsqldb.jdbcDriver");
		}
		catch (Exception e) {
			System.out.println("error in loading drver in conectFrame");
		}
		
		m = main;
		goButton = new JButton("Connect");
		cancelButton = new JButton("Cancel");
		
		goButton.addActionListener(this);
		cancelButton.addActionListener(this);
				
		usernameLabel = new JLabel("Username: ");
		passwordLabel = new JLabel("Password: ");
		dataSourceLabel = new JLabel("Data Source: ");
		
		usernameField = new JTextField();
		passwordField = new JTextField();
		dataSourceField = new JTextField();
		
		panel1 = new JPanel();
		Xlayout1 = new BoxLayout(panel1, BoxLayout.X_AXIS);
		panel1.setLayout(Xlayout1);
		panel1.add(dataSourceLabel);
		panel1.add(dataSourceField);
		
		panel1.setPreferredSize(new Dimension(450, 30));
		panel1.setMinimumSize(panel1.getPreferredSize());
		
		panel2 = new JPanel();
		Xlayout2 = new BoxLayout(panel2, BoxLayout.X_AXIS);
		panel2.setLayout(Xlayout2);
		panel2.add(usernameLabel);
		panel2.add(usernameField);
		
		panel2.setPreferredSize(new Dimension(450, 30));
		panel2.setMinimumSize(panel2.getPreferredSize());
		
		panel3 = new JPanel();
		Xlayout3 = new BoxLayout(panel3, BoxLayout.X_AXIS);
		panel3.setLayout(Xlayout3);
		panel3.add(passwordLabel);
		panel3.add(passwordField);
		
		panel3.setPreferredSize(new Dimension(450, 30));
		panel3.setMinimumSize(panel3.getPreferredSize());
		
		panel4 = new JPanel();
		Xlayout4 = new BoxLayout(panel4, BoxLayout.X_AXIS);
		panel4.setLayout(Xlayout4);
		panel4.add(goButton);
		panel4.add(cancelButton);
		
		panel4.setPreferredSize(new Dimension(450, 60));
		panel4.setMinimumSize(panel4.getPreferredSize());
		
		mainPanel = new JPanel();
		layout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.setLayout(layout);
		mainPanel.add(panel1);
		mainPanel.add(panel2);
		mainPanel.add(panel3);
		mainPanel.add(panel4);
		
		mainPanel.setPreferredSize(new Dimension(450, 150));
		mainPanel.setMinimumSize(mainPanel.getPreferredSize());
		
		add(mainPanel);
		
		setPreferredSize(new Dimension(450, 150));
		setMinimumSize(this.getPreferredSize());
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == goButton){
			if (dataSourceField.getText().length() > 0) {
				dataSource = dataSourceField.getText();
				username = usernameField.getText();
				password = passwordField.getText();
				
				try {
					makeConnection();
					dispose();
				} 
				catch (Exception ex) {
					JOptionPane.showMessageDialog(this, "Error in connecting to database.\nError: " + ex.getMessage());
				}
			}
			else {
				JOptionPane.showMessageDialog(this, "Database Address can not be empty.");

			}
		}
		else if (e.getSource() == cancelButton) {
			dispose();
		}
	}
	
	public void makeConnection() throws SQLException {
		m.sendConnection(DriverManager.getConnection(dataSource, username, password));
	}
}
