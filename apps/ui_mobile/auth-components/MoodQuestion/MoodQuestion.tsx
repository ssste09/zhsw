import { useState } from "react";
import { Pressable, StyleSheet, Text, View } from "react-native";
import { MOOD_NOW_QUESTION } from "../UserQuestionnaire/questionnaire";
import { useNavigation } from "@react-navigation/native";

const MOOD_EMOJI: Record<string, string> = {
  calm: "😌",
  stressed: "😖",
  sad_low: "😔",
  happy: "😊",
  energetic: "⚡️",
  bored: "😐",
};

const MoodQuestion = () => {
  const navigation = useNavigation<any>();
  const [selected, setSelected] = useState("");

  const handlePress = (choiceId: string) => {
    setSelected(choiceId);
    navigation.navigate("Location");
    console.log(selected);
  };

  return (
    <View style={styles.container}>
      <Text style={styles.questionText}>{MOOD_NOW_QUESTION.text}</Text>

      <View style={styles.grid}>
        {MOOD_NOW_QUESTION.choices.map((choice) => {
          return (
            <Pressable
              key={choice.id}
              onPress={() => handlePress(choice.id)}
              style={[
                styles.moodCard,
                selected === choice.id && styles.moodCardSelected,
              ]}
            >
              <Text
                style={[
                  styles.label,
                  selected === choice.id && styles.labelSelected,
                ]}
              >
                {choice.label}
              </Text>

              <Text style={styles.emoji}>{MOOD_EMOJI[choice.id]}</Text>
            </Pressable>
          );
        })}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    padding: 20,
  },

  questionText: {
    fontSize: 20,
    fontWeight: "700",
    marginBottom: 20,
    textAlign: "center",
  },

  grid: {
    flexDirection: "row",
    flexWrap: "wrap",
    justifyContent: "space-between",
  },

  moodCard: {
    width: "48%",
    paddingVertical: 16,
    marginBottom: 14,
    borderRadius: 16,
    borderWidth: 1,
    borderColor: "#ddd",
    alignItems: "center",
    backgroundColor: "#fff",
  },

  moodCardSelected: {
    borderColor: "#111",
    backgroundColor: "#111",
  },

  label: {
    fontSize: 14,
    color: "#555",
    marginBottom: 8,
  },

  labelSelected: {
    color: "#fff",
  },

  emoji: {
    fontSize: 36,
  },
});

export default MoodQuestion;
