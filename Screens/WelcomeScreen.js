import React from 'react';
import { SafeAreaView, View, Text, StyleSheet, TouchableOpacity, Image } from 'react-native';
import { Color, LogoStyle } from '../GlobalStyles';
import { useNavigation } from '@react-navigation/native';

const WelcomeScreen = () => {
    const navigation = useNavigation();

    const handleGetStarted = () => {
        navigation.navigate('CreateProfile');
    };
    return (
        <SafeAreaView style={styles.container}>
            <View style={styles.header}>
                {/* Placeholder for any header content */}
            </View>
            <View style={styles.content}>
                <Image
                    style={styles.logo}
                    resizeMode="cover"
                    source={require("../assets/splashLogo.png")}
                />
                <Text style={styles.subtitle}>Great Health Is The Key Of Great Success</Text>
                <TouchableOpacity style={styles.button} onPress={handleGetStarted}>
                    <Text style={styles.buttonText}>GET STARTED</Text>
                </TouchableOpacity>
                <Text style={styles.loginText}>Already have an account? <Text style={{ color: Color.colorGreen }}>Log in</Text></Text>
            </View>
            <View style={styles.footer}>
                {/* Placeholder for any footer content */}
            </View>
        </SafeAreaView>
    );
};

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
    backgroundColor: Color.colorGreen,
    paddingHorizontal: 60,
    paddingVertical: 10,
    borderRadius: 25,
    marginBottom: 15,
  },
  buttonText: {
    color: 'white',
    fontWeight: 'bold',
    fontSize: 18,
  },
  loginText: {
    color: 'gray',
    fontSize: 14,
  },
  footer: {
    flex: 1,
  },
  logo: {
    width: 200,
    height: 200,
    marginBottom: 30,
  },
});

export default WelcomeScreen;
