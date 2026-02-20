import { expect } from "detox";

describe("Successfully Login", () => {
  beforeAll(async () => {
    jest.mock("@react-native-async-storage/async-storage", () =>
      // eslint-disable-next-line @typescript-eslint/no-require-imports
      require("@react-native-async-storage/async-storage/jest/async-storage-mock"),
    );
    await device.launchApp({
      newInstance: true,
      delete: true,
      launchArgs: { MOCK_API: "1" },
    });
  });

  beforeEach(async () => {
    await device.reloadReactNative();
  });

  it("should have welcome screen", async () => {
    await expect(element(by.text("Welcome"))).toBeVisible();
  });

  it("Should type into fields and login successfully", async () => {
    await element(by.id("email")).tap();
    await element(by.id("email")).typeText("stella@gamil.com");
    await element(by.id("password")).tap();
    await element(by.id("password")).typeText("Random_password");
    await element(by.id("submitLogin")).tap();

    await waitFor(element(by.id("questionnaireView")))
      .toExist()
      .withTimeout(5000);
  });
});

describe("Login Error", () => {
  beforeAll(async () => {
    jest.mock("@react-native-async-storage/async-storage", () =>
      // eslint-disable-next-line @typescript-eslint/no-require-imports
      require("@react-native-async-storage/async-storage/jest/async-storage-mock"),
    );
    await device.launchApp({
      newInstance: true,
      delete: true,
    });
  });

  beforeEach(async () => {
    await device.reloadReactNative();
  });

  it("Should type into fields and return invalid credentials", async () => {
    await element(by.id("email")).tap();
    await element(by.id("email")).typeText("stella@gamil.com");
    await element(by.id("password")).tap();
    await element(by.id("password")).typeText("Random_password");
    await element(by.id("submitLogin")).tap();

    await waitFor(element(by.id("questionnaireView")))
      .not.toExist()
      .withTimeout(5000);

    await waitFor(element(by.text("Invalid credentials")))
      .toExist()
      .withTimeout(2000);
  });
});
