import {
  SafeAreaView,
  StyleSheet,
  Text,
  TextInput,
  TouchableOpacity,
  View,
  Image,
  KeyboardAvoidingView,
  Platform,
  Alert,
} from 'react-native';
import React, {useState} from 'react';
import Spacing from '../constants/Spacing';
import FontSize from '../constants/FontSize';
import Colors from '../constants/Colors';
import Font from '../constants/Font';

import {useNavigation} from '@react-navigation/native';

const LoginScreen = () => {
  const [focused, setFocused] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  // const [isGuest, setIsGuest] = useState(false);

  const [emailError, setEmailError] = useState('');
  const [passwordError, setPasswordError] = useState('');

  const navigation = useNavigation();

  const handleRegister = () => {
    navigation.navigate('SignUpScreen');
  };

  const handleLogin = async (isGuest) => {
    // Reset previous error messages
    if (isGuest) {
      navigation.navigate('ExploreScreen', {name: 'Guest'});
      return;
    }
    setEmailError('');
    setPasswordError('');

    // Basic form validation
    let isValid = true;

    if (!email) {
      setEmailError('Please enter your email');
      isValid = false;
    }
    if (!password) {
      setPasswordError('Please enter your password');
      isValid = false;
    }

    // Further validation for email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (email && !emailRegex.test(email)) {
      setEmailError('Please enter a valid email address');
      isValid = false;
    }

    const loginData = {
      email: email,
      password: password,
    };
    try {
      const response = await fetch(
        'https://server-teal-eight.vercel.app/api/users/login',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(loginData),
        },
      );

      if (response.ok) {
        const responseData = await response.json();

        Alert.alert('Login successful');
        navigation.navigate('ExploreScreen', {name: responseData.name}); // Pass name to ExploreScreen
      } else {
        console.error('Login failed:', await response.json());
      }
    } catch (error) {
      console.error('Error during login:', error);
    }
  };

  return (
    <SafeAreaView style={{flex: 1, backgroundColor: '#011'}}>
      <KeyboardAvoidingView
        behavior={Platform.OS === 'ios' ? 'padding' : 'height'}
        style={{
          flex: 1,
          justifyContent: 'space-between',
          padding: Spacing * 2,
        }}>
        <View>
          {/* Existing content until "Forgot your password?" */}
          <View style={{alignItems: 'center'}}>
            <Text
              style={{
                fontSize: FontSize.large,
                color: Colors.text,
                fontFamily: 'Poppins-Regular',
              }}>
              Hey there,
            </Text>
            <Text
              style={{
                fontFamily: 'Poppins-Bold',
                fontSize: FontSize.xLarge,
                maxWidth: '80%',
                textAlign: 'center',
                color: '#fff',
              }}>
              Welcome Back
            </Text>
          </View>

          <View style={{marginVertical: Spacing}}>
            {/* Email Input */}
            <View
              style={[
                {
                  flexDirection: 'row',
                  alignItems: 'center',
                  fontFamily: 'Poppins-Regular',
                  fontSize: FontSize.small,
                  paddingLeft: 10,
                  backgroundColor: Colors.lightPrimary,
                  borderRadius: 14,
                  marginVertical: Spacing,
                },
                focused && {
                  borderWidth: 3,
                  shadowOffset: {width: 4, height: Spacing},
                  shadowColor: Colors.primary,
                  shadowOpacity: 0.2,
                  shadowRadius: Spacing,
                },
              ]}>
              <Image
                source={require('../assets/Message.png')}
                style={{width: 20, height: 20, marginRight: 10}}
              />
              <TextInput
                onFocus={() => setFocused(true)}
                onBlur={() => setFocused(false)}
                placeholderTextColor={Colors.darkText}
                placeholder="Email"
                keyboardType="email-address"
                value={email}
                onChangeText={(text) => setEmail(text)} // Update the email state
                style={{flex: 1}}
              />
            </View>
            <View>
              <Text style={{color: 'red'}}>{emailError}</Text>
            </View>

            {/* Password Input */}
            <View
              style={[
                {
                  flexDirection: 'row',
                  alignItems: 'center',
                  fontFamily: 'Poppins-Regular',
                  fontSize: FontSize.small,
                  paddingLeft: 10,
                  backgroundColor: Colors.lightPrimary,
                  borderRadius: 14,
                  marginVertical: Spacing,
                },
                focused && {
                  borderWidth: 3,
                  shadowOffset: {width: 4, height: Spacing},
                  shadowColor: Colors.primary,
                  shadowOpacity: 0.2,
                  shadowRadius: Spacing,
                },
              ]}>
              <Image
                source={require('../assets/Lock.png')}
                style={{width: 20, height: 20, marginRight: 10}}
              />
              <TextInput
                onFocus={() => setFocused(true)}
                onBlur={() => setFocused(false)}
                placeholderTextColor={Colors.darkText}
                placeholder="Password"
                secureTextEntry
                value={password}
                onChangeText={(text) => setPassword(text)} // Update the password state
                style={{flex: 1}}
              />
            </View>
            <View>
              <Text style={{color: 'red'}}>{passwordError}</Text>
            </View>
          </View>

          <View>
            <Text
              style={{
                fontFamily: 'Poppins-Regular',
                fontSize: FontSize.small,
                color: Colors.text,
                alignSelf: 'center',
                textDecorationLine: 'underline',
              }}>
              Forgot your password?
            </Text>
          </View>
          
          <View style={{marginVertical: Spacing}}></View>
          
          <View style={{flexDirection: 'row', alignItems: 'center', justifyContent: 'center'}}>
            <TouchableOpacity
              onPress={() => handleLogin(true)}
              style={{
                marginLeft: 5,
                alignItems: 'center',
                justifyContent: 'center',
              }}>
            <Text
              style={{
                fontFamily: 'Poppins-Regular',
                fontSize: FontSize.small,
                color: Colors.text,
                textDecorationLine: 'underline',
              }}>
              Continue as a guest?
            </Text>
            </TouchableOpacity>
          </View>
        </View>

        {/* Move down content */}
        <View>
          <TouchableOpacity
            onPress={() => handleLogin(false)}
            style={{
              padding: Spacing,
              backgroundColor: Colors.primary,
              marginVertical: Spacing * 3,
              borderRadius: 50,
              shadowColor: Colors.primary,
              shadowOffset: {
                width: 0,
                height: Spacing,
              },
              shadowOpacity: 0.3,
              shadowRadius: Spacing,
            }}>
            <View
              style={{
                flexDirection: 'row',
                alignItems: 'center',
                justifyContent: 'center',
              }}>
              <Image
                source={require('../assets/loginButton.png')}
                style={{width: 20, height: 20, marginRight: 10}}
              />
              <Text
                style={{
                  fontFamily: 'Poppins-Bold',
                  color: Colors.onPrimary,
                  fontSize: 20,
                  marginTop: 5,
                }}>
                Login
              </Text>
            </View>
          </TouchableOpacity>

          <View
            style={{
              marginVertical: Spacing * 2,
              flexDirection: 'row',
              alignItems: 'center',
              justifyContent: 'center',
            }}>
            <View
              style={{
                flex: 1,
                height: 1,
                backgroundColor: Colors.text,
                marginRight: Spacing / 2,
              }}
            />
            <Text
              style={{
                fontFamily: 'Poppins-Regular',
                color: Colors.text,
                textAlign: 'center',
                fontSize: FontSize.small,
                marginHorizontal: Spacing / 2,
              }}>
              Or
            </Text>
            <View
              style={{
                flex: 1,
                height: 1,
                backgroundColor: Colors.text,
                marginLeft: Spacing / 2,
              }}
            />
          </View>

          <View style={{flexDirection: 'row', justifyContent: 'center'}}>
            <Text
              style={{
                color: 'white',
                fontSize: FontSize.small,
                fontFamily: 'Poppins-Regular',
              }}>
              Don't have an account?
            </Text>
            <TouchableOpacity onPress={handleRegister}>
              <Text
                style={{
                  fontFamily: 'Poppins-Regular',
                  color: '#EC9F05',
                  paddingLeft: 4,
                  fontSize: FontSize.small,
                }}>
                Register
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
};

export default LoginScreen;
