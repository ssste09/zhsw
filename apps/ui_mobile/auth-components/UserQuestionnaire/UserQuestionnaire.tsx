import { Button, ScrollView, StyleSheet, Text, View } from "react-native";
import { useState } from "react";
import {
  QUESTIONNAIRE,
  Question,
  QuestionId,
  QuestionType,
} from "./questionnaire";
import { useNavigation } from "@react-navigation/native";
import { Answer, QuestionnaireAnswer } from "./types";
import { useCreateProfile } from "hooks/profile";
import { createProfileRequest, INITIAL_QUESTIONNAIRE_STATE } from "./utils";
import MultipleChoiceQuestionnaire from "ui-components/MultipleChoiceQuestionnaire";
import OneTextInput from "ui-components/OneTextInput";

const UserQuestionnaire = () => {
  const [answers, setAnswers] = useState<QuestionnaireAnswer>(
    INITIAL_QUESTIONNAIRE_STATE,
  );
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [validationError, setValidationError] = useState<string | null>(null);

  const { trigger: submitProfile } = useCreateProfile();

  const currentQuestion: Question =
    QUESTIONNAIRE.questions[currentQuestionIndex];
  const selected = answers[currentQuestion.id];

  const navigation = useNavigation<any>();

  const toggleChoice = (
    questionId: QuestionId,
    choiceId: Answer,
    type: QuestionType,
  ) => {
    setAnswers((prev) => {
      const current = prev[questionId];

      if (type === "single") {
        return { ...prev, [questionId]: choiceId };
      }

      if (!choiceId) {
        return { ...prev };
      }

      const currentAnswers = Array.isArray(current) ? current : [];
      const toggledAnswer = currentAnswers.includes(choiceId as string)
        ? currentAnswers.filter((currentAnswer) => currentAnswer !== choiceId)
        : [...currentAnswers, choiceId];

      return { ...prev, [questionId]: toggledAnswer };
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
      setValidationError("This field is required");
      return;
    }

    setValidationError(null);

    if (currentQuestionIndex < QUESTIONNAIRE.questions.length - 1) {
      setCurrentQuestionIndex((i) => i + 1);
    } else {
      submitQuestionnaire();
    }
  };

  const goPrev = () => {
    setValidationError(null);
    if (currentQuestionIndex > 0) {
      setCurrentQuestionIndex((i) => i - 1);
    }
  };

  const submitQuestionnaire = async () => {
    const submitProfileRequest = createProfileRequest(answers);
    console.log("request", submitProfileRequest);
    submitProfile(submitProfileRequest).then((response) =>
      console.log("response", response.profileType),
    );
    navigation.navigate("MoodQuestion");
  };

  return (
    <ScrollView
      contentContainerStyle={styles.container}
      testID="questionnaireView"
    >
      {/* Title */}
      <Text style={styles.title}>{QUESTIONNAIRE.title}</Text>

      {/* Progress */}
      <Text style={styles.progress}>
        {`${currentQuestionIndex + 1} / ${QUESTIONNAIRE.questions.length}`}
      </Text>

      {/* Question */}
      <View style={styles.questionBlock}>
        <Text style={styles.questionText}>
          {`${currentQuestionIndex + 1}. ${currentQuestion.text}`}
        </Text>

        <Text style={styles.choiceHint}>
          {`${
            currentQuestion.type === "multi"
              ? "You can select more than one option • "
              : currentQuestion.type === "single"
                ? "You can only select one option • "
                : ""
          }
            ${currentQuestion.required ? "Required" : "Optional"}`}
        </Text>

        {validationError && (
          <Text style={styles.errorText}>{validationError}</Text>
        )}
        {currentQuestion.type !== "number" ? (
          <MultipleChoiceQuestionnaire
            choices={currentQuestion.choices || []}
            currentQuestion={currentQuestion}
            onToggle={toggleChoice}
            selectedAnswers={selected}
          />
        ) : (
          <OneTextInput
            value={answers.MaxTravelTime?.toString()}
            onChange={(value) =>
              setAnswers((prev) => ({ ...prev, MaxTravelTime: value }))
            }
            keyboardType="numeric"
            placeholder="Please enter the time in minutes"
          />
        )}
      </View>

      {/* Navigation */}
      <View style={styles.navButtons}>
        <Button
          title="Previous"
          onPress={goPrev}
          disabled={currentQuestionIndex === 0}
        />

        <Button
          title={
            currentQuestionIndex === QUESTIONNAIRE.questions.length - 1
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
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 6,
    padding: 10,
    marginBottom: 10,
  },

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
