import { QuestionId } from "./questionnaire";

export type Answer = string | string[] | number | undefined;

export type QuestionnaireAnswer = Record<QuestionId, Answer>;
