import { LibraryBook } from "../types";
import LibraryBookRow from "./LibraryBookRow";

type Props = {
  libraryBooks?: LibraryBook[];
};

const TableHeader = () => {
  return (
    <thead>
      <tr className="bg-stone-300">
        <th className={"text-left text-stone-800 px-2 py-4"}>ID</th>
        <th className={"text-left text-stone-800 px-2 py-4"}>Title</th>
        <th className={"text-left text-stone-800 px-2 py-4"}>Author</th>
        <th className={"text-left text-stone-800 px-2 py-4"}>Status</th>
        <th className={"text-left text-stone-800 px-2 py-4"}>Due Date</th>
        <th className={"text-left text-stone-800 px-2 py-4"}></th>
      </tr>
    </thead>
  );
};

const LibraryBookTable = ({ libraryBooks }: Props) => {
  return (
    <table className="w-full">
      <TableHeader />
      <tbody>
        {libraryBooks &&
          libraryBooks.map((libraryBook, i) => (
            <LibraryBookRow key={i} libraryBook={libraryBook} index={i} />
          ))}
      </tbody>
    </table>
  );
};

export default LibraryBookTable;
