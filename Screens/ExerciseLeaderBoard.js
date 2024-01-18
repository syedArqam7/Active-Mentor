import React from 'react';
import { View, Text, Image, StyleSheet, ScrollView, TouchableOpacity } from 'react-native';
import { useNavigation } from '@react-navigation/native';

const ExerciseLeaderBoard = () => {
	const navigation = useNavigation();

	const handleBackPress = () => {
			navigation.navigate('ExerciseScreen'); // Replace 'ExerciseScreen' with the actual screen name
	};

	return (
	<View style={styles.container}>
		<TouchableOpacity style={styles.backButton} onPress={handleBackPress}>
			<Text style={styles.backButtonText}>Back</Text>
		</TouchableOpacity>
		<Text style={styles.headerTitle}>Leaderboard</Text>

		{/* Tabs */}
		<View style={styles.tabsContainer}>
			<Text style={styles.tab}>Daily</Text>
			<Text style={styles.tab}>Weekly</Text>
			<Text style={styles.tabActive}>Global</Text>
		</View>

      {/* Top Three */}
		<View style={styles.topThreeContainer}>
			{/* This container will hold the top three users, you can map through data to create these */}
		</View>

		{/* Leaderboard List */}
		<ScrollView style={styles.leaderboardList}>
			{/* This should be dynamically generated based on your leaderboard data */}
			<View style={styles.leaderboardItem}>
				<Text style={styles.positionText}>1st Position</Text>
				<Text style={styles.userName}>Alie Thomas</Text>
				<Text style={styles.points}>48 Points</Text>
			</View>
			{/* Repeat for each position */}
		</ScrollView>
	</View>
  );
};

const styles = StyleSheet.create({
container: {
	flex: 1,
	backgroundColor: '#000',
	paddingTop: 50, // Adjust for status bar height
},
backButton: {
	marginLeft: 20,
},
backButtonText: {
	color: '#fff',
},
headerTitle: {
	color: '#fff',
	fontSize: 24,
	fontWeight: 'bold',
	textAlign: 'center',
	marginVertical: 20,
},
tabsContainer: {
	flexDirection: 'row',
	justifyContent: 'center',
	marginBottom: 20,
},
tab: {
	color: '#fff',
	marginHorizontal: 10,
},
tabActive: {
	color: '#fff',
	fontWeight: 'bold',
	marginHorizontal: 10,
},
topThreeContainer: {
	// Additional styling needed
},
leaderboardList: {
	marginTop: 20,
},
leaderboardItem: {
	backgroundColor: '#1c1c1e',
	borderRadius: 10,
	marginHorizontal: 20,
	marginBottom: 10,
	padding: 15,
},
positionText: {
	color: '#fff',
	fontWeight: 'bold',
	marginBottom: 5,
},
userName: {
	color: '#fff',
	marginBottom: 5,
},
points: {
	color: '#fff',
},
  // Add additional styles for images, and the crown icon for the first place
});

export default ExerciseLeaderBoard;
