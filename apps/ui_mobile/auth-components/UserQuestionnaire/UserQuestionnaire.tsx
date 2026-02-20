import {
  Button,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from "react-native";
import { useState } from "react";
import { QUESTIONNAIRE, Question } from "./questionnaire";
import AsyncStorage from "@react-native-async-storage/async-storage";
import { decodeToken, questionnaireStorageKey } from "hooks/auth";
import { useNavigation } from "@react-navigation/native";

const UserQuestionnaire = () => {
  const questionnaire = QUESTIONNAIRE;

  const [answers, setAnswers] = useState<Record<string, string | string[]>>({});
  const [currentIndex, setCurrentIndex] = useState(0);
  const [error, setError] = useState<string | null>(null);

  const currentQuestion: Question = questionnaire.questions[currentIndex];
  const selected = answers[currentQuestion.id];

  const navigation = useNavigation<any>();

  const toggleChoice = (
    questionId: string,
    choiceId: string,
    type: "single" | "multi",
  ) => {
    setAnswers((prev) => {
      const current = prev[questionId];

      if (type === "single") {
        return { ...prev, [questionId]: choiceId };
      }

      const arr = Array.isArray(current) ? current : [];
      const next = arr.includes(choiceId)
        ? arr.filter((x) => x !== choiceId)
        : [...arr, choiceId];

      return { ...prev, [questionId]: next };
    });
  };

  const isAnswered = (question: Question) => {
    if (!question.required) return true;

    const value = answers[question.id];

    if (question.type === "single") {
      return typeof value === "string";
    }

    return Array.isArray(value) && value.length > 0;
  };

  const goNext = () => {
    if (!isAnswered(currentQuestion)) {
      setError("This field is required");
      return;
    }

    setError(null);

    if (currentIndex < questionnaire.questions.length - 1) {
      setCurrentIndex((i) => i + 1);
    } else {
      submitQuestionnaire();
    }
  };

  const goPrev = () => {
    setError(null);
    if (currentIndex > 0) {
      setCurrentIndex((i) => i - 1);
    }
  };

  const submitQuestionnaire = async () => {
    const userId = await decodeToken();

    if (!userId) return;

    const key = questionnaireStorageKey(userId);

    console.log("SAVING TO KEY:", key);
    console.log("SAVING ANSWERS:", answers);

    await AsyncStorage.setItem(key, JSON.stringify(answers));

    const verify = await AsyncStorage.getItem(key);
    console.log("VERIFY AFTER SAVE:", verify);

    navigation.navigate("MoodQuestion");
  };

  return (
    <ScrollView contentContainerStyle={styles.container} testID="questionnaireView">
      {/* Title */}
      <Text style={styles.title}>{questionnaire.title}</Text>

      {/* Progress */}
      <Text style={styles.progress}>
        {currentIndex + 1} / {questionnaire.questions.length}
      </Text>

      {/* Question */}
      <View style={styles.questionBlock}>
        <Text style={styles.questionText}>
          {currentIndex + 1}. {currentQuestion.text}
        </Text>

        <Text style={styles.choiceHint}>
          {currentQuestion.type === "single"
            ? "Single choice"
            : "Multiple choice"}
          {currentQuestion.required ? " • required" : " • optional"}
        </Text>

        {error && <Text style={styles.errorText}>{error}</Text>}

        {currentQuestion.choices.map((choice) => {
          const isChecked =
            currentQuestion.type === "single"
              ? selected === choice.id
              : Array.isArray(selected) && selected.includes(choice.id);

          return (
            <Pressable
              key={choice.id}
              style={styles.choiceRow}
              onPress={() =>
                toggleChoice(
                  currentQuestion.id,
                  choice.id,
                  currentQuestion.type,
                )
              }
              accessibilityRole={
                currentQuestion.type === "single" ? "radio" : "checkbox"
              }
              accessibilityState={{ checked: isChecked }}
            >
              <View
                style={[
                  styles.checkboxBox,
                  isChecked && styles.checkboxBoxChecked,
                ]}
              >
                {isChecked && <Text style={styles.checkboxTick}>✓</Text>}
              </View>

              <Text style={styles.choiceText}>{choice.label}</Text>
            </Pressable>
          );
        })}
      </View>

      {/* Navigation */}
      <View style={styles.navButtons}>
        <Button
          title="Previous"
          onPress={goPrev}
          disabled={currentIndex === 0}
        />

        <Button
          title={
            currentIndex === questionnaire.questions.length - 1
              ? "Submit"
              : "Next"
          }
          onPress={goNext}
        />
      </View>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  container: { padding: 20 },

  title: {
    fontSize: 22,
    fontWeight: "700",
    marginBottom: 6,
  },

  progress: {
    color: "#777",
    marginBottom: 16,
  },

  questionBlock: {
    marginBottom: 24,
  },

  questionText: {
    fontSize: 16,
    fontWeight: "600",
    marginBottom: 6,
  },

  choiceHint: {
    color: "#777",
    fontSize: 13,
    marginBottom: 10,
  },

  errorText: {
    color: "red",
    marginBottom: 8,
  },

  choiceRow: {
    flexDirection: "row",
    alignItems: "center",
    paddingVertical: 12,
    paddingHorizontal: 14,
    borderWidth: 1,
    borderColor: "#ddd",
    borderRadius: 12,
    marginBottom: 8,
    backgroundColor: "#fff",
  },

  checkboxBox: {
    width: 22,
    height: 22,
    borderRadius: 6,
    borderWidth: 2,
    borderColor: "#888",
    alignItems: "center",
    justifyContent: "center",
    marginRight: 12,
  },

  checkboxBoxChecked: {
    backgroundColor: "#111",
    borderColor: "#111",
  },

  checkboxTick: {
    color: "#fff",
    fontWeight: "800",
    fontSize: 14,
    lineHeight: 16,
  },

  choiceText: {
    fontSize: 15,
    color: "#111",
    flexShrink: 1,
  },

  navButtons: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 24,
  },
});

export default UserQuestionnaire;
