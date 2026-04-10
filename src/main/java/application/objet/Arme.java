package application.objet;

import application.personnage.Personnage;

public enum Arme implements Objet {
    EPEE("Epée", 10, 0, 20),
    CASQUE("Casque", 0, 10, 20),
    HACHE("Hache", 20, 0, 10),
    BOUCLIER("Bouclier", 0, 20, 10),
    VIDE("Vide", 0, 0, 5);

    private String nom_base;
    private int attaque_bonus;
    private int defense_bonus;
    private final int poids;

    Arme(String nom_base, int attaque_bonus, int defense_bonus, int poids) {
        this.nom_base = nom_base;
        this.attaque_bonus = attaque_bonus;
        this.defense_bonus = defense_bonus;
        this.poids = poids;
    }

    @Override
    public String getNom() {
        return nom_base;
    }

    public String getStats() {
        String attaque = (attaque_bonus > 0) ? "+" + attaque_bonus + "ATT " : "";
        String defense = (defense_bonus > 0) ? "+" + defense_bonus + "DEF " : "";
        return (!(attaque + defense).isEmpty()) ? attaque + defense : "";
    }

    public boolean isVide() {
        return nom_base.contentEquals("Vide");
    }

    public int getAttaqueBonus() {
        return attaque_bonus;
    }

    public int getDefenseBonus() {
        return defense_bonus;
    }

    public int getPoids() {
        return poids;
    }

    @Override
    public void appliquerEffet(Personnage cible) {
        cible.augmenterAttaqueActuelle(attaque_bonus);
        cible.augmenterDefenseActuelle(defense_bonus);
    }

    public void retirerEffet(Personnage cible) {
        cible.augmenterAttaqueActuelle(-attaque_bonus);
        cible.augmenterDefenseActuelle(-defense_bonus);
    }

}
