package project.scores;

import java.io.Serializable;

public class Score
    implements Serializable {

    private final String nickname;
    private final int score;

    public Score(String nickname, int score) {
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
