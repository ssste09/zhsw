import { LoginUserRequest, SignUpUserRequest } from "@myorg/auth_api";
import useSWRMutation from "swr/mutation";
import * as SecureStore from "expo-secure-store";
import { jwtDecode } from "jwt-decode";
import { authApi } from "clients/auth";

export const storeAuthToken = (token: string) => {
  try {
    SecureStore.setItem("auth", token);
  } catch (error) {
    console.error("Failed to store token securely", error);
  }
};

export const getAuthToken = () => {
  try {
    const token = SecureStore.getItem("auth");
    return token;
  } catch (error) {
    console.error("Failed to retrieve token", error);
    return null;
  }
};

export const removeAuthToken = async () => {
  try {
    await SecureStore.deleteItemAsync("auth");
  } catch (error) {
    console.error("Failed to delete token", error);
  }
};

export const useLogin = () =>
  useSWRMutation(
    { key: "login" },
    (_key, { arg: loginUserRequest }: { arg: LoginUserRequest }) =>
      authApi.login({ loginUserRequest }),
  );

export const useSignUp = () =>
  useSWRMutation(
    { key: "signup" },
    (_key, { arg: signUpUserRequest }: { arg: SignUpUserRequest }) =>
      authApi.signup({ signUpUserRequest }),
  );

export const decodeToken = async (): Promise<string | null> => {
  const token = getAuthToken();
  if (!token) return null;

  const decoded = jwtDecode<{ sub?: string }>(token);
  return decoded.sub ?? null;
};

export const questionnaireStorageKey = (userId: string) =>
  `questionnaire_answers:${userId}`;
