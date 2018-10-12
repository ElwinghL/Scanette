package fr.ufc.l3info.oprog;

import java.io.FileNotFoundException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
class ProductDBFailureException extends Exception {

    ProductDBFailureException() {
        System.out.println("Cet article n'a pas été trouvé.");
    }
}
/**
 * Classe représentant la scanette.
 */
public class Scanette {
    private boolean bloquee = true;
    public boolean relecture = false;
    public ArticleDB DB = new ArticleDB();
    private Map<Long, Integer> Panier = new HashMap<Long, Integer>();
    private Set<Long> Inconnu = new HashSet<>();

    /**
     * Créé une scanette en l'initialisant avec la base de données d'articles dont
     * le chemin est donné en paramètre.
     *
     * @param pathToProductFile chemin vers le fichier de la base de données d'articles
     * @throws ProductDBFailureException lorsque la base de données n'a pas pu s'initialiser.
     */
    public Scanette(String pathToProductFile) throws ProductDBFailureException {
        try {
            DB.init(pathToProductFile);
        } catch (Exception e) {
            throw new ProductDBFailureException();
        }
    }

    /**
     * Permet de débloquer la scanette pour un client donné. On supposera que
     * l'on ne gère pas les identifiants des clients.
     *
     * @return 0 si la scanette a bien été débloquée,
     * -1 si celle-ci n'était pas bloquée.
     */
    public int debloquer() {
        if (!bloquee) {
            return -1;
        }
        else {
            bloquee = false;
            return 0;
        }
    }


    /**
     * Scanne un produit par l'intermédiaire de son code EAN13.
     * Cette méthode sert à la fois pour ajouter un produit au
     * panier du client et pour effectuer une relecture.
     *
     * @param ean13 le code EAN13 du produit scanné
     * @return 0 si le scan du produit s'est correctement déroulé.
     * -1 si la scanette n'était pas dans le bon état
     * -2 si le produit scanné a provoqué une erreur
     * (produit non reconnu ou produit inexistant dans le panier)
     */
    public int scanner(long ean13) {
        if (bloquee) {
            return -1;
        }
        Article article;
        try {
            article = DB.getArticle(ean13);
        } catch (ArticleNotFoundException e) {
            Inconnu.add(ean13);
            return -2;
        }
        if (article.isValidEAN13()) {
            if (relecture) {
                if (Panier.containsKey(ean13)) {
                    return 0;
                }
                return -3;
            } else {
                if (Panier.containsKey(ean13)) {
                    int number;
                    number = Panier.get(ean13);
                    Panier.put(ean13, number + 1);
                    return 0;
                }
                Panier.put(ean13, 1);
                return 0;
            }
        }
        return -2;
    }


    /**
     * Supprime du panier le produit dont le code EAN13 est donné en paramètre.
     *
     * @param ean13 le code EAN du produit à supprimer.
     * @return 0 si l'occurrence du produit a bien été supprimée,
     * -1 si la scanette n'était pas dans le bon état,
     * -2 si le produit n'existait pas dans le panier
     */
    public int supprimer(long ean13) {
        if (bloquee || relecture) {
            return -1;
        }
        if (Panier.containsKey(ean13)) {
            int number;
            number = Panier.get(ean13);
            if (number == 1) {
                Panier.remove(ean13);
                return 0;
            }
            Panier.put(ean13, number - 1);
            return 0;
        }
        return -2;
    }

    /**
     * Permet de connaître la quantité d'un produit dans le panier.
     *
     * @param ean13 le code EAN13 du produit dont on souhaite connaître la quantité.
     * @return le nombre d'occurrences du produit dans le panier.
     */
    public int quantite(long ean13) {
        if (Panier.containsKey(ean13)) {
            int number;
            number = Panier.get(ean13);
            return number;
        }
        else {
            return 0;
        }
    }


    /**
     * Permet d'abandonner toute transaction en cours et de re-bloquer la scanette.
     */
    public void abandon() {
        bloquee = true;
        Panier = new HashMap<Long, Integer>(); // Reset Panier
        Inconnu = new HashSet<Long>();
    }


    /**
     * Permet de consulter les codes EAN qui n'ont pas été reconnus lors des courses.
     *
     * @return Un ensemble de codes EAN13 non reconnus.
     */
    public Set<Long> getReferencesInconnues() { //l'ensemble vide est géré par abandon
        return Inconnu;
    }


    /**
     * Permet d'extraire les articles composant le panier du client.
     *
     * @return Un ensemble d'Article reconnus par la scanette.
     */
    public Set<Article> getArticles() throws ArticleNotFoundException { //l'ensemble vide est géré par abandon
        Set<Article> scan = new HashSet<Article>();
        Set<Long> ean = Panier.keySet();
        Iterator it = ean.iterator();
        while (it.hasNext()) {
            long ean13 = (long) it.next();
            Article article = DB.getArticle(ean13);
            scan.add(article);
        }
        return scan;
    }


    /**
     * Permet de transmettre les informations de la scanette à la caisse en se connectant
     * à celle-ci. En fonction de la réponse de la caisse, la scanette changera d'état.
     *
     * @param c La caisse avec laquelle la scanette interagit.
     * @return 0 si la scanette a terminé son travail (pas de relecture)
     * 1 si une relecture est demandée par la caisse
     * -1 en cas d'erreur
     */
    public int transmission(Caisse c) {
        if (relecture || bloquee) {
            return -1;
        }
        switch (c.connexion(this)) {
            case 0:  // pas de relecture -> job's done
                abandon();
                return 0;
            case 1: // relecture -> il reste du boulot
                relecture = true;
                return 1;
            case -1: // erreur du côté de la caisse
                abandon();
                return -1;
        }
        return 1;
    }
}
