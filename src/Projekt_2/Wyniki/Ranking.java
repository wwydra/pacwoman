package Projekt_2.Wyniki;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Ranking
    implements Serializable{

    private List<Wynik> wyniki;

    public Ranking() {
        this.wyniki = zaladujWyniki();
    }

    public void dodajWynik(Wynik wynik){
        wyniki.add(wynik);
        zapiszWyniki();
    }

    public List<Wynik> zaladujWyniki(){
        List<Wynik> wyniki = new ArrayList<>();
        try{
            FileInputStream fis = new FileInputStream("src/Projekt_2/Wyniki/HighScores.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            wyniki = (List<Wynik>) ois.readObject();
            ois.close();
            fis.close();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        wyniki.sort((w1, w2) -> w2.getScore() - w1.getScore());
        return wyniki;
    }

    public void zapiszWyniki(){
        wyniki.sort((w1, w2) -> w2.getScore() - w1.getScore());
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/Projekt_2/Wyniki/HighScores.ser"));
            oos.writeObject(wyniki);
            oos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<Wynik> getWyniki(){
        wyniki.sort((w1, w2) -> w2.getScore() - w1.getScore());
        return wyniki;
    }
}
