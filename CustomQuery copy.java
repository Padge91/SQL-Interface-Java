import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.*;


@SuppressWarnings("serial")
public class CustomQuery extends JFrame implements ActionListener{
	
	private String SQLstatement;
	
	private JLabel instructionLabel;
	private JTextArea textArea;
	private JButton goButton;
	private JButton cancelButton;
	
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panel3;
	private JPanel mainPanel;
	
	private BoxLayout VLayout;
	private BoxLayout XLayout1;
	private BoxLayout XLayout2;
	private BoxLayout XLayout3;
	
	private MainFrame main;
	
	public CustomQuery (MainFrame m) {
		main = m;
		
		instructionLabel = new JLabel("Enter your query below.");
		textArea = new JTextArea();
		goButton = new JButton("Go");
		cancelButton = new JButton("Cancel");
		
		goButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		panel1 = new JPanel();
		XLayout1 = new BoxLayout(panel1, BoxLayout.X_AXIS);
		panel1.setLayout(XLayout1);
		panel1.add(instructionLabel);
		
		panel1.setPreferredSize(new Dimension(450, 30));
		panel1.setMinimumSize(panel1.getPreferredSize());
		
		panel2 = new JPanel();
		XLayout2 = new BoxLayout(panel2, BoxLayout.X_AXIS);
		panel2.setLayout(XLayout2);
		panel2.add(textArea);
		
		panel2.setPreferredSize(new Dimension(450, 100));
		panel2.setMinimumSize(panel2.getPreferredSize());
		
		panel3 = new JPanel();
		XLayout3 = new BoxLayout(panel3, BoxLayout.X_AXIS);
		panel3.setLayout(XLayout3);
		panel3.add(goButton);
		panel3.add(cancelButton);
		
		panel3.setPreferredSize(new Dimension(450, 60));
		panel3.setMinimumSize(panel3.getPreferredSize());
		
		mainPanel = new JPanel();
		VLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
		mainPanel.setLayout(VLayout);
		mainPanel.add(panel1);
		mainPanel.add(panel2);
		mainPanel.add(panel3);
		
		mainPanel.setPreferredSize(new Dimension(450, 190));
		mainPanel.setMinimumSize(mainPanel.getPreferredSize());
		
		add(mainPanel);
		
		setPreferredSize(new Dimension(450, 190));
		setMinimumSize(this.getPreferredSize());
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == goButton){
			SQLstatement = textArea.getText();
			
			if (SQLstatement.length() == 0) {
				JOptionPane.showMessageDialog(this, "Text field must not be empty.");
			}
			
			main.runSQL(SQLstatement);
			dispose();
			
		}
		else if (e.getSource() == cancelButton) {
			dispose();
		}
	}
	
}
