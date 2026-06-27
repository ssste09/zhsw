import { useSignUp } from "hooks/auth";
import { StyleSheet, View, Button, ScrollView, Text } from "react-native";
import { useNavigation } from "@react-navigation/native";
import { isValidNumber } from "react-native-phone-entry";
import { useCallback, useState } from "react";
import { useForm } from "react-hook-form";
import { isE2E } from "e2e/utils/isE2E";
import {
  SignUpUserRequest,
  SignUpUserRequestGenderEnum,
} from "@myorg/auth_api";
import FormInput from "ui-components/FormInput";
import { SignUpUserFormValues } from "./types";
import { validateBirthDate, validatePassword } from "./utils";

const Signup = () => {
  const navigation = useNavigation<any>();

  const { trigger: signUp, error: signupError } = useSignUp();

  const defaultCountryCode = "IT";

  const [countryCode, setCountryCode] = useState(defaultCountryCode);
  const [callingCode, setCallingCode] = useState(["39"]);

  const onValid = async (values: SignUpUserFormValues) => {
    try {
      const payload: SignUpUserRequest = {
        ...values,
        birthDate: new Date(values.birthDate),
      };

      await signUp(payload);
      navigation.navigate("Login");
    } catch (error) {
      console.error("Signup failed", error);
    }
  };

  const checkNumberLength = useCallback(
    (value?: string) =>
      callingCode.some(
        (ncc) => (value || "")?.replace(`+${ncc}`, "").length > 0,
      ),
    [callingCode],
  );

  const {
    control,
    handleSubmit,
    formState: { errors, isSubmitting },
    trigger: triggerValidateForField,
  } = useForm<SignUpUserFormValues>({
    mode: "onBlur",
    defaultValues: {
      name: "",
      lastName: "",
      email: "",
      gender: isE2E ? SignUpUserRequestGenderEnum.Female : undefined,
      phone: "",
      birthDate: "",
      address: {
        street: "",
        streetNumber: undefined,
        city: "",
        postalCode: "",
        country: "",
      },
      password: "",
    },
  });

  const genderItems = [
    { label: "Select gender", value: "" },
    { label: "Male", value: SignUpUserRequestGenderEnum.Male },
    { label: "Female", value: SignUpUserRequestGenderEnum.Female },
    { label: "Other", value: SignUpUserRequestGenderEnum.Other },
  ];

  console.log("errors", errors);

  return (
    <ScrollView
      contentContainerStyle={styles.container}
      testID="signup"
      keyboardShouldPersistTaps="handled"
      keyboardDismissMode="on-drag"
    >
      <FormInput
        label="First name"
        control={control}
        name="name"
        rules={{ required: true }}
        errors={errors}
        testID="firstName"
      />
      <FormInput
        label="Last name"
        control={control}
        name="lastName"
        rules={{ required: true }}
        errors={errors}
        testID="lastName"
      />

      <FormInput
        type="email"
        label="Email"
        control={control}
        name="email"
        rules={{
          required: true,
          pattern: {
            value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
            message: "Enter a valid email address",
          },
        }}
        errors={errors}
        testID="signupEmail"
      />

      <FormInput
        label="Gender"
        control={control}
        name="gender"
        rules={{ required: true }}
        errors={errors}
        type="select"
        options={genderItems}
        trigger={triggerValidateForField}
        testID="gender"
      />

      <FormInput
        type="phoneNumber"
        control={control}
        label="Phone Number"
        name="phone"
        rules={{
          required: true,
          validate: (value) =>
            !checkNumberLength(value?.toString())
              ? "Phone Number is required"
              : isValidNumber(value?.toString() || "", countryCode) ||
                "Phone Number is invalid",
        }}
        trigger={triggerValidateForField}
        setCountryCode={setCountryCode}
        setCallingCode={setCallingCode}
        errors={errors}
      />

      <FormInput
        type="birthDate"
        label="Birth Date"
        control={control}
        name="birthDate"
        rules={{
          required: "Birth Date is required",
          pattern: {
            value: /^\d{4}-\d{2}-\d{2}$/,
            message: "Use format YYYY-MM-DD",
          },

          validate: (value) => validateBirthDate(value),
        }}
        errors={errors}
        testID="birthDate"
      />

      <FormInput
        label="Street"
        control={control}
        name="address.street"
        rules={{ required: true }}
        errors={errors}
        testID="street"
      />

      <FormInput
        type="number"
        label="Street Number"
        control={control}
        name="address.streetNumber"
        rules={{ required: true }}
        errors={errors}
        testID="streetNumber"
      />

      <FormInput
        label="City"
        control={control}
        name="address.city"
        rules={{ required: true }}
        errors={errors}
        testID="city"
      />

      <FormInput
        label="Postal Code"
        control={control}
        name="address.postalCode"
        rules={{ required: true }}
        errors={errors}
        testID="postalCode"
      />

      <FormInput
        label="Country"
        control={control}
        name="address.country"
        rules={{ required: true }}
        errors={errors}
        testID="country"
      />

      <FormInput
        type="password"
        label="Password"
        control={control}
        name="password"
        rules={{
          required: true,
          validate: (value) => validatePassword(value),
        }}
        errors={errors}
        testID="signupPassword"
      />

      <View style={{ marginTop: 16 }}>
        <Button
          title={"Sign up"}
          onPress={handleSubmit(onValid)}
          disabled={isSubmitting}
          testID="submitSignup"
        />
        {signupError && (
          <Text style={styles.error}>Email is already registered</Text>
        )}
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    padding: 20,
    justifyContent: "center",
  },
  title: {
    fontSize: 24,
    fontWeight: "700",
    textAlign: "center",
    marginBottom: 16,
  },
  form: {
    marginBottom: 24,
    gap: 12,
  },
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 6,
    padding: 10,
    marginBottom: 10,
  },
  error: {
    color: "red",
    marginTop: 8,
    fontWeight: "600",
  },
});

export default Signup;
