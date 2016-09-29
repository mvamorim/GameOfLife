import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameOfLife extends JFrame {
	
	JPanel panel = new JPanel(new GridBagLayout());
	JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
	JPanel incrementPanel = new JPanel(new GridLayout(1, 4));
	GridBagConstraints c = new GridBagConstraints();
	GridPanel gp = new GridPanel(400, 400, 4);
	JScrollPane sPane = new JScrollPane(gp);
	
	public GameOfLife() {
	
		sPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        sPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//sPane.setBounds(50, 30, 300, 50);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(sPane, c);
		
		c.gridx = 0;
		c.gridy = 1;
		createButtons();
		panel.add(buttonPanel, c);
		
		c.gridx = 0;
		c.gridy = 2;
		panel.add(incrementPanel, c);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(panel);

		this.pack();
		this.setVisible(true);

		gp.loop();
	}
	
	private final void createButtons() { 
		JButton start = new JButton("Iniciar");
		JButton stop = new JButton("Resetar");
		JButton increment = new JButton(">");
		JButton decrement = new JButton("<");
		JLabel gridSizeLabel = new JLabel(gp.getGridSize() + "", SwingConstants.CENTER);
		JLabel gapLabel1 = new JLabel(" ");
		JLabel gapLabel2 = new JLabel(" ");
		JButton showGridLines = new JButton("Grade");
		
		start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				if(gp.isRunning()) {
					start.setText("Iniciar");
				}else {
					start.setText("Pausar");
				}
				gp.start();
            }
        });
		
		stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				gp.reset();
				if(!gp.isRunning()) {
					start.setText("Iniciar");
				}else {
					start.setText("Pausar");
				}
               
            }
        });
		
		increment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				gp.incrementGridSize();
				gridSizeLabel.setText(gp.getGridSize() + "");
				//gp.reset();
				gp.repaint();
				if(!gp.isRunning()) {
					start.setText("Iniciar");
				}else {
					start.setText("Pausar");
				}
            }
        });
		
		decrement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				gp.decrementGridSize();
				gridSizeLabel.setText(gp.getGridSize() + "");
				//gp.reset();
				gp.repaint();
				if(!gp.isRunning()) {
					start.setText("Iniciar");
				}else {
					start.setText("Pausar");
				}
            }
        });
		
		showGridLines.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
				gp.toggleGrid();
            }
        });
		
		incrementPanel.add(decrement);
		incrementPanel.add(increment);
		incrementPanel.add(gridSizeLabel);
		incrementPanel.add(showGridLines);
		buttonPanel.add(start);
		buttonPanel.add(stop);
	}
	
	public static void main(String args[]) {
		new GameOfLife();
	}
}