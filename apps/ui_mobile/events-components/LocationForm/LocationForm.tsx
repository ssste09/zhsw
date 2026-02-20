import { LocationGETRequest } from "@myorg/apis_api";
import { useNavigation } from "@react-navigation/native";
import { useLocation } from "hooks/location";
import { LocationFormValues } from "./types";
import { FunctionComponent } from "react";
import { useForm } from "react-hook-form";
import {
  Button,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from "react-native";
import FormInput from "ui-components/FormInput";

const LocationForm: FunctionComponent = () => {
  const navigation = useNavigation<any>();
  const { trigger: triggerLocation } = useLocation();

  const onValid = async (values: LocationFormValues) => {
    if (values.postalCode.length < 4) {
      values.postalCode = "^" + values.postalCode;
    }

    if (values.city.length < 10) {
      values.city = "^" + values.city;
    }

    const payload: LocationGETRequest = { ...values };
    await triggerLocation(payload);
    navigation.navigate("Home");
  };

  const {
    control,
    handleSubmit,
    formState: { errors },
  } = useForm<LocationFormValues>({
    mode: "onBlur",
    defaultValues: {
      postalCode: "",
      city: "",
    },
  });

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === "ios" ? "padding" : undefined}
    >
      <ScrollView
        contentContainerStyle={styles.container}
        keyboardShouldPersistTaps="handled"
      >
        <Text style={styles.title}>Enter your location</Text>

        <View style={styles.form}>
          <FormInput
            label="Postal Code"
            name="postalCode"
            type="select"
            placeholder="Postal Code (PLZ)"
            rules={{ required: false }}
            control={control}
            errors={errors}
          />

          <FormInput
            label="City"
            name="city"
            type="select"
            placeholder="City"
            rules={{ required: false }}
            control={control}
            errors={errors}
          />

          <Button title="Search" onPress={handleSubmit(onValid)} />
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
  container: {
    flexGrow: 1,
    padding: 20,
    justifyContent: "center",
  },
  title: {
    fontSize: 22,
    fontWeight: "700",
    textAlign: "center",
    marginBottom: 24,
  },
  form: {
    marginBottom: 24,
    gap: 12,
  },
  header: {
    fontSize: 20,
    fontWeight: "600",
    marginBottom: 8,
  },
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 6,
    padding: 10,
    marginBottom: 10,
  },
  switch: {
    alignItems: "center",
    gap: 8,
  },
  error: {
    color: "red",
    marginTop: 8,
    fontWeight: "600",
  },
});

export default LocationForm;
