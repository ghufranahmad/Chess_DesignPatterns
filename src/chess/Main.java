package chess;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pieces.King;

/**
 * @author Ashish Kedia and Adarsh Mohata
 *
 */

/**
 * This is the Main Class of our project. All GUI Elements are declared,
 * initialized and used in this class itself. It is inherited from the JFrame
 * Class of Java's Swing Library.
 * 
 */

public class Main extends JFrame {
	private static final String CHESS_TITLE = "Chess";
	private static final String BLACK_PLAYER = "Black";
	private static final String WHITE_PLAYER = "White";
	private static final int TOTAL_TIME_FOR_TURN = 60;
	private static final long serialVersionUID = 1L;
	private static final int HEIGHT = 700;
	private static final int WIDTH = 1110;

	private Cell previousCell;
	private int currentTurn = 0;

	private BoardState chessBoardState;
	private ArrayList<Cell> possibleDestinations = new ArrayList<Cell>();

	private Player whitePlayer, blackPlayer, selectedPlayer;

	private JPanel boardPanel, controlPanel, temporaryPanel, timeDisplayPanle, playerViewPanel, timePanel;
	private JPanel whitePlayerDetailsPanel, whitePlayerComboPanel, whitePlayerPanel;
	private JPanel blackPlayerDetailsPnale, blackPlayerComboPanel, blackPlayerPanel;

	private JSplitPane splitPane;
	private JLabel timeLabel, moveLabel, turnLabel;

	private Time timer;

	private boolean isSelected = false, end = false;

	private Container content;
	private JComboBox<String> whitePlayerCombo, blackPlayerCombo;

	private String move = WHITE_PLAYER;

	private JScrollPane whitePlayerScrollPane, blackPlayerScrollPane;
	private JSlider timeSlider;
	private BufferedImage image;

	private Button startButton, whitePlayerSelectButton, blackPlayerSelectButton, newWhitePlayerButton,
			newBlackPlayerButton;
	public static int timeRemaining = TOTAL_TIME_FOR_TURN;

	public static void main(String[] args) {
		Main chessBoard = new Main();
		chessBoard.setVisible(true);
		chessBoard.setResizable(false);
	}

	private Main() {
		createContentLayout();
		chessBoardState = new BoardState(boardPanel, this);
	}

	private void createContentLayout() {

		createChessBoardPanel();

		content = getContentPane();
		setSize(WIDTH, HEIGHT);
		setTitle(CHESS_TITLE);
		content.setBackground(Color.black);

		createControlPanel();

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, temporaryPanel, controlPanel);

		content.add(splitPane);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void createControlPanel() {
		controlPanel = new JPanel();
		content.setLayout(new BorderLayout());
		controlPanel.setLayout(new GridLayout(3, 3));
		controlPanel.setBorder(BorderFactory.createTitledBorder(null, "Statistics", TitledBorder.TOP,
				TitledBorder.CENTER, new Font("Lucida Calligraphy", Font.PLAIN, 20), Color.ORANGE));

		createWhitePlayerPanel();
		createBlackPlayerPanel();

		controlPanel.add(whitePlayerPanel);
		controlPanel.add(blackPlayerPanel);

		initializeTimer();

		createTimePanel();
		controlPanel.add(timePanel);

		createTemporaryPanel();

		controlPanel.setMinimumSize(new Dimension(285, 700));
	}

	private void createChessBoardPanel() {
		boardPanel = new JPanel(new GridLayout(8, 8));
		boardPanel.setMinimumSize(new Dimension(800, 700));
		ImageIcon img = new ImageIcon(this.getClass().getResource("icon.png"));
		this.setIconImage(img.getImage());

		boardPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		boardPanel.setMinimumSize(new Dimension(800, 700));
	}

