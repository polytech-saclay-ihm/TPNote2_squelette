package application.personnage;

import application.objet.*;

import java.util.*;

public class Personnage {
    private String pseudo;
    private final Entite type; // Heros ou Monstre
    private int vie;
    private int vie_max;
    private int attaqueActuelle;
    private int defenseActuelle;
    private int niveau;
    private int xp;
    private int en_charge;
    private List<Objet> inventaire;
    private List<Arme> equipements;
    private int attaque_bonus_combat;
    private int defense_bonus_combat;

    public Personnage(String pseudo, Entite type) {
        this(pseudo, type, 2, 9);
    }

    public Personnage(String pseudo, Entite type, int equipement_max, int inventaire_max) {
        this.pseudo = pseudo;
        this.type = type;
        this.vie = type.getPvMax();
        this.vie_max = type.getPvMax();
        this.attaqueActuelle = type.getAttaque();
        this.defenseActuelle = type.getDefense();
        this.niveau = 0;
        this.xp = 0;
        initCharge();
        initEquipement(equipement_max);
        initInventaire(inventaire_max);
    }

    public void initCharge() {
        this.en_charge = -1;
    }

    public void initEquipement(int equipement_max) {
        equipements = Arrays.asList(new Arme[equipement_max]);
        Collections.fill(equipements, Arme.VIDE);
    }

    public void initInventaire(int inventaire_max) {
        inventaire = Arrays.asList(new Objet[inventaire_max]);
        Collections.fill(inventaire, Consommable.VIDE);
    }

    public void ajouterObjetInventaire(Objet objet) {
        inventaire.set(getPremierInventaireLibre(), objet);
    }

    public void ajouterObjetInventaire(int idx_inventaire, Objet objet) {
        inventaire.set(idx_inventaire, objet);
    }

    // Méthode pour équiper une arme
    public void equiper(Arme arme) {
        arme.appliquerEffet(this);
        equipements.set(getPremierEquipementLibre(), arme);

    }

    // Méthode pour équiper une arme
    public void equiper(Arme arme, int index) {
        arme.appliquerEffet(this);
        equipements.set(index, arme);
    }

    public void jeterEquipement(int idx_equipement) {
        equipements.get(idx_equipement).retirerEffet(this);
        equipements.set(idx_equipement, Arme.VIDE);
    }

    public void rangerEquipement(int idx_equipement, int idx_inventaire) {
        ajouterObjetInventaire(idx_inventaire, equipements.get(idx_equipement));
        jeterEquipement(idx_equipement);
    }

    public void echangerEquipement(int idx_inventaire, int idx_equipement) {
        Arme equipement_new = (Arme) inventaire.get(idx_inventaire);
        rangerEquipement(idx_equipement, idx_inventaire);
        equiper(equipement_new, idx_equipement);
    }

    // Méthode pour utiliser un objet consommable
    public void utiliserObjet(int idx_objet) {
        inventaire.get(idx_objet).appliquerEffet(this);
        jeterObjet(idx_objet);
    }

    public void jeterObjet(int idx_objet) {
        inventaire.set(idx_objet, Consommable.VIDE);
    }

    // Méthode pour subir des dégâts
    public void subirDegats(int degats) {
        this.vie = Math.max(0, this.vie - degats);
    }

    public boolean isEnVie() {
        return (vie > 0);
    }

    public void updateCharge() {
        if (en_charge == -1) {
            en_charge = 1;
        } else if (en_charge >= 0) {
            en_charge -= 1;
        }
    }

    public int getNbTourChargeRestant() {
        return en_charge;
    }

    // Getters/Setters
    public String getPseudo() {
        return pseudo;
    }

    public int getVie() {
        return vie;
    }

    public int getVieMax() {
        return vie_max;
    }

    public int getAttaqueActuelle() {
        return attaqueActuelle;
    }

    public int getDefenseActuelle() {
        return defenseActuelle;
    }

    public void augmenterAttaqueActuelle(int attaque) {
        attaqueActuelle += attaque;
    }

    public void augmenterDefenseActuelle(int defense) {
        defenseActuelle += defense;
    }

    public int getAttaqueCombat() {
        return attaqueActuelle + attaque_bonus_combat;
    }

    public int getDefenseCombat() {
        return defenseActuelle + defense_bonus_combat;
    }

