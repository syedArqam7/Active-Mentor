import React, {Component} from 'react';
import {
  ScrollView,
  View,
  StatusBar,
  NativeModules,
  NativeEventEmitter,
  Platform,
  SafeAreaView,
  Text,
  StyleSheet,
  TouchableOpacity,
  TextInput,
} from 'react-native';

import RNBootSplash from 'react-native-bootsplash';
import {
  exerciseTypes,
  AI_Exercises,
  BallPerson_AI_Exercises,
  Ball_AI_Exercises,
  nonAI_Exercises,
  ExerciseVariations,
} from './exercise_data';

const {
  ToastExample,
  JugglingBridge: {startLandscapeActivity},
} = NativeModules;

export default class AI extends Component {
  constructor() {
    super();
    this.state = {
      bg_color: bg_colors[0],
      countdownTime: 30,
      cameraFacing: 'FRONT',
    };
    this.cameraFacingValues = {
      FRONT: 'BACK',
      BACK: 'FRONT',
    };
    this.colorMap = {
      FRONT: '#DA4167',
      BACK: '#2fee12',
    };
  }

  componentDidMount() {
    console.disableYellowBox = true;
    RNBootSplash.hide();
    // if (Platform.OS === 'ios') {
    //   this.eventListener = iOSNativeAppEvents.addListener(
    //     'exerciseCompleted',
    //     (result) => {
    //       console.log('END EXERCISE RESULT => ', result);
    //     },
    //   );
    // } else
    if (Platform.OS === 'android') {
      const eventEmitter = new NativeEventEmitter(ToastExample);
      this.eventListener = eventEmitter.addListener('ai-complete', (event) => {
        console.log(event); // "someValue"
      });
    }
  }

  componentWillUnmount() {
    if (Platform.OS === 'ios' || Platform.OS === 'android') {
      this.eventListener.remove(); //Removes the listener
    }
  }

  onPress = (item) => {
    console.log(item);
    item['countDownMiliSeconds'] = this.state.countdownTime * 1000;
    item['selectedCameraFacing'] = this.state.cameraFacing;
    console.log(item);
    startLandscapeActivity(
      JSON.stringify({...item}),
      parseInt(item.id),
    );
  };

  createSection = (section_heading, information_list) => {
    return (
      <View style={{padding: 20}}>
        <Text style={styles.heading}>{section_heading}</Text>
        {information_list.map((item, index) => {
          if (item.id in ExerciseVariations) {
            let v = ExerciseVariations[item.id];
            let btns = [];
            for (const [exerciseVariation, exerciseName] of Object.entries(v)) {
              item.exerciseVariation = parseInt(exerciseVariation);
              item.title = exerciseName;
              let data = {...item};
              let btn = (
                <View style={styles.buttonRow}>
                  {/* //Back facing Camera */}
                  <Button
                    key={`${index}_AIExercises_Back`}
                    index={index}
                    onPress={() => this.onPress(data)}
                    title={`${data.title}`}
                  />
                  <TextInput
                    keyboardType="number-pad"
                    maxLength={3}
                    numberOfLines={1}
                    defaultValue={`${data['score']}`}
                    style={styles.score_text}
                    onChangeText={(value) => {
                      data['score'] = parseInt(value);
                      console.log(data);
                    }}
                  />
                </View>
              );
              btns.push(btn);
            }
            return btns;
          }
        })}
      </View>
    );
  };

  createHeader = () => {
    return (
      <>
        <View style={styles.headerRow}>
          <Text style={styles.header}>Time (sec)</Text>
          <TextInput
            keyboardType="number-pad"
            maxLength={3}
            numberOfLines={1}
            defaultValue={`${this.state.countdownTime}`}
            style={styles.score_text}
            onChangeText={(value) => {
              this.setState({countdownTime: parseInt(value)});
            }}
          />
        </View>

        <View style={styles.headerRow}>
          <TouchableOpacity
            style={[
              styles.header,
              {
                flex: 3,
                backgroundColor: this.colorMap[this.state.cameraFacing],
              },
            ]}
            onPress={() => {
              this.setState({
                cameraFacing: this.cameraFacingValues[this.state.cameraFacing],
              });
            }}>
            <Text
              style={{
                fontWeight: 'bold',
              }}>{`${this.state.cameraFacing} CAMERA`}</Text>
          </TouchableOpacity>
          <Text style={[styles.header, {borderRadius: 10}]}>Score</Text>
        </View>
      </>
    );
  };

  render() {
    return (
      <SafeAreaView
        style={[styles.full, {backgroundColor: this.state.bg_color}]}>
        {this.createHeader()}
        <ScrollView style={[styles.full, styles.dark]}>
          {this.createSection('Person Exercises', AI_Exercises)}
          {this.createSection('Ball Exercises', Ball_AI_Exercises)}
          {this.createSection('BallPerson Exercises', BallPerson_AI_Exercises)}
          {this.createSection('Non-AI Exercises', nonAI_Exercises)}
        </ScrollView>
      </SafeAreaView>
    );
  }
}

const colors = ['#aabbcc', '#aaccae', '#ccaaaa', '#9486c2', '#cc93ca'];
const bg_colors = ['#000000', '#ffffff'];

function Button(props) {
  const {onPress, title, index} = props;
  return (
    <TouchableOpacity
      activeOpacity={0.8}
      onPress={onPress}
      style={[styles.button, {backgroundColor: colors[index % 5]}]}>
      <Text style={styles.text}>{title}</Text>
    </TouchableOpacity>
  );
}

const styles = StyleSheet.create({
  full: {
    flex: 1,
  },
  heading: {
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'center',
    marginBottom: 10,
    color: 'yellow',
  },
  buttonRow: {
    flexDirection: 'row',
    flex: 1,
    alignItems: 'center',
    justifyContent: 'space-between',
    height: '100%',
  },
  button: {
    borderRadius: 10,
    padding: 10,
    marginBottom: 10,
    justifyContent: 'center',
    alignItems: 'center',
    flex: 2,
    marginHorizontal: 3,
  },
  text: {
    fontSize: 16,
  },
  score_text: {
    flex: 1,
    marginLeft: 5,
    backgroundColor: 'white',
    minHeight: 30,
    textAlign: 'center',
    fontWeight: 'bold',
  },
  header: {
    justifyContent: 'center',
    alignItems: 'center',
    flex: 1,
    marginHorizontal: 3,
    backgroundColor: 'yellow',
    color: 'black',
    fontWeight: 'bold',
    padding: 10,
    textAlign: 'center',
    textAlignVertical: 'center',
  },
  headerRow: {
    flexDirection: 'row',
    paddingHorizontal: 20,
    marginVertical: 3,
  },
});
