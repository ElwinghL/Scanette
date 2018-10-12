package fr.ufc.l3info.oprog;

/*
 * Classe décrivant un article.
 *
 *   _______ _____  __    ____        _   _ _   _____
 *  |__   __|  __ \/_ |  / __ \      | | (_) | |  __ \
 *     | |  | |__) || | | |  | |_   _| |_ _| | | |__) | __ ___   __ _
 *     | |  |  ___/ | | | |  | | | | | __| | | |  ___/ '__/ _ \ / _` |
 *     | |  | |     | | | |__| | |_| | |_| | | | |   | | | (_) | (_| |
 *     |_|  |_|     |_|  \____/ \__,_|\__|_|_| |_|   |_|  \___/ \__, |
 *                                                               __/ |
 *                                                              |___/
 */
public class Article
{

  /** Code EAN 13 de l'article */
  long codeEAN13;

  /** Dénomination commerciale de l'article */
  String nom;

  /** Prix unitaire de l'article */
  double prixUnitaire;

  /** Constructeur
   * @param _ean13 le code EAN13 de l'article
   * @param _pu le prix unitaire de l'article
   * @param _nom le nom de l'article
   */
  public Article(long _ean13, double _pu, String _nom)
  {
    codeEAN13 = _ean13;
    prixUnitaire = _pu;
    nom = (_nom == null) ? "" : _nom;
  }

  /**
   * Accesseur pour le nom de l'article.
   * @return le nom de l'article
   */
  public String getNom()
  {
    return nom;
  }

  /**
   * Accesseur pour le code barre (EAN13) de l'article.
   * @return le code EAN13 de l'article
   */
  public long getCodeEAN13()
  {
    return codeEAN13;
  }

  /**
   * Accesseur pour le prix unitaire de l'article
   * @return la valeur du prix unitaire de l'article
   */
  public double getPrixUnitaire()
  {
    return prixUnitaire;
  }

  /**
   * Teste si deux articles sont égaux (i.e. s'ils ont le même code EAN13).
   * @param a l'article à comparer à l'objet courant.
   * @return true si les deux articles sont égaux, false sinon.
   */
  public boolean equals(Article a)
  {
      if (this == a)
      {
          return true;
      }
      if (a == null)
      {
          return false;
      }
      Article other = a;
      return codeEAN13 == other.codeEAN13;
  }
  /**
   * Vérifie si le code EAN13 est bien correct.
   * @return true si le code EAN13 est valide, false sinon.
   */
  public boolean isValidEAN13()
  {
  	//On utilise la méthode de la clé de contrôle comme suit : https://fr.wikipedia.org/wiki/EAN_13
  	String EAN = Long.toString(codeEAN13);
  	int ponderation[] = {1,3,1,3,1,3,1,3,1,3,1,3};
  	int somme = 0;

  	if (EAN.length() > 13)
    {
        return false;
    }

  	while (EAN.length() < 13)
    {
        EAN = "0" + EAN;
    }

  	for (int i = 0; i < 12; i++)
    {
  		somme += ponderation[i] * Character.getNumericValue(EAN.charAt(i));
  	}
  	if (somme%10 == 0)
    {
  		return ((somme%10) == Character.getNumericValue(EAN.charAt(12)));
  	}
  	else
    {
      return ((10-somme%10) == Character.getNumericValue(EAN.charAt(12)));
  	}
  }
}