    public void augmenterAttaqueCombat(int attaque) {
        attaque_bonus_combat += attaque;
    }

    public void augmenterDefenseCombat(int defense) {
        defense_bonus_combat += defense;
    }

    public void augmenterVie(int vie) {
        this.vie += vie;
        if (this.vie > vie_max) {
            this.vie = vie_max;
        }
    }

    public String getNom() {
        return type.getNom();
    }

    public List<Objet> getInventaire() {
        return inventaire;
    }

    public List<String> getNomEquipementAt(List<Integer> liste_index) {
        ArrayList<String> liste_armes = new ArrayList<String>();
        for (Integer idx : liste_index) {
            liste_armes.add(equipements.get(idx).getNom());
        }
        return liste_armes;
    }

    public List<Integer> getListIndexEquipementOccupe() {
        ArrayList<Integer> list_index = new ArrayList<>();
        for (int i = 0; i < equipements.size(); i++) {
            if (!equipements.get(i).isVide()) {
                list_index.add(i);
            }
        }
        return list_index;
    }

    public List<Integer> getListIndexEquipementLibre() {
        ArrayList<Integer> list_index = new ArrayList<>();
        for (int i = 0; i < equipements.size(); i++) {
            if (equipements.get(i).isVide()) {
                list_index.add(i);
            }
        }
        return list_index;
    }

    public int getNbEquipementLibre() {
        int libre = 0;
        for (Arme arme : equipements) {
            if (arme.isVide()) {
                libre += 1;
            }
        }
        return libre;
    }

    public int getPremierEquipementLibre() {
        int idx = 0;
        while (!equipements.get(idx).isVide()) {
            idx += 1;
        }
        return idx;
    }

    public List<String> getNomInventaireAt(List<Integer> liste_index) {
        ArrayList<String> liste_objets = new ArrayList<String>();
        for (Integer idx : liste_index) {
            liste_objets.add(inventaire.get(idx).getNom());
        }
        return liste_objets;
    }

    public List<Integer> getListIndexInventaireOccupe() {
        ArrayList<Integer> list_index = new ArrayList<>();
        for (int i = 0; i < inventaire.size(); i++) {
            if (!inventaire.get(i).isVide()) {
                list_index.add(i);
            }
        }
        return list_index;
    }

    public List<Integer> getListIndexInventaireConsommable() {
        ArrayList<Integer> list_index = new ArrayList<>();
        for (int i = 0; i < inventaire.size(); i++) {
            if (!inventaire.get(i).isVide() && (inventaire.get(i).getClass() == Consommable.class)) {
                list_index.add(i);
            }
        }
        return list_index;
    }

    public List<Integer> getListIndexInventaireVide() {
        ArrayList<Integer> list_index = new ArrayList<>();
        for (int i = 0; i < inventaire.size(); i++) {
            if (inventaire.get(i).isVide()) {
                list_index.add(i);
            }
        }
        return list_index;
    }

    public int getNbInventaireLibre() {
        int libre = 0;
        for (Objet objet : inventaire) {
            if (objet.isVide()) {
                libre += 1;
            }
        }
        return libre;
    }

    public int getPremierInventaireLibre() {
        int idx = 0;
        while (!inventaire.get(idx).isVide()) {
            idx += 1;
        }
        return idx;
    }

    public List<Arme> getEquipements() {
        return equipements;
    }

    public int getNiveau() {
        return niveau;
    }

    public Entite getType() {
        return type;
    }

    public void setNiveau(int niveau) {
        int number_update = niveau - this.niveau;
        for (int i = 0; i < number_update; i++) {
            updateStats();
        }
        this.niveau = niveau;
    }

    public int getXp() {
        return xp;
    }

    public void addXp(int xp) {
        this.xp += xp;
        if (this.xp > getXpLevelUp()) {
            this.xp = this.xp - getXpLevelUp();
            niveau += 1;
            updateStats();
        }
    }

    public void updateStats() {
        attaqueActuelle += 0.2 * attaqueActuelle;
        defenseActuelle += 0.2 * defenseActuelle;
        vie_max += 0.2 * vie_max;
    }

    public int getXpLevelUp() {
        return (int) (25 * Math.pow(niveau + 1, 1.5));
    }
}