	private void createTemporaryPanel() {
		temporaryPanel = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				try {
					image = ImageIO.read(this.getClass().getResource("clash.jpg"));
				} catch (IOException ex) {
					System.out.println("not found");
				}

				g.drawImage(image, 0, 0, null);
			}
		};

		temporaryPanel.setMinimumSize(new Dimension(800, 700));
	}

	private void createTimePanel() {
		playerViewPanel = new JPanel(new FlowLayout());
		playerViewPanel.add(timeSlider);
		JLabel setTime = new JLabel("Set Timer(in mins):");

		startButton = new Button("Start");
		startButton.setBackground(Color.black);
		startButton.setForeground(Color.white);
		startButton.addActionListener(new StartButtonListener());
		startButton.setPreferredSize(new Dimension(120, 40));

		setTime.setFont(new Font("Arial", Font.BOLD, 16));
		timeLabel = new JLabel("Time Starts now", JLabel.CENTER);
		timeLabel.setFont(new Font("SERIF", Font.BOLD, 30));
		timeDisplayPanle = new JPanel(new FlowLayout());
		timePanel = new JPanel(new GridLayout(3, 3));
		timePanel.add(setTime);
		timePanel.add(playerViewPanel);
		timeDisplayPanle.add(startButton);
		timePanel.add(timeDisplayPanle);
		timer = new Time(timeLabel, this);
	}

	private void createBlackPlayerPanel() {
		blackPlayerPanel = new JPanel();
		blackPlayerDetailsPnale = new JPanel(new GridLayout(3, 3));
		blackPlayerComboPanel = new JPanel();

		blackPlayerPanel.setBorder(BorderFactory.createTitledBorder(null, "Black Player", TitledBorder.TOP,
				TitledBorder.CENTER, new Font("times new roman", Font.BOLD, 18), Color.BLUE));
		blackPlayerPanel.setLayout(new BorderLayout());

		blackPlayerCombo = new JComboBox<String>(getPlayerNames());

		blackPlayerScrollPane = new JScrollPane(blackPlayerCombo);

		blackPlayerSelectButton = new Button("Select");

		blackPlayerSelectButton.addActionListener(new PlayerSelectionListener(1));

		newBlackPlayerButton = new Button("New Player");

		newBlackPlayerButton.addActionListener(new AddNewPlayerListener(1));
		blackPlayerComboPanel.setLayout(new FlowLayout());
		blackPlayerComboPanel.add(blackPlayerScrollPane);
		blackPlayerComboPanel.add(blackPlayerSelectButton);
		blackPlayerComboPanel.add(newBlackPlayerButton);
		blackPlayerPanel.add(blackPlayerComboPanel, BorderLayout.NORTH);

		JPanel blackstats = new JPanel(new GridLayout(3, 3));
		blackstats.add(new JLabel("Name   :"));
		blackstats.add(new JLabel("Played :"));
		blackstats.add(new JLabel("Won    :"));

		blackPlayerPanel.add(blackstats, BorderLayout.WEST);
	}

	private void createWhitePlayerPanel() {
		whitePlayerDetailsPanel = new JPanel(new GridLayout(3, 3));

		whitePlayerPanel = new JPanel();
		whitePlayerComboPanel = new JPanel();
		whitePlayerPanel.setBorder(BorderFactory.createTitledBorder(null, "White Player", TitledBorder.TOP,
				TitledBorder.CENTER, new Font("times new roman", Font.BOLD, 18), Color.RED));
		whitePlayerPanel.setLayout(new BorderLayout());

		JPanel whitestats = new JPanel(new GridLayout(3, 3));
		whitestats.add(new JLabel("Name   :"));
		whitestats.add(new JLabel("Played :"));
		whitestats.add(new JLabel("Won    :"));

		whitePlayerCombo = new JComboBox<String>(getPlayerNames());

		whitePlayerScrollPane = new JScrollPane(whitePlayerCombo);
		whitePlayerComboPanel.setLayout(new FlowLayout());

		whitePlayerSelectButton = new Button("Select");
		whitePlayerSelectButton.addActionListener(new PlayerSelectionListener(0));
		newWhitePlayerButton = new Button("New Player");
		newWhitePlayerButton.addActionListener(new AddNewPlayerListener(0));

		whitePlayerComboPanel.add(whitePlayerScrollPane);
		whitePlayerPanel.add(whitePlayerComboPanel, BorderLayout.NORTH);
		whitePlayerComboPanel.add(whitePlayerSelectButton);
		whitePlayerComboPanel.add(newWhitePlayerButton);
		whitePlayerPanel.add(whitestats, BorderLayout.WEST);
	}

	private void initializeTimer() {
		timeSlider = new JSlider();
		timeSlider.setMinimum(1);
		timeSlider.setMaximum(15);
		timeSlider.setValue(1);
		timeSlider.setMajorTickSpacing(2);
		timeSlider.setPaintLabels(true);
		timeSlider.setPaintTicks(true);
		timeSlider.addChangeListener(new TimeChangeListener());
	}

	private String[] getPlayerNames() {
		ArrayList<Player> playersList = Player.fetch_players();
		Iterator<Player> witr = playersList.iterator();
		ArrayList<String> playerNames = new ArrayList<String>();
		while (witr.hasNext())
			playerNames.add(witr.next().name());

		return playerNames.toArray(new String[playerNames.size()]);
	}

	public void changeTurn() {
		King king = chessBoardState.getKing(currentTurn);
		Cell kingsCell = chessBoardState.getCell(king);
		if (kingsCell.ischeck()) {
			currentTurn ^= 1;
			endGame();
		}

		if (!possibleDestinations.isEmpty()) {
			clearDestinationList(possibleDestinations);
		}

		if (previousCell != null) {
			previousCell.deselect();
			previousCell = null;
		}
		
		currentTurn ^= 1;
		if (!end && timer != null) {
			timer.reset();
			timer.start();
			playerViewPanel.remove(turnLabel);
			if (move == WHITE_PLAYER)
				move = BLACK_PLAYER;
			else
				move = WHITE_PLAYER;

			turnLabel.setText(move);
			playerViewPanel.add(turnLabel);
		}
	}

	private void clearDestinationList(ArrayList<Cell> destList) {
		ListIterator<Cell> it = destList.listIterator();
		while (it.hasNext())
			it.next().removepossibledestination();
	}

	private void highlightDestinations(ArrayList<Cell> destList) {
		ListIterator<Cell> destListIterator = destList.listIterator();
		while (destListIterator.hasNext())
			destListIterator.next().setpossibledestination();
	}

	private boolean isCheckAfetrMove(Cell source, Cell target) {
		return isMoveAllowed(source, target, currentTurn);
	}

	private boolean isMoveAllowed(Cell source, Cell target, final int turn) {
		BoardState newboardstate = null;
		try {
			newboardstate = (BoardState) chessBoardState.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		if (newboardstate.getPiece(target) != null)
			newboardstate.getCell(target).removePiece();

		newboardstate.getCell(target).setPiece(newboardstate.getPiece(source));
		if (newboardstate.getPiece(target) instanceof King) {
			newboardstate.updateKing(turn, target);
		}

		newboardstate.getCell(source).removePiece();
		if (newboardstate.getKing(turn).isindanger(newboardstate.getChessBoard()) == true)
			return true;
		else
			return false;
	}

	private ArrayList<Cell> filterAllowedMoves(ArrayList<Cell> possibleMovesList, final Cell source) {
		return allowedCheckMoves(possibleMovesList, source, currentTurn);
	}

	private ArrayList<Cell> allowedCheckMoves(ArrayList<Cell> possibleMovesList, final Cell source, final int turn) {
		ArrayList<Cell> allowedMoves = new ArrayList<Cell>();
		ListIterator<Cell> possibleMovesIterator = possibleMovesList.listIterator();

		while (possibleMovesIterator.hasNext()) {
			Cell target = possibleMovesIterator.next();
			if (!isMoveAllowed(source, target, turn)) {
				allowedMoves.add(target);
			}
		}

		return allowedMoves;
	}

	public boolean isCheckMate(int color) {
		ArrayList<Cell> possibleMovesForKing = new ArrayList<Cell>();
		for (int i = 0; i < Board.ROWS; i++) {
			for (int j = 0; j < Board.COLUMNS; j++) {
				if (chessBoardState.getPiece(i, j) != null && chessBoardState.getPiece(i, j).getcolor() == color) {
					possibleMovesForKing.clear();
					possibleMovesForKing = chessBoardState.getPiece(i, j).move(chessBoardState.getChessBoard(), i, j);
					possibleMovesForKing = allowedCheckMoves(possibleMovesForKing, chessBoardState.getCell(i, j),
							color);
					if (possibleMovesForKing.size() != 0)
						return false;
				}
			}
		}

		return true;
	}

	@SuppressWarnings("deprecation")
	private void endGame() {
		clearDestinationList(possibleDestinations);

		stopTimer();
		declareWinner();

		whitePlayerPanel.remove(whitePlayerDetailsPanel);
		blackPlayerPanel.remove(blackPlayerDetailsPnale);
		timeDisplayPanle.remove(timeLabel);

		timeDisplayPanle.add(startButton);
		playerViewPanel.remove(moveLabel);
		playerViewPanel.remove(turnLabel);
		playerViewPanel.revalidate();
		playerViewPanel.add(timeSlider);

		splitPane.remove(boardPanel);
		splitPane.add(temporaryPanel);
		newWhitePlayerButton.enable();
		newBlackPlayerButton.enable();
		whitePlayerSelectButton.enable();
		blackPlayerSelectButton.enable();
		end = true;

		createContentLayout();
		chessBoardState = new BoardState(boardPanel, this);

		setVisible(true);
		setResizable(false);
	}

	private void stopTimer() {
		timeDisplayPanle.disable();
		timer.countdownTimer.stop();
	}

	private void declareWinner() {
		String winner = null;
		if (previousCell != null)
			previousCell.removePiece();
		if (currentTurn == 0) {
			whitePlayer.updateGamesWon();
			whitePlayer.Update_Player();
			winner = whitePlayer.name();
		} else {
			blackPlayer.updateGamesWon();
			blackPlayer.Update_Player();
			winner = blackPlayer.name();
		}

		JOptionPane.showMessageDialog(boardPanel, "Checkmate!!!\n" + winner + " wins");
	}

	public void makeMove(Cell currentCell) {
		if (previousCell == null) {
			if (currentCell.getpiece() != null) {
				if (currentCell.getpiece().getcolor() != currentTurn)
					return;
				currentCell.select();
				previousCell = currentCell;
				possibleDestinations.clear();
				possibleDestinations = currentCell.getpiece().move(chessBoardState.getChessBoard(), currentCell.x,
						currentCell.y);
				if (currentCell.getpiece() instanceof King)
					possibleDestinations = filterAllowedMoves(possibleDestinations, currentCell);
				else {
					if (chessBoardState.getCell(chessBoardState.getKing(currentTurn)).ischeck())
						possibleDestinations = new ArrayList<Cell>(
								filterAllowedMoves(possibleDestinations, currentCell));
					else if (possibleDestinations.isEmpty() == false
							&& isCheckAfetrMove(currentCell, possibleDestinations.get(0)))
						possibleDestinations.clear();
				}
				highlightDestinations(possibleDestinations);
			}
		} else {
			if (currentCell.x == previousCell.x && currentCell.y == previousCell.y) {
				currentCell.deselect();
				clearDestinationList(possibleDestinations);
				possibleDestinations.clear();
				previousCell = null;
			} else if (currentCell.getpiece() == null
					|| previousCell.getpiece().getcolor() != currentCell.getpiece().getcolor()) {
				if (currentCell.ispossibledestination()) {
					if (currentCell.getpiece() != null)
						currentCell.removePiece();
					currentCell.setPiece(previousCell.getpiece());
					if (previousCell.ischeck())
						previousCell.removecheck();
					previousCell.removePiece();

					// Setting check to other king'cell
					if (chessBoardState.getKing(currentTurn ^ 1).isindanger(chessBoardState.getChessBoard())) {
						chessBoardState.getCell(chessBoardState.getKing(currentTurn ^ 1)).setcheck();
						if (isCheckMate(chessBoardState.getKing(currentTurn ^ 1).getcolor())) {
							previousCell.deselect();
							if (previousCell.getpiece() != null)
								previousCell.removePiece();
							endGame();
						}
					}
					if (chessBoardState.getKing(currentTurn).isindanger(chessBoardState.getChessBoard()) == false)
						chessBoardState.getCell(chessBoardState.getKing(currentTurn)).removecheck();
					if (currentCell.getpiece() instanceof King) {
						chessBoardState.updateKing(currentTurn, currentCell);
					}
					changeTurn();
					if (!end) {
						timer.reset();
						timer.start();
					}
				}
				if (previousCell != null) {
					previousCell.deselect();
					previousCell = null;
				}
				clearDestinationList(possibleDestinations);
				possibleDestinations.clear();
			} else if (previousCell.getpiece().getcolor() == currentCell.getpiece().getcolor()) {
				previousCell.deselect();
				clearDestinationList(possibleDestinations);
				possibleDestinations.clear();
				currentCell.select();
				previousCell = currentCell;
				possibleDestinations = currentCell.getpiece().move(chessBoardState.getChessBoard(), currentCell.x,
						currentCell.y);
				if (currentCell.getpiece() instanceof King)
					possibleDestinations = filterAllowedMoves(possibleDestinations, currentCell);
				else {
					if (chessBoardState.getCell(chessBoardState.getKing(currentTurn)).ischeck())
						possibleDestinations = new ArrayList<Cell>(
								filterAllowedMoves(possibleDestinations, currentCell));
					else if (possibleDestinations.isEmpty() == false
							&& isCheckAfetrMove(currentCell, possibleDestinations.get(0)))
						possibleDestinations.clear();
				}
				highlightDestinations(possibleDestinations);
			}
		}

		if (currentCell.getpiece() != null && currentCell.getpiece() instanceof King) {
			chessBoardState.updateKing(((King) currentCell.getpiece()), currentCell);
		}
	}

	class StartButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			if (whitePlayer == null || blackPlayer == null) {
				JOptionPane.showMessageDialog(controlPanel, "Fill in the details");
				return;
			}

			updatePlayersRecord();
			disablePlayerButtons();

			splitPane.remove(temporaryPanel);
			splitPane.add(boardPanel);
			playerViewPanel.remove(timeSlider);

			addNewMoveLabel();
			addNewTurnLabel();

			timeDisplayPanle.remove(startButton);
			timeDisplayPanle.add(timeLabel);
			timer.start();
		}

		private void addNewTurnLabel() {
			turnLabel = new JLabel(move);
			turnLabel.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
			turnLabel.setForeground(Color.blue);
			playerViewPanel.add(turnLabel);
		}

		private void addNewMoveLabel() {
			moveLabel = new JLabel("Move:");
			moveLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
			moveLabel.setForeground(Color.red);
			playerViewPanel.add(moveLabel);
		}

		@SuppressWarnings("deprecation")
		private void disablePlayerButtons() {
			newWhitePlayerButton.disable();
			newBlackPlayerButton.disable();

			whitePlayerSelectButton.disable();
			blackPlayerSelectButton.disable();
		}

		private void updatePlayersRecord() {
			whitePlayer.updateGamesPlayed();
			whitePlayer.Update_Player();

			blackPlayer.updateGamesPlayed();
			blackPlayer.Update_Player();
		}
	}

	class TimeChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent arg0) {
			timeRemaining = timeSlider.getValue() * 60;
		}
	}

	class PlayerSelectionListener implements ActionListener {
		private int color;

		PlayerSelectionListener(int i) {
			color = i;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			selectedPlayer = null;

			JComboBox<String> currentPlayerNamesComboBox = (color == 0) ? whitePlayerCombo : blackPlayerCombo;
			JComboBox<String> otherPlayerNamesComboBox = (color == 0) ? blackPlayerCombo : whitePlayerCombo;

			ArrayList<Player> otherPlayersList = Player.fetch_players();
			ArrayList<Player> currentPlayersList = Player.fetch_players();
			if (otherPlayersList.isEmpty())
				return;

			JPanel playerDetails = (color == 0) ? whitePlayerDetailsPanel : blackPlayerDetailsPnale;
			JPanel playerPanel = (color == 0) ? whitePlayerPanel : blackPlayerPanel;

			if (isSelected == true)
				playerDetails.removeAll();

			String playerName = (String) currentPlayerNamesComboBox.getSelectedItem();
			Iterator<Player> currentPlayerIterator = currentPlayersList.iterator();
			Iterator<Player> otherPlayerIterator = otherPlayersList.iterator();
			while (currentPlayerIterator.hasNext()) {
				Player player = currentPlayerIterator.next();
				if (player.name().equals(playerName)) {
					selectedPlayer = player;
					break;
				}
			}

			while (otherPlayerIterator.hasNext()) {
				Player p = otherPlayerIterator.next();
				if (p.name().equals(playerName)) {
					otherPlayersList.remove(p);
					break;
				}
			}

			if (selectedPlayer == null)
				return;

			if (color == 0)
				whitePlayer = selectedPlayer;
			else
				blackPlayer = selectedPlayer;

			otherPlayerNamesComboBox.removeAllItems();
			for (Player s : otherPlayersList)
				otherPlayerNamesComboBox.addItem(s.name());

			playerDetails.add(new JLabel(" " + selectedPlayer.name()));
			playerDetails.add(new JLabel(" " + selectedPlayer.gamesplayed()));
			playerDetails.add(new JLabel(" " + selectedPlayer.gameswon()));

			playerPanel.revalidate();
			playerPanel.repaint();
			playerPanel.add(playerDetails);
			isSelected = true;
		}

	}

	class AddNewPlayerListener implements ActionListener {
		private int color;

		AddNewPlayerListener(int i) {
			color = i;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			addNewPlayer();
		}

		private void addNewPlayer() {
			JPanel playerPanel = (color == 0) ? whitePlayerPanel : blackPlayerPanel;
			ArrayList<Player> playersList = Player.fetch_players();
			Iterator<Player> playersListIterator = playersList.iterator();

			String playerName = JOptionPane.showInputDialog(playerPanel, "Enter your name");

			if (playerName == null || playerName.isEmpty()) {
				return;
			}

			while (playersListIterator.hasNext()) {
				if (playersListIterator.next().name().equals(playerName)) {
					JOptionPane.showMessageDialog(playerPanel, "Player exists");
					return;
				}
			}

			Player newPlayer = new Player(playerName);
			newPlayer.Update_Player();
			if (color == 0)
				whitePlayer = newPlayer;
			else
				blackPlayer = newPlayer;

			updatePlayerPanel(playerPanel, newPlayer);
			isSelected = true;
		}

		private void updatePlayerPanel(JPanel playerPanel, Player newPlayer) {
			JPanel panelDetails = (color == 0) ? whitePlayerDetailsPanel : blackPlayerDetailsPnale;
			panelDetails.removeAll();
			panelDetails.add(new JLabel(" " + newPlayer.name()));
			panelDetails.add(new JLabel(" " + newPlayer.gamesplayed()));
			panelDetails.add(new JLabel(" " + newPlayer.gameswon()));
			playerPanel.revalidate();
			playerPanel.repaint();
			playerPanel.add(panelDetails);
		}
	}
}