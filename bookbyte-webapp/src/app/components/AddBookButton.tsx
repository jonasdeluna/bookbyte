import Button from "@/components/Button";
import { PlusIcon } from "@heroicons/react/24/solid";
import Modal from "@/components/Modal";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import Input from "@/components/Input";
import { Book, LibraryBook, LibraryBookShort } from "../types";
import { useMutation, useQueryClient } from "react-query";

type AddBookForm = { id: string } & Book;

const ISBN13_PATTERN = /^\d{13}$/;
const ID_PATTERN = /^\d{16}$/;

const AddBookButton = () => {
  const [isOpen, setIsOpen] = useState(false);

  const {
    register,
    formState: { errors },
    handleSubmit,
    reset,
  } = useForm<AddBookForm>();

  const queryClient = useQueryClient();

  const addBookMutation = useMutation({
    mutationFn: ({
      book,
      libraryBook,
    }: {
      book: Book;
      libraryBook: LibraryBookShort;
    }) =>
      Promise.all([
        fetch("http://localhost:8080/books/", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(book),
        }),
        fetch("http://localhost:8080/library/", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(libraryBook),
        }),
      ]),
    onError: (error) => {
      console.log(error);
    },
    onSuccess: () => {
      queryClient.invalidateQueries("librarybooks");
      reset();
      closeModal();
    },
  });

  const openModal = () => {
    setIsOpen(true);
  };

  const closeModal = () => {
    setIsOpen(false);
  };

  const addBook = ({ id, ...book }: AddBookForm) => {
    const libraryBook: LibraryBookShort = {
      id,
      isbn13: book.isbn13,
    };

    addBookMutation.mutate({ book, libraryBook });
  };

  return (
    <>
      <Modal
        isOpen={isOpen}
        closeModal={closeModal}
        title="Add book to library"
      >
        <form onSubmit={handleSubmit(addBook)} className="flex flex-col gap-4">
          <Input
            label="ID"
            name="id"
            error={errors.id && "Invalid ID"}
            register={register}
            pattern={ID_PATTERN}
            required
          />
          <Input
            label="ISBN"
            name="isbn13"
            error={errors.isbn13 && "Invalid ISBN"}
            register={register}
            pattern={ISBN13_PATTERN}
            required
          />
          <Input
            label="Title"
            name="title"
            error={errors.title && "Invalid title"}
            register={register}
            required
          />
          <Input
            label="Author"
            name="author"
            error={errors.author && "Invalid author"}
            register={register}
            required
          />
          <Button submit>
            <div className="flex items-center gap-1">
              <PlusIcon className="h-6 w-6" /> Add book
            </div>
          </Button>
        </form>
      </Modal>
      <Button onClick={openModal}>
        <div className="flex items-center gap-1">
          <PlusIcon className="h-6 w-6" /> Add book
        </div>
      </Button>
      {addBookMutation.isError && (
        <p className="text-red-500">{addBookMutation.error as any}</p>
      )}
    </>
  );
};

export default AddBookButton;
