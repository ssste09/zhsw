import {
  ActivityType,
  AvoidType,
  BudgetType,
  EnergeticWantsType,
  FoodPreferencesType,
  IntensityType,
  PreferredTimeType,
  SadHelpType,
  SocialVibeType,
  StressedHelpType,
} from "@myorg/profile_api";

export type QuestionnaireIdType =
  | ActivityType
  | SocialVibeType
  | IntensityType
  | BudgetType
  | PreferredTimeType
  | StressedHelpType
  | SadHelpType
  | EnergeticWantsType
  | FoodPreferencesType
  | AvoidType
  | MoodType;

export type Choice = {
  id: QuestionnaireIdType;
  label: string;
};

enum MoodType {
  calm = "CALM",
  stressed = "STRESSED",
  sad_low = "SAD_LOW",
  happy = "HAPPY",
  energetic = "ENERGETIC",
  bored = "BORED",
}

export type QuestionType = "single" | "multi" | "number" | "string";

export type Question = {
  id: QuestionId;
  text: string;
  type: QuestionType;
  required?: boolean;
  choices?: Choice[];
  value?: number | string;
  // Optional metadata (handy later for grouping / logic)
  section?: "basics" | "energy" | "budget" | "mood_mapping" | "filters";
};

export type QuestionnaireProps = {
  id: string;
  title: string;
  questions: Question[];
};

export enum QuestionId {
  ActivityType = "ActivityType",
  SocialVibe = "SocialVibe",
  Intensity = "Intensity",
  MaxTravelTime = "MaxTravelTime",
  Budget = "Budget",
  PreferredTime = "PreferredTime",
  StressedHelp = "StressedHelp",
  SadHelp = "SadHelp",
  EnergeticWants = "EnergeticWants",
  FoodPreferences = "FoodPreferences",
  Avoid = "Avoid",
  MoodNow = "MoodNow",
}

