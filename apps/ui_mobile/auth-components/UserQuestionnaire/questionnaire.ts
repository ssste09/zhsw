export type Choice = {
  id: string;
  label: string;
};

export type Question = {
  id: string;
  text: string;
  type: "single" | "multi";
  required?: boolean;
  choices: Choice[];
  // Optional metadata (handy later for grouping / logic)
  section?: "basics" | "energy" | "budget" | "mood_mapping" | "filters";
};

export type QuestionnaireProps = {
  id: string;
  title: string;
  questions: Question[];
};

export const QUESTIONNAIRE: QuestionnaireProps = {
  id: "preferences_1",
  title: "Your Preferences",
  questions: [
    {
      id: "activity_types",
      section: "basics",
      text: "What kinds of activities do you generally enjoy?",
      type: "multi",
      required: true,
      choices: [
        { id: "outdoor", label: "Outdoor / Nature" },
        { id: "culture", label: "Culture & Museums" },
        { id: "food", label: "Food & Drinks" },
        { id: "nightlife", label: "Nightlife" },
        { id: "fitness", label: "Sports / Fitness" },
        { id: "wellness", label: "Wellness / Relax" },
        { id: "shopping", label: "Shopping" },
        { id: "live_events", label: "Live events (concerts, theatre)" },
        { id: "social", label: "Social / Meet people" },
        { id: "quiet", label: "Quiet / Solo activities" },
      ],
    },
    {
      id: "social_vibe",
      section: "basics",
      text: "What's your usual social vibe?",
      type: "single",
      required: true,
      choices: [
        { id: "solo", label: "Solo" },
        { id: "partner", label: "With a partner" },
        { id: "small_group", label: "Small group" },
        { id: "any", label: "Any / depends" },
      ],
    },
    {
      id: "intensity",
      section: "energy",
      text: "Preferred intensity level",
      type: "single",
      required: false,
      choices: [
        { id: "low", label: "Low (chill)" },
        { id: "medium", label: "Medium" },
        { id: "high", label: "High (active/adventurous)" },
      ],
    },
    {
      id: "max_travel_time",
      section: "energy",
      text: "How far are you willing to go?",
      type: "single",
      required: false,
      choices: [
        { id: "15", label: "Up to 15 min" },
        { id: "30", label: "Up to 30 min" },
        { id: "60", label: "Up to 60 min" },
        { id: "any", label: "No preference" },
      ],
    },
    {
      id: "budget",
      section: "budget",
      text: "Typical budget for an activity",
      type: "single",
      required: false,
      choices: [
        { id: "free", label: "Free / very low" },
        { id: "budget", label: "€ (budget)" },
        { id: "mid", label: "€€ (mid)" },
        { id: "treat", label: "€€€ (treat)" },
      ],
    },
    {
      id: "preferred_time",
      section: "budget",
      text: "When do you usually prefer activities?",
      type: "multi",
      required: false,
      choices: [
        { id: "morning", label: "Morning" },
        { id: "afternoon", label: "Afternoon" },
        { id: "evening", label: "Evening" },
        { id: "night", label: "Night" },
      ],
    },
    {
      id: "stressed_help",
      section: "mood_mapping",
      text: "When you feel stressed, what helps most?",
      type: "multi",
      required: true,
      choices: [
        { id: "park_walk", label: "Walk in nature / parks" },
        { id: "spa", label: "Spa / wellness" },
        { id: "quiet_cafe", label: "Quiet café" },
        { id: "yoga", label: "Yoga / meditation" },
        { id: "museum_calm", label: "Museum / calm culture" },
        { id: "shopping", label: "Shopping" },
        { id: "comfort_food", label: "Comfort food" },
        { id: "low_stimulation", label: "Stay home / low stimulation" },
      ],
    },
    {
      id: "sad_help",
      section: "mood_mapping",
      text: "When you feel sad/low, what helps most?",
      type: "multi",
      required: true,
      choices: [
        { id: "social_activities", label: "Social activities" },
        { id: "light_outdoor_walk", label: "Light outdoor walk" },
        { id: "comfort_food", label: "Comfort food" },
        { id: "live_music_cinema", label: "Live music / cinema" },
        { id: "cute_cafes", label: "Cute cafés" },
        { id: "gym_movement", label: "Gym / movement" },
        { id: "museums_culture", label: "Museums / culture" },
      ],
    },
    {
      id: "energetic_wants",
      section: "mood_mapping",
      text: "When you feel energetic, what do you want?",
      type: "multi",
      required: true,
      choices: [
        { id: "sports_workouts", label: "Sports / workouts" },
        { id: "hiking_outdoors", label: "Hiking / outdoors" },
        { id: "nightlife", label: "Nightlife" },
        { id: "events", label: "Events" },
        { id: "new_restaurants_bars", label: "Trying new restaurants / bars" },
        { id: "explore_neighborhoods", label: "Exploring neighborhoods" },
      ],
    },
    {
      id: "food_preferences",
      section: "filters",
      text: "Food preferences",
      type: "multi",
      required: true,
      choices: [
        { id: "vegetarian", label: "Vegetarian" },
        { id: "vegan", label: "Vegan" },
        { id: "halal", label: "Halal" },
        { id: "gluten_free", label: "Gluten-free" },
        { id: "no_preference", label: "No preference" },
      ],
    },
    {
      id: "avoid",
      section: "filters",
      text: "Avoid (we'll filter these out)",
      type: "multi",
      required: false,
      choices: [
        { id: "crowded", label: "Crowded places" },
        { id: "loud", label: "Loud places" },
        { id: "alcohol_focused", label: "Alcohol-focused places" },
        { id: "long_lines", label: "Long lines" },
        {
          id: "outdoor_weather_sensitive",
          label: "Outdoor (weather sensitive)",
        },
        { id: "late_night", label: "Late-night activities" },
      ],
    },
  ],
};

export const MOOD_NOW_QUESTION: Question = {
  id: "mood_now",
  text: "How are you feeling right now?",
  type: "single",
  required: true,
  choices: [
    { id: "calm", label: "Calm" },
    { id: "stressed", label: "Stressed" },
    { id: "sad_low", label: "Sad / low" },
    { id: "happy", label: "Happy" },
    { id: "energetic", label: "Energetic" },
    { id: "bored", label: "Bored" },
  ],
};
