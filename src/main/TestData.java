package main;

import java.io.IOException;
import java.util.*;

import dataProvajderi.DataProvider;
import dataProvajderi.KlijentProvider;
import dataProvajderi.KozmetickiTretmanProvider;
import dataProvajderi.MenadzerProvider;
import dataProvajderi.RecepcionerProvider;
import dataProvajderi.TipTretmanaProvider;
import entiteti.Klijent;
import entiteti.KozmetickiTretman;
import entiteti.NivoStrucneSpreme;
import entiteti.Pol;
import entiteti.Recepcioner;
import helpers.Settings;

public class TestData {

	private static final String[] names = {
	        "Milan", "Ana", "Luka", "Jelena", "Stefan", "Mina", "Nikola", "Sara", "Marko", "Ivana",
	        "Vuk", "Maja", "Nemanja", "Jovana", "Filip", "Jasmina", "Petar", "Milica", "Aleksandar", "Kristina"
	};

	private static final String[] surnames = {
	        "Jovanovic", "Petrovic", "Nikolic", "Milosevic", "Markovic", "Savic", "Kovacevic", "Stojanovic", "Radic", "Simic",
	        "Kovacevic", "Andric", "Djordjevic", "Todorovic", "Ilic", "Pavlovic", "Ristic", "Milinkovic", "Dimitrijevic", "Milanovic"
	};


    private static final String[] passwords = {
            "pass123", "pass456", "pass789", "pass321", "pass654", "pass987", "pass234", "pass567", "pass890", "pass123"
    };

    private static final String[] addresses = {
            "123 Main St", "456 Elm St", "789 Oak Ave", "321 Pine Rd", "654 Maple Dr", "987 Birch Ln", "234 Cedar Rd", "567 Willow Ave", "890 Spruce St", "123 Cherry Dr"
    };

    private static final String[] phoneNumbers = {
            "1234567890", "2345678901", "3456789012", "4567890123", "5678901234", "6789012345", "7890123456", "8901234567", "9012345678", "0123456789"
    };

    private static final Pol[] genders = {Pol.MUSKI, Pol.ZENSKI};

    private static final NivoStrucneSpreme[] nss = {
            NivoStrucneSpreme.SKOLE_BEZ, NivoStrucneSpreme.SREDNJA_SKOLA,
            NivoStrucneSpreme.OSNOVNE_STUDIJE, NivoStrucneSpreme.MASTER_STUDIJE,
            NivoStrucneSpreme.DOKTORSKE_STUDIJE
    };

    private static final Double[] bazePlate = {600d, 500d, 800d, 100d, 542d, 622d};

    private static final Integer[] godineStaza = {1, 2, 3, 4, 5, 6, 7, 8, 12, 16, 20};

    private static final int numKlijent = 24;

    private static <T> T getRandom(T[] array) {
        int randomIndex = new Random().nextInt(array.length);
        return array[randomIndex];
    }

    private static boolean getRandom() {
        return new Random().nextBoolean();
    }

    private static String generateUniqueUsername(Set<String> existingUsernames, String firstName, String surname) {
        String baseUsername = (firstName + surname).toLowerCase();
        String username = baseUsername;
        int counter = 1;

        while (existingUsernames.contains(username)) {
            username = baseUsername + counter;
            counter++;
        }

        existingUsernames.add(username);
        return username;
    }

    private static List<Klijent> generateKlijent(Set<String> existingUsernames) {
        List<Klijent> klijenti = new ArrayList<>();

        for (int i = 0; i < numKlijent; i++) {
            String firstName = getRandom(names);
            String surname = getRandom(surnames);
            String username = generateUniqueUsername(existingUsernames, firstName, surname);

            klijenti.add(
                    new Klijent(
                            firstName,
                            surname,
                            getRandom(phoneNumbers),
                            getRandom(addresses),
                            username,
                            getRandom(passwords),
                            getRandom(genders),
                            getRandom()
                    )
            );
        }

        return klijenti;
    }

