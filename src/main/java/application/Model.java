package application;

import application.objet.Arme;
import application.objet.Consommable;
import application.objet.Objet;
import application.personnage.*;

import java.util.*;

public class Model {

    public static int INVENTAIRE_MAX = 9;
    public static int EQUIPEMENTS_MAX = 2;

    private Personnage heros;
    private Personnage adversaire_actuel;
    private int tour;
    private int num_combat = 1;
    private ArrayList<String> historique;
    private boolean is_fin_combat = false;
    private boolean is_level_up = false;
    private boolean game_over = false;

    private Objet last_recompense_objet_combat;
    private int last_recompense_xp_combat;

    Model() {
        tour = 0;
        historique = new ArrayList<>();
    }

    public void initPartie(String pseudo, String classe) {
        heros = new Personnage(pseudo, Heros.valueOf(classe.toUpperCase()), EQUIPEMENTS_MAX, INVENTAIRE_MAX);
    }

    public void initCombat() {
        historique.clear();
        tour = 1;
        is_fin_combat = false;
        is_level_up = false;
        game_over = false;
        last_recompense_objet_combat = Arme.VIDE;
        last_recompense_xp_combat = 0;
        Monstre monstre = genererMonstre();
        adversaire_actuel = new Personnage(monstre.getNom(), monstre);
        heros.initCharge();
    }

    public int executerTour(int action_user) {

        // 1 : attaquer, 2 : spécial, 3 : défense
        historique.add(executerAction(action_user, heros, adversaire_actuel));
        int nb_actions = 1;
        if (!adversaire_actuel.isEnVie()) {
            is_fin_combat = true;
            historique.add(adversaire_actuel.getPseudo() + " est mort.");
            nb_actions++;
            genererRecompensesCombat();
            num_combat += 1;
        }
        if (!is_fin_combat) {
            int action_adversaire = choisirActionAdversaire();
            historique.add(executerAction(action_adversaire, adversaire_actuel, heros));
            nb_actions++;
            if (!heros.isEnVie()) {
                is_fin_combat = true;
                historique.add(heros.getPseudo() + " est mort.");
                nb_actions++;
                game_over = true;
            }
        }
        tour += 1;
        return nb_actions;
    }

    public void genererRecompensesCombat() {
        int niveau = heros.getNiveau();
        last_recompense_xp_combat = genererRecompenseXpCombat();
        last_recompense_objet_combat = genererRecompenseObjetCombat();
        if (niveau < heros.getNiveau()) {
            is_level_up = true;
        }

    }

    public int genererRecompenseXpCombat() {
        Monstre monstre = (Monstre) adversaire_actuel.getType();
        int recompense = monstre.getRecompenseXP();
        heros.addXp(recompense);
        return recompense;
    }

    public Objet genererRecompenseObjetCombat() {
        List<Objet> objets_possibles = new ArrayList<Objet>();
        // Ajouter les objets
        objets_possibles.addAll(Arrays.asList(Arme.values()));
        // Ajouter les consommables
        objets_possibles.addAll(Arrays.asList(Consommable.values()));
        // Calculer le poids total
        int poids_total = objets_possibles.stream().mapToInt(Objet::getPoids).sum();

        // Sélectionner un objet aléatoire pondéré
        Random random = new Random();
        int poids_random = random.nextInt(poids_total);
        int cumulativeWeight = 0;

        for (Objet objet : objets_possibles) {
            cumulativeWeight += objet.getPoids();
            if (poids_random < cumulativeWeight) {
                heros.ajouterObjetInventaire(objet);
                return objet;
            }
        }
        return null;
    }

    public boolean isFinCombat() {
        return is_fin_combat;
    }

    public List<String> getActionsPossibles() {
        return (heros.getNbTourChargeRestant() > 0) ? Arrays.asList("Objet", "Passer")
                : Arrays.asList("Attaque", "Spécial", "Défense", "Objet");

    }

    public String executerAction(int action, Personnage source, Personnage cible) {
        String action_string = "";
        switch (action) {

            case 1: // Attaque
                action_string = source.getPseudo() + " attaque ";
                action_string += "(" + attaque(source, cible) + ")";
                break;
            case 2: // Special
                source.updateCharge();
                if (source.getNbTourChargeRestant() > 0) {
                    action_string += source.getPseudo() + " charge " + source.getType().getNomAttaqueSpeciale();
                    action_string += "(" + source.getNbTourChargeRestant() + " tour restant)";
                } else {
                    action_string += source.getPseudo() + " lance " + source.getType().getNomAttaqueSpeciale() + " : ";
                    action_string += special(source, cible);
                    source.updateCharge();
                }
                break;
            case 3: // Defend
                action_string = source.getPseudo() + " défend";
                defense(source);
                break;
            default:
                return action_string;
        }

        return action_string;
    }

