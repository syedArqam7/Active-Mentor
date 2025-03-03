import * as React from 'react';
import {Image, StyleSheet, View, Text, Dimensions} from 'react-native';
import {Color, FontFamily} from '../GlobalStyles';

const SplashScreen = () => {
  return (
    <View style={styles.container}>
      <Image
        style={styles.logo}
        resizeMode="contain"
        source={require('../assets/logo2.png')}
      />
      <Text style={styles.yourWorkoutExpert}>Your Workout Expert</Text>
    </View>
  );
};

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: Color.colorBlack,
    justifyContent: 'center',
    alignItems: 'center',
  },
  logo: {
    width: windowWidth * 0.4,
    height: windowHeight * 0.4, // Adjust the height as per your requirement
    marginBottom: 20,
  },
  yourWorkoutExpert: {
    fontSize: 24,
    textTransform: 'uppercase',
    fontStyle: 'italic',
    fontWeight: '400',
    fontFamily: FontFamily.poppinsLightItalic,
    color: Color.colorWhite,
    textAlign: 'center',
  },
});

export default SplashScreen;