    private static List<String[]> generateRecepcioner(Set<String> existingUsernames) {
        List<String[]> recepcioneri = new ArrayList<>();

        for (int i = 0; i < numKlijent; i++) {
        	String name = getRandom(names);
        	String surname = getRandom(surnames);
            recepcioneri.add(
                    RecepcionerProvider.TO_CSV.convert(new Recepcioner(
                            name,
                            surname,
                            getRandom(phoneNumbers),
                            getRandom(addresses),
                            generateUniqueUsername(existingUsernames, name, surname),
                            getRandom(passwords),
                            getRandom(genders),
                            getRandom(godineStaza),
                            getRandom(bazePlate),
                            getRandom(),
                            getRandom(nss)
                    ))
            );
        }

        return recepcioneri;
    }

    private static List<String[]> generateMenadzer(Set<String> existingUsernames) {
        List<String[]> menadzeri = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
        	String name = getRandom(names);
        	String surname = getRandom(surnames);
            menadzeri.add(
                    MenadzerProvider.TO_CSV.convert(new entiteti.Menadzer(
                            name,
                            surname,
                            getRandom(phoneNumbers),
                            getRandom(addresses),
                            generateUniqueUsername(existingUsernames, name, surname),
                            getRandom(passwords),
                            getRandom(genders),
                            getRandom(godineStaza),
                            getRandom(bazePlate),
                            getRandom(),
                            getRandom(nss)
                    ))
            );
        }

