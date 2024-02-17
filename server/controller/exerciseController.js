const Exercise = require('../models/exerciseModel');
const UserExercise = require('../models/userExerciseModel');

async function addExercise(name, description) {
    const exercise = new Exercise({
        name,
        description,
    });
    await exercise.save();
}

module.exports = {addExercise};