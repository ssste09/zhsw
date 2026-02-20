import { useNavigation } from "@react-navigation/native";
import { removeAuthToken } from "hooks/auth";
import { Button } from "react-native";

export const Logout = () => {
  const navigation = useNavigation<any>();
  const handleLogout = async () => {
    try {
      await removeAuthToken();
      //const t = await getAuthToken();
      //console.log("token after delete", t);
      navigation.navigate("Login");
    } catch (error) {
      console.error(error);
    }
  };

  return <Button title="Logout" onPress={handleLogout} />;
};

export default Logout;
