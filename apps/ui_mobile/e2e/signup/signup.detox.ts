import { expect } from "detox";

describe("Successfully Signup", () => {
  beforeAll(async () => {
    await device.launchApp({
      newInstance: true,
      delete: true,
      launchArgs: { MOCK_API: "1" },
    });
    await element(by.id("createAccount")).tap();
  });

  it("should have welcome screen", async () => {
    await expect(element(by.id("signup"))).toBeVisible();
  });

  it("Should type into fields and signup successfully", async () => {
    await element(by.id("firstName")).tap();
    await element(by.id("firstName")).typeText("Stella");

    await element(by.id("lastName")).tap();
    await element(by.id("lastName")).typeText("Vas");

    await element(by.id("signupEmail")).tap();
    await element(by.id("signupEmail")).typeText("stella@gmail.com");

    await element(by.id("phoneNumber")).tap();
    await element(by.id("phoneNumber")).replaceText("+393331234567");

    await element(by.id("birthDate")).tap();
    await element(by.id("birthDate")).typeText("1999-03-02");
    await element(by.id("birthDate")).tapReturnKey();

    await element(by.id("signup")).scrollTo("bottom");

    await element(by.id("street")).tap();
    await element(by.id("street")).typeText("My street");
    await element(by.id("street")).tapReturnKey();

    await element(by.id("streetNumber")).tap();
    await element(by.id("streetNumber")).typeText("5");
    await element(by.id("streetNumber")).tapReturnKey();

    await element(by.id("city")).tap();
    await element(by.id("city")).typeText("Zurich");
    await element(by.id("city")).tapReturnKey();

    await element(by.id("postalCode")).tap();
    await element(by.id("postalCode")).typeText("8135");
    await element(by.id("postalCode")).tapReturnKey();

    await element(by.id("country")).tap();
    await element(by.id("country")).typeText("Switzerland");
    await element(by.id("country")).tapReturnKey();

    await element(by.id("signupPassword")).tap();
    await element(by.id("signupPassword")).typeText("Random_pass182");
    await element(by.id("signupPassword")).tapReturnKey();

    await element(by.id("submitSignup")).tap();

    await waitFor(element(by.id("login"))).toBeVisible();
  });
});

describe("Signup Error", () => {
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

  it("Should type into fields and return user is already registered", async () => {
    await element(by.id("createAccount")).tap();
    await expect(element(by.id("signup"))).toBeVisible();
    await element(by.id("firstName")).tap();
    await element(by.id("firstName")).typeText("Stella");

    await element(by.id("lastName")).tap();
    await element(by.id("lastName")).typeText("Vas");

    await element(by.id("signupEmail")).tap();
    await element(by.id("signupEmail")).typeText("stella@gmail.com");

    await element(by.id("phoneNumber")).tap();
    await element(by.id("phoneNumber")).replaceText("+393331234567");

    await element(by.id("birthDate")).tap();
    await element(by.id("birthDate")).typeText("1999-03-02");
    await element(by.id("birthDate")).tapReturnKey();

    await element(by.id("signup")).scrollTo("bottom");

    await element(by.id("street")).tap();
    await element(by.id("street")).typeText("My street");
    await element(by.id("street")).tapReturnKey();

    await element(by.id("streetNumber")).tap();
    await element(by.id("streetNumber")).typeText("5");
    await element(by.id("streetNumber")).tapReturnKey();

    await element(by.id("city")).tap();
    await element(by.id("city")).typeText("Zurich");
    await element(by.id("city")).tapReturnKey();

    await element(by.id("postalCode")).tap();
    await element(by.id("postalCode")).typeText("8135");
    await element(by.id("postalCode")).tapReturnKey();

    await element(by.id("country")).tap();
    await element(by.id("country")).typeText("Switzerland");
    await element(by.id("country")).tapReturnKey();

    await element(by.id("signupPassword")).tap();
    await element(by.id("signupPassword")).typeText("Random_pass182");
    await element(by.id("signupPassword")).tapReturnKey();

    await element(by.id("submitSignup")).tap();

   await waitFor(element(by.text("Email is already registered")))
     .toBeVisible()
     .withTimeout(5000);

   await expect(element(by.id("login"))).not.toExist();

  });
});
