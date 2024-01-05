# Release 0.1.0

In this initial release, we've set up the main project structure for the frontend. We've created a JavaFX project, configured the FXML file, and coded basic file boilerplate in the BookByteApp and BookByteController. Basic documentation for the different modules have been made as well.

We also introduce the following classes:

- **Library Class**: We've introduced the Library class, serving as the digital representation of a physical library. It is equipped with a collection of LibraryBook objects, each representing a book housed within the library.

- **Book Class**: The Book class represents a book. Currently, it includes essential attributes such as a title and an ISBN (International Standard Book Number).

- **Book Catalog**: The BookCatalog class serves as a database over all Book instances, and make them acessible by ISBN or title.

- **LibraryBook Class**: LibraryBook represents a book that is housed within the library. Each LibraryBook has a unique ID and is linked to a corresponding Book instance.

- **Validation Utilities**: The ValidationUtils has methods for validating ISBNs and LibraryBook IDs.

In addition, we have the following classes for file management:

- **BookFileSaver** and **BookFileLoader**: Respectively saves and loads either Book or LibraryBook instances to the file system.

- **BookSerializer** and **BookDeserializer**: These classes do the serialization and deserialization of the Book class.

- **LibraryBookSerializer** and **LibraryBookDeserializer**: These classes do the serialization and deserialization of the LibraryBook class.

In this initial release, our librarian can perform the following actions:

- **Add and Remove Books**: Books can be added to or removed from the library's inventory.

- **Save and Load Books**: The librarian has the ability to save and load the library's collection to and from the file system.

This is described further in "User Story 1", which is now fulfilled.