package application.objet;

import application.personnage.Personnage;

public enum Consommable implements Objet {
    POTION("Potion", 10, 0, 20),
    ELIXIR("Elixir", 0, 10, 0),
    VIDE("Vide", 0, 0, 5);

    private String nom_base;
    private int vie_bonus;
    private final int poids;

    Consommable(String nom_base, int vie_bonus, int mana_bonus, int poids) {
        this.nom_base = nom_base;
        this.vie_bonus = vie_bonus;
        this.poids = poids;
    }

    @Override
    public String getNom() {
        return nom_base;
    }

    public String getStats() {
        return (vie_bonus > 0) ? "+" + vie_bonus + "PV " : "";
    }

    @Override
    public void appliquerEffet(Personnage cible) {
        cible.augmenterVie(vie_bonus);

    }

    public boolean isVide() {
        return nom_base.contentEquals("Vide");
    }

    public int getVieBonus() {
        return vie_bonus;
    }

    public int getPoids() {
        return poids;
    }
}
