export const scoreTypes = {
  COUNTDOWN: 'COUNTDOWN',
  INFINITE: 'INFINITE',
  FAILURE: 'FAILURE',
  NREPETITIONS: 'NREPETITIONS',
  TOTALREPITITIONS: 'TOTALREPITITIONS',
  QUESTION: 'QUESTION',
  HIGHSCORE: 'HIGHSCORE',
  NONAI: 'NON_AI',
  NSECONDS: 'NSECONDS',
  TOTAL: 'TOTAL',
  TIMERSCORE: 'TIMERSCORE',
  MAXSECONDS: 'MAXSECONDS',
};

export const ExerciseVariations = {
  0: {
    0: 'High Knees',
  },

  1: {
    0: 'Juggling, Any',
    1: 'J, Below Knee',
    2: 'J, Above Knee',
    3: 'J, Left Foot',
    4: 'J, Right Foot',
    5: 'J, Alternate',
    6: 'J, BK, A',
    7: 'J, AK, A',
  },

  2: {
    0: 'Jumping Jacks',
    1: 'Inverse JJ',
  },

  3: {
    0: 'Jumping Squats',
  },

  4: {
    0: 'V-Cuts, Normal',
    1: 'V-Cuts, Inverse',
    2: 'V-Cuts, Left',
    3: 'V-Cuts, Right',
    4: 'V-Cuts, Any',
  },

  5: {
    0: 'Squats',
  },

  6: {
    0: 'Deadlifts',
  },

  7: {
    0: 'Ball Bounces, No Cones',
    1: 'Ball Bounces, Cones',
  },

  8: {
    0: 'Wallsits',
  },

  9: {
    0: 'Situps',
  },

  10: {
    0: 'Pushups',
    1: 'Paused Pushups',
  },

  11: {
    0: 'FrameGame',
  },

  12: {
    0: 'Knee Pushups',
  },

  13: {
    0: 'Dribbling',
    1: 'D, Cones',
    2: 'D, Questions',
  },

  14: {
    0: 'Ladder Run',
  },

  15: {
    0: 'Planks',
  },

  16: {
    0: 'Side Planks Left',
    1: 'Side Planks Right',
  },

  17: {
    0: 'Leg Raise',
  },

  18: {
    0: 'Growing Grass',
  },

  19: {
    0: 'Side Leg Raise Left',
    1: 'Side Leg Raise Right'
  },

  20: {
    0: 'Hurdle Jumps',
  },
  
  21: {
    0: 'Hop hop',
  },

  22: {
    0: 'Person',
  },

  23: {
    0: 'Ball Person',
  },

  24: {
    0: 'TouchPoints Hands',
    1: 'TouchPoints Legs',
  },

  25: {
    0: 'Order Games',
  },

  26: {
    0: 'Person Pose',
  },
  
  28: {
    0: 'Burpees',
  },
 
  29: {
    0: 'SidePlank Dips Left',
    1: 'SidePlank Dips Right',
  },

  30: {
    0: 'Skater Jumps',
  },

  31: {
    0: 'Ball Pushups Closed Grip',
    1: 'Ball Pushups Rolling',
  },

  32: {
    0: 'Scissors-L',
    1: 'Scissors-R',
    2: 'Scissors-A',
  },

  33: {
    0: 'Step Over',
  },

  34: {
    0: 'Triangles Left',
    1: 'Triangles Right',
  },

  35: {
    0: 'Sole Rolls',
  },

  36: {
    0: 'Knee To Chest',
  },

  42: {
    0: 'Bulgarian Split Squats Left',
    1: 'Bulgarian Split Squats Right',
  },

  43: {
    0: 'Plank Commandos',
  },

  45: {
    0: 'Bridge'
  },
  
  47: {
    0: 'Lunges',
    1: 'Lateral Lunges',
    2: 'Curtsy Lunges',
  },

  48: {
    0: 'Inside Push Pull',
  },

  49: {
    0: 'In Out Any Foot',
    1: 'In Out Left Foot',
    2: 'In Out Right Foot',
  },

  50: {
    0: 'Push Pull Laces',
  },

  53: {
    0: 'Single leg Deadlift',
  },

  54: {
    0: 'Broad Jumps',
  },

  55: {
    0: 'Tricep Dips',
  },

  27: {
    0: 'Roll Stops',
    1: 'Roll Stops With Cones'
  },
 
};

