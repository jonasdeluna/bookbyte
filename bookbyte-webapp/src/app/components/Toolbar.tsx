"use client";
import { LibraryBook } from "../types";
import AddBookButton from "./AddBookButton";

const Toolbar = () => {
  return (
    <div className="my-3 flex justify-end">
      <AddBookButton />
    </div>
  );
};

export default Toolbar;
