export type Book = {
  isbn13: string;
  title: string;
  author: string;
};

export type BookStatus = "AVAILABLE" | "BORROWED";

export type LibraryBook = {
  id: string;
  book: Book;
  status: BookStatus;
  borrower?: Person;
  dueDate?: Date;
};

export type LibraryBookShort = {
  id: string;
  isbn13: string;
};

export type Person = {
  uuid: string;
  name: string;
  email: string;
};
