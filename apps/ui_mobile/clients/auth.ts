import {
  AuthControllerApi,
  Configuration,
  LoginUserRequest,
  SignUpUserRequest,
} from "@myorg/auth_api";
import { isE2E } from "../e2e/utils/isE2E";

// ---- MOCK IMPLEMENTATION ----
const mockAuthApi = {
  async login({ loginUserRequest }: { loginUserRequest: LoginUserRequest }) {
    return {
      token: "e2e.fake.jwt.token",
      user: { id: "1", email: loginUserRequest.email },
    };
  },

  async signup({
    signUpUserRequest,
  }: {
    signUpUserRequest: SignUpUserRequest;
  }) {
    return signUpUserRequest;
  },
};

// ---- REAL IMPLEMENTATION ----
const realAuthApi = new AuthControllerApi(
  new Configuration({ basePath: "http://localhost:8080" }),
);

// ---- EXPORT SWITCHED CLIENT ----
export const authApi = isE2E ? mockAuthApi : realAuthApi;
console.log("MOCK_API flag:", isE2E);
