# System quizowy
System quizowy służący do zbierania pytań od użytkowników napisany w Springboocie.
## Elementy warte uwagi

 - sesje w Springboot
 - wysyłanie email
 - baza H2 z JPA zapisywana do pliku; zastosowanie relacji (@ManyToOne)
 - tabele z elementami UI (przyciski, listy rozwijane) z funkcjami poszczególnych elementów działającymi bezpośrednio na dany obiekt w tabeli
 - tabele ładowane z listy oraz za pomocą lazy loading
 - prosty CSS obiektów UI
 - REST wyświetlający wszystkie pytania dodane przez użytkowników w formacie CSV
 - zawartość do edycji zależna od aktualnie zalogowanego użytkownika
 - opcje administratorskie niedostępne dla zwykłych użytkowników (zmiana uprawnień użytkowników, usuwanie ich)
 - użycie streamAPI do działań na listach
 - wyrażenia regularne (nie można wysłać prośby dodania z maila innego niż w domenie utp.edu.pl)
## Co zmienić by działało?
Zawartość pliku

> systemQuiz\src\main\resources\application.properties

Należy wypełnić odpowiednio pola.

**Przykład**

    spring.datasource.url=jdbc:h2:file:C:/Baza/baza
    spring.datasource.driverClassName=org.h2.Driver
    spring.datasource.username=sa
    spring.datasource.password=password
    spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
    #to sprawia ze dane w bazie sie nie usuna
    spring.jpa.hibernate.ddl-auto=update
    
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=nazwa@gmail.com
    spring.mail.password=zaq1@WSX
    spring.mail.properties.mail.smtp.auth=true;
    spring.mail.properties.mail.smtp.starttls.enable=true
    spring.mail.properties.mail.smtp.starttls.required=true

Używając serwera SMTP Google może istnieć możliwość zaznaczenia checkboxa w panelu:

> [https://myaccount.google.com/lesssecureapps](https://myaccount.google.com/lesssecureapps)

Bez zaznaczenia tej opcji może wystąpić błąd logowania.