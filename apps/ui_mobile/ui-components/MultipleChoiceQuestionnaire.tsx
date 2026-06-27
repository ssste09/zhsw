import {
  Choice,
  Question,
  QuestionId,
  QuestionType,
} from "auth-components/UserQuestionnaire/questionnaire";
import { Answer } from "auth-components/UserQuestionnaire/types";
import { FunctionComponent } from "react";
import { Pressable, StyleSheet, Text, View } from "react-native";

interface MultipleChoiceQuestionnaireProps {
  choices: Choice[];
  currentQuestion: Question;
  selectedAnswers: Answer;
  onToggle: (
    questionId: QuestionId,
    choiceId: Answer,
    type: QuestionType,
  ) => void;
}

const MultipleChoiceQuestionnaire: FunctionComponent<
  MultipleChoiceQuestionnaireProps
> = ({ choices, currentQuestion, selectedAnswers, onToggle }) => {
  return choices?.map((choice) => {
    const isChecked =
      currentQuestion.type === "single"
        ? selectedAnswers === choice.id
        : Array.isArray(selectedAnswers) && selectedAnswers.includes(choice.id);
    return (
      <Pressable
        key={choice.id}
        style={styles.choiceRow}
        onPress={() =>
          onToggle(currentQuestion.id, choice.id, currentQuestion.type)
        }
        accessibilityRole={
          currentQuestion.type === "single" ? "radio" : "checkbox"
        }
        accessibilityState={{ checked: isChecked }}
      >
        <View
          style={[styles.checkboxBox, isChecked && styles.checkboxBoxChecked]}
        >
          {isChecked && <Text style={styles.checkboxTick}>✓</Text>}
        </View>

        <Text style={styles.choiceText}>{choice.label}</Text>
      </Pressable>
    );
  });
};

const styles = StyleSheet.create({
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
});

export default MultipleChoiceQuestionnaire;
