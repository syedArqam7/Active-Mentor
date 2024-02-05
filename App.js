import React, {useEffect, useState} from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {createNativeStackNavigator} from '@react-navigation/native-stack';

import SplashScreen from './Screens/SplashScreen';
import WelcomeScreen from './Screens/WelcomeScreen';
import CreateProfile from './Screens/CreateProfile';
import ExerciseMenu from './Screens/ExerciseMenu';
import ExerciseScreen from './Screens/ExerciseScreen';
import ExerciseLeaderBoard from './Screens/ExerciseLeaderBoard';
import ExerciseSummary from './Screens/ExerciseSummary';
import ExploreScreen from './Screens/ExploreScreen';
import AI from './AI';
import SignUpScreen from './Screens/SignUpScreen';
import LoginScreen from './Screens/Login';

const Stack = createNativeStackNavigator();

const App = () => {
  const [hideSplashScreen, setHideSplashScreen] = useState(false); // Initially false to show the splash screen

  useEffect(() => {
    const timer = setTimeout(() => {
      setHideSplashScreen(true); // Hide the splash screen after a delay
    }, 1500); // Adjust the time as needed

    return () => clearTimeout(timer);
  }, []);

  return (
    <NavigationContainer>
      <Stack.Navigator screenOptions={{headerShown: false}}>
        {!hideSplashScreen && (
          <Stack.Screen name="SplashScreen" component={SplashScreen} />
        )}
        {hideSplashScreen && (
          <>
            <Stack.Screen name="WelcomeScreen" component={WelcomeScreen} />
            <Stack.Screen name="SignUpScreen" component={SignUpScreen} />
            <Stack.Screen name="LoginScreen" component={LoginScreen} />
            <Stack.Screen name="CreateProfile" component={CreateProfile} />
            <Stack.Screen name="ExploreScreen" component={ExploreScreen} />
            <Stack.Screen name="ExerciseMenu" component={ExerciseMenu} />
            <Stack.Screen name="ExerciseScreen" component={ExerciseScreen} />
            <Stack.Screen
              name="ExerciseLeaderBoard"
              component={ExerciseLeaderBoard}
            />
            <Stack.Screen name="ExerciseSummary" component={ExerciseSummary} />
          </>
        )}
      </Stack.Navigator>
    </NavigationContainer>
    // <AI />
  );
};

export default App;
