package fr.ufc.l3info.oprog;

import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Tests unitaires pour la classe Article.
 */
public class ArticleTest {

    Article art1, art2, art3, art4, art5, art6, art7;

    @Before
    public void setUp()
    {
      art1 = new Article(3474377910731l, 1.1, "Marker pour tableau blanc Maxiflo");
      art2 = new Article(9789059603981l, 2.0, "Cahier Vert Gd Carreaux");
      art3 = new Article(9789059603981l, 2.1, "Cahier Bleu Gd Carreaux");
      art4 = new Article(9789059603986l, 2.1, "Cahier KiMarchPa");
      art5 = new Article(8255468219910l, 1000, "Coca");
      art6 = new Article(8255468219911l, 1000, null);
      art7 = new Article(99999999999999l, 1, null);
    }

    /**
     * Test du bon stockage des informations dans l'objet.
     */
    @Test
    public void testArticle0()
    {
      assertEquals(3474377910731l, art1.getCodeEAN13());
      assertTrue(art1.getPrixUnitaire() == 1.1);
      assertEquals("Marker pour tableau blanc Maxiflo", art1.getNom());
    }
    @Test
    public void testArticleEquals()
    {

        assertTrue(art2.equals(art3));
        assertTrue(art2.equals(art2));
    }
    @Test
    public void testArticleInEquals()
    {
    	assertTrue(!art1.equals(art2));
        assertTrue(!art1.equals(null));
    }
    @Test
    public void testArticleValid()
    {
    	assertTrue(art2.isValidEAN13());
        assertTrue(art5.isValidEAN13());
    }
    @Test
    public void testArticleInValid()
    {
    	assertTrue(!art4.isValidEAN13());
        assertTrue(!art6.isValidEAN13());
        assertTrue(!art7.isValidEAN13());
    }
}
