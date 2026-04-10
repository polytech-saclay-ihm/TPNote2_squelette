package application;

import application.personnage.Heros;
import application.personnage.Monstre;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class Controller {

    private Model model;
    private View view;
    private int current_heros_index;
    private Image[] heros_images;
    private Image[] adversaire_images;

    private int last_index_inventaire_selected;
    private int last_index_equipement_selected;

    Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        // Chargement des images pour les stocker en mémoire

        // On récupère les images des heros en parcourant les élément de l'enum Heros
        heros_images = new Image[Heros.values().length];
        for (int i = 0; i < Heros.values().length; i++) {
            heros_images[i] = new Image(Heros.values()[i].getImageFile());
        }
        // Même chose pour les monstres
        adversaire_images = new Image[Monstre.values().length];
        for (int i = 0; i < Monstre.values().length; i++) {
            adversaire_images[i] = new Image(Monstre.values()[i].getImageFile());
        }
        current_heros_index = 0;
        last_index_equipement_selected = -1;
        last_index_inventaire_selected = -1;
        selectHeros(current_heros_index);


    }

    private void selectHeros(int index) {
        view.image_heros.setImage(heros_images[index]);
        //// A COMPLETER
        //// PARTIE 1 - Question 2

    }

    private void updateStats() {
        //// A COMPLETER
        //// PARTIE 2 - Question 2
    }

    public void afficherObjets() {
        //// A COMPLETER
        //// PARTIE 2 - Question 4
    }

    //// A COMPLETER
    /// Partie 3
    public void updateCombatUI(int nb_actions) {
        view.label_round.setText("Round " + model.getTour());
        view.barre_vie_heros_combat.setProgress((double) model.getHeros().getVie() / model.getHeros().getVieMax());
        view.barre_vie_adversaire_combat
                .setProgress((double) model.getAdversaireActuel().getVie() / model.getAdversaireActuel().getVieMax());

        //// A COMPLETER
        /// Partie 3 - Question 2
        // Gestion Historique

        if (!model.isFinCombat()) {
            if (model.getActionsPossibles().size() == 2) {
                view.bouton_attaque.setDisable(true);
                view.bouton_special.setText("Passer");
                view.bouton_defense.setDisable(true);
            } else {
                view.bouton_attaque.setDisable(false);
                view.bouton_special.setText("Special");
                view.bouton_defense.setDisable(false);
            }
        } else {
            //// A COMPLETER
            /// Partie 3 - Question 3
            // Remplacer la ligne ci-dessous par l'affichage de la
            // fenêtre de dialogue modale (Alert)
            afficherMenuPreparation();
        }
    }

    /* ===MENU PRINCIPAL====================================================== */
    public void afficherMenuPrincipal() {
        view.menu_principal.setVisible(true);
        view.menu_preparation.setVisible(false);
        view.menu_combat.setVisible(false);

    }

    public void setCurrentHerosNext() {
        current_heros_index = (current_heros_index + 1 < Heros.values().length) ? current_heros_index + 1 : 0;
        selectHeros(current_heros_index);
    }

    public void setCurrentHerosPrevious() {
        current_heros_index = (current_heros_index - 1 >= 0) ? current_heros_index - 1 : Heros.values().length - 1;
        selectHeros(current_heros_index);
    }

    public void commencerPartie() {
        model.initPartie(view.textfield_pseudo.getText(), Heros.values()[current_heros_index].getNom());
        view.label_pseudo.setText(model.getHeros().getPseudo());
        view.image_heros_stats.setImage(heros_images[current_heros_index]);
        afficherMenuPreparation();
    }

    /* ===MENU PREPARATION====================================================== */
    public void afficherMenuPreparation() {
        view.menu_principal.setVisible(false);
        view.menu_preparation.setVisible(true);
        view.menu_combat.setVisible(false);
        view.label_niveau.setText("Niveau " + model.getHeros().getNiveau());
        view.barre_xp.setProgress((double) model.getHeros().getXp() / model.getHeros().getXpLevelUp());
        view.barre_vie_restant.setProgress((double) model.getHeros().getVie() / model.getHeros().getVieMax());
        updateStats();
        afficherObjets();

    }

    private void setVboxActions(String[] actions, String type, int index) {
        for (int i = 0; i < view.vbox_actions.getChildren().toArray().length; i++) {
            Button b = (Button) view.vbox_actions.getChildren().get(i);
            b.setVisible(true);
            b.setText(actions[i]);
            setButtonAction(actions[i], type, b, index);
        }
        view.vbox_actions.setVisible(true);
    }

    private void setButtonAction(String action, String type, Button b, int index) {
        if (type.contentEquals("inventaire") && action.contentEquals(("Equiper"))
                && model.getHeros().getNbEquipementLibre() == 0) {
            b.setDisable(true);
        } else {
            b.setDisable(false);
        }

        b.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (type.contentEquals("equipement")) {
                    if (action.contentEquals(("Ranger"))) {
                        model.rangerEquipement(index, -1);
                    } else {
                        model.jeterEquipement(index);
                    }
                } else {
                    if (action.contentEquals(("Equiper"))) {
                        model.equiperObjet(index, model.getHeros().getPremierEquipementLibre());
                    } else if (action.contentEquals("Utiliser")) {
                        model.utiliserObjet(index);
                    } else {
                        model.getHeros().jeterObjet(index);
                    }
                }
                view.vbox_actions.setVisible(false);
                clearBorderObjets();
                updateStats();
                afficherObjets();
            }
        });

    }

    public void clearBorderObjets() {
        if (last_index_equipement_selected != -1) {
            view.grid_equipement.getChildren().get(last_index_equipement_selected)
                    .setStyle("-fx-border-color: black; -fx-border-width: 1;");
        }
        if (last_index_inventaire_selected != -1) {
            view.grid_inventaire.getChildren().get(last_index_inventaire_selected)
                    .setStyle("-fx-border-color: black; -fx-border-width: 1;");
        }
    }

    /*
     * ===MENU COMBAT=============================================================
     */

    public void commencerCombat() {
        model.initCombat();

        view.menu_principal.setVisible(false);
        view.menu_preparation.setVisible(false);
        view.menu_combat.setVisible(true);

        view.label_combat.setText("Combat " + model.getNumCombat());
        view.label_round.setText("Round " + model.getTour());

        view.label_heros_combat.setText(model.getHeros().getPseudo());
        view.image_heros_combat.setImage(heros_images[current_heros_index]);

        view.label_adversaire_combat.setText(model.getAdversaireActuel().getPseudo());
        view.image_adversaire_combat
                .setImage(adversaire_images[Monstre.getIndexFromName(model.getAdversaireActuel().getNom())]);

        updateCombatUI(-1);
    }

    public void executerActionCombat(int action) {
        // 1 : attaquer, 2 : spécial, 3 : défense
        int nb_actions = 0;
        switch (action) {
            case 1:
                nb_actions = model.executerTour(1);
                break;
            case 2:
                nb_actions = model.executerTour(2);
                break;
            case 3:
                nb_actions = model.executerTour(3);
                break;
        }
        updateCombatUI(nb_actions);
    }

    // Décommenter la partie suivante lorsque vous avez ajouté les éléments
    // grid_equipement et grid_inventaire depuis SceneBuilder
    // public void utiliserEquipement(int index){
    // String[] actions = {"Ranger","Jeter"};
    // if(model.getHeros().getEquipements().get(index).isVide()){
    // view.vbox_actions.setVisible(false);
    // }else {
    // setVboxActions(actions, "equipement",index);
    // }
    // clearBorderObjets();
    // if(! model.getHeros().getEquipements().get(index).isVide()) {
    // view.grid_equipement.getChildren().get(index).setStyle("-fx-border-color:
    // red;-fx-border-width: 3;");
    // }
    // last_index_equipement_selected = index;
    // }
    // public void utiliserObjetMenu(int index){
    // String action_objet =
    // (model.getHeros().getInventaire().get(index).getClass()== Arme.class) ?
    // "Equiper" : "Utiliser";
    // String[] actions = {action_objet,"Jeter"};
    // if(model.getHeros().getInventaire().get(index).isVide()){
    // view.vbox_actions.setVisible(false);
    // }else {
    // setVboxActions(actions, "inventaire",index);
    // }
    // clearBorderObjets();
    // if(! model.getHeros().getInventaire().get(index).isVide()) {
    // view.grid_inventaire.getChildren().get(index).setStyle("-fx-border-color:
    // red;-fx-border-width: 3;");
    // }
    // last_index_inventaire_selected = index;
    // }
    //

}
