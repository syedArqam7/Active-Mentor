import React, {useState} from 'react';
import {
  View,
  Text,
  Image,
  TouchableOpacity,
  Switch,
  StyleSheet,
  SafeAreaView,
  ScrollView,
  NativeModules,
} from 'react-native';
import {useNavigation} from '@react-navigation/native';
import {
  exerciseTypes,
  AI_Exercises,
  BallPerson_AI_Exercises,
  Ball_AI_Exercises,
  nonAI_Exercises,
  ExerciseVariations,
} from '../exercise_data';

const {
  ToastExample,
  JugglingBridge: {startLandscapeActivity, startPortraitActivity},
} = NativeModules;

const ExerciseScreen = ({route}) => {
  const navigation = useNavigation();
  const [landscapePosition, setLandscapePosition] = useState(false);
  const [frontCameraEnabled, setFrontCameraEnabled] = useState(false);
  const {selectedExercise} = route.params;

  const {name, mins, score, image} = selectedExercise;

  const landscapePositionSwitch = () =>
    setLandscapePosition((previousState) => !previousState);
  const toggleFrontCameraSwitch = () =>
    setFrontCameraEnabled((previousState) => !previousState);

  const handleBackPress = () => {
    navigation.navigate('ExploreScreen');
  };

  const handleStartNow = () => {
    // navigation.navigate('ExerciseSummary', { selectedExercise });
    const item = AI_Exercises.find((item) => item.title === name);
    if (frontCameraEnabled) item.selectedCameraFacing = 'FRONT';
    else item.selectedCameraFacing = 'BACK';
    item.countDownMiliSeconds = 10000;
    if (landscapePosition)
      startLandscapeActivity(JSON.stringify({...item}), parseInt(item.id));
    else {
      try {
        startPortraitActivity(JSON.stringify({...item}), parseInt(item.id));
      } catch (error) {
        console.error(
          `Failed to start portrait activity with ${cameraFacing} camera: ${error}`,
        );
      }
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.contentContainer}>
        <View style={styles.header}>
          <TouchableOpacity style={styles.backButton} onPress={handleBackPress}>
            <Text style={styles.backButtonText}>Back</Text>
          </TouchableOpacity>
        </View>
        <Text style={styles.titleText}>{name}</Text>
        <Image source={image} style={styles.image} />

        <View style={styles.scoreSection}>
          <Text style={styles.scoreTitle}>Your High Score</Text>
          <Text style={styles.scoreValue}>{score}</Text>
          <Text style={styles.scoreDescription}>
            This Exercise could take 60 mins. Do as many jumping jacks as
            possible & make sure you keep going don't stop
          </Text>
        </View>
        <View style={styles.switchContainer}>
          <Text style={styles.switchLabel}>
            {landscapePosition ? 'Landscape' : 'Portrait'}
          </Text>
          <Switch
            trackColor={{false: '#767577', true: '#81b0ff'}}
            thumbColor={landscapePosition ? '#f5dd4b' : '#f4f3f4'}
            ios_backgroundColor="#3e3e3e"
            onValueChange={landscapePositionSwitch}
            value={landscapePosition}
          />
        </View>
        <View style={styles.switchContainer}>
          <Text style={styles.switchLabel}>
            {frontCameraEnabled ? 'Front Camera' : 'Back Camera'}
          </Text>
          <Switch
            trackColor={{false: '#767577', true: '#81b0ff'}}
            thumbColor={frontCameraEnabled ? '#f5dd4b' : '#f4f3f4'}
            ios_backgroundColor="#3e3e3e"
            onValueChange={toggleFrontCameraSwitch}
            value={frontCameraEnabled}
          />
        </View>
        <TouchableOpacity style={styles.startButton} onPress={handleStartNow}>
          <Text style={styles.startButtonText}>START NOW</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
  },
  contentContainer: {
    padding: 20,
  },
  header: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginTop: 20,
    marginBottom: 20,
  },
  headerText: {
    color: '#fff',
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  backButton: {
    // marginBottom: 20,
  },
  backButtonText: {
    color: '#fff',
  },
  titleText: {
    fontSize: 30,
    fontWeight: 'bold',
    color: '#fff',
    marginBottom: 10,
  },
  image: {
    width: '100%',
    height: 200,
    borderRadius: 10,
    marginBottom: 20,
  },
  scoreSection: {
    backgroundColor: 'rgba(255, 255, 255, 0.1)',
    borderRadius: 10,
    padding: 20,
    marginBottom: 20,
  },
  scoreTitle: {
    fontSize: 16,
    color: '#fff',
    marginBottom: 5,
  },
  scoreValue: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#fff',
    marginBottom: 5,
  },
  scoreDescription: {
    fontSize: 14,
    color: '#fff',
  },
  switchContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    marginBottom: 20,
  },
  switchLabel: {
    fontSize: 16,
    color: '#fff',
  },
  startButton: {
    backgroundColor: '#EC9F05',
    borderRadius: 30,
    paddingVertical: 15,
    paddingHorizontal: 30,
    alignItems: 'center',
    justifyContent: 'center',
  },
  startButtonText: {
    fontSize: 16,
    color: '#fff',
    fontWeight: 'bold',
  },
});

export default ExerciseScreen;
