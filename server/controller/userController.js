const User = require('../models/userModel');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const RegisterUser = async (req, res) => {
  const {name, email, password} = req.body;

  if (!name || !email || !password) {
    return res.status(422).json({error: 'Please fill all the fields properly'});
  }

  try {
    const userExist = await User.findOne({email: email});
    if (userExist) {
      return res.status(422).json({error: 'Email already exists'});
    } else {
      // Hash the password before saving to the database
      // const hashedPassword = await bcrypt.hash(password, 12);
      // const user = new User({name, email, password: hashedPassword});
      const user = new User({name, email, password});
      await user.save();
      res.status(201).json({message: 'User registered successfully'});
    }
  } catch (err) {
    console.error(err.message);
    res.status(500).send('Server Error');
  }
};

const LoginUser = async (req, res) => {
  try {
    const {email, password} = req.body;

    if (!email || !password) {
      return res.status(400).json({error: 'Please provide email and password'});
    }

    const user = await User.findOne({email});

    if (!user) {
      return res.status(400).json({error: 'Invalid email credentials'});
    }

    const isMatch = await bcrypt.compare(password, user.password);

    if (!isMatch) {
      return res.status(400).json({error: 'Invalid password credentials'});
    }

    const token = jwt.sign({_id: user._id}, process.env.JWT_SECRET, {
      expiresIn: '1d',
    });

    // Set cookie with the token
    res.cookie('token', token, {
      expiresIn: new Date(Date.now() + 86400000), // 1 day in milliseconds
      httpOnly: true,
    });

    res.json({message: 'Login successful'});
  } catch (error) {
    console.error(error.message);
    res.status(500).send('Server Error');
  }
};

module.exports = {LoginUser, RegisterUser};
