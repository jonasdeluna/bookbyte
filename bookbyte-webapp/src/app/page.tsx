"use client";

import { useEffect, useState } from "react";
import LibraryBookTable from "./components/LibraryBookTable";
import Toolbar from "./components/Toolbar";
import { Book, BookStatus, LibraryBook, Person } from "./types";
import { useQueries, useQuery, useQueryClient } from "react-query";

export default function Home() {
  const fetchBooks = async () => {
    return fetch(`${process.env.NEXT_PUBLIC_API_URL}/books/`)
      .then((res) => (res.status === 204 ? [] : res.json()))
      .then(
        (data) =>
          Object?.fromEntries(
            data?.filter((x: Book) => x).map((x: Book) => [x.isbn13, x]),
          ),
      );
  };

  const fetchPeople = async () => {
    return fetch(`${process.env.NEXT_PUBLIC_API_URL}/person/`)
      .then((res) => (res.status === 204 ? [] : res.json()))
      .then(
        (data) =>
          Object?.fromEntries(
            data?.filter((x: Person) => x).map((x: Person) => [x.uuid, x]),
          ),
      );
  };

  const fetchLibraryBooks = async () => {
    const books = await fetchBooks();
    const people = await fetchPeople();

    return fetch(`${process.env.NEXT_PUBLIC_API_URL}/library/`)
      .then((res) => res.json())
      .then((data) =>
        data
          .map(
            (d: any) =>
              ({
                id: d.id,
                book: books[d.isbn13],
                status: d.borrower ? "BORROWED" : "AVAILABLE",
                borrower: d.borrower && people[d.borrower],
                dueDate: d.dueDate && new Date(d.dueDate * 1000),
              }) as LibraryBook,
          )
          .filter((book: LibraryBook) => book && book.book !== undefined),
      );
  };

  const { data: libraryBooks } = useQuery({
    queryKey: ["librarybooks"],
    queryFn: fetchLibraryBooks,
    refetchOnMount: false,
    refetchOnWindowFocus: false,
  });

  return (
    <main className="my-0 mx-auto w-[1280px] max-w-full">
      <Toolbar />
      <LibraryBookTable libraryBooks={libraryBooks} />
    </main>
  );
}
