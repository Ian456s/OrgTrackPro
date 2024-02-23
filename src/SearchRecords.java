import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class SearchRecords extends JPanel {
    static JFrame frame;
    static BufferedWriter out, out2, out3;
    static Admin curUser;
    DefaultTableModel model;
    JTable table;
    TableRowSorter<TableModel> rowSorter;
    JScrollPane sp;

    public SearchRecords() {
        setLayout(null);

        //titlebar
        JPanel titlebar = new JPanel();
        titlebar.setBackground(Color.decode("#76BEE8"));
        titlebar.setLayout(new BorderLayout());
        titlebar.setBorder(new EmptyBorder(10, 10, 10, 80));
        titlebar.setBounds(0, 0, 1280, 60);
        //titlebar.add(Common.getImage("orgtrackpro-logo.png"), BorderLayout.EAST); //TODO make smaller logo
        add(titlebar);

        //sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBorder( new EmptyBorder(15, 15, 15, 15));
        sidebar.setBackground(Color.decode("#3E3F40"));
        sidebar.setBounds(0, 0, 300, 720);
        //button to add a new organization to the records
		JButton addOrg = new JButton("Add An Organization");
		addOrg.setBounds(750, 80, 180, 25);
		addOrg.addActionListener(e -> {
			try {
				new AddNewOrg();
			} catch (NullPointerException e1) {
				e1.printStackTrace();
			}
		});
        //sidebar.add(addOrg);
        //add(sidebar);
        add(addOrg);

        JLabel title1 = new JLabel("Search Records");
        title1.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 22));
        title1.setForeground(Color.gray);
        title1.setBounds(125, 10, 250, 50);
        add(title1);

        //data to be displayed in the JTable
        String[][] data = new String[Org.getOrgs().size()][6]; //2D array to hold information on all active students
        int organizationNum = 0;
        for(Org o : Org.getOrgs()){
            data[organizationNum][0] = String.valueOf(organizationNum);
            data[organizationNum][1] = o.getName();
            data[organizationNum][2] = o.getEmail();
            data[organizationNum][3] = o.getPhoneNum();
            data[organizationNum][4] = o.getAddress();
            data[organizationNum][5] = o.getDesc();
            organizationNum++;
        }
        String[] columnNames = {"Order #", "Organization Name", "Email", "Phone #", "Address", "Description"}; //array to hold column names of the data table
       
        //initializing the data table
        model = new DefaultTableModel(data, columnNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        rowSorter = new TableRowSorter<>(table.getModel());
        table.setRowHeight(25);
        table.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        sp = new JScrollPane(table);
        sp.setBounds(120, 110, 900, 500);
        add(sp);

        //creating the search filter feature
        JLabel lbl = new JLabel("Enter Search:");
        lbl.setBackground(Color.white);
        lbl.setForeground(Color.gray);
        lbl.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        lbl.setBounds(155, 60, 150, 50);
        JTextField tf = new JTextField();
        tf.setEditable(true);
        tf.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 18));
        tf.setBounds(280, 70, 400, 35);
        add(lbl);
        add(tf);

        //dynamically searching the table
        table.setRowSorter(rowSorter);
        tf.getDocument().addDocumentListener(new DocumentListener(){

            //if user enters a search entry
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = tf.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            //if user deletes a character sequence from their search entry
            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = tf.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            //if user changes a character sequence from their search entry
            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

    }

    protected static void saveUser() throws IOException {
        out = new BufferedWriter(new FileWriter("data\\admins.txt"));
        out2 = new BufferedWriter(new FileWriter("data\\userSchools.txt"));
        
        for(Admin a : Admin.getAdmins()) {
            out.write(a.getName() + " ");
        }
        out.newLine();
        for(Admin a : Admin.getAdmins()) {
            out.write(a.getEmail() + " ");
        }
        out.newLine();
        for(Admin a : Admin.getAdmins()) {
            out.write(a.getPassword() + " ");
        }
        out.newLine();
        out.close();

        for(Admin a : Admin.getAdmins()) {
            out2.write(a.getSchool());
            out2.newLine();
        }
        out2.close();
    }

    protected static void saveOrg() throws IOException {
        out3 = new BufferedWriter(new FileWriter("data\\organizations.txt"));
        
        for(Org o : Org.getOrgs()) {
            out3.write(o.getName() + "|");
        }
        out3.newLine();
        for(Org o : Org.getOrgs()) {
            out3.write(o.getEmail() + "|");
        }
        out3.newLine();
        for(Org a : Org.getOrgs()) {
            out3.write(a.getPhoneNum() + "|");
        }
        out3.newLine();
        for(Org a : Org.getOrgs()) {
            out3.write(a.getAddress() + "|");
        }
        out3.newLine();
        for(Org a : Org.getOrgs()) {
            out3.write(a.getDesc() + "|");
        }
        out3.newLine();
        out3.close();
    }
}