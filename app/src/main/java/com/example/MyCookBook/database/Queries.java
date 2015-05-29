package com.example.MyCookBook.database;

/**
 * Created by Sonia on 2014-11-24.
 */


public class Queries {
    public Queries() {
        super();
    }

    // product
    public String getAllProducts() {
        return "SELECT prod._ID, prod.Nazwa, kcal, " +
                "prod.TypProduktu, typ._ID, typ.TypProduktu " +
                "FROM PRODUKTY prod, TYP_PRODUKTU typ " +
                "WHERE prod.TypProduktu = typ.TypProduktu";
    }

    public String getVegetable() {
        return "SELECT prod._ID, prod.Nazwa, kcal, " +
                "prod.TypProduktu, typ._ID, typ.TypProduktu " +
                "FROM PRODUKTY prod, TYP_PRODUKTU typ " +
                "WHERE prod.TypProduktu = typ.TypProduktu " +
                "AND prod.TypProduktu = 'Warzywo' " +
                "ORDER BY prod.Nazwa";
    }

    public String getFruit() {
        return "SELECT prod._ID, prod.Nazwa, kcal, prod.TypProduktu, typ._ID, typ.TypProduktu " +
                "FROM PRODUKTY prod, TYP_PRODUKTU typ WHERE prod.TypProduktu = typ.TypProduktu AND prod.TypProduktu = 'Owoc' ORDER BY prod.Nazwa";
    }

    public String getCereal() {
        return "SELECT prod._ID, prod.Nazwa, kcal, prod.TypProduktu, typ._ID, typ.TypProduktu " +
                "FROM PRODUKTY prod, TYP_PRODUKTU typ WHERE prod.TypProduktu = typ.TypProduktu AND prod.TypProduktu = 'Produkt Zbożowy' ORDER BY prod.Nazwa";
    }

    public String getDairy() {
        return "SELECT prod._ID, prod.Nazwa, kcal, prod.TypProduktu, typ._ID, typ.TypProduktu " +
                "FROM PRODUKTY prod, TYP_PRODUKTU typ WHERE prod.TypProduktu = typ.TypProduktu AND prod.TypProduktu = 'Nabiał' ORDER BY prod.Nazwa";
    }

    public String getMeat() {
        return "SELECT prod._ID, prod.Nazwa, kcal, prod.TypProduktu, typ._ID, typ.TypProduktu " +
                "FROM PRODUKTY prod, TYP_PRODUKTU typ WHERE prod.TypProduktu = typ.TypProduktu AND prod.TypProduktu = 'Mięso' ORDER BY prod.Nazwa";
    }

    public String getSweets() {
        return "SELECT prod._ID, prod.Nazwa, kcal, prod.TypProduktu, typ._ID, typ.TypProduktu " +
                "FROM PRODUKTY prod, TYP_PRODUKTU typ WHERE prod.TypProduktu = typ.TypProduktu AND prod.TypProduktu = 'Słodycze' ORDER BY prod.Nazwa";
    }

    public String getOthers() {
        return "SELECT prod._ID, prod.Nazwa, kcal, prod.TypProduktu, typ._ID, typ.TypProduktu " +
                "FROM PRODUKTY prod, TYP_PRODUKTU typ WHERE prod.TypProduktu = typ.TypProduktu AND prod.TypProduktu = 'Inne' ORDER BY prod.Nazwa";
    }

    // recipe
    public String getDessert() {
        return "SELECT przep._ID,  przep.Nazwa, " +
                "przep.Tresc, przep.TypPrzepisu, " +
                "przep.Ulubione, typ._ID, typ.TypPrzepisu " +
                "FROM PRZEPIS przep, TYP_PRZEPISU typ " +
                "WHERE przep.TypPrzepisu = typ.TypPrzepisu " +
                "AND przep.TypPrzepisu = 'Deser' " +
                "ORDER BY przep.Nazwa";
    }

    public String getMainCourse() {
        return "SELECT przep._ID,  przep.Nazwa, przep.Tresc, przep.TypPrzepisu,  przep.Ulubione, typ._ID, typ.TypPrzepisu " +
                "FROM PRZEPIS przep, TYP_PRZEPISU typ WHERE przep.TypPrzepisu = typ.TypPrzepisu AND przep.TypPrzepisu = 'Danie Główne' ORDER BY przep.Nazwa";
    }

