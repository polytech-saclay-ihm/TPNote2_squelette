package application.personnage;

import java.io.FileInputStream;
import java.util.Map;

public interface Entite {

    abstract Map<String, Double> attaquerSpecial(Personnage cible);

    String getNom();

    int getPvMax();

    int getAttaque();

    int getDefense();

    public String getNomAttaqueSpeciale();

    public FileInputStream getImageFile();

}
