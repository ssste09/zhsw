import {
  Configuration,
  LocationApiControllerApi,
  LocationGETRequest,
} from "@myorg/apis_api";
import useSWRMutation from "swr/mutation";

const locationApi = new LocationApiControllerApi(
  new Configuration({ basePath: "http://localhost:8081" }),
);

export const useLocation = () =>
  useSWRMutation(
    { key: "login" },
    (_key, { arg: locationGetRequest }: { arg: LocationGETRequest }) =>
      locationGetRequest.city || locationGetRequest.postalCode
        ? locationApi.locationGET(locationGetRequest)
        : undefined,
  );
