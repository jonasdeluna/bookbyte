import { useForm } from "react-hook-form";
import { LibraryBook, Person } from "../types";
import Button from "@/components/Button";
import Input from "@/components/Input";
import { useMutation, useQueryClient } from "react-query";

type Props = {
  libraryBook: LibraryBook;
};

type BorrowBookForm = {
  name: string;
  email: string;
  days: number;
};

type BorrowBookRequest = {
  uuid: string;
  days: number;
};

const EMAIL_PATTERN =
  /^[a-zA-Z0-9_+&*-]+(?:\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\.)+[a-zA-Z]{2,7}$/;

const LibraryBookDetails = ({ libraryBook }: Props) => {
  const {
    register,
    formState: { errors },
    handleSubmit,
    reset,
  } = useForm<BorrowBookForm>();

  const queryClient = useQueryClient();

  const addPersonMutation = useMutation({
    mutationFn: (person: Person) =>
      fetch(`${process.env.NEXT_PUBLIC_API_URL}/person/`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(person),
      }),
  });

  const borrowBookMutation = useMutation({
    mutationFn: (borrow: BorrowBookRequest) =>
      fetch(`${process.env.NEXT_PUBLIC_API_URL}/library/${libraryBook.id}/borrow`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(borrow),
      }),
    onSuccess: () => {
      queryClient.invalidateQueries("librarybooks");
      reset();
    },
  });

  const returnBookMutation = useMutation({
    mutationFn: (ret: BorrowBookRequest) =>
      fetch(`${process.env.NEXT_PUBLIC_API_URL}/library/${libraryBook.id}/return`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(ret),
      }),
    onSuccess: () => {
      queryClient.invalidateQueries("librarybooks");
    },
  });

  const borrowBook = ({ name, email, days }: BorrowBookForm) => {
    const person: Person = {
      uuid: crypto.randomUUID(),
      name,
      email,
    };

    addPersonMutation.mutateAsync(person).then(() => {
      borrowBookMutation.mutate({ uuid: person.uuid, days });
    });
  };

  const returnBook = () => {
    returnBookMutation.mutate({ uuid: libraryBook.borrower!.uuid, days: 0 });
  };

  return (
    <div>
      <h1>
        Book status: {libraryBook.status}{" "}
        {libraryBook.status == "BORROWED" &&
          `by ${libraryBook.borrower?.name} (${libraryBook.borrower?.email})`}
      </h1>
      <h1>{libraryBook.status == "AVAILABLE" ? "Borrow" : "Return"}</h1>
      {libraryBook.status === "AVAILABLE" ? (
        <form
          onSubmit={handleSubmit(borrowBook)}
          className="flex flex-col gap-4"
        >
          <Input
            label="Name"
            name="name"
            error={errors.name && "Invalid Name"}
            register={register}
            required
          />
          <Input
            label="Email"
            name="email"
            error={errors.email && "Invalid Email"}
            register={register}
            pattern={EMAIL_PATTERN}
            required
          />
          <Input
            label="Number of days"
            name="days"
            error={errors.days && "Invalid number of days"}
            register={register}
            required
          />
          <Button submit>Borrow</Button>
        </form>
      ) : (
        <Button onClick={returnBook}>Return</Button>
      )}
    </div>
  );
};

export default LibraryBookDetails;
