package application.objet;

import application.personnage.Personnage;

abstract public interface Objet {

    public String getNom();

    public String getStats();

    public void appliquerEffet(Personnage cible);

    public int getPoids();

    boolean isVide();
}
