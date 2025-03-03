import React, { useState } from 'react';
import { View, Text, TouchableOpacity, StyleSheet, ScrollView } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { BottomButton, Color } from '../GlobalStyles';

const ExerciseSummary = ({ route }) => {
  const navigation = useNavigation();
  const { selectedExercise } = route.params;

	const handlePlayAgain = () => {
		navigation.navigate('ExerciseScreen');
	}

	const handleLeaderboard = () => {
		navigation.navigate('ExerciseLeaderboard');
	}

	const handleBackPress = () => {
		navigation.navigate('ExerciseMenu');
	}

  return (
    <View style={styles.container}>
      <TouchableOpacity style={styles.backButton} onPress={handleBackPress}>
        <Text style={styles.backButtonText}>Back</Text>
      </TouchableOpacity>
      <Text style={styles.headerTitle}>Summary</Text>

      <View style={styles.scoreContainer}>
        <Text style={styles.totalScore}>11</Text>
        <Text style={styles.totalScoreLabel}>Total Score</Text>
      </View>

      <Text style={styles.exerciseName}>{selectedExercise}</Text>

      <View style={styles.statsContainer}>
        <View style={styles.statItem}>
          <Text style={styles.statLabel}>Max Streak</Text>
          <Text style={styles.statValue}>12</Text>
        </View>
        <View style={styles.divider} />
        <View style={styles.statItem}>
          <Text style={styles.statLabel}>Quality Score</Text>
          <Text style={styles.statValue}>42</Text>
        </View>
        <View style={styles.divider} />
        <View style={styles.statItem}>
          <Text style={styles.statLabel}>Leaderboard</Text>
          <Text style={styles.statValue}>48</Text>
        </View>
      </View>

      <TouchableOpacity style={styles.deleteButton} onPress={handleLeaderboard}>
        <Text style={styles.deleteButtonText}>Go to Leaderboard</Text>
      </TouchableOpacity>

      <TouchableOpacity style={styles.playAgainButton} onPress={handlePlayAgain}>
        <Text style={styles.playAgainButtonText}>PLAY AGAIN</Text>
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
    alignItems: 'center',
    paddingTop: 50, // Adjust for status bar height
  },
  backButton: {
    position: 'absolute',
    top: 20,
    left: 20,
  },
  backButtonText: {
    color: '#fff',
  },
  headerTitle: {
    color: '#fff',
    fontSize: 24,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  scoreContainer: {
    alignItems: 'center',
    marginBottom: 20,
  },
  totalScore: {
    color: 'limegreen',
    fontSize: 48,
    fontWeight: 'bold',
  },
  totalScoreLabel: {
    color: 'limegreen',
    fontSize: 16,
  },
  exerciseName: {
    color: '#fff',
    fontSize: 32,
    fontWeight: 'bold',
    marginBottom: 20,
  },
  statsContainer: {
    alignSelf: 'stretch',
    marginHorizontal: 20,
  },
  statItem: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    paddingVertical: 10,
  },
  statLabel: {
    color: '#fff',
    fontSize: 18,
  },
  statValue: {
    color: 'limegreen',
    fontSize: 18,
    fontWeight: 'bold',
  },
  divider: {
    height: 1,
    backgroundColor: 'grey',
    marginVertical: 5,
  },
  deleteButton: {
    backgroundColor: 'red',
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 20,
    marginBottom: 20,
  },
  deleteButtonText: {
    color: '#fff',
    fontSize: 18,
  },
  playAgainButton: {
    backgroundColor: Color.colorGreen,
    paddingVertical: 10,
    paddingHorizontal: 20,
    borderRadius: 20,
  },
  playAgainButtonText: {
    color: '#000',
    fontSize: 18,
    fontWeight: 'bold',
  },
});

export default ExerciseSummary;
