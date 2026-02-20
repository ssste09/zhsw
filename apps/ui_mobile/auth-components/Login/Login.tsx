import { useNavigation } from "@react-navigation/native";
import { storeAuthToken, useLogin } from "hooks/auth";
import React, { useState } from "react";
import {
  StyleSheet,
  Text,
  TextInput,
  View,
  Button,
  KeyboardAvoidingView,
  ScrollView,
  Platform,
} from "react-native";

export const Login = () => {
  const navigation = useNavigation<any>();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState(false);
  const { trigger } = useLogin();

  const handleSubmit = async () => {
    try {
      const data = await trigger({ email, password });
      const token = data.token;

      if (!token) {
        console.error("No token returned from signup");
        setError(true);
        return;
      }

      await storeAuthToken(token);
      //const storedToken = await getAuthToken();
      //console.log("Token from SecureStore:", storedToken);

      console.log("Logged in!", token);

      navigation.navigate("UserQuestionnaire");
    } catch (err: any) {
      console.error("errorLogins", err);
      setError(true);
    }
  };

  return (
    <KeyboardAvoidingView
      style={{ flex: 1 }}
      behavior={Platform.OS === "ios" ? "padding" : undefined}
    >
      <ScrollView
        contentContainerStyle={styles.container}
        keyboardShouldPersistTaps="handled"
      >
        <Text style={styles.title}>Welcome</Text>

        <View style={styles.form}>
          <Text style={styles.header}>Login</Text>

          <TextInput
            style={styles.input}
            placeholder="Email"
            value={email}
            onChangeText={setEmail}
            autoCapitalize="none"
            textContentType="emailAddress"
            testID="email"
          />

          <TextInput
            style={styles.input}
            placeholder="Password"
            value={password}
            onChangeText={setPassword}
            testID="password"
            secureTextEntry
          />

          <Button title="Submit" onPress={handleSubmit} testID="submitLogin" />

          {error && <Text style={styles.error}>Invalid credentials</Text>}
        </View>

        <View style={styles.switch}>
          <Text>Don't have an account?</Text>
          <Button
            title="Create Account"
            onPress={() => navigation.navigate("Signup")}
          />
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
    fontSize: 28,
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

export default Login;
