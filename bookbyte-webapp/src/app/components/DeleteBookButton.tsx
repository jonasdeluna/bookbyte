import Modal from "@/components/Modal";
import { TrashIcon } from "@heroicons/react/24/solid";
import { useState } from "react";
import { LibraryBook } from "../types";
import Button from "@/components/Button";
import { useMutation, useQueryClient } from "react-query";

type Props = {
  libraryBook: LibraryBook;
};

const DeleteBookButton = ({ libraryBook }: Props) => {
  const [isOpen, setIsOpen] = useState(false);

  const openModal = (e: React.MouseEvent<SVGSVGElement>) => {
    e.stopPropagation();
    setIsOpen(true);
  };

  const closeModal = () => {
    setIsOpen(false);
  };

  const queryClient = useQueryClient();
  const deleteBookMutation = useMutation({
    mutationFn: () =>
      fetch(`${process.env.NEXT_PUBLIC_API_URL}/library/${libraryBook.id}`, {
        method: "DELETE",
      }),
    onSuccess: () => {
      queryClient.invalidateQueries("librarybooks");
      closeModal();
    },
  });

  return (
    <>
      <Modal
        isOpen={isOpen}
        closeModal={closeModal}
        title={`Do you really want to delete '${libraryBook.book.title}'?`}
      >
        <div className="flex gap-2">
          <Button onClick={closeModal}>Cancel</Button>
          <Button danger onClick={() => deleteBookMutation.mutate()}>
            Delete
          </Button>
        </div>
      </Modal>
      <TrashIcon
        onClick={openModal}
        className="h-6 w-6 cursor-pointer text-red-500"
      />
    </>
  );
};

export default DeleteBookButton;
