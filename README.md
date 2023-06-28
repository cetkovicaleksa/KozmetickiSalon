# KozmetickiSalon
 Projekat iz OOP.



 paket: dataProvajderi

  Sadrzi sve klase ciji objekti su zaduzeni za rad sa fajlovima odnosno bazom
  podataka. Ove klase implementiraju interfejs Provider za kojeg je inspiracija
  bila http progokol sa get, post, put i delete requestovima.

  Svaki entitet ima svog provajdera, i cuva se u posebnom fajlu.
  Posto se entiteti povezuju (npr. Kozmeticar ima listu svih tretmana za koje je obucen)
  potrebno je nekako identifikovati povezane objekte zbog upisa u fajl.
  Ovo je odgovornost providera koji dodjeljuje id entitetima po potrebi.

  DataManager sluzi da grupise sve providere. Salon kao atribut ima referencu na
  svog data menadzera. Pored grupisanja providera data manager omogucava salonu
  pristup data providerima bez trazenja konkretnog providera. Vise o ovome u objasnjenju
  paketa helpers.


paket: entitetima

  Sadrzi sve klase koje predstavljaju korisnike salona ili ostale entitete kojima
  salon rukuje. Sadrze samo svoje osnovne atribute konstruktore i getere/setere.

  Korisnik je abstraktna klasa koju nasledjuju svi ostali tipovi korisnika salona.
  Sadrzi sve atribute i metode zajednicke za sve korisnike.
  Children of Korisnik: Klijent i Zaposleni

  Zaposleni je abstraktna klasa koja nasledjuje Korisnik klasu. Sadrzi jos dodatnih
  atributa i metoda jedinstvenih za sve zaposlene. (npr. izracunajPlatu()).
  Children of Zaposleni: Recepcioner, Menadzer, Kozmeticar



paket: exceptions

  SAdrzi posebne exceptione koje koristi salon.


paket: helpers

  Pomocne klase: Tester, Puter. Sluze za pristup entitetime.
  Odnosno za pretragu entiteta i njihou izmjenu.

  Komunikacija izmedju salona i baze se obavlja preko requestova koji kao parametre
  primaju Tester i opcionalni Puter.

  Tester sluzi da provjeri da li neki entitet ispunjava date uslove zadate njegovim
  atributom filter. Ovaj atribut implementira funkcionalni interfejs Predicate
  i implementira njegovu jedinu metodu test(). Ova klasa takodje ima metode za
  promjenu filtera i kombinaciju filtera sa drugim filterima.

  Puter sluzi da bi updejtovao atribute datog entiteta.

  DataManager na zakljucije kom provideru da se obrati na osnovu proslijedjenog
  testera.

paket: saloni

  Razliciti tipovi salona imaju razlicitu funkcionalnost.
  Salon odnosno neulogovani salon ima metode samo za login i registraciju.
  Dok ostali saloni imaju metode koje pristupaju dataManageru preko speakToTheManager()

  Ulogovani salon cuva referencu na svoj odgovarajuci neulogovani salon kako bi
  imao pristup dataManageru salona...

  U ulogovanom salonu ce se nalaziti metode kao zakaziTretman(), prikazTretmana(), a
  kod menadzera i metode koje omogucavaju crud.
