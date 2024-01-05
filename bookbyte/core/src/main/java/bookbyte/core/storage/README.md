# JSON Documentation

## Overview

BookByte uses JSON files to store data. This document describes the structure of these files. 

### Book

The Book file represents a book in the database. It has the following structure:
```json
{
  "isbn": "string",
  "title": "string",
  "author": "string",
}
```

### LibraryBook

The LibraryBook file represents a book in the library. It has the following structure:
```json
{
  "id": "string",
  "isbn": "string",
  "status": "string",
  "borrower": "string",
  "dueDate": "long",
}
```
The `isbn` property is a reference to an existing `Book`. The `status` property can be one of the following values:

- `AVAILABLE`
- `BORROWED`

### Person

The Person file represents a person in the database. It has the following structure:
```json
{
  "id": "string",
  "name": "string",
  "email": "string",
}
```