export const QUESTIONNAIRE: QuestionnaireProps = {
  id: "preferences_1",
  title: "Your Preferences",
  questions: [
    {
      id: QuestionId.ActivityType,
      section: "basics",
      text: "What kinds of activities do you generally enjoy?",
      type: "multi",
      required: true,
      choices: [
        { id: ActivityType.Outdoor, label: "Outdoor / Nature" },
        { id: ActivityType.Culture, label: "Culture & Museums" },
        { id: ActivityType.FoodAndDrinks, label: "Food & Drinks" },
        { id: ActivityType.Nightlife, label: "Nightlife" },
        { id: ActivityType.Fitness, label: "Sports / Fitness" },
        { id: ActivityType.Wellness, label: "Wellness / Relax" },
        { id: ActivityType.Shopping, label: "Shopping" },
        {
          id: ActivityType.LiveEvents,
          label: "Live events (concerts, theatre)",
        },
        { id: ActivityType.Social, label: "Social / Meet people" },
        { id: ActivityType.Quiet, label: "Quiet / Solo activities" },
      ],
    },
    {
      id: QuestionId.SocialVibe,
      section: "basics",
      text: "What's your usual social vibe?",
      type: "single",
      required: true,
      choices: [
        { id: SocialVibeType.Solo, label: "Solo" },
        { id: SocialVibeType.Partner, label: "With a partner" },
        { id: SocialVibeType.SmallGroup, label: "Small group" },
        { id: SocialVibeType.Any, label: "Any / depends" },
      ],
    },
    {
      id: QuestionId.Intensity,
      section: "energy",
      text: "Preferred intensity level",
      type: "single",
      required: false,
      choices: [
        { id: IntensityType.Low, label: "Low (chill)" },
        { id: IntensityType.Medium, label: "Medium" },
        { id: IntensityType.High, label: "High (active/adventurous)" },
      ],
    },
    {
      id: QuestionId.MaxTravelTime,
      section: "energy",
      text: "How far are you willing to go?",
      type: "number",
      required: false,
    },
    {
      id: QuestionId.Budget,
      section: "budget",
      text: "Typical budget for an activity",
      type: "single",
      required: false,
      choices: [
        { id: BudgetType.Free, label: "Free / very low" },
        { id: BudgetType.Cheap, label: "€ (budget)" },
        { id: BudgetType.Mid, label: "€€ (mid)" },
        { id: BudgetType.Expensive, label: "€€€ (treat)" },
      ],
    },
    {
      id: QuestionId.PreferredTime,
      section: "budget",
      text: "When do you usually prefer activities?",
      type: "multi",
      required: false,
      choices: [
        { id: PreferredTimeType.Morning, label: "Morning" },
        { id: PreferredTimeType.Afternoon, label: "Afternoon" },
        { id: PreferredTimeType.Evening, label: "Evening" },
        { id: PreferredTimeType.Night, label: "Night" },
      ],
    },
    {
      id: QuestionId.StressedHelp,
      section: "mood_mapping",
      text: "When you feel stressed, what helps most?",
      type: "multi",
      required: true,
      choices: [
        { id: StressedHelpType.Walk, label: "Walk in nature / parks" },
        { id: StressedHelpType.Spa, label: "Spa / wellness" },
        { id: StressedHelpType.Cafe, label: "Quiet café" },
        { id: StressedHelpType.Yoga, label: "Yoga / meditation" },
        { id: StressedHelpType.Museum, label: "Museum / calm culture" },
        { id: StressedHelpType.Shopping, label: "Shopping" },
        { id: StressedHelpType.ComfortFood, label: "Comfort food" },
        { id: StressedHelpType.Home, label: "Stay home / low stimulation" },
      ],
    },
    {
      id: QuestionId.SadHelp,
      section: "mood_mapping",
      text: "When you feel sad/low, what helps most?",
      type: "multi",
      required: true,
      choices: [
        { id: SadHelpType.SocialActivities, label: "Social activities" },
        { id: SadHelpType.OutdoorWalk, label: "Light outdoor walk" },
        { id: SadHelpType.ComfortFood, label: "Comfort food" },
        { id: SadHelpType.LiveMusicCinema, label: "Live music / cinema" },
        { id: SadHelpType.Cafe, label: "Cute cafés" },
        { id: SadHelpType.Sport, label: "Gym / movement" },
        { id: SadHelpType.Culture, label: "Museums / culture" },
      ],
    },
    {
      id: QuestionId.EnergeticWants,
      section: "mood_mapping",
      text: "When you feel energetic, what do you want?",
      type: "multi",
      required: true,
      choices: [
        { id: EnergeticWantsType.Sport, label: "Sports / workouts" },
        { id: EnergeticWantsType.OutdoorsHiking, label: "Hiking / outdoors" },
        { id: EnergeticWantsType.Nightlife, label: "Nightlife" },
        { id: EnergeticWantsType.Events, label: "Events" },
        {
          id: EnergeticWantsType.RestaurantsBars,
          label: "Trying new restaurants / bars",
        },
        { id: EnergeticWantsType.Exploring, label: "Exploring neighborhoods" },
      ],
    },
    {
      id: QuestionId.FoodPreferences,
      section: "filters",
      text: "Food preferences",
      type: "multi",
      required: true,
      choices: [
        { id: FoodPreferencesType.Vegetarian, label: "Vegetarian" },
        { id: FoodPreferencesType.Vegan, label: "Vegan" },
        { id: FoodPreferencesType.Halal, label: "Halal" },
        { id: FoodPreferencesType.GlutenFree, label: "Gluten-free" },
        { id: FoodPreferencesType.None, label: "No preference" },
      ],
    },
    {
      id: QuestionId.Avoid,
      section: "filters",
      text: "Avoid (we'll filter these out)",
      type: "multi",
      required: false,
      choices: [
        { id: AvoidType.Crowded, label: "Crowded places" },
        { id: AvoidType.Loud, label: "Loud places" },
        { id: AvoidType.Alcohol, label: "Alcohol-focused places" },
        { id: AvoidType.LongLines, label: "Long lines" },
        {
          id: AvoidType.OutdoorWeatherSensitive,
          label: "Outdoor (weather sensitive)",
        },
        { id: AvoidType.LateNight, label: "Late-night activities" },
      ],
    },
  ],
};

export const MOOD_NOW_QUESTION: Question = {
  id: QuestionId.MoodNow,
  text: "How are you feeling right now?",
  type: "single",
  required: true,
  choices: [
    { id: MoodType.calm, label: "Calm" },
    { id: MoodType.stressed, label: "Stressed" },
    { id: MoodType.sad_low, label: "Sad / low" },
    { id: MoodType.happy, label: "Happy" },
    { id: MoodType.energetic, label: "Energetic" },
    { id: MoodType.bored, label: "Bored" },
  ],
};
