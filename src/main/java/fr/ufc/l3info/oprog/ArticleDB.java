package fr.ufc.l3info.oprog;
/*
 * Classe décrivant la base de données des articles référencés.
 *
 *   _______ _____ ___     ____        _   _ _   _____
 *  |__   __|  __ \__ \   / __ \      | | (_) | |  __ \
 *     | |  | |__) | ) | | |  | |_   _| |_ _| | | |__) | __ ___   __ _
 *     | |  |  ___/ / /  | |  | | | | | __| | | |  ___/ '__/ _ \ / _` |
 *     | |  | |    / /_  | |__| | |_| | |_| | | | |   | | | (_) | (_| |
 *     |_|  |_|   |____|  \____/ \__,_|\__|_|_| |_|   |_|  \___/ \__, |
 *                                                                __/ |
 *                                                               |___/
 */


//import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashSet;
import java.util.Set;

class FileFormatException extends Exception
{
    FileFormatException()
    {
        System.out.println("Votre fichier est invalide.");
    }
}
class ArticleNotFoundException extends Exception
{
    ArticleNotFoundException()
    {
        System.out.println("Cet article n'existe pas.");
    }
}
public class ArticleDB
{
    Set<Article> DataBase;

    public ArticleDB()
    {
        DataBase = new HashSet<Article>();
    }
    public void init(String fileCSV) throws IOException, FileFormatException
    {
        String line = "";

        BufferedReader br = new BufferedReader(new FileReader(fileCSV));

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] data = line.split(",");

                if (data.length == 3)
                {
                    long EAN13 = Long.parseLong(data[0]);
                    double pu = Double.parseDouble(data[1]);
                    String nom = data[2];

                    Article art = new Article(EAN13, pu, nom);
                    if (art.isValidEAN13() && (art.nom != null) && (pu > 0))
                    {
                        DataBase.add(art);
                    }
                    else
                    {
                        DataBase.clear();
                        throw new FileFormatException();
                    }
                }

                else
                {
                    DataBase.clear();
                    throw new FileFormatException();
                }
            }
    }

    public int getTailleDB()
    {
        return DataBase.size();
    }

    public Article getArticle(long EAN) throws ArticleNotFoundException
    {
        if (DataBase.isEmpty())
        {
            throw new ArticleNotFoundException();
        }
        else
        {

            for (Article art : DataBase)
            {
                if (EAN == art.codeEAN13)
                {
                    return art;
                }
            }
            //Si pas de retour en fin de boucle
            throw new ArticleNotFoundException();
        }
    }
}
