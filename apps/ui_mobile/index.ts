console.log("DetoxConstants:", launchArguments);

import App from "App";
import { registerRootComponent } from "expo";
import { launchArguments } from "expo-launch-arguments";

// registerRootComponent calls AppRegistry.registerComponent('main', () => App);
// It also ensures that whether you load the app in Expo Go or in a native build,
// the environment is set up appropriately
registerRootComponent(App);
