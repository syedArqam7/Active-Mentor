import React, {useState} from 'react';
import {
  View,
  Text,
  StyleSheet,
  ScrollView,
  TouchableOpacity,
} from 'react-native';
import {useNavigation} from '@react-navigation/native';

const ExerciseLeaderBoard = () => {
  const navigation = useNavigation();
  const [activeTab, setActiveTab] = useState('Global');

  const handleBackPress = () => {
    navigation.navigate('ExploreScreen');
  };

  const handleTabPress = (tab) => {
    setActiveTab(tab);
    // You can add logic here to fetch leaderboard data based on the selected tab
  };

  // Sample data for the leaderboard
  const leaderboardData = [
    {position: '1st Position', userName: 'Alie Thomas', points: 48},
    {position: '2nd Position', userName: 'John Doe', points: 42},
    {position: '3rd Position', userName: 'Jane Smith', points: 38},
    // Add more data as needed
  ];

  return (
    <View style={styles.container}>
      <TouchableOpacity style={styles.backButton} onPress={handleBackPress}>
        <Text style={styles.backButtonText}>Back</Text>
      </TouchableOpacity>
      <Text style={styles.headerTitle}>Leaderboard</Text>

      {/* Tabs */}
      <View style={styles.tabsContainer}>
        <TouchableOpacity onPress={() => handleTabPress('Daily')}>
          <Text style={[styles.tab, activeTab === 'Daily' && styles.tabActive]}>
            Daily
          </Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => handleTabPress('Weekly')}>
          <Text
            style={[styles.tab, activeTab === 'Weekly' && styles.tabActive]}>
            Weekly
          </Text>
        </TouchableOpacity>
        <TouchableOpacity onPress={() => handleTabPress('Global')}>
          <Text
            style={[styles.tab, activeTab === 'Global' && styles.tabActive]}>
            Global
          </Text>
        </TouchableOpacity>
      </View>

      {/* Leaderboard List */}
      <ScrollView style={styles.leaderboardList}>
        {leaderboardData.map((item, index) => (
          <View key={index} style={styles.leaderboardItem}>
            <Text style={styles.positionText}>{item.position}</Text>
            <Text style={styles.userName}>{item.userName}</Text>
            <Text style={styles.points}>{item.points} Points</Text>
          </View>
        ))}
      </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000',
    paddingTop: 50,
  },
  backButton: {
    marginLeft: 20,
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
  headerTitle: {
    color: '#fff',
    fontSize: 48,
    // fontWeight: 'bold',
    textAlign: 'center',
    marginVertical: 10,
    fontFamily: 'Poppins-Bold',
  },
  tabsContainer: {
    flexDirection: 'row',
    justifyContent: 'center',
    marginBottom: 20,
  },
  tab: {
    color: '#fff',
    marginHorizontal: 10,
    fontSize: 24,
  },
  tabActive: {
    color: '#EC9F05',
    fontWeight: 'bold',
    marginHorizontal: 10,
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
    fontSize: 24,
    fontFamily: 'Poppins-Bold',
  },
  userName: {
    color: '#fff',
    marginBottom: 5,
  },
  points: {
    color: '#fff',
  },
});

export default ExerciseLeaderBoard;