export const AI_Exercises = [
  {
    countDownMiliSeconds: 30000,
    description: 'High Knee\ntill ' + 10,
    scoreType: scoreTypes.NREPETITIONS,
    exerciseVariation: 0,
    id: 0,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'High Knees',
    useQuestions: true,
    questionMode: 'PICTURE',
    questionCount: 5,
    answerCount: 4,
    questionPath: 'pictureQuestions.json',
  },
  {
    countDownMiliSeconds: 30000,
    description: 'Jumping Jacks\n ' + 30 + ' seconds\n',
    scoreType: scoreTypes.NREPETITIONS,
    exerciseVariation: 0,
    id: 2,
    score: 20,
    selectedCameraFacing: 'FRONT',
    title: 'Jumping Jacks',
    useQuestions: false,
  },
  {
    countDownMiliSeconds: 30000,
    description: 'Perform a jump and then a squat, sequentially.',
    scoreType: scoreTypes.COUNTDOWN,
    exerciseVariation: 0,
    id: 3,
    score: 50,
    selectedCameraFacing: 'FRONT',
    title: 'Jumping Squats',
    useQuestions: false,
  },
  {
    countDownMiliSeconds: 30000,
    description: 'Squats\ntill ' + 25 + '\n',
    scoreType: scoreTypes.NREPETITIONS,
    id: 5,
    score: 25,
    selectedCameraFacing: 'FRONT',
    title: 'Squats',
    useQuestions: false,
  },
  {
    countDownMiliSeconds: 30000,
    description:
      'Perform proper deadlifts.. One of the ultimate compound exercises.',
    scoreType: scoreTypes.COUNTDOWN,
    id: 6,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'Deadlift',
    useQuestions: false,
  },
  {
    nSeconds: 30000,
    description: 'Wallsit\nFor ' + 30 + 'seconds\n',
    scoreType: scoreTypes.NSECONDS,
    id: 8,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'Wall Sit',
    useQuestions: false,
  },
  {
    countDownMiliSeconds: 30000,
    description: 'Sit Ups\n HIGHSCORE ' + 25 + '\n',
    scoreType: scoreTypes.NREPETITIONS,
    id: 9,
    score: 25,
    selectedCameraFacing: 'FRONT',
    title: 'Sit Ups',
    useQuestions: false,
  },
  {
    countDownMiliSeconds: 30000,
    description: 'Push Ups\n HIGHSCORE ' + 5 + '\n',
    scoreType: scoreTypes.NREPETITIONS,
    exerciseVariation: 0,
    id: 10,
    score: 5,
    selectedCameraFacing: 'FRONT',
    title: 'Push Ups',
    useQuestions: false,
  },
  
  {
    countDownMiliSeconds: 30000,
    description: 'Jump into shown frames according to its shape, the faster, the better.' + 20,
    scoreType: scoreTypes.NREPETITIONS,
    id: 11,
    score: 20,
    selectedCameraFacing: 'FRONT',
    title: 'FrameGame',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Knee Pushup\nTill ' + 20 + ' reps\n',
    scoreType: scoreTypes.NREPETITIONS,
    id: 12,
    score: 15,
    selectedCameraFacing: 'FRONT',
    title: 'Knee Pushup',
    useQuestions: false,
  },
  
  {
    countDownMiliSeconds: 30000,
    description: 'Ladder Run till ' + 15,
    scoreType: scoreTypes.NREPETITIONS,
    id: 14,
    score: 15,
    selectedCameraFacing: 'FRONT',
    title: 'Ladder Run',
    useQuestions: false,
  },

  {
    nSeconds: 30000,
    description: 'Planks\nFor ' + 30 + ' seconds\n',
    scoreType: scoreTypes.NSECONDS,
    id: 15,
    score: 30,
    selectedCameraFacing: 'FRONT',
    title: 'Planks',
    useQuestions: false,
  },
  {
    nSeconds: 30000,
    description: 'Side Plank\nHIGHSCORE ' + 30 + ' seconds\n',
    scoreType: scoreTypes.NSECONDS,
    id: 16,
    score: 20,
    selectedCameraFacing: 'FRONT',
    exerciseVariation: 1,
    title: 'Side Plank',
    useQuestions: false,
  },
  {
    countDownMiliSeconds: 30000,
    description: 'LegRaise\n HIGHSCORE ' + 20 + '\n',
    scoreType: scoreTypes.NREPETITIONS,
    id: 17,
    score: 20,
    selectedCameraFacing: 'FRONT',
    title: 'Leg Raise',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: "Survival Game\nJump on the patches off grass to ensure they don't grow above you",
    scoreType: scoreTypes.MAXSECONDS,
    id: 18,
    score: 15,
    selectedCameraFacing: 'FRONT',
    title: 'Growing Grass',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Side Leg Raise till ' + 15,
    scoreType: scoreTypes.NREPETITIONS,
    id: 19,
    score: 15,
    selectedCameraFacing: 'FRONT',
    title: 'Side Leg Raise',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Hurdle Jumps till ' + 15,
    scoreType: scoreTypes.NREPETITIONS,
    id: 20,
    score: 15,
    selectedCameraFacing: 'FRONT',
    title: 'Hurdle Jump',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Hop hop till ' + 15,
    scoreType: scoreTypes.NREPETITIONS,
    id: 21,
    score: 15,
    selectedCameraFacing: 'FRONT',
    title: 'Hop Hop',
    useQuestions: false,
  },
  {
    countDownMiliSeconds: 30000,
    description: 'Touch the Touchpoints as fast as you can ',
    scoreType: scoreTypes.COUNTDOWN,
    exerciseVariation: 1,
    id: 24,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'Touchpoints',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Select all the points in the right order as fast as you can',
    scoreType: scoreTypes.COUNTDOWN,
    id: 25,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'Order Games',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Person Pose ' + 10,
    scoreType: scoreTypes.NREPETITIONS,
    id: 26,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'Person Pose',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Do Burpees',
    scoreType: scoreTypes.COUNTDOWN,
    id: 28,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'Burpees',
    useQuestions: false,
  },
  
  {
    countDownMiliSeconds: 30000,
    description: 'SidePlank Dips ' + 15,
    scoreType: scoreTypes.NREPETITIONS,
    id: 29,
    score: 15,
    exerciseVariation: 0,
    selectedCameraFacing: 'FRONT',
    title: 'SidePlank Dips',
    useQuestions: false,
  },
 
  {
    countDownMiliSeconds: 30000,
    description: 'Jump from left to right, landing on one foot',
    scoreType: scoreTypes.NREPETITIONS,
    id: 30,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'Skater Jumps',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Knee to chest till ' + 20,
    scoreType: scoreTypes.NREPETITIONS,
    id: 36,
    score: 20,
    selectedCameraFacing: 'FRONT',
    title: 'Knee To Chest',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Bulgarian Split Squats ' + 20,
    scoreType: scoreTypes.NREPETITIONS,
    id: 42,
    score: 20,
    selectedCameraFacing: 'FRONT',
    title: 'Bulgarian Split Squats',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Plank Commanods ' + 20,
    scoreType: scoreTypes.NREPETITIONS,
    id: 43,
    score: 20,
    selectedCameraFacing: 'FRONT',
    title: 'Plank Commandos',
    useQuestions: false,
  },

  {
    nSeconds: 30000,
    description: 'Bridge\nFor ' + 30 + ' seconds\n',
    scoreType: scoreTypes.NSECONDS,
    id: 45,
    score: 30,
    selectedCameraFacing: 'FRONT',
    title: 'Bridge',
    useQuestions: false,
  },
  
  {
    countDownMiliSeconds: 30000,
    description: 'Do some lunges',
    scoreType: scoreTypes.NREPETITIONS,
    exerciseVariation: 1,
    id: 47,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'Lunges',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Single leg Deadlift ' + 15,
    scoreType: scoreTypes.NREPETITIONS,
    id: 53,
    score: 15,
    selectedCameraFacing: 'FRONT',
    title: 'Single leg Deadlift',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Jump from red region and land in yellow region',
    scoreType: scoreTypes.NREPETITIONS,
    id: 54,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'Broad Jumps',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Tricep Dips ' + 20,
    scoreType: scoreTypes.NREPETITIONS,
    id: 55,
    score: 20,
    selectedCameraFacing: 'FRONT',
    title: 'Tricep Dips',
    useQuestions: false,
  },

];

