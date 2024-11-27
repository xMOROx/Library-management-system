# Documentation

## Table of Contents

- [Documentation](#documentation)
  - [Technologies](#technologies)
  - [Setup](#setup)
  - [Database Schema](#database-schema)
  - [Object Schema](#object-schema)
  - [Adding a New User](#adding-a-new-user)
  - [Data Display](#data-display)
  - [Data Updates And Additions](#data-updates-and-additions)
  - [Profile](#profile)
  - [Settings](#settings)
  - [Announcements](#announcements)
  - [Statistics](#statistics)
  - [Recommendations](#recommendations)
  - [Reviews](#reviews)
  

## Technologies

- Java 17
- Spring Boot
- Spring Data JPA/JDBC
- Spring Mail
- MySQL/MariaDB
- JavaFX
- MaterialFX
- JavaFX-Ikonli
- Ikonli-Fontawesome5
- Lombok
- Gradle

## Setup

Set the database credentials in the `application.properties` file.
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name  
spring.datasource.username=your_username  
spring.datasource.password=your_password  
```

Disable `spring.profiles.active=dev`.

Run the file `src/main/java/pl/edu/agh/managementlibrarysystem/ManagementLibrarySystemApplication.java` or execute the command `gradle bootRun` in the root directory of the project.

## Database Schema

![Database Schema](./docs/images/database_scheme.png)

## Object Schema

![Object Schema](./docs/images/orm_scheme.png)

Objects can be found in the directory/package `src/main/java/pl/edu/agh/managementlibrarysystem/models`.

## Adding a New User

### Creating a new user account
![Step 1: Create Account](./docs/images/creating_users/create_account_1.png)
![Step 2: Confirm Account Creation](./docs/images/creating_users/create_account_2.png)

### Adding and Modifying Users as an Admin
![Adding Users as Admin](./docs/images/creating_users/add_other.png)
![Editing Users as Admin](./docs/images/creating_users/edit_other.png)

## Data Display

### Viewing Books
![Viewing Books](./docs/images/data/book_show.png)

### Viewing Additional Book Details
![Book Details](./docs/images/data/details.png)

### Viewing Borrowed Books
![Borrowed Books](./docs/images/data/issued_show.png)

### Viewing Notifications
![Notifications](./docs/images/data/notifications_show.png)

### Viewing Users
![Users List](./docs/images/data/users_show.png)

## Data Updates And Additions

### Adding Books to the Library (including authors, publishers, and genres)
![Add Book with Details](./docs/images/updates/add_book.png)

### Returning or Renewing a Book (if days are specified)
![Return Book](./docs/images/updates/return_book.png)

### Borrowing a Book
![Borrow Book](./docs/images/updates/isssue.png)

## Profile

### Viewing and Modifying User Profiles
![User Profile](./docs/images/profile.png)

## Settings

### Changing System Settings as an Administrator
- Changing the admin password:
  ![Change Admin Password](./docs/images/updates/settings_sign.png)
- Modifying fees:
  ![Modify Fees](./docs/images/updates/settings_fee.png)
- Configuring the SMTP server:
  ![SMTP Configuration](./docs/images/updates/settings_mail.png)

## Announcements

### Sending Notification Emails to Users (by librarian or administrator)
![Add Notification](./docs/images/updates/add_notification.png)

## Statistics

### Available for both regular users and administrators/librarians:
- **User Statistics**:
  ![User Statistics](./docs/images/data/stats_us.png)
- **Admin Statistics**:
  ![Admin Statistics](./docs/images/data/stats_ad.png)
- **Shared Visualizations**:
  ![Shared Charts](./docs/images/data/charts.png)

## Recommendations

A module providing book recommendations to users based on observed preferences:
![Book Recommendations](./docs/images/data/recomended.png)

## Reviews

Allows users to review and rate books:
![Review and Rate Books](./docs/images/updates/reviewing.png)
