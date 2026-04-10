package application;

import application.objet.Arme;
import application.objet.Objet;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

public class View {

    /// MENU PRINCIPAL======================================================
    @FXML
    VBox menu_principal;
    @FXML
    BorderPane image_heros_display_pane;
    @FXML
    ImageView image_heros;
    // Partie 1 : listener + selectHeros
    // @FXML
    // Label label_heros;
    // @FXML
    // ProgressBar barre_vie;
    // @FXML
    // ProgressBar barre_attaque;
    // @FXML
    // ProgressBar barre_defense;
    @FXML
    HBox hbox_selection_heros;
    @FXML
    Button bouton_commencer;
    @FXML
    TextField textfield_pseudo;
    // PARTIE 1 : label_message erreur
    // @FXML
    // Label label_message_pseudo_vide;

    //// MENU PREPARATION======================================================
    @FXML
    BorderPane menu_preparation;
    @FXML
    Label label_pseudo;
    @FXML
    ImageView image_heros_stats;
    @FXML
    Label label_niveau;
    @FXML
    ProgressBar barre_xp;
    @FXML
    ProgressBar barre_vie_restant;
    // Partie 2 : stats heros
    // @FXML
    // ProgressBar barre_vie_stats;
    // @FXML
    // ProgressBar barre_attaque_stats;
    // @FXML
    // ProgressBar barre_defense_stats;
    @FXML
    GridPane grid_equipement;
    @FXML
    GridPane grid_inventaire;
    @FXML
    VBox vbox_actions;
    @FXML
    Button bouton_commencer_combat;
    @FXML
    Button bouton_abandonner_combat;

    //// Menu combat==============================================================
    @FXML
    BorderPane menu_combat;
    @FXML
    Label label_combat;
    @FXML
    Label label_round;
    // Partie 3 : affichage historique
    // @FXML
    // TextArea textarea_historique;
    @FXML
    Label label_heros_combat;
    @FXML
    ImageView image_heros_combat;
    @FXML
    ProgressBar barre_vie_heros_combat;
    @FXML
    Label label_adversaire_combat;
    @FXML
    ImageView image_adversaire_combat;
    @FXML
    ProgressBar barre_vie_adversaire_combat;
    @FXML
    Button bouton_attaque;
    @FXML
    Button bouton_special;
    @FXML
    Button bouton_defense;

    private Controller controller;

    @FXML
    private void initialize() {

        //// A COMPLETER
        //// PARTIE 1 - Question 3
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @FXML
    private void onNextClick() {
        controller.setCurrentHerosNext();
    }

    @FXML
    private void onPreviousClick() {
        controller.setCurrentHerosPrevious();
    }

    @FXML
    private void onQuitterClick() {
        Platform.exit();
    }

    public Label initLabelObject(Objet objet) {
        Label l = new Label(objet.getNom() + "\n" + objet.getStats());
        l.setTextAlignment(TextAlignment.CENTER);
        l.setStyle("-fx-border-color: black;");
        l.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        GridPane.setHalignment(l, HPos.CENTER);
        l.setAlignment(Pos.CENTER);

        return l;
    }

    @FXML
    private void onAbandonnerClick() {
        controller.afficherMenuPrincipal();
    }

    @FXML
    private void onCommencerClick() {
        controller.commencerPartie();
    }

    @FXML
    private void onCommencerCombatClick() {
        controller.commencerCombat();
    }

    // 1 : attaquer, 2 : spécial, 3 : défense
    @FXML
    public void onAttaqueClick() {
        controller.executerActionCombat(1);
    }

    @FXML
    public void onSpecialClick() {
        controller.executerActionCombat(2);
    }

    @FXML
    public void onDefenseClick() {
        controller.executerActionCombat(3);
    }

    // Décommenter la partie suivante lorsque vous avez ajouté les éléments
    // grid_equipement et grid_inventaire
    // et décommenté la partie correspondante dans le Controller

    // public void initLabelObjetEquipement(Objet objet, int index){
    // Label l = initLabelObject(objet);
    // l.setOnMouseClicked(new EventHandler<MouseEvent>() {
    // @Override
    // public void handle(MouseEvent event) {
    // controller.utiliserEquipement(index);
    // }
    // });
    // grid_equipement.add(l, index, 0);
    // }
    //
    // public void initLabelObjetInventaire(Objet objet, int index){
    // Label l = initLabelObject(objet);
    // l.setOnMouseClicked(new EventHandler<MouseEvent>() {
    // @Override
    // public void handle(MouseEvent event) {
    // controller.utiliserObjetMenu(index);
    // }
    // });
    // grid_inventaire.add(l, index, 0);
    // }

}
