package application.personnage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public enum Heros implements Entite {
    GUERRIER("Guerrier", 50, 14, 20, "Coup puissant", "guerrier.png") {
        @Override
        public Map<String, Double> attaquerSpecial(Personnage cible) {
            return Map.of("Dégâts doublés!", 2.0);
        }
    },
    VOLEUR("Voleur", 40, 20, 15, "Attaque furtive", "voleur.png") {
        @Override
        public Map<String, Double> attaquerSpecial(Personnage cible) {
            double degats_coeff = (Math.random() > 0.33) ? 3 : 0;
            String effect = (degats_coeff > 0) ? "Coup critique !" : "Coup manqué";
            return Map.of(effect, degats_coeff);
        }
    },
    MAGE("Mage", 30, 30, 10, "Magie instable", "mage.png") {
        @Override
        public Map<String, Double> attaquerSpecial(Personnage cible) {
            return Map.of("Dégâts magiques!", (1 + Math.random()));
        }
    };

    private final String nom;
    private final int pvMax;
    private final int attaque;
    private final int defense;
    private final String nom_attaque_speciale;
    private final FileInputStream image_file;

    Heros(String nom, int pvMax, int attaque, int defense, String nom_attaque_speciale, String image_file) {
        this.nom = nom;
        this.pvMax = pvMax;
        this.attaque = attaque;
        this.defense = defense;
        this.nom_attaque_speciale = nom_attaque_speciale;
        try {
            this.image_file = new FileInputStream("src/main/resources/application/" + image_file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public FileInputStream getImageFile() {
        return image_file;
    }

    public String getNom() {
        return nom;
    }

    public int getPvMax() {
        return pvMax;
    }

    public int getAttaque() {
        return attaque;
    }

    public int getDefense() {
        return defense;
    }

    public String getNomAttaqueSpeciale() {
        return nom_attaque_speciale;
    }
}
