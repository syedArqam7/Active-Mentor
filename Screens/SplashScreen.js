import * as React from "react";
import { Image, StyleSheet, View, Text } from "react-native";
import { Border, Color, FontFamily } from "../GlobalStyles";

const SplashScreen = () => {
  return (
    <View style={styles.SplashScreen}>
      <Image
        style={styles.LogoStyle}
        resizeMode="cover"
        source={require("../assets/splashLogo.png")}
      />
      <Text style={styles.yourWorkoutExpert}>Your Workout Expert</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  SplashScreen: {
    backgroundColor: Color.colorBlack,
    flex: 1,
    width: "100%",
    height: 932,
    overflow: "hidden",
  },
  LogoStyle: {
    left: "8%",
    justifyContent: "center",
    position: "absolute",
    width: "80%",
    height: "30%",
    top: 250,
    alignItems: "center",
    position: "absolute",
  },
  yourWorkoutExpert: {
    marginLeft: -102,
    top: 568,
    fontSize: 18,
    textTransform: "uppercase",
    fontStyle: "italic",
    fontWeight: "300",
    fontFamily: FontFamily.poppinsLightItalic,
    color: Color.colorWhite,
    textAlign: "center",
    left: "50%",
    position: "absolute",
  },
});

export default SplashScreen;
