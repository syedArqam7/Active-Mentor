import React, { useState } from 'react';
import {
  SafeAreaView,
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  CheckBox,
  Button,
} from 'react-native';
import { BottomButton, Color } from '../GlobalStyles';
import { useNavigation } from '@react-navigation/native';

const CreateProfile = () => {
  const [isSelected, setSelection] = useState(false);
  const navigation = useNavigation();
  const handleNext = () => {
    // Handle next button press
    navigation.navigate('ExerciseMenu');
  };

  return (
    <SafeAreaView style={styles.container}>
      <ScrollView contentContainerStyle={styles.scrollView}>
        <Text style={styles.header}>Create Your Profile</Text>
        
        <TouchableOpacity style={styles.optionButton}>
          <Text style={styles.optionText}>Create New Profile</Text>
          <Text style={styles.newLabel}>NEW</Text>
        </TouchableOpacity>
        
        <TouchableOpacity style={styles.optionButton}>
          <Text style={styles.optionText}>Continue With Your GYM ID</Text>
        </TouchableOpacity>
        
        <Text style={styles.infoText}>
          If you don't have a Gym ID, please ask your mentor for it or you can create a new profile
        </Text>
        
        <TouchableOpacity style={styles.optionButton}>
          <Text style={styles.optionText}>Try The Demo Now</Text>
        </TouchableOpacity>
        
        <View style={styles.checkboxContainer}>
          <CheckBox
            value={isSelected}
            onValueChange={setSelection}
            style={[styles.checkbox, { borderColor: 'green' }]}
            tintColors={{ true: Color.colorGreen, false: 'white' }}
          />
          <Text style={styles.label}>I Accept All Term & Conditions</Text>
        </View>
        
        <TouchableOpacity
          style={[BottomButton, { backgroundColor: isSelected ? Color.colorGreen : 'gray' }]}
          disabled={!isSelected}
            onPress={handleNext}
        >
          <Text style={styles.nextButtonText}>NEXT</Text>
        </TouchableOpacity>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'black',
  },
  scrollView: {
    flexGrow: 1,
    justifyContent: 'center',
    padding: 20,
  },
  header: {
    color: 'white',
    fontSize: 38,
    fontWeight: 'bold',
    marginBottom: 32,
  },
  optionButton: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 20,
    borderWidth: 1,
    borderColor: 'white',
    borderRadius: 10,
    marginBottom: 20,
  },
  optionText: {
    color: 'white',
    fontSize: 18,
  },
  newLabel: {
    backgroundColor: 'green',
    color: 'white',
    padding: 5,
    fontSize: 12,
  },
  infoText: {
    color: 'gray',
    fontSize: 14,
    marginBottom: 20,
  },
  checkboxContainer: {
    flexDirection: 'row',
    marginBottom: 20,
    alignItems: 'center',
  },
  checkbox: {
    alignSelf: "center",
    
  },
  label: {
    color: 'white',
    margin: 8,
  },
  nextButton: {
    padding: 15,
    borderRadius: 25,
  },
  nextButtonText: {
    color: 'white',
    textAlign: 'center',
    fontWeight: 'bold',
    fontSize: 18,
  },
});

export default CreateProfile;
