import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.*;

public class FormControl extends JFrame {
	private static final long serialVersionUID = 1L;
	public static Controller controller;
	public String addedDBname="";
	int updateRate = 10000;// in millis
	public final String emptyString = "";
	public FormControl(Controller controller) {
		super("Database Control");
		File f = new File("c:\\soft\\javaDB\\log.txt");
		if(f.exists()){
			f.delete();
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JMenuBar bar = new JMenuBar();
	    JMenu edit = new JMenu("Edit");
        JMenuItem add = new JMenuItem("Add");
        add.addActionListener(new ActionListener()
		{
  		  public void actionPerformed(ActionEvent e)
  		  {
  		    JFrame enterName = new JFrame();
  		    enterName.setTitle("Add a DataBase");
  		    enterName.setBounds(50, 50, 250, 350);
  		    enterName.setDefaultCloseOperation(1);
  		    JLabel label = new JLabel("DB path:");
  		    JLabel label1 = new JLabel("DB port:");
  		    JLabel label2 = new JLabel("DB name:");
  		    JLabel label3 = new JLabel("DB user:");
  		    JLabel label4 = new JLabel("User password:");
  		    JLabel info = new JLabel("(Path is 'localhost' or machine IP)");
  		    JLabel info1 = new JLabel("(User should have permissions for re-");
  		    JLabel info2 = new JLabel("mote access)");
  		    JLabel status = new JLabel("");
  		    JTextField path = new JTextField();
  		    JTextField port = new JTextField();
  		    JTextField name = new JTextField();
  		    JTextField user = new JTextField();
  		    JTextField password = new JTextField();
  		    path.setPreferredSize(new Dimension(100,20));
  		    port.setPreferredSize(new Dimension(40,20));
  		    name.setPreferredSize(new Dimension(100,20));
  		    user.setPreferredSize(new Dimension(100,20));
  		    password.setPreferredSize(new Dimension(100,20));
  		    JButton button = new JButton("Add");
  		    button.addActionListener(new ActionListener() {
  		    	public void actionPerformed(ActionEvent e) {
  		    		if(!name.getText().trim().isEmpty()&&!path.getText().trim().isEmpty()&&!user.getText().trim().isEmpty()&&!password.getText().trim().isEmpty()) {
  		    			try {
  		    				String result = controller.addDB(name.getText().trim(), path.getText().trim(),Integer.parseInt(port.getText().trim()), user.getText().trim(), password.getText().trim());
  		    				String str = name.getText().trim()+" "+path.getText().trim()+" "+port.getText().trim()+" "+user.getText().trim()+" "+password.getText().trim()+"\n";
  		    				BufferedWriter writer;
  		    				if(result.equals("Success")) {
  		    					try {
  		  		    			    writer = new BufferedWriter(new FileWriter("C:\\soft\\javaDb\\dblist.txt", true));
  		  		    			    writer.append(str);
  		  		    			    writer.close();
  		  		    			    
  		  		    				}catch(Exception ex) {
  		  		    					
  		  		    				}
  		    					enterName.setVisible(false);
  		    				}
  		    				else {
  		    					status.setText(result+"(check info)");
  		    				}
  		    				
  		    				
  		    			}catch(Throwable ex) {
  		    				System.out.println(ex.getMessage());
  		    				if(ex.getMessage().equals("DB does not exist")) {
  		    					enterName.setVisible(false);
  		    					return;
  		    				}
  		    			}
  		    			
  		    			
  		    			
  		    		}
  		    	}

  		    });
  		    Container container = enterName.getContentPane();
  		    container.setLayout(new GridLayout(0,1,2,2));
  		    container.add(label);
  		    container.add(info);
  		    container.add(path);
  		    container.add(label1);
  		    container.add(port);
  		    container.add(label2);
  		    container.add(name);
  		    container.add(label3);
  		    container.add(info1);
  		    container.add(info2);
  		    container.add(user);
  		    container.add(label4);
  		    container.add(password);
  		    container.add(button);
  		    container.add(status);
  		    
  		    enterName.setVisible(true);
  		    
  		  }
  		 
  		});
        edit.add(add);
        bar.add(edit);
        setJMenuBar(bar);
       
		
		this.controller = controller;
		ArrayList<Model> result = controller.getAll();
		this.setBounds(50, 50, 600, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().removeAll();
		
		for (Model model : result) {
			Container container = this.getContentPane();
			JPanel panel = new JPanel();
			panel.add(new JLabel(model.DBname));
			panel.add(new JLabel(model.DBsize + "/" + model.DBcapacity));
			panel.add(new JLabel(model.getPath()+":"+model.getPort()));
			container.setLayout(new GridLayout(0, 4));
			container.add(panel);
		}
		this.invalidate();
		this.validate();
		this.repaint();
		this.setVisible(true);
	}

	public void update() {
		
		ArrayList<Model> result = controller.getAll();
		this.getContentPane().removeAll();
		
		for (Model model : result) {
			Container container = this.getContentPane();
			container.setLayout(new GridLayout(0, 4));
			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout()); 
	        GridBagConstraints c = new GridBagConstraints(); 
	        c.fill = GridBagConstraints.HORIZONTAL; 
	        c.gridx = 0; 
	        c.gridy = 1; 
	        JLabel nm = new JLabel(model.DBname);
			panel.add(nm,c);
			String life = model.actualDBstatus?"Alive":"Disabled";
			if(!model.actualDBstatus&&!model.actualDBstatus) {
				c.gridx = 2; 
		        c.gridy = 1; 
				JLabel label = new JLabel();
				label.setForeground(Color.RED);
				
				if(model.deadDate.isEmpty()) {
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss yyyy/MM/dd");
					LocalDateTime now = LocalDateTime.now();
					model.deadDate = dtf.format(now);
					try {
		    			    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\soft\\javaDb\\log.txt", true));
		    			    writer.newLine();
		    			    writer.append("Time: "+dtf.format(now)+", DB: "+model.DBname+" on "+model.getPath()+":"+model.getPort()+" disabled");
		    			    writer.close();
		    			    
		    				}catch(Exception ex) {
		    					
		    				}
				}
				
				label.setText(model.deadDate);
				
				panel.add(label,c);
			}if(!model.deadDate.isEmpty()&&model.actualDBstatus){
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss yyyy/MM/dd");
				LocalDateTime now = LocalDateTime.now();
				model.deadDate = dtf.format(now);
				try {
	    			    BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\soft\\javaDb\\log.txt", true));
	    			    writer.newLine();
	    			    writer.append("Time: "+dtf.format(now)+", DB: "+model.DBname+" on "+model.getPath()+":"+model.getPort()+" went back alive");
	    			    writer.close();
	    			    
	    				}catch(Exception ex) {
	    					
	    				}
				model.deadDate = this.emptyString;
			}
			
			c.gridx = 1; 
	        c.gridy = 1; 
			JLabel lf = new JLabel(life);
			lf.setForeground(model.actualDBstatus?Color.BLUE:Color.RED);
			panel.add(lf,c);
			c.gridx = 0; 
	        c.gridy = 2; 
			panel.add(new JLabel(model.DBsize + "/" + model.DBcapacity+ "  "),c);
			c.gridx = 1; 
	        c.gridy = 2; 
			panel.add(new JLabel(model.getPath()+":"+model.getPort()),c);
			container.add(panel);
		}
		this.invalidate();
		this.validate();
		this.repaint();
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				update();
			}
		}, updateRate);
	}

}
