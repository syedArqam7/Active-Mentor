import React, {useState} from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  ImageBackground,
  Image,
  Dimensions,
} from 'react-native';
import {useNavigation} from '@react-navigation/native';
import {Color} from '../GlobalStyles';
import {exercises} from './exercise_list';

const ExerciseMenu = () => {
  const navigation = useNavigation();
  const [selectedExercise, setSelectedExercise] = useState('Jumping Jacks');
  const windowWidth = Dimensions.get('window').width;
  const windowHeight = Dimensions.get('window').height;

  const handleContinue = (exercise) => {
    navigation.navigate('ExerciseScreen', {selectedExercise: exercise.name});
  };

  const handleBackPress = () => {
    navigation.goBack();
  };

  const renderExerciseCards = () => {
    return exercises.map((exercise, index) => (
      <View key={index} style={styles.cardContainer}>
        <ImageBackground
          style={[styles.cardImage, {height: windowHeight * 0.24}]}
          source={exercise.image}>
          <View style={styles.overlay}>
            <View style={styles.progressBarContainer}>
              <View
                style={[
                  styles.progressBarFill,
                  {width: `${parseInt(exercise.score, 10)}%`},
                ]}
              />
            </View>
            <View style={styles.cardContent}>
              <View style={styles.cardTopRow}>
                <View style={styles.cardTopRight}>
                  <Text
                    style={[styles.cardTitle, {fontSize: windowWidth * 0.025}]}>
                    {exercise.name}
                  </Text>
                </View>
                <View style={styles.cardTopLeft}>
                  <Text style={styles.scoreText}>{exercise.score}</Text>
                  <Text style={styles.scoreText1}> Points</Text>
                </View>
              </View>
              <View style={styles.cardBottomRow}>
                <Text
                  style={[
                    styles.cardSubtitle,
                    {fontSize: windowWidth * 0.013},
                  ]}>
                  {exercise.mins} mins more
                </Text>
                <TouchableOpacity
                  style={styles.button}
                  onPress={() => handleContinue(exercise)}>
                  <Text style={styles.buttonText}>Continue</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </ImageBackground>
      </View>
    ));
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <TouchableOpacity style={styles.backButton} onPress={handleBackPress}>
          <Text style={styles.backButtonText}>Back</Text>
        </TouchableOpacity>
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
    fontSize: 38,
    fontFamily: 'Poppins-Bold',
    textAlign: 'center',
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
    justifyContent: 'flex-end',
  },
  overlay: {
    backgroundColor: 'rgba(0, 0, 0, 0.55)',
    padding: 20,
  },
  progressBarContainer: {
    height: 10,
    backgroundColor: '#111',
    borderRadius: 10,
    overflow: 'hidden',
    marginBottom: 10,
  },
  progressBarFill: {
    height: '100%',
    backgroundColor: Color.colorOrange,
  },
  cardContent: {},
  cardTopRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    marginBottom: 5,
    alignItems: 'center',
  },
  cardTopLeft: {
    flexDirection: 'column',
  },
  cardTopRight: {
    flex: 1,
  },
  cardTitle: {
    color: '#fff',
    marginBottom: 5,
    width: '65%',
    fontFamily: 'Poppins-Regular',
  },
  cardSubtitle: {
    color: '#fff',
    fontFamily: 'Poppins-Regular',
  },
  scoreText: {
    color: '#fff',
    fontSize: 48,
    fontWeight: 'bold',
    textAlign: 'left',
  },
  scoreText1: {
    color: '#fff',
    fontSize: 14,
    fontFamily: 'Poppins-Regular',
    textAlign: 'center',
    paddingBottom: 10,
  },
  cardBottomRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  button: {
    backgroundColor: Color.colorGreen,
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 5,
    alignItems: 'center',
  },
  buttonText: {
    color: '#fff',
    fontFamily: 'Poppins-Bold',
  },

  backButton: {
    // marginLeft: 20,
    padding: 10,
    width: 100,
    backgroundColor: '#EC9F05',
    borderRadius: 10,
  },
  backButtonText: {
    color: '#fff',
    fontSize: 16,
    textAlign: 'center',
    fontFamily: 'Poppins-Bold',
  },
});

export default ExerciseMenu;
