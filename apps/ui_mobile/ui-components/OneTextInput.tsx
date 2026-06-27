import { FunctionComponent } from "react";
import { KeyboardTypeOptions, StyleSheet, TextInput, View } from "react-native";

interface OneTextInputProps {
  value: any;
  onChange: (value: any) => void;
  keyboardType?: KeyboardTypeOptions;
  placeholder: string;
}

const OneTextInput: FunctionComponent<OneTextInputProps> = ({
  value,
  onChange,
  keyboardType = "default",
  placeholder,
}) => {
  return (
    <View style={styles.input}>
      <TextInput
        keyboardType={keyboardType}
        value={value}
        placeholder={placeholder}
        onChange={onChange}
      />
    </View>
  );
};

const styles = StyleSheet.create({
  input: {
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 6,
    padding: 10,
    marginBottom: 10,
  },
});

export default OneTextInput;
