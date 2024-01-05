type Props = {
  onClick?: () => void;
  children: React.ReactNode | React.ReactNode[];
  submit?: boolean;
  danger?: boolean;
};

const Button = ({ children, submit, danger, onClick }: Props) => {
  return (
    <button
      type={submit ? "submit" : "button"}
      onClick={onClick}
      className={`py-2 px-4 shadow-md rounded-full ${
        danger ? "bg-red-500" : "bg-stone-200"
      } font-bold hover:${danger ? "bg-red-600" : "bg-stone-300"} active:${
        danger ? "bg-red-700" : "bg-stone-400"
      } duration-100`}
    >
      {children}
    </button>
  );
};

export default Button;
