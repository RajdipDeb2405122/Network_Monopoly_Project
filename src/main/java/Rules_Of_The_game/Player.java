package Rules_Of_The_game;

public interface Player {

    String getPlayer_name();
    void setPlayer_name(String name);

    int getPlayer_amount();
    void setPlayer_amount(int amount);

    int getPlayer_place();
    void setPlayer_place(int place);

    boolean[] getPlayers_owned_places();

    void resetOwnedPlaces();

    int getExp_cnt();
    void setExp_cnt(int exp);
}
