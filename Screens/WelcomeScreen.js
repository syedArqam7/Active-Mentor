import React from 'react';
import {
  SafeAreaView,
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Image,
  Dimensions,
} from 'react-native';
import {Color, LogoStyle} from '../GlobalStyles';
import {useNavigation} from '@react-navigation/native';

const WelcomeScreen = () => {
  const navigation = useNavigation();

  const handleGetStarted = () => {
    navigation.navigate('SignUpScreen');
  };
  const handleLogin = () => {
    navigation.navigate('LoginScreen');
  };
  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.header}>
        {/* Placeholder for any header content */}
      </View>
      <View style={styles.content}>
        <Image
          style={styles.logo}
          resizeMode="contain"
          source={require('../assets/logo2.png')}
        />
        <TouchableOpacity style={styles.button} onPress={handleGetStarted}>
          <Text style={styles.buttonText}>GET STARTED</Text>
        </TouchableOpacity>
      </View>
      <View style={styles.footer}></View>
    </SafeAreaView>
  );
};

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'black',
  },
  header: {
    flex: 1,
  },
  content: {
    flex: 5,
    justifyContent: 'center',
    alignItems: 'center',
  },
  title: {
    fontSize: 40,
    fontWeight: 'bold',
    color: 'lime',
    marginBottom: 30,
  },
  subtitle: {
    fontSize: 20,
    color: 'white',
    textAlign: 'center',
    paddingHorizontal: 30,
    marginBottom: 30,
  },
  button: {
    backgroundColor: '#EC9F05',
    paddingHorizontal: 60,
    paddingVertical: 10,
    borderRadius: 25,
    marginBottom: 15,
  },
  buttonText: {
    color: 'white',
    fontSize: 18,
    fontFamily: 'Poppins-Bold',
  },
  loginText: {
    color: 'gray',
    fontSize: 14,
  },
  footer: {
    flex: 1,
    justifyContent: 'flex-end', // Align items at the bottom of the screen
    paddingBottom: 20, // Optional: Add padding to the bottom to separate from the edge
  },
  logo: {
    width: windowWidth * 0.4,
    height: windowHeight * 0.4,
    marginBottom: 40,
  },
});

export default WelcomeScreen;
