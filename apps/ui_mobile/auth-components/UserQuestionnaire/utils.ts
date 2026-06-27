import {
  ActivityType,
  AvoidType,
  BudgetType,
  EnergeticWantsType,
  FoodPreferencesType,
  IntensityType,
  PreferredTimeType,
  ProfileUserRequest,
  SadHelpType,
  SocialVibeType,
  StressedHelpType,
} from "@myorg/profile_api";
import { QuestionnaireAnswer } from "./types";

export const INITIAL_QUESTIONNAIRE_STATE: QuestionnaireAnswer = {
  ActivityType: undefined,
  Avoid: undefined,
  Budget: undefined,
  EnergeticWants: undefined,
  FoodPreferences: undefined,
  Intensity: undefined,
  MaxTravelTime: undefined,
  MoodNow: undefined,
  PreferredTime: undefined,
  SadHelp: undefined,
  SocialVibe: undefined,
  StressedHelp: undefined,
};

export const createProfileRequest = (
  answers: QuestionnaireAnswer,
): ProfileUserRequest => ({
  activityTypes: answers.ActivityType as ActivityType[],
  socialVibe: answers.SocialVibe as SocialVibeType,
  energeticWants: answers.EnergeticWants as EnergeticWantsType[],
  foodPreferences: answers.FoodPreferences as FoodPreferencesType[],
  sadHelp: answers.SadHelp as SadHelpType[],
  stressedHelp: answers.StressedHelp as StressedHelpType[],
  avoid: answers.Avoid as AvoidType[],
  budget: answers.Budget as BudgetType,
  intensity: answers.Intensity as IntensityType,
  maxTravelTime: answers?.MaxTravelTime as number | undefined,
  preferredTime: answers.PreferredTime as PreferredTimeType[],
});
