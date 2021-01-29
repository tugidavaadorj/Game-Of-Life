import javax.swing.JFrame;
import java.util.Scanner;

public class Main {

	public Main() {
		JFrame frame = new JFrame();
		GameOfLife game = new GameOfLife();
		//game.importState("aliveCellPulsar3.txt");
		game.randomGame(.45);
		
		frame.add(game);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Game Of Life");
		
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		
		Scanner Tester = new Scanner(System.in);
		System.out.println("Press S to continue");
		if (Tester.nextLine().equals("S")){
			game.GameRules();
		}
		Scanner Tester2 = new Scanner(System.in);
		System.out.println("Press S to continue");
		if (Tester2.nextLine().equals("S")){
			game.GameRules();
		}
	}
	
	public static void main(String[] args) {
		
		new Main();
	}

}
