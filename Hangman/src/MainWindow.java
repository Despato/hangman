import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame {
	
	private int remainingGuesses;
	private String wrongGuesses;
	private String word;
	private String visible;

	public MainWindow(String toGuess) {
		remainingGuesses = 10;
		wrongGuesses = "";
		word = toGuess;

		visible = "";

		for(int i = 0; i < word.length(); ++i) {
			visible += "_ ";
		}

		JLayeredPane lPane = new JLayeredPane();
		JPanel corePanel = new JPanel();
		corePanel.setLayout(new BorderLayout());
		
	
		final JLabel status = new JLabel("You have "+remainingGuesses+" remaining", SwingConstants.CENTER);
		final JLabel wrong = new JLabel("Wrong guesses so far: "+wrongGuesses);
		final JLabel visibleLabel = new JLabel(visible, SwingConstants.CENTER);
		final JTextField input = new JTextField(); 
		
		JPanel southPanel = new JPanel(new GridLayout(4, 1));
		southPanel.add(status);
		southPanel.add(visibleLabel);
		southPanel.add(input, BorderLayout.CENTER);
		southPanel.setBackground(Color.ORANGE);
		southPanel.add(wrong);
		
		corePanel.add(southPanel, BorderLayout.SOUTH);
		
		
		final Background bg = new Background();
		final HangmanFigure hf = new HangmanFigure();
		
		corePanel.add(hf, BorderLayout.CENTER);
		corePanel.add(bg, BorderLayout.CENTER);
		
		lPane.add(corePanel, BorderLayout.CENTER);

		this.add(corePanel, BorderLayout.CENTER);
		
		input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String text = input.getText();
				
				if(text.length()  == 1 && text.matches("[a-z]")) {
					
					boolean guessFound = false;
					
					for(int i = 0; i < word.length(); ++i) {
						if(text.charAt(0) == word.charAt(i)) {
							guessFound = true;
							
							String newVisible = "";
							for(int j = 0; j < visible.length(); ++j) {
								if(j == (i*2)) {
									newVisible += word.charAt(i);
								}
								else {
									newVisible += visible.charAt(j);
								}
							}
							visible = newVisible;
							visibleLabel.setText(visible);
						}
					}
					
					if(!guessFound) {
						if(--remainingGuesses >= 0) {
							status.setText("You have "+remainingGuesses+" guesses remaining");
							wrongGuesses += text+" ";
							wrong.setText("Wrong guesses so far: "+wrongGuesses);
							//hf.set();
						}
						else {
							status.setText("You lost: the word was "+word);
							input.setEnabled(false);
						}
					}
					else {
						String actualVisible = "";
						for(int i = 0; i < visible.length(); i+=2) {
							actualVisible += visible.charAt(i);
						}
						
						if(actualVisible.equals(word)) {
							status.setText("Congratulations, you have won!");
							input.setEnabled(false);
						}
					}
					
				}
				else {
					System.out.println("Invalid input!");
				}
				
				input.setText("");
			}
			
		});
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public static String chooseWord(String file) throws IOException{
		   InputStream is = MainWindow.class.getResourceAsStream(file);
		   
		   InputStreamReader fr = new InputStreamReader(is);
	       BufferedReader readFile = new BufferedReader(fr);
	      
	       String line = null;
	       Random rand = new Random();
	       final int MAXWORD = 200;
	       int randNum =  rand.nextInt((MAXWORD) + 1);
	       String word = "";
	       
	       int i = 0;
	       while ((line = readFile.readLine()) != null && i <= randNum){
	    	   word = line;
	    	   i++;
	       }
	       return word;
	}   
	
	public static void main(String[] args) throws IOException {
				
		new MainWindow(chooseWord("PirateWords.txt"));
	}
}