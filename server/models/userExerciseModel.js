const userExerciseSchema = new mongoose.Schema({
    userId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'User',
      required: true,
    },
    exerciseId: {
      type: mongoose.Schema.Types.ObjectId,
      ref: 'Exercise',
      required: true,
    },
    score: {
      type: Number,
      required: true,
    },
  });
  
const UserExercise = mongoose.model('UserExercise', userExerciseSchema);
  
module.exports = UserExercise;