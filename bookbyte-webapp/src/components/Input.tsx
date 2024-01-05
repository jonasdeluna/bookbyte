import { UseFormRegister, ValidationRule } from "react-hook-form";

type Props = {
  label?: string;
  required?: boolean;
  register: UseFormRegister<any>;
  name: string;
  error?: string;
  pattern?: ValidationRule<RegExp>;
};

const Input = ({ label, required, register, name, error, pattern }: Props) => {
  return (
    <div>
      {label && (
        <label>
          {label}
          {error && <span className="text-red-600"> {error}</span>}
        </label>
      )}
      <input
        id={name}
        {...register(name, { pattern })}
        required={required}
        type="text"
        className="bg-stone-50 border border-stone-300 text-sm rounded-lg focus:ring-blue-200 focus:border-blue-200 block w-full p-2.5"
      />
    </div>
  );
};

export default Input;
