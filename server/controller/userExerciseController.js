const UserExercise = require('../models/userExerciseModel');
async function addUserExercise(userId, exerciseId, score) {
    const userExercise = new UserExercise({
      userId,
      exerciseId,
      score,
    });
    await userExercise.save();
}
  
module.exports = {addUserExercise};