# Core Module Documentation

## Overview

The core module contains the core functionality of BookByte. It is responsible for the following:

- Defining the data model
- Defining the storage layer

## Data Model

The data model is defined in the root package of the core module. It is composed of the following classes:

### Book.java
[Book.java](./book/Book.java) is a class that represents a book. It has the following properties:

- `isbn` - The ISBN of the book
- `title` - The title of the book
- `author` - The author of the book

### BookCatalog.java
[BookCatalog.java](./book/BookCatalog.java) is a class that represents the catalog of books. It contains a list of books and is responsible for adding and removing books from the catalog.

### LibraryBook.java
[LibraryBook.java](./library/LibraryBook.java) is a class that represents the physical books in the library. It uses the Book class and an original id. This is because the ISBN is only a number that describes the book, and the id is so the librarian can track each book in case of duplicates. 

### Library.java
[Library.java](./library/Library.java) is a class that represents the library. It contains a BookCatalog and a list of LibraryBooks. It is responsible for adding and removing books from the library.

### BookStatus.java
[BookStatus.java](./library/BookStatus.java) is a enum class that defines the status of a libraryBook. It contains the values AVAILABLE and BORROWED

### Person.java
[Person.java](./person/Person.java) is a class that represents a person. It contains an uuid(unique user id), name and email. 

### PersonCatalog.java
[PersonCatalog.java](./person/PersonCatalog.java) is a class that represents the catalog of people. It contains a list of people and is responsible for adding and removing people from the catalog.
### ValidationUtils.java
[ValidationUtils.java](./ValidationUtils.java) is a utility class that checks if the ISBN only contains numbers and 13 characters. It also checks that the bookID only contains numbers and 16 numbers.

## Storage Layer

The storage layer is defined in the `storage` package of the core module. It is responsible for serializing and deserializing the data model to and from a file. You can read more about its data structure in the [README.md](../storage/README.md) in the storage module. It is composed of the following classes:

### Serializer.java
[Serializer.java](./storage/Serializer.java) is a interface for the individual serializers.

### BookSerializer.java
[BookSerializer.java](./storage/BookSerializer.java) is a class that serializes/deserializes a BookCatalog to JSON.

### LibraryBookSerializer.java
[LibraryBookSerializer.java](./storage/LibraryBookSerializer.java) is a class that serializes/deserializes a Library to JSON.

### PersonSerializer.java
[PersonSerializer.java](./storage/PersonSerializer.java) is a class that serializes/deserializes a Person to JSON.

### ModelAccess.java
[ModelAccess.java](./storage/ModelAccess.java) is a interface that defines reading and writing to a data source.

### DirectModelAccess.java
[DirectModelAccess.java](./storage/DirectModelAccess.java) is a class that saves/loads directly in the system's memory.

### FileModelAccess.java
[FileModelAccess.java](./storage/FileModelAccess.java) is a class for reading and writing to a file.

### RemoteModelAccess.java
[RemoteModelAccess.java](./storage/RemoteModelAccess.java) is a class for reading and writing to a server.
