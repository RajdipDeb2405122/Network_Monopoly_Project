package Rules_Of_The_game;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class PlayerArea implements Player {

	private String player_name;
	private int player_amount;
	private ImageView domino;
	private ImageView arrow;
	private int player_place;

    private int exp_cnt = 1;


	private boolean[] players_owned_places = new boolean[41];

	private Label label_amount;



	public PlayerArea() {
		resetOwnedPlaces();
	}

	public PlayerArea(String player_name,
					  int player_amount,
					  ImageView domino,
					  ImageView arrow,
					  int player_place,
					  Label label_amount) {

		this.player_name = player_name;
		this.player_amount = player_amount;
		this.domino = domino;
		this.arrow = arrow;
		this.player_place = player_place;
		this.label_amount = label_amount;

		resetOwnedPlaces();
	}


    //Reset
	@Override
	public void resetOwnedPlaces() {
		for (int i = 0; i < players_owned_places.length; i++) {
			players_owned_places[i] = false;
		}
	}




	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public int getPlayer_amount() {
		return player_amount;
	}

	public void setPlayer_amount(int player_amount) {
		this.player_amount = player_amount;


		if (label_amount != null) {
			label_amount.setText(player_amount + " $");
		}
	}

	public ImageView getDomino() {
		return domino;
	}

	public void setDomino(ImageView domino) {
		this.domino = domino;
	}

	public ImageView getArrow() {
		return arrow;
	}

	public void setArrow(ImageView arrow) {
		this.arrow = arrow;
	}

	public int getPlayer_place() {
		return player_place;
	}

	public void setPlayer_place(int player_place) {
		this.player_place = player_place;
	}

	public boolean[] getPlayers_owned_places() {
		return players_owned_places;
	}

	public void setPlayers_owned_places(boolean[] players_owned_places) {
		this.players_owned_places = players_owned_places;
	}

	public Label getLabel_amount() {
		return label_amount;
	}

	public void setLabel_amount(Label label_amount) {
		this.label_amount = label_amount;
	}


    @Override
    public int getExp_cnt() {
        return exp_cnt;
    }

    @Override
    public void setExp_cnt(int exp_cnt) {
        this.exp_cnt = exp_cnt;
    }

}
