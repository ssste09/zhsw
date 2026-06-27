import {
  Configuration,
  ProfileControllerApi,
  ProfileUserRequest,
} from "@myorg/profile_api";
import useSWRMutation from "swr/mutation";
import { getAuthToken } from "./auth";

const profileApi = new ProfileControllerApi(
  new Configuration({
    basePath: "http://localhost:8082",
    headers: {
      Authorization: `Bearer ${getAuthToken()}`,
    },
  }),
);

export const useCreateProfile = () =>
  useSWRMutation(
    { key: "profile/createProfile" },
    (_key, { arg: profileUserRequest }: { arg: ProfileUserRequest }) =>
      profileApi.createProfile({ profileUserRequest }),
  );
