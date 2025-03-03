const express = require('express');

// controller functions
const {LoginUser, RegisterUser} = require('../controller/userController');
const {addExercise} = require('../controller/exerciseController');
const {addUserExercise} = require('../controller/userExerciseController');
// const { loginUser, signupUser } = require("../controllers/userController");

const router = express.Router();

// login route
router.post('/login', LoginUser);

// signup route
router.post('/register', RegisterUser);

// add exercise route
router.post('/add-exercise', addExercise);

// add user exercise route
router.post('/add-user-exercise', addUserExercise);

module.exports = router;
