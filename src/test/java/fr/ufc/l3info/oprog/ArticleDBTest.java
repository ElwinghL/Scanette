package fr.ufc.l3info.oprog;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ArticleDBTest
{
    ArticleDB DB1, DB2, DB3;
    Article art1, art2;

    @Before
    public void setUp() throws IOException, FileFormatException
    {
        art1 = new Article(5410188006711l,2.15,"Tropicana Tonic Breakfast");
        DB1 = new ArticleDB();
        DB2 = new ArticleDB();
        DB3 = new ArticleDB();

        String path1 = "src/main/resources/produits.csv";
        DB2.init(path1);
    }
    @Test
    public void testArticleDB() throws ArticleNotFoundException
    {
        assertTrue(DB1.getTailleDB() == 0);
        assertTrue(DB2.getArticle(5410188006711l).equals(art1));
    }
    @Test(expected = ArticleNotFoundException.class)
    public void emptyDB() throws ArticleNotFoundException
    {
        art2 = DB1.getArticle(5410188006711l);
    }
    @Test(expected = IOException.class)
    public void nofile() throws FileFormatException, IOException
    {
        String path2 = "src/main/resources/produitspala.csv"; //N'existe pas.
        DB2.init(path2);
    }
    @Test(expected = ArticleNotFoundException.class)
    public void articleError() throws ArticleNotFoundException
    {
        String path2 = "src/main/resources/produitspala.csv"; //N'existe pas.
        assertTrue(DB2.getArticle(9999999999999l).equals(art1));
    }
    @Test(expected = FileFormatException.class)
    public void fileformatError() throws FileFormatException, IOException
    {
        String path3 = "src/main/resources/produits2.csv";
        DB3.init(path3);
    }
}