    public String getSalad() {
        return "SELECT przep._ID,  przep.Nazwa, przep.Tresc, przep.TypPrzepisu,  przep.Ulubione, typ._ID, typ.TypPrzepisu " +
                "FROM PRZEPIS przep, TYP_PRZEPISU typ WHERE przep.TypPrzepisu = typ.TypPrzepisu AND przep.TypPrzepisu = 'Sałatka' ORDER BY przep.Nazwa";
    }

    public String getSoup() {
        return "SELECT przep._ID,  przep.Nazwa, przep.Tresc, przep.TypPrzepisu,  przep.Ulubione, typ._ID, typ.TypPrzepisu " +
                "FROM PRZEPIS przep, TYP_PRZEPISU typ WHERE przep.TypPrzepisu = typ.TypPrzepisu AND przep.TypPrzepisu = 'Zupa' ORDER BY przep.Nazwa";
    }

    public String getStarter() {
        return "SELECT przep._ID,  przep.Nazwa, przep.Tresc, przep.TypPrzepisu,  przep.Ulubione, typ._ID, typ.TypPrzepisu " +
                "FROM PRZEPIS przep, TYP_PRZEPISU typ WHERE przep.TypPrzepisu = typ.TypPrzepisu AND przep.TypPrzepisu = 'Przystawka' ORDER BY przep.Nazwa";
    }

    public String getFavourites() {
        return "SELECT przep._ID,  przep.Nazwa, przep.Tresc, przep.TypPrzepisu,  przep.Ulubione, typ._ID, typ.TypPrzepisu " +
                "FROM PRZEPIS przep, TYP_PRZEPISU typ WHERE przep.TypPrzepisu = typ.TypPrzepisu AND Ulubione = 1 ORDER BY przep.Nazwa";
    }

    // ingredients
    public String getIngredients(String idPrzepisu) {
        return "SELECT skl._ID, skl.IDProduktu, skl.IDPrzepisu, skl.Ilosc, skl.Jednostka, jed._ID, jed.Jednostka, " +
                "przep._ID, prod._ID " +
                "FROM PRZEPIS przep LEFT OUTER JOIN SKLADNIKI skl ON przep._ID = skl.IDPrzepisu " +
                "INNER JOIN PRODUKTY prod ON prod._ID = skl.IDProduktu " +
                "INNER JOIN JEDNOSTKI jed ON skl.Jednostka = jed.Jednostka " +
                "WHERE skl.IDPrzepisu = " + idPrzepisu;
    }

    // calculator
    public String getCalcProducts() {
        return "SELECT * FROM KALKULATOR";
    }

    // searching

    public String searchRecipes(String radio, String spinner, String prod_1, String prod2, String prod3) {
        return "SELECT DISTINCT przep._ID, przep.Nazwa, " +
                "Tresc, przep.TypPrzepisu, Ulubione, " +
                "typ._ID, typ.TypPrzepisu " +
                "FROM PRZEPIS przep " +
                "LEFT OUTER JOIN SKLADNIKI skl " +
                            "ON przep._ID = skl.IDPrzepisu " +
                "INNER JOIN PRODUKTY prod " +
                            "ON prod._ID = skl.IDProduktu " +
                "INNER JOIN TYP_PRZEPISU typ " +
                            "ON przep.TypPrzepisu = typ.TypPrzepisu " +
                "INNER JOIN TYP_PRODUKTU typP " +
                            "ON prod.TypProduktu = typP.TypProduktu " +
                "WHERE przep.TypPrzepisu = '" + spinner +"' "
                + prod_1 + prod2 + prod3 +
                "AND " + radio;
    }

    public String getAllRecipes() {
        return "SELECT przep._ID,  przep.Nazwa, przep.Tresc, przep.TypPrzepisu,  przep.Ulubione, typ._ID, typ.TypPrzepisu " +
                "FROM PRZEPIS przep, TYP_PRZEPISU typ WHERE przep.TypPrzepisu = typ.TypPrzepisu";
    }

    public String rowsRecipes(){
        return "SELECT COUNT(*) FROM PRZEPIS;";
    }
    public String rowsIngredients(){
        return "SELECT COUNT(*) FROM SKLADNIKI;";
    }
}
