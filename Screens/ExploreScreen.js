import React, {useState} from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Image,
} from 'react-native';
import {useNavigation} from '@react-navigation/native';
import {BottomButton, Color} from '../GlobalStyles';
import {exercises} from './exercise_list';

const ExploreScreen = ({route}) => {
  const [isHistoryTab, setHistoryTab] = useState(false);
  const [selectedExercise, setSelectedExercise] = useState('Jumping Jacks'); // ['Jumping Jacks', 'High Knees', 'Push Ups', 'Squats', 'Lunges', 'All'
  const [score, setScore] = useState(50); // [0 - 100
  const navigation = useNavigation();

    const selectedExerciseScore =
      exercises.find((exercise) => exercise.name === selectedExercise)?.score ||
      'N/A';


  const {name} = route.params; // Extracting the name from route params

  const handleBackPress = () => {
    navigation.navigate('ExerciseMenu');
  };

  const handleContinue = () => {
    navigation.navigate('ExerciseScreen', {selectedExercise});
  };

  const handleLeaderboardTabClick = () => {
    navigation.navigate('ExerciseLeaderBoard', {selectedExercise});

    setHistoryTab(false);
  };

  const handleHistoryTabClick = () => {
    setHistoryTab(true);
  };

  const handleExerciseTabClick = (exercise) => {
    setSelectedExercise(exercise);
  };

  // Add this function for logout
  const handleLogout = () => {
    // Perform logout actions here, such as clearing local storage, resetting state, etc.
    // Then navigate back to the login screen
    navigation.navigate('LoginScreen');
  };

  // const exercises = [
  //   {
  //     title: 'Squats',
  //     imageSource: require('../assets/exercises/squat.jpg'),
  //   },
  //   {
  //     title: 'Jumping Jacks',
  //     imageSource: require('../assets/exercises/jumping.jpeg'),
  //   },
  //   {
  //     title: 'High Knees',
  //     imageSource: require('../assets/exercises/highknee.jpg'),
  //   },
  //   {
  //     title: 'Push Ups',
  //     imageSource: require('../assets/exercises/push_ups.jpg'),
  //   },
  //   {
  //     title: 'Deadlift',
  //     imageSource: require('../assets/exercises/deadlift.jpg'),
  //   },
  //   {
  //     title: 'Planks',
  //     imageSource: require('../assets/exercises/plank.jpg'),
  //   },
  //   {
  //     title: 'Side Plank',
  //     imageSource: require('../assets/exercises/sidePlank.jpg'),
  //   },
  //   {
  //     title: 'Knee Pushup',
  //     imageSource: require('../assets/exercises/kneePushups.jpg'),
  //   },
  // ];

  return (
    <View style={styles.container}>
      <ScrollView
        showsHorizontalScrollIndicator={false}
        style={styles.tabsScrollContainer}>
        <View style={styles.header}>
          <Text style={styles.menuTitle}>
            Hey, <Text style={styles.highlightedText}>{name}!</Text>{' '}
          </Text>
          <TouchableOpacity onPress={handleLogout} style={styles.logoutButton}>
            <Text style={styles.logoutButtonText}>Logout</Text>
          </TouchableOpacity>

          <Text style={styles.menuTitle}>Let's start exploring</Text>
        </View>

        <View style={styles.imageContainer}>
          <ScrollView
            horizontal
            showsHorizontalScrollIndicator={false}
            style={styles.tabsScrollContainer}>
            {exercises.map((exercise, index) => (
              <View key={index} style={styles.imageWithTextContainer}>
                <TouchableOpacity
                  onPress={() => handleExerciseTabClick(exercise.name)}>
                  <Image source={exercise.image} style={styles.image} />
                  <View style={styles.overlay} />
                  <Text style={styles.imageText}>{exercise.name}</Text>
                </TouchableOpacity>
              </View>
            ))}
          </ScrollView>
        </View>

        {/* Score Card */}
        <View style={styles.scoreCard}>
          <View style={styles.scoreCardHeader}>
            <View style={styles.scoreContainer}>
              <View style={styles.con1}>
                <View>
                  <Text style={styles.history}>History</Text>
                  <Text style={styles.timeLeft}>1 min exercise</Text>
                </View>
                <TouchableOpacity
                  style={styles.continueButton}
                  onPress={handleContinue}>
                  <Text style={styles.continueButtonText}>See more</Text>
                </TouchableOpacity>
              </View>
            </View>
            <View style={styles.scoreContainer}>
              <View style={styles.con1}>
                <Text style={styles.exerciseTitle}>{selectedExercise}</Text>
                <View style={styles.continueButton}>
                  <Text style={styles.continueButtonText}>
                    Score: {selectedExerciseScore}
                  </Text>
                </View>
              </View>
            </View>
          </View>
        </View>

        {/* Tabs */}
        <View style={styles.header}>
          <Text style={styles.menuTitle}>Our Features</Text>
        </View>

        <View style={styles.featureCard}>
          <TouchableOpacity onPress={handleBackPress}>
            <Image
              source={require('../assets/exercises/exercise.jpg')}
              style={styles.featureImage}
            />
            <View style={styles.overlay} />
            <Text style={styles.allExerciseLabel}>All Excercises</Text>
          </TouchableOpacity>
        </View>
        <View style={styles.featureCard}>
          <TouchableOpacity onPress={handleLeaderboardTabClick}>
            <Image
              source={require('../assets/exercises/squat.jpg')}
              style={styles.featureImage}
            />
            <View style={styles.overlay} />
            <Text style={styles.allExerciseLabel}>Leaderboard</Text>
          </TouchableOpacity>
        </View>
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  imageWithTextContainer: {
    position: 'relative', // Make the container relative for absolute positioning
  },
  overlay: {
    ...StyleSheet.absoluteFillObject,
    backgroundColor: 'rgba(0, 0, 0, 0.45)', // Adjust the opacity as needed
    borderRadius: 10, // Match the border radius of the image
  },
  imageText: {
    position: 'absolute',
    bottom: 10, // Adjust the distance from the bottom as needed
    left: 10, // Adjust the distance from the left as needed
    color: 'white', // Adjust the text color as needed
    fontWeight: 'bold', // Adjust the font weight as needed
    fontSize: 16, // Adjust the fontSize as needed
  },
  container: {
    flex: 1,
    backgroundColor: '#000',
    paddingTop: 20,
  },
  con1: {
    flexDirection: 'row',
    justifyContent: 'space-between',
  },
  header: {
    flex: 0,
    flexDirection: 'column',
    justifyContent: 'space-between',
    // alignItems: 'center',
    // marginTop: 30,
    marginVertical: 20,
    // height: 100,
  },
  imageContainer: {
    flex: 0,
    flexDirection: 'column',
    justifyContent: 'space-between',
    marginTop: 20,
    paddingHorizontal: 20,
  },
  image: {
    width: 150,
    height: 120,
    borderRadius: 15,
    marginRight: 20,
  },
  featureCard: {
    position: 'relative',
    borderRadius: 10,
    marginHorizontal: 20,
    marginBottom: 40,
  },
  featureImage: {
    width: '100%',
    height: 400,
    borderRadius: 10,
  },
  allExerciseLabel: {
    position: 'absolute',
    padding: 20,
    bottom: 0, // Adjust the distance from the bottom as needed
    color: 'white', // Adjust the text color as needed
    fontSize: 42, // Adjust the fontSize as needed
    fontFamily: 'Poppins-Bold',
  },

  featureText: {
    position: 'absolute',
    bottom: 20, // Adjust the distance from the bottom as needed
    left: 5, // Adjust the distance from the left as needed
    color: 'white', // Adjust the text color as needed
    fontWeight: 'bold', // Adjust the font weight as needed
    fontSize: 16, // Adjust the fontSize as needed
  },

  highlightedText: {
    color: '#EC9F05',
    fontFamily: 'Poppins-Bold',
  },
  logo: {
    width: 50,
    height: 45,
    marginVertical: 25,
    marginRight: 25,
  },
  menuTitle: {
    color: '#fff',
    fontSize: 42,
    marginLeft: 20,
    fontFamily: 'Poppins-Bold',
  },
  tabsScrollContainer: {
    height: 130, // Adjust this height as needed
    marginBottom: 10,
    marginTop: -20,
  },

  scoreCard: {
    backgroundColor: '#1c1c1e',
    borderRadius: 10,
    paddingHorizontal: 40,
    paddingVertical: 40,
    marginHorizontal: 20,
    // alignItems: 'center',
  },
  scoreCardHeader: {
    flexDirection: 'column',
    justifyContent: 'space-between',
    // alignItems: 'center',
    // marginBottom: 10,
  },
  scoreContainer: {
    // alignItems: 'center',
  },
  exerciseTitle: {
    color: '#fff',
    fontSize: 42,
    // fontWeight: 'bold',
    fontFamily: 'Poppins-Regular',
    marginTop: 10,
  },
  history: {
    color: '#fff',
    fontSize: 42,
    fontWeight: 'bold',
  },
  score: {
    color: 'white',
    fontSize: 28,
    fontWeight: 'bold',
  },
  scoreLabel: {
    color: '#fff',
    fontSize: 16,
    marginBottom: 10,
    textAlign: 'center',
  },
  timeLeft: {
    color: '#fff',
    textAlign: 'left',
  },
  continueButton: {
    backgroundColor: '#EC9F05',
    borderRadius: 10,
    paddingVertical: 10,
    paddingHorizontal: 20,
    // alignSelf: 'stretch',
    alignItems: 'center',
    marginVertical: 10,
  },
  continueButtonText: {
    color: '#fff',
    fontSize: 16,
    textAlign: 'center',
    fontFamily: 'Poppins-Bold',
  },
  // Add styles for the logout button
  logoutButton: {
    position: 'absolute',
    top: 20,
    right: 20,
    paddingVertical: 10,
    paddingHorizontal: 15,
    backgroundColor: '#EC9F05',
    borderRadius: 10,
    width: 100,
  },
  logoutButtonText: {
    color: '#fff',
    fontSize: 16,
    textAlign: 'center',
    fontFamily: 'Poppins-Bold',
  },
});

export default ExploreScreen;
