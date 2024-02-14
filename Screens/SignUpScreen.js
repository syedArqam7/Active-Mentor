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

const SignUpScreen = () => {
  const [focused, setFocused] = useState(false);
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nameError, setNameError] = useState('');
  const [emailError, setEmailError] = useState('');
  const [passwordError, setPasswordError] = useState('');
  const [isCheckboxChecked, setCheckboxChecked] = useState(false);

  const navigation = useNavigation();

  const handleLogin = () => {
    navigation.navigate('LoginScreen');
  };

  const handleRegister = async () => {
    // Reset previous error messages
    setNameError('');
    setEmailError('');
    setPasswordError('');

    // Basic form validation
    let isValid = true;
    if (!name) {
      setNameError('Please enter your name');
      isValid = false;
    }
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

    if (isValid) {
      // Proceed with registration
      try {
        const registrationData = {
          name: name,
          email: email,
          password: password,
        };

        const response = await fetch(
          'http://192.168.100.15:3000/api/users/register',
          {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(registrationData),
          },
        );

        if (response.ok) {
          Alert.alert('Registration successful');
          navigation.navigate('LoginScreen', {registrationData});
        } else {
          console.error('Registration failed:', await response.json());
        }
      } catch (error) {
        console.error('Error during registration:', error);
      }
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
                fontSize: FontSize.large,
                maxWidth: '80%',
                textAlign: 'center',
                color: '#fff',
              }}>
              Create an Account
            </Text>
          </View>

          <View style={{marginVertical: Spacing}}>
            {/* name Input */}
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
                source={require('../assets/Profile.png')}
                style={{width: 20, height: 20, marginRight: 10}}
              />
              <TextInput
                onFocus={() => setFocused(true)}
                onBlur={() => setFocused(false)}
                placeholderTextColor={Colors.darkText}
                placeholder="Name"
                value={name}
                onChangeText={(text) => setName(text)}
                style={{flex: 1}}
              />
            </View>
            <View>
              <Text style={{color: 'red'}}>{nameError}</Text>
            </View>
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
                onChangeText={(text) => setEmail(text)}
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
                value={password} // Add this line to bind the value to the state
                onChangeText={(text) => setPassword(text)} // Update the password state
                style={{flex: 1}}
              />
            </View>
            <View>
              <Text style={{color: 'red'}}>{passwordError}</Text>
            </View>

            {/* Mobile Number Input */}
            {/* <View
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
                source={require('../assets/Lock.png')} // Replace with your mobile icon
                style={{width: 20, height: 20, marginRight: 10}}
              />
              <TextInput
                onFocus={() => setFocused(true)}
                onBlur={() => setFocused(false)}
                placeholderTextColor={Colors.darkText}
                placeholder="Mobile Number"
                keyboardType="phone-pad" // Use phone-pad for mobile numbers
                value={mobileNumber}
                onChangeText={(text) => setMobileNumber(text)}
                style={{flex: 1}}
              />
            </View> */}

            {/* Checkbox */}
            <View
              style={{
                flexDirection: 'row',
                alignItems: 'center',
                justifyContent: 'center',
              }}>
              <TouchableOpacity
                onPress={() => setCheckboxChecked(!isCheckboxChecked)}
                style={{
                  width: 20,
                  height: 20,
                  borderRadius: 5,
                  borderWidth: 1,
                  borderColor: Colors.text,
                  marginRight: Spacing / 2,
                  justifyContent: 'center',
                  alignItems: 'center',
                  backgroundColor: isCheckboxChecked ? Colors.primary : '#fff',
                }}>
                {isCheckboxChecked && (
                  <Image
                    source={require('../assets/Lock.png')} // Replace with your check icon
                    style={{width: 15, height: 15}}
                  />
                )}
              </TouchableOpacity>
              <Text
                style={{
                  fontFamily: 'Poppins-Regular',
                  fontSize: 12,
                  color: Colors.text,
                  paddingTop: 6,
                }}>
                By continuing you accept our Privacy Policy and Terms of Use.
              </Text>
            </View>
          </View>
        </View>

        {/* Move down content */}
        <View>
          <TouchableOpacity
            onPress={handleRegister}
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
              <Text
                style={{
                  fontFamily: 'Poppins-Bold',
                  color: Colors.onPrimary,
                  fontSize: 20,
                  marginTop: 5,
                }}>
                Register
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
              Already have an account?
            </Text>
            <TouchableOpacity onPress={handleLogin}>
              <Text
                style={{
                  fontFamily: 'Poppins-Regular',
                  color: '#EC9F05',
                  paddingLeft: 4,
                  fontSize: FontSize.small,
                }}>
                Login
              </Text>
            </TouchableOpacity>
          </View>
        </View>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
};

export default SignUpScreen;
