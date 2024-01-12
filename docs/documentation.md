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

![Schemat bazydanych](./images/database_scheme.png)

## Schemat obiektowy

![Schemat obiektowy](./images/orm_scheme.png)

Obiekty znajdują się w katalogu/pakiecie `src/main/java/pl/edu/agh/managementlibrarysystem/models`.

## Dodawanie nowego użytkownika

Obiekty związane z dodawaniem użytkownika:

- controllers/AddUserController.java
- services/UserService.java
- repositories/UserRepository.java
- resources/fxml/create-user.fxml

![Dodawanie nowego użytkownika 1](./images/creating_users/create_account_1.png)
![Dodawanie nowego użytkownika 2](./images/creating_users/create_account_2.png)

Dodawanie i modyfikacja użytkownika od strony administratora:

- controllers/AddOtherUserController.java
- controllers/EditUserController.java
- services/UserService.java
- repositories/UserRepository.java
- resources/fxml/editUsers.fxml
- resources/fxml/addOtherUsers.fxml

![Dodawanie nowych użytkowników jako administrator](./images/creating_users/add_other.png)
![Modyfikacja użytkowników jako administrator](./images/creating_users/edit_other.png)

## Wyświetlanie danych

Wyświetlanie książek:
- controllers/BookController.java
- services/BookService.java
- repositories/BookRepository.java
- resources/fxml/books.fxml

![Wyświetlanie książek](./images/data/book_show.png)

Wyświetlanie dodatkowych informacji o książce:

![Detale książki](./images/data/details.png)

Wyświetlanie wypożyczonych książek:
- controllers/IssuedBookController.java
- services/BookService.java
- repositories/BookRepository.java
- resources/fxml/issuedBooks.fxml

![Wyświetlanie wypożyczonych książek](./images/data/isssued_show.png)

Wyświetlanie powiadomień:
- controllers/NotificationsController.java
- services/NotificationService.java
- repositories/NotificationRepository.java
- resources/fxml/notifications.fxml

![Wyświetlanie powiadomień](./images/data/notifications_show.png)

Wyświetlanie użytkowników:
- controllers/AllUserListController.java
- services/UserService.java
- repositories/UserRepository.java
- resources/fxml/usersAdmin.fxml

![Wyświetlanie użytkowników](./images/data/users_show.png)

## Aktualizacja/dodawanie danych

Podczas dodawania książek do biblioteki możliwe jest również dodanie nowych autorów, wydawców, oraz gatunków:
- controllers/entry/AuthorDataEntryController.java
- controllers/entry/BookDataEntryController.java
- controllers/entry/GenresDataEntryController.java
- controllers/entry/PublisherDataEntryController.java
- services/AuthorService.java
- services/BookService.java
- services/GenresService.java
- services/PublisherService.java
- repositories/AuthorRepository.java
- repositories/BookRepository.java
- repositories/GenresRepository.java
- repositories/PublisherRepository.java
- resources/fxml/authorDataEntry.fxml
- resources/fxml/bookDataEntry.fxml
- resources/fxml/genresDataEntry.fxml
- resources/fxml/publisherDataEntry.fxml

![Dodawanie książki wraz z autorami](./images/updates/add_book.png)

Zwrot/odnowienie książki (jeśli podana została liczba dni):
- controllers/ReturnBookController.java
- services/BookService.java
- repositories/BookRepository.java
- resources/returnBook.fxml

![Zwracanie książki](./images/updates/return_book.png)

Wypożyczenie książki:
- controllers/IssueBookController.java
- services/BookService.java
- services/UserService.java
- repositories/BookRepository.java
- repositories/UserRepository.java
- resources/issueBook.fxml

![Wypożyczanie książki](./images/updates/isssue.png)

## Profil

Klient biblioteki może wyświetlać i modyfikować swój profil:
- controllers/ProfileController.java
- services/ProfileService.java
- services/NotificationService.java
- services/BookService.java
- repositories/ProfileRepository.java
- repositories/NotificationRepository.java
- repositories/BookRepository.java
- resources/user.fxml

![Profil użytkownika](./images/profile.png)

## Settings

Administrator może zmienić globalne ustawienia systemu zarządzającego np. wartość opłat, serwer SMTP z którego korzystać będzie sysyem:
- resources/adminSettings.fxml
- resources/changeAdminPassword.fxml
- resources/configureEmailServer.fxml
- resources/changeFeePerDay.fxml
- service/UserService.java
- service/SettingsService.java
- session/UserSession.java
- repository/SettingsRepository.java
- repository/UserRepository.java
- controller/AdminSettingsController.java

![Zmiana hasła administratora](./images/updates/settings_sign.png)
![Zmiana opłat](./images/updates/settings_fee.png)
![Zmiana serwera SMTP](./images/updates/settings_mail.png)

## Annoucements

Bibliotekarz oraz administrator mogą zlecać wysyłanie maili powiadamiających odpowiednich użytkowników:
- resources/addNotification.fxml
- controller/AddNotificationController.java
- service/EmailService.java
- service/NotificationService.java
- service/BookService.java
- service/UserService.java
- repository/SettingsRepository.java
- repository/BookRepository.java
- repository/UserRepository.java
- repository/IssuedBooksRepository.java

![Dodawanie powiadomienia](./images/updates/add_notification.png)

## Statistics

Statystyki dostępne zarówno dla zwykłych użytkowników jak i dla administratorów/bibliotekarzy

Dla użytkowników:
- resources/statisticsUser.fxml
- controller/StatisticsUserController.java

![Statystyki użytkownika](./images/data/stats_us.png)

Dla użytkowników:
- resources/statisticsAdmin.fxml
- controller/StatisticsAdminController.java

![Statystyki administratora](./images/data/stats_ad.png)

Wspólne:
- resources/charts.fxml
- controller/popups/ChartsController.java
- service/StatisticsService.java
- repository/UserRepository.java
- repository/BookRepository.java
- repository/AuthorRepository.java
- repository/GenresRepository.java
- repository/ReadBookRepository.java
- repository/ReviewBookRepository.java
- repository/IssuedBookRepository.java

![Wspólne wykresy](./images/data/charts.png)

## Recomendations

Część aplikacji odpowiadająca za polecanie użytkownikom książek na podstawie zaobserwowanych preferencji:
- recommender/
- controller/RecommendedBookController.java
- service/BookService.java
- repository/BookRepository.java

![Polecanie książki](./images/data/recommended.png)

## Reviews

Pozwala użytkownikom na ocenę oraz recenzję książek:
- service/popups/ReviewBookController.java
- resources/reviewBook.fxml

![Ocena i recenzja książek](./images/updates/reviewing.png)


