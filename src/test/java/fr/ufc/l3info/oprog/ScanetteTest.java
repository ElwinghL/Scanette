package fr.ufc.l3info.oprog;

//Mockito
//JUnit
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ScanetteTest {
    Scanette ExempleBip, ExempleBipRelecture, ExempleBipScanne, ExempleBipScanneInconnu, ExempleBipbloquee, BipnoDB,BipfakeDB,Bip;
    Article Tropicana, Trapicono, Cookie;
    Caisse c;

    String path1,path2,path3;

    @Before
    public void setUp() throws ProductDBFailureException {

        path1 = "src/main/resources/produits.csv";
        path2 = "src/main/resources/produits2.csv";
        path3 = "src/main/resources/produits3.csv"; //pas là

        //Scanettes
        ExempleBip = new Scanette(path1);
        ExempleBipRelecture = new Scanette(path1);
        ExempleBipScanne = new Scanette(path1);
        ExempleBipScanneInconnu = new Scanette(path1);
        ExempleBipbloquee = new Scanette(path1);
        //Articles
        Tropicana = new Article(5410188006711L,2.15,"Tropicana Tonic Breakfast");
        Trapicono = new Article(9999999999999L,9999,"Trapicono Tonic Breakslow");
        Cookie = new Article(3560070048786L,0.87,"Cookies choco");
        //Déblocage
        ExempleBipRelecture.debloquer();
        ExempleBipScanne.debloquer();
        //Blocage
        ExempleBipbloquee.abandon();
        ExempleBipRelecture.scanner(Tropicana.getCodeEAN13()); // Ajout d'un article
        ExempleBipScanne.scanner(Cookie.getCodeEAN13());

        ExempleBipRelecture.relecture = true; // Passage en relecture
    }

    @Test(expected = ProductDBFailureException.class)
    public void Scanette_no_file() throws ProductDBFailureException {
        BipnoDB = new Scanette(path3);
    }

    @Test(expected = ProductDBFailureException.class)
    public void Scanette_fake_file() throws ProductDBFailureException {
        BipfakeDB = new Scanette(path2);
    }

    @Test
    public void Scanette() throws ProductDBFailureException {
        Bip = new Scanette(path1);
        assertTrue(ExempleBip.DB.DataBase.containsAll(ExempleBip.DB.DataBase));
    }

    @Test
    public void debloquer_bloquee() {
        assertEquals(ExempleBipbloquee.debloquer(), 0);
    }
    @Test
    public void debloquer_debloquee() {
        ExempleBip.debloquer();
        assertEquals(ExempleBip.debloquer(), -1);
    }

    @Test
    public void scanner_bloquee() {
        ExempleBipbloquee.abandon();
        assertEquals(ExempleBipbloquee.scanner(Tropicana.getCodeEAN13()), -1);
    }
    @Test
    public void scanner_fake_EAN() { assertEquals(ExempleBipRelecture.scanner(Trapicono.getCodeEAN13()), -2); }
    @Test
    public void scanner_pas_panier() {
        ExempleBipRelecture.debloquer();
        assertEquals(ExempleBipRelecture.scanner(Cookie.getCodeEAN13()), -3);
    }
    @Test
    public void scanner() { assertEquals(ExempleBipScanne.scanner(Cookie.getCodeEAN13()), 0); }
    @Test
    public void supprimer_true() {
        ExempleBipScanne.scanner(Cookie.getCodeEAN13());
        assertEquals(ExempleBipScanne.supprimer(Cookie.getCodeEAN13()),0);
    }
    @Test
    public void supprimer_false() {
        assertEquals(ExempleBipScanne.supprimer(Tropicana.getCodeEAN13()),-2); // Pas dans le panier
        assertEquals(ExempleBipScanne.supprimer(Trapicono.getCodeEAN13()),-2); // Pas correct
    }
    @Test
    public void supprimer_bloquee() {
        assertEquals(ExempleBipbloquee.supprimer(Tropicana.getCodeEAN13()),-1); // Bloquée donc panier "vide"
    }

    @Test
    public void quantite() {
        ExempleBipScanne.scanner(Cookie.getCodeEAN13());
        assertEquals(ExempleBipbloquee.quantite(Cookie.getCodeEAN13()), 0);
        assertEquals(ExempleBipScanne.quantite(Cookie.getCodeEAN13()), 2);
    }

    @Test
    public void abandon() {
        ExempleBipbloquee.abandon(); //Bloque Au cas où
        ExempleBipbloquee.debloquer(); //Débloque
        ExempleBipbloquee.abandon(); //Rebloque
        assertEquals(ExempleBipbloquee.debloquer(), 0);

    }

    @Test
    public void getReferencesInconnues() {
        ExempleBipScanneInconnu.debloquer(); //Au cas où
        ExempleBipScanneInconnu.scanner(222222L);
        Set<Long> test222222L = new HashSet<Long>();
        Set<Long> empty = new HashSet<Long>();
        test222222L.add(222222L);
        assertEquals(test222222L,ExempleBipScanneInconnu.getReferencesInconnues());
        ExempleBipScanneInconnu.abandon();
        assertEquals(empty,ExempleBipScanneInconnu.getReferencesInconnues());
    }

    @Test
    public void getArticles() throws ArticleNotFoundException {
        Set<Article> empty = new HashSet<Article>();
        ExempleBipbloquee.abandon();
        assertEquals(empty,ExempleBipbloquee.getArticles());
    }

    @Test
    public void transmission() {
    }
}