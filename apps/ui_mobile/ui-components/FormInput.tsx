import React from "react";
import { Picker } from "@react-native-picker/picker";
import { useState } from "react";

import {
  Control,
  Controller,
  FieldErrors,
  FieldValues,
  Path,
  UseControllerProps,
  UseFormTrigger,
} from "react-hook-form";
import {
  Modal,
  Platform,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import { PhoneInput } from "react-native-phone-entry";

interface FormInputProps<T extends FieldValues> {
  label: string;
  control: Control<T, any, T>;
  name: Path<T>;
  rules: UseControllerProps<T>["rules"];
  errors: FieldErrors<T>;
  type?:
    | "select"
    | "text"
    | "number"
    | "phoneNumber"
    | "email"
    | "password"
    | "birthDate";
  options?: { label: string; value: any }[];
  trigger?: UseFormTrigger<T>;
  setCountryCode?: (cc: string) => void;
  setCallingCode?: (ncc: string[]) => void;
  placeholder?: string;
  additionalProps?: any;
}

const FormInput = <T extends FieldValues>({
  label,
  control,
  name,
  rules,
  type = "text",
  options = [],
  trigger,
  setCountryCode,
  setCallingCode,
  placeholder,
  additionalProps,
}: FormInputProps<T>) => {
  const [open, setOpen] = useState(false);

  return (
    <>
      <Text>{label}</Text>
      <Controller
        control={control}
        name={name}
        rules={rules}
        render={({
          field: { onChange, onBlur, value },
          fieldState: { error },
        }) => {
          const errorMessage =
            (typeof error?.message === "string" && error.message.trim()) ||
            (error ? `${label} is required` : "");
          if (type === "phoneNumber") {
            return (
              <>
                <PhoneInput
                  defaultValues={{
                    countryCode: "IT",
                    callingCode: "+39",
                    phoneNumber: "+39",
                  }}
                  value={value}
                  onChangeText={(number) => {
                    onChange(number);
                  }}
                  onChangeCountry={(countryCode) => {
                    setCountryCode?.(countryCode.cca2);
                    setCallingCode?.(countryCode.callingCode);
                  }}
                  countryPickerProps={{
                    withFilter: true,
                    withFlag: true,
                    withCountryNameButton: true,
                  }}
                  maskInputProps={{
                    onBlur: () => {
                      onBlur();
                      trigger?.(name);
                    },
                  }}
                  hideDropdownIcon={false}
                  isCallingCodeEditable={false}
                />
                {!!error && <Text style={styles.error}>{errorMessage}</Text>}
              </>
            );
          }
          if (type === "select" && options?.length > 0) {
            const selectedLabel =
              options.find((i) => i.value === (value ?? ""))?.label ??
              `Select ${label}`;

            const validateNow = () => {
              onBlur(); // mark as touched
              trigger?.(name); // run validation
            };

            if (Platform.OS === "android") {
              return (
                <>
                  <View style={styles.input}>
                    <Picker
                      mode="dropdown"
                      selectedValue={value ?? ""}
                      onValueChange={(v) => {
                        onChange(v === "" ? undefined : v);
                        validateNow();
                      }}
                      placeholder={placeholder}
                    >
                      {options.map((i) => (
                        <Picker.Item
                          key={i.label}
                          label={i.label}
                          value={i.value}
                        />
                      ))}
                    </Picker>
                  </View>
                  {!!error && <Text style={styles.error}>{errorMessage}</Text>}
                </>
              );
            }

            return (
              <>
                <Pressable style={styles.input} onPress={() => setOpen(true)}>
                  <Text>{selectedLabel}</Text>
                </Pressable>

                <Modal visible={open} transparent animationType="slide">
                  <Pressable
                    style={{ flex: 1 }}
                    onPress={() => {
                      setOpen(false);
                      validateNow();
                    }}
                  />
                  <View style={{ backgroundColor: "white" }}>
                    <Pressable
                      onPress={() => {
                        setOpen(false);
                        validateNow();
                      }}
                      style={{ padding: 12, alignSelf: "flex-end" }}
                    >
                      <Text style={{ fontWeight: "600" }}>Done</Text>
                    </Pressable>

                    <Picker
                      selectedValue={value ?? ""}
                      onValueChange={(v) => {
                        onChange(v === "" ? undefined : v);
                        //trigger?.(name);
                      }}
                    >
                      {options.map((i) => (
                        <Picker.Item
                          key={i.label}
                          label={i.label}
                          value={i.value}
                        />
                      ))}
                    </Picker>
                  </View>
                </Modal>
                {!!error && <Text style={styles.error}>{errorMessage}</Text>}
              </>
            );
          }
          if (type === "email") {
            return (
              <>
                <TextInput
                  style={styles.input}
                  onChangeText={onChange}
                  onBlur={onBlur}
                  value={value}
                  autoCapitalize="none"
                  keyboardType="email-address"
                  placeholder={placeholder}
                />
                {!!error && <Text style={styles.error}>{errorMessage}</Text>}
              </>
            );
          }
          if (type === "password") {
            return (
              <>
                <TextInput
                  style={styles.input}
                  onChangeText={onChange}
                  onBlur={onBlur}
                  value={value}
                  secureTextEntry
                  placeholder={placeholder}
                />
                {!!error && <Text style={styles.error}>{errorMessage}</Text>}
              </>
            );
          }
          if (type === "birthDate") {
            return (
              <>
                <TextInput
                  style={styles.input}
                  onChangeText={onChange}
                  onBlur={onBlur}
                  value={value}
                  placeholder={placeholder ? placeholder : "YYYY-MM-DD"}
                />
                {!!error && <Text style={styles.error}>{errorMessage}</Text>}
              </>
            );
          }
          if (type === "number") {
            return (
              <>
                <TextInput
                  style={styles.input}
                  keyboardType="numeric"
                  onChangeText={(text) => onChange(Number(text) || 0)}
                  onBlur={onBlur}
                  value={String(value ?? "")}
                  placeholder={placeholder}
                />
                {!!error && <Text style={styles.error}>{errorMessage}</Text>}
              </>
            );
          }

          return (
            <>
              <TextInput
                style={styles.input}
                onChangeText={onChange}
                onBlur={onBlur}
                value={value}
                placeholder={placeholder}
                {...additionalProps}
              />
              {!!error && <Text style={styles.error}>{errorMessage}</Text>}
            </>
          );
        }}
      />
    </>
  );
};

const styles = StyleSheet.create({
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 6,
    padding: 10,
    marginBottom: 10,
  },

  pickerContainer: {
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 6,
    marginBottom: 12,
  },
  error: {
    color: "red",
    marginTop: 2,
    fontWeight: "600",
  },
});

export default FormInput;
