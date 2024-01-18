import React, { useState } from 'react';
import {
  View,
  Text,
  ScrollView,
  TouchableOpacity,
  StyleSheet,
  Image,
 } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { BottomButton, Color } from '../GlobalStyles';

const ExerciseMenu = () => {
  const [isHistoryTab, setHistoryTab] = useState(false);
  const [selectedExercise, setSelectedExercise] = useState('Jumping Jacks'); // ['Jumping Jacks', 'High Knees', 'Push Ups', 'Squats', 'Lunges', 'All'
  const [score, setScore] = useState(50); // [0 - 100
  const navigation = useNavigation();

  const handleContinue = () => {
    navigation.navigate('ExerciseScreen', { selectedExercise });
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
          {/* <Image style={styles.logo} source={require('../assets/am-logo.png')} /> */}
        </View>
        {/* Exercise Tabs */}
        <ScrollView horizontal showsHorizontalScrollIndicator={false} style={styles.tabsScrollContainer}>
          <View style={styles.tabsContainer}>
            <TouchableOpacity
              style={[styles.tab, selectedExercise === 'Jumping Jacks' ? styles.activeTab : null]}
              onPress={() => handleExerciseTabClick('Jumping Jacks')}
            >
              <Text style={[styles.tab, selectedExercise === 'Jumping Jacks' ? { color: Color.colorGreen } : null]}>Jumping Jacks</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[styles.tab, selectedExercise === 'High Knees' ? styles.activeTab : null]}
              onPress={() => handleExerciseTabClick('High Knees')}
            >
              <Text style={[styles.tab, selectedExercise === 'High Knees' ? { color: Color.colorGreen } : null]}>High Knees</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[styles.tab, selectedExercise === 'Push Ups' ? styles.activeTab : null]}
              onPress={() => handleExerciseTabClick('Push Ups')}
            >
              <Text style={[styles.tab, selectedExercise === 'Push Ups' ? { color: Color.colorGreen } : null]}>Push Ups</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[styles.tab, selectedExercise === 'Squats' ? styles.activeTab : null]}
              onPress={() => handleExerciseTabClick('Squats')}
            >
              <Text style={[styles.tab, selectedExercise === 'Squats' ? { color: Color.colorGreen } : null]}>Squats</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[styles.tab, selectedExercise === 'Lunges' ? styles.activeTab : null]}
              onPress={() => handleExerciseTabClick('Lunges')}
            >
              <Text style={[styles.tab, selectedExercise === 'Lunges' ? { color: Color.colorGreen } : null]}>Lunges</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={[styles.tab, selectedExercise === 'All' ? styles.activeTab : null]}
              onPress={() => handleExerciseTabClick('All')}
            >
              <Text style={[styles.tab, selectedExercise === 'All' ? { color: Color.colorGreen } : null]}>All</Text>
            </TouchableOpacity>
          </View>
        </ScrollView>
        
        {/* Score Card */}
        <View style={styles.scoreCard}>
          <View style={styles.progressBarContainer}>
            <View style={[styles.progressBarFill, { width: `${score}%` }]} />
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
          <TouchableOpacity style={styles.continueButton} onPress={handleContinue}>
            <Text style={styles.continueButtonText}>Continue</Text>
          </TouchableOpacity>
        </View>

        {/* Leaderboard and History Tabs */}
        <View style={styles.tabsContainer}>
          <TouchableOpacity
            style={[styles.secondaryButton, isHistoryTab ? styles.selectedButton : null]}
            onPress={handleHistoryTabClick}
          >
            <Text style={[styles.secondaryButtonText, isHistoryTab ? styles.selectedButtonText : null]}>
              History
            </Text>
          </TouchableOpacity>
          <TouchableOpacity
            style={[styles.secondaryButton, !isHistoryTab ? styles.selectedButton : null]}
            onPress={handleLeaderboardTabClick}
          >
            <Text style={[styles.secondaryButtonText, !isHistoryTab ? styles.selectedButtonText : null]}>
              Leader board
            </Text>
          </TouchableOpacity>
        </View>
        <ScrollView>
        {/* History List */}
        {isHistoryTab && (
          <>
            {/* History Item */}
            {Array.from({ length: 12 }).map((_, index) => (
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
            {Array.from({ length: 12 }, (_, index) => (
              <View key={index} style={styles.historyItem}>
                <View style={styles.historyFirstRow}>
                  <Text style={styles.historyExercise}>{index + 1}{index === 0 ? 'st' : index === 1 ? 'nd' : index === 2 ? 'rd' : 'th'} Position</Text>
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
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'space-between',
    // marginTop: 30,
    marginVertical: 20,
  },
  logo: {
    width: "10%",
    height: "30%",
  },
  menuTitle: {
    color: '#fff',
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'left',
    marginLeft: 20,
  },
  tabsScrollContainer: {
    flexGrow: 0, // Add this to prevent ScrollView from filling the whole screen
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
    backgroundColor: Color.colorGreen,
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
    color: 'limegreen',
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
    backgroundColor: Color.colorGreen,
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
