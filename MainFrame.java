import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;


//test connection string is jdbc:hsqldb:file:/Users/nicholaspadgett/Desktop/untitled folder/New Database1


@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener, MouseListener{
	
	public static void main (String[] args) {
		new MainFrame();
	}
	

	private Connection conn = null;
	private ArrayList<String> tableNames = new ArrayList<String>();
	private String[][] columnNames;
	
	//components for panel1
	private BoxLayout vBox1;
	private JPanel panel1;
	private Dimension d1;
	
	private JButton connectButton;
	private JLabel fromLabel;
	private JLabel selectLabel;
	private JList fromList;
	private JScrollPane fromListScroll;
	private JList selectList;
	private JScrollPane selectListScroll;
	
	//components for panel2
	private BoxLayout vBox2;
	private JPanel panel2;
	private Dimension d2;
	
	private JLabel whereLabel;
	private JComboBox where1;
	private JComboBox where2;
	private JTextField where3;
	
	//component for panel3
	private BoxLayout vBox3;
	private JPanel panel3;
	private Dimension d3;
	
	private JScrollPane scrollPane;
	
	//components for panel4
	private BoxLayout vBox4;
	private JPanel panel4;
	private Dimension d4;
	
	private JButton goButton;
	private JButton customQueryButton;
	private JButton printButton;
	private JButton exportButton;
	
	//parent panel
	private BoxLayout verticalLayout;
	private JPanel parentPanel;
	private Dimension parentDimension;
	
	//list for comparing
	Object[] whereStrings = {"=", "<=", ">="};
		
	//default constructor
	public MainFrame() {
		
		//load drivers; default driver for testing is for hsqldb
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//instantiate panel1 and components for it
		panel1 = new JPanel();
		vBox1 = new BoxLayout(panel1, BoxLayout.X_AXIS);
		panel1.setLayout(vBox1);
		
		connectButton = new JButton("Connect");
		connectButton.addActionListener(this);
		
		fromLabel = new JLabel("From: ");
		fromList = new JList();
		fromList.addMouseListener(this);
		
		fromListScroll = new JScrollPane(fromList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		selectLabel = new JLabel("Select: ");
		selectList = new JList();
		selectListScroll = new JScrollPane(selectList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		//add components to panel1
		panel1.add(connectButton);
		panel1.add(fromLabel);
		panel1.add(fromListScroll);
		panel1.add(selectLabel);
		panel1.add(selectListScroll);
		
		//set dimensions for panel1
		d1 = new Dimension(600, 60);
		panel1.setPreferredSize(d1);
		panel1.setMinimumSize(panel1.getPreferredSize());
		
		//instantiate panel2 and components for it
		panel2 = new JPanel();
		vBox2 = new BoxLayout(panel2, BoxLayout.X_AXIS);
		panel2.setLayout(vBox2);
		
		whereLabel = new JLabel("Where: ");
		where1 = new JComboBox();
		where2 = new JComboBox(whereStrings);
		where3 = new JTextField();
		
		//add components to panel2
		panel2.add(whereLabel);
		panel2.add(where1);
		panel2.add(where2);
		panel2.add(where3);
		
		//set dimensions for panel2
		d2 = new Dimension(600, 60);
		panel2.setPreferredSize(d2);
		panel2.setMinimumSize(panel2.getPreferredSize());
		
		//instantiate panel3 and components for it
		panel3 = new JPanel();
		vBox3 = new BoxLayout(panel3, BoxLayout.X_AXIS);
		panel3.setLayout(vBox3);
		
		scrollPane = new JScrollPane();
		
		//add components to panel3
		panel3.add(scrollPane);
		
		//set dimensions for panel3
		d3 = new Dimension(600, 400);
		panel3.setPreferredSize(d3);
		panel3.setMinimumSize(panel3.getPreferredSize());
		
		//instantiate panel4 and components for it
		panel4 = new JPanel();
		vBox4 = new BoxLayout(panel4, BoxLayout.X_AXIS);
		panel4.setLayout(vBox4);
		
		goButton = new JButton("Go");
		goButton.addActionListener(this);
		customQueryButton = new JButton("Custom Query");
		customQueryButton.addActionListener(this);
		printButton = new JButton("Print");
		printButton.addActionListener(this);
		exportButton = new JButton("Export");
		exportButton.addActionListener(this);
		
		//add components to panel4
		panel4.add(goButton);
		panel4.add(customQueryButton);
		panel4.add(printButton);
		panel4.add(exportButton);
		
		//set dimensions for panel4
		d4 = new Dimension(600, 60);
		panel4.setPreferredSize(d4);
		panel4.setMinimumSize(panel4.getPreferredSize());
		
		//instantiate parent panel and components for it
		parentPanel = new JPanel();
		verticalLayout = new BoxLayout(parentPanel, BoxLayout.Y_AXIS);
		parentPanel.setLayout(verticalLayout);
		parentPanel.add(panel1);
		parentPanel.add(panel2);
		parentPanel.add(panel3);
		parentPanel.add(panel4);
		
		parentDimension = new Dimension(650, 580);
		parentPanel.setMinimumSize(parentDimension);
		parentPanel.setPreferredSize(parentPanel.getPreferredSize());
		
		//set size for parent panel
		setMinimumSize(parentDimension);
		setPreferredSize(parentDimension);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		add(parentPanel);
		setVisible(true);
		
	}
	
	//-------------------------------------------------------------------
	
	//method to send a connection to this class from ConnectFrame class
	public void sendConnection(Connection connection) {
		conn = connection;
		ResultSet rs;
		
		try {
			DatabaseMetaData md = conn.getMetaData();
			rs = md.getTables(null, null, "%", null);
		
			while (rs.next()) {
				if (!rs.getString(3).startsWith("SYSTEM")) {
					tableNames.add(rs.getString(3));
				}
			}
		
		}
		catch (Exception e) {
			System.out.println("error in sendConnection()");
		}
		
		//populate list of table names
		fromList.setModel(new DefaultComboBoxModel(tableNames.toArray()));
		this.repaint();
		panel1.repaint();
	}
	
	//-------------------------------------------------------------------
	
	//method to run the SQL statement
	public void runSQL(String s) {
		ResultSet set = null;
		String str;
		str = s;
		String fromStatement = " FROM ";
		String selectStatement = "SELECT ";
		String whereStatement = " WHERE ";
		boolean first = false;
		boolean first2 = false;
		
		if (str.length() == 0) {
			//get the froms
			if (fromList.getSelectedIndex() == -1){
				JOptionPane.showMessageDialog(this, "A Table needs to be selected from the \"FROM\" menu.");
				return;
			}
			else {
				for(int i : fromList.getSelectedIndices()) {
					if (!first) {
						first = true;
						fromStatement += fromList.getModel().getElementAt(i).toString();
					}
					else {
						fromStatement += ", " + fromList.getModel().getElementAt(i).toString();
					}
				}
			}
			
			
			
			//get the selects
			if (selectList.getSelectedIndex() == -1) {
				JOptionPane.showMessageDialog(this, "A Column must be selected from the \"SELECT\" menu.");
			}
			else {
				for(int i : selectList.getSelectedIndices()) {
					if (i == 0) {
						selectStatement += " *";
						break;
					}
					else if (!first2){
						first2 = true;
						selectStatement += selectList.getModel().getElementAt(i).toString();
					}
					else {
						selectStatement += ", " + selectList.getModel().getElementAt(i).toString();
					}
				}
			}
			
			
			
			//get the wheres
			if (where1.getSelectedIndex() != 0){
				whereStatement += where1.getSelectedItem().toString();
				
				if (where2.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(this, "\"WHERE\" statement is not complete.");
					return;
				}	
				whereStatement += where2.getSelectedItem().toString();
				
				if (where3.getText().length() == 0) {
					JOptionPane.showMessageDialog(this, "\"WHERE\" statement is not complete.");
					return;
				}
				whereStatement += where3.getText();
				str = selectStatement + fromStatement + whereStatement + ";";
			}
			else {
				str = selectStatement + fromStatement + ";";
			}
		}
		
		//create the statement and execute it
		try {
			Statement state = conn.createStatement();		
			set = state.executeQuery(str);
		}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error when executing SQL statement: \n " + ex.getMessage().toString());
			return;
		}
		
		//send the resultSet to the displayResults method
		displayResults(set);
	}
	
	//-------------------------------------------------------------------
	
	//method to display the resultSet
	public void displayResults(ResultSet set) {
		//set record elements in table, then print table to scroll pane
		JTable table = null;
		
		try {
			table = new JTable(buildTableModel(set));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error in populating table.");
			return;
		}
	
		scrollPane = new JScrollPane(table);
		scrollPane.repaint();
		panel3.repaint();
		this.repaint();
		
	}
	
	//-------------------------------------------------------------------
	
	//method to convert ResultSet to table
	private static DefaultTableModel buildTableModel(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
	
	//-------------------------------------------------------------------
	
	//action performed listener method
	public void actionPerformed(ActionEvent e) {
		//if connect button is used
		if (e.getSource() == connectButton){
			new ConnectFrame(this);
		}
		//if go button is used
		else if (e.getSource() == goButton){
			runSQL("");
		} 
		//if custom query is entered
		else if (e.getSource() == customQueryButton) {
			new CustomQuery(this);
		} 
		else if (e.getSource() == printButton) {
			//print prompt
		}
		else if (e.getSource() == exportButton) {
			//export prompt
		}
	}
	
	//--------------------------------------------------------------------
	
	//mouse clicked listener method
	public void mouseClicked(MouseEvent me) {
		if (me.getSource() == fromList) {
			//get columns from selected tables
			int[] selectedInd = fromList.getSelectedIndices();
			
			if (selectedInd.length == 0) {
				where1.setModel(new DefaultComboBoxModel());
				selectList.setModel(new DefaultComboBoxModel());
				panel1.repaint();
				panel2.repaint();
			}
			else {
				String[] selectedTables = new String[selectedInd.length];
				ArrayList<String> columns = new ArrayList<String>();
			
				for (int i = 0; i < selectedInd.length; i++){
					selectedTables[i] = fromList.getModel().getElementAt(selectedInd[i]).toString();
				}
				
				columns.add("ALL");
			
				//get columns from one table at a time
				for (int i = 0; i < selectedInd.length; i++) {
					try {
						DatabaseMetaData dbmd = conn.getMetaData();
						ResultSet rs = dbmd.getColumns(null, null, selectedTables[i], null);
					
						while (rs.next()) {
							columns.add(selectedTables[i] + "." + rs.getString(4));
						}
					
					} catch (Exception e) {
						System.out.println("Error when getting metaData in mouseClicked event");
					}
				}
			
				where1.setModel(new DefaultComboBoxModel(columns.toArray()));
				selectList.setModel(new DefaultComboBoxModel(columns.toArray()));
				panel1.repaint();
				panel2.repaint();
			}
		}
	}

//-------------------------------------------------------------------------
	
	//these can be ignored, at least for now
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

