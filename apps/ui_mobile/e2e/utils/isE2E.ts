import { launchArguments } from "expo-launch-arguments";

export const launchArgs = launchArguments.value?.() ?? {};
export const isE2E = launchArguments.MOCK_API === "1";