        return menadzeri;
    }
    
    private static List<KozmetickiTretman> getAndGenerateAllTretmani(){
    	
    	List<KozmetickiTretman> tretmani = new ArrayList<>();
        List<KozmetickiTretman.TipTretmana> tipovi = new ArrayList<>();
        
        // Tretman 1
        KozmetickiTretman tretman1 = new KozmetickiTretman("Tretman lica", "Ovaj tretman cisti i osvjezava kozu lica.");
        tipovi.add(tretman1.newTipTretmana("Osnovni tretman lica", 50.0f, 60));
        tipovi.add(tretman1.newTipTretmana("Tretman sa maskom", 75.0f, 75));
        tretmani.add(tretman1);
        
        
        // Tretman 2
        KozmetickiTretman tretman2 = new KozmetickiTretman("Masaža leđa", "Relaksirajuća masaža leđa za opuštanje mišića.");
        tipovi.add(tretman2.newTipTretmana("Masaža sa aromaterapijom", 65.0f, 45));
        tipovi.add(tretman2.newTipTretmana("Dubinska masaža", 90.0f, 60));
        tretmani.add(tretman2);
        
        // Tretman 3
        KozmetickiTretman tretman3 = new KozmetickiTretman("Manikir", "Nega ruku i noktiju uz oblikovanje i lakiranje.");
        tipovi.add(tretman3.newTipTretmana("Klasični manikir", 25.0f, 30));
        tipovi.add(tretman3.newTipTretmana("Shellac manikir", 40.0f, 45));
        tretmani.add(tretman3);
        
        // Tretman 4
        KozmetickiTretman tretman4 = new KozmetickiTretman("Depilacija", "Uklanjanje neželjenih dlačica voskom.");
        tretman4.newTipTretmana("Depilacija nogu", 40.0f, 60);
        tretman4.newTipTretmana("Depilacija bikini zone", 35.0f, 45);
        tretmani.add(tretman4);
        
     // Tretman 5
        KozmetickiTretman tretman5 = new KozmetickiTretman("Tretman noktiju", "Nega i oblikovanje noktiju uz dodatke kao što su gel ili akril.");
        tipovi.add(tretman5.newTipTretmana("Manikir sa gel lakom", 50.0f, 60));
        tipovi.add(tretman5.newTipTretmana("Produžavanje noktiju gelom", 70.0f, 90));
        tretmani.add(tretman5);

        // Tretman 6
        KozmetickiTretman tretman6 = new KozmetickiTretman("Tretman tela", "Opuštajući tretmani za celo telo.");
        tipovi.add(tretman6.newTipTretmana("Relaksaciona masaža", 80.0f, 75));
        tipovi.add(tretman6.newTipTretmana("Piling tela", 45.0f, 60));
        tretmani.add(tretman6);

        // Tretman 7
        KozmetickiTretman tretman7 = new KozmetickiTretman("Tretman kose", "Tretmani za negu kose i vlasišta.");
        tipovi.add(tretman7.newTipTretmana("Dubinska hidratacija kose", 55.0f, 50));
        tipovi.add(tretman7.newTipTretmana("Keratinsko ravnanje", 120.0f, 120));
        tretmani.add(tretman7);

        // Tretman 8
        KozmetickiTretman tretman8 = new KozmetickiTretman("Tretman obrva", "Oblikovanje i bojenje obrva.");
        tipovi.add(tretman8.newTipTretmana("Obrve za oblikovanje", 20.0f, 30));
        tipovi.add(tretman8.newTipTretmana("Bojenje obrva", 15.0f, 15));
        tretmani.add(tretman8);
        
     // Tretman 9
        KozmetickiTretman tretman9 = new KozmetickiTretman("Tretman stopala", "Nega stopala uz masažu i piling.");
        tipovi.add(tretman9.newTipTretmana("Piling stopala", 40.0f, 45));
        tipovi.add(tretman9.newTipTretmana("Luksuzna pedikir masaža", 60.0f, 60));
        tipovi.add(tretman9.newTipTretmana("Parafinska maska", 50.0f, 40));
        tretmani.add(tretman9);

        // Tretman 10
        KozmetickiTretman tretman10 = new KozmetickiTretman("Mikrodermoabrazija", "Dubinsko čišćenje kože mikro-kristalima.");
        tipovi.add(tretman10.newTipTretmana("Lagana mikrodermoabrazija", 80.0f, 60));
        tipovi.add(tretman10.newTipTretmana("Intenzivna mikrodermoabrazija", 120.0f, 90));
        tretmani.add(tretman10);

        // Tretman 11
        KozmetickiTretman tretman11 = new KozmetickiTretman("Tretman usana", "Nega i hidratacija usana.");
        tipovi.add(tretman11.newTipTretmana("Hidratantna maska za usne", 25.0f, 20));
        tretmani.add(tretman11);

        // Tretman 12
        KozmetickiTretman tretman12 = new KozmetickiTretman("Tretman očiju", "Smanjenje podočnjaka i umornog izgleda.");
        tipovi.add(tretman12.newTipTretmana("Hidrogel maska za oči", 30.0f, 30));
        tretmani.add(tretman12);
        
        ArrayList<String[]> converted = new ArrayList<>();
        for(KozmetickiTretman kt : tretmani) {
        	converted.add(KozmetickiTretmanProvider.TO_CSV.convert(kt));
        }
        
        ArrayList<String[]> convertedTT = new ArrayList<>();
        for(KozmetickiTretman.TipTretmana tt : tipovi) {
        	String[] tip = TipTretmanaProvider.TO_CSV.convert(tt);
        	tip[3] = tt.getTretman().getNaziv();
        	
        	convertedTT.add(tip);
        }
        
        try {
        	DataProvider.writeToCsv(converted, Settings.getDefaultSettings().getKozmetickiTretmanFilePath(), DataProvider.CSV_DELIMITER);
        	DataProvider.writeToCsv(convertedTT, Settings.getDefaultSettings().getTipKozmetickogTretmanaFilePath(), DataProvider.CSV_DELIMITER);
        }catch(IOException e) {
        	
        }
        
        
        return tretmani;
    }

    public static void main(String[] args) {
        Settings settings = Settings.getDefaultSettings();

        Set<String> existingUsernames = new HashSet<>();

        List<Klijent> generatedKlijenti = generateKlijent(existingUsernames);
        List<String[]> klijentiStr = new ArrayList<>();
        generatedKlijenti.forEach(k -> klijentiStr.add(KlijentProvider.TO_CSV.convert(k)));

        List<String[]> generatedRecepcioneri = generateRecepcioner(existingUsernames);
        List<String[]> generatedMenadzeri = generateMenadzer(existingUsernames);

        try {
            DataProvider.writeToCsv(klijentiStr, settings.getKlijentFilePath(), DataProvider.CSV_DELIMITER);
            System.out.println("Done klijenti");
            DataProvider.writeToCsv(generatedRecepcioneri, settings.getRecepcionerFilePath(), DataProvider.CSV_DELIMITER);
            DataProvider.writeToCsv(generatedMenadzeri, settings.getMenadzerFilePath(), DataProvider.CSV_DELIMITER);
        } catch (IOException e) {
            System.out.println("Mistake: " + e.getMessage());
        }
        
        TestData.getAndGenerateAllTretmani();
    }
}
