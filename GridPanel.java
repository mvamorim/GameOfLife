import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class GridPanel extends JPanel implements MouseListener, MouseMotionListener {
	
	private int columns, rows, gridSize;
	private HashMap<String, Cell> cells = new HashMap<String, Cell>();
	private HashMap<String, Cell> toRemove = new HashMap<String, Cell>();
	private HashMap<String, Cell> toAdd = new HashMap<String, Cell>();
	private boolean running = false, showGridLines = true;
	private int height, width;  
	
	
	public GridPanel(int h, int w, int gridSize) {
		setPreferredSize(new Dimension(h, w));
		addMouseListener(this);
		addMouseMotionListener(this);
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		this.height = h;
		this.width = w;
		this.gridSize = gridSize;
		rows = h / gridSize;
		columns = w / gridSize;
		
	}
	
	public void start() {
		if(!running) {
			running = true;
		}else {
			running = false;
		}
	}
	
	public void reset() {
		running = false;
		cells.clear();
		toRemove.clear();
		toAdd.clear();
		repaint();
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public int getGridSize() {
		return gridSize;
	}
	
	public void incrementGridSize() {
		if(gridSize < height) {
			gridSize++;
			while(height % gridSize != 0) {
				gridSize++;
			}
			rows = height / gridSize;
			columns = width / gridSize;
		}
	}
	
	public void decrementGridSize() {
		if(gridSize > 1) {
			gridSize--;
			while(height % gridSize != 0) {
				gridSize--;
			}
			rows = height / gridSize;
			columns = width / gridSize;
		}
	}
	
	public void addCell(int row, int column) {
		toAdd.put(getKey(row, column), new Cell(row, column));
	}
	
	public void removeCell(int row, int column) {
		cells.remove(getKey(row, column));
	}
	
	private String getKey(int row, int column) {
		return row + "_" + column;
	}
	
	private void drawGrid(Graphics g) {
		g.setColor(new Color(139, 137, 137, 75));
		for(int i = 0; i <= height; i += gridSize) {
			g.drawLine(0, i, width, i);
		}
		
		for(int i = 0; i <= height; i += gridSize) {
			g.drawLine(i, 0, i, height);
		}
		
	}
	
	public void toggleGrid() {
		if(showGridLines) {
			showGridLines = false;
		}else {
			showGridLines = true;
		}
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(showGridLines) {
			drawGrid(g);
		}
		
		if(!running){
			cells.putAll(toAdd);
		}
		
		g.setColor(Color.BLACK);
		for(Cell c : cells.values()) {
			c.draw(g);
		}
		
		
		/*
		for(Map.Entry<String, Cell> c : cells.entrySet()) {
			c.getValue().draw(g);
		}
		*/
	}
	
	/*
	 * ------- EVENTOS DE MOUSE -------
	 */
	public void mouseClicked(MouseEvent e) {
		click(e);
	}
	
	public void mousePressed(MouseEvent e) {
		click(e);
	}
	
	public void mouseDragged(MouseEvent e) {
		click(e);
	}
	
	private void click(MouseEvent e) {
		if(SwingUtilities.isLeftMouseButton(e)) {
			addCell(e.getPoint().y / gridSize, e.getPoint().x / gridSize);
			if(!running)
				repaint();
		}else if(SwingUtilities.isRightMouseButton(e)) {
			removeCell(e.getPoint().y / gridSize, e.getPoint().x / gridSize);
			if(!running)
				repaint();
		}
	}
	
	public void loop() {
		while(true) {
			while(running) {
				/*
				for(int i = 0; i < rows; i++) {
					for(int j = 0; j < columns; j++) {
						setSituation(i, j, getNeighbors(i, j));
					}
				}
				*/
				/*
				Iterator it = cells.entrySet().iterator();
				while(it.hasNext()){
						Map.Entry entry = (Map.Entry) it.next();
						Cell c = (Cell) entry.getValue();
						verifyAllActive(c);
						it.remove();
				}
				*/
				
				for(Cell c: cells.values()) {
					verifyAllActive(c);
				}
				
				
				for(Cell c : toRemove.values()) {
					cells.remove(getKey(c.getRow(), c.getColumn()));
				}
				
				cells.putAll(toAdd);
				/*
				for(Cell c : toAdd) {
					cells.put(getKey(c.getRow(), c.getColumn()), c);
				}
				*/
				repaint();
				
				toRemove.clear();
				toAdd.clear();
				
				System.out.println(cells.values().size());
				try { Thread.sleep(1000/30); } catch(Exception e) {}
			}
			
			try { Thread.sleep(1000/30); } catch(Exception e) {}
		}
	}
	
	
	private void setSituation(int row, int column, int n) {
		Cell c = cells.get(getKey(row, column));
		if(n >= 4 && cells.containsKey(getKey(row, column))) toRemove.put(getKey(c.getRow(), c.getColumn()), c);
		else if(n <= 1 && cells.containsKey(getKey(row, column))) toRemove.put(getKey(c.getRow(), c.getColumn()), c);
		else if(n == 3) toAdd.put(getKey(row, column), new Cell(row, column));
		
	}
	
	// Melhor algoritimo de verificação de célula
	// Ao invez de verificar todas as possiveis celulas no grid, verifica-se somente as celulas ativas e cada celula em volta dela
	// ou seja: Dado um grid de 500 * 500, contendo 250 mil celulas, a cada tick todas estas celulas seriam verificadas.
	// neste novo algoritimo, somente as celulas ativas e suas vizinhas sao verificadas.
	// dado 200 celulas ativas, são verificadas, 200 * 8 * 8, logo 12800 celulas. Uma redução drástica.
	// porém, em um determinado momento, isso pode inverter.
	private void verifyAllActive(Cell cl) {
		int r = cl.getRow();
		int c = cl.getColumn();
		
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				setSituation(r + i, c + j, getNeighbors(r + i, c + j));
			}
		}
	}
	
	private int getNeighbors(Cell c) {
		return getNeighbors(c.getRow(), c.getColumn());
	}
	
	private int getNeighbors(int r, int c) {
		int counter = 0;
		
		for(int i = -1; i < 2; i++) {
			for(int j = -1; j < 2; j++) {
				if(i == 0 && j == 0)
					continue;
				if(cells.containsKey(getKey(r + i, c + j)))
					counter++;
			}
		}

		return counter;
	}
	
	private class Cell {
		private int row, column;
		
		public Cell(int row, int column) {
			this.row = row;
			this.column = column;
		}
		
		
		public int getRow() {
			return row;
		}
		
		public int getColumn() {
			return column;
		}
		
		public void draw(Graphics g) { 
			g.fillRect(column * gridSize, row * gridSize, gridSize, gridSize);
		}
	}
	
	// Metodos nao utilizados
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
}