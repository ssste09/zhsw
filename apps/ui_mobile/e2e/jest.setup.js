// eslint-disable-next-line no-undef
jest.mock("@react-native-async-storage/async-storage", () =>
  // eslint-disable-next-line @typescript-eslint/no-require-imports
  require("@react-native-async-storage/async-storage/jest/async-storage-mock"),
);

// eslint-disable-next-line no-undef
jest.mock("@myorg/auth_api", () => ({
  login: async () => ({
    token: "e2e-token",
  }),
}));
