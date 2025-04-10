package project.scores;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Ranking
    implements Serializable{

    private final List<Score> scores;

    public Ranking() {
        this.scores = loadScores();
    }

    public void addScore(Score score){
        scores.add(score);
        saveScores();
    }

    public List<Score> loadScores(){
        List<Score> scoresList = new ArrayList<>();
        File file = new File("src/project/scores/HighScores.ser");
        if (!file.exists() || file.length() == 0) {
            System.out.println("No saved scores found or file is empty.");
            return scoresList;
        }
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)){
            scoresList = (List<Score>) ois.readObject();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        scoresList.sort((w1, w2) -> w2.getScore() - w1.getScore());
        return scoresList;
    }

    public void saveScores(){
        scores.sort((w1, w2) -> w2.getScore() - w1.getScore());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/project/scores/HighScores.ser"))){
            oos.writeObject(scores);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<Score> getScores(){
        scores.sort((w1, w2) -> w2.getScore() - w1.getScore());
        return scores;
    }
}
