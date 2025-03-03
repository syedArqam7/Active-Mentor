import React from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  ImageBackground,
  Image,
} from 'react-native';
import {useNavigation} from '@react-navigation/native';
import {Color} from '../GlobalStyles';

const exercises = [
  {
    name: 'Jumping Jacks',
    mins: '10',
    score: '48',
    image: require('../assets/exercises/jumping_jack.png'),
  },
  {
    name: 'High Knees',
    mins: '25',
    score: '54',
    image: require('../assets/exercises/high_knees.png'),
  },
  {
    name: 'Push Ups',
    mins: '15',
    score: '76',
    image: require('../assets/exercises/jumping_jack.png'),
  },
  {
    name: 'Squats',
    mins: '15',
    score: '76',
    image: require('../assets/exercises/jumping_jack.png'),
  },
  {
    name: 'Lunges',
    mins: '15',
    score: '76',
    image: require('../assets/exercises/jumping_jack.png'),
  },
  {
    name: 'All',
    mins: '15',
    score: '76',
    image: require('../assets/exercises/jumping_jack.png'),
  },
  // Add other exercises here
];

const ExerciseMenu = () => {
  const navigation = useNavigation();

  const handleContinue = (exercise) => {
    navigation.navigate('ExerciseScreen', {exercise});
  };

  const renderExerciseCards = () => {
    return exercises.map((exercise, index) => (
      <View key={index} style={styles.cardContainer}>
        {/* <TouchableOpacity onPress={() => handleContinue(exercise.name)}> */}
        <ImageBackground style={styles.cardImage} source={exercise.image}>
          <View style={styles.overlay}>
            <Text style={styles.cardTitle}>{exercise.name}</Text>
            <Text style={styles.cardSubtitle}>{exercise.mins} mins more</Text>
            <View style={styles.progressBarContainer}>
              <View
                style={[
                  styles.progressBarFill,
                  {width: `${parseInt(exercise.score, 10)}%`},
                ]}
              />
            </View>
            <Text style={styles.scoreText}>{exercise.score} Points</Text>
            <TouchableOpacity
              style={styles.button}
              onPress={() => handleContinue(exercise.name)}>
              <Text style={styles.buttonText}>Continue</Text>
            </TouchableOpacity>
          </View>
        </ImageBackground>
        {/* </TouchableOpacity> */}
      </View>
    ));
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>Exercises</Text>
        <Image style={styles.logo} source={require('../assets/logo2.png')} />
      </View>
      <ScrollView contentContainerStyle={styles.scrollViewContainer}>
        {renderExerciseCards()}
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
    padding: 20,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 20,
  },
  title: {
    color: '#fff',
    fontSize: 28,
    // fontWeight: 'bold',
    fontFamily: 'Poppins-Bold',
  },
  logo: {
    width: 50,
    height: 50,
  },
  scrollViewContainer: {
    paddingVertical: 10,
  },
  cardContainer: {
    borderRadius: 10,
    marginBottom: 20,
    overflow: 'hidden',
  },
  cardImage: {
    width: '100%',
    // height: 200,
    justifyContent: 'flex-end',
  },
  overlay: {
    backgroundColor: 'rgba(0, 0, 0, 0.6)',
    padding: 20,
  },
  cardTitle: {
    color: '#fff',
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 5,
  },
  cardSubtitle: {
    color: '#fff',
    fontSize: 16,
    marginBottom: 5,
  },
  progressBarContainer: {
    height: 10,
    backgroundColor: '#111',
    borderRadius: 10,
    overflow: 'hidden',
    marginBottom: 5,
  },
  progressBarFill: {
    height: '100%',
    backgroundColor: Color.colorOrange,
  },
  scoreText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
  button: {
    backgroundColor: Color.colorGreen,
    paddingVertical: 10,
    marginTop: 10,
    borderRadius: 5,
    alignItems: 'center',
  },
  buttonText: {
    color: '#111',
    fontFamily: 'Poppins-Bold',
  },
});

export default ExerciseMenu;
