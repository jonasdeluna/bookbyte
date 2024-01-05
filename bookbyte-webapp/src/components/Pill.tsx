type Props = {
  color: string;
  children: React.ReactNode | React.ReactNode[];
};

const Pill = ({ color, children }: Props) => {
  return (
    <div
      className={`inline-block bg-${color}-100 px-2 rounded-full text-${color}-600`}
    >
      {children}
    </div>
  );
};

export default Pill;
