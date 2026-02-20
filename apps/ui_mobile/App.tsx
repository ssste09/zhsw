import React from "react";
import { createStaticNavigation } from "@react-navigation/native";
import { createNativeStackNavigator } from "@react-navigation/native-stack";
import Login from "auth-components/Login/Login";
import Signup from "auth-components/Signup/Signup";
import Logout from "auth-components/Logout/Logout";

import { SafeAreaProvider, SafeAreaView } from "react-native-safe-area-context";
import UserQuestionnaire from "auth-components/UserQuestionnaire/UserQuestionnaire";
import MoodQuestion from "auth-components/MoodQuestion/MoodQuestion";
import Home from "events-components/Home/Home";
import LocationForm from "events-components/LocationForm/LocationForm";

const RootStack = createNativeStackNavigator({
  screens: {
    Login: {
      screen: Login,
      options: { title: "Login" },
    },
    Signup: {
      screen: Signup,
      options: { title: "Signup" },
    },
    Logout: {
      screen: Logout,
      options: { title: "Logout" },
    },
    UserQuestionnaire: {
      screen: UserQuestionnaire,
      options: { title: "Questionnaire" },
    },
    MoodQuestion: {
      screen: MoodQuestion,
      options: { title: "Mood" },
    },
    Location: {
      screen: LocationForm,
      options: { title: "Location" },
    },
    Home: {
      screen: Home,
      options: { title: "Home" },
    },
  },
});

const Navigation = createStaticNavigation(RootStack);

export const App = () => (
  <SafeAreaProvider>
    <SafeAreaView style={{ flex: 1 }}>
      <Navigation />
    </SafeAreaView>
  </SafeAreaProvider>
);

export default App;
