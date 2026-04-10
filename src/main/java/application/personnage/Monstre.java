package application.personnage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

public enum Monstre implements Entite {
    // Déclaration des monstres avec leurs stats
    BLUE_MONSTER("Monstre bleu", 30, 15, 8, 20, 50, "Coup de corne", "blue_monster.png") {
        @Override
        public Map<String, Double> attaquerSpecial(Personnage cible) {
            double degats_coeff = (Math.random() > 0.5) ? (int) (1.2) : 0;
            String effect = (degats_coeff > 0) ? "Coup critique !" : "Coup manqué";
            return Map.of(effect, degats_coeff);
        }
    },
    BABY_CTHULU("Baby Cthulhu", 50, 20, 12, 40, 30, "Coup de tentacules", "baby_cthulhu.png") {
        @Override
        public Map<String, Double> attaquerSpecial(Personnage cible) {
            double degats_coeff = (Math.random() > 0.5) ? (int) (1.4) : 0;
            String effect = (degats_coeff > 0) ? "Coup critique !" : "Coup manqué";
            return Map.of(effect, degats_coeff);
        }
    },
    ORC("Orc", 60, 30, 20, 60, 15, "Coup de massue", "orc.png") {
        @Override
        public Map<String, Double> attaquerSpecial(Personnage cible) {
            double degats_coeff = 1.1; // 30% de dégâts en plus
            cible.augmenterDefenseCombat((int) (cible.getDefenseActuelle() * 0.1)); // Diminue la défense de la cible de
                                                                                    // 10%
            return Map.of("Défense de " + cible.getPseudo() + " diminuée", degats_coeff);
        }
    },
    DEMON("Démon", 100, 50, 40, 100, 5, "Pacte avec le diable", "demon.png") {
        @Override
        public Map<String, Double> attaquerSpecial(Personnage cible) {
            double degats_coeff = 1.5;
            cible.augmenterAttaqueCombat((int) (cible.getAttaqueCombat() * 1.2));
            return Map.of("Attaque de " + cible.getPseudo() + " augmentée", degats_coeff);
        }
    };

    // Attributs
    private final String nom;
    private final int pvMax;
    private final int attaque;
    private final int defense;
    private final int recompenseXP;
    private final int poids;
    private final String nom_attaque_speciale;
    private final FileInputStream image_file;

    // Constructeur
    Monstre(String nom, int pvMax, int attaque, int defense, int recompenseXP, int poids, String nom_attaque_speciale,
            String image_file) {
        this.nom = nom;
        this.pvMax = pvMax;
        this.attaque = attaque;
        this.defense = defense;
        this.recompenseXP = recompenseXP;
        this.poids = poids;
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

    // Getters
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

    public int getRecompenseXP() {
        return recompenseXP;
    }

    public int getPoids() {
        return poids;
    }

    public String getNomAttaqueSpeciale() {
        return nom_attaque_speciale;
    }

    public static int getIndexFromName(String name) {
        for (int i = 0; i < Monstre.values().length; i++) {
            if (Monstre.values()[i].nom.contentEquals(name)) {
                return i;
            }
        }
        return -1;
    }
}
