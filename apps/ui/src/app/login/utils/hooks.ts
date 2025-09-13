import {
  AuthControllerApi,
  Configuration,
  LoginUserRequest
} from "@your-scope/auth-client";
import useSWRMutation from "swr/mutation";

const authApi: AuthControllerApi = new AuthControllerApi(
  new Configuration({ basePath: "http://localhost:8080" })
);

export const useLogin = () =>
  useSWRMutation("/login", (key, { arg: request }: { arg: LoginUserRequest }) =>
    authApi.login(request)
  );
