import { isDate, isStrongPassword } from "validator";

const MIN_AGE = 14;

export const validatePassword = (value: any) => {
  const v = typeof value === "string" ? value : "";

  const isStrong = isStrongPassword(v, {
    minLength: 8,
    minLowercase: 1,
    minUppercase: 1,
    minNumbers: 1,
    minSymbols: 1,
    returnScore: false,
  });

  return (
    isStrong ||
    "Password must contain:\n" +
      "• at least 8 characters\n" +
      "• 1 uppercase letter\n" +
      "• 1 lowercase letter\n" +
      "• 1 number\n" +
      "• 1 symbol"
  );
};

export const validateBirthDate = (value: any) => {
  if (!value) return "Birth Date is required";

  const valid = isDate(value.toString(), {
    format: "YYYY-MM-DD",
    strictMode: true,
    delimiters: ["-"],
  });

  if (!valid) return "Enter a valid date";

  const birthDate = new Date(value.toString());

  const today = new Date();
  today.setHours(0, 0, 0, 0);

  if (birthDate > today) {
    return "Birth Date cannot be in the future";
  }

  const age =
    today.getFullYear() -
    birthDate.getFullYear() -
    (today <
    new Date(today.getFullYear(), birthDate.getMonth(), birthDate.getDate())
      ? 1
      : 0);

  if (age < MIN_AGE) {
    return `You must be at least ${MIN_AGE} years old`;
  }

  return true;
};
