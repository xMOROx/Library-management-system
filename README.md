# Dokumentacja

## Spis treści

- [Dokumentacja](#dokumentacja)
  - [Spis treści](#spis-treści)
  - [Technologie](#technologie)
  - [Uruchomienie](#uruchomienie)
  - [Schemat bazydanych](#schemat-bazydanych)
  - [Schemat obiektowy](#schemat-obiektowy)
  - [GUI](#gui)
  - [Dodawanie nowego użytkownika](#dodawanie-nowego-użytkownika)

## Technologie

- Java 17
- Spring Boot
- Spring Data JPA/JDBC
- Spring mail
- MySQL/MariaDB
- JavaFX
- MaterialFX
- JavaFX-Ikonli
- Ikonli-Fontawesome5
- Lombok
- Gradle

## Uruchomienie

Należy w pliku `application.properties` ustawić dane do bazy danych.

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=your_username
spring.datasource.password=your_password
```

i wyłączyć  `spring.profiles.active=dev`

Wystarczy uruchomić plik `src/main/java/pl/edu/agh/managementlibrarysystem/ManagementLibrarySystemApplication.java`
albo uruchomić komendę `gradle bootRun` w katalogu głównym projektu.

## Schemat bazydanych

![Schemat bazydanych](./docs/images/database_scheme.png)

## Schemat obiektowy

![Schemat obiektowy](./docs/images/orm_scheme.png)

Obiekty znajdują się w katalogu/pakiecie `src/main/java/pl/edu/agh/managementlibrarysystem/models`.

## Dodawanie nowego użytkownika
![Dodawanie nowego użytkownika 1](./docs/images/creating_users/create_account_1.png)
![Dodawanie nowego użytkownika 2](./docs/images/creating_users/create_account_2.png)

## Dodawanie i modyfikacja użytkownika od strony administratora:
![Dodawanie nowych użytkowników jako administrator](./docs/images/creating_users/add_other.png)
![Modyfikacja użytkowników jako administrator](./docs/images/creating_users/edit_other.png)

## Wyświetlanie danych

### Wyświetlanie książek:

![Wyświetlanie książek](./docs/images/data/book_show.png)

### Wyświetlanie dodatkowych informacji o książce:

![Detale książki](./docs/images/data/details.png)

### Wyświetlanie wypożyczonych książek:

![Wyświetlanie wypożyczonych książek](./docs/images/data/issued_show.png)

### Wyświetlanie powiadomień:

![Wyświetlanie powiadomień](./docs/images/data/notifications_show.png)

### Wyświetlanie użytkowników:
![Wyświetlanie użytkowników](./docs/images/data/users_show.png)

## Aktualizacja/dodawanie danych

### Podczas dodawania książek do biblioteki możliwe jest również dodanie nowych autorów, wydawców, oraz gatunków:
![Dodawanie książki wraz z autorami](./docs/images/updates/add_book.png)

### Zwrot/odnowienie książki (jeśli podana została liczba dni):
![Zwracanie książki](./docs/images/updates/return_book.png)

### Wypożyczenie książki:
![Wypożyczanie książki](./docs/images/updates/isssue.png)

## Profil

### Klient biblioteki może wyświetlać i modyfikować swój profil:
![Profil użytkownika](./docs/images/profile.png)

## Settings

### Administrator może zmienić globalne ustawienia systemu zarządzającego np. wartość opłat, serwer SMTP z którego korzystać będzie system:
![Zmiana hasła administratora](./docs/images/updates/settings_sign.png)
![Zmiana opłat](./docs/images/updates/settings_fee.png)
![Zmiana serwera SMTP](./docs/images/updates/settings_mail.png)

## Annoucements

### Bibliotekarz oraz administrator mogą zlecać wysyłanie maili powiadamiających odpowiednich użytkowników:

![Dodawanie powiadomienia](./docs/images/updates/add_notification.png)

## Statistics

### Statystyki dostępne zarówno dla zwykłych użytkowników jak i dla administratorów/bibliotekarzy

- Dla użytkowników:
![Statystyki użytkownika](./docs/images/data/stats_us.png)

- Dla użytkowników:
![Statystyki administratora](./docs/images/data/stats_ad.png)

- Wspólne:
![Wspólne wykresy](./docs/images/data/charts.png)

## Recomendations

Część aplikacji odpowiadająca za polecanie użytkownikom książek na podstawie zaobserwowanych preferencji:
![Polecanie książki](./docs/images/data/recomended.png)

## Reviews

Pozwala użytkownikom na ocenę oraz recenzję książek:
![Ocena i recenzja książek](./docs/images/updates/reviewing.png)


