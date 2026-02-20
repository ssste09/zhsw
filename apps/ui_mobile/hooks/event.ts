import { Configuration, EventsApiControllerApi } from "@myorg/apis_api";

export const eventApi = new EventsApiControllerApi(
  new Configuration({ basePath: "http://localhost:8081" }),
);