    public String attaque(Personnage source, Personnage cible) {
        int degats = calculerDegats(source.getAttaqueCombat(), cible);
        String effet = "- " + degats + "PV";
        cible.subirDegats(degats);
        return effet;
    }

    public int calculerDegats(int val_degats, Personnage cible) {
        return Math.max(0, val_degats - cible.getDefenseCombat() / 2);
    }

    public String special(Personnage source, Personnage cible) {
        Map<String, Double> hash_special = source.getType().attaquerSpecial(cible);
        String effet = hash_special.keySet().stream().findFirst().get();
        int degats = calculerDegats((int) (source.getAttaqueCombat() * hash_special.get(effet)), cible);
        effet += "(- " + degats + " PV)";
        cible.subirDegats(degats);
        return effet;
    }

    public void defense(Personnage source) {
        source.augmenterDefenseCombat((int) (source.getDefenseCombat() * 0.3));
    }

    public Monstre genererMonstre() {
        int poidsTotal = 0;
        for (Monstre monstre : Monstre.values()) {
            poidsTotal += monstre.getPoids();
        }

        // Générer un nombre aléatoire entre 0 et poidsTotal
        int randomValue = (int) (Math.random() * poidsTotal);

        // Sélectionner le monstre en fonction du poids
        int cumulativeWeight = 0;
        for (Monstre monstre : Monstre.values()) {
            cumulativeWeight += monstre.getPoids();
            if (randomValue < cumulativeWeight) {
                return monstre;
            }
        }
        return Monstre.BLUE_MONSTER;
    }

    public int choisirActionAdversaire() {
        if (adversaire_actuel.getNbTourChargeRestant() > 0) {
            return 2;
        }
        // Poids de base pour chaque action
        double poids_attaque = 50; // Probabilité de base : 50%
        double poids_special = 30; // 30%
        double poids_defense = 20; // 20%

        // Ajustement des poids en fonction de l'état du combat
        if (adversaire_actuel.getVie() < adversaire_actuel.getVieMax() * 0.3) {
            poids_defense += 20; // +20% de chance de defendre si PV bas
            poids_attaque -= 10; // -10% de chance d'attaquer
        }

        if (heros.getVie() < heros.getVieMax() * 0.4) {
            poids_attaque += 20; // +20% de chance d'attaquer si le héros est faible
        }

        // Calcul du poids total
        double poidsTotal = poids_attaque + poids_special + poids_defense;

        // Génération d'un nombre aléatoire pondéré
        double randomValue = Math.random() * poidsTotal;

        // Sélection de l'action en fonction du poids
        if (randomValue < poids_attaque) {
            return 1;
        } else if (randomValue < poids_attaque + poids_special) {
            return 2;
        } else {
            return 3;
        }
    }

    public void jeterEquipement(int idx_equipement) {
        heros.jeterEquipement(idx_equipement);
    }

    public void rangerEquipement(int idx_equipement, int idx_inventaire) {
        if (idx_inventaire == -1) {
            heros.rangerEquipement(idx_equipement, heros.getPremierInventaireLibre());
        } else {
            heros.rangerEquipement(idx_equipement, idx_inventaire);
        }
    }

    public void equiperObjet(int idx_inventaire, int idx_equipement) {
        Arme objet = heros.getEquipements().get(idx_equipement);
        heros.echangerEquipement(idx_inventaire, idx_equipement);
    }

    public void utiliserObjet(int idx_objet) {
        heros.utiliserObjet(idx_objet);
    }

    public boolean isEquipementLibre() {
        return (EQUIPEMENTS_MAX - heros.getNbEquipementLibre() > 0);
    }

    public List<String> getChoixHeros() {
        ArrayList<String> list_choix = new ArrayList<>();
        for (int i = 0; i < Heros.values().length; i++) {
            Heros heros = Heros.values()[i];
            list_choix.add(heros.getNom() + " - VIE : " + heros.getPvMax()
                    + " | ATT : " + heros.getAttaque()
                    + " | DEF : " + heros.getDefense());
        }
        return list_choix;
    }

    public List<Integer> getRange(int min, int max) {
        List<Integer> list = new ArrayList<>();
        for (int i = min; i <= max; i++)
            list.add(i);
        return list;
    }

    public boolean isLevelUp() {
        return is_level_up;
    }

    public Objet getLastRecompenseObjetCombat() {
        return last_recompense_objet_combat;
    }

    public int getLastRecompenseXpCombat() {
        return last_recompense_xp_combat;
    }

    public boolean isGameOver() {
        return game_over;
    }

    public Personnage getHeros() {
        return heros;
    }

    public Personnage getAdversaireActuel() {
        return adversaire_actuel;
    }

    public ArrayList<String> getHistorique() {
        return historique;
    }

    public int getTour() {
        return tour;
    }

    public int getNumCombat() {
        return num_combat;
    }

}
