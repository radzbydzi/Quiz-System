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

## Co dodano w Spring Initializr?

 - **Spring Boot DevTools** - po każdym zapisie następuje automatyczna rekompilacja kodu
 - **Spring Web** - kontrola WEB i REST
 - **Spring Session** - sesje w Spring
 - **Vaadin** - UI strony
 - **Spring Data JPA** - klasy Java są szablonami tabel w bazie, a obiekty tych klas encjami 
 - **H2 Database** - baza danych

## Co dodatkowo dodano w pom.xml?
Obsługa emaili

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-mail</artifactId>
    </dependency>
    <dependency>
    	<groupId>javax.mail</groupId>
    	<artifactId>javax.mail-api</artifactId>
    	<version>1.5.2</version>
    </dependency>
Dla funkcji skrótu SHA1

    <dependency>
        <groupId>commons-codec</groupId>
        <artifactId>commons-codec</artifactId>
    </dependency>
