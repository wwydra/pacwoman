package Projekt_2.Wyniki;

import java.io.Serializable;

public class Wynik
    implements Serializable {

    private String nickname;
    private int score;

    public Wynik(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return String.format("%-20s%10d", this.nickname, this.score);
    }
}
