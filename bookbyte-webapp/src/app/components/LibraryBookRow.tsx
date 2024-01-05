import Pill from "@/components/Pill";
import { LibraryBook } from "../types";
import DeleteBookButton from "./DeleteBookButton";
import Modal from "@/components/Modal";
import { useState } from "react";
import LibraryBookDetails from "./LibraryBookDetails";

type Props = {
  libraryBook: LibraryBook;
  index: number;
};

function formatDate(date: Date) {
  const day = String(date.getDate()).padStart(2, "0");
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const year = String(date.getFullYear());

  return `${day}/${month}/${year}`;
}

const LibraryBookRow = ({ libraryBook, index }: Props) => {
  const [isOpen, setIsOpen] = useState(false);

  const openModal = () => {
    setIsOpen(true);
  };

  const closeModal = () => {
    setIsOpen(false);
  };

  return (
    <>
      <tr
        onClick={openModal}
        key={`${libraryBook.id}`}
        className={`${
          index % 2 == 0 ? "bg-stone-50" : "bg-stone-100"
        } cursor-pointer`}
      >
        <td className="px-2 py-4">{libraryBook.id}</td>
        <td className="px-2 py-4">{libraryBook.book.title}</td>
        <td className="px-2 py-4">{libraryBook.book.author}</td>
        <td className="px-2 py-4">
          <Pill color={libraryBook.status === "AVAILABLE" ? "green" : "red"}>
            {libraryBook.status === "AVAILABLE" ? "Available" : "Borrowed"}
          </Pill>
        </td>
        <td className="px-2 py-4">
          {libraryBook.dueDate ? formatDate(libraryBook.dueDate) : "-"}
        </td>
        <td className="px-2 py-4">
          <DeleteBookButton libraryBook={libraryBook} />
        </td>
        <td>
          <Modal
            isOpen={isOpen}
            closeModal={closeModal}
            title={`${libraryBook.id} - ${libraryBook.book.title}`}
          >
            <LibraryBookDetails libraryBook={libraryBook} />
          </Modal>
        </td>
      </tr>
    </>
  );
};

export default LibraryBookRow;
