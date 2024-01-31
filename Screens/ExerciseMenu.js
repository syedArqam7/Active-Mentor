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

const ExerciseMenu = () => {
  const [isHistoryTab, setHistoryTab] = useState(false);
  const [selectedExercise, setSelectedExercise] = useState('Jumping Jacks'); // ['Jumping Jacks', 'High Knees', 'Push Ups', 'Squats', 'Lunges', 'All'
  const [score, setScore] = useState(50); // [0 - 100
  const navigation = useNavigation();

  const handleContinue = () => {
    navigation.navigate('ExerciseScreen', {selectedExercise});
  };

  const handleLeaderboardTabClick = () => {
    setHistoryTab(false);
  };

  const handleHistoryTabClick = () => {
    setHistoryTab(true);
  };

  const handleExerciseTabClick = (exercise) => {
    setSelectedExercise(exercise);
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.menuTitle}>Exercises</Text>
        <Image style={styles.logo} source={require('../assets/orangeLogo.png')} />
      </View>
      <ScrollView
        horizontal
        showsHorizontalScrollIndicator={false}
        style={styles.tabsScrollContainer}>
        <View style={styles.tabsContainer}>
          {[
            'Jumping Jacks',
            'High Knees',
            'Push Ups',
            'Squats',
            'Lunges',
            'All',
          ].map((exercise) => (
            <TouchableOpacity
              key={exercise}
              style={styles.tabButton}
              onPress={() => handleExerciseTabClick(exercise)}>
              <Text
                style={[
                  styles.tabText,
                  selectedExercise === exercise ? styles.activeTabText : null,
                ]}>
                {exercise}
              </Text>
            </TouchableOpacity>
          ))}
        </View>
      </ScrollView>

      {/* Score Card */}
      <View style={styles.scoreCard}>
        <View style={styles.progressBarContainer}>
          <View style={[styles.progressBarFill, {width: `${score}%`}]} />
        </View>
        <View style={styles.scoreCardHeader}>
          <View style={styles.scoreContainer}>
            <Text style={styles.exerciseTitle}>{selectedExercise}</Text>
            <Text style={styles.timeLeft}>25 mins more</Text>
          </View>
          <View style={styles.scoreContainer}>
            <Text style={styles.score}>{score}</Text>
            <Text style={styles.scoreLabel}>Points</Text>
          </View>
        </View>
        <TouchableOpacity
          style={styles.continueButton}
          onPress={handleContinue}>
          <Text style={styles.continueButtonText}>Continue</Text>
        </TouchableOpacity>
      </View>

      {/* Leaderboard and History Tabs */}
      <View style={styles.tabsContainer}>
        <TouchableOpacity
          style={[
            styles.secondaryButton,
            isHistoryTab ? styles.selectedButton : null,
          ]}
          onPress={handleHistoryTabClick}>
          <Text
            style={[
              styles.secondaryButtonText,
              isHistoryTab ? styles.selectedButtonText : null,
            ]}>
            History
          </Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[
            styles.secondaryButton,
            !isHistoryTab ? styles.selectedButton : null,
          ]}
          onPress={handleLeaderboardTabClick}>
          <Text
            style={[
              styles.secondaryButtonText,
              !isHistoryTab ? styles.selectedButtonText : null,
            ]}>
            Leader board
          </Text>
        </TouchableOpacity>
      </View>
      <ScrollView>
        {/* History List */}
        {isHistoryTab && (
          <>
            {/* History Item */}
            {Array.from({length: 12}).map((_, index) => (
              <View key={index} style={styles.historyItem}>
                <View style={styles.historyFirstRow}>
                  <Text style={styles.historyExercise}>1 min exercise</Text>
                  <Text style={styles.historyScore}>Score</Text>
                  <Text style={styles.historyScoreValue}>11</Text>
                </View>
                <View style={styles.historySecondRow}>
                  <Text style={styles.historyDetail}>Jumping Jacks</Text>
                </View>
              </View>
            ))}
          </>
        )}

        {/* Leaderboard List */}
        {!isHistoryTab && (
          <>
            {Array.from({length: 12}, (_, index) => (
              <View key={index} style={styles.historyItem}>
                <View style={styles.historyFirstRow}>
                  <Text style={styles.historyExercise}>
                    {index + 1}
                    {index === 0
                      ? 'st'
                      : index === 1
                      ? 'nd'
                      : index === 2
                      ? 'rd'
                      : 'th'}{' '}
                    Position
                  </Text>
                  <Text style={styles.userScore}>48 Points</Text>
                </View>
                <View style={styles.historySecondRow}>
                  <Text style={styles.historyDetail}>Alie Thomas</Text>
                </View>
              </View>
            ))}
          </>
        )}
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
  },
  header: {
    flex: 0,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    // marginTop: 30,
    marginVertical: 20,
    // height: 100,
  },
  logo: {
    width: 50,
    height: 45,
    marginVertical: 25,
    marginRight: 25,
  },
  menuTitle: {
    color: '#fff',
    fontSize: 30,
    fontWeight: 'bold',
    // textAlign: 'left',
    marginLeft: 20,
  },
  tabsScrollContainer: {
    height: 130, // Adjust this height as needed
    marginBottom: 10,
    marginTop: -20,
  },

  tabButton: {
    // Add styles specific to TouchableOpacity here
    paddingHorizontal: 10,
    paddingVertical: 5,
    marginHorizontal: 5,
  },
  tabText: {
    color: '#fff',
    fontSize: 16,
  },
  activeTabText: {
    color: Color.colorLime,
    fontSize: 16,
    fontWeight: 'bold',
  },

  tabsContainer: {
    flexDirection: 'row',
    alignItems: 'center',
    paddingVertical: 10,
    paddingHorizontal: 10,
  },
  tab: {
    color: '#fff',
    fontSize: 16,
    paddingHorizontal: 5,
  },
  activeTab: {
    color: 'limegreen',
    fontSize: 16,
    fontWeight: 'bold',
    paddingHorizontal: 5,
  },
  progressBarContainer: {
    height: 10,
    backgroundColor: '#111',
    borderRadius: 10,
  },
  progressBarFill: {
    width: '50%', // This width would be dynamic based on progress
    height: 10,
    backgroundColor: Color.colorOrange,
    borderRadius: 10,
  },
  scoreCard: {
    backgroundColor: '#1c1c1e',
    borderRadius: 10,
    padding: 20,
    marginHorizontal: 20,
    // alignItems: 'center',
  },
  scoreCardHeader: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    // marginBottom: 10,
  },
  scoreContainer: {
    // alignItems: 'center',
  },
  exerciseTitle: {
    color: '#fff',
    fontSize: 24,
    fontWeight: 'bold',
  },
  score: {
    color: 'white',
    fontSize: 48,
    fontWeight: 'bold',
  },
  scoreLabel: {
    color: '#fff',
    fontSize: 16,
    marginBottom: 10,
  },
  timeLeft: {
    color: '#fff',
    marginTop: 10,
    textAlign: 'left',
  },
  continueButton: {
    backgroundColor: Color.colorGreen,
    borderRadius: 20,
    paddingVertical: 10,
    paddingHorizontal: 20,
    alignSelf: 'stretch',
    alignItems: 'center',
    marginVertical: 10,
  },
  continueButtonText: {
    color: '#000',
    fontSize: 18,
    fontWeight: 'bold',
  },
  secondaryButton: {
    backgroundColor: '#222',
    borderRadius: 20,
    paddingVertical: 10,
    paddingHorizontal: 20,
    flex: 1,
    alignItems: 'center',
    margin: 5,
  },
  secondaryButtonText: {
    color: '#fff',
    fontSize: 16,
  },
  historyTitle: {
    color: '#fff',
    fontSize: 20,
    fontWeight: 'bold',
    paddingLeft: 20,
    marginBottom: 10,
  },
  historyItem: {
    backgroundColor: '#1c1c1e',
    borderRadius: 10,
    padding: 20,
    marginHorizontal: 20,
    marginBottom: 10,
  },
  historyFirstRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  historySecondRow: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  historyExercise: {
    color: '#fff',
    fontSize: 16,
  },
  historyDetail: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
    // marginBottom: 5,
  },
  historyScore: {
    color: '#fff',
    fontSize: 14,
    position: 'absolute',
    right: 20,
  },
  historyScoreValue: {
    color: 'limegreen',
    fontSize: 14,
    position: 'absolute',
    right: 20,
    top: 20,
  },
  historyStreaks: {
    color: '#fff',
    fontSize: 14,
    position: 'absolute',
    right: 20,
    bottom: 40,
  },
  historyStreaksValue: {
    color: 'limegreen',
    fontSize: 14,
    position: 'absolute',
    right: 20,
    bottom: 20,
  },
  selectedButton: {
    backgroundColor: Color.colorOrange,
  },
  selectedButtonText: {
    color: '#000',
  },
  leaderBoardItem: {
    backgroundColor: '#1c1c1e',
    padding: 15,
    marginHorizontal: 20,
    marginBottom: 10,
    borderRadius: 10,
  },
  position: {
    color: '#FFFFFF',
    fontSize: 18,
  },
  userName: {
    color: '#FFFFFF',
    fontSize: 18,
    fontWeight: 'bold',
  },
  userScore: {
    color: Color.colorGreen,
    fontSize: 18,
    fontWeight: 'bold',
  },
});

export default ExerciseMenu;
