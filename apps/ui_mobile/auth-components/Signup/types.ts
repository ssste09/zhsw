import { SignUpUserRequest } from "@myorg/auth_api";

export interface SignUpUserFormValues
  extends Omit<SignUpUserRequest, "birthDate"> {
  birthDate: string;
}