export const BallPerson_AI_Exercises = [
  {
    countDownMiliSeconds: 30000,
    description: 'Juggling\n HIGHSCORE ' + 20,
    scoreType: scoreTypes.HIGHSCORE,
    exerciseVariation: 0,
    id: 1,
    score: 20,
    selectedCameraFacing: 'FRONT',
    title: 'Juggling',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Do VCuts',
    scoreType: scoreTypes.NREPETITIONS,
    exerciseVariation: 1,
    id: 4,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'VCuts',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Bounce the balls between your legs as fast as possible',
    scoreType: scoreTypes.NREPETITIONS,
    exerciseVariation: 1,
    id: 7,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'Ball Bounces',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Ball Pushups',
    scoreType: scoreTypes.NREPETITIONS,
    id: 31,
    score: 50,
    selectedCameraFacing: 'FRONT',
    title: 'Ball Pushups',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Do Scissors',
    scoreType: scoreTypes.NREPETITIONS,
    exerciseVariation: 2,
    id: 32,
    score: 10,
    selectedCameraFacing: 'FRONT',
    title: 'Scissors',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Do Step Over',
    scoreType: scoreTypes.NREPETITIONS,
    id: 33,
    score: 50,
    selectedCameraFacing: 'FRONT',
    title: 'Step Over',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Triangles',
    scoreType: scoreTypes.NREPETITIONS,
    exerciseVariation: 0,
    id: 34,
    score: 20,
    selectedCameraFacing: 'FRONT',
    title: 'Triangles',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Sole Rolls',
    scoreType: scoreTypes.NREPETITIONS,
    id: 35,
    score: 50,
    selectedCameraFacing: 'FRONT',
    title: 'Sole Rolls',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Inside Push Pull',
    scoreType: scoreTypes.NREPETITIONS,
    id: 48,
    score: 50,
    selectedCameraFacing: 'FRONT',
    title: 'Inside Push Pull',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'In Out',
    scoreType: scoreTypes.NREPETITIONS,
    id: 49,
    score: 50,
    exerciseVariation: 0,
    selectedCameraFacing: 'FRONT',
    title: 'In Out',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Push Pull Laces',
    scoreType: scoreTypes.NREPETITIONS,
    id: 50,
    score: 50,
    selectedCameraFacing: 'FRONT',
    title: 'Push Pull Laces',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 30000,
    description: 'Roll Stops',
    scoreType: scoreTypes.NREPETITIONS,
    id: 27,
    score: 10,
    selectedCameraFacing: 'FRONT',
    exerciseVariation: 0,
    title: 'Roll Stops',
    useQuestions: false,
  },

];

export const Ball_AI_Exercises = [
  {
    countDownMiliSeconds: 30000,
    description: 'Dribble the ball in front of the camera',
    scoreType: scoreTypes.QUESTION,
    exerciseVariation: 1,
    id: 13,
    score: 50,
    selectedCameraFacing: 'FRONT',
    title: 'Dribbling',
    useQuestions: true,
    questionMode: 'PICTURE',
    questionCount: 10,
    answerCount: 2,
    questionPath: 'pictureQuestions.json',
  },
];

export const nonAI_Exercises = [
  {
    countDownMiliSeconds: 0,
    description: 'Test\n Non AI\n',
    scoreType: scoreTypes.NONAI,
    id: 22,
    score: 0,
    selectedCameraFacing: 'FRONT',
    title: 'Person',
    useQuestions: false,
  },

  {
    countDownMiliSeconds: 1000,
    description: 'Test\n Non AI\n',
    scoreType: scoreTypes.NONAI,
    id: 23,
    score: 100,
    selectedCameraFacing: 'FRONT',
    title: 'Ball Person',
    useQuestions: false,
  },
];
